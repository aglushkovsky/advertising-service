package io.github.aglushkovsky.advertisingservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"ancestorLocality", "descendantLocality"})
@Entity
@Table(name = "locality_parts_relation")
public class LocalityPart implements Comparable<LocalityPart> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Locality ancestorLocality;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Locality descendantLocality;

    private Integer depth;

    @Override
    public int compareTo(LocalityPart o) {
        return o.getDepth() == null ? -1 : o.getDepth().compareTo(depth);
    }
}
