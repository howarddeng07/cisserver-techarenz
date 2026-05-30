package edu.cis.Model;
import java.util.ArrayList;


public class CISUser {
    private String userID;
    private String name;
    private String yearLevel;
    private ArrayList<Order> orders;
    private double money;

    public CISUser(String userID, String name, String yearLevel)
    {
        this.userID = userID;
        this.name = name;
        this.yearLevel = yearLevel;
        this.orders = new ArrayList<>();
        this.money = 50.0;
    }
    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getYearLevel() { return yearLevel; }
    public void setYearLevel(String yearLevel) { this.yearLevel = yearLevel; }
    public ArrayList<Order> getOrders() { return orders; }
    public void setOrders(ArrayList<Order> orders) { this.orders = orders; }
    public double getMoney() { return money; }
    public void setMoney(double money) { this.money = money; }
    public String toString()
    {
        String orderString = "";
        for (int i = 0; i < orders.size(); i++)
        {
            if (i > 0) orderString += ", ";
            orderString += orders.get(i).toString();
        }
        return "CISUser{userID='" + userID + "', name='" + name + "', yearLevel='" + yearLevel + "', orders= " + orderString + ", money=" + money + "}";
    }
}

