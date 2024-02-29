import java.util.Scanner;

public class TelephoneDirectory {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Directory dir = new Directory();
        do {
            displayChoice();
            System.out.print("Enter choice:");
            int key = sc.nextInt();
            switch (key) {
                case 1:
                    sc.nextLine();
                    System.out.println("Enter name of the person");
                    String name = sc.nextLine();
                    System.out.println("Enter area of person");
                    String area = sc.next();
                    sc.nextLine();

                    System.out.println("Press 1 to add phone number, 2 to add landline or 3 to add both");
                    int choice = sc.nextInt();
                    switch (choice) {
                        case 1:
                            dir.addPersonPhone(name, area);
                            System.out.println("Press 1 to add person to favorite");
                            int fav = sc.nextInt();
                            if (fav == 1) {
                                dir.addToFavorite(name);
                            }
                            break;
                        case 2:
                            dir.addPersonLandLine(name, area);
                            System.out.println("Press 1 to add person to favorite");
                            fav = sc.nextInt();
                            if (fav == 1) {
                                dir.addToFavorite(name);
                            }
                            break;
                        case 3:
                            dir.addPersonPhoneAndLandLine(name, area);
                            System.out.println("Press 1 to add person to favorite");
                            fav = sc.nextInt();
                            if (fav == 1) {
                                dir.addToFavorite(name);
                            }
                            break;
                        default:
                            break;
                    }
                    break;

                case 2:
                    dir.displayDirectory();
                    break;

                case 3:
                    dir.displayFavorites();
                    break;

                case 4:
                    sc.nextLine();
                    System.out.println("Enter name");
                    name = sc.nextLine();
                    dir.addToFavorite(name);
                    break;

                case 5:
                    sc.nextLine();
                    System.out.println("Enter name");
                    name = sc.nextLine();
                    dir.removeFromFavorite(name);
                    break;

                case 6:
                    dir.displayNameFromArea();
                    sc.nextLine();
                    break;

                case 7:
                    dir.removeContact();
                    sc.nextLine();
                    break;

                case 8:
                    dir.searchByName();
                    break;

                case 9:
                    dir.searchbyPrefix();
                default:
                    System.out.println("Invalid Input!");
                    break;
            }
        } while (true);
    }

    static void displayChoice() {
        System.out.println(
                "Press\n1 to add person\n2 to display directory\n3 to display favorite contacts\n4 to add to favorite\n5 to remove from favorites\n6 to display people from same area\n7 to remove person from directory\n8 to search by name\n9 to search by prefix");
    }
}

class Directory {
    static Scanner sc = new Scanner(System.in);
    //static data
    // ==============================================================================================================================================

    /*
     * CLASS NODE TO INITIATE VARIABLES AND SET NODE REFERENCE
     */

    class Node {
        Node next, prev;
        String name;
        Details d;
        boolean favorite;

        public Node(String name, String area, long phone_number_1, long land_line) {
            this.name = name.toUpperCase();
            d = new Details(area, phone_number_1, land_line);
            next = prev = null;
            favorite = false;
        }
    }

    Node first = null;

    // ==============================================================================================================================================

    /*
     * TO ADD CONTACT WITH ONLY PHONE NUMBER
     */

    void addPersonPhone(String name, String area) {
        long phone_number_1;
        while (true) {
            System.out.println("Enter mobile number");
            phone_number_1 = sc.nextLong();
            if (isMobileNoValid(phone_number_1)) {
                break;
            }
        }
        
        long s = 0;
        System.out.println("");

        Node n = new Node(name, area, phone_number_1, s);
        if (first == null) {
            first = n;
            return;
        }

        Node temp = first;

        while (temp.next != null) {
            temp = temp.next;
        }
        temp.next = n;
        n.prev = temp;
        sortByName();
    }

    // ==============================================================================================================================================

    /*
     * TO ADD CONTACT WITH ONLY LAND-LINE NUMBER
     */

    void addPersonLandLine(String name, String area) {
        long land_line;
        while (true) {
            System.out.println("Enter mobile number");
            land_line = sc.nextLong();
            if (isLandLineNoValid(land_line)) {
                break;
            }
        }
        long s = 0;

        Node n = new Node(name, area, s, land_line);
        if (first == null) {
            first = n;
            return;
        }

        Node temp = first;

        while (temp.next != null) {
            temp = temp.next;
        }
        temp.next = n;
        n.prev = temp;
        sortByName();

    }

