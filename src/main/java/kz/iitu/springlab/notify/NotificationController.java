package kz.iitu.springlab.notify;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @GetMapping("/notify")
    public String notify(
            @RequestParam(defaultValue = "email") String type,
            @RequestParam(defaultValue = "Hello") String message) {

        Notifier notifier;

        if (type.equalsIgnoreCase("sms")) {
            notifier = new SmsNotifier();
        } else {
            notifier = new EmailNotifier();
        }

        NotificationService service = new NotificationService(notifier);
        return service.notify(message);
    }
}