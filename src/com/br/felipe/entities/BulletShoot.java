package com.br.felipe.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.br.felipe.main.Game;
import com.br.felipe.world.*;

public class BulletShoot extends Entity{
	
	private double dx;
	private double dy;
	private double spd = 4;
	
	private int life = 30,curLife = 0;
	
	
	public BulletShoot(int x, int y, int width, 
			int height, BufferedImage sprite,
			double dx,double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}
	
	public void tick() {
		x+=dx*spd;
		y+=dy*spd;
		curLife++;
		if(curLife == life) {
			Game.bullets.remove(this);
			return;
		}
		
	}
	
	public void render(Graphics g){
		g.setColor(Color.YELLOW);
		g.fillOval((int)this.getX() - Camera.x, (int)this.getY() - 
				Camera.y,width,height);
	}
	
	public Double getSpeed() {
		return this.spd;
	}

}
