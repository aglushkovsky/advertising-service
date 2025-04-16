package io.github.aglushkovsky.advertisingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class UserRate {

    @Id
    private Long id;

    private Double value;

    @ManyToOne
    private User author;

    @ManyToOne
    private User recipient;

    private LocalDateTime createdAt;
}
