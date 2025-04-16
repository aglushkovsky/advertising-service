package io.github.aglushkovsky.advertisingservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString(exclude = {"outgoingRates", "incomingRates"})
@EqualsAndHashCode(exclude = {"outgoingRates", "incomingRates"})
@Entity
@Table(schema = "public") // FIXME из-за того, что слово user - зарезервировано в postgres, нужно указывать схему
public class User {

    @Id
    private Long id;

    private String login;

    private String passwordHash;

    private String email;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Double totalRating;

    @OneToMany(mappedBy = "author")
    private List<UserRate> outgoingRates = new ArrayList<>();

    @OneToMany(mappedBy = "recipient")
    private List<UserRate> incomingRates = new ArrayList<>();
}
