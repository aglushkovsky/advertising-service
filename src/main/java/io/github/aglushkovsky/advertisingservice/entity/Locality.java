package io.github.aglushkovsky.advertisingservice.entity;

import io.github.aglushkovsky.advertisingservice.entity.enumeration.LocalityType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@NamedNativeQuery(name = "getFullLocalityHierarchyQuery", resultClass = Locality.class, query = """
        WITH RECURSIVE r AS (SELECT l1.id,
                                    l1.locality_name,
                                    l1.parent_locality,
                                    l1.type
                             FROM locality l1
                                      LEFT JOIN locality l2 ON l1.parent_locality = l2.id
                             WHERE l1.id = :leafLocalityId
                             UNION ALL
                             SELECT l.id,
                                    l.locality_name,
                                    l.parent_locality,
                                    l.type
                             FROM locality l
                                      JOIN r ON l.id = r.parent_locality)
        SELECT id, locality_name, parent_locality, type
        FROM r
        """)
public class Locality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "locality_name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_locality")
    private Locality parentLocality;

    @Enumerated(EnumType.STRING)
    private LocalityType type;
}
