package com.example.managingpromotions.controllers;


import com.example.managingpromotions.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.managingPromotions.api.model.LoginCredentials;
import pl.managingPromotions.api.model.RegistrationCredentials;
import pl.managingPromotions.api.model.UserAppResponseDTO;

@RestController
@AllArgsConstructor
@RequestMapping
public class AccountController {

    private final AccountService accountService;

    @PostMapping(value = "/login")
    public void login(@RequestBody final LoginCredentials credentials) {
    }

/*    @PostMapping(value = "/registration")
    public ResponseEntity<UserAppResponseDTO> registration(@RequestBody final RegistrationCredentials registrationCredentials) {

        UserAppResponseDTO userAppResponseDTO = accountService.createUserAccount(registrationCredentials);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }*/
}
