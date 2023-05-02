package com.example.managingpromotions.controllers.letterNewsletter;

import com.example.managingpromotions.services.newsletter.LetterNewsLetter;
import com.example.managingpromotions.services.newsletter.LetterNewsletterGroszekService;
import com.example.managingpromotions.services.newsletter.LetterNewsletterService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.managingPromotions.api.model.LetterNewsletterFileDTO;
import pl.managingPromotions.api.model.LetterNewsletterResponseDTO;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "api/v1/newspapers")
public class LetterNewsletterController {

    private final LetterNewsletterService letterNewsletterService;
    private final LetterNewsLetter letterNewsletterServiceEleclerc;
    private final LetterNewsletterGroszekService letterNewsletterGroszekService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/eLeclerc")
    public void letterNewsLetterEleclerc() throws InterruptedException, IOException {
        letterNewsletterServiceEleclerc.fetchPDFFromWeb();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/groszek")
    public void letterNewsLetterGroszek() throws IOException {
        letterNewsletterGroszekService.fetchPDFFromWeb();
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Get all newsletters")
    public List<LetterNewsletterResponseDTO> getAllNewspapers() {

        return letterNewsletterService.getAllNewsletters();
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Get newsletter for id")
    public LetterNewsletterFileDTO getNewsletter(@PathVariable Long id) {
        return letterNewsletterService.getNewsletterById(id);
    }
}
