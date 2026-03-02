import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a library patron (user).
 * Tracks checked out books, fines, and account status.
 */
public class Patron {
    private String patronId;
    private String name;
    private String email;
    private PatronType type;
    private boolean suspended;
    private double fines;
    private Map<String, LocalDate> bookMap;
    private int overdue;
    private LocalDate memberDate;

    public enum PatronType {
        STUDENT,
        FACULTY,
        STAFF,
        PUBLIC,
        CHILD
    }

    /**
     * Creates a new Patron.
     *
     * @param patronId Unique patron ID (format: P-XXXXX)
     * @param name Patron's full name
     * @param email Patron's email address
     * @param type Patron type (determines checkout limits)
     */
    public Patron(String patronId, String name, String email, PatronType type) {
        this.patronId = patronId;
        this.name = name;
        this.email = email;
        this.type = type;
        this.suspended = false;
        this.fines = 0.0;
        this.bookMap = new HashMap<>();
        this.overdue = 0;
        this.memberDate = LocalDate.now();
    }

    // Getters
    public String getPatronId() {
        return patronId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public PatronType getType() {
        return type;
    }

    public boolean isAccountSuspended() {
        return suspended;
    }

    public double getFineBalance() {
        return fines;
    }

    public Map<String, LocalDate> getCheckedOutBooks() {
        return bookMap;
    }

    public int getCheckoutCount() {
        return bookMap.size();
    }

    public int getOverdueCount() {
        return overdue;
    }

    public LocalDate getMemberSince() {
        return memberDate;
    }

    /**
     * Returns the maximum number of books this patron can check out
     * based on their patron type.
     *
     * @return Maximum checkout limit
     */
    public int getMaxCheckoutLimit() {
        switch (type) {
            case FACULTY:
                return 20;
            case STAFF:
                return 15;
            case STUDENT:
                return 10;
            case PUBLIC:
                return 5;
            case CHILD:
                return 3;
            default:
                return 5;
        }
    }

    /**
     * Returns the standard loan period in days for this patron type.
     *
     * @return Loan period in days
     */
    public int getLoanPeriodDays() {
        if(type==PatronType.FACULTY)return 60;
        else if(type==PatronType.STAFF)return 45;
        else if(type==PatronType.STUDENT)return 30;
        else if(type==PatronType.PUBLIC)return 21;
        else if(type==PatronType.CHILD)return 14;
        else return 21;
    }

    public void resetFines() {
        this.fines = 0.0;
    }

    public boolean chkSuspended() {
        return this.suspended;
    }

    // Setters
    public void setAccountSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public void setOverdueCount(int count) {
        this.overdue = count;
    }

    /**
     * Adds a fine to the patron's balance.
     *
     * @param amount Amount to add
     */
    public void addFine(double amount) {
        if (amount > 0) {
            this.fines += amount;
        } else {
        }
    }

    /**
     * Pays off a portion of the fine balance.
     *
     * @param amount Amount to pay
     * @return Remaining balance
     */
    public double payFine(double amount)
    {
        if (amount <= 0) return this.fines;
        this.fines = Math.max(0.0, this.fines - amount);

        return this.fines;
    }

    /**
     * Adds a book to the checked out books list.
     *
     * @param isbn Book ISBN
     * @param dueDate Due date for the book
     */
    public void addCheckedOutBook(String isbn, LocalDate dueDate) {
        bookMap.put(isbn, dueDate);
    }

    /**
     * Removes a book from the checked out books list.
     *
     * @param isbn Book ISBN to remove
     */
    public void removeCheckedOutBook(String isbn) {
        bookMap.remove(isbn);
    }

    /**
     * Checks if this patron currently has a specific book checked out.
     *
     * @param isbn Book ISBN
     * @return true if book is checked out by this patron
     */
    public boolean hasBookCheckedOut(String isbn) {
        if (bookMap.containsKey(isbn) == true) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Compares patrons based on patronId.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Patron other = (Patron) obj;
        if (patronId == null) {
            if (other.patronId != null) return false;
        } else if (!patronId.equals(other.patronId)) {
            return false;
        }
        return true;
    }

    /**
     * Checks if a patron type string matches a given type.
     *
     * @param typeString The type as a string
     * @param expectedType The expected patron type
     * @return true if types match
     */
    public boolean isPatronType(String typeString, Patron.PatronType expectedType) {
        //SER316 TASK 2 SPOTBUGS FIX
        if (Objects.isNull(typeString) || Objects.isNull(expectedType)) {
            return false;
        }

        //SER316 TASK 2 SPOTBUGS FIX
        return typeString.equals(expectedType.toString());
    }

    /**
     * Validates if a patron is eligible to check out books you can assume this method is correct.
     * This helper method consolidates patron-related eligibility checks.
     * Students can assume this method is correct and use it in their implementation.
     *
     * Returns error codes for the following conditions (checked in order):
     * - Patron is null → 3.1
     * - Account is suspended → 3.0
     * - Has 3 or more overdue books → 4.0
     * - Has $10.00 or more in fines → 4.1
     *
     * @param patron The patron to validate
     * @return 0.0 if eligible, or appropriate error code (3.1, 3.0, 4.0, 4.1)
     */
    public static double validatePatronEligibility(Patron patron) {
        final int MAX_OVERDUE_BOOKS = 3;
        final double NULL_PATRON = 3.1;
        final double SUSPENDED_ACCT = 3.0;
        final double OVERDUE_BOOKS = 4.0;
        final double TEN_DOLLAR_FINE = 10.0;
        final double TEN_PLUS_FINE = 4.1;
        final double ELIGABLE = 0.0;

        if (patron == null) {
            return NULL_PATRON;
        }
        if (patron.isAccountSuspended()) {
            return SUSPENDED_ACCT;
        }
        if (patron.getOverdueCount() >= MAX_OVERDUE_BOOKS) {
            return OVERDUE_BOOKS;
        }
        if (patron.getFineBalance() >= TEN_DOLLAR_FINE) {
            return TEN_PLUS_FINE;
        }
        return ELIGABLE; // Eligible
    }


    @Override
    public String toString() {
        return patronId+"-"+name+"("+type+")"+"[Books:"+bookMap.size()+"/"+getMaxCheckoutLimit()+",Fines:$"+ fines +"]";
    }
}
