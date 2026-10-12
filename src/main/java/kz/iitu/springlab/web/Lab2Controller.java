package kz.iitu.springlab.web;

import kz.iitu.springlab.notify.NotificationService;
import kz.iitu.springlab.lifecycle.LifecycleDemo;
import org.springframework.web.bind.annotation.*;
import kz.iitu.springlab.scope.TicketOffice;
import kz.iitu.springlab.notify.Notifier;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.*;

@RestController
@RequestMapping("/lab2")
public class Lab2Controller {

    private final NotificationService service;
    private final LifecycleDemo lifecycle;
    private final TicketOffice ticketOffice;
    private final Notifier customNotifier;

    public Lab2Controller(NotificationService service, LifecycleDemo lifecycle,
                          TicketOffice ticketOffice,
                          @Qualifier("truncate") Notifier customNotifier) {
        this.service = service;
        this.lifecycle = lifecycle;
        this.ticketOffice = ticketOffice;
        this.customNotifier = customNotifier;
    }

    @GetMapping("/primary")
    public String primary(@RequestParam(defaultValue = "Hello") String message) {
        return service.viaPrimary(message);
    }

    @GetMapping("/console")
    public String console(@RequestParam(defaultValue = "Hello") String message) {
        return service.viaConsole(message);
    }

    @GetMapping("/all")
    public List<String> all(@RequestParam(defaultValue = "Hello") String message) {
        return service.viaAll(message);
    }

    @GetMapping("/names")
    public Set<String> names() {
        return service.names();
    }

    @GetMapping("/lifecycle")
    public List<String> lifecycle() {
        return lifecycle.events();
    }
    @GetMapping("/scopes")
    public Map<String, Object> scopes() {
        return ticketOffice.demo();
    }
    @GetMapping("/custom")
    public String custom(@RequestParam(defaultValue = "Hello") String text) {
        return customNotifier.send(text);
    }
}
