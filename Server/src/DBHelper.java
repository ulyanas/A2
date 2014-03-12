
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;
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
public class DBHelper {

    public static void addItem(String table, String productID, String description,
            int quantity, float perUnitCost) throws ClassNotFoundException, SQLException {
        String dbName = findDatabaseByTable(table);
        Connection DBConn = createConnection("localhost", dbName);
        PreparedStatement ps;

        if (dbName.equals("inventory")) {
            ps = DBConn.prepareStatement(
                    "INSERT INTO " + table + " (product_code, description, quantity, price)"
                    + "VALUES (?, ?, ?, ?);");
        } else {
            ps = DBConn.prepareStatement(
                    "INSERT INTO " + table + " (productid, productdescription, productquantity, productprice)"
                    + "VALUES (?, ?, ?, ?);");
        }

        ps.setString(1, productID);
        ps.setString(2, description);
        ps.setInt(3, quantity);
        ps.setFloat(4, perUnitCost);
        ps.executeQuery();
    }

    public static LinkedList<InventoryItem> listItems(String table) throws ClassNotFoundException, SQLException {
        String dbName = findDatabaseByTable(table);
        Connection DBConn = createConnection("localhost", dbName);
        PreparedStatement ps = DBConn.prepareStatement("SELECT * FROM " + table + ";");
        ResultSet res = ps.executeQuery();

        LinkedList<InventoryItem> list = new LinkedList<InventoryItem>();
        while (res.next()) {
            InventoryItem item = new InventoryItem();
            item.setProductID(res.getString(1));
            item.setDescription(res.getString(2));
            item.setQuantity(res.getInt(3));
            item.setPerUnitCost(res.getFloat(4));
            list.add(item);
        }
        return list;
    }

    public static void deleteItem(String table, String productID) throws ClassNotFoundException, SQLException {
        String dbName = findDatabaseByTable(table);
        Connection DBConn = createConnection("localhost", dbName);
        PreparedStatement ps;

        if (dbName.equals("inventory")) {
            ps = DBConn.prepareStatement("DELETE FROM " + table + " WHERE product_code = ?;");
        } else {
            ps = DBConn.prepareStatement("DELETE FROM " + table + " WHERE productid = ?;");
        }

        ps.setString(1, productID);

        ps.executeUpdate();
    }

    public static void decrementItem(String table, String productID) throws ClassNotFoundException, SQLException {
        String dbName = findDatabaseByTable(table);
        Connection DBConn = createConnection("localhost", dbName);
        PreparedStatement ps;

        if (dbName.equals("inventory")) {
            ps = DBConn.prepareStatement("UPDATE " + table + " SET quantity=(quantity-1) where product_code = ?;");
        } else {
            ps = DBConn.prepareStatement("UPDATE " + table + " SET productquantity=(productquantity-1) where productid = ?;");
        }

        ps.setString(1, productID);

        ps.executeUpdate();
    }

    public static void submitOrder(String firstName, String lastName, String customerAddress, String phoneNumber,
            float fCost, List<InventoryItem> items) throws ClassNotFoundException, SQLException {
        String dbName = findDatabaseByTable("order");
        Connection DBConn = createConnection("localhost", dbName);
        
        Calendar rightNow = Calendar.getInstance();
        String orderTableName = "order" + String.valueOf(rightNow.getTimeInMillis());
        
        PreparedStatement ps = DBConn.prepareStatement("CREATE TABLE " + orderTableName
                + "(item_id int unsigned not null auto_increment primary key, "
                + "product_id varchar(20), description varchar(80), "
                + "item_price float(7,2) );");
        ps.executeUpdate();

        int TheHour = rightNow.get(Calendar.HOUR_OF_DAY);
        int TheMinute = rightNow.get(Calendar.MINUTE);
        int TheSecond = rightNow.get(Calendar.SECOND);
        int TheDay = rightNow.get(Calendar.DAY_OF_WEEK);
        int TheMonth = rightNow.get(Calendar.MONTH);
        int TheYear = rightNow.get(Calendar.YEAR);
        String dateTimeStamp = TheMonth + "/" + TheDay + "/" + TheYear + " "
                + TheHour + ":" + TheMinute + ":" + TheSecond;

        ps = DBConn.prepareStatement("INSERT INTO orders "
                + "(order_date, first_name, last_name, address, phone, total_cost, shipped, ordertable) "
                + " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?);");
        ps.setString(1, dateTimeStamp);
        ps.setString(2, firstName);
        ps.setString(3, lastName);
        ps.setString(4, customerAddress);
        ps.setString(5, phoneNumber);
        ps.setFloat(6, fCost);
        ps.setBoolean(7, false);
        ps.setString(8, orderTableName);
        ps.executeUpdate();

        for (InventoryItem item : items) {
            ps = DBConn.prepareStatement("INSERT INTO " + orderTableName
                    + " (product_id, description, item_price) "
                    + "VALUES (?, ?, ?);");
            ps.setString(1, item.getProductID());
            ps.setString(2, item.getDescription());
            ps.setFloat(3, item.getPerUnitCost());
            ps.executeUpdate();
        }
    }

