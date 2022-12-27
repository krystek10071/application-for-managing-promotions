package com.example.managingpromotions.services.newsletter;

import com.example.managingpromotions.model.NewsletterFile;
import com.example.managingpromotions.model.repository.NewsletterFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.managingPromotions.api.model.LetterNewsletterResponseDTO;
import pl.managingPromotions.api.model.ShopEnum;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LetterNewsletterService {

    private final NewsletterFileRepository newsletterFileRepository;


    public List<LetterNewsletterResponseDTO> getAllNewsletters() {

        List<NewsletterFile> newsletters = newsletterFileRepository.findAll();
        List<LetterNewsletterResponseDTO> letterNewsletterResponseDTOS = new ArrayList<>();

        newsletters.forEach(newsletterFile -> {

            LetterNewsletterResponseDTO letterNewsletterDTO = createLetterNewsletterDTO(newsletterFile);
            letterNewsletterResponseDTOS.add(letterNewsletterDTO);
        });

        return letterNewsletterResponseDTOS;
    }

    private LetterNewsletterResponseDTO createLetterNewsletterDTO(NewsletterFile newsletterFile) {

        return LetterNewsletterResponseDTO.builder()
                .id(newsletterFile.getId())
                .shopName(newsletterFile.getShopName())
                .shopName(ShopEnum.ELECLERC)
                .extension("pdf")
                .fileName(newsletterFile.getFileName())
                .build();
    }

    private String generatePdfInBase64(NewsletterFile newsletterFile) {

        File file = new File(newsletterFile.getPath());
        try {
            InputStream inputStream = new FileInputStream(file);
            String pdfInBase64 = Base64.getEncoder().encodeToString(inputStream.readAllBytes());
            inputStream.close();

            return pdfInBase64;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
