package edu.cis.Model;

public class Order {
    private String itemID;
    private String type;
    private String orderID;

    public Order(String itemID, String type, String orderID){
        this.itemID = itemID;
        this.type = type;
        this.orderID = orderID;
    }
    public String getItemID() { return itemID; }
    public String getType() { return type; }
    public String getOrderID() { return orderID; }

    public void setItemID(String itemID) { this.itemID = itemID; }
    public void setType(String type) { this.type = type; }
    public void setOrderID(String orderID) { this.orderID = orderID; }

    public String toString()
    {
        return "Order{itemID='" + itemID + "', type='" + type + "', orderID='" + orderID + "'}";
    }
}
