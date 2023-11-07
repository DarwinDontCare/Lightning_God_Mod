package net.darwindontcare.lighting_god.utils;

import com.google.gson.Gson;
import net.darwindontcare.lighting_god.player_anim.AnimationData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class JsonReader {
    public static AnimationData ReadJson(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            // Leia o arquivo linha por linha e armazene-o em uma string
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }

            Gson gson = new Gson();

            AnimationData data = gson.fromJson(jsonContent.toString(), AnimationData.class);

            return data;
        } catch (Exception exception) {
            System.out.println(exception.toString());
            return null;
        }
    }
}
