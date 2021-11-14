package com.example.managingpromotions.controllers;

import com.example.managingpromotions.services.IParser;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

@Controller
public class ParserManageController {

    private final IParser parserService;
    private final IParser ladyBirdService;

    public ParserManageController(@Qualifier("lidlParser") IParser parserService, @Qualifier("ladyBirdParser") IParser ladyBirdService) {
        this.parserService = parserService;
        this.ladyBirdService = ladyBirdService;
    }

    public void parse() {
       // ladyBirdService.prepareData();
        parserService.prepareData("Lidl");
    }
}
