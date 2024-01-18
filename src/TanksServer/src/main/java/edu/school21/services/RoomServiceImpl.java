package edu.school21.services;

import edu.school21.domain.Room;
import edu.school21.domain.Tank;
import edu.school21.domain.User;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomServiceImpl implements RoomService {

    List<Room> rooms = new ArrayList<>();

    @Override
    public void addRoom(Room room) {
        rooms.add(room);
    }

    @Override
    public void deleteRoom(Room room) {
        rooms.remove(room);
    }

    @Override
    public void joinRoom(User player) {
        if (checkUserInRoom(player))
            return;

        for (int i = 0; i < rooms.size(); i++) {
            if (!rooms.get(i).getIsFull()) {
                rooms.get(i).setPlayer2(player);
                rooms.get(i).setIsFull(true);
                return;
            }
        }
        rooms.add(Room.builder()
                .player1(player)
                .player2(null)
                .build());
    }

    private boolean checkUserInRoom(User player) {
        for (Room room : rooms) {
            if (room.getPlayer1() != null && room.getPlayer1().equals(player))
                return true;
            if (room.getPlayer2() != null && room.getPlayer2().equals(player))
                return true;
        }
        return false;
    }

    @Override
    public Optional<Room> findEmptyRoom() {
        for (Room room : rooms) {
            if (!room.getIsFull()) {
                return Optional.of(room);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Room> findRoomByPlayer(User player) {
        for (Room room : rooms) {
            if (room.getPlayer1().equals(player) || room.getPlayer2().equals(player)) {
                return Optional.of(room);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Room> findRoomByPlayerName(String playerName) {
        for (Room room : rooms) {
            if (room.getPlayer1().getName().equals(playerName)
                    || room.getPlayer2().getName().equals(playerName)) {
                return Optional.of(room);
            }
        }
        return Optional.empty();
    }
    @Override
    public Optional<User> findOpponentInRoom(String name) {
        for (Room room : rooms) {
            if (room.getPlayer1().getName().equals(name)
                    && room.getPlayer2() != null) {
                return Optional.of(room.getPlayer2());
            }
            if (room.getPlayer2().getName().equals(name)
                    && room.getPlayer1() != null) {
                return Optional.of(room.getPlayer1());
            }
        }
        return Optional.empty();
    }
    @Override
    public void updateUserInRoom(User user) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getPlayer1() != null
                    && rooms.get(i).getPlayer1().getName().equals(user.getName())) {
                rooms.get(i).setPlayer1(user);
            }
            if (rooms.get(i).getPlayer2() != null
                    && rooms.get(i).getPlayer2().getName().equals(user.getName())) {
                rooms.get(i).setPlayer2(user);
            }
        }
    }

    @Override
    public String toString() {
        return "RoomServiceImpl{" +
                "rooms=" + rooms +
                '}';
    }

    @Override
    public List<Room> getRooms() {
        return rooms;
    }
}
