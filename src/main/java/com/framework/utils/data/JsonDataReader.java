package com.framework.utils.data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

public class JsonDataReader {

    private static final Logger logger = LogManager.getLogger(JsonDataReader.class);
    private static final Gson gson = new Gson();  // Reusable instance

    /**
     * Reads JSON file and maps it to given model class.
     *
     * @param path   Absolute JSON file path
     * @param clazz  POJO class (model)
     * @return Mapped object of type T
     */
    public static <T> T loadJson(String path, Class<T> clazz) {

        logger.info("Reading JSON file: {}", path);

        try (FileReader reader = new FileReader(path)) {

            T data = gson.fromJson(reader, clazz);

            if (data == null) {
                throw new RuntimeException("JSON parsing returned null → empty or invalid structure.");
            }

            logger.info("JSON successfully parsed into class: {}", clazz.getSimpleName());
            return data;

        } catch (FileNotFoundException e) {
            logger.error("JSON file not found: {}", path);
            throw new RuntimeException("File not found: " + path);

        } catch (IOException e) {
            logger.error("IO Exception while reading JSON: {}", e.getMessage());
            throw new RuntimeException("IO error reading JSON: " + e.getMessage());

        } catch (Exception e) {
            logger.error("Failed to parse JSON → {}, Error: {}", path, e.getMessage());
            throw new RuntimeException("JSON parsing failed: " + e.getMessage());
        }
    }
}
