package io.github.aglushkovsky.advertisingservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"author", "ad"})
@EqualsAndHashCode(exclude = {"author", "ad"})
@Builder
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User author;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Ad ad;

    private LocalDateTime createdAt;

    @Column(name = "comment_text")
    private String text;
}
