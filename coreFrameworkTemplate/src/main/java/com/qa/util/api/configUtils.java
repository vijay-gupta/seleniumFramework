package com.qa.util.api;

import org.yaml.snakeyaml.Yaml;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class configUtils {

    public static Map<String, Object> getServiceDescription(String relPath, String strKey) {
        InputStream inputStream = null;
        Map<String, Object> ymlSubDoc = null;
        try {
            inputStream = new FileInputStream(
                    new File(System.getProperty("user.dir") + File.separator + relPath));
            Yaml yaml = new Yaml();
            Map<String, Object> ymlDoc = yaml.load(inputStream);
            ymlSubDoc = (Map<String, Object>)ymlDoc.get(strKey);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return ymlSubDoc;
    }

}
