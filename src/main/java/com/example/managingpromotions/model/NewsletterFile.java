package com.example.managingpromotions.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class NewsletterFile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_newsletter_file")
    @SequenceGenerator(
            name = "seq_newsletter_file",
            sequenceName = "seq_newsletter_file",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "path")
    private String path;

    @Column(name = "extension")
    private String extension;

    @Column(name = "created_date")
    private LocalDate createdDate;

}
