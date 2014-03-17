// Contains the implementation of a LSP client.

package lsp

import (
	"container/list"
	"encoding/json"
	"errors"
	"fmt"
	"github.com/cmu440/lspnet"
	"time"
	// "message"
)

type Common struct {
	addr   *lspnet.UDPAddr
	params *Params
	connID int

	readWin          *list.List
	readWinStartNum  int
	writeWinStartNum int
	writeWin         *list.List
	ackList          *list.List

	connIDSynChan            chan int
	epochCounterSynChan      chan int
	isConnectedSynChan       chan bool
	isReceivedMessageSynChan chan bool

	canReadMsgNumSynChan   chan int
	needWriteMsgNumSynChan chan int

	isAppClosedSynChan chan bool
	isNetLostSynChan   chan bool

	epochSignalChan chan *Message
	closeSignalChan chan *Message
}

func NewCommon(addr *lspnet.UDPAddr, params *Params) *Common {
	comm := &Common{
		addr:     addr,
		params:   params,
		readWin:  list.New(),
		writeWin: list.New(),
		ackList:  list.New(),

		readWinStartNum:  1,
		writeWinStartNum: 1,
		connID:           0,

		connIDSynChan:            make(chan int, 1),
		epochCounterSynChan:      make(chan int, 1),
		isConnectedSynChan:       make(chan bool, 1),
		isReceivedMessageSynChan: make(chan bool, 1),

		canReadMsgNumSynChan:   make(chan int, 1),
		needWriteMsgNumSynChan: make(chan int, 1),

		isAppClosedSynChan: make(chan bool, 1),
		isNetLostSynChan:   make(chan bool, 1),

		epochSignalChan: make(chan *Message, 1),
		closeSignalChan: make(chan *Message, 1),
	}
	for i := 0; i <= params.WindowSize-1; i++ {
		comm.writeWin.PushBack(nil)
		comm.readWin.PushBack(nil)
	}
	// init lock
	comm.epochCounterSynChan <- 0
	comm.isConnectedSynChan <- false
	comm.isReceivedMessageSynChan <- false
	comm.connIDSynChan <- 0
	comm.canReadMsgNumSynChan <- 0
	comm.needWriteMsgNumSynChan <- 0
	comm.isAppClosedSynChan <- false
	comm.isNetLostSynChan <- false
	return comm
}
func (comm *Common) setConnID(connID int) {
	comm.connID = connID
	<-comm.connIDSynChan
	comm.connIDSynChan <- connID
}

func (comm *Common) addMsgToACKList(msg *Message) {
	if comm.ackList.Len() >= comm.params.WindowSize {
		comm.ackList.Remove(comm.ackList.Front())
	}
	comm.ackList.PushBack(msg)
}

func (comm *Common) addMsgToReadSlideWindow(msg *Message) {
	index := comm.readWinStartNum
	for e := comm.readWin.Front(); e != nil; e = e.Next() {
		if e.Value == nil && index == msg.SeqNum {
			e.Value = msg
			break
		}
		index++
	}
	return
}

func (comm *Common) slideReadSlideWindow() *list.List {
	completedMsgList := list.New()
	for {
		e := comm.readWin.Front()
		if e != nil && e.Value != nil {
			completedMsgList.PushBack(e.Value)
			comm.readWin.Remove(e)
			comm.readWin.PushBack(nil)
		} else {
			break
		}
	}
	return completedMsgList
}

func (comm *Common) addACKMsgToWriteSlideWindow(msg *Message) bool {
	for e := comm.writeWin.Front(); e != nil; e = e.Next() {
		if e.Value != nil && e.Value.(*Message).SeqNum == msg.SeqNum {
			e.Value = nil
			return true
		}
	}
	return false
}

func (comm *Common) addDataMsgToWriteSlideWindow(msg *Message) bool {
	for e := comm.writeWin.Back(); e != nil; e = e.Prev() {
		if e.Value == nil && (e.Prev() == nil || e.Prev().Value != nil) {
			e.Value = msg
			return true
		} else if e.Value != nil {
			break
		}
	}
	return false
}

func (comm *Common) slideWriteSlideWindow() int {
	slideNum := 0
	for {
		e := comm.writeWin.Front()
		if e != nil && e.Value == nil {
			comm.writeWin.Remove(e)
			slideNum++
		} else {
			break
		}
	}
	// add element
	for i := 0; i <= slideNum-1; i++ {
		comm.writeWin.PushBack(nil)
	}
	return slideNum
}

