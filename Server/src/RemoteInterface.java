/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author zmolo_000
 */
public interface RemoteInterface extends Remote {
     public LinkedList<InventoryItem> listInventory(String table) throws RemoteException;

     public void addItem(String table, String productID, String description,
     int quantity, float perUnitCost) throws RemoteException;

     public void deleteItem(String table, String productID) throws RemoteException;

     public void decrementItem(String table, String productID) throws RemoteException;

     public String login(String login, String password) throws RemoteException;
     
     public void submitOrder(String firstName, String lastName, String customerAddress, String phoneNumber,
            float fCost, List<InventoryItem> items) throws RemoteException;
}
