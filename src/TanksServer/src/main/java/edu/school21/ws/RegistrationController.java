package edu.school21.ws;

import edu.school21.dto.AddUserRequestDto;
import edu.school21.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    private final SimpMessageSendingOperations messagingTemplate;

    public RegistrationController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/registration")
    public void registrationUser(AddUserRequestDto message) {
        System.out.println("Authenticate user: " + message);
        boolean isSign = userService.signIn(message.getName(), message.getPassword());
        messagingTemplate.convertAndSend("/topic/registration." + message.getUuid(), isSign);
    }

}
