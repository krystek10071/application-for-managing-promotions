package com.example.managingpromotions.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name = "date_from")
    private Date dateFrom;

    @Column(name = "date_to")
    private Date dateTo;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserApp userApp;
}
