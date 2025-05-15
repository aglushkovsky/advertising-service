package io.github.aglushkovsky.advertisingservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"sender", "receiver"})
@EqualsAndHashCode(exclude = {"sender", "receiver"})
@Builder
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User sender;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User receiver;

    private LocalDateTime sentAt;

    @Column(name = "message_text")
    private String text;
}
