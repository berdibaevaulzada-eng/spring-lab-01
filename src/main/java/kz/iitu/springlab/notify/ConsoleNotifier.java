package kz.iitu.springlab.notify;

public class ConsoleNotifier implements Notifier {

    @Override
    public String send(String message) {
        return "Console: " + message;
    }

    @Override
    public String channel() {
        return "console";
    }
}