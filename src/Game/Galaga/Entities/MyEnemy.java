package Game.Galaga.Entities;

import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.image.BufferedImage;
//Created my enemy 
public class MyEnemy extends BaseEntity {
    int row,col,newx,newy;//row 1-2, col 0-7
    boolean justSpawned=true,attacking=false, positioned=false,hit=false,centered = false;
    Animation idle,turn90Left;
    int spawnPos;//0 is left 1 is top, 2 is right, 3 is bottom
    int formationX,formationY,speed,centerCoolDown=60;
    int timeAlive=0,beeofflimits=190;
    public MyEnemy(int x, int y, int width, int height, Handler handler, int row, int col) {
        super(x, y, width, height, Images.galagaEnemyBee[0], handler);
        this.row = row;
        this.col = col;
        BufferedImage[] idleAnimList= new BufferedImage[2];
        idleAnimList[0] = Images.galagaNewEnemy[1];
        idleAnimList[1] = Images.galagaNewEnemy[3];
        idle = new Animation(512,idleAnimList);
        turn90Left = new Animation(128,Images.galagaNewEnemy);
        spawn();
        speed = 4;
        formationX=(handler.getWidth()/4)+(col*((handler.getWidth()/2)/8))+8;
        formationY=(row*(handler.getHeight()/10))+8;
    }

    private void spawn() {
        spawnPos = random.nextInt(3);
        switch (spawnPos){
            case 0://left
                x = (handler.getWidth()/4)-width;
                y = random.nextInt(handler.getHeight()-handler.getHeight()/8);
                break;
            case 1://top
                x = random.nextInt((handler.getWidth()-handler.getWidth()/2))+handler.getWidth()/4;
                y = -height;
                break;
            case 2://right
                x = (handler.getWidth()/2)+ width + (handler.getWidth()/4);
                y = random.nextInt(handler.getHeight()-handler.getHeight()/8);
                break;

        }
        
        bounds.x=x;
        bounds.y=y;
        handler.getGalagaState().entityManager.entities.add(this);
    }

    @Override
    public void tick() {
        super.tick();
        idle.tick();
        if (hit){
            if (enemyDeath.end){
                remove = true;
                return;
            }
            enemyDeath.tick();
        }
        if (justSpawned){
            timeAlive++;
            if (!centered && Point.distance(x,y,handler.getWidth()/2,handler.getHeight()/2)>speed){//reach center of screen
                switch (spawnPos){
                    case 0://left
                        x+=speed;
                        if (Point.distance(x,y,x,handler.getHeight()/2)>speed) {
                            if (y > handler.getHeight() / 2) {
                                y -= speed;
                            } else {
                                y += speed;
                            }
                        }
                        break;
                    case 1://top
                        y+=speed;
                        if (Point.distance(x,y,handler.getWidth()/2,y)>speed) {
                            if (x > handler.getWidth() / 2) {
                                x -= speed;
                            } else {
                                x += speed;
                            }
                        }
                        break;
                    case 2://right
                        x-=speed;
                        if (Point.distance(x,y,x,handler.getHeight()/2)>speed) {
                            if (y > handler.getHeight() / 2) {
                                y -= speed;
                            } else {
                                y += speed;
                            }
                        }
                        break;
               
                }
                if (timeAlive>=60*60*2){
                    //more than 2 minutes in this state then die
                    //60 ticks in a second, times 60 is a minute, times 2 is a minute
                    remove = true;
                }

            }else {//move to formation
                if (!centered){
                    centered = true;
                    timeAlive = 0;
                }
                if (centerCoolDown<=0){
                    if (Point.distance(x, y, formationX, formationY) > speed) {//reach center of screen
                        if (Math.abs(y-formationY)>6) {
                            y -= speed;
                        }
                        if (Point.distance(x,y,formationX,y)>speed/2) {
                            if (x >formationX) {
                                x -= speed;
                            } else {
                                x += speed;
                            }
                        }
                        
                        
                    }else if(!(Point.distance(x, y, formationX, formationY) > speed)) {
                    	positioned = true;
                    	justSpawned = false;
                    	attacking = true;
                    	
                    }
                    else{
                        positioned =true;
                        justSpawned = false;
                        
                        
                    }
                }else{
                    centerCoolDown--;
                    
                    
                }
                if (timeAlive>=60*60*2){
                    //more than 2 minutes in this state then die
                    //60 ticks in a second, times 60 is a minute, times 2 is a minute
                    remove = true;
                    
                }
            }
        }if (positioned){
        	attacking = true;
        	positioned = false;
        	
        	
        	
        	

        }
        	if(attacking) {
        		//enemy attack player
        		beeofflimits--;
        		int positionship = handler.getGalagaState().entityManager.playerShip.x;
        		if(positionship>x) {
        			x++;
        		}else if(positionship<x) {
        			x--;
        		}
        		y+=speed;
        		if(beeofflimits<=0) {
        			hit = true;
        		
        		
        		
        	}
        
        }
        
        bounds.x=x;
        bounds.y=y;
        
    }

    @Override
    public void render(Graphics g) {
        //((Graphics2D)g).draw(new Rectangle(formationX,formationY,32,32)); edited the hit box for the aesthetics
        if (arena.contains(bounds)) {
            if (hit){
                g.drawImage(enemyDeath.getCurrentFrame(), x, y, width, height, null);
            }else{
                g.drawImage(idle.getCurrentFrame(), x, y, width, height, null);

            }
        }
    }

    @Override
    public void damage(BaseEntity damageSource) {
        super.damage(damageSource);
        if (damageSource instanceof PlayerLaser || damageSource instanceof BadBunny){
            hit=true;
            handler.getScoreManager().addGalagaCurrentScore(100);
            handler.getMusicHandler().playEffect("explosion.wav");
            damageSource.remove = true;     
            //added bad bunny easter egg :)
        }
    }
}

