package controllers;

import dev.renzozukeram.winter.annotations.Get;
import dev.renzozukeram.winter.annotations.RequestMapping;
import dev.renzozukeram.winter.message.ResponseEntity;

@RequestMapping("/calculator")
public class Calculator {

    @Get("/sum")
    public ResponseEntity sum(double a, double b) {
        return new ResponseEntity(200, String.format("The sum of %.2f and %.2f is %.2f", a, b, a + b));
    }

    @Get("/minus")
    public ResponseEntity minus(double a, double b) {
        return new ResponseEntity(200, String.format("%.2f minus %.2f is %.2f", a, b, a - b));
    }
}
