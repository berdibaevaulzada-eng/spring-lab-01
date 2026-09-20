package kz.iitu.springlab.web;

import kz.iitu.springlab.notify.NotificationService;
import kz.iitu.springlab.lifecycle.LifecycleDemo;
import org.springframework.web.bind.annotation.*;
import kz.iitu.springlab.scope.TicketOffice;

import java.util.*;

@RestController
@RequestMapping("/lab2")
public class Lab2Controller {

    private final NotificationService service;
    private final LifecycleDemo lifecycle;
    private final TicketOffice ticketOffice;

    public Lab2Controller(NotificationService service, LifecycleDemo lifecycle,
                          TicketOffice ticketOffice) {
        this.service = service;
        this.lifecycle = lifecycle;
        this.ticketOffice = ticketOffice;
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
}
