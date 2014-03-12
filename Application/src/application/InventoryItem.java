/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author zmolo_000
 */
public class InventoryItem {
    private String productID;
    private String description;
    private Float perUnitCost;
    private int quantity;

    /**
     * @return the productID
     */
    public String getProductID() {
        return productID;
    }

    /**
     * @param productID the productID to set
     */
    public void setProductID(String productID) {
        this.productID = productID;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the perUnitCost
     */
    public Float getPerUnitCost() {
        return perUnitCost;
    }

    /**
     * @param perUnitCost the perUnitCost to set
     */
    public void setPerUnitCost(Float perUnitCost) {
        this.perUnitCost = perUnitCost;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
