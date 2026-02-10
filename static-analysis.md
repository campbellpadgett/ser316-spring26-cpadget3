# Static Analysis Report - Assignment 4

**Student Name:** Campbell Padgett  
**ASU ID:** cpadget3
**Date:** 2/9

---

**What is the Gradle wrapper and why is it useful for team projects?**
- The wrapper means you can automate gradle in a CI pipeline. It also helps keep everyone using the same version of gradle.

## Part 1: GitHub Actions Setup

**Which branches fail on GitHub Actions? Explain why.**

1. **Branch:** ___________
   - **Status:** Passing / Failing
   - **Reason:**

2. **Branch:** ___________
   - **Status:** Passing / Failing
   - **Reason:**

---

## Part 2: Checkstyle Analysis

### Initial Results (StaticAnalysis branch - before fixes)

**Main source violations:** _____
**Test source violations:** _____

### After Fixing Issues

**Main source violations:** _____
**Violations fixed:** _____

---

## Part 3: SpotBugs Analysis

### Initial Results (StaticAnalysis branch - before fixes)

**Bugs found in main:** _____

### Bugs Fixed

1. **Bug:** [Brief description]
   - **File:** [Filename]
   - **Location:** Line ___
   - **Fix applied:**

2. **Bug:** [Brief description]
   - **File:** [Filename]
   - **Location:** Line ___
   - **Fix applied:**

3. **Bug:** [Brief description]
   - **File:** [Filename]
   - **Location:** Line ___
   - **Fix applied:**

---

## Part 4: Branch Comparison

### Checkstyle Comparison

| Branch | Main Violations | Test Violations | Total |
|--------|----------------|-----------------|-------|
| Blackbox | | | |
| Review | | | |
| StaticAnalysis (initial) | | | |
| StaticAnalysis (after fixes) | | | |

### SpotBugs Comparison

| Branch | Main Bugs | Test Bugs | Total |
|--------|-----------|-----------|-------|
| Blackbox | | | |
| Review | | | |
| StaticAnalysis (initial) | | | |
| StaticAnalysis (after fixes) | | | |

**Did Review branch improve code quality compared to Blackbox?**


---

## Part 5: Merging to Dev Branch

### Merge Strategy

**How did you merge Review and StaticAnalysis into Dev?**


**Merge conflicts encountered:** _____

### Dev Branch Quality After Merge

**Checkstyle violations:** _____
**SpotBugs issues:** _____

**Did quality improve or worsen? Explain:**


**Build successful:** [Yes/No]

---

## Part 6: Reflection

**Do you think your code got better through this process?**


**In what order would you use these quality practices in the future?**


**Most valuable lesson:**


