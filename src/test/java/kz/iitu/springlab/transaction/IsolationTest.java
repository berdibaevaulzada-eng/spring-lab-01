package kz.iitu.springlab.transaction;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IsolationTest {

    @Autowired
    private DataSource dataSource;

    private BigDecimal readBalance(Connection connection, Long id)
            throws Exception {

        String sql = "SELECT balance FROM accounts WHERE id = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet result = statement.executeQuery()) {
                assertTrue(result.next(), "Account not found");
                return result.getBigDecimal("balance");
            }
        }
    }

    private void updateBalance(Connection connection, Long id,
                               BigDecimal balance) throws Exception {

        String sql = "UPDATE accounts SET balance = ? WHERE id = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setBigDecimal(1, balance);
            statement.setLong(2, id);
            statement.executeUpdate();
        }
    }

    private void checkIsolation(int isolation, boolean shouldChange)
            throws Exception {

        try (Connection first = dataSource.getConnection();
             Connection second = dataSource.getConnection()) {

            first.setAutoCommit(false);
            second.setAutoCommit(false);

            first.setTransactionIsolation(isolation);
            second.setTransactionIsolation(
                    Connection.TRANSACTION_READ_COMMITTED);

            try {
                BigDecimal original = readBalance(first, 1L);
                System.out.println("First read: " + original);

                BigDecimal newBalance = original.add(
                        new BigDecimal("50.00"));

                updateBalance(second, 1L, newBalance);
                second.commit();

                BigDecimal secondRead = readBalance(first, 1L);
                System.out.println("Second read: " + secondRead);

                if (shouldChange) {
                    assertEquals(0, newBalance.compareTo(secondRead));
                    System.out.println("READ_COMMITTED: value changed");
                } else {
                    assertEquals(0, original.compareTo(secondRead));
                    System.out.println("REPEATABLE_READ: value unchanged");
                }

            } finally {
                first.rollback();
                second.rollback();
            }
        }
    }

    @Test
    void testReadCommitted() throws Exception {
        checkIsolation(Connection.TRANSACTION_READ_COMMITTED, true);
    }

    @Test
    void testRepeatableRead() throws Exception {
        checkIsolation(Connection.TRANSACTION_REPEATABLE_READ, false);
    }
}
