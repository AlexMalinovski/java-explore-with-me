package ru.practicum.explorewithme.basic.service.locations.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.basic.lib.dto.locations.enums.PlaceStatus;
import ru.practicum.explorewithme.basic.service.locations.models.embeddable.Location;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.util.Locale;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "places")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", nullable = false, length = 7000)
    private String description;

    @Column(name = "status", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private PlaceStatus status;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "lat", nullable = false)),
            @AttributeOverride(name = "lon", column = @Column(name = "lon", nullable = false)),
    })
    private Location location;

    @Column(name = "radius", nullable = false)
    private Integer radius;

    @Column(name = "name_lower", nullable = false, length = 50, unique = true)
    private String nameLowercase; // добавлено для совместимости с H2(не поддерживает function indexes)

    @PrePersist
    @PreUpdate
    private void setLowercase() {
        this.nameLowercase = this.name != null ? this.name.toLowerCase(Locale.ROOT) : null;
    }

}
