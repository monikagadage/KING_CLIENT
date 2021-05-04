package com.KindIsDeadPlayer;

import static java.nio.file.StandardOpenOption.WRITE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utility {

	
	private String PlayerName;
	private String writefilepath;
	private String readfilepath;
	private static volatile Utility Utility = null;

	Utility() {
		// private constructor
	}
	//singleton object creation
	public static Utility getInstance() {
		if (Utility == null) {
			synchronized (Utility.class) {
				if (Utility == null) {
					Utility = new Utility();
				}
			}
		}
		return Utility;
	}

	public void SetPlayerName(String name) {
		this.PlayerName=name;
	}
	
	public String GetPlayerName() {
		return this.PlayerName;
	}
	
	public  String getFileWritePath() {
		return writefilepath;
	}
	public void setFileWritePath(String fileWritePath) {
		this.writefilepath = fileWritePath;
	}
	
	public static void readFile(String filename, boolean canBreak) throws InterruptedException, IOException {
		canBreak = false;
		String line;
		try {

			FileInputStream fInP1 = new FileInputStream(filename);
			BufferedReader bReadP1 = new BufferedReader(new InputStreamReader(fInP1));
			while (!canBreak) {
				line = bReadP1.readLine();
				if (line == null || line.isEmpty()) {				
					Thread.sleep(3000);
					continue;
				}
				canBreak = parseMessage(line);
				clearTheFile(filename);
				fInP1.getChannel().position(0);
				bReadP1 = new BufferedReader(new InputStreamReader(fInP1));
				if (canBreak)
					break;
			}
			bReadP1.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void clearTheFile(String fileName) throws IOException {
		
        FileWriter fwOb = new FileWriter(fileName, false); 
        PrintWriter pwOb = new PrintWriter(fwOb, false);
        pwOb.flush();
        pwOb.close();
        fwOb.close();
    }
	
	public static Boolean parseMessage(String message) throws InterruptedException {
		
		String[] messageArray = message.split(":");
		String[] messageDetails = messageArray[1].split(",");
		String messageNumber = messageArray[0];
		List<String> messageDetailsList = new ArrayList<String>(Arrays.asList(messageDetails));

		if ("02".equals(messageNumber))
			PlayerProcessing.populateMapRandomly(messageNumber, messageDetailsList);
		else if("03".equals(messageNumber))
			PlayerProcessing.distributeRandomFollowers(messageNumber, messageDetailsList);
		else if("04".equals(messageNumber))
			PlayerProcessing.randomlyNameRegion(messageNumber, messageDetailsList);
		
		return false;
		
	}
	
	public static void writeFile(String filePath, String message) throws InterruptedException {
		try {
			File file = new File(filePath);
			Path path = Paths.get(filePath);
			OutputStream outputStream = Files.newOutputStream(path, WRITE);
			outputStream.write(message.getBytes());
			outputStream.close();
			Thread.sleep(1000);
			if (file.exists())
	            file.delete();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getReadfilepath() {
		return readfilepath;
	}

	public void setReadfilepath(String readfilepath) {
		this.readfilepath = readfilepath;
	}
	
}
