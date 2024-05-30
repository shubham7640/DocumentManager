package com.springReact.DocumentManager.service.Impl;

import com.springReact.DocumentManager.entity.ConfirmationEntity;
import com.springReact.DocumentManager.entity.CredentialEntity;
import com.springReact.DocumentManager.entity.RoleEntity;
import com.springReact.DocumentManager.entity.UserEntity;
import com.springReact.DocumentManager.enumeration.Authority;
import com.springReact.DocumentManager.enumeration.EventType;
import com.springReact.DocumentManager.event.UserEvent;
import com.springReact.DocumentManager.exception.ApiException;
import com.springReact.DocumentManager.repository.ConfirmationRepository;
import com.springReact.DocumentManager.repository.CredentialRepository;
import com.springReact.DocumentManager.repository.RoleRepository;
import com.springReact.DocumentManager.repository.UserRepository;
import com.springReact.DocumentManager.service.UserService;
import com.springReact.DocumentManager.util.UserUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

//    @Autowired
    private final UserRepository userRepository;

//    @Autowired
    private final RoleRepository roleRepository;

//    @Autowired
    private final CredentialRepository credentialRepository;

//    @Autowired
    private final ConfirmationRepository confirmationRepository;

//    @Autowired
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void createUser(String firstname, String lastName, String email, String password) {
        var userEntity=(UserEntity) userRepository.save(createNewUser(firstname,lastName,email));
        var credentialEntity = new CredentialEntity(password,userEntity);
        credentialRepository.save(credentialEntity);
        var confirmationEntity = new ConfirmationEntity(userEntity);
        confirmationRepository.save(confirmationEntity);
        applicationEventPublisher.publishEvent(new UserEvent(userEntity, EventType.REGISTRATION,
                Map.of("key",confirmationEntity.getKey())));


    }

    @Override
    public RoleEntity getRoleName(String name) {
        var role = roleRepository.findByRoleNameIgnoreCase(name);
        return role.orElseThrow(()->new ApiException("Role not found"));
    }

    @Override
    public void verifyAccountToken(String token) {
        var confirmationEntity = getUserConfirmation(token);
        var userEntity = getUserEntityByEmail(confirmationEntity.getUserEntity().getEmail());
        userEntity.setEnabled(true);
        userRepository.save(userEntity);
        confirmationRepository.delete(confirmationEntity);
    }

    private UserEntity getUserEntityByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email).orElseThrow(()->new ApiException("User Email Not found"));
    }

    private ConfirmationEntity getUserConfirmation(String token) {
        return confirmationRepository.findBykey(token).orElseThrow(()->new ApiException("Token Not found or expired"));
    }

    private UserEntity createNewUser(String firstName,String lastName, String email)
    {
        var role = getRoleName(Authority.USER.name());
        // We can't use value here as name is stored in DB not the corresponding value
        return UserUtil.createuserEntity(firstName,lastName,email,role);
    }


}
