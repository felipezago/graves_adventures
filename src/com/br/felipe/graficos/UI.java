package com.br.felipe.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.br.felipe.main.Game;

public class UI {
	
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(8, 4, 70, 8);
		g.setColor(Color.green);
		g.fillRect(8, 4, (int)((Game.player.life/Game.player.maxlife)*70), 8);
		g.setFont(new Font("Arial", Font.BOLD, 9));
		g.setColor(Color.black);
		g.drawString((int)Game.player.life+"/"+(int)Game.player.maxlife, 30, 11);
		
		if(Game.player.ammo == 0) {
			g.setFont(new Font("Arial", Font.BOLD, 10));
			g.setColor(Color.red);
			g.drawString("Sem Munição", 110, 11);
		}
		
		/*g.setFont(new Font("Arial", Font.BOLD, 15));
		g.setColor(Color.red);
		g.drawString(""+(int)(Player.life-Player.maxlife), (int)Game.player.getX(), (int)Game.player.getY());*/
	}
	
}
