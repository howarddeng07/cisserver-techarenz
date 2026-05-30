package edu.cis.Model;
import java.util.ArrayList;
public class Menu {
    private ArrayList<MenuItem> eatriumItems;
    private String adminID;

    public Menu()
    {
        this.eatriumItems = new ArrayList<>();
        this.adminID = "";
    }

    public ArrayList<MenuItem> getEatriumItems() { return eatriumItems; }
    public void setEatriumItems(ArrayList<MenuItem> eatriumItems) { this.eatriumItems = eatriumItems; }
    public String getAdminID() { return adminID; }
    public void setAdminID(String adminID) { this.adminID = adminID; }

    public String toString()
    {
        return "Menu{eatriumItems=" + eatriumItems.toString() + ", adminID='" + adminID + "'}";
    }
}