type client struct {
	// TODO: implement this!
	comm    *Common
	connUDP *lspnet.UDPConn

	appCloseSynChan     chan bool
	netLostCloseSynChan chan bool

	// data chan
	readBufToAppChan   chan *Message
	netToReadWinChan   chan *Message
	epochToReadWinChan chan *Message

	// appOrNetToWriteBufChan chan *Message
	appToWriteBufChan   chan *Message
	netToWriteBufChan   chan *Message
	epochToWriteBufChan chan *Message

	connChan chan *Message

	//Req chan
	// epochSignalChan chan *Message
	// // writeWinSignalChan       chan SingnalType
	// // ackListSignalChan        chan SingnalType
	// closeSignalChan chan *Message
	//
	// appCloseChan     chan AppCloseStageType
	// netLostCloseChan chan NetLostCloseStageType
}

// NewClient creates, initiates, and returns a new client. This function
// should return after a connection with the server has been established
// (i.e., the client has received an Ack message from the server in response
// to its connection request), and should return a non-nil error if a
// connection could not be made (i.e., if after K epochs, the client still
// hasn't received an Ack message from the server in response to its K
// connection requests).
//
// hostport is a colon-separated string identifying the server's host address
// and port number (i.e., "localhost:9999").
func NewClient(hostport string, params *Params) (Client, error) {
	if params == nil {
		params = &Params{5, 2000, 1}
	}

	addr, err := lspnet.ResolveUDPAddr("udp", hostport)
	if err != nil {
		return nil, err
	}

	conn, err := lspnet.DialUDP("udp", nil, addr)
	if err != nil {
		return nil, err
	}

	c := &client{
		comm:    NewCommon(addr, params),
		connUDP: conn,

		readBufToAppChan:   make(chan *Message),
		netToReadWinChan:   make(chan *Message),
		epochToReadWinChan: make(chan *Message),

		appToWriteBufChan:   make(chan *Message),
		netToWriteBufChan:   make(chan *Message),
		epochToWriteBufChan: make(chan *Message),

		connChan: make(chan *Message),

		appCloseSynChan:     make(chan bool, 1),
		netLostCloseSynChan: make(chan bool, 1),
	}

	// go c.manageReceivedMsg()
	go c.handleEpoch()
	go c.readFromServer()
	go c.handleReadSlideWindow()
	go c.handleWriteSlideWindow()

	//init lock
	// c.connIDSynChan <- 0

	c.appCloseSynChan <- false
	c.netLostCloseSynChan <- false

	//start connect
	c.writeToServer(c.generateMsg(MsgConnect, 0, nil))
	connMsg := <-c.connChan
	if connMsg.Type == MsgAck && connMsg.SeqNum == 0 {
		SetValueToIntChan(c.comm.connIDSynChan, connMsg.ConnID)
		SetValueToBoolChan(c.comm.isConnectedSynChan, true)
		c.comm.connID = connMsg.ConnID
		fmt.Println("Client:", c.comm.connID, "Start")
		return c, nil
	}
	return nil, errors.New("Connection failed")
}

func (c *client) netLostClose() {
	// SetValueToBoolChan(c.comm.isNetLostSynChan, true)
	// isConnected := GetValueFromBoolChan(c.comm.isConnectedSynChan)
	// if !isConnected {
	// 	c.connChan <- c.generateMsg(MsgNetLost, 0, nil) //connection failed
	// }
	// c.comm.epochSignalChan <- c.generateMsg(MsgNetLost, 0, nil)
	// c.appOrNetToReadWinChan <- c.generateMsg(MsgNetLost, 0, nil)
	// c.appOrNetToWriteBufChan <- c.generateMsg(MsgNetLost, 0, nil)
	// c.connUDP.Close()
}

func (c *client) appClose() {
	// SetValueToBoolChan(c.comm.isAppClosedSynChan, true)

	// c.appOrNetToWriteBufChan <- c.generateMsg(MsgAppClose, 0, nil)
	// <-c.comm.closeSignalChan // wait for write finish
	// c.comm.epochSignalChan <- c.generateMsg(MsgAppClose, 0, nil)
	// c.appOrNetToReadWinChan <- c.generateMsg(MsgAppClose, 0, nil)
	// c.connUDP.Close()

}

func GetValueFromIntChan(in chan int) int {
	value := <-in
	in <- value
	return value
}

func SetValueToIntChan(in chan int, value int) {
	<-in
	in <- value
	return
}

func GetValueFromBoolChan(in chan bool) bool {
	value := <-in
	in <- value
	return value
}

func SetValueToBoolChan(in chan bool, value bool) {
	<-in
	in <- value
	return
}
func UpdateValueToIntChan(in chan int, delta int) {
	value := <-in
	in <- value + delta
	return
}

