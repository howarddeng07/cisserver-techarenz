/*
 * File: CIServer.java
 * ------------------------------
 * When it is finished, this program will implement a basic
 * ecommerce network management server.  Remember to update this comment!
 */

package edu.cis.Controller;

import acm.program.*;
import edu.cis.Model.*;
import edu.cis.Utils.SimpleServer;

import java.util.ArrayList;

public class CIServer extends ConsoleProgram
        implements SimpleServerListener
{

    /* The internet port to listen to requests on */
    private static final int PORT = 8000;

    /* The server object. All you need to do is start it */
    private SimpleServer server = new SimpleServer(this, PORT);
    private ArrayList<CISUser> users = new ArrayList<>();
    private Menu menu = new Menu();

    /**
     * Starts the server running so that when a program sends
     * a request to this server, the method requestMade is
     * called.
     */
    public void run()
    {
        println("Starting server on port " + PORT);
        server.start();
    }

    /**
     * When a request is sent to this server, this method is
     * called. It must return a String.
     *
     * @param request a Request object built by SimpleServer from an
     *                incoming network request by the client
     */
    public String requestMade(Request request)
    {
        String cmd = request.getCommand();
        println(request.toString());


        if (cmd.equals(CISConstants.PING))
        {
            return "Hello, internet";
        }
        if (cmd.equals(CISConstants.CREATE_USER))
        {
            return createUser(request);
        }
        if (cmd.equals(CISConstants.ADD_MENU_ITEM))
        {
            return addMenuItem(request);
        }
        if (cmd.equals(CISConstants.PLACE_ORDER))
        {
            return placeOrder(request);
        }
        if (cmd.equals(CISConstants.DELETE_ORDER))
        {
            return deleteOrder(request);
        }
        if (cmd.equals(CISConstants.GET_USER))
        {
            return getUser(request);
        }
        if (cmd.equals(CISConstants.GET_ITEM))
        {
            return getItem(request);
        }
        if (cmd.equals(CISConstants.GET_ORDER))
        {
            return getOrder(request);
        }
        if (cmd.equals(CISConstants.GET_CART))
        {
            return getCart(request);
        }

        return "Error: Unknown command " + cmd + ".";
    }
    private String createUser(Request req)
    {
        String userID = req.getParam(CISConstants.USER_ID_PARAM);
        String userName = req.getParam(CISConstants.USER_NAME_PARAM);
        String yearLevel = req.getParam(CISConstants.YEAR_LEVEL_PARAM);

        if (userID == null || userName == null || yearLevel == null)
        {
            return CISConstants.PARAM_MISSING_ERR;
        }


        for (CISUser user : users)
        {
            if (user.getUserID().equals(userID))
            {
                return CISConstants.DUP_USER_ERR;
            }
        }

        CISUser newUser = new CISUser(userID, userName, yearLevel);
        users.add(newUser);
        return CISConstants.SUCCESS;
    }
    private String addMenuItem(Request req)
    {
        String name = req.getParam(CISConstants.ITEM_NAME_PARAM);
        String desc = req.getParam(CISConstants.DESC_PARAM);
        String priceStr = req.getParam(CISConstants.PRICE_PARAM);
        String itemID = req.getParam(CISConstants.ITEM_ID_PARAM);
        String type = req.getParam(CISConstants.ITEM_TYPE_PARAM);

        if (name == null || desc == null || priceStr == null || itemID == null || type == null)
        {
            return CISConstants.PARAM_MISSING_ERR;
        }


        for (MenuItem item : menu.getEatriumItems())
        {
            if (item.getId().equals(itemID))
            {
                return CISConstants.DUP_ITEM_ERR;
            }
        }


        double price = Double.parseDouble(priceStr);


        MenuItem newItem = new MenuItem(name, desc, price, itemID, type);
        menu.getEatriumItems().add(newItem);
        return CISConstants.SUCCESS;
    }
    private String placeOrder(Request req)
    {
        String orderID = req.getParam(CISConstants.ORDER_ID_PARAM);
        String userID = req.getParam(CISConstants.USER_ID_PARAM);
        String orderType = req.getParam(CISConstants.ORDER_TYPE_PARAM);
        String itemID = req.getParam(CISConstants.ITEM_ID_PARAM);


        if (orderID == null || userID == null || orderType == null || itemID == null)
        {
            return CISConstants.PARAM_MISSING_ERR;
        }


        CISUser user = findUser(userID);
        if (user == null)
        {
            return CISConstants.USER_INVALID_ERR;
        }


        MenuItem menuItem = findMenuItem(itemID);
        if (menuItem == null)
        {
            return CISConstants.INVALID_MENU_ITEM_ERR;
        }


        for (CISUser u : users)
        {
            for (Order order : u.getOrders())
            {
                if (order.getOrderID().equals(orderID))
                {
                    return CISConstants.DUP_ORDER_ERR;
                }
            }
        }


        if (menuItem.getAmountAvailable() <= 0)
        {
            return CISConstants.SOLD_OUT_ERR;
        }


        if (user.getMoney() < menuItem.getPrice())
        {
            return CISConstants.USER_BROKE_ERR;
        }


        Order newOrder = new Order(itemID, orderType, orderID);
        user.getOrders().add(newOrder);
        user.setMoney(user.getMoney() - menuItem.getPrice());
        menuItem.setAmountAvailable(menuItem.getAmountAvailable() - 1);

        return CISConstants.SUCCESS;
    }
    private String deleteOrder(Request req)
    {
        String userID = req.getParam(CISConstants.USER_ID_PARAM);
        String orderID = req.getParam(CISConstants.ORDER_ID_PARAM);

        if (userID == null || orderID == null)
        {
            return CISConstants.PARAM_MISSING_ERR;
        }

        CISUser user = findUser(userID);
        if (user == null)
        {
            return CISConstants.USER_INVALID_ERR;
        }


        for (int i = 0; i < user.getOrders().size(); i++)
        {
            if (user.getOrders().get(i).getOrderID().equals(orderID))
            {
                user.getOrders().remove(i);
                return CISConstants.SUCCESS;
            }
        }


        return CISConstants.ORDER_INVALID_ERR;
    }
    private String getUser(Request req)
    {
        String userID = req.getParam(CISConstants.USER_ID_PARAM);
        CISUser user = findUser(userID);
        if (user == null) return CISConstants.USER_INVALID_ERR;
        return user.toString();
    }

    private String getItem(Request req)
    {
        String itemID = req.getParam(CISConstants.ITEM_ID_PARAM);
        MenuItem item = findMenuItem(itemID);
        if (item == null) return CISConstants.INVALID_MENU_ITEM_ERR;
        return item.toString();
    }

    private String getOrder(Request req)
    {
        String userID = req.getParam(CISConstants.USER_ID_PARAM);
        String orderID = req.getParam(CISConstants.ORDER_ID_PARAM);

        CISUser user = findUser(userID);
        if (user == null) return CISConstants.USER_INVALID_ERR;

        for (Order order : user.getOrders())
        {
            if (order.getOrderID().equals(orderID))
            {
                return order.toString();
            }
        }
        return CISConstants.ORDER_INVALID_ERR;
    }

    private String getCart(Request req)
    {
        String userID = req.getParam(CISConstants.USER_ID_PARAM);
        CISUser user = findUser(userID);
        if (user == null) return CISConstants.USER_INVALID_ERR;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < user.getOrders().size(); i++)
        {
            if (i > 0) sb.append(", ");
            sb.append(user.getOrders().get(i).toString());
        }
        return sb.toString();
    }
    private CISUser findUser(String userID)
    {
        for (CISUser user : users)
        {
            if (user.getUserID().equals(userID))
            {
                return user;
            }
        }
        return null;
    }

    private MenuItem findMenuItem(String itemID)
    {
        for (MenuItem item : menu.getEatriumItems())
        {
            if (item.getId().equals(itemID))
            {
                return item;
            }
        }
        return null;
    }


    public static void main(String[] args)
    {
        CIServer f = new CIServer();
        f.start(args);
    }

}
