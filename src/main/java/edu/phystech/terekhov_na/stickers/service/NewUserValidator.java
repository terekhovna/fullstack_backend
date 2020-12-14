package edu.phystech.terekhov_na.stickers.service;

import edu.phystech.terekhov_na.stickers.model.User;
import edu.phystech.terekhov_na.stickers.model.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class NewUserValidator {
    private final UserRepository userRepository;
    private static final Pattern emailPatter = Pattern.compile("\\S\\S*@\\S\\S*\\.\\S\\S*");

    private boolean isValidEmail(String email) {
        return emailPatter.matcher(email).matches();
    }
    
    public Optional<String> validateNewUser(User user) {
        if(user.getLogin() == null) {
            return Optional.of("login is null");
        }
        if(user.getEmail() == null) {
            return Optional.of("email is null");
        }
        if(user.getPassword() == null) {
            return Optional.of("password is null");
        }
        if(!isValidEmail(user.getEmail())) {
            return Optional.of("wrong email address");
        }
        if(userRepository.findByLogin(user.getLogin()).isPresent()) {
            return Optional.of("login exists");
        }
        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            return Optional.of("email exists");
        }

        return Optional.empty();
    }
}