func (c *client) handleEpoch() {
	// send epoch signal when epoch event occurs
	for {
		timeoutChan := time.After(time.Duration(c.comm.params.EpochMillis) * time.Millisecond)
		select {
		case <-timeoutChan:
			// syn
			isConnected := GetValueFromBoolChan(c.comm.isConnectedSynChan)
			isReceivedMessage := GetValueFromBoolChan(c.comm.isReceivedMessageSynChan)
			epochCounter := GetValueFromIntChan(c.comm.epochCounterSynChan)
			SetValueToIntChan(c.comm.epochCounterSynChan, epochCounter+1)

			// fmt.Println(epochCounter)
			if epochCounter >= c.comm.params.EpochLimit {
				// fmt.Print("aaaaaaaaaaaaaaaaaa")
				c.netLostClose()
			}

			// fmt.Println("isConnected", isConnected)
			if !isConnected {
				c.writeToServer(c.generateMsg(MsgConnect, 0, nil))
			} else {
				c.epochToReadWinChan <- c.generateMsg(MsgEpoch, 0, nil)
				c.epochToWriteBufChan <- c.generateMsg(MsgEpoch, 0, nil)
			}
			if !isReceivedMessage {
				// fmt.Println("Epoch No Massage Received")
				c.writeToServer(c.generateMsg(MsgAck, 0, nil))
			}

		case msg := <-c.comm.epochSignalChan:
			if msg.Type == MsgAppClose || msg.Type == MsgNetLost {
				return
			}
		}
	}
}

func (c *client) handleWriteSlideWindow() {
	// defer close(out)
	// This list will store all values received from 'in'.
	// All values should eventually be sent back through 'out',
	// even if the 'in' channel is suddenly closed.
	// isAppClose := false
	buffer := list.New()

	for {
		select {
		case msg, _ := <-c.appToWriteBufChan:
			msg.SeqNum = c.comm.writeWinStartNum
			c.comm.writeWinStartNum++
			buffer.PushBack(msg)
			// try to add to buffer
			c.getDataMsgFromWriteBuf(buffer)
		case msg, _ := <-c.netToWriteBufChan:
			if c.comm.addACKMsgToWriteSlideWindow(msg) {
				// UpdateValueToIntChan(c.comm.needWriteMsgNumSynChan, -1)
				c.comm.slideWriteSlideWindow()
				c.getDataMsgFromWriteBuf(buffer)
			}
		case <-c.epochToWriteBufChan:
			for e := c.comm.writeWin.Front(); e != nil; e = e.Next() {
				if e.Value != nil {
					c.writeToServer(e.Value.(*Message))
				}
			}

			// default:
			// 	c.getDataMsgFromWriteBuf(buffer)
			// 	if isAppClose && buffer.Len() == 0 && c.comm.writeWin.Len() == 0 {
			// 		// add data has been sent and all want to close, then return
			// 		c.comm.closeSignalChan <- c.generateMsg(MsgAppClose, 0, nil)
			// 		return
			// 	}
			// if msg.Type == MsgAck {
			// 	// add ack
			// 	// success wri
			// 	// printList("WriteWIn", c.comm.writeWin)
			// 	// if c.comm.addACKMsgToWriteSlideWindow(msg) {
			// 	// 	UpdateValueToIntChan(c.comm.needWriteMsgNumSynChan, -1)
			// 	// 	// success slide
			// 	// 	for e := buffer.Front(); e != nil; e = e.Next() {
			// 	// 		if !c.comm.addDataMsgToWriteSlideWindow(e.Value.(*Message)) {
			// 	// 			break
			// 	// 		}
			// 	// 	}
			// 	// }
			// 	if c.comm.addACKMsgToWriteSlideWindow(msg) {
			// 		// UpdateValueToIntChan(c.comm.needWriteMsgNumSynChan, -1)
			// 		c.comm.slideWriteSlideWindow()
			// 		c.getDataMsgFromWriteBuf(buffer)
			// 	}

			// } else if msg.Type == MsgData {
			// 	// fmt.Println("Write Buf" + msg.String())
			// 	// update need write
			// 	// UpdateValueToIntChan(c.comm.needWriteMsgNumSynChan, 1)
			// 	// add msg to buffer end
			// 	msg.SeqNum = c.comm.writeWinStartNum
			// 	c.comm.writeWinStartNum++
			// 	buffer.PushBack(msg)
			// 	// try to add to buffer
			// 	// c.getDataMsgFromWriteBuf(buffer)
			// 	c.getDataMsgFromWriteBuf(buffer)

			// // } else if msg.Type == MsgEpoch {
			// 	for e := c.comm.writeWin.Front(); e != nil; e = e.Next() {
			// 		if e.Value != nil {
			// 			c.writeToServer(e.Value.(*Message))
			// 		}
			// 	}
			// } else if msg.Type == MsgAppClose {
			// 	isAppClose = true
			// 	fmt.Println(isAppClose)
			// } else if msg.Type == MsgNetLost {
			// 	return
			// }
		}
	}
}

