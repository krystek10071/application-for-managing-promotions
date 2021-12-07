package com.example.managingpromotions.controllers;

import com.example.managingpromotions.services.shopParser.IParser;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

@Controller
public class ParserManageController {

    private final IParser parserService;
    private final IParser auchanParser;

    public ParserManageController(@Qualifier("lidlParser") IParser parserService, @Qualifier("auchanService") IParser auchanParser) {
        this.parserService = parserService;
        this.auchanParser = auchanParser;
    }

    public void parse() {
        parserService.prepareData("Lidl");
        auchanParser.prepareData("Auchan");
    }
}
