package com.zmark.core.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import java.io.IOException;

/**
 * @author zhengguangchen
 */

public class JsonHelper {
    static ObjectMapper objectMapper = new ObjectMapper();

    public JsonHelper() {
    }

    public static <T> T json2Object(String json, Class<T> beanClass) {
        if (json.isEmpty()) {
            return null;
        } else {
            try {
                return objectMapper.readValue(json, beanClass);
            } catch (JsonParseException var3) {
                var3.printStackTrace();
            } catch (JsonMappingException var4) {
                var4.printStackTrace();
            } catch (IOException var5) {
                var5.printStackTrace();
            }

            return null;
        }
    }

    public static <T> T json2Object(byte[] data, Class<T> beanClass) {
        if (data == null) {
            return null;
        } else {
            try {
                return objectMapper.readValue(data, beanClass);
            } catch (JsonParseException var3) {
                var3.printStackTrace();
            } catch (JsonMappingException var4) {
                var4.printStackTrace();
            } catch (IOException var5) {
                var5.printStackTrace();
            }

            return null;
        }
    }

    public static String toJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }
}
