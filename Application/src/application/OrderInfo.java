package application;

import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author zmolo_000
 */
public class OrderInfo {

    private String orderID;     // ID of the order
    private String orderTable;  // name of table with list of items
    private String firstName;   // first name
    private String lastName;    // last name
    private String phone;       // phone
    private String address;     // address
    private String orderDate;   // order date
    private boolean shipped;     // shipped or not
    private float totalCost;     // total cost
    private List<InventoryItem> items;   // order items

    /**
     * @return the orderTable
     */
    public String getOrderTable() {
        return orderTable;
    }

    /**
     * @param orderTable the orderTable to set
     */
    public void setOrderTable(String orderTable) {
        this.orderTable = orderTable;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the orderDate
     */
    public String getOrderDate() {
        return orderDate;
    }

    /**
     * @param orderDate the orderDate to set
     */
    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * @return the items
     */
    public List<InventoryItem> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(List<InventoryItem> items) {
        this.items = items;
    }

    /**
     * @return the shipped
     */
    public boolean isShipped() {
        return shipped;
    }

    /**
     * @param shipped the shipped to set
     */
    public void setShipped(boolean shipped) {
        this.shipped = shipped;
    }

    /**
     * @return the totalCost
     */
    public float getTotalCost() {
        return totalCost;
    }

    /**
     * @param totalCost the totalCost to set
     */
    public void setTotalCost(float totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * @return the orderID
     */
    public String getOrderID() {
        return orderID;
    }

    /**
     * @param orderID the orderID to set
     */
    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
}
