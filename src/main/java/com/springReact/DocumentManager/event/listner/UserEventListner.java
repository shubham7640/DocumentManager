package com.springReact.DocumentManager.event.listner;

import com.springReact.DocumentManager.event.UserEvent;
import com.springReact.DocumentManager.service.EmailService;
import lombok.RequiredArgsConstructor;
//import org.apache.catalina.User;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventListner {

    private  final EmailService emailService;

    @EventListener
    public void onUserEvent(UserEvent userEvent)
    {
        switch (userEvent.getType()){
            case REGISTRATION -> emailService.sendNewAccountEmail(userEvent.getUserEntity().getFirstName(),
                    userEvent.getUserEntity().getEmail(), (String)userEvent.getData().get("key"));

            case RESETPASSWORD -> emailService.sendPasswordResetEmail(userEvent.getUserEntity().getFirstName(),
                    userEvent.getUserEntity().getEmail(), (String)userEvent.getData().get("key"));

            default -> {}
        }
    }

}
