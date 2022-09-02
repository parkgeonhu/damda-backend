package com.sikugeon.damda.common.util;

import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public interface JsonUtils {

    static Object readJsonSimpleDemo(String filename) throws Exception {
        FileReader reader = new FileReader(filename);
        JSONParser jsonParser = new JSONParser();
        return jsonParser.parse(reader);
    }
}
