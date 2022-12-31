package org.ccu.core.config.internal;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.ccu.core.CCU;

public class ConfigObjectClass {

  private CCU addon;

  private File configFile;
  private JsonObject defaultJsonObject;
  private JsonArray defaultJsonArray;
  private JsonObject defaultJsonArrayLayout;
  private JsonObject jsonObjectData;
  private JsonArray jsonArrayData;


  public ConfigObjectClass make(CCU addon, File cFile, JsonObject dJsonObject) {
    this.configFile = cFile;
    this.defaultJsonObject = dJsonObject;
    this.addon = addon;
    return this;
  }

  public ConfigObjectClass make(CCU addon, File cFile, JsonArray dJsonArray, JsonObject dJsonObject) {
    this.configFile = cFile;
    this.defaultJsonArray = dJsonArray;
    this.defaultJsonArrayLayout = dJsonObject;
    this.addon = addon;
    return this;
  }

  public void configure() {
    if (!this.configFile.exists()) {
      String jsonString;
      if (this.defaultJsonObject == null) {
        jsonString = this.defaultJsonArray.toString();
      } else {
        jsonString = this.defaultJsonObject.toString();
      }
      writeToFile(jsonString);
    } else {
      if (this.defaultJsonObject == null) {
        try {
          this.jsonArrayData = (new Gson()).fromJson(new FileReader(this.configFile.getPath()), JsonArray.class);
        } catch (FileNotFoundException e) {
          this.addon.logger().error(this.configFile.getName() + " was not found! CCU might not work as expected.");
          e.printStackTrace();
        }
        validate(this.jsonArrayData, this.defaultJsonArrayLayout);
      } else {
        try {
          this.jsonObjectData = (new Gson()).fromJson(new FileReader(this.configFile.getPath()), JsonObject.class);
        } catch (FileNotFoundException e) {
          this.addon.logger().error(this.configFile.getName() + " was not found! CCU might not work as expected.");
          e.printStackTrace();
        }
        validate(this.jsonObjectData, this.defaultJsonObject);
      }
    }
  }

  private void validate(JsonArray configData, JsonObject defaultConfigData) {
    boolean changed = false;

    if (configData.size() == 0) {
      JsonArray jsonArray = new JsonArray();
      jsonArray.add(defaultConfigData);
      writeToFile(jsonArray.toString());
    } else {
      for (JsonElement jsonElement : configData) {
        for (String key :  defaultConfigData.keySet()) {
          if (!jsonElement.getAsJsonObject().has(key)) {
            jsonElement.getAsJsonObject().add(key, defaultConfigData.get(key));
            changed = true;
          }
        }
      }

      if (changed) {
        writeToFile(configData.toString());
      }

    }
  }

  private void validate(JsonObject configData, JsonObject defaultConfigData) {
    boolean changed = false;
    for (String key: defaultConfigData.keySet()) {
      if (!configData.has(key)) {
        configData.add(key, defaultConfigData.get(key));
        changed = true;
      }
    }
    if (changed) {
      writeToFile(configData.toString());
    }
  }
  public void save() {
    String jsonString;
    if (this.defaultJsonObject == null) {
      jsonString = this.jsonArrayData.toString();
    } else {
      jsonString = this.jsonObjectData.toString();
    }
    writeToFile(jsonString);
  }
  private void writeToFile(String jsonString) {
    if (jsonString != null) {
      try {
        FileWriter file = new FileWriter(this.configFile.getPath());
        file.write(jsonString);
        file.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public int size() {
    return this.jsonArrayData.size();
  }

  public JsonElement get(int i) {
    return this.jsonArrayData.get(i);
  }

  public JsonElement get(String memberName) {
    return this.jsonObjectData.get(memberName);
  }

  public boolean has(String memberName) { return this.jsonObjectData.has(memberName);}

  public JsonArray getAsJsonArray() {return this.jsonArrayData;}

  public JsonObject getAsJsonObject() {return  this.jsonObjectData;}

  public void addDefaultToArray() {
    this.jsonArrayData.add(this.defaultJsonArrayLayout);
  }

}
