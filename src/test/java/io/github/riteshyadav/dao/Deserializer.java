package io.github.riteshyadav.dao;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Deserializer<T> {


    public List<T> getDataList(String jsonFileName, Type type) {

        Gson gson = new Gson();
        List<T> json = new ArrayList<>();

        String environment = System.getProperty("env");

        try (BufferedReader bReader = new BufferedReader(new
                InputStreamReader(getClass().getClassLoader().getResourceAsStream(String.format("%s/%s", environment, jsonFileName))))) {

            json = gson.fromJson(bReader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }
}
