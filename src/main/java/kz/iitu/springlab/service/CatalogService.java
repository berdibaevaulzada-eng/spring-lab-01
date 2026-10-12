package kz.iitu.springlab.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;
import kz.iitu.springlab.audit.Audited;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@Service
public class CatalogService {
    @Autowired
    @Lazy
    private CatalogService self;

    public String findById(long id) {
        sleep(50);
        return "Item No." + id;
    }
    @Audited(action = "CATALOG_LIST", logArguments = true)
    public List<String> findAll(int limit) {
        sleep(300);
        return IntStream.rangeClosed(1, limit)
                .mapToObj(i -> "Element No." + i)
                .toList();
    }
    @Audited(action = "CATALOG_REMOVE")
    public String remove(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Incorrect identifier: " + id);
        }
        return "Removed item No." + id;
    }
    public String removeTwice(long id) {
        String first = self.remove(id);
        String second = self.remove(id + 1);
        return first + "; " + second;
    }
    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}