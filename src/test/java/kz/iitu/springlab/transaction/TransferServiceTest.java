package kz.iitu.springlab.transaction;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransferServiceTest {

    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountRepository repository;

    @Test
    void testSuccessfulTransfer() {
        Account alice = repository.findById(1L).orElseThrow();
        Account bob = repository.findById(2L).orElseThrow();

        BigDecimal aliceBefore = alice.getBalance();
        BigDecimal bobBefore = bob.getBalance();

        transferService.transfer(1L, 2L, new BigDecimal("100.00"));

        BigDecimal aliceAfter = repository.findById(1L)
                .orElseThrow().getBalance();

        BigDecimal bobAfter = repository.findById(2L)
                .orElseThrow().getBalance();

        assertEquals(0, aliceBefore.subtract(new BigDecimal("100.00"))
                .compareTo(aliceAfter));

        assertEquals(0, bobBefore.add(new BigDecimal("100.00"))
                .compareTo(bobAfter));

        System.out.println("Transfer successful!");
        System.out.println("Alice: " + aliceAfter);
        System.out.println("Bob: " + bobAfter);
    }

    @Test
    void testInsufficientFunds() {
        assertThrows(IllegalArgumentException.class, () ->
                transferService.transfer(
                        1L, 2L, new BigDecimal("999999.00")
                )
        );

        System.out.println("Insufficient funds detected!");
    }

    @Test
    void testRollback() {
        BigDecimal aliceBefore = repository.findById(1L)
                .orElseThrow().getBalance();

        BigDecimal bobBefore = repository.findById(2L)
                .orElseThrow().getBalance();

        assertThrows(IllegalArgumentException.class, () ->
                transferService.transfer(
                        1L, 2L, new BigDecimal("999999.00")
                )
        );

        BigDecimal aliceAfter = repository.findById(1L)
                .orElseThrow().getBalance();

        BigDecimal bobAfter = repository.findById(2L)
                .orElseThrow().getBalance();

        assertEquals(0, aliceBefore.compareTo(aliceAfter));
        assertEquals(0, bobBefore.compareTo(bobAfter));

        System.out.println("Balances unchanged after failed transfer!");
        System.out.println("Alice: " + aliceAfter);
        System.out.println("Bob: " + bobAfter);
    }

    @Test
    void testRealRollback() {

        BigDecimal balanceBefore = repository.findById(1L)
                .orElseThrow().getBalance();

        assertThrows(RuntimeException.class, () ->
                transferService.transferWithError(
                        1L, new BigDecimal("100.00")
                )
        );

        BigDecimal balanceAfter = repository.findById(1L)
                .orElseThrow().getBalance();

        assertEquals(0, balanceBefore.compareTo(balanceAfter));

        System.out.println("Real rollback successful!");
        System.out.println("Balance before: " + balanceBefore);
        System.out.println("Balance after: " + balanceAfter);
    }

}
