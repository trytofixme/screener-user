package ru.screener.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "user_state")
public class UserState {

    public enum State {
        NONE,
        WAIT_PERIOD_LONG, WAIT_PERIOD_SHORT, WAIT_PERIOD_DUMP,
        WAIT_PERCENT_LONG, WAIT_PERCENT_SHORT, WAIT_PERCENT_DUMP
    }

    @Id
    @UuidGenerator
    private UUID id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", unique = true)
    private AppUser user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state = State.NONE;

    @Column(nullable = false)
    private Instant updatedAt;
}
