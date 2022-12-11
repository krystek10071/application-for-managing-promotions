package com.example.managingpromotions.services.newsletter;

import com.example.managingpromotions.exception.StorageException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Optional;

@Slf4j
public abstract class LetterNewsLetterAbstract {

    protected void createDirectory(String destination, String rootLocation) {
        if (!createDirectoryIfNotExists(destination, rootLocation)) {
            String errorMessage = "Failed to create directory: " + destination;
            log.error(errorMessage);
            throw new StorageException(errorMessage);
        }
    }

    private boolean createDirectoryIfNotExists(String destination, String rootLocation) {
        final String fullDestination = appendDestinationToRoot(destination, rootLocation);
        File dir = new File(fullDestination);

        if (!dir.exists()) {
            log.info("Creating folder in destination: {}", fullDestination);
            return dir.mkdirs();
        }

        return true;
    }

    protected String appendDestinationToRoot(final String destination, String rootLocation) {
        return Optional.ofNullable(destination)
                .map(s -> rootLocation + destination)
                .orElse(rootLocation);
    }

}
