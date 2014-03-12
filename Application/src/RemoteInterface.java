/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

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
}
