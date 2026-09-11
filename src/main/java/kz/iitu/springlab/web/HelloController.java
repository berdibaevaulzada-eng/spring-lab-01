package kz.iitu.springlab.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class HelloController {

    @Value("${app.owner:unknown}")
    private String owner;

    @GetMapping("/hello")
    public Greeting hello(@RequestParam(defaultValue = "world") String name) {
        return new Greeting("Hello, " + name + "!", owner, LocalDateTime.now());
    }

    @GetMapping("/info")
    public Info info() {
        return new Info(
                owner,
                System.getProperty("java.version"),
                Runtime.getRuntime().availableProcessors()
        );
    }
    @GetMapping("/temperature")
    public Temperature temperature(@RequestParam(defaultValue = "32") double f) {
        double celsius = (f - 32) * 5 / 9;
        double kelvin = celsius + 273.15;

        return new Temperature(f, celsius, kelvin);
    }
    public record Greeting(String message, String owner, LocalDateTime timestamp) { }
    public record Temperature(double fahrenheit, double celsius, double kelvin) { }
    public record Info(String owner, String javaVersion, int cpuCores) { }
}
