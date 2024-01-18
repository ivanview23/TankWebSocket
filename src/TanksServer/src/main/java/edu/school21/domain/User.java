package edu.school21.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "name")
    String name;

    @Column(name = "password")
    String password;

    @Transient
    long lastActivity;

    @Transient
    Tank tank;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    Statistic statistic;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (this.name.equals(((User) o).getName())) {
            return true;
        }
        return false;
    }

    public void takeDamage(int damage) {
        tank.reduceHealth(damage);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", lastActivity=" + lastActivity +
                ", password='" + password + '\'' +
                '}';
    }
}
