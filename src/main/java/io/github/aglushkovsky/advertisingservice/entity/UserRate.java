package io.github.aglushkovsky.advertisingservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"author", "recipient"})
@EqualsAndHashCode(exclude = {"author", "recipient"})
@Builder
@Entity
public class UserRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double value;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User author;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User recipient;

    private LocalDateTime createdAt;
}
