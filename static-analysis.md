# Static Analysis Report - Assignment 4

**Student Name:** Campbell Padgett  
**ASU ID:** cpadget3
**Date:** 2/9

---

**What is the Gradle wrapper and why is it useful for team projects?**
- The wrapper means you can automate gradle in a CI pipeline. It also helps keep everyone using the same version of gradle.

## Part 1: GitHub Actions Setup

**Which branches fail on GitHub Actions? Explain why.**

1. **Branch:** StaticAnalysis
   - **Status:** Success
   - **Reason:** Has gradle wrapper and checkstyle config file

2. **Branch:** Review
   - **Status:** Failing
   - **Reason:** does not have Gradle wrapper or checkstyle config folder

3. **Branch:** Whitebox
   - **Status:** Failing
   - **Reason:** does not have Gradle wrapper or checkstyle config folder

4. **Branch:** Blackbox
   - **Status:** Failing
   - **Reason:** Has gradle wrapper but no checkstyle config file

5. **Branch:** main
   - **Status:** Failing
   - **Reason:** does not have Gradle wrapper or checkstyle config folder

---

## Part 2: Checkstyle Analysis

### Initial Results (StaticAnalysis branch - before fixes)

**Main source violations:** 102 (31 in checkout)
**Test source violations:** 116

### After Fixing Issues

**Main source violations:** 80 (9 in checkout)
**Violations fixed:** 22

---

## Part 3: SpotBugs Analysis

### Initial Results (StaticAnalysis branch - before fixes)

**Bugs found in main:** 7

### Bugs Fixed

1. **Bug:** Comparison of String objects using ==
   - **File:** Checkout.java
   - **Location:** Line 330
   - **Fix applied:** used .equals()

2. **Bug:** checking for object equality with Comparison of String objects using ==
   - **File:** Checkout.java
   - **Location:** Line 204-213
   - **Fix applied:** used Objects.isNull()

3. **Bug:** returns a private field
   - **File:** Checkout.java
   - **Location:** Line 424
   - **Fix applied:** made copy of field and returned that instead

4. **Bug:** returns a private field
   - **File:** Checkout.java
   - **Location:** Line 428
   - **Fix applied:** made copy of field and returned that instead

---

## Part 4: Branch Comparison

### Checkstyle Comparison

| Branch                       | Main Violations | Test Violations | Total |
|------------------------------|-----------------|-----------------|-------|
| Blackbox                     |                 | 141             | 141   |
| Review                       |                 | 141             | 141   |
| StaticAnalysis (initial)     | 102             | 116             | 218   |  
| StaticAnalysis (after fixes) | 80              | 116             | 196   |

### SpotBugs Comparison

| Branch                       | Main Bugs | Test Bugs | Total |
|------------------------------|-----------|-----------|-------|
| Blackbox                     | 7         |           | 7     |
| Review                       | 7         |           | 7     |
| StaticAnalysis (initial)     | 7         |           | 7     |
| StaticAnalysis (after fixes) | 2         |           | 2     |

**Did Review branch improve code quality compared to Blackbox?**
Yes, it made it more maintainable, especially when dealing with the magic numbers and the 
String and object equality checks. Now, the readability is greatly improved and the checks for null
and equality can be relied on. 

---

## Part 5: Merging to Dev Branch

### Merge Strategy

**How did you merge Review and StaticAnalysis into Dev?**
I started with Review and had only one conflict. Then I merged Static Analysis and kept all the changes made there for dev to now have


**Merge conflicts encountered:** 8

### Dev Branch Quality After Merge

**Checkstyle violations:** 4
**SpotBugs issues:** 4

**Did quality improve or worsen? Explain:**

With respect to Checkout.java, it improved, through there were two new instances of there being no javadoc after the merge. 

**Build successful:** [Yes/No]
Yes
---

## Part 6: Reflection

**Do you think your code got better through this process?**
Yes. After reading through the memoranda project files, I now have a greater appreciation for javadoc comments and for style consistency


**In what order would you use these quality practices in the future?**
I would have checked first for error and warnings first, then move on the style consistency

**Most valuable lesson:**
I would say it's that testing can require the same level of effort as the actual writing on the code 
and that writing with a detailed description in javadocs can save you a lot of time

