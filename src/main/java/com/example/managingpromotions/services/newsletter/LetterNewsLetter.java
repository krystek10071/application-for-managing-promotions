package com.example.managingpromotions.services.newsletter;

import java.io.IOException;

public interface LetterNewsLetter {
    void fetchPDFFromWeb() throws IOException, InterruptedException;
}
