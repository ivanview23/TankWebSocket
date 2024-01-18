package edu.school21.ws;

import edu.school21.domain.Room;
import edu.school21.domain.Tank;
import edu.school21.domain.User;
import edu.school21.dto.RoomRequestDto;
import edu.school21.services.RoomService;
import edu.school21.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    private final SimpMessageSendingOperations messagingTemplate;

    public RoomController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/joinRoom")
    public void joinRoom(RoomRequestDto roomRequestDto) {
        System.out.println("in joinRoom: " + roomRequestDto.getName());
        Optional<User> userOpt = userService.getUserByName(roomRequestDto.getName());
        if (!userOpt.isPresent()) {
            return;
        }
        User user = userOpt.get();
        user.setTank(new Tank(
                50,
                50,
                roomRequestDto.getHeight(),
                roomRequestDto.getWidth(),
                150
                )
        );
        roomService.joinRoom(user);
        Room room = roomService.findRoomByPlayer(user).get();
        if (!(room.getPlayer2() == null)) {
            messagingTemplate.convertAndSend(
                    "/topic/joinRoom.player." + room.getPlayer1().getName(),
                    room.getPlayer2().getName()
            );
            messagingTemplate.convertAndSend(
                    "/topic/joinRoom.player." + room.getPlayer2().getName(),
                    room.getPlayer1().getName()
            );
        } else {
            messagingTemplate.convertAndSend(
                    "/topic/joinRoom",
                    "Player: " + room.getPlayer1().getName() + " create room."
            );
        }
        for (Room room1 : roomService.getRooms()) {
            System.out.println(room1);
        }
    }
}
