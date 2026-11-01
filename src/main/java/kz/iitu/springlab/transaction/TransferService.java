package kz.iitu.springlab.transaction;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {

    private final AccountRepository repository;

    public TransferService(AccountRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void transfer(Long fromId, Long toId, BigDecimal amount) {

        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        if (fromId.equals(toId)) {
            throw new IllegalArgumentException("Accounts must be different");
        }

        Account from = repository.findById(fromId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));

        Account to = repository.findById(toId)
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        if (from.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        repository.save(from);
        repository.save(to);
    }

    @Transactional
    public void transferWithError(Long fromId, BigDecimal amount) {

        Account from = repository.findById(fromId)
                .orElseThrow();

        from.setBalance(from.getBalance().subtract(amount));

        repository.saveAndFlush(from);

        // Simulate an error after updating the balance
        throw new RuntimeException("Simulated transaction failure");
    }

}
