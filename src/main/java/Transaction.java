import java.time.LocalDate;

public class Transaction {
    Patron patron;
    Book book;
    LocalDate checkoutDate;
    LocalDate dueDate;
    LocalDate returnDate;

    Transaction(Patron patron, Book book, LocalDate checkoutDate, LocalDate dueDate) {
        this.patron = patron;
        this.book = book;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
        this.returnDate = null;
    }

    /**
     * Calculates the fine amount for an overdue book. Assume this javadoc is correct.
     *
     * Fine calculation rules:
     * - First 7 days overdue: $0.25 per day
     * - Days 8-14 overdue: $0.50 per day
     * - Days 15+ overdue: $1.00 per day
     * - REFERENCE and TEXTBOOK types: double the normal rate
     * - Maximum fine per book: $25.00
     *
     * Examples:
     * - 5 days overdue, FICTION: 5 * $0.25 = $1.25
     * - 10 days overdue, NONFICTION: (7 * $0.25) + (3 * $0.50) = $3.25
     * - 20 days overdue, TEXTBOOK: ((7*$0.25) + (7*$0.50) + (6*$1.00)) * 2 = $23.50
     * - 50 days overdue, FICTION: would be $41.75, but capped at $25.00
     *
     * @param numOfDays Number of days the book is overdue
     * @param bookType The type of book (affects fine rate)
     * @return Fine amount in dollars
     */
    public static double calculateFine(int numOfDays, Book.BookType bookType) {
        final int WEEK = 7;
        final double WEEK_FINE = 0.25;
        final int TWO_WEEKS = 14;
        final double TWO_WEEKS_FINE = 0.50;
        final double TWO_WEEKS_PLUS_FINE = 0.50;
        final double MAX_FINE_AMOUNT = 25.0;

        if (numOfDays <= 0) {
            return 0.0;
        }

        double fine = 0.0;

        // First 7 days: $0.25/day
        int days1 = Math.min(numOfDays, WEEK);
        fine += days1 * WEEK_FINE;

        // Days 8-14: $0.50/day
        if (numOfDays > WEEK) {
            int days2 = Math.min(numOfDays - WEEK, WEEK);
            fine += days2 * TWO_WEEKS_FINE;
        }

        // Days 15+: $1.00/day
        if (numOfDays > TWO_WEEKS) {
            int days3 = numOfDays - TWO_WEEKS;
            fine += days3 * TWO_WEEKS_PLUS_FINE;
        }

        // Double rate for REFERENCE and TEXTBOOK
        if (bookType == Book.BookType.REFERENCE || bookType == Book.BookType.TEXTBOOK) {
            fine *= 2.0;
        }

        // Cap at maximum fine amount
        return Math.min(fine, MAX_FINE_AMOUNT);
    }
}