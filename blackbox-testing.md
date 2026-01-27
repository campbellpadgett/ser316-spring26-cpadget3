# Black Box Testing Report - Assignment 2

**Student Name:** Campbell Padgett
**ASU ID:** cpadget3
**Date:** 1/24

---

## Part 1: Equivalence Partitioning (EP)

Identify equivalence partitions for the `checkoutBook(Book book, Patron patron)` method based on the specification (JavaDoc).

Create **multiple tables**, one per partition category (e.g., book state, patron state, renewal, limits, etc.).

Do **not** put everything into one table.

**Column Explanations:**
- **Partition ID**: Unique identifier (e.g., EP 1.1, EP 2.1)
- **State**: The specific state/value for this partition (e.g., "Unavailable", "Available")
- **Valid/Invalid**: Whether this partition represents valid or invalid input
- **Input Condition**: Precise condition that defines this partition
- **Expected Return**: What return code you expect
- **Expected Behavior**: What should happen

### Example EP Table: Book Availability

| Partition ID | State | Valid/Invalid | Input Condition | Expected Return | Expected Behavior |
|--------------|-------|---------------|----------------|-----------------|------------------|
| EP 1.1 | Unavailable (0 copies) | Invalid | availableCopies == 0 AND other conditions allow checkout | 2.0 | No copies to checkout |
| EP 1.2 | Available (1+ copies) | Valid | availableCopies > 0 AND other conditions allow checkout | Success | Book can be checked out |

**Example test cases:** `testBookAvailable()`, `testUnavailableBook()`

---

### Your EP Tables (add as many as needed)

Book Availability

| Partition ID | State | Valid/Invalid | Input Condition | Expected Return | Expected Behavior |
|--------------|-------|---------------|----------------|-----------------|------------------|
| EP 1.1       | Unavailable (0 copies) | Invalid | availableCopies == 0 AND other conditions allow checkout | 2.0 | No copies to checkout |
| EP 1.2       | Available (1+ copies) | Valid | availableCopies > 0 AND other conditions allow checkout | Success | Book can be checked out |

Book Type

| Partition ID | State                 | Valid/Invalid | Input Condition                                                                       | Expected Return | Expected Behavior |
|--------------|-----------------------|---------------|---------------------------------------------------------------------------------------|-----------------|------------------|
| EP 2.1       | referenceOnly = true  | Invalid       | availableCopies == 0 due to reference only = True AND other conditions allow checkout | 5.0             | No copies to checkout |
| EP 2.2       | referenceOnly = false | Valid         | availableCopies == totalCopies AND other conditions allow checkout                    | Success         | Book can be checked out |

Book Null

| Partition ID | State              | Valid/Invalid | Input Condition           | Expected Return                          | Expected Behavior |
|--------------|--------------------|---------------|---------------------------|------------------------------------------|------------------|
| EP 3.1       | Book() is NULL     | Invalid       | Book().isNull() == True   | 2.1                                      | No copies to checkout |
| EP 3.2       | Book() is not NULL | Valid         | Book().isNull() == False  | Success  other conditions allow checkout | Book can be checked out |

Patron Type

| Partition ID | State                                     | Valid/Invalid  | Input Condition                                                                                                    | Expected Return | Expected Behavior              |
|--------------|-------------------------------------------|----------------|--------------------------------------------------------------------------------------------------------------------|-----------------|--------------------------------|
| EP 4.1       | STUDENT                                   | Valid          | patron != null, eligible, not renewal, book available, below max limit                                             | Success         | Book can be checked out        |
| EP 4.2       | FACULTY                                   | Valid          | Same as EP 4.2                                                                                                     | Success         | Book can be checked out        |
| EP 4.3       | STAFF                                     | Valid          | Same as EP 4.2                                                                                                     | Success         | Book can be checked out        |
| EP 4.4       | PUBLIC                                    | Valid          | Same as EP 4.2                                                                                                     | Success         | Book can be checked out        |
| EP 4.5       | CHILD                                     | Valid          | Same as EP 4.2                                                                                                     | Success         | Book can be checked out        |
| EP 4.6       | Any type with the max limit               | Invalid        | not renewal AND patron.getCheckoutCount() == patron.getMaxCheckoutLimit() AND other conditions allow checkout      | 3.2             | Deny checkout due to max limit |
| EP 4.7       | Any type within 2 of max after checkout   | Valid          | not renewal AND book available AND afterCount >= maxLimit-2 AND overdueCount not in 1–2                            | 1.1             | Book can be checked out        |
| EP 4.8       | Any type renewal                          | Valid          | patron already has this ISBN checked out (renewal) AND eligibility passed AND book not null AND not reference-only | 0.1             | Book can be checked out .      |

Patron suspension

