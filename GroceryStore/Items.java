package GroceryStore;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Items {
    int itemid;
    String name;
    String category;
    Double price;
    int quantity;

    public Items(int itemid, String name, String category, Double price, int quantity) {
        this.itemid = itemid;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    public int getItemid() {
        return itemid;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public Double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Items [itemid=" + itemid + ", name=" + name + ", category=" + category + ", price=" + price
                + ", quantity=" + quantity + "]";
    }

    
    // Check if an item with the given name exists
    ResultSet isItemExist(Connection con, String name) throws SQLException {
        PreparedStatement ps = con.prepareStatement("Select * from store where iname=?");
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        return rs;
    }


    // Check if items in a given category exist
    ResultSet isCategoryExist(Connection con, String category) throws SQLException {
        if(con!=null){
            PreparedStatement ps = con.prepareStatement("Select * from store where category=?");
            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();
            return rs;
        }else{
            return null;
        }
        
    }


    // Get item details by item id
    static Items getItem(Connection con, int id) {
        try {
            String query = "select * from store where itemid=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Items(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(5), rs.getInt(4));
            }
        } catch (Exception e) {
            // Handle exceptions
        }

        return null;
    }


    // Check if an item is in stock
    static boolean isItemInStock(Connection con, int id, int quantity) {
        try {
            String query = "select quantity from store where itemid=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (quantity > rs.getInt(1))
                    return false;
            }
        } catch (Exception e) {
            // Handle exceptions
        }
        return true;
    }


    // Update item stock quantity
    static void updateStock(Connection con, int id, int quantity1) throws Exception {
        con.setAutoCommit(false);
        try {
            PreparedStatement ps = con.prepareStatement("select quantity from store where itemid=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                ;
            int dataQuantity = rs.getInt(1);
            CallableStatement cs = con.prepareCall("call stockUpdate(?,?)");
            cs.setInt(1, id);
            if (Thread.currentThread().getName().equals("add")) {
                cs.setInt(2, dataQuantity - quantity1);
            } else if (Thread.currentThread().getName().equals("remove")) {
                cs.setInt(2, dataQuantity + quantity1);
            }
            cs.execute();
            con.commit();
        } catch (SQLException e) {
            // Handle SQL exceptions
            System.out.println("no item exists..");
            System.out.println("rolling back..");
            con.rollback();
        } catch (Exception e) {
            // Handle other exceptions
            System.out.println(e);
            System.out.println("rolling back..");
            con.rollback();
        }
    }
}
