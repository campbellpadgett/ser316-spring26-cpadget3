import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Manages library checkout operations.
 * Handles book checkouts, returns, renewals, and fine calculations.
 */
public class Checkout {
    private static final double MAX_CHECKOUT = 3.2;
    private static final double NULL_BOOK = 2.1;
    private static final double REFERENCE_BOOK = 5.0;
    private static final double SUCCESS_RENEWAL = 0.1;
    private static final double SUCCESS_WARNING = 1.1;
    private static final double AVAILABLE_BOOK = 2.0;

    private Map<String, Book> bookList; // ISBN -> Book
    private Map<String, Patron> patrons; // PatronID -> Patron
    private List<Transaction> history; //

    /**
     * Constructor for the Checkout class, no inputs are needed.
     */
    public Checkout() {
        this.bookList = new HashMap<>();
        this.patrons = new HashMap<>();
        this.history = new ArrayList<>();
    }

    /**
     * Adds instance of Book class to bookList by isbn number
     *
     * @param book an instance of the book class to add
     * @return void
     */
    public void addBook(Book book) {
        bookList.put(book.getIsbn(), book);
    }

    /**
     * Adds instance of Patron class to add by patron id
     *
     * @param patron an instance of the Patron class to add
     * @return void
     */
    public void registerPatron(Patron patron) {
        patrons.put(patron.getPatronId(), patron);
    }

    /**
     * Main checkout method - processes a book checkout for a patron.
     * This method performs comprehensive validation and returns a status code.
     *
     * TESTING NOTE: Transaction history is maintained internally and can be assumed
     * to work correctly. Students performing black-box testing should focus on:
     * - Return codes (observable via the method return value)
     * - Book availability changes (observable via book.getAvailableCopies())
     * - Patron's checked-out books (observable via patron.getCheckedOutBooks())
     * Do NOT attempt to test transaction history - it is not publicly accessible.
     *
     * Return codes:
     *   0.0 - Success, book checked out normally
     *   0.1 - Success, renewal (patron already had this book,
     *      renewal sets the due date to (today + patron.getLoanPeriodDays()).)
     *   1.0 - Success with warning (patron has 1-2 overdue books)
     *   1.1 - Success with warning (patron within 2 of max checkout limit after this checkout)
     *        Max limits: FACULTY=20 (e.g. warning at 18, 19, 20 including current checkout),
     *        STAFF=15, STUDENT=10, PUBLIC=5, CHILD=3
     *   2.0 - Book unavailable (all copies checked out)
     *   2.1 - Book is null
     *   3.0 - Patron account is suspended
     *   3.1 - Patron is null
     *   3.2 - Patron at maximum checkout limit
     *      (FACULTY=20, STAFF=15, STUDENT=10, PUBLIC=5, CHILD=3)
     *   4.0 - Patron has 3 or more overdue books
     *   4.1 - Patron has $10.00 or more in unpaid fines
     *   5.0 - Book is reference-only (cannot be checked out)
     *
     * Validation order (observable priority when multiple conditions apply):
     *   1. Call validatePatronEligibility() assume this method is correct - returns in this order
     *      1.1. eligible 0.0 -> continue with step 2
     *      1.2. checks patron null (3.1) -> return this error code right away
     *      1.3. suspended (3.0) -> return this error code right away
     *      1.4. overdue count >= 3 (4.0) -> return this error code right away
     *      1.5. fines >= $10 (4.1) -> return this error code right away
     *   2. Check if book is null (2.1)
     *   3. Check if book is reference-only (5.0)
     *   4. If renewal, return 0.1 immediately (renew skips step 5)
     *   5. If not-renewal
     *      5.1. Check if book is available (2.0)
     *      5.2. Check if patron is at max checkout limit (3.2)
     *      5.3. Process checkout (update patron checkedOutBooks, call book.checkout()),
     *          then determine success code (priority 1.0, then 1.1, else 0.0)
     *
     *
     * Success non-renewal:
     *   - book will be added to list of checkedOutBooks of patron with
     *          dueDate = today + patron.getLoanPeriodDays()
     *   - book.checkout() will be called reducing the availability by 1
     *
     * Success renewal:
     *  - patron.getCheckedOutBooks() is updated to today + loanPeriodDays; b
     *      ook.checkout() is not called; available copies do not change.
     *
     * Additional notes:
     *  - getCheckoutCount() refers to the number of books currently checked out
     *      (size of the patron's checked-out collection), not lifetime transactions;
     *      renewals do not increase this count.
     *  - For any non-success return code (2.x–5.x), neither the patron's checked-out
     *      books nor the book's available copies should change.
     *  - Tests may assume due dates equal
     *      LocalDate.now().plusDays(patron.getLoanPeriodDays()) on the day the test runs.
     *  - A book is unavailable if and only if book.getAvailableCopies() <= 0
     *      (i.e., book.isAvailable() is false).
     *  - Console output (including Easter eggs) is non-functional and should not be
     *      asserted in tests.
     *
     * @param book The book to checkout (can be null)
     * @param patron The patron checking out the book (can be null)
     * @return Status code indicating result (see above)
     */
    public double checkoutBook(Book book, Patron patron)
    {
        double eligable = Patron.validatePatronEligibility(patron);
        if (eligable != 0.0) {
            return eligable;
        }

        if (book == null) return NULL_BOOK;
        if (book.isReferenceOnly()) return REFERENCE_BOOK;


        LocalDate now = LocalDate.now();
        LocalDate dueDate = now.plusDays(patron.getLoanPeriodDays());
        if (patron.hasBookCheckedOut(book.getIsbn()))
        {
            patron.addCheckedOutBook(book.getIsbn(), dueDate);
            history.add(new Transaction(patron, book, now, dueDate));

            return SUCCESS_RENEWAL;
        }

        // availability check
        if (!book.isAvailable()) return AVAILABLE_BOOK;

        // max check
        if (patron.getCheckoutCount() >= patron.getMaxCheckoutLimit()) return MAX_CHECKOUT;

        // checkout
        patron.addCheckedOutBook(book.getIsbn(), dueDate);
        book.checkout();
        history.add(new Transaction(patron, book, now, dueDate));

        //find code after checkout
        if (patron.getOverdueCount() >= 1 && patron.getOverdueCount() <= 2) return 1.0;

        int max = patron.getMaxCheckoutLimit();
        int countAfter = patron.getCheckoutCount();
        if (countAfter >= (max - 2)) return SUCCESS_WARNING;

        return 0.0;

//        // Normal success
//        return 0.0;
    }

