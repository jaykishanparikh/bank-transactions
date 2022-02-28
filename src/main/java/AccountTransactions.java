import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


public class AccountTransactions {

    String filePath;
    String accountId;
    LocalDateTime from;
    LocalDateTime to;

    public static void main(String[] args) throws IOException {
        AccountTransactions accountTransactions = new AccountTransactions();
        accountTransactions.readInputParam();
        accountTransactions.processAccountTransactions();
    }

    /**
     * Create a filtered transactions map from the transactions file
     */
    List<Transaction> filterTransactionsMap(String filePath) throws IOException {
        List<Transaction> transactionList = new ArrayList<>();

        try (Reader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            for (CSVRecord record : csvParser) {
                Transaction transaction = fromCSVRecordToTransaction(record);
                transactionList.add(transaction);
                transactionList.removeIf(t -> t.getTransactionId().equals(transaction.getRelatedTransaction()));
            }
        }

        return transactionList.
                stream()
                .filter(c ->
                        (c.getFromAccountId().equals(accountId) || c.getToAccountId().equals(accountId)) &&
                                c.getCreatedAt().isAfter(from) &&
                                c.getCreatedAt().isBefore(to))
                .collect(Collectors.toList());
    }

    /**
     * calculate total balance amount for the account
     */
    void processAccountTransactions() throws IOException {
        List<Transaction> transactionList = filterTransactionsMap(filePath);
        printOutPut(calculateAccountBalance (transactionList), transactionList.size());
    }

    Long calculateAccountBalance (List<Transaction> transactionList) {
        AtomicReference<Long> totalBalance = new AtomicReference<>(0L);
        transactionList.stream().filter(t -> t.getToAccountId().equals(accountId)).forEach(
                transaction -> totalBalance.updateAndGet(v -> v + transaction.getAmount())
        );

        transactionList.stream().filter(t -> t.getFromAccountId().equals(accountId)).forEach(
                transaction -> totalBalance.updateAndGet(v -> v - transaction.getAmount())
        );
        return totalBalance.get();
    }

    private void readInputParam() {
        System.out.println("Input file path:");
        filePath = new Scanner(System.in).nextLine();

        System.out.println("accountId:");
        accountId = new Scanner(System.in).nextLine();

        System.out.println("from:");
        from = Utils.fromStringToLocalDateTime(new Scanner(System.in).nextLine());

        System.out.println("to:");
        to = Utils.fromStringToLocalDateTime(new Scanner(System.in).nextLine());
    }

    private Transaction fromCSVRecordToTransaction(CSVRecord record) {
        Transaction transaction = new Transaction(record.get("transactionId"),
                record.get("fromAccountId"),
                record.get("toAccountId"),
                Utils.fromStringToLocalDateTime(record.get("createdAt")),
                new BigDecimal(record.get("amount")).longValue(),
                record.get("transactionType")
        );
        try {
            transaction.setRelatedTransaction(record.get("relatedTransaction"));
        } catch (IllegalArgumentException e) {
            transaction.setRelatedTransaction(null);
        }
        return transaction;
    }

    private void printOutPut(Long totalBalance, int totalTransactions) {
        System.out.println("Relative balance for the period is: " + NumberFormat.getCurrencyInstance().format(totalBalance));
        System.out.println("Number of transactions included is: " + totalTransactions);
    }
}