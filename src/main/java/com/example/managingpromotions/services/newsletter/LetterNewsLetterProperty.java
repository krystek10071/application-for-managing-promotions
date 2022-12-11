package com.example.managingpromotions.services.newsletter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties
public class LetterNewsLetterProperty {

    private String rootLocation;
}