func (c *client) getDataMsgFromWriteBuf(buffer *list.List) {
	for buffer.Len() > 0 && c.comm.addDataMsgToWriteSlideWindow(buffer.Front().Value.(*Message)) {
		// fmt.Println("Read Write Win Default: Send ACK")
		c.writeToServer(buffer.Front().Value.(*Message))
		buffer.Remove(buffer.Front())
		// fmt.Println("Add to Write Win")
	}
}

func (c *client) handleReadSlideWindow() {
	// defer close(out)
	// This list will store all values received from 'in'.
	// All values should eventually be sent back through 'out',
	// even if the 'in' channel is suddenly closed.
	buffer := list.New()
	// isNetLost := false

	for {
		if buffer.Len() == 0 {
			select {

			case msg, _ := <-c.netToReadWinChan:
				c.comm.addMsgToReadSlideWindow(msg)
				msgList := c.comm.slideReadSlideWindow()
				c.comm.readWinStartNum += msgList.Len()
				buffer.PushBackList(msgList)
				// update can read num
				UpdateValueToIntChan(c.comm.canReadMsgNumSynChan, msgList.Len())
				// add to ack list and write back ack
				c.writeToServer(c.generateMsg(MsgAck, msg.SeqNum, nil))
				c.comm.addMsgToACKList(c.generateMsg(MsgAck, msg.SeqNum, nil))

			case <-c.epochToReadWinChan:
				for e := c.comm.ackList.Front(); e != nil; e = e.Next() {
					c.writeToServer(e.Value.(*Message))
				}
			}
		} else {
			select {

			case c.readBufToAppChan <- buffer.Front().Value.(*Message):
				buffer.Remove(buffer.Front())

			case msg, _ := <-c.netToReadWinChan:
				c.comm.addMsgToReadSlideWindow(msg)
				msgList := c.comm.slideReadSlideWindow()
				c.comm.readWinStartNum += msgList.Len()
				buffer.PushBackList(msgList)
				// update can read num
				UpdateValueToIntChan(c.comm.canReadMsgNumSynChan, msgList.Len())
				// add to ack list and write back ack
				c.writeToServer(c.generateMsg(MsgAck, msg.SeqNum, nil))
				c.comm.addMsgToACKList(c.generateMsg(MsgAck, msg.SeqNum, nil))

			case <-c.epochToReadWinChan:
				for e := c.comm.ackList.Front(); e != nil; e = e.Next() {
					c.writeToServer(e.Value.(*Message))
				}
			}
		}
	}
	// if isNetLost {
	// 	c.readBufToAppChan <- c.generateMsg(MsgNetLost, 0, nil)
	// }
	// if msg.Type == MsgData {
	// 	//add to read win
	// 	c.comm.addMsgToReadSlideWindow(msg)
	// 	msgList := c.comm.slideReadSlideWindow()
	// 	c.comm.readWinStartNum += msgList.Len()
	// 	buffer.PushBackList(msgList)
	// 	// update can read num
	// 	UpdateValueToIntChan(c.comm.canReadMsgNumSynChan, msgList.Len())
	// 	// add to ack list and write back ack
	// 	// fmt.Println("Read Win Receive Data: Send ACK")
	// 	c.writeToServer(c.generateMsg(MsgAck, msg.SeqNum, nil))
	// 	c.comm.addMsgToACKList(c.generateMsg(MsgAck, msg.SeqNum, nil))
	// } else if msg.Type == MsgEpoch {
	// 	// send ack to net
	// 	// fmt.Println("Epoch ACK:", c.comm.ackList.Len())
	// 	// printList("ACK List", c.comm.ackList)
	// 	for e := c.comm.ackList.Front(); e != nil; e = e.Next() {
	// 		// fmt.Println("WriteToNet:" + e.Value.(*Message).String())
	// 		// fmt.Println("Read Win Receive Epoch: Send ACK")
	// 		c.writeToServer(e.Value.(*Message))
	// 	}

	// } else if msg.Type == MsgAppClose {
	// 	return
	// } else if msg.Type == MsgNetLost {
	// 	isNetLost = true
	//
}

