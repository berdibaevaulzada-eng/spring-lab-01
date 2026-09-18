package kz.iitu.springlab.notify;

public class NotificationService {

    private final Notifier notifier;

    public NotificationService(Notifier notifier) {
        this.notifier = notifier;
    }

    public String notify(String message) {
        return notifier.send(message);
    }
}