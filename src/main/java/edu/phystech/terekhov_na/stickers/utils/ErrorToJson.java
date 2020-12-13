package edu.phystech.terekhov_na.stickers.utils;

import org.json.JSONObject;

public class ErrorToJson {
    public static String errorToJson(String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error", message);
        return jsonObject.toString();
    }
}
