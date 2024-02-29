package GroceryStore;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

public class User extends Items {

    // Class-level variables
    static Hashtable<Integer, Integer> basket = new Hashtable<>();
    static Hashtable<String, Items> basketInfo = new Hashtable<>();
    static Scanner sc = new Scanner(System.in);
    static Boolean isSignIn = false;
    static Connection con;
    static String phone;


    // Constructor
    User() {
        super(0, "", "", 0.0, 0);
        try {
            // Establish a database connection
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/grocerystore", "root", "");
        } catch (Exception e) {
            System.out.println("Connection not established");
        }
    }


    // Method to search for items by name
    void searchByItem() throws Exception {
        System.out.println("Enter Item Name:");
        String name = sc.nextLine().toLowerCase();
        ResultSet rs = isItemExist(con, name);
        if (displayItem(rs)) {
            addItemToCart();
        }
    }


    // Method to display item details
    boolean displayItem(ResultSet rs) throws SQLException {
        Boolean flag = true;
        while (rs.next()) {
            flag = false;
            System.out.println("________________________________________");
            System.out.println("Item Id :-            " + rs.getInt("itemid"));
            System.out.println("Item Name:-           " + rs.getString("iname"));
            System.out.println("Item Price:-          " + rs.getDouble("price"));
            System.out.println("Only " + rs.getInt("quantity") + " Unit left..");
            System.out.println("________________________________________");
        }
        if (flag) {
            System.out.println("________________________________________");
            System.out.println("NO Item Found...");
            System.out.println("________________________________________");
            return false;
        } else
            return true;
    }


    // Method to search for items by category
    void searchByCategory() throws Exception {
        System.out.println("Enter Category Name:");
        String category = sc.nextLine().toLowerCase();
        ResultSet rs = isCategoryExist(con, category);
        if (displayItem(rs)) {
            addItemToCart();
        }
    }


    // Method to add items to the shopping cart
    void addItemToCart() throws Exception {
        int choice = 0;
        do {
            try {
                System.out.println("_______________________");
                System.out.println("1) Add to basket");
                System.out.println("2) View basket");
                System.out.println("3) EXIT");
                System.out.println("_______________________");
                System.out.println("Enter choice:");
                choice = Integer.parseInt(sc.nextLine());
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input...");
            } catch (NumberFormatException e) {
            }

            switch (choice) {
                case 1:
                    Thread.currentThread().setName("add");
                    addItem();
                    break;
                case 2:
                    viewCart();
                    break;
                case 3:
                    System.out.println("Exiting....");
                    break;
                default:
                    System.out.println("Invalid Input...");
                    break;
            }
        } while (choice != 3);
    }


    // Static method to add an item to the cart
    static void addItem() throws Exception {
        System.out.print("Enter item id:");
        int item_id = sc.nextInt();
        System.out.print("Enter Quantity:");
        int quantity = sc.nextInt();
        sc.nextLine();
        if (quantity <= 0) {
            throw new NegativeInput("Quantity Can't Be Negative");
        }
        if (isItemInStock(con, item_id, quantity)) {
            Items items = getItem(con, item_id);
            if (items != null) {
                if (basket.containsKey(item_id)) {
                    basket.put(item_id, basket.get(item_id) + quantity);
                } else {
                    basket.put(item_id, quantity);
                    basketInfo.put(items.name, items);
                }
                System.out.println("Added Item..");
                updateStock(con, item_id, quantity);
            } else {
                System.out.println("Invalid Item");
            }
        } else {
            System.out.println("Item out of stock");
        }
    }


    // Method to view the shopping cart
    static void viewCart() throws Exception {
        int choice = 0;
        if (basket.isEmpty()) {
            System.out.println("Your basket is empty");
            return;
        }

        do {
            int totalItems = 0;
            for (String item_name : basketInfo.keySet()) {
                System.out.println("________________________________");
                Items item = basketInfo.get(item_name);
                if (basket.get(item.itemid) == null) {
                    continue;
                }
                System.out.println(++totalItems + ")");
                System.out.println("Item ID:- " + item.itemid);
                System.out.println("Name: " + item_name);
                System.out.println("Quantity: " + basket.get(item.itemid));
                System.out.println("Total price: " + item.price * basket.get(item.itemid));
                System.out.println("________________________________");
            }
            try {
                viewItemMenu();
                choice = sc.nextInt();
                sc.nextLine();
            } catch (Exception e) {
            }

            switch (choice) {
                case 1:
                    removeItemFromCart(con);
                    break;
                case 2:
                    addItemFromCart(con);
                    break;
                case 3:
                    if (isSignIn) {
                        payment();
                        break;
                    } else {
                        Boolean signInCheck;
                        System.out.println("You need to sign in first for Buying");
                        do {
                            signInCheck = signIn();
                        } while (!signInCheck);
                        payment();
                    }
                    break;
                case 4:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }
        } while (choice != 4);
    }


