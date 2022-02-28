import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AccountTransactionsTest {

    private final AccountTransactions testInstance = new AccountTransactions();

    private List<Transaction> testTransactionMap;
    private URL fileUrl;


    @BeforeEach
    void setUp(){
        String testFileName = "test_transactions.csv";
        fileUrl = getClass().getClassLoader().getResource(testFileName);
        testInstance.filePath = getClass().getClassLoader().getResource(testFileName).toString();
        testInstance.accountId = "ACC334455";
        testInstance.from = Utils.fromStringToLocalDateTime("20/10/2018 12:00:00");
        testInstance.to = Utils.fromStringToLocalDateTime("20/10/2018 19:00:00");
    }

    @Test
    void testInputFile() throws IOException {
        assertNotNull(fileUrl);

        testTransactionMap = testInstance.filterTransactionsMap(fileUrl.getPath());
        assertNotNull(testTransactionMap);
        assertEquals(2, testTransactionMap.size());

        assertEquals(testTransactionMap.get(0).getTransactionId(), "TX10001");
        assertEquals(testTransactionMap.get(0).getAmount(), 25);
        assertEquals(testTransactionMap.get(1).getTransactionId(), "TX10003");
        assertEquals(testTransactionMap.get(1).getAmount(), 15);
    }

    @Test
    void testCalculateFinalBalance()  throws IOException  {
        Long balance = testInstance.calculateAccountBalance (getTestTransactionMap());

        assertNotNull(balance);
        assertEquals(-10, balance);
    }

    private List<Transaction>  getTestTransactionMap () {
        List<Transaction> testMap = new ArrayList<>();
        testMap.add(
            new Transaction ("TX10001", "ACC334455", "ACC778899",
                Utils.fromStringToLocalDateTime("20/10/2018 12:47:55"), 25L, "PAYMENT")
        );
        testMap.add(
                new Transaction ("TX10003", "ACC778899", "ACC334455",
                        Utils.fromStringToLocalDateTime("20/10/2018 17:37:55"), 15L, "PAYMENT")
        );
        return testMap;
    }
}
