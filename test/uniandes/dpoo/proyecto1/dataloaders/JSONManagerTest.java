package uniandes.dpoo.proyecto1.dataloaders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.util.Map;

public class JSONManagerTest {

    @TempDir
    Path tempDir;

    String testJSONPath;

    @BeforeEach
    void setUp() throws IOException {
        testJSONPath = tempDir.resolve("test.json").toString();
        String jsonContent = "{\n" +
                "  \"insurance_name\": { \"type\": \"String\", \"primaryKey\": true },\n" +
                "  \"insurance_cost_per_day\": { \"type\": \"Integer\" }\n" +
                "}";
        Files.write(tempDir.resolve("test.json"), jsonContent.getBytes());
    }

    @Test
    void testLoadJSON() throws Exception {
        Map<String, Object> data = JSONManager.loadJSON(testJSONPath);
        Assertions.assertNotNull(data);
        Assertions.assertTrue(data.containsKey("insurance_name")); 
        Assertions.assertTrue(data.containsKey("insurance_cost_per_day"));
    }

    @Test
    void testLoadJSONWithInvalidPath() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            JSONManager.loadJSON("");
        });
    }

    @Test
    void testGetJSONFileName() {
        String fileName = JSONManager.getJSONFileName(testJSONPath);
        Assertions.assertEquals("test", fileName); 
    }
}
