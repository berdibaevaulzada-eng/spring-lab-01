package kz.iitu.springlab.notify;

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