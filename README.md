# Transaction Account Balance
A simple Java program that calculates account net balance from list of transactions.

This program accepts input parameters from user, implement business logic and prints account balance and number of transactions that were considered to calculate the balance.

## Requirement
The goal of this program is to implement a system that analyses financial transaction records.
The system is initialised with an input file in CSV format containing a list of
transaction records.

Once initialised it is able to print the relative account balance (positive or
negative) in a given time frame.

The relative account balance is the sum of funds that were transferred to / from an
account in a given time frame, it does not account for funds that were in that account
prior to the timeframe.

Another requirement is that, if a transaction has a reversing transaction, this
transaction is omitted from the calculation, even if the reversing transaction is
outside the given time frame.

## Assumptions
1) Input file and records are all in a valid format
2) Transaction are recorded in order


## Technologies & Tools used
1. Java 1.11
2. Testing - JUnit 5
3. Build tool – Gradle 6.4

## Build
```
./gradlew clean build
```

## Run
```
./gradlew run --console=plain
```

Sample Run 
```
jaykishanparikh@Jaykishanp bank-transactions % ./gradlew run --console=plain

> Task :compileJava
Note: /bank-transactions/src/main/java/AccountTransactions.java uses or overrides a deprecated API.
Note: Recompile with -Xlint:deprecation for details.

> Task :processResources
> Task :classes

> Task :run
Input file path:
/bank-transactions/src/main/resources/transactions.csv
accountId:
ACC334455
from:
20/10/2018 12:00:00
to:
20/10/2018 19:00:00
Relative balance for the period is: -$10.00
Number of transactions included is: 2

BUILD SUCCESSFUL in 1m 4s
3 actionable tasks: 3 executed
```

## Testing
- JUnit 
This project covers all scenarios for unit test in /test directory.
