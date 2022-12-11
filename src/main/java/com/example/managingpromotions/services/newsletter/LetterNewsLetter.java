package com.example.managingpromotions.services.newsletter;

import java.io.IOException;

public interface LetterNewsLetter {

    String fetchUrlToNewsLetterAddress(String urlNewsLetter);

    void fetchPDFFromWeb() throws IOException, InterruptedException;
}
