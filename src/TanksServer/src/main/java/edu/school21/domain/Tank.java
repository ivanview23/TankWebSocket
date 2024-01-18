package edu.school21.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tank {
    int x;
    int y;
    int height;
    int width;
    int health = 150;

    public void reduceHealth(int damage) {
        health -= damage;
    }
}
