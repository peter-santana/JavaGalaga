package Game.Galaga.Entities;

import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Created by AlexVR on 1/25/2020
 */
public class PlayerShip extends BaseEntity{

    private int health = 3,attackCooldown = 30,speed =6,destroyedCoolDown = 60*7, yehyeh = 1;
    private boolean attacking = false, destroyed = false;
    private Animation deathAnimation;


     public PlayerShip(int x, int y, int width, int height, BufferedImage sprite, Handler handler) {
        super(x, y, width, height, sprite, handler);

        deathAnimation = new Animation(256,Images.galagaPlayerDeath);
    }

    @Override
    public void tick() {
    	
        super.tick();
        if (destroyed){
            if (destroyedCoolDown<=0){
                destroyedCoolDown=60*7;
                destroyed=false;
                //added the handler so every time player dies, score is deleted
                handler.getScoreManager().removeGalagaCurrentScore(handler.getScoreManager().getGalagaCurrentScore());
                deathAnimation.reset();
                bounds.x=x;
            }else{
                deathAnimation.tick();
                destroyedCoolDown--;
            }
        }else {
            if (attacking) {
                if (attackCooldown <= 0) {
                    attacking = false;
                } else {
                    attackCooldown--;
                }
            }
            
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER) && !attacking) {
                handler.getMusicHandler().playEffect("laser.wav");
                attackCooldown = 30;
                attacking = true;
                handler.getGalagaState().entityManager.entities.add(new PlayerLaser(this.x + (width / 2), this.y - 3, width / 5, height / 2, Images.galagaPlayerLaser, handler, handler.getGalagaState().entityManager));
            }
            //Added a mini Bad Bunny Easter Egg :)
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_B) && !attacking && yehyeh>=1) {
                handler.getMusicHandler().playEffect("badbunny.wav");
                attacking = true;
                handler.getGalagaState().entityManager.entities.add(new BadBunny(this.x + (width / 2), this.y - 3, width  , height*2 , Images.galagaBadBunny, handler, handler.getGalagaState().entityManager));
                yehyeh --;
                
            }
            // N button is for self destruct
            if(handler.getKeyManager().explode) {
            	destroyed = true;
            	handler.getMusicHandler().playEffect("explosion.wav");
            	health = health - 1;
            	            
       	   }//H button gives the player one more live, limit if player already has 3 lives
            if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_H) && (health <3)){
            	health = health + 1;
            	handler.getMusicHandler().playEffect("health.wav");
            }
            // P button summons Bees.
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_P)) {
                boolean EBOccuppied = false;  // this boolean makes sure that a position will be false, thus finding one will be easier.
    			int count = 0;
    			
    			// these random ints generate a position that bees are allowed in 
    			Random i = new Random();
    			Random j = new Random();
    			int row = i.nextInt(2)+3;
    			int col = j.nextInt(8);
    			if (handler.getGalagaState().entityManager.Beegrid[row][col] == false) {count += 1;} // checking if a position is open using those random ints
                while(!EBOccuppied && count >=1) {
                	if(handler.getGalagaState().entityManager.Beegrid[row][col] == false) {
                		new EnemyBee(0, 0, 25,25, handler, row, col);
                		handler.getGalagaState().entityManager.Beegrid[row][col] = true; // if spot is found, set it to true
                		count += 1;
                		EBOccuppied = true;
                	}
                	
                	// generate new positions
                	row = i.nextInt(2)+3;
        			col = j.nextInt(8);
                } 
            }
            // O button summons MyEnemy
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_O)) {
            	
            	
            	boolean MEOccuppied = false; // this boolean makes sure that a position will be false, thus finding one will be easier.
            	// these random ints generate a position that bees are allowed in 
            	
                Random i = new Random();
    			Random j = new Random();
    			int row = i.nextInt(3);
    			int col = j.nextInt(6)+1;
    			int track = 0;
    			if (handler.getGalagaState().entityManager.Beegrid[row][col] == false) {track += 1;} // checking if a position is open using those random ints
                while(!MEOccuppied && track >=1) {
                	if(handler.getGalagaState().entityManager.Beegrid[row][col] == false) {
                		new MyEnemy(0, 0, 25, 25, handler, row, col);
                		handler.getGalagaState().entityManager.Beegrid[row][col] = true; // if spot is found, set it to true
                		track += 1;
                		MEOccuppied = true;
                	}
                	// generate new positions
                	row = i.nextInt(3);
        			col = j.nextInt(6)+1;
                }   
            }
            //Bounds have been set
            if (handler.getKeyManager().left && x>handler.getWidth()/4 + 6) {
                x -= (speed);
            }
            //Pause Button has been added 
            if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_L)) {
            	handler.changeState(handler.getPauseState());
            }
            //Math was done for the right bound
            if (handler.getKeyManager().right && x<handler.getWidth()/4 * 2.806) {
                x += (speed);
            }
            //Score manager added over here
            if (handler.getScoreManager().getGalagaCurrentScore()>handler.getScoreManager().getGalagaHighScore()) {
            	handler.getScoreManager().setGalagaHighScore(handler.getScoreManager().getGalagaCurrentScore());
            }        
           
           
      
            bounds.x = x;
        }
    }

    @Override
    public void render(Graphics g) {
         if (destroyed){
             if (deathAnimation.end){
                 g.drawString("READY",handler.getWidth()/2-handler.getWidth()/12,handler.getHeight()/2);
             }else {
                 g.drawImage(deathAnimation.getCurrentFrame(), x, y, width, height, null);
             }
         }else {
             super.render(g);
         }
    }
    @Override
    public void damage(BaseEntity damageSource) {
        if (damageSource instanceof PlayerLaser || damageSource instanceof BadBunny){
            return;
        }
        health--;
        destroyed = true;
        handler.getMusicHandler().playEffect("explosion.wav");
        bounds.x = -10;
                
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }
    
    

}
