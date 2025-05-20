package io.github.aglushkovsky.advertisingservice.entity;

import io.github.aglushkovsky.advertisingservice.entity.enumeration.LocalityType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"ancestors", "descendants"})
@Builder
@Entity
public class Locality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "descendantLocality")
    private List<LocalityPart> ancestors = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "ancestorLocality")
    private List<LocalityPart> descendants = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private LocalityType type;
}