    // ==============================================================================================================================================

    /*
     * TO ADD CONTACT WITH BOTH MOBILE AND LAND-LINE NUMBER
     */

    void addPersonPhoneAndLandLine(String name, String area) {
        long phone_number_1;
        while (true) {
            System.out.println("Enter mobile number");
            phone_number_1 = sc.nextLong();
            if (isMobileNoValid(phone_number_1)) {
                break;
            }
        }
        long land_line;
        while (true) {
            System.out.println("Enter mobile number");
            land_line = sc.nextLong();
            if (isLandLineNoValid(land_line)) {
                break;
            }
        }
        long s = 0;

        Node n = new Node(name, area, phone_number_1, land_line);
        if (first == null) {
            first = n;
            return;
        }

        Node temp = first;

        while (temp.next != null) {
            temp = temp.next;
        }
        temp.next = n;
        n.prev = temp;
        sortByName();

    }

    // ==============================================================================================================================================

    /*
     * TO SEARCH BY NAME
     */

    void searchByName() {
        if (first == null) {
            System.out.println("Phone directory is empty!");
            return;
        }
        sc.nextLine();

        System.out.println("Enter the name");
        String name = sc.nextLine();

        Node temp = first;
        boolean flag = false;

        while (temp != null) {
            if (temp.name.equalsIgnoreCase(name)) {
                flag = true;
                System.out.println("Name           : " + temp.name);
                System.out.println("Area           : " + temp.d.area);
                if (temp.d.phone_number_1 != 0) {
                    System.out.println("Phone number 1 : " + temp.d.phone_number_1);
                } else {
                    System.out.println("Phone number 1 : ---");
                }
                if (temp.d.land_line != 0) {
                    System.out.println("Land-line      : " + temp.d.land_line);
                } else {
                    System.out.println("Land-line      : ---");
                }
                System.out.println();
            }
            temp = temp.next;
        }

        if (!flag) {
            System.out.println("No contacts with name : " + name);
        }
    }

    void sortByName() {
        if (first == null) {
            System.out.println("Phone directory is empty!");
            return;
        }
        // int count = nodeCount();
        for (int i = 0; i < nodeCount(); i++) {
            Node temp = first;
            Node temp1 = first;
            for (int j = 0; j < nodeCount() - i - 1; j++) {
                if (temp1.name.compareToIgnoreCase(temp1.next.name) > 0) {
                    swap(temp1, temp1.next);
                }
                temp1 = temp1.next;
            }
            temp = temp.next;
        }
    }

    int nodeCount() {

        Node temp = first;
        int count = 0;
        while (temp != null) {
            count++;
            temp = temp.next;
        }
        return count;
    }

    void swap(Node temp, Node temp1) {
        String name = temp.name;
        temp.name = temp1.name;
        temp1.name = name;

        boolean change = temp.favorite;
        temp.favorite = temp1.favorite;
        temp1.favorite = change;

        Details details_swap = temp.d;
        temp.d = temp1.d;
        temp1.d = details_swap;
    }

    // ==============================================================================================================================================

    /*
     * TO DISPLAY THE ENTIRE DIRECTORY
     */

    void displayDirectory() {
        if (first == null) {
            System.out.println("No contacts in directory");
            return;
        }

        Node temp = first;
        int i = 1;

        System.out.println("YOUR DIRECTORY");
        System.out.println();
        while (temp != null) {
            System.out.println("Contact " + i++);
            display(temp);
            temp = temp.next;
        }

    }

    void display(Node temp) {
        System.out.println("Name           : " + temp.name);
        System.out.println("Area           : " + temp.d.area);
        if (temp.d.phone_number_1 != 0) {
            System.out.println("Phone number 1 : " + temp.d.phone_number_1);
        } else {
            System.out.println("Phone number 1 : ---");
        }
        if (temp.d.land_line != 0) {
            System.out.println("Land-line      : " + temp.d.land_line);
        } else {
            System.out.println("Land-line      : ---");
        }
        System.out.println();
    }

    // ==============================================================================================================================================

