package com.iitb.lokavidya.core.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserPreferences {
	
	public JsonObject json;
	public File jsonFile;
	
	public UserPreferences() {
		
		jsonFile = new File("resources", "userPreferences.json");
		json = readJsonFromFile(jsonFile.getAbsolutePath());
		if (!jsonFile.exists() || json == null || json.isJsonNull()) {
			// JSON not available, create a new JSON
			System.out.println("UpdateUserPreferences : creating new UserPreferences json");
			createNewJson();
		}
		
		// read json file only if it is not newly created
		if(json == null || json.isJsonNull()) {
			JsonElement jsonElement = readJsonFromFile(jsonFile.getAbsolutePath());
			json = jsonElement.getAsJsonObject();
		}
		
		System.out.println(json.toString());
		persist();
	}
	
	public void persist() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(jsonFile));
			bw.write(json.toString());
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(json.toString());
	}
	
	public void createNewJson() {
		json = getDefaultJsonObject();
	}
	
	public JsonObject readJsonFromFile(String fileUrl) {
		FileReader reader;
		try {
			reader = new FileReader(fileUrl);
			BufferedReader br = new BufferedReader(reader);
			String data = "";
			String currentLine;
			while((currentLine = br.readLine()) != null) {
				data += currentLine;
			}
			return new JsonParser().parse(data).getAsJsonObject();
		} catch (Exception e) {
			// pass
			System.out.println("UpdateUserPreferences.readJsonFromFile : cannot read Json File");
			return null;
		} 
	}
	
	public JsonObject getDefaultJsonObject() {
		JsonObject defaultObject = new JsonObject();
		JsonParser p = new JsonParser();
		
		// display instructions
		JsonObject displayInstructions = new JsonObject();
		displayInstructions.add("readInstructions", p.parse("n"));
		displayInstructions.add("openPresentation", p.parse("n"));
		displayInstructions.add("openVideo", p.parse("n"));
		displayInstructions.add("openAndroidProject", p.parse("n"));
		displayInstructions.add("openPdf", p.parse("n"));
		defaultObject.add("displayInstructions", displayInstructions);
		
		// paths
		JsonObject paths = new JsonObject();
		paths.add("OpenOffice", p.parse(""));
		paths.add("ghostScript", p.parse(""));
		defaultObject.add("paths", paths);
		
		return defaultObject;
	}
	
	public static void validateJsonObject() {
		String input1 = "{\"state\":1,\"cmd\":1}";
        String input2 = "{\"cmd\":1,\"state\":1}";
        ObjectMapper om = new ObjectMapper();
        try {
            Map<String, Object> m1 = (Map<String, Object>)(om.readValue(input1, Map.class));
            Map<String, Object> m2 = (Map<String, Object>)(om.readValue(input2, Map.class));
            System.out.println(m1.keySet());
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void vaildateObject(JsonObject validityObject, JsonObject object) {
		
	}
	
	public boolean updateDisplayInstruction(String property, String value) {
		JsonObject displayInstructions = json.get("displayInstructions").getAsJsonObject();
		JsonElement jsonValue = displayInstructions.get(property);
		if(jsonValue == null) {
			return false;
		}
		jsonValue = new JsonParser().parse(value);
		displayInstructions.remove(property);
		displayInstructions.add(property, jsonValue);
		
		// make changes to json file
		persist();
		
		return true;
	}
	
	public String getDisplayInstruction(String property) {
		JsonObject displayInstructions = json.get("displayInstructions").getAsJsonObject();
		JsonElement value = displayInstructions.get(property);
		if (value == null || value.isJsonNull()) {
			return null;
		} else {
			return value.getAsString();
		}
	}
	
	public boolean updatePath(String property, String value) {
		System.out.println("updatePath : called");
		JsonObject paths = json.get("paths").getAsJsonObject();
		JsonElement jsonValue = paths.get(property);
		if(jsonValue == null) {
			return false;
		}
		
		// get value with escaped forward slash
		String newValue = "";
		char[] charArray = value.toCharArray();
		for(int i=0; i<charArray.length; i++) {
			if(charArray[i] == '/') {
				newValue += "?";
			} else {
				newValue += charArray[i];
			}
		}
		System.out.println(newValue);
		jsonValue = new JsonParser().parse(newValue);
		paths.remove(property);
		paths.add(property, jsonValue);
		
		System.out.println("updatePath : json : " + json.toString());
		
		// make changes to json file
		persist();
		
		return true;
	}
	
	public String getPath(String property) {
		System.out.println("getPath called");
		JsonObject paths = json.get("paths").getAsJsonObject();
		JsonElement value = paths.get(property);
		if (value == null || value.isJsonNull()) {
			return null;
		} 
		
		String valueString = value.getAsString();
		String newValueString = "";
		char[] charArray = valueString.toCharArray();
		for(int i=0; i<charArray.length; i++) {
			if(charArray[i] == '?') {
				newValueString += "/";
			} else {
				newValueString += charArray[i];
			}
		}
		return newValueString;
	}
	
	public static void main(String [] args) {
//		UserPreferences u = new UserPreferences();
		validateJsonObject();
	}
	
}
