package dev.renzozukeram.application;

import dev.renzozukeram.test.MyObject;
import dev.renzozukeram.winter.annotations.Get;
import dev.renzozukeram.winter.annotations.RequestMapping;

@RequestMapping("/creditbureau")
public class CreditBureau {

    @Get
    public Object test() {
        return "This is a test";
    }

    @Get("/matchuser")
    public Object matchUser(String username) {
        System.out.println("The user " + username + " was called");
        return new MyObject(username, 5);
    }
}
