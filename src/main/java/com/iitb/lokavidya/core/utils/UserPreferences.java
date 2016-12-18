package com.iitb.lokavidya.core.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
	}
	
	public void createNewJson() {
		json = new JsonObject();
		JsonParser p = new JsonParser();
		
		// display instructions
		JsonObject displayInstructions = new JsonObject();
		displayInstructions.add("readInstructions", p.parse("n"));
		displayInstructions.add("openPresentation", p.parse("n"));
		displayInstructions.add("openVideo", p.parse("n"));
		displayInstructions.add("openAndroidProject", p.parse("n"));
		displayInstructions.add("openPdf", p.parse("n"));
		json.add("displayInstructions", displayInstructions);	
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
	
	public boolean updateDisplayInstruction(String property, String value) {
		JsonObject displayInstructions = json.get("displayInstructions").getAsJsonObject();
		JsonElement jsonValue = displayInstructions.get(property);
		if(jsonValue == null || jsonValue.isJsonNull()) {
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
	
	public static void main(String [] args) {
		UserPreferences u = new UserPreferences();
	}
	
}
