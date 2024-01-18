package edu.school21.ws;

import edu.school21.domain.Room;
import edu.school21.domain.User;
import edu.school21.dto.BulletMessage;
import edu.school21.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.Optional;

import static java.lang.Thread.sleep;

@Controller
public class BulletsController {
    @Autowired
    RoomService roomService;
    private final SimpMessageSendingOperations messagingTemplate;

    public BulletsController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/bullets.{name}")
    public void receiveCoordinates(
            @DestinationVariable("name") String name,
            BulletMessage message
    ) {
        messagingTemplate.convertAndSend("/topic/bullets." + name, message);

        try {
            checkBang(message, name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void checkBang(BulletMessage message, String name) throws InterruptedException {
        Integer tempY = message.getY();
        Integer tempX = message.getX();
        Optional<User> user = roomService.findOpponentInRoom(name);
        User opponent = user.get();
        while (tempY >= 75) {
            tempY -= message.getSpeed();
            if (user.isPresent()) {
//                User opponent = user.get();
                int opTankX = opponent.getTank().getX();
                int opTankY = opponent.getTank().getY();
                if (tempX >= opTankX && tempX <= opTankX + opponent.getTank().getWidth()) {
                    if (tempY >= opTankY && tempY <= opTankY + opponent.getTank().getHeight()) {
                        System.out.println("bang");
                        opponent.takeDamage(50);
                        messagingTemplate.convertAndSend("/topic/bang." + opponent.getName(), "y");
                        messagingTemplate.convertAndSend("/topic/bang." + name, "o");

                        if (opponent.getTank().getHealth() <= 0) {
                            Room room = roomService.findRoomByPlayerName(opponent.getName()).get();
                            roomService.deleteRoom(room);
                            messagingTemplate.convertAndSend("/topic/outRoom." + opponent.getName()
                                    , "w");
                            messagingTemplate.convertAndSend("/topic/outRoom." + name
                                    , "l");
                            break;
                        }
                        roomService.updateUserInRoom(opponent);


                        System.out.println("tank " + name + " position: " + user.get().getTank());
                        System.out.println("tank " + opponent.getName() + " position: " + opponent.getTank());
                        break;
                    }
                }
            }
            sleep(message.getDelay());
        }
    }
}
