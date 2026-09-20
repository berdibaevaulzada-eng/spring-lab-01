package kz.iitu.springlab.notify;

import jakarta.annotation.PostConstruct;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("truncate")
@Order(3)
public class TruncateNotifier implements Notifier {

    @PostConstruct
    public void init() {
        System.out.println("TruncateNotifier initialized");
    }

    @Override
    public String send(String message) {
        if (message.length() > 20) {
            return message.substring(0, 20) + "...";
        }
        return message;
    }

    @Override
    public String channel() {
        return "truncate";
    }
}
