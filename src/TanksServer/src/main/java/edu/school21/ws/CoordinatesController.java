package edu.school21.ws;

import edu.school21.domain.Room;
import edu.school21.dto.CoordinatesMessage;
import edu.school21.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class CoordinatesController {
    @Autowired
    private RoomService roomService;

    private final SimpMessageSendingOperations messagingTemplate;

    public CoordinatesController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/coordinates.{opName}")
    public void receiveCoordinates(
            @DestinationVariable("opName") String userName,
            CoordinatesMessage message
    ) {
        Optional<Room> room = roomService.findRoomByPlayerName(userName);
        if (room.isPresent()) {
            if (room.get().getPlayer1().getName().equals(userName)) {
                room.get().updateTankPlayer1(message.getX(), 70);
            } else {
                room.get().updateTankPlayer2(message.getX(), 70);
            }
        }
        messagingTemplate.convertAndSend("/topic/coordinates." + userName, message);
    }
}
