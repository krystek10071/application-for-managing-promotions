package com.example.managingpromotions.services;

import com.example.managingpromotions.exception.DataValidationException;
import com.example.managingpromotions.mapper.UserAppMapper;
import com.example.managingpromotions.model.UserApp;
import com.example.managingpromotions.model.enums.RoleUserEnum;
import com.example.managingpromotions.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.managingPromotions.api.model.RegistrationCredentials;
import pl.managingPromotions.api.model.UserAppResponseDTO;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserAppMapper userAppMapper;

    private final UserRepository userRepository;

    @Transactional
    public UserAppResponseDTO createUserAccount(RegistrationCredentials registrationCredentials) {

        credentialsValidation(registrationCredentials);

        if (!checkUserIfExist(registrationCredentials)) {
            throw new DataValidationException("the username: " + registrationCredentials.getUsername() + " is already exist");
        }

        String encryptedPassword = "{bcrypt}" + new BCryptPasswordEncoder().encode(registrationCredentials.getPassword());

        UserApp userApp = UserApp.builder()
                .username(registrationCredentials.getUsername())
                .password(encryptedPassword)
                .role(RoleUserEnum.USER)
                .isEnabled(true)
                .build();

        UserApp savedUser = userRepository.save(userApp);
        log.info("User with id: {} is saved", savedUser.getId());

        return userAppMapper.mapUserAppToUserAppMapper(userApp);
    }

    private boolean checkUserIfExist(RegistrationCredentials credentials) {

        return userRepository.findByUsername(credentials.getUsername()).isPresent();
    }


    private void credentialsValidation(RegistrationCredentials credentials) {

        if (!credentials.getPassword().equals(credentials.getPasswordConfirmation())) {
            throw new DataValidationException("Password confirmation and password should be identical");
        }
    }
}
