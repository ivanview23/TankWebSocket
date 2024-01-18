package edu.school21.services;

import edu.school21.domain.Room;
import edu.school21.domain.User;

import java.util.List;
import java.util.Optional;

public interface RoomService {

    void addRoom(Room room);

    void deleteRoom(Room room);

    void joinRoom(User player);

    Optional<Room> findRoomByPlayer(User player);

    public Optional<Room> findRoomByPlayerName(String playerName);

    public Optional<User> findOpponentInRoom(String playerId);

    Optional<Room> findEmptyRoom();

    public void updateUserInRoom(User user);

    List<Room> getRooms();
}
