# White Box Testing Report - Assignment 3

**Student Name:** Campbell Padgett
**ASU ID:** cpadget3
**Date:** 2/4

---

## Part 1: Control Flow Graph for countBooksByType()

### Graph Description

Draw or describe your control flow graph here. Include:
- Node numbers and what they represent
- Edges showing control flow
- Conditions at decision points

**You can hand-draw and insert an image, or describe it in text format.**
![img.png](img.png)

### Node Coverage Sequences

List the sequences needed for complete node coverage:

**Sequence 1:**
- 351-351
- Ensure that if type is null, we get 0
- **Test case:**


**Sequence 2:**
- 358, 367, 374
- Ensure that the looped is increased if the book is avilable 
- **Test case:**


**Sequence 3:**
- 358, 365, 379 
- Ensure that the looped is returned without increasing is the book type isn't matched
- **Test case:**


### Edge Coverage Sequences

List the sequences needed for complete edge coverage:

**Sequence 1:**
- **Edges covered:**
- **Test case:**


**Sequence 2:**
- **Edges covered:**
- **Test case:**


---

## Part 2: Code Coverage with JaCoCo

### Initial Coverage for Checkout.java

**Before adding tests:**
- **Line Coverage:** ___%
- **Branch Coverage:** ___%

### Coverage for countBooksByType()

**Before additional tests:**
- **Branch Coverage:** ___%

**After reaching 80% branch coverage:**
- **Branch Coverage:** ___%
- **Tests added:**

### Final Overall Coverage

- **Line Coverage:** ___%
- **Branch Coverage:** ___%

---

## Part 3: checkoutBook() Implementation

### Test-Driven Development Process

**Number of tests from BlackBox assignment:** ___

**Implementation challenges:**
1.
2.

**All tests passing:** [Yes/No]

---

## Part 4: Reflection

**How did white-box testing differ from black-box testing?**

**Which approach do you find more effective? Why?**

**Would you prefer TDD or implementation first test later? Why?**
