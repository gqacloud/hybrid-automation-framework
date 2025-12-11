package com.framework.utils;

import java.io.FileReader;

import com.google.gson.Gson;

public class JsonDataReader {
	
	public static <T> T loadJson(String path, Class<T> clazz) {
        try {
            return new Gson().fromJson(new FileReader(path), clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read JSON file: " + path + " → " + e.getMessage());
        }
    }
}
