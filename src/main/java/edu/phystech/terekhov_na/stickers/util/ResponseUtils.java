package edu.phystech.terekhov_na.stickers.util;

import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class ResponseUtils {
    public static ResponseEntity<?> buildError(String msg) {
        return ResponseEntity.badRequest().body(ErrorToJson.errorToJson(msg));
    }

    public static ResponseEntity<?> makeResponse(Optional<String> error) {
        return error.<ResponseEntity<?>>map(ResponseUtils::buildError)
                .orElse(ResponseUtils.makeOk());
    }

    public static ResponseEntity<?> makeOk() {
        return ResponseEntity.ok("{}");
    }
}
