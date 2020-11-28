package edu.phystech.terekhov_na.stickers.controllers;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class ResponseUtils {
    public static ResponseEntity<?> buildError(String msg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error", msg);
        return ResponseEntity.badRequest().body(jsonObject.toString());
    }

    public static ResponseEntity<?> makeResponse(Optional<String> error) {
        return error.<ResponseEntity<?>>map(ResponseUtils::buildError)
                .orElse(ResponseUtils.makeOk());
    }

    public static ResponseEntity<?> makeOk() {
        return ResponseEntity.ok("{}");
    }
}
