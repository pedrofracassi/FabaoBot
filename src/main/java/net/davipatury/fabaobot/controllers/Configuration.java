/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.controllers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import net.davipatury.fabaobot.FabaoUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author Davi
 */
public class Configuration {
    
    private JSONObject json;
    private final String path;
    
    public Configuration(String path, boolean createIfDoesntExists, boolean quitIfDoesntExists) {
        this.path = path;
        
        try {
            load();
        } catch (IOException ex) {
            if(createIfDoesntExists) {
                try (FileWriter writer = new FileWriter(path)) {
                    JSONObject newConfig = new JSONObject()
                        .accumulate("bot", new JSONObject()
                            .accumulate("bot_token", "1234567890")
                            .accumulate("prefix", "!")
                            .accumulate("owner_id", "135152303773712384")
                        ).accumulate("memes", new JSONObject()
                            .accumulate("memes_path", "data/memes.json")
                            .accumulate("pastebin_key", "1234567890")
                        ).accumulate("music", new JSONObject()
                            .accumulate("youtube_api_key", "1234567890")
                            .accumulate("twitch_client_id", "1234567890")
                        ).accumulate("apis", new JSONObject()
                            .accumulate("openweather_appid", "1234567890")
                        );
                    
                    writer.write(newConfig.toString(4));
                } catch (FileNotFoundException | UnsupportedEncodingException ex1) {
                } catch (IOException ex1) {}
                
                try {
                    load();
                } catch (IOException ex1) {}
                
                FabaoUtils.logToConsole("Configuration file created!");
            }
            
            if(quitIfDoesntExists) {
                System.exit(0);
            }
        }
    }
    
    private void load() throws IOException {
        json = new JSONObject(new JSONTokener(new FileReader(path)));
    }
    
    public void save() {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(json.toString(4));
        } catch (FileNotFoundException | UnsupportedEncodingException ex1) {
        } catch (IOException ex1) {}
    }
    
    public JSONObject getCategory(String key) {
        return json.optJSONObject(key);
    }
    
    public void addValue(String categoryKey, String key, Object value) {
        JSONObject category = json.optJSONObject(categoryKey);
        if(category == null) {
            json.accumulate(categoryKey, new JSONObject());
            category = json.getJSONObject(categoryKey);
        }
        category.put(key, value);
    }
}
