package edu.school21.dto;

import edu.school21.domain.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomDto {

    String id;
    String name;
    String owner;
    String opponent;
    Boolean isEmpty;

}