// Blocks until all values in the buffer have been sent through
// the 'out' channel.
func flush(buffer *list.List, out chan<- *Message) {
	for e := buffer.Front(); e != nil; e = e.Next() {
		out <- (e.Value).(*Message)
	}
}

func (c *client) readFromServer() {
	var buffer [2000]byte
	for {
		// fmt.Println(time.Now().String() + "ReceiveFromNet:Start")
		//read from net
		n, err := c.connUDP.Read(buffer[0:])
		if err != nil {
			return
		}
		var msg Message
		json.Unmarshal(buffer[0:n], &msg)
		// fmt.Println(time.Now().String() + "ReceiveFromNet:" + msg.String())
		// fmt.Println()

		//epochdelete
		//epoch counter zero
		SetValueToIntChan(c.comm.epochCounterSynChan, 0)

		if msg.Type == MsgAck {
			// fmt.Println("Enter Write Win" + msg.String())
			if msg.SeqNum == 0 {

				//syn isConnected
				//epochdelete
				isConnected := GetValueFromBoolChan(c.comm.isConnectedSynChan)
				// // if not connected, then send message to conn channel
				// // if do not judge, then this will block, since the
				// // the conn channel is only readed by New client once
				if !isConnected {
					c.connChan <- &msg
				}
			} else {
				// fmt.Println("ACK To Win" + msg.String())
				c.netToWriteBufChan <- &msg
				// fmt.Println(msg.String())
			}
		} else {
			// fmt.Println(msg.String())
			//syn isReceivedMessage
			// fmt.Println("Enter Read Win" + msg.String())
			SetValueToBoolChan(c.comm.isReceivedMessageSynChan, true)
			// add to read win
			c.netToReadWinChan <- &msg
		}
		// fmt.Println(time.Now().String() + "ReceiveFromNet:Done")
	}
}

func (c *client) writeToServer(msg *Message) {
	// fmt.Println("WriteToNet:" + msg.String())
	stream, _ := json.Marshal(msg)
	c.connUDP.Write(stream)
}

func (c *client) generateMsg(msgType MsgType, seqNum int, payload []byte) *Message {
	//syn conn id

	switch msgType {
	case MsgAck:
		connID := GetValueFromIntChan(c.comm.connIDSynChan)
		return NewAck(connID, seqNum)
	case MsgData:
		connID := GetValueFromIntChan(c.comm.connIDSynChan)
		return NewData(connID, seqNum, payload)
	case MsgConnect:
		return NewConnect()
	case MsgEpoch:
		return NewEpoch()
	case MsgAppClose:
		return NewAppClose()
	case MsgNetLost:
		return NewNetLost()
	case MsgAppRead:
		return NewAppRead()
	default:
		return NewConnect()
	}
}
func (c *client) ConnID() int {
	//syn conn id
	connID := GetValueFromIntChan(c.comm.connIDSynChan)

	return connID
}

func (c *client) Read() ([]byte, error) {
	// TODO: remove this line when you are ready to begin implementing this method.
	// select {} // Blocks indefinitely.
	appClose := GetValueFromBoolChan(c.comm.isAppClosedSynChan)
	if appClose {
		return nil, errors.New("Application have already closed")
	}

	msg := <-c.readBufToAppChan
	// fmt.Println("Read" + msg.String())
	if msg.Type == MsgNetLost {
		return nil, errors.New("Connection lost")
	} else if msg.Type == MsgData {
		return msg.Payload, nil
	}
	return nil, nil
}

func (c *client) Write(payload []byte) error {
	appClose := GetValueFromBoolChan(c.comm.isAppClosedSynChan)
	netLostClose := GetValueFromBoolChan(c.comm.isNetLostSynChan)

	if appClose || netLostClose {
		return errors.New("Connection lost or Application have already closed")
	} else {
		msg := c.generateMsg(MsgData, 0, payload)
		// fmt.Println("Write Func Start" + msg.String())
		c.appToWriteBufChan <- msg
		// fmt.Println("Write Func Done" + msg.String())
		return nil
	}
}

func (c *client) Close() error {
	appClose := GetValueFromBoolChan(c.comm.isAppClosedSynChan)
	netLostClose := GetValueFromBoolChan(c.comm.isNetLostSynChan)
	if appClose {
		return errors.New("Already Closed")
	} else if netLostClose {
		return errors.New("Connection lost")
	} else {
		c.appClose()
		return nil
	}
}

func printList(name string, list *list.List) {
	fmt.Println(name + ":start")
	for e := list.Front(); e != nil; e = e.Next() {
		fmt.Print(e.Value)
		fmt.Print(" ")
	}
	fmt.Println()
	fmt.Println(name + ":done")
	return
}
