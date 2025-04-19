package io.github.aglushkovsky.advertisingservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private BigDecimal price;

    private String description;

    @ManyToOne(optional = false)
    private Locality locality;

    @ManyToOne(optional = false)
    private User publisher;

    private LocalDateTime publishedAt;

    private boolean isPromoted;
}
