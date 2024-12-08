package dev.renzozukeram.winter.remoteObject;

import dev.renzozukeram.test.MyObject;
import dev.renzozukeram.winter.annotations.Get;
import dev.renzozukeram.winter.annotations.RequestMapping;

@RequestMapping("/creditbureau")
public class CreditBureau {

    public CreditBureau() {
    }

    @Get("/pamonha")
    public Object matchUser(String username) {
        System.out.println("The user " + username + " was called");
        return new MyObject(username, 5);
    }
}
