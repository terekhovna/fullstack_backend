package edu.phystech.terekhov_na.stickers.controllers;

import edu.phystech.terekhov_na.stickers.dao.UserDao;
import edu.phystech.terekhov_na.stickers.model.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


@RestController
//@CrossOrigin(value = "http://localhost:3000", allowCredentials = "true")
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserDao userDao;

//    @PostMapping("/sign_in")
//    public ResponseEntity<?> getUserByLoginOrEmail(@RequestBody User user, HttpServletResponse response) {
//        log.info("Request to login {}", user);
//        var result = userDao.getUserByLoginOrEmail(user.getLogin(), user.getEmail());
//        if(result.isLeft()) {
//            return ResponseUtils.buildError(result.getLeft());
//        }
//        User originalUser = result.get();
//        if(!originalUser.getPassword().equals(user.getPassword())) {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//        Cookie cookie = new Cookie("user_id", originalUser.getId().toString());
//        response.addCookie(cookie);
//        return ResponseEntity.ok().body(originalUser);
//    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
//        Cookie cookie = new Cookie("user_id", "0");
//        response.addCookie(cookie);
        return ResponseUtils.makeOk();
    }

    @PostMapping("/sign_up")
    public ResponseEntity<?> signUpUser(@RequestBody User user, HttpServletResponse response) {
        return userDao.addUser(user).<ResponseEntity<?>>map(u -> {
//            Cookie cookie = new Cookie("user_id", u.getId().toString());
//            response.addCookie(cookie);
            return ResponseEntity.ok(u);
        }).getOrElseGet(ResponseUtils::buildError);
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

    @RequestMapping("/perform_login")
    public ResponseEntity<?> performLogin() {
        return ResponseEntity.ok().build();
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
}
