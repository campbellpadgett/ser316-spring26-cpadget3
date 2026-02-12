import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Sample White-Box tests for the Checkout system.
 * This class demonstrates how to write white-box tests using:
 * - Control Flow Graph (CFG) analysis
 * - Statement coverage
 * - Branch coverage
 * - Path coverage
 *
 * White-box testing focuses on testing the IMPLEMENTATION by
 * examining the code structure and ensuring all paths are tested.
 */
public class CheckoutWhiteBoxSample {

    private Checkout checkout;

    @BeforeEach
    public void setUp() {
        checkout = new Checkout();
    }

    @Test
    @DisplayName("WB Test: countBooksByType - null type branch")
    public void testCountBooksByType_NullType() {
        // Branch: type == null → TRUE
        int result = checkout.countBooksByType(null, false);
        assertEquals(0, result, "Should return 0 for null type");
    }


    @Test
    @DisplayName("WB Test 2: countBooksByType (358, 367, 374)")
    public void testCountBooksByType_OnlyAvailableTrue_IncrementsWhenAvailableAndTypeMatches()
    {
        // Arrange: 2 FICTION books, one available and one unavailable; plus a non-matching available book
        Book fictionAvailable = new Book("1111111111", "Book 1", "Author 1", Book.BookType.FICTION, 2);

        Book fictionUnavailable = new Book("2222222222", "Book 2", "Author 2", Book.BookType.FICTION, 1);
        fictionUnavailable.setAvailableCopies(0); // make unavailable, so b.isAvailable() == false

        Book nonFictionAvailable = new Book("3333333333", "Book 3 - nonfiction", "Author 3", Book.BookType.NONFICTION, 1);

        checkout.addBook(fictionAvailable);
        checkout.addBook(fictionUnavailable);
        checkout.addBook(nonFictionAvailable);

        // Act: onlyAvailable=true means we only count matching type with b.isAvailable()==true
        int result = checkout.countBooksByType(Book.BookType.FICTION, true);

        // Assert: only the available FICTION book increments looped
        assertEquals(1, result, "onlyAvailable = true");
    }

    @Test
    @DisplayName("WB Test 3: countBooksByType (358, 365, 379)")
    public void testCountBooksByType_TypeMismatch_DoesNotIncrement()
    {
        // Arrange: only NONFICTION books exist
        Book nonFiction1 = new Book("1111111111", "Book 1", "Author 1", Book.BookType.NONFICTION, 1);
        Book nonFiction2 = new Book("2222222222", "Book 2", "Author 2", Book.BookType.NONFICTION, 3);

        checkout.addBook(nonFiction1);
        checkout.addBook(nonFiction2);

        // Act: request FICTION; no books match b.getType() == type
        int result = checkout.countBooksByType(Book.BookType.FICTION, true);

        // Assert: looped never increments
        assertEquals(0, result, "should be 0");
    }


    @Test
    @DisplayName("WB Test 4: isValidISBN - null and empty check")
    public void testIsValidISBN_NullAndEmpty_False()
    {
        assertFalse(checkout.isValidISBN(null));
        assertFalse(checkout.isValidISBN(""));
    }

    @Test
    @DisplayName("WB Test 5: isValidISBN - contains letters check")
    public void testIsValidISBN_NonDigits_False()
    {
        assertFalse(checkout.isValidISBN("978-0-12A4-5678-9"));
        assertFalse(checkout.isValidISBN("01234!6789"));
    }

    @Test
    @DisplayName("WBTest 6: calculateFine - numOfDays check")
    public void testCalculateFine_NonPositiveDays_Zero()
    {
        assertEquals(0.0, checkout.calculateFine(0, Book.BookType.FICTION), 0.0001);
        assertEquals(0.0, checkout.calculateFine(-5, Book.BookType.FICTION), 0.0001);
    }

    @Test
    @DisplayName("WB Test 7: calculateFine - rates for fiction and nonfition check")
    public void testCalculateFine_Tiers()
    {
        assertEquals(1.25, checkout.calculateFine(5, Book.BookType.FICTION), 0.0001);
        assertEquals(3.25, checkout.calculateFine(10, Book.BookType.NONFICTION), 0.0001);
    }

    @Test
    @DisplayName("WB Test 8: calculateFine - 2x rate cehck")
    public void testCalculateFine_DoubleRateAndCap()
    {
        assertEquals(22.50, checkout.calculateFine(20, Book.BookType.TEXTBOOK), 0.0001);
        assertEquals(25.0, checkout.calculateFine(200, Book.BookType.FICTION), 0.0001);
    }

}
