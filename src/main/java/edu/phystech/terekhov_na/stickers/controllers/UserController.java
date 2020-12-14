package edu.phystech.terekhov_na.stickers.controllers;

import edu.phystech.terekhov_na.stickers.dao.UserDao;
import edu.phystech.terekhov_na.stickers.model.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserDao userDao;

    @PostMapping("/sign_in_error")
    public ResponseEntity<?> wrongSignIn(@RequestBody User user) {
        log.info("Error login for {}", user);
        var result = userDao.getUserByLoginOrEmail(user.getLogin(), user.getEmail());
        if(result.isLeft()) {
            return ResponseUtils.buildError(result.getLeft());
        }
        User originalUser = result.get();
        if(!originalUser.getPassword().equals(user.getPassword())) {
            return ResponseUtils.buildError("wrong password");
        }
        return ResponseUtils.buildError("unknown error");
    }

    @PostMapping("/sign_up")
    public ResponseEntity<?> signUpUser(@RequestBody User user) {
        log.info("Request to sign up user {}", user);
        return userDao.addUser(user).<ResponseEntity<?>>map(ResponseEntity::ok)
                .getOrElseGet(ResponseUtils::buildError);
    }

    @PostMapping("/restore_data")
    public ResponseEntity<?> restoreData(@RequestBody User user) {
        log.info("Request to restore data {}", user);
        var result = userDao.getUserByLoginOrEmail(user.getLogin(), user.getEmail());
        if(result.isLeft()) {
            return ResponseUtils.buildError(result.getLeft());
        }
        return ResponseEntity.ok().body(result.get());
    }

    @PostMapping("/user")
    public ResponseEntity<?> getData(@AuthenticationPrincipal String userId) {
        log.info("Get data of user {}", userId);
        if ("anonymousUser".equals(userId)) {
            return ResponseUtils.makeOk();
        }
        return userDao.getUserById(userId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .getOrElseGet(ResponseUtils::buildError);
    }

    @GetMapping("/users")
    @Transactional
    public ResponseEntity<?> logAllUsers() {
        log.info("log all users");
        userDao.logAllUsers();
        return ResponseUtils.makeOk();
    }
}
