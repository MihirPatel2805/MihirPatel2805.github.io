package GroceryStore;

// Custom exception class to handle negative input
public class NegativeInput extends Exception {
    // Constructor that takes a message to describe the exception
    NegativeInput(String s) {
        // Call the constructor of the parent class (Exception) with the provided message
        super(s);
    }
}