    /*
     * TO DISPLAY PEOPLE FROM SAME AREA
     */

    void displayNameFromArea() {
        if (first == null) {
            System.out.println("No favorites in directory");
            return;
        }

        sc.nextLine();
        System.out.println("Enter area");
        String area = sc.nextLine();
        Node temp = first;
        int i = 1;

        System.out.println("PEOPLE FROM " + area);
        System.out.println();
        while (temp != null) {
            if (temp.d.area.equalsIgnoreCase(area)) {
                System.out.println("Contact " + i++);
                display(temp);
            }
            temp = temp.next;
        }
    }

    // ==============================================================================================================================================

    /*
     * TO DISPLAY FAVORITE CONTACTS
     */

    void displayFavorites() {
        if (first == null) {
            System.out.println("No favorites in directory");
            return;
        }

        Node temp = first;
        int i = 1;

        System.out.println("YOUR FAVORITES");
        System.out.println();
        while (temp != null) {
            if (temp.favorite) {
                System.out.println("Contact " + i++);
                display(temp);
            }
            temp = temp.next;
        }
    }

    // ==============================================================================================================================================

    /*
     * TO REMOVE CONTACT FROM DIRECTORY
     */

    void removeContact() {
        if (first == null) {
            System.out.println("Directory is empty");
            return;
        }
        sc.nextLine();
        System.out.println("Enter the name of the person");
        String name = sc.nextLine();
        if (first.next == null && first.name.equalsIgnoreCase(name)) {
            first = null;
            return;
        }

        if (first.name.equalsIgnoreCase(name)) {
            Node temp = first;
            first.next.prev = null;
            first = first.next;
            temp.next = null;
            return;
        }

        boolean flag = false;

        Node temp = first;
        while (temp != null) {
            if (temp.name.equalsIgnoreCase(name)) {
                flag = true;
                break;
            }
            temp = temp.next;
        }

        if (flag) {
            if (temp.next == null) {
                temp.prev.next = temp.prev = null;
            } else {
                temp.next.prev = temp.prev;
                temp.prev.next = temp.next;
                temp.next = temp.prev = null;
            }
        }
    }

    // void deleteAlternateNumber() {

    // }

    // ==============================================================================================================================================

    /*
     * TO ADD A PERSON TO FAVORITE
     */

    void addToFavorite(String name) {
        Node temp = first;
        while (temp != null) {
            if (temp.name.equalsIgnoreCase(name)) {
                temp.favorite = true;
                return;
            }
            temp = temp.next;
        }
    }

    // ==============================================================================================================================================

    /*
     * TO REMOVE A PERSON FROM FAVORITE
     */

    void removeFromFavorite(String name) {
        Node temp = first;
        while (temp != null) {
            if (temp.name.equalsIgnoreCase(name)) {
                temp.favorite = false;
                return;
            }
            temp = temp.next;
        }
    }
    // ==============================================================================================================================================

    /*
     * TO search by char
     */
    void searchbyChar() {
        System.out.println("Enter character");
        char c = sc.next().toUpperCase().charAt(0);
        Node temp = first;
        while (temp != null) {
            if (c == temp.name.charAt(0)) {
                display(temp);
            }
            temp = temp.next;
        }
    }
    // ==============================================================================================================================================

    /*
     * TO search by Prefix
     */
    void searchbyPrefix() {
        System.out.println("Enter Prefix");
        String c = sc.next().toUpperCase();
        Node temp = first;
        while (temp != null) {
            if (temp.name.startsWith(c)) {
                display(temp);
            }
            temp = temp.next;
        }
    }

    boolean isMobileNoValid(Long mobilenum){
        String num="";
        num=num.valueOf(mobilenum);
        if(num.length()!=10){
            return false;
        }
        return true;
    }
    boolean isLandLineNoValid(Long mobilenum){
        String num="";
        num=num.valueOf(mobilenum);
        //num.valueOf(mobilenum);
        if(num.length()!=8){
            return false;
        }
        return true;
    }
}

class Details {
    String area;
    long phone_number_1, land_line;

    public Details(String area, long phone_number_1, long land_line) {
        this.area = area;
        this.phone_number_1 = phone_number_1;
        this.land_line = land_line;
    }
}