package io.github.aglushkovsky.advertisingservice.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User sender;

    @ManyToOne(optional = false)
    private User receiver;

    private LocalDateTime sentAt;

    @Column(name = "message_text")
    private String text;
}