    public static List<OrderInfo> getPendingOrders() throws ClassNotFoundException, SQLException {
        return getOrders(0);
    }

    public static List<OrderInfo> getShippedOrders() throws ClassNotFoundException, SQLException {
        return getOrders(1);
    }

    public static List<OrderInfo> getOrders(int shipped) throws ClassNotFoundException, SQLException {
        String dbName = findDatabaseByTable("order");
        Connection DBConn = createConnection("localhost", dbName);
        PreparedStatement ps = DBConn.prepareStatement("SELECT * FROM orders WHERE shipped = 1;");
        ResultSet res = ps.executeQuery();

        List<OrderInfo> orderList = new LinkedList<OrderInfo>();
        while (res.next()) {
            OrderInfo order = new OrderInfo();
            order.setOrderID(res.getString(1));
            order.setOrderDate(res.getString(2));
            order.setFirstName(res.getString(3));
            order.setLastName(res.getString(4));
            order.setAddress(res.getString(5));
            order.setPhone(res.getString(6));
            order.setTotalCost(res.getFloat(7));
            order.setShipped(res.getBoolean(8));
            order.setOrderTable(res.getString(9));
            orderList.add(order);
        }
        return orderList;
    }

    public static OrderInfo getOrderInfo(int orderID) throws ClassNotFoundException, SQLException {
        String dbName = findDatabaseByTable("order");
        Connection DBConn = createConnection("localhost", dbName);
        PreparedStatement ps = DBConn.prepareStatement("SELECT * FROM orders WHERE order_id = ?;");
        ps.setInt(1, orderID);

        ResultSet res = ps.executeQuery();
        OrderInfo orderInfo = new OrderInfo();

        while (res.next()) {
            orderInfo.setOrderID(res.getString(1));
            orderInfo.setOrderDate(res.getString(2));
            orderInfo.setFirstName(res.getString(3));
            orderInfo.setLastName(res.getString(4));
            orderInfo.setAddress(res.getString(5));
            orderInfo.setPhone(res.getString(6));
            orderInfo.setShipped(res.getBoolean(7));
            orderInfo.setTotalCost(res.getFloat(8));
            orderInfo.setOrderTable(res.getString(9));
        }
        return orderInfo;
    }

    public static void shipOrder(int orderId) throws ClassNotFoundException, SQLException {
        String dbName = findDatabaseByTable("order");
        Connection DBConn = createConnection("localhost", dbName);
        PreparedStatement ps = DBConn.prepareStatement("UPDATE orders SET shipped = ? WHERE order_id = ?;");
        ps.setBoolean(1, true);
        ps.setInt(2, orderId);
        ps.executeQuery();
    }

    public static String login(String login, String password) throws ClassNotFoundException, SQLException {
        String dbName = findDatabaseByTable("users");
        Connection DBConn = createConnection("localhost", dbName);
        PreparedStatement ps = DBConn.prepareStatement("SELECT * FROM users WHERE login = ?;");
        ps.setString(1, login);
        ResultSet res = ps.executeQuery();
        while (res.next()) {
            String dbPassword = res.getString(2);
            String dbRole = res.getString(3);
            if (dbPassword.equals(password)) {
                return dbRole;
            }
        }
        return "badAuthorization";
    }

    // finds a suitable database on the server given the table name
    private static String findDatabaseByTable(String table) {
        if (table.equals("order")) {
            return "orderinfo";
        } else if (table.equals("users")) {
            return "userinfo";
        } else if (table.equals("trees") || table.equals("shrubs") || table.equals("seeds")) {
            return "inventory";
        } // else if (table.equals("cultureboxes") || table.equals("processing") || table.equals("genomics") || table.equals("referencematerials")){
        else {
            return "leaftech";
        }
    }

    private static Connection createConnection(String serverIP, String dbName) throws ClassNotFoundException, SQLException {
        Connection DBConn = null;       // MySQL connection handle
        Class.forName("com.mysql.jdbc.Driver");
        String sourceURL = "jdbc:mysql://" + serverIP + ":3306/" + dbName;
        DBConn = DriverManager.getConnection(sourceURL, "remote", "remote_pass");
        return DBConn;
    }
}
