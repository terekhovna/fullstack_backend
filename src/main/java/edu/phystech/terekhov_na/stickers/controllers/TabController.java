package edu.phystech.terekhov_na.stickers.controllers;

import edu.phystech.terekhov_na.stickers.dao.UserDao;
import edu.phystech.terekhov_na.stickers.model.Tab;
import edu.phystech.terekhov_na.stickers.model.UserRepository;
import edu.phystech.terekhov_na.stickers.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TabController {
    private final Logger log = LoggerFactory.getLogger(TabController.class);
    private final UserDao userDao;
    private final UserRepository userRepository;

    @GetMapping("/tabs")
    public ResponseEntity<?> getData(@AuthenticationPrincipal String userId) {
        log.info("Get tabs for user {}", userId);
        return userDao.getUserById(userId).<ResponseEntity<?>>map(
                user -> ResponseEntity.ok(user.getUserData()))
                .getOrElseGet(ResponseUtils::buildError);
    }

    @PostMapping("/tabs")
    public ResponseEntity<?> addTab(@RequestBody Tab tab, @AuthenticationPrincipal String userId) {
        log.info("Add tab {} for user {}", tab, userId);
        return userDao.getUserById(userId).<ResponseEntity<?>>map(user ->
                ResponseUtils.makeResponse(userDao.addTab(user, tab)))
                .getOrElseGet(ResponseUtils::buildError);
    }

    @PostMapping("/tabs/{id}")
    public ResponseEntity<?> changeActiveTab(@AuthenticationPrincipal String userId, @PathVariable Long id) {
        log.info("change active tab for user {} on {}", userId, id);
        return userDao.getUserById(userId).<ResponseEntity<?>>map(user ->
                ResponseUtils.makeResponse(userDao.setActiveTab(user, id)))
                .getOrElseGet(ResponseUtils::buildError);
    }
}