    /**
     * Counts available books of a specific type in inventory.
     * Useful for inventory management and reporting.
     *
     * This method demonstrates more complex control flow for white-box testing:
     * - Loop iteration
     * - Nested conditional statements
     * - Multiple decision points
     *
     * @param type The book type to count (FICTION, NONFICTION, REFERENCE, TEXTBOOK, CHILDREN)
     * @param onlyAvailable If true, counts only books with availableCopies > 0;
     *                      if false, counts all books of the type regardless of availability
     * @return Number of books matching the criteria (0 if type is null or no matches found)
     */
    public int countBooksByType(Book.BookType type, boolean onlyAvailable) {

        if (type == null) {
            return 0;
        }

        int looped = 0;

        // Loop through all books in inventory
        for (Book b : bookList.values()) {

            if (b == null) {
                continue;
            }

            // Check if book matches the requested type
            if (b.getType() == type) {
                // Nested condition: filter by availability if requested
                if (onlyAvailable) {
                    // Only count if book has available copies
                    if (b.isAvailable()) {
                        looped++;
                    }
                } else {
                    // Count all books of this type regardless of availability
                    looped++;
                }
            }
        }

        return looped;
    }

    /**
     * Processes a book return.
     * Calculates any overdue fines and updates patron/book status.
     *
     * @param isbn The ISBN of the book being returned
     * @param patron The patron returning the book
     * @return Fine amount charged (0.0 if not overdue)
     */
    public double returnBook(String isbn, Patron patron) {
        if (patron == null || !patron.hasBookCheckedOut(isbn)) {
            return -1.0;
        }

        Book book = bookList.get(isbn);
        if (book == null) {
            return -1.0;
        }

        LocalDate dueDate = patron.getCheckedOutBooks().get(isbn);
        LocalDate today = LocalDate.now();
        long daysOverdue = ChronoUnit.DAYS.between(dueDate, today);

        double fine = 0.0;
        if (daysOverdue > 0) {
            fine = Transaction.calculateFine((int) daysOverdue, book.getType());
            patron.addFine(fine);
        }

        // Update patron and book
        patron.removeCheckedOutBook(isbn);
        book.returnBook();

        // Update transaction history to mark book as returned
        for (Transaction t : history) {
            if (t.patron.equals(patron) && t.book.equals(book) && t.returnDate == null) {
                t.returnDate = today;
                break;
            }
        }

        return fine;
    }
}
