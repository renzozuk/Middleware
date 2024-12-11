package entities;

import dev.renzozukeram.winter.annotations.*;
import dev.renzozukeram.winter.message.ResponseEntity;

@RequestMapping("/creditbureau")
public class CreditBureau {

    @Get
    public ResponseEntity testGet() {
        System.out.println("GET request called succesffully");
        return new ResponseEntity(200, "GET request called succesffully");
    }

    @Post
    public ResponseEntity testPost() {
        System.out.println("POST request called succesfully");
        return new ResponseEntity(201, "POST request called succesfully");
    }

    @Put
    public ResponseEntity testPut() {
        System.out.println("PUT request called succesfully");
        return new ResponseEntity(200, "PUT request called succesfully");
    }

    @Patch
    public ResponseEntity testPatch() {
        System.out.println("PATCH request called succesfully");
        return new ResponseEntity(200, "PATCH request called successfully");
    }

    @Delete
    public ResponseEntity testDelete() {
        System.out.println("DELETE request called succesfully");
        return new ResponseEntity(204);
    }

    @Get("/users")
    public ResponseEntity getUser(Integer username) {
        System.out.println("The user " + username + " was called");
        return new ResponseEntity(200, username);
    }

    @Post("/users")
    public ResponseEntity postUser(@RequestBody User user) {
        System.out.println("The user " + user.getUsername() + " was created and its password is " + user.getPassword());
        return new ResponseEntity(201, user.getUsername());
    }

    @Get("/sum")
    public ResponseEntity sum(int a, int b) {
        System.out.println("The sum of " + a + " and " + b + " is " + (a + b));
        return new ResponseEntity(200, (a + b));
    }

    @Get("/test")
    public ResponseEntity test(boolean verifier, @RequestBody User user) {
        System.out.println(verifier ? "It's true" : "It's false");
        return new ResponseEntity(200, user);
    }
}
