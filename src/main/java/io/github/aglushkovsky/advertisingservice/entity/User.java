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
    private Role role = Role.USER;

    private Double totalRating = 0.0;

    @OneToMany(mappedBy = "author")
    private List<UserRate> outgoingRates = new ArrayList<>();

    @OneToMany(mappedBy = "recipient")
    private List<UserRate> incomingRates = new ArrayList<>();

    @OneToMany(mappedBy = "publisher")
    private List<Ad> createdAds = new ArrayList<>();
}
