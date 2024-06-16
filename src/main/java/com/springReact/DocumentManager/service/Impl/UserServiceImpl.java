package com.springReact.DocumentManager.service.Impl;

import com.springReact.DocumentManager.cache.CacheStore;
import com.springReact.DocumentManager.domain.RequestContext;
import com.springReact.DocumentManager.dto.User;
import com.springReact.DocumentManager.entity.ConfirmationEntity;
import com.springReact.DocumentManager.entity.CredentialEntity;
import com.springReact.DocumentManager.entity.RoleEntity;
import com.springReact.DocumentManager.entity.UserEntity;
import com.springReact.DocumentManager.enumeration.Authority;
import com.springReact.DocumentManager.enumeration.EventType;
import com.springReact.DocumentManager.enumeration.LoginType;
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

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

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

    //Please note here we are Autowiring using name
    private final CacheStore<String,Integer> userLoginCache;

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

    @Override
    public void updateLoginAttempt(String email, LoginType loginType) {
        var userEntity = getUserEntityByEmail(email);
        RequestContext.setUserId(userEntity.getId());
        switch (loginType){
            case LOGIN_ATTEMPT -> {
                if(userLoginCache.get(userEntity.getEmail())==null) {
                    //Please note if email doesn't exist in cache means no login attempt was made in last 15 mins,
                    // so we can allow the user attempt to login
                    userEntity.setLoginAttempts(0);
                    userEntity.setAccountNonLocked(true);
                }
                userEntity.setLoginAttempts(userEntity.getLoginAttempts()+1);
                userLoginCache.put(userEntity.getEmail(),userEntity.getLoginAttempts());
                if(userLoginCache.get(userEntity.getEmail())>5) {
                    userEntity.setAccountNonLocked(false);
                }

            }
            case LOGIN_SUCCESS -> {
                userEntity.setAccountNonLocked(true);
                userEntity.setLoginAttempts(0);
                userEntity.setLastLogin(LocalDateTime.now());
                userLoginCache.evict(userEntity.getEmail());

            }
        }
        userRepository.save(userEntity);
    }

    @Override
    public User getUserByUserId(String userId) {
        var userEntity = userRepository.findUserEntityByUserId(userId).orElseThrow(()-> new ApiException("Unable to find User by ID"));
        return UserUtil.fromUserEntity(userEntity,userEntity.getRole(),getUserCredentialsById(userEntity.getId()));
    }

    @Override
    public User getUserByEmail(String email) {
        var userEntity = getUserEntityByEmail(email);


        return UserUtil.fromUserEntity(userEntity,userEntity.getRole(),getUserCredentialsById(userEntity.getId()));
    }



    @Override
    public CredentialEntity getUserCredentialsById(Long userId) {
        var credentialEntity = credentialRepository.getCredentialsByUserEntityId(userId);
        return credentialEntity.orElseThrow(()-> new ApiException("Unable to find User credentials"));
    }

}
