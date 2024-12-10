package entities;

import dev.renzozukeram.winter.annotations.Get;
import dev.renzozukeram.winter.annotations.RequestMapping;
import dev.renzozukeram.winter.message.ResponseEntity;

@RequestMapping("/creditbureau")
public class CreditBureau {

    @Get("/matchuser")
    public ResponseEntity matchUser(String username) {
        System.out.println("The user " + username + " was called");
        return new ResponseEntity(200, username);
    }
}