    // Method to display the view item menu
    static void viewItemMenu() {
        System.out.println("1) Remove item");
        System.out.println("2) Add item");
        System.out.println("3) BUY Items");
        System.out.println("4) EXIT");
    }


    // Method to remove an item from the cart
    static void removeItemFromCart(Connection con) throws Exception {
        try {
            Thread.currentThread().setName("remove");
            System.out.print("Enter item id:");
            int item_id = sc.nextInt();
            if (basket.containsKey(item_id)) {
                System.out.print("Enter Quantity:");
                int quantity = sc.nextInt();
                sc.nextLine();
                if (quantity <= 0) {
                    throw new NegativeInput("Quantity Can't Be Negative");
                }
                if (basket.get(item_id) >= quantity) {
                    basket.put(item_id, basket.get(item_id) - quantity);
                    if (basket.get(item_id) == 0) {
                        basket.remove(item_id);
                    }
                    updateStock(con, item_id, quantity);
                    System.out.println("Removed..");
                } else {
                    System.out.println("That Much Quantity Is Not Present In Cart");
                }
            } else {
                System.out.println("No Such Item Present In Cart");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    // Method to add an item from the cart
    static void addItemFromCart(Connection con) throws Exception {
        try {
            Thread.currentThread().setName("add");
            System.out.println("Enter Item id:");
            int item_id = sc.nextInt();
            if (basket.containsKey(item_id)) {
                System.out.println("Enter Quantity:");
                int quantity = sc.nextInt();
                if (quantity <= 0) {
                    throw new NegativeInput("Quantity Can't Be Negative");
                }
                if (isItemInStock(con, item_id, quantity)) {
                    basket.put(item_id, basket.get(item_id) + quantity);
                    updateStock(con, item_id, quantity);
                    System.out.println("Added..");
                } else {
                    System.out.println("Out of stock");
                }
            } else {
                System.out.println("No Such Item Present In Cart");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    // Method for user sign-in
    static boolean signIn() throws Exception {
        System.out.println("Don't have an account? Press 1");
        int choice = sc.nextInt();
        sc.nextLine();
        if (choice == 1)
            signUp();
        System.out.println("FOR SIGN IN >>>");
        System.out.println("Enter Phone Number:");
        phone = sc.nextLine();
        if (!isLoginValid(phone, "0")) {
            System.out.println("Invalid Phone Number");
            return false;
        }
        System.out.println();
        for (int i = 0; i < 3; i++) {
            System.out.print("Enter the Password: ");
            String pass = sc.nextLine();
            if (isLoginValid(phone, pass)) {
                isSignIn = true;
                System.out.println("Sign-In done");
                return true;
            } else
                System.out.println("Incorrect Password");
            if (i != 2)
                System.out.print("Try again: ");
            continue;
        }
        return false;
    }


    // Method for user sign-up
    static void signUp() throws SQLException {
        System.out.println("FOR SIGN UP >>>");
        String pass;
        boolean flag = true;
        System.out.println("Enter name:");
        String name = sc.nextLine();
        System.out.println("Enter Phone Number:");
        do {
            phone = sc.nextLine();
        } while (!isPhoneNumberValid(phone));
        System.out.println("Enter password:");
        do {
            if (!flag) {
                System.out.println("Enter a Strong Password");
            }
            flag = false;
            pass = sc.nextLine();
        } while (!isPassStrong(pass));
        System.out.println("Enter address:");
        String address = sc.nextLine();
        addUserDetails(name, phone, pass, address, con);
        System.out.println("SIGN UP DONE.......");
    }


    // Method to add user details to the database
    static void addUserDetails(String name, String phone, String pass, String address, Connection con) throws SQLException {
        PreparedStatement ps = con.prepareStatement("insert into userdetails(uname,phone,pass,address) values(?,?,?,?)");
        ps.setString(1, name);
        ps.setString(2, phone);
        ps.setString(3, pass);
        ps.setString(4, address);
        int r = ps.executeUpdate();
    }


    // Method to check if a password is strong
    static boolean isPassStrong(String password) {
        String uppercaseRegex = ".*[A-Z].*";
        String lowercaseRegex = ".*[a-z].*";
        String digitRegex = ".*\\d.*";
        String specialCharRegex = ".*[@#$%^&+=!].*";
        int minLength = 8;

        boolean hasUppercase = Pattern.matches(uppercaseRegex, password);
        boolean hasLowercase = Pattern.matches(lowercaseRegex, password);
        boolean hasDigit = Pattern.matches(digitRegex, password);
        boolean hasSpecialChar = Pattern.matches(specialCharRegex, password);
        boolean isLongEnough = password.length() >= minLength;

        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar && isLongEnough;
    }


    // Method to check if a phone number is valid
    static boolean isPhoneNumberValid(String phone) {
        if ((phone.length() == 10) && (phone.startsWith("9") || phone.startsWith("7") || phone.startsWith("6")))
            return true;
        else
            return false;
    }


    // Method to check if login credentials are valid
    static boolean isLoginValid(String phone, String pass) throws Exception {
        PreparedStatement ps = con.prepareStatement("select phone,pass from userdetails where phone=?");
        ps.setString(1, phone);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            if(pass=="0"){
                return true;
            }
            else if (pass.equals(rs.getString(2))) {
                return true;
            }
        }
        return false;
    }


    // Method to validate a UPI ID
    public static boolean isValidUPI(String upiId) {
        int atIndex = upiId.indexOf("@");
        if (upiId.isEmpty() || !upiId.contains("@") || atIndex == 0 || atIndex == upiId.length() - 1
                || upiId.substring(atIndex + 1).length() < 2) {
            return false;
        } else {
            return true;
        }
    }


    // Method for payment processing
    public static void payment() throws Exception {
        if (basket.isEmpty()) {
            System.out.println("Your Cart Is Empty.");
            return;
        } else {
            String sql = "SELECT address FROM userdetails WHERE phone='" + phone + "'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                System.out.println("Items will be delivered to this address.\n" + rs.getString(1));
                System.out.println();
                System.out.println();
            }
            System.out.println("Enter Your UPI ID: ");
            String Upi_id = sc.nextLine();
            if (isValidUPI(Upi_id)) {
                System.out.println("Enter 1 to Place Order.\nEnter any other number to Cancel.");
                int orderplace = sc.nextInt();
                if (orderplace == 1) {
                    System.out.println("Your Order is confirmed.\nIt will be delivered to your DoorStep soon.\nThank you.\nYour Invoice will be in your Invoice folder:");
                    Set<Integer> a = basket.keySet();
                FileWriter f2 = new FileWriter("D:\\sem 2\\PROJECTS\\JAVA\\INDI\\individual_project\\Invoice\\Invoice"+Upi_id+".txt");
                BufferedWriter fw=new BufferedWriter(f2);
                fw.write("                                                               Your  Invoice               ");
                fw.newLine();
                fw.newLine();
                String sqldetail="SELECT userid,uname,phone,pass,address FROM userdetails WHERE phone="+phone;
                Statement stdetail=con.createStatement();
                ResultSet rsdetail=stdetail.executeQuery(sqldetail);
                while(rsdetail.next()){
                    fw.write("Billing Address:");
                    fw.newLine();
                    fw.write(rsdetail.getString(2));
                    fw.newLine();
                    fw.write(rsdetail.getString(5));
                    fw.newLine();
                    fw.write("Mobile Num: "+rsdetail.getString(3));
                    fw.newLine();
                    fw.newLine();
                    fw.newLine();
                    fw.newLine();
                }
                float totalBill = 0;
                    for (String item_name : basketInfo.keySet()) {
                        Items item = basketInfo.get(item_name);
                        totalBill += basket.get(item.itemid) * item.price;
                    }
                    fw.write(" sr.n0          id               Item              quantity            Total Price");
                    for (String item_name : basketInfo.keySet()) {
                        System.out.println("________________________________");
                        fw.newLine();
                        Items item = basketInfo.get(item_name);
                        if (basket.get(item.itemid) == null) {
                            continue;
                        }
                        int totalItems = 0;
                        fw.write("   "+(++totalItems) + "  ");
                        fw.write("          " + item.itemid);
                        fw.write("                " + item_name);
                        fw.write("             " + basket.get(item.itemid));
                        fw.write("                    " + item.price * basket.get(item.itemid));
                    }
                    fw.newLine();
                    fw.write("                                                                            Total Price: "+totalBill+" /-");
                    fw.newLine();
                    fw.newLine();
                    fw.newLine();
                    fw.newLine();
                    fw.newLine();
                    fw.write("                                                               Thank You");
                    fw.flush();
                    Main.main(null);
                } else {
                    return;
                }
            } else {
                payment();
                }
            }
        }
    }

