package dev.amanda.organization.domain;

import dev.amanda.shared.domain.BaseEntity;
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
    String name;

    @Column(nullable = false, unique = true)
    String realm;

    @OneToMany(mappedBy = "organization",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    public List<User> users = new ArrayList<>();
}
