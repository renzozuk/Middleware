package controllers;

import dev.renzozukeram.winter.annotations.Get;
import dev.renzozukeram.winter.annotations.Post;
import dev.renzozukeram.winter.annotations.RequestBody;
import dev.renzozukeram.winter.annotations.RequestMapping;
import dev.renzozukeram.winter.message.ResponseEntity;
import exceptions.InvalidOperatorException;
import model.entities.Operation;

@RequestMapping("/calculator")
public class Calculator {

    @Get
    public ResponseEntity nothing() {
        return new ResponseEntity(200, "There's nothing to return here.");
    }

    @Get("/sum")
    public ResponseEntity sum(double a, double b) {
        return new ResponseEntity(200, String.format("The sum of %.2f and %.2f is %.2f", a, b, a + b));
    }

    @Get("/minus")
    public ResponseEntity minus(double a, double b) {
        return new ResponseEntity(200, String.format("%.2f minus %.2f is %.2f", a, b, a - b));
    }

    @Post("/calculate")
    public ResponseEntity calculate(@RequestBody Operation operation) {
        try {
            return new ResponseEntity(200, operation.calculate());
        } catch (InvalidOperatorException e) {
            return new ResponseEntity(400, e.getMessage());
        }
    }
}
