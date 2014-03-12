/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.util.LinkedList;

/**
 *
 * @author zmolo_000
 */
public interface RemoteInterface {

    public LinkedList<InventoryItem> listInventory(String table);

    public void addItem(String table, String productID, String description,
            int quantity, float perUnitCost);

    public void deleteItem(String table, String productID);

    public void decrementItem(String table, String productID);

    public void login(String login, String password);
}