| Partition ID | State                             | Valid/Invalid | Input Condition                                                                                      | Expected Return | Expected Behavior          |
|--------------|-----------------------------------|---------------|------------------------------------------------------------------------------------------------------|-----------------|----------------------------|
| EP 5.1       | Patron is suspended               | Invalid       | patron.isAccountSuspended() == true AND (regardless of book input / availability / renewal / limits) | 3.0             | Book can't be checked out. |
| EP 5.2       | Suspended AND book is null        | Invalid       | suspended AND book == null                                                                           | 3.0             | Book can't be checked out. |
| EP 5.3       | Suspended AND book unavailable    | Invalid       | suspended AND book.getAvailableCopies() <= 0                                                         | 3.0             | Book can't be checked out. |
| EP 5.4       | Suspended AND reference-only book | Invalid       | suspended AND book.isReferenceOnly() == true                                                         | 3.0             | Book can't be checked out. |
| EP 5.5       | Suspended AND renewal case        | Invalid       | suspended AND patron already has the ISBN checked out                                                | 3.0             | Book can't be checked out. |
| EP 5.6       | Patron is null                    | Invalid       | patron == null                                                                                       | 3.1             | Book can't be checked out. |

Renewals

| Partition ID | State                                                    | Valid/Invalid | Input Condition                                                                                                          | Expected Return | Expected Behavior                                                           |
|--------------|----------------------------------------------------------|---------------|--------------------------------------------------------------------------------------------------------------------------|-----------------|-----------------------------------------------------------------------------|
| EP 6.1       | Renewal with book being available                        | Valid         | patron != null AND book != null AND book.isReferenceOnly() == false AND patron.hasBookCheckedOut(book.getIsbn()) == true | 0.1             | Book can be checked out, renewed                                            |
| EP 6.2       | Renewal with book being unavailable (0 copies)           | Valid         | Same as EP 6.1 AND book.getAvailableCopies() <= 0                                                                        | 0.1             | Book can be checked out, renewed                                            |
| EP 6.3       | Renewal when patron is at max checkout                   | Valid         | Same as EP 6.1 AND patron.getCheckoutCount() == patron.getMaxCheckoutLimit()                                             | 0.1             | Book can be checked out, renewed                                            |
| EP 6.4       | Renewal attempt but book == null                         | Invalid       | patron != null AND eligible AND book == null                                                                             | 2.1             | Renewal cannot be evaluated without a book/ISBN, Book can't be checked out. |
| EP 6.5       | Renewal attempt with reference-only book                 | Invalid       | Same as EP 6.4 AND book.isReferenceOnly()==true AND patron has ISBN checked out                                          | 5.0             | Book can't be checked out.                                                  |
| EP 6.6       | Renewal prevented due to patron ineligible (suspended)   | Invalid       | patron != null AND book != null AND patron has ISBN checked out AND validatePatronEligibility(patron) == 3.0             | 3.0             | Book can't be checked out.                                                  |
| EP 6.7       | Renewal prevented due to patron ineligible (≥3 overdue)  | Invalid       | Same as EP 6.6 AND validatePatronEligibility(patron) == 4.0                                                              | 4.0             | Book can't be checked out.                                                  |
| EP 6.8       | Renewal prevented due to patron ineligible (fines ≥ $10) | Invalid       | Same as EP 6.6 AND validatePatronEligibility(patron) == 4.1                                                              | 4.1             | Book can't be checked out.                                                  |
| EP 6.9       | Patron is null                                           | Invalid       | patron == null                                                                                                           | 3.1             | Book can't be checked out.                                                  |


---

## Part 2: Boundary Value Analysis (BVA)

Important BVA cases may overlap with EP. That is OK. You can reference all relevant EP/BVA coverage in Part 3.

### Example BVA Table: Overdue Count (Threshold: 3)

| Test ID   | Boundary     | Input Value      | Expected Return                                    | Rationale                   |
|-----------|--------------|------------------|----------------------------------------------------|-----------------------------|
| BVA 1.1   | Below        | overdueCount = 0 | Success (depends on other setup)                   | Below warning threshold     |
| BVA 1.2   | Warning High | overdueCount = 2 | 1.0                                                | Just below reject threshold |
| BVA 1.3   | At           | overdueCount = 3 | 4.0                                                | At rejection boundary       |
| BVA 1.4   | Above        | overdueCount = 4 | 4.0                                                | Above rejection boundary    |

---

### Your BVA Tables (add more as needed)

Checkout Values

| Test ID   | Boundary | Input Value                  | Expected Return | Rationale                   |
|-----------|----------|------------------------------|-----------------|-----------------------------|
| BVA 1.1   | Below    | checkoutCount = maxLimit - 4 | 0.0             | Below warning threshold     |
| BVA 1.2   | Warning  | checkoutCount = maxLimit - 3 | 1.1             | Warning threshold           |
| BVA 1.3   | Warning  | checkoutCount = maxLimit - 2 | 1.1             | Just below reject threshold |
| BVA 1.4   | Below    | checkoutCount = maxLimit - 1 | 1.1             | Just below reject threshold |
| BVA 1.5   | At       | checkoutCount = maxLimit     | 3.2             | At rejection boundary       |
| BVA 1.6   | Above    | checkoutCount = maxLimit + 1 | 3.2             | Above rejection boundary    |

