package edu.school21.ws;

import edu.school21.domain.User;
import edu.school21.dto.AddUserRequestDto;
import edu.school21.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class SignUpController {

    @Autowired
    private UserService userService;

    private final SimpMessageSendingOperations messagingTemplate;

    public SignUpController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/signUp")
    public void registrationUser(AddUserRequestDto message) {
        System.out.println("SignUp user: " + message);
        Optional<User> userOptional = userService.signUp(message.getName(), message.getPassword());
        if (userOptional.isPresent()) {
            String name = userOptional.get().getName();
            System.out.println(name);
            System.out.println("/topic/signUp." + message.getUuid());
            messagingTemplate.convertAndSend(
                    "/topic/signUp." + message.getUuid(), name);
        } else {
            messagingTemplate.convertAndSend(
                    "/topic/error." + message.getUuid(),
                    "Wrong");
        }
    }
}