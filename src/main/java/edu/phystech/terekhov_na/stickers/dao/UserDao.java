package edu.phystech.terekhov_na.stickers.dao;

import edu.phystech.terekhov_na.stickers.model.*;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@AllArgsConstructor
public class UserDao {
    private final UserRepository userRepository;
    private final TabRepository tabRepository;
    private final UserDataRepository userDataRepository;

    public Either<String, User> getUserById(String userId) {
        long id;
        try {
            id = Long.parseLong(userId);
        }
        catch (NumberFormatException e) {
            return Either.left("not such user");
        }
        return userRepository.findById(id).<Either<String, User>>map(Either::right)
                .orElse(Either.left("not such user"));
    }

    public Either<String, User> getUserByLoginOrEmail(String login, String email) {
        if (login != null && !login.isEmpty()) {
            return userRepository.findByLogin(login).<Either<String, User>>map(Either::right)
                    .orElse(Either.left("unknown login"));
        }
        else {
            return userRepository.findByEmail(email).<Either<String, User>>map(Either::right)
                    .orElse(Either.left("unknown email"));
        }
    }

    public Either<String, Tab> getActiveTab(User user) {
        var userData = user.getUserData();
        if (userData == null) {
            return Either.left("no user data");
        }
        Long activeTab = userData.getActiveTabId();
        if (activeTab == null) {
            return Either.left("select tab first");
        }
        return userData.getTabs().stream().filter(tab -> tab.getId().equals(activeTab))
                .findAny().<Either<String, Tab>>map(Either::right)
                .orElse(Either.left("wrong active tab"));
    }

    public Either<String, Tab> getActiveTab(String userId) {
        return getUserById(userId).flatMap(this::getActiveTab);
    }

    public Optional<String> addTab(User user, Tab tab) {
        if(tab.getId() != null && tabRepository.findById(tab.getId()).isPresent()) {
            return Optional.of("invalid tab");
        }

        UserData userDataToUpdate = userDataRepository.getOne(user.getUserData().getId());
        userDataToUpdate.getTabs().add(
                Tab.builder().title(tab.getTitle()).tasks(List.of()).build());
        userDataRepository.save(userDataToUpdate);

        if(userDataToUpdate.getActiveTabId() == null) {
            userDataToUpdate.setActiveTabId(userDataToUpdate.getTabs().get(0).getId());
            userDataRepository.save(userDataToUpdate);
        }
        return Optional.empty();
    }

    public Optional<String> setActiveTab(User user, Long tabId) {
        UserData userDataToUpdate = userDataRepository.getOne(user.getUserData().getId());
        if(userDataToUpdate.getTabs().stream().noneMatch(tab -> tab.getId().equals(tabId))) {
            return Optional.of("no such tab");
        }
        userDataToUpdate.setActiveTabId(tabId);
        userDataRepository.save(userDataToUpdate);
        return Optional.empty();
    }

    public Either<String, User> addUser(User user) {
        if(user.getLogin() == null) {
            return Either.left("login is null");
        }
        if(user.getEmail() == null) {
            return Either.left("email is null");
        }
        if(user.getPassword() == null) {
            return Either.left("password is null");
        }
        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            return Either.left("email exists");
        }
        if(userRepository.findByLogin(user.getLogin()).isPresent()) {
            return Either.left("login exists");
        }
        return Either.right(userRepository.save(User.builder()
                .email(user.getEmail())
                .login(user.getLogin())
                .userData(UserData.builder().tabs(List.of()).build())
                .password(user.getPassword()).build()));
    }

    public void logAllUsers() {
        userRepository.findAll().forEach(System.out::println);
    }
}
