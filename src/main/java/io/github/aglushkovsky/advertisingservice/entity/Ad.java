package io.github.aglushkovsky.advertisingservice.entity;

import io.github.aglushkovsky.advertisingservice.entity.enumeration.AdStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"locality", "publisher"})
@EqualsAndHashCode(exclude = {"locality", "publisher"})
@Builder
@Entity
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Long price;

    private String description;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Locality locality;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User publisher;

    private LocalDateTime publishedAt;

    @Enumerated(EnumType.STRING)
    private AdStatus status;

    private Boolean isPromoted;
}
