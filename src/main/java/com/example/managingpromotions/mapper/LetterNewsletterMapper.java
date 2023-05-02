package com.example.managingpromotions.mapper;

import com.example.managingpromotions.model.NewsletterFile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.managingPromotions.api.model.LetterNewsletterFileDTO;

@Mapper(componentModel = "spring")
public interface LetterNewsletterMapper {

    @Mapping(target = "file", source = "pdfInBase64")
    LetterNewsletterFileDTO mapNewsletterFileToLetterNewsletterFileDTO(NewsletterFile newsletterFile, String pdfInBase64);
}
