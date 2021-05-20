package com.br.felipe.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import com.br.felipe.world.World;

public class Menu {
	
	public String[] options = {"Novo Jogo", "Carregar Jogo", "Sair", "Continuar"};
	
	public int currentOption = 0;
	
	public int maxOption = options.length - 1;
	
	public boolean up, down, enter; 
	
	public static boolean pause= false, saveExists = false, saveGame = false;
	
	public void tick() {
		
		File file= new File("save.txt");
		
		if(file.exists()) {
			saveExists = true;
		}else {
			saveExists = false;
		}
		
		if(up) {
			up = false;
			currentOption--;
			if(currentOption < 0) {
				currentOption = maxOption;
			}
		}
		
		if(down) {
			down = false;
			currentOption++;
			if(currentOption > maxOption) {
				currentOption = 0;
			}
		}
		
		if(enter) {
			enter= false;
			if(options[currentOption] == "Novo Jogo" || options[currentOption] == "Continuar") {
				Game.gameState="NORMAL";
				pause= false;
				file = new File("save.txt");
				file.delete();
			}else if(options[currentOption] == "Carregar Jogo") {
				file = new File("save.txt");
				
				if(file.exists()) {
					String saver = loadGame(10);
					applySave(saver);
				}
			}else if(options[currentOption] == "Sair") {
				System.exit(1);
			}
		}
	}
	
	public static String loadGame(int encode) {
		String line = "";
		File file = new File("save.txt");
		
		if(file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
				try {
					while((singleLine = reader.readLine()) != null) {
						String[] trans = singleLine.split(":");
						char[] val = trans[1].toCharArray();
						trans[1] = "";
						for(int i =0; i < val.length; i++) {
							val[i]-=encode;
							trans[1] +=val[i];						
						}
						line+=trans[0];
						line+=":";
						line+=trans[1];
						line+="/";
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}catch(FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		return line;
	}
	
	public static void applySave(String str) {
		String[] spl = str.split("/");
		for(int i = 0; i <spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			switch(spl2[0]) 
			{
				case "level":
					System.out.println(spl2[1]);
					World.restartGame("level"+spl2[1]+".png");
					Game.gameState = "NORMAL";
					pause = false;
					break;
					
				case "vida":
					Game.player.life = Integer.parseInt(spl2[1]);
					break;
			}
		}
	}
	
	public static void saveGame(String[] val1, int[] val2, int encode) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter("save.txt"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < val1.length; i++) {
			String current = val1[i];
			current += ":";
			char[] value = Integer.toString(val2[i]).toCharArray();
			
			for(int n = 0; n < value.length; n++) {
				value[n]+= encode;
				current += value[n];
			}
			
			try {
				writer.write(current);
				if(i < val1.length - 1) {
					writer.newLine();
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			writer.flush();
			writer.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0,0,0,100));
		g2.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.yellow);
		g.setFont(new Font("Arial", Font.BOLD, 36));
		g.drawString("> Graves Adventures <", (Game.WIDTH*Game.SCALE)/2 - 180, (Game.HEIGHT*Game.SCALE)/2 - 160);
		
		//opc
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 32));
		if(pause == false) {
			g.drawString("Novo jogo", (Game.WIDTH*Game.SCALE)/2 - 65, 160);
		}else {
			g.drawString("Resumir", (Game.WIDTH*Game.SCALE)/2 - 55, 160);
		}
		
		g.drawString("Carregar jogo", (Game.WIDTH*Game.SCALE)/2 - 95, 220);
		g.drawString("Sair", (Game.WIDTH*Game.SCALE)/2 - 15, 280);
		
		if(options[currentOption] == "Novo Jogo") {
			g.drawString(">", (Game.WIDTH*Game.SCALE)/2 - 150, 160);
		}else if(options[currentOption] == "Carregar Jogo") {
			g.drawString(">", (Game.WIDTH*Game.SCALE)/2 - 150, 220);
		}else if(options[currentOption] == "Sair") {
			g.drawString(">", (Game.WIDTH*Game.SCALE)/2 - 150, 280);
		}
		
		
	}
	
}
