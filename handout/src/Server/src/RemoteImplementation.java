

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zmolo_000
 */
public class RemoteImplementation extends UnicastRemoteObject implements RemoteInterface {
   public RemoteImplementation() throws RemoteException {
    }


    public void writeToLogFile(String userName, String timeStamp, int flag) {
       try{
        FileWriter writer = new FileWriter("logFile.txt", true);
        if(flag == 1){
            writer.append(userName + " logged in at " + timeStamp);
            writer.write(13);
            writer.write(10);
        }
        else {
            writer.append(userName + " logged out at " + timeStamp );
            writer.write(13);
            writer.write(10);
        }
        writer.close();
       }
       catch(IOException exception){
       }
       return;
    }


    @Override
    public LinkedList<InventoryItem> listInventory(String table) throws RemoteException {
        LinkedList<InventoryItem> result = new LinkedList<>();
        try {
            result = DBHelper.listItems(table);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public void addItem(String table, String productID, String description,
            int quantity, float perUnitCost) throws RemoteException {
        try {
            DBHelper.addItem(table, productID, description, quantity, perUnitCost);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteItem(String table, String productID) throws RemoteException {
        try {
            DBHelper.deleteItem(table, productID);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void decrementItem(String table, String productID) throws RemoteException {
        try {
            int productCount = DBHelper.getItemInfo(table, productID).getQuantity();
            if (productCount <= 1){
                DBHelper.deleteItem(table, productID);
            }
            else {
                DBHelper.decrementItem(table, productID);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String login(String login, String password) throws RemoteException {
        String result = "badAuthorization";
        try {
            result = DBHelper.login(login, password);
            String timeStamp = new SimpleDateFormat("yyyy MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
            if(result != null )
                writeToLogFile(login, timeStamp,1);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public void logout(String username) throws RemoteException{
       String logoutTimestamp = new SimpleDateFormat("yyyy MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
       writeToLogFile(username, logoutTimestamp,0);
       return;
    }
    
    @Override
    public void submitOrder(String firstName, String lastName, String customerAddress,
        String phoneNumber, float fCost, List<InventoryItem> items) throws RemoteException {
        try {
            DBHelper.submitOrder(firstName, lastName, customerAddress, phoneNumber, fCost, items);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public LinkedList<OrderInfo> getPendingOrders() throws RemoteException {
        LinkedList<OrderInfo> result = new LinkedList<OrderInfo>();
        try {
            result = DBHelper.getPendingOrders();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public LinkedList<OrderInfo> getShippedOrders() throws RemoteException {
        LinkedList<OrderInfo> result = new LinkedList<OrderInfo>();
        try {
            result = DBHelper.getShippedOrders();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public OrderInfo getOrderInfo(int orderID) throws RemoteException {
        OrderInfo result = new OrderInfo();
        try {
            result = DBHelper.getOrderInfo(orderID);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public void shipOrder(int orderId) throws RemoteException {
        try {
            DBHelper.shipOrder(orderId);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteUser(String login) throws RemoteException {
        try {
            DBHelper.deleteUser(login);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void addUser(String login, String password, String role) throws RemoteException {
        try {
            DBHelper.addUser(login, password, role);
        } catch (SQLException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public LinkedList<UserInfo> getListUsers() throws RemoteException {
        LinkedList<UserInfo> listUsers = new LinkedList<UserInfo>();
        try {
            listUsers = DBHelper.getListUsers();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listUsers;
    }
}
