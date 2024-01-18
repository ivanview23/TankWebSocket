package edu.school21.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "statistic")
public class Statistic {
    @Id
    @Column(name = "user_name")
    String userName;
    @Column(name = "game")
    int countGame = 0;
    @Column(name = "victory")
    int countVictory = 0;
    @Column(name = "fail")
    int countFail = 0;

    @OneToOne
    @JoinColumn(name = "user_name")
    User user;

    public Statistic(String userName) {
        this.userName = userName;
    }
}
