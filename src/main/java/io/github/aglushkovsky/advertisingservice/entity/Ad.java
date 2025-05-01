package io.github.aglushkovsky.advertisingservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"locality", "publisher"})
@Entity
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private BigDecimal price;

    private String description;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Locality locality;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User publisher;

    private LocalDateTime publishedAt;

    private Boolean isPromoted;
}
