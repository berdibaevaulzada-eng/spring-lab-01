package kz.iitu.springlab.notify;

public class NoopNotifier implements Notifier {

    @Override
    public String send(String message) {
        return "No operation";
    }

    @Override
    public String channel() {
        return "noop";
    }
}