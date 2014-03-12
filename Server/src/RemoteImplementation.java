

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zmolo_000
 */
public class RemoteImplementation extends UnicastRemoteObject implements RemoteInterface {

    public RemoteImplementation() throws RemoteException {

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
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(RemoteImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }    
}
