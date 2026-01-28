import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Sample Black-Box tests for the Checkout system.
 * This class demonstrates how to write black-box tests using:
 * - Equivalence Partitioning (EP)
 * - Boundary Value Analysis (BVA)
 * - Parametrized tests across multiple implementations
 *
 * Black-box testing focuses on testing the SPECIFICATION WITHOUT
 * looking at the implementation.
 *
 * The parameterized structure allows testing all Checkout implementations
 * with the same tests to identify which implementations have bugs.
 */
public class CheckoutBlackBoxSample {

    private Checkout checkout;

    /**
     * Provides the list of Checkout classes to test.
     * Each test will run against ALL implementations.
     */
    @SuppressWarnings("unchecked")
    static Stream<Class<? extends Checkout>> checkoutClassProvider() {
        return (Stream<Class<? extends Checkout>>) Stream.of(
                Checkout0.class,
                Checkout1.class,
                Checkout2.class,
                Checkout3.class
        );
    }

    // Uncomment when you implement the method in assign 3 and comment the above
//    static Stream<Class<? extends Checkout>> checkoutClassProvider() {
////        return Stream.of(Checkout.class);
////    }


    /**
     * Helper method to create Checkout instance from class using reflection.
     */
    private Checkout createCheckout(Class<? extends Checkout> clazz) throws Exception {
        Constructor<? extends Checkout> constructor = clazz.getConstructor();
        return constructor.newInstance();
    }

    /**
     * SAMPLE TEST 1: Tests successful checkout of an available book
     * This tests the valid equivalence partition - all conditions met.
     */
    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T2: Successful checkout - available book, eligible patron")
    public void testBookAvailable(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        // Setup: Create available book and eligible patron
        Book book = new Book("978-0-123456-78-9", "Test Book",
                "Test Author", Book.BookType.FICTION, 1);

        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                Patron.PatronType.STUDENT);

        checkout.addBook(book); // adding the book to the library
        checkout.registerPatron(patron); // adding a patrol to the system

        // Execute checkout
        double result = checkout.checkoutBook(book, patron);

        // Verify: Should return 0.0 for success
        assertEquals(0.0, result, 0.01,
                "Expected successful checkout (0.0) for " + checkoutClass.getSimpleName());

        // Verify: Book should now be unavailable
        assertFalse(book.isAvailable(),
                "Book should be unavailable after checkout for " + checkoutClass.getSimpleName());

        // Verify: Patron should have the book in their checked-out list
        assertTrue(patron.hasBookCheckedOut(book.getIsbn()),
                "Patron should have book in checked-out list for " + checkoutClass.getSimpleName());

        // Verify: Checkout count increased
        assertEquals(1, patron.getCheckoutCount(),
                "Patron checkout count should be 1 for " + checkoutClass.getSimpleName());
    }

    /**
     * SAMPLE TEST 2: Tests checkout with unavailable book
     * This tests an invalid equivalence partition.
     */
