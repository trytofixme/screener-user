package ru.screener.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exchange_pref")
public class ExchangePref {

    @Id
    @UuidGenerator
    private UUID id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", unique = true)
    private AppUser user;

    @Column(nullable = false)
    private boolean bybitEnabled = true;
    @Column(nullable = false)
    private boolean moexEnabled  = false;

    @Column(nullable = false)
    private Instant updatedAt;
}
