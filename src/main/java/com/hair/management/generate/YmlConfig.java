package com.hair.management.generate;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class YmlConfig {
    private static Map<String, String> allMap = new HashMap<>();

    public static String getValue(String key){
        return allMap.get(key);
    }

    static {
        Yaml yaml = new Yaml();
        InputStream inputStream = YmlConfig.class.getResourceAsStream("/application.yml");
        Iterator<Object> result = yaml.loadAll(inputStream).iterator();
        while (result.hasNext()) {
            Map map = (Map) result.next();
            iteratorYml(map, null);
        }
    }

    public static void iteratorYml(Map map, String key) {
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Object key2 = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof LinkedHashMap) {
                if (key == null) {
                    iteratorYml((Map) value, key2.toString());
                } else {
                    iteratorYml((Map) value, key + "." + key2.toString());
                }
            }
            if (value instanceof String|| value instanceof Integer) {
                if (key == null) {
                    allMap.put(key2.toString(), value.toString());
                }
                if (key != null) {
                    allMap.put(key + "." + key2.toString(), value.toString());
                }
            }
        }
    }
}
