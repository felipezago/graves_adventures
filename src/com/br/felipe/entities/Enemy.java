package com.br.felipe.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.br.felipe.main.Game;
import com.br.felipe.main.Sound;
import com.br.felipe.world.Camera;
import com.br.felipe.world.World;

public class Enemy extends Entity{
	
	private double speed = 0.7;
	//private int maskx = 8, masky = 10, maskw = 10, maskh = 10;
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 1;
	private BufferedImage[] sprites;
	private int life = 10;
	private boolean isDamaged = false;
	private int damageFrames = 10, damageCurrent = 0;
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		sprites = new BufferedImage[2];
		sprites[0] = Game.spritesheet.getSprite(112, 16, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(112+16, 16, 16, 16);
	}
	
	public void tick() {
		
		if(calculateDistance(this.getX(), this.getY(), Game.player.getX(), Game.player.getY()) < 40) { 
					
		if(isCollidingWithPlayer() == false) {
			if((int)x < Game.player.getX() && World.isFree((int)(x+speed), (int)this.getY())
					&& !isColliding((int)(x+speed), (int)this.getY())) {
				x+=speed;
			}else if((int)x > Game.player.getX() && World.isFree((int)(x-speed), (int)this.getY())
					&& !isColliding((int)(x-speed), (int)this.getY())) {
				x-=speed;
			}
			
			if((int)y < Game.player.getY() && World.isFree((int)this.getX(), (int)(y+speed))
					&& !isColliding((int)this.getX(), (int)(y+speed))) {
				y+=speed;
			}else if((int)y > Game.player.getY() && World.isFree((int)this.getX(), (int)(y-speed))
					&& !isColliding((int)this.getX(), (int)(y-speed))) {
				y-=speed;
			}
			}else {
				//Colidindo
				if(Game.rand.nextInt(100) < 10) {
					Sound.hurtEffect.play();
					Game.player.life-=5;
					Game.player.isDamaged = true;
					System.err.println("Vida: "+Game.player.life);
				}				
				
			}
			}else {
				
			}
		
		frames++;
		if(frames == maxFrames) {
			frames = 0;
			index++;
			if(index > maxIndex) {
				index = 0;
			}
		}
		
		collidingBullet();
		
		if(life <=  0) {
			destroySelf();
			return;
			
		}
		
		if(isDamaged) {
			this.damageCurrent++;
			if(this.damageCurrent == this.damageFrames) {
				this.damageCurrent = 0;
				this.isDamaged = false;
			}
		}
	}
	
	public void collidingBullet(){
		for(int i = 0; i<Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);
			if(e instanceof BulletShoot) {
				if(Entity.isColliding(this, e)){
					isDamaged = true;
					life--;
					Game.bullets.remove(i);
					return;
				}
			}
		}
	
	}
	
	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
	
	public boolean isCollidingWithPlayer() {
		Rectangle enemyCurrent= new Rectangle((int)this.getX(), (int)this.getY(),World.TILE_SIZE, World.TILE_SIZE);
		Rectangle player= new Rectangle((int)Game.player.getX(), (int)Game.player.getY(), 16, 16);
		
		return enemyCurrent.intersects(player);
	}
	
	public boolean isColliding(int xnext, int ynext) {
		Rectangle enemyCurrent= new Rectangle(xnext, ynext,World.TILE_SIZE, World.TILE_SIZE);
		
		for(int i=0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			
			if(e == this)
				continue;
			
			Rectangle targetEnemy = new Rectangle((int)e.getX(), (int)e.getY(),World.TILE_SIZE, World.TILE_SIZE);
			
			if(enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
			
		}
		
		return false;
	}
	
	public void render(Graphics g) {
		if(!isDamaged) {
			g.drawImage(sprites[index], (int)this.getX() - Camera.x, (int)this.getY() - Camera.y, null);
		}else {
			g.drawImage(Entity.ENEMY_FEEDBACK, (int)this.getX() - Camera.x, (int)this.getY() - Camera.y, null);
		}
		
		//g.setColor(Color.blue);
		//g.fillRect((int)this.getX() + maskx - Camera.x, (int)this.getY() + masky - Camera.y, maskw, maskh);
	}

}
