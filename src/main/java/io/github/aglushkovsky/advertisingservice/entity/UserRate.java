package io.github.aglushkovsky.advertisingservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"author", "recipient"})
@EqualsAndHashCode(exclude = {"author", "recipient"})
@Entity
public class UserRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double value;

    @ManyToOne(optional = false)
    private User author;

    @ManyToOne(optional = false)
    private User recipient;

    private LocalDateTime createdAt;
}
