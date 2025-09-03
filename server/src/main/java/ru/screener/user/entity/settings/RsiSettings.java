package ru.screener.user.entity.settings;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import ru.screener.user.entity.user.AppUser;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "user")
@Entity
@Table(name = "rsi_settings")
public class RsiSettings {

    @Id
    @UuidGenerator
    private UUID id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", unique = true)
    private AppUser user;

    @Column(name = "short_time_frame")
    @Enumerated(EnumType.STRING)
    private TimeFrame shortTimeFrame;
    @Column(name = "long_time_frame")
    @Enumerated(EnumType.STRING)
    private TimeFrame longTimeFrame;

    @Column(name = "short_rsi", precision = 10, scale = 5)
    private BigDecimal shortRsi;
    @Column(name = "long_rsi", precision = 10, scale = 5)
    private BigDecimal longRsi;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}