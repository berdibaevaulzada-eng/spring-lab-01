package kz.iitu.springlab.notify;

public class EmailNotifier implements Notifier {

    @Override
    public String send(String message) {
        return "EMAIL: " + message;
    }

    @Override
    public String channel() {
        return "email";
    }
}