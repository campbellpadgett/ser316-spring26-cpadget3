# Code Review Checklist

**Reviewer Name:** Campbell Padgett
**Date:** 1/24
**Branch:** Blackbox

## Instructions
Review ALL source files (in main not test) in the project and identify defects using the categories below. Log at least 5 defects total:
- At least 1 from CS (Coding Standards)
- At least 1 from CG (Code Quality/General)
- At least 1 from FD (Functional Defects)
- Remaining can be from any category

## Review Categories

- **CS**: Coding Standards (naming conventions, formatting, style violations)
- **CG**: Code Quality/General (design issues, code smells, maintainability)
- **FD**: Functional Defects (logic errors, incorrect behavior, bugs)
- **MD**: Miscellaneous (documentation, comments, other issues)

## Defect Log

| Defect ID | File          | Line(s) | Category | Description                                                                                                      | Severity |
|-----------|---------------|---------|----------|------------------------------------------------------------------------------------------------------------------|----------|
| 1 | Book.java     | 54-100  | CS       | Inconsistent use of "this" keyword in class getters and setup function (Coding Standards)                        | Low      |
| 2 | Book.java     | 121-133 | CG       | Overly ocmplicated equality check in equals()                                                                    | Medium   |
| 3 | Checkout.java | 241-247 | CG       | Use case of == instead of equals()                                                                               | Medium   |
| 4 | Patron.java   | 161-164 | FD       | payFine() doesn't check for values > 0                                                                           | High     |
| 5 | Book.java     | 105-110 | FD       | returnBook() arbitrairily sets a limit to the number of books that can be returned to 100 instead of totalCopies | High     |
| 6 |               |         |          |                                                                                                                  |          |
| 7 |               |         |          |                                                                                                                  |          |
| 8 |               |         |          |                                                                                                                  |          |
| 9 |               |         |          |                                                                                                                  |          |
| 10 |               |         |          |                                                                                                                  |          |

**Severity Levels:**
- **Critical**: Causes system failure, data corruption, or security issues
- **High**: Major functional defect or significant quality issue
- **Medium**: Moderate issue affecting maintainability or minor functional problem
- **Low**: Minor style issue or cosmetic problem

## Example Entry

| Defect ID | File          | Line(s) | Category | Description                                | Severity |
|-----------|---------------|---------|----------|--------------------------------------------|----------|
| 1 | Checkout.java | 17      | CS       | Variable bookList misleading - Map not List | Medium |
| 2 | Book.java     | 107     | FD       | Magic number 100 should be totalCopies      | High |

## Notes
- Be specific with line numbers
- Provide clear, actionable descriptions
- Consider: readability, maintainability, correctness, performance, security
- Focus on issues that impact code quality or functionality
