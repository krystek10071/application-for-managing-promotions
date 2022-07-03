package com.example.managingpromotions.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsletterFileRepository extends JpaRepository<NewsletterFile, Long> {
}