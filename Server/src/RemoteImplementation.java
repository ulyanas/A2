

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
   BufferedWriter writer = null;
   static int count =0;
   public RemoteImplementation() throws RemoteException {

    }
    
  
    public Writer writeToLogFile(String login, String timeStamp, int flag) {
        try {
             
               writer = new BufferedWriter(new FileWriter("logFile.txt", true));
               if(count == 0){
               writer.append("\n UserName   \t    Type      \t     TimeStamp ");
               writer.newLine();
               count++;
               }              
            } 
            catch (IOException ex) {}       
       try{
        if(flag == 1){
            writer.append("\n" +"  "+ login + "         logged in at         " + timeStamp);
            writer.newLine();
        }
        else {
            writer.append(login + "       logged out at         " + timeStamp );
            writer.newLine();
        }
                    
        writer.write("\n");
        writer.close();
       }
       catch(IOException ex){}
        return writer;
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
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            System.out.println(result);
            if(result != null )
                writeToLogFile(login,timeStamp,1);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }    
    
   /*
    public String logout( String logout) throws RemoteException{
        String res="logout";
        
        String logoutTimestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        writeToLogFile(logout, logoutTimestamp,0);
       return res;
    }
    */
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
