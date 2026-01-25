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

| Partition ID | State                                     | Valid/Invalid  | Input Condition                                                                                                                  | Expected Return | Expected Behavior              |
|--------------|-------------------------------------------|----------------|----------------------------------------------------------------------------------------------------------------------------------|-----------------|--------------------------------|
| EP 4.1       | STUDENT                                   | Valid          | patron != null, eligible, not renewal, book available, below max limit                                                           | Success         | Book can be checked out        |
| EP 4.2       | FACULTY                                   | Valid          | patron != null, eligible, not renewal, book available, below max limit                                                           | Success         | Book can be checked out        |
| EP 4.3       | STAFF                                     | Valid          | patron != null, eligible, not renewal, book available, below max limit                                                           | Success         | Book can be checked out        |
| EP 4.4       | PUBLIC                                    | Valid          | patron != null, eligible, not renewal, book available, below max limit                                                           | Success         | Book can be checked out        |
| EP 4.5       | CHILD                                     | Valid          | patron != null, eligible, not renewal, book available, below max limit                                                           | Success         | Book can be checked out        |
| EP 4.6       | Any type with the max limit               | Invalid        | not renewal AND patron.getCheckoutCount() == patron.getMaxCheckoutLimit() AND other conditions allow checkout                    | 3.2             | Deny checkout due to max limit |
| EP 4.7       | Any type within 2 of max after checkout   | Valid          | not renewal AND book available AND afterCount >= maxLimit-2 AND overdueCount not in 1–2                                          | 1.1             | Book can be checked out        |
| EP 4.8       | Any type renewal                          | Valid          | patron already has this ISBN checked out (renewal) AND eligibility passed AND book not null AND not reference-only               | 0.1             | Book can be checked out .      |

Patron suspension

| Partition ID | State                             | Valid/Invalid | Input Condition                                                                                      | Expected Return | Expected Behavior                   |
| ------------ |-----------------------------------|---------------|------------------------------------------------------------------------------------------------------|-----------------|-------------------------------------|
| EP S.1       | Patron is suspended               | Invalid       | patron.isAccountSuspended() == true AND (regardless of book input / availability / renewal / limits) | **3.0**         | Book can't be checkout.             |
| EP S.2       | Suspended AND book is null        | Invalid       | suspended AND book == null                                                                           | **3.0**         | Book can't be checkout.             |
| EP S.3       | Suspended AND book unavailable    | Invalid       | suspended AND book.getAvailableCopies() <= 0                                                         | **3.0**         | Book can't be checkout.             |
| EP S.4       | Suspended AND reference-only book | Invalid       | suspended AND book.isReferenceOnly() == true                                                         | **3.0**         | Book can't be checkout.             |
| EP S.5       | Suspended AND renewal case        | Invalid       | suspended AND patron already has the ISBN checked out                                                | **3.0**         | Book can't be checkout.             |
| EP S.6       | Patron is null                    | Invalid       | patron == null                                                                                       | **3.1**         | Book can't be checkout.             |

---

## Part 2: Boundary Value Analysis (BVA)

Important BVA cases may overlap with EP. That is OK. You can reference all relevant EP/BVA coverage in Part 3.

### Example BVA Table: Overdue Count (Threshold: 3)

| Test ID | Boundary | Input Value | Expected Return | Rationale |
|---------|----------|-------------|-----------------|-----------|
| BVA 1.1 | Below | overdueCount = 0 | Success (depends on other setup) | Below warning threshold |
| BVA 1.2 | Warning High | overdueCount = 2 | 1.0 | Just below reject threshold |
| BVA 1.3 | At | overdueCount = 3 | 4.0 | At rejection boundary |
| BVA 1.4 | Above | overdueCount = 4 | 4.0 | Above rejection boundary |

---

### Your BVA Tables (add more as needed)

| Test ID | Boundary | Input Value | Expected Return | Rationale |
|---------|----------|-------------|-----------------|-----------|
| BVA ___ | | | | |

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

