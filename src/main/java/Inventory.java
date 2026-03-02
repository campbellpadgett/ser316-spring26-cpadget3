import java.util.HashMap;
import java.util.Map;

public class Inventory {

    private Map<String, Patron> patrons; // PatronID -> Patron
    private static Map<String, Book> bookList; // ISBN -> Book
    private static final int ISBN_TEN = 10;
    private static final int ISBN_THIRTEEN = 13;

    public Inventory()
    {
        this.patrons = new HashMap<>();
        this.bookList = new HashMap<>();
    }

    /**
     * provides a Map of all books within inventory
     * @return Map<String, Book> a map of the books in inventory associated with isbn numebrs
     */
    //SER316 TASK 2 SPOTBUGS FIX
    public Map<String, Book> getInventory()
    {

        //SER316 TASK 2 SPOTBUGS FIX
        Map<String, Book> blist = Map.of();
        blist.putAll(this.bookList);

        return blist;
    }

    /**
     * provides a Map of all Patrons who have a relation to the library
     * @return Map<String, Patron> a map of the Patrons with a relationship to the library
     */
    //SER316 TASK 2 SPOTBUGS FIX
    public Map<String, Patron> getPatrons()
    {
        //SER316 TASK 2 SPOTBUGS FIX
        Map<String, Patron> plist = Map.of();
        plist.putAll(this.patrons);

        return plist;
    }

    /**
     * Validates ISBN format you can assume this javadoc is correct.
     * Valid formats:
     * - ISBN-10: 10 digits (e.g., "0123456789")
     * - ISBN-13: 13 digits (e.g., "9780123456789")
     * - ISBN with hyphens: XXX-X-XXXX-XXXX-X (e.g., "978-0-1234-5678-9")
     *
     * Invalid:
     * - null or empty strings
     * - Contains letters or special characters (except hyphens)
     * - Wrong number of digits after removing hyphens
     *
     * @param isbn The ISBN string to validate
     * @return true if valid format, false otherwise
     */
    public static boolean isValidISBN(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            return false;
        }


        String numbers = isbn.replace("-", "");

        // Check if all remaining characters are digits
        if (!numbers.matches("\\d+")) {
            return false;
        }

        // Check length (must be 10 or 13 digits)
        int length = numbers.length();
        return length == ISBN_TEN || length == ISBN_THIRTEEN;
    }
}
