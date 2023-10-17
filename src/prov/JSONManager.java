package prov;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.LinkedHashMap;

public class JSONManager {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private JSONManager() {
    }

    public static Map<String, Object> loadJSON(String jsonFilePath) throws Exception {
        checkJSONFilePath(jsonFilePath);
        return objectMapper.readValue(new File(jsonFilePath), new TypeReference<LinkedHashMap<String, Object>>(){});
    }

    private static void checkJSONFilePath(String jsonFilePath) {
        if (jsonFilePath == null || jsonFilePath.trim().isEmpty()) {
            throw new IllegalArgumentException("JSON file path cannot be null or empty.");
        }
    }
    
    public static String getJSONFileName(String jsonFilePath) {
        Path path = Paths.get(jsonFilePath);
        String fileNameWithExtension = path.getFileName().toString();
        return fileNameWithExtension.replace(".json", "");
    }
}


