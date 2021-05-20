package com.br.felipe.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;


import com.br.felipe.main.Game;
import com.br.felipe.main.Sound;
import com.br.felipe.world.Camera;
import com.br.felipe.world.World;


public class Player extends Entity{

	public boolean right, up, left, down;
	public int rightDir = 0;
	public int leftDir = 1;
	public int dir = rightDir;
	public double speed = 1.4;	
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;	
	private BufferedImage playerDamage;
	public boolean isDamaged = false;
	private int damageFrames = 0;	
	private int frames = 0, maxFrames = 5, index = 0, 
			maxIndex = 3;
	private boolean moved = false;
	public double life = 100, maxlife=100;
	public int ammo = 0;	
	public boolean hasGun = false;	
	public boolean shoot = false;
	public boolean mouseShoot = false;
	public int mx, my;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		// TODO Auto-generated constructor stub
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		playerDamage = Game.spritesheet.getSprite(0, 16, 16, 16);
		
		for(int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16);
		}
		
		for(int i = 0; i < 4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16);
		}
		
		
	}
	
	public void tick() {
		moved = false;
		if(right && World.isFree((int)(x+speed), (int)y)) {
			moved = true;
			dir = rightDir;
			x+=speed;
		
		}
		
		else if(left && World.isFree((int)(x-speed), (int)y)) {
			moved = true;
			dir = leftDir;
			x-=speed;
		}
		
		if(up && World.isFree((int)x, (int)(y-speed))) {
			moved = true;
			y-=speed;
		}
		
		else if(down && World.isFree((int)x, (int)(y+speed))) {
			moved = true;
			y+=speed;
		}
		
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) {
					index = 0;
				}
			}
		}
		
		checkCollisionLifePack();
		checkCollisionAmmo();
		checkCollisionWeapon();
				
		if(isDamaged) {
			this.damageFrames++;
			if(this.damageFrames == 8) {
				this.damageFrames = 0;
				isDamaged = false;
			}
		}
		
		if(shoot){
			shoot = false;
			if(hasGun && ammo > 0) {
				//Sound.hitEffect.play();
				ammo--;
				int dx = 0;
				int px = 0;
				int py = 6;
				
				if(dir == rightDir) {
					px = 18;
					dx = 1;
				}else {
					px = -8;
					dx = -1;
				}
				
				BulletShoot bullet = new BulletShoot((int)(this.getX()+px), (int)(this.getY()+py), 3, 3, null, dx, 0);
				Game.bullets.add(bullet);
								
			}
		}		

		if(mouseShoot) {
			
			mouseShoot = false;			
			
			if(hasGun && ammo > 0) {
				//Sound.hitEffect.play();
			ammo--;
			//Criar bala e atirar!

			int px = 0,py = 8;
			double angle = 0;
			if(dir == rightDir) {
				px = 18;
				angle = Math.atan2(my - (this.getY()+py - Camera.y),mx - (this.getX()+px - Camera.x));
			}else {
				px = -8;
				angle = Math.atan2(my - (this.getY()+py - Camera.y),mx - (this.getX()+px - Camera.x));
			}
			
			double dx = Math.cos(angle);
			double dy = Math.sin(angle);
			
			BulletShoot bullet = new BulletShoot((int)this.getX()+px,(int)this.getY()+py,3,3,null,dx,dy);
			Game.bullets.add(bullet);
			}
		}
		
		
		if(life<=0) {
			life = 0;
			//Sound.dieEffect.play();
			Game.gameState = "GAME_OVER";
		}
		
		updateCamera();
	}
	
	public void updateCamera() {
		Camera.x = Camera.clamp((int)this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH) ;
		Camera.y = Camera.clamp((int)this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT);
		
		
	}
	
	public void checkCollisionLifePack() {
		for(int i = 0; i < Game.entities.size(); i++){
			Entity atual = Game.entities.get(i);
			
			if(atual instanceof LifePack) {
				if(Entity.isColliding(this, atual)) {
					//Sound.pickEffect.play();
					if(life == maxlife) {
						//Sound.pickEffect.stop();			
					}else {
						life+=10;
						Game.entities.remove(atual);
					}
					
					if(life >= 100) {
						life = 100;				
					}
					
				}
			}
		}
	}
	
	public void checkCollisionAmmo() {
		for(int i = 0; i < Game.entities.size(); i++){
			Entity atual = Game.entities.get(i);
			
			if(atual instanceof Ammo) {
				if(Entity.isColliding(this, atual)) {
					//Sound.pickEffect.play();
					ammo+=100;
					Game.entities.remove(atual);
					
				}
			}
		}
	}	
	
	public void checkCollisionWeapon() {
		for(int i = 0; i < Game.entities.size(); i++){
			Entity atual = Game.entities.get(i);
			
			if(atual instanceof Weapon) {
				if(Entity.isColliding(this, atual)) {
					//Sound.pickEffect.play();
					hasGun = true;
					
					Game.entities.remove(atual);
					
				}
			}
		}
	}	

	
	public void render(Graphics g) {
		if(!isDamaged) {
			if(dir == rightDir) {
				g.drawImage(rightPlayer[index], (int)this.getX() - Camera.x, (int)this.getY() - Camera.y, null);
				if(hasGun) {
					g.drawImage(Entity.GUN_RIGHT, (int)this.getX()+5 - Camera.x, (int)this.getY() - Camera.y +4, null);				}
			}else if(dir == leftDir) {
				g.drawImage(leftPlayer[index], (int)this.getX() - Camera.x, (int)this.getY() - Camera.y, null);
				if(hasGun) {
					g.drawImage(Entity.GUN_LEFT, (int)this.getX()-5 - Camera.x, (int)this.getY() - Camera.y +4, null);	
				}
			}
			
		}else {
			g.drawImage(playerDamage, (int)this.getX() - Camera.x, (int)this.getY() - Camera.y, null);
		}
		
	}
	
}
