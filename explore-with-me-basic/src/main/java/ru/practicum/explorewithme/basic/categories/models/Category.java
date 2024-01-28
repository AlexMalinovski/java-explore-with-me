package ru.practicum.explorewithme.basic.categories.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "name_lower", nullable = false, length = 50, unique = true)
    private String nameLowercase; // добавлено для совместимости с H2(не поддерживает function indexes)

    @PrePersist
    @PreUpdate
    private void setLowercase() {
        this.nameLowercase = this.name != null ? this.name.toLowerCase(Locale.ROOT) : null;
    }
}
