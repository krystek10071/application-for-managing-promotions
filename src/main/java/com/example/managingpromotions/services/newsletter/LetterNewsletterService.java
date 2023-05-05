package com.example.managingpromotions.services.newsletter;

import com.example.managingpromotions.exception.ResourceNotFoundException;
import com.example.managingpromotions.mapper.LetterNewsletterMapper;
import com.example.managingpromotions.model.NewsletterFile;
import com.example.managingpromotions.model.repository.NewsletterFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.managingPromotions.api.model.LetterNewsletterFileDTO;
import pl.managingPromotions.api.model.LetterNewsletterResponseDTO;

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

    private final LetterNewsletterMapper letterNewsletterMapper;
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

    public LetterNewsletterFileDTO getNewsletterById(Long id) {

        NewsletterFile newsletterFile = newsletterFileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Newsletter with id: " + id + " not found"));

        String fileInBase64 = generatePdfInBase64(newsletterFile);
        return letterNewsletterMapper.mapNewsletterFileToLetterNewsletterFileDTO(newsletterFile, fileInBase64);
    }

    private LetterNewsletterResponseDTO createLetterNewsletterDTO(NewsletterFile newsletterFile) {

        return LetterNewsletterResponseDTO.builder()
                .id(newsletterFile.getId())
                .shopName(newsletterFile.getShopName())
                .extension("pdf")
                .fileName(newsletterFile.getFileName())
                .startDate(String.valueOf(newsletterFile.getStarDate()))
                .endDate(String.valueOf(newsletterFile.getEndDate()))
                .build();
    }

    private String generatePdfInBase64(NewsletterFile newsletterFile) {
        try (InputStream inputStream = new FileInputStream(newsletterFile.getPath())) {
            byte[] pdfBytes = inputStream.readAllBytes();
            return Base64.getEncoder().encodeToString(pdfBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
