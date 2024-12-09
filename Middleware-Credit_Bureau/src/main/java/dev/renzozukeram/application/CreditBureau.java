package dev.renzozukeram.application;

import dev.renzozukeram.test.MyObject;
import dev.renzozukeram.winter.annotations.Get;
import dev.renzozukeram.winter.annotations.RequestMapping;
import dev.renzozukeram.winter.message.ResponseEntity;

@RequestMapping("/creditbureau")
public class CreditBureau {

    @Get
    public Object test() {
        return new ResponseEntity(200, "This is a test");
    }

    @Get("/matchuser")
    public ResponseEntity matchUser(String username) {
        System.out.println("The user " + username + " was called");
        return new ResponseEntity(200, new MyObject(username, 5));
    }
}
