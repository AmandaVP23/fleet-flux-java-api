package dev.amanda.organization.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.amanda.infrastructure.shared.domain.BaseEntity;
import dev.amanda.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Organization extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String realm;

    @Column(nullable = false, unique = true)
    private String hostname;

    @OneToMany(mappedBy = "organization",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> users = new ArrayList<>();
}
