package ru.practicum.explorewithme.basic.users.models;

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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 250)
    private String name;

    @Column(name = "email", nullable = false, length = 254)
    private String email;

    @Column(name = "email_lower", nullable = false, length = 254, unique = true)
    private String emailLowercase; // добавлено для совместимости с H2(не поддерживает function indexes)

    @PrePersist
    @PreUpdate
    private void setLowercase() {
        this.emailLowercase = this.email != null ? this.email.toLowerCase(Locale.ROOT) : null;
    }
}