Loan Period Values

| Test ID | Boundary                           | Input Value                              | Input Value   | Expected Return | Rationale                                                                         |
|---------|------------------------------------|------------------------------------------|---------------|-----------------|-----------------------------------------------------------------------------------|
| BVA 2.1 | Lowest (Child)                     | CHILD, normal checkout                   | 14            | Success         | Lowest loan period threshold for that patron type                                 |
| BVA 2.2 | Lowest (Public)                    | PUBLIC, normal checkout                  | 21            | Success         | Lowest loan period threshold for that patron type                                 |
| BVA 2.3 | Lowest (Student)                   | STUDENT, normal checkout                 | 30            | Success         | Lowest loan period threshold for that patron type                                 |
| BVA 2.4 | Lowest (Staff)                     | STAFF, normal checkout                   | 45            | Success         | Lowest loan period threshold for that patron type                                 |
| BVA 2.5 | Max amount tital, lowest (faculty) | FACULTY, normal checkout                 | 60            | Success         | Lowest loan period threshold for that patron type but highest for loaning overall |
| BVA 2.6 | Renewal boundary (Lowest)          | CHILD, renewal (patron already has ISBN) | 14            | 0.1             | Lowest loan period threshold for renewal                                          |
| BVA 2.7 | Renewal boundary (max)             | FACULTY, renewal                         | 60            | 0.1             | Highest loan period threshold for renewal                                         |

Fee Values

| Test ID | Boundary                           | Input Value                   | Expected Return | Rationale                                               |
|---------|------------------------------------|-------------------------------|-----------------|---------------------------------------------------------|
| BVA 3.1 | Below (lowest)                     | 0                             | Success         | Below the fine threshold                                |
| BVA 3.2 | Below                              | 9.99                          | Success         | Below the rejection threshold                           |
| BVA 3.3 | At                                 | 10.00                         | 4.1             | At rejection threshold                                  |
| BVA 3.4 | Above                              | 10.01                         | 4.1             | Above the threshold                                     |
| BVA 3.5 | Above (highest)                    | 25.00                         | 4.1             | Above the threshold at the highest rate withe rejection |
| BVA 3.6 | At threshold with renewal attempts | 10.00 and book is checked out | 4.1             | Highest renewal and fee                                 |
| BVA 3.7 | At threshold with book = null      | 10.00 and book is null        | 4.1             | Edge case with fee                                      |

---

## Part 3: Test Cases Designed

List at least **20** test cases you designed based on your EP/BVA analysis.

Each test case should include:
- EP/BVA coverage
- specific inputs / setup
- expected return code
- expected **observable state changes** (if any)

> Do not test console output.

### Test Case Table
At least some of your tests should verify observable state changes, not just return values.

**Checkout0-3 Columns:** Mark each implementation as Pass (✓) or Fail (✗) for this test case. This helps you track which implementations have bugs and will be useful for Part 4 analysis.

| Test ID Name | EP/BVA | Input Description | Expected Return | Expected State Changes | Checkout0 | Checkout1 | Checkout2 | Checkout3 |
|--------------|--------|-------------------|-----------------|------------------------|-----------|-----------|-----------|-----------|
| T1 testUnavailableBook | EP 1.1 | Book unavailable (0 copies), eligible patron | 2.0 | No state change | ✓ | ✓ | ✗ | ✓ |
| T2 testBookAvailable | EP 1.2 | Book available (1+ copies), eligible patron, no warnings normal checkout | 0.0 | Patron map updated; copies of book change | ✗ | ✗ | ✓ | ✓ |

(Add rows until you have at least 20.)

---

## Part 4: Bug Analysis

### Easter Eggs Found
List any easter egg messages you observed:
- 
- 

### Implementation Results

| Implementation | Bugs Found (count) |
|----------------|---------------------|
| Checkout0      | |
| Checkout1      | |
| Checkout2      | |
| Checkout3      | |

### Bugs Discovered
List distinct bugs you identified for each implementation. Each bug must cite at least one test case that revealed it.

**Checkout0:**
- Bug 1: [Brief description] — Revealed by: [Test ID]

**Checkout1:**
- Bug 1: [Brief description] — Revealed by: [Test ID]

**Checkout2:**
- Bug 1: [Brief description] — Revealed by: [Test ID]

**Checkout3:**
- Bug 1: [Brief description] — Revealed by: [Test ID]

### Comparative Analysis
Compare the four implementations:
- Which bugs are most critical (cause the worst failures)?
- Which implementation would you use if you had to choose?
- Why? Justify your choice considering bug severity and frequency.

---

## Part 5: Reflection

**Which testing technique was most effective for finding bugs?**

**What was the most challenging aspect of this assignment?**

**How did you decide on your EP and BVA?**

**Describe one test where checking only the return value would NOT have been sufficient to detect a bug.**

