package edu.school21.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Room implements Serializable {

    @Builder.Default
    String id = UUID.randomUUID().toString();
    User player1;
    User player2;
    @Builder.Default
    Boolean isFull = false;

    public void updateTankPlayer1(int x, int y) {
        Tank tank = player1.getTank();
        tank.setX(x);
        tank.setY(y);
        player1.setTank(tank);
    }

    public void updateTankPlayer2(int x, int y) {
        Tank tank = player2.getTank();
        tank.setX(x);
        tank.setY(y);
        player2.setTank(tank);
    }
}
