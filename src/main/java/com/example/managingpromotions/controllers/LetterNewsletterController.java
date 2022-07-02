package com.example.managingpromotions.controllers;

import com.example.managingpromotions.services.newsletter.LetterNewsletterGroszekService;
import com.example.managingpromotions.services.newsletter.LetterNewsletterServiceEleclerc;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "api/v1/newspapers")
public class LetterNewsletterController {

    private final LetterNewsletterServiceEleclerc letterNewsletterServiceEleclerc;
    private final LetterNewsletterGroszekService letterNewsletterGroszekService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/eLeclerc")
    public void letterNewsLetterEleclerc() throws AWTException, InterruptedException {
        letterNewsletterServiceEleclerc.fetchPDFFromWeb();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/groszek")
    public void letterNewsLetterGroszek() throws AWTException, InterruptedException {
        letterNewsletterGroszekService.fetchPDFFromWeb();
    }
}
