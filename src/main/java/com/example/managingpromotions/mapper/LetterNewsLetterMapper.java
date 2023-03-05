package com.example.managingpromotions.mapper;

import com.example.managingpromotions.model.NewsletterFile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.managingPromotions.api.model.LetterNewsletterDTO;

@Mapper(componentModel = "spring")
public interface LetterNewsLetterMapper {

    @Mapping(target = "file", source = "fileInBase64")
    LetterNewsletterDTO mapNewsLetterFileToLetterNewsletterDTO(NewsletterFile newsletterFile, String fileInBase64);
}
