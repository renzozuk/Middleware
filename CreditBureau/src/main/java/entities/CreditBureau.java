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
    public ResponseEntity getUser(String username) {
        System.out.println("The user " + username + " was called");
        return new ResponseEntity(200, username);
    }

    @Post("/users")
    public ResponseEntity postUser(String username, String password) {
        System.out.println("The user " + username + " was created");
        return new ResponseEntity(201, username);
    }
}