//    @ParameterizedTest
//    @MethodSource("checkoutClassProvider")
//    @DisplayName("T1: Unavailable book returns error code 2.0")
//    public void testUnavailableBook(Class<? extends Checkout> checkoutClass) throws Exception {
//        checkout = createCheckout(checkoutClass);
//
//        // Setup: Create unavailable book
//        Book book = new Book("978-0-123456-78-9", "Test Book",
//                "Test Author", Book.BookType.FICTION, 5);
//        book.setAvailableCopies(0);  // We are pretending it has been checked out by others and is not available anymore
//
//        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
//                Patron.PatronType.STUDENT);
//
//        checkout.addBook(book);
//        checkout.registerPatron(patron);
//
//        // Execute checkout
//        double result = checkout.checkoutBook(book, patron);
//
//        // Verify: Should return 2.0 for unavailable book
//        assertEquals(2.0, result, 0.01,
//                "Expected error code 2.0 for unavailable book for " + checkoutClass.getSimpleName());
//
//        // Verify: Patron should NOT have the book
//        assertFalse(patron.hasBookCheckedOut(book.getIsbn()),
//                "Patron should NOT have book in list for " + checkoutClass.getSimpleName());
//    }


    private void createCheeckedBooks(Patron patron, int count) {
        for (int i = 0; i < count; i++) {
            String isbn = "DUMMY-" + i;
            patron.addCheckedOutBook(isbn, LocalDate.now().plusDays(7));
        }
    }




    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T1 testPatronIsNull, EP 5.6, patron = null, book non-null, refOnly=false, availableCopies=5, expected 3.1")
    public void testPatronIsNull(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        Book book = new Book("ISBN-1", "Test Book", "Author", Book.BookType.FICTION, 5);
        checkout.addBook(book);

        int copiesBefore = book.getAvailableCopies();
        double result = checkout.checkoutBook(book, null);

        assertEquals(3.1, result, 0.01);
        assertEquals(copiesBefore, book.getAvailableCopies());
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T2 testSuspendedPatron, EP 5.1, patron STUDENT, suspended=true, fines=0, overdue=0, book not null, refOnly=false, availableCopies=5, not renewal, expected 3.0")
    public void testSuspendedPatron(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        Book book = new Book("ISBN-1", "Test Book", "Author", Book.BookType.FICTION, 5);
        Patron patron = new Patron("1", "Test Patron", "e@e.com", Patron.PatronType.STUDENT);
        patron.setAccountSuspended(true);

        checkout.addBook(book);
        checkout.registerPatron(patron);

        int copiesBefore = book.getAvailableCopies();
        int countBefore = patron.getCheckoutCount();

        double result = checkout.checkoutBook(book, patron);

        assertEquals(3.0, result, 0.01);
        assertEquals(copiesBefore, book.getAvailableCopies());
        assertEquals(countBefore, patron.getCheckoutCount());
        assertFalse(patron.hasBookCheckedOut(book.getIsbn()));
    }



    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T3 testSuspendedWithBookNull, EP 5.2, patron PUBLIC, suspended=true, book = null, expected 3.0")
    public void testSuspendedWithBookNull(Class<? extends Checkout> checkoutClass) throws Exception {

        checkout = createCheckout(checkoutClass);

        Patron patron = new Patron("1", "Test Patron", "e@e.com", Patron.PatronType.PUBLIC);
        patron.setAccountSuspended(true);
        checkout.registerPatron(patron);

        int countBefore = patron.getCheckoutCount();
        double result = checkout.checkoutBook(null, patron);

        assertEquals(3.0, result, 0.01);
        assertEquals(countBefore, patron.getCheckoutCount());
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T4 testFinesAtThreshold, BVA 3.3, patron STAFF, suspended=false, fines=10.00, overdue=0, book non-null, availableCopies=5, refOnly=false, expected 4.1")
    public void testFinesAtThreshold(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        Book book = new Book("ISBN-1", "Test Book", "Author", Book.BookType.FICTION, 5);
        Patron patron = new Patron("1", "Test Patron", "e@e.com", Patron.PatronType.STAFF);
        patron.addFine(10.00);

        checkout.addBook(book);
        checkout.registerPatron(patron);

        int copiesBefore = book.getAvailableCopies();
        int countBefore = patron.getCheckoutCount();

        double result = checkout.checkoutBook(book, patron);

        assertEquals(4.1, result, 0.01);
        assertEquals(copiesBefore, book.getAvailableCopies());
        assertEquals(countBefore, patron.getCheckoutCount());
        assertFalse(patron.hasBookCheckedOut(book.getIsbn()));
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T5 testFinesBelow, BVA 3.2, patron STAFF, fines=9.99, overdue=0, checkoutCount=0, book availableCopies=5, refOnly=false, not renewal, expected 0.0")
    public void testFinesBelow(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        Book book = new Book("ISBN-1", "Test Book", "Author", Book.BookType.FICTION, 5);
        Patron patron = new Patron("1", "Test Patron", "e@e.com", Patron.PatronType.STAFF);
        patron.addFine(9.99);

        checkout.addBook(book);
        checkout.registerPatron(patron);

        int copiesBefore = book.getAvailableCopies();
        int countBefore = patron.getCheckoutCount();

        double result = checkout.checkoutBook(book, patron);

        assertEquals(0.0, result, 0.01);
        assertEquals(copiesBefore - 1, book.getAvailableCopies());
        assertEquals(countBefore + 1, patron.getCheckoutCount());
        assertTrue(patron.hasBookCheckedOut(book.getIsbn()));

        LocalDate expectedDue = LocalDate.now().plusDays(patron.getLoanPeriodDays());
        assertEquals(expectedDue, patron.getCheckedOutBooks().get(book.getIsbn()));
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T6 testOverdueAtBoundary, BVA 1.3, patron STUDENT, overdue=3, fines=0, suspended=false, book availableCopies=5, refOnly=false, expected 4.0")
    public void testOverdueAtBoundary(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        Book book = new Book("ISBN-1", "Test Book", "Author", Book.BookType.FICTION, 5);
        Patron patron = new Patron("1", "Test Patron", "e@e.com", Patron.PatronType.STUDENT);
        patron.setOverdueCount(3);

        checkout.addBook(book);
        checkout.registerPatron(patron);

        int copiesBefore = book.getAvailableCopies();
        int countBefore = patron.getCheckoutCount();

        double result = checkout.checkoutBook(book, patron);

        assertEquals(4.0, result, 0.01);
        assertEquals(copiesBefore, book.getAvailableCopies());
        assertEquals(countBefore, patron.getCheckoutCount());
        assertFalse(patron.hasBookCheckedOut(book.getIsbn()));
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T7 testOverdueWarning, BVA 1.2, patron STUDENT, overdue=2, fines=0, suspended=false, checkoutCount=0, book availableCopies=5, refOnly=false, expected 1.0")
    public void testOverdueWarning(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        Book book = new Book("ISBN-1", "Test Book", "Author", Book.BookType.FICTION, 5);
        Patron patron = new Patron("1", "Test Patron", "e@e.com", Patron.PatronType.STUDENT);
        patron.setOverdueCount(2);

        checkout.addBook(book);
        checkout.registerPatron(patron);

        int copiesBefore = book.getAvailableCopies();
        int countBefore = patron.getCheckoutCount();

        double result = checkout.checkoutBook(book, patron);

        assertEquals(1.0, result, 0.01);
        assertEquals(copiesBefore - 1, book.getAvailableCopies());
        assertEquals(countBefore + 1, patron.getCheckoutCount());
        assertTrue(patron.hasBookCheckedOut(book.getIsbn()));

        LocalDate expectedDue = LocalDate.now().plusDays(patron.getLoanPeriodDays());
        assertEquals(expectedDue, patron.getCheckedOutBooks().get(book.getIsbn()));
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T8 testBookIsNullButEligiblePatron, EP 3.1, patron FACULTY, eligible, suspended=false, overdue=0, fines=0, book=null, expected 2.1")
    public void testBookIsNullButEligiblePatron(Class<? extends Checkout> checkoutClass) throws Exception {

        checkout = createCheckout(checkoutClass);

        Patron patron = new Patron("1", "Test Patron", "e@e.com", Patron.PatronType.FACULTY);
        checkout.registerPatron(patron);

        int countBefore = patron.getCheckoutCount();
        double result = checkout.checkoutBook(null, patron);

        assertEquals(2.1, result, 0.01);
        assertEquals(countBefore, patron.getCheckoutCount());
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T9 testBookIsReferenceOnly, EP 2.1, patron PUBLIC, eligible, book non-null, refOnly=true, availableCopies=10, expected 5.0")
    public void testBookIsReferenceOnly(Class<? extends Checkout> checkoutClass) throws Exception {

        checkout = createCheckout(checkoutClass);

        Book book = new Book("ISBN-1", "Ref Book", "Author", Book.BookType.REFERENCE, 10);
        Patron patron = new Patron("1", "Test Patron", "e@e.com", Patron.PatronType.PUBLIC);

        checkout.addBook(book);
        checkout.registerPatron(patron);

        int copiesBefore = book.getAvailableCopies();
        int countBefore = patron.getCheckoutCount();

        double result = checkout.checkoutBook(book, patron);

        assertEquals(5.0, result, 0.01);
        assertEquals(copiesBefore, book.getAvailableCopies());
        assertEquals(countBefore, patron.getCheckoutCount());
        assertFalse(patron.hasBookCheckedOut(book.getIsbn()));
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T10 testBookIsReferenceOnly, EP 1.1, patron STUDENT, eligible, book non-null, refOnly=false, availableCopies=0, not renewal, checkoutCount=0, expected 2.0")
    public void testUnavailableBook(Class<? extends Checkout> checkoutClass) throws Exception {


        checkout = createCheckout(checkoutClass);

        Book book = new Book("ISBN-1", "Test Book", "Author", Book.BookType.FICTION, 5);
        book.setAvailableCopies(0);

        Patron patron = new Patron("1", "Test Patron", "e@e.com", Patron.PatronType.STUDENT);

        checkout.addBook(book);
        checkout.registerPatron(patron);


        int copiesBefore = book.getAvailableCopies();
        int countBefore = patron.getCheckoutCount();

        double result = checkout.checkoutBook(book, patron);

        assertEquals(2.0, result, 0.01);
        assertEquals(copiesBefore, book.getAvailableCopies());
        assertEquals(countBefore, patron.getCheckoutCount());
        assertFalse(patron.hasBookCheckedOut(book.getIsbn()));
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T11 testCheckout, EP 1.2, patron PUBLIC, eligible, overdue=0, fines=0, checkoutCount=0, book availableCopies=2, refOnly=false, not renewal, expected 0.0")
    public void testCheckout(Class<? extends Checkout> checkoutClass) throws Exception {

        checkout = createCheckout(checkoutClass);

        Book book = new Book("ISBN-1", "Test Book", "Author", Book.BookType.FICTION, 2);
        Patron patron = new Patron("1", "Test Patron", "e@e.com", Patron.PatronType.PUBLIC);

        checkout.addBook(book);
        checkout.registerPatron(patron);

        int copiesBefore = book.getAvailableCopies();
        int countBefore = patron.getCheckoutCount();

        double result = checkout.checkoutBook(book, patron);

        assertEquals(0.0, result, 0.01);
        assertEquals(copiesBefore - 1, book.getAvailableCopies());
        assertEquals(countBefore + 1, patron.getCheckoutCount());
        assertTrue(patron.hasBookCheckedOut(book.getIsbn()));
        LocalDate expectedDue = LocalDate.now().plusDays(patron.getLoanPeriodDays());
        assertEquals(expectedDue, patron.getCheckedOutBooks().get(book.getIsbn()));
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T12 testRenewal, EP 6.1, EP 4.8, patron STUDENT, eligible, patron map already contains ISBN-1 with old dueDate, book availableCopies=0, refOnly=false, expected 0.1")
    public void testRenewal(Class<? extends Checkout> checkoutClass) throws Exception {


        checkout = createCheckout(checkoutClass);

        Book book = new Book("ISBN-1", "Test Book", "Author", Book.BookType.FICTION, 1);
        book.setAvailableCopies(0);

        Patron patron = new Patron("1", "Test Patron", "e@e.com", Patron.PatronType.STUDENT);
        LocalDate oldDue = LocalDate.now().minusDays(10);
        patron.addCheckedOutBook(book.getIsbn(), oldDue);

        checkout.addBook(book);
        checkout.registerPatron(patron);

        int copiesBefore = book.getAvailableCopies();
        int countBefore = patron.getCheckoutCount();

        double result = checkout.checkoutBook(book, patron);



        assertEquals(0.1, result, 0.01);
        assertEquals(copiesBefore, book.getAvailableCopies());
        assertEquals(countBefore, patron.getCheckoutCount());
        LocalDate expectedDue = LocalDate.now().plusDays(patron.getLoanPeriodDays());

        assertEquals(expectedDue, patron.getCheckedOutBooks().get(book.getIsbn()));
        assertNotEquals(oldDue, patron.getCheckedOutBooks().get(book.getIsbn()));
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T13 testRenewalBlockedByReferenceOnly, EP 6.5, patron STUDENT, eligible, patron already has ISBN-1 checked out, book refOnly=true, availableCopies=5, expected 5.0")
    public void testRenewalBlockedByReferenceOnly(Class<? extends Checkout> checkoutClass) throws Exception {

        checkout = createCheckout(checkoutClass);

        Book book = new Book("ISBN-1", "Ref Book", "Author", Book.BookType.REFERENCE, 5);
        Patron patron = new Patron("1", "Test Patron", "e@e.com", Patron.PatronType.STUDENT);

        LocalDate oldDue = LocalDate.now().plusDays(3);
        patron.addCheckedOutBook(book.getIsbn(), oldDue);

        checkout.addBook(book);
        checkout.registerPatron(patron);

        int copiesBefore = book.getAvailableCopies();
        int countBefore = patron.getCheckoutCount();

        double result = checkout.checkoutBook(book, patron);

        assertEquals(5.0, result, 0.01);
        assertEquals(copiesBefore, book.getAvailableCopies());
        assertEquals(countBefore, patron.getCheckoutCount());
        assertEquals(oldDue, patron.getCheckedOutBooks().get(book.getIsbn()));
    }



}