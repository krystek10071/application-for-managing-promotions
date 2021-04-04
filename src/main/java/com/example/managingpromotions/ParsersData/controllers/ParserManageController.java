package com.example.managingpromotions.ParsersData.controllers;

import com.example.managingpromotions.ParsersData.services.IParser;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

@Controller
public class ParserManageController {

    private final IParser parserService;

    public ParserManageController(@Qualifier("lidlParser") IParser parserService) {
        this.parserService = parserService;
    }

    public void parse() {
        parserService.prepareData();
    }
}
