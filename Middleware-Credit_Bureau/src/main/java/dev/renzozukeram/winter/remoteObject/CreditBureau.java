package dev.renzozukeram.winter.remoteObject;

import dev.renzozukeram.winter.annotations.Get;
import dev.renzozukeram.winter.annotations.RequestMapping;

@RequestMapping("/creditbureau")
public class CreditBureau {

    @Get
    public String matchUser() {
        System.out.println("Match user was called");
        return "";
    }
}
