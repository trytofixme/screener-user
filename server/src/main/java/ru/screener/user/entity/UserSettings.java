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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_settings")
public class UserSettings {

    @Id
    @UuidGenerator
    private UUID id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", unique = true)
    private AppUser user;

    private Integer longPeriodMin;
    private Integer shortPeriodMin;
    private Integer dumpPeriodMin;

    @Column(precision = 7, scale = 3)
    private BigDecimal longPercent;
    @Column(precision = 7, scale = 3)
    private BigDecimal shortPercent;
    @Column(precision = 7, scale = 3)
    private BigDecimal dumpPercent;

    @Column(nullable = false)
    private Instant updatedAt;
}