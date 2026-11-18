# Banking API CA3 – Eclipse procedure

## Requirements
- JDK 17 or later
- Eclipse IDE with Maven (m2e) support
- Internet connection for Maven dependencies
- Postman for API-tool testing

## Import
1. Extract this ZIP.
2. In Eclipse choose File → Import → Maven → Existing Maven Projects.
3. Browse to the extracted `Banking_API_Eclipse_Project` folder.
4. Select `pom.xml`, then Finish.
5. Right-click the project → Maven → Update Project. Select Force Update if needed.
6. Check Window → Preferences → Java → Installed JREs and select JDK 17+.

## Run application
1. Open `src/main/java` → `com.example.bankingapi`.
2. Right-click `BankingApiApplication.java` → Run As → Java Application (or Spring Boot App).
3. Wait for `Tomcat started on port 8080` and `Started BankingApiApplication` in Console.
4. Keep it running while using Postman.

## Run JUnit tests in Eclipse
1. Open `src/test/java` → `com.example.bankingapi` → `BankingApiIntegrationTest.java`.
2. Right-click the file → Run As → JUnit Test.
3. Green bar means all tests passed. Red means one or more tests failed.
4. Double-click a failed test in the JUnit view to inspect the error.
5. To run one test, right-click inside its method and select Run As → JUnit Test.
6. Alternative: right-click project → Run As → Maven test.

JUnit tests use Spring MockMvc and do not need the server started separately.

## Postman API requests
Base URL: http://localhost:8080

1. POST /api/accounts with JSON `{"name":"Rahul"}`. Expected 200. Copy returned id.
2. POST /api/accounts/{id}/deposit with `{"amount":5000}`. Expected 200 and balance 5000.
3. POST /api/accounts/{id}/withdraw with `{"amount":1000}`. Expected 200 and balance 4000.
4. GET /api/accounts/{id}/balance. Expected 200 and body 4000.

Negative cases:
- Deposit `{"amount":-500}` → 400
- Deposit `{"amount":0}` → 400
- Withdraw `{"amount":10000}` from a zero-balance account → 400
- GET `/api/accounts/999999/balance` → 404
- Deposit `{}` → 400
- Create `{"name":"   "}` → 400

## Test case list
TC01 create account; TC02 positive deposit; TC03 withdrawal; TC04 balance retrieval;
TC05 negative deposit; TC06 zero deposit; TC07 insufficient funds; TC08 unknown account;
TC09 missing amount; TC10 blank account name.

## Notes
- Accounts are stored in memory and are cleared when the application restarts.
- This is a classroom demonstration, not a real banking application.
- Record actual test results and timings only after execution.
