package io.github.aglushkovsky.advertisingservice.entity;

import io.github.aglushkovsky.advertisingservice.entity.enumeration.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"outgoingRates", "incomingRates", "createdAds"})
@EqualsAndHashCode(exclude = {"outgoingRates", "incomingRates", "createdAds"})
@Builder
@Entity
@Table(schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;

    private String passwordHash;

    private String email;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Double totalRating;

    @Builder.Default
    @OneToMany(mappedBy = "author")
    private List<UserRate> outgoingRates = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "recipient")
    private List<UserRate> incomingRates = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "publisher")
    private List<Ad> createdAds = new ArrayList<>();
}
