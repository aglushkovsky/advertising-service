package io.github.aglushkovsky.advertisingservice.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User author;

    @ManyToOne(optional = false)
    private Ad ad;

    private LocalDateTime createdAt;

    @Column(name = "comment_text")
    private String text;
}
