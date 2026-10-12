package kz.iitu.springlab.notify;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("sms")
@Order(2)
public class SmsNotifier implements Notifier {

    @Override
    public String send(String message) {
        return "SMS: " + message;
    }

    @Override
    public String channel() {
        return "sms";
    }
}