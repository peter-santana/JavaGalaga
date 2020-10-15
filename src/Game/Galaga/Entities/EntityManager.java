package Game.Galaga.Entities;

import java.awt.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;



/**
 * Created by AlexVR on 1/25/2020
 */
public class EntityManager {

    public List<BaseEntity> entities;
    public PlayerShip playerShip;
    public EnemyBee enemyBee;
    public boolean [][] Beegrid = new boolean [5][11];

    public EntityManager(PlayerShip playerShip) {
        entities = new CopyOnWriteArrayList<>();
        this.playerShip = playerShip;
    }


    public void tick(){
        playerShip.tick();
        ArrayList<BaseEntity> toRemove = new ArrayList<>();
        for (BaseEntity entity: entities){
        	System.out.println(entity.getClass());
        	if(entity instanceof EnemyBee) {
        		EnemyBee eb = ((EnemyBee) entity);
        		System.out.println("This bee is in: " + eb.row + " " + eb.col);
        	}
        	else {
        		System.out.println("It is not a bee, it is instead a: " + entity);
        	}
        	
        	
            if (entity.remove){
                toRemove.add(entity);
                continue;
            }
            entity.tick();
            if (entity.bounds.intersects(playerShip.bounds)){
                playerShip.damage(entity);
            }
        }
        for (BaseEntity toErase:toRemove){
        	if (toErase instanceof EnemyBee) {
				EnemyBee toEraseB = (EnemyBee) toErase;
				int row = toEraseB.row;
				int col = toEraseB.col;
				Beegrid[row][col] = false;
			}
        	if (toErase instanceof MyEnemy) {
				MyEnemy toEraseB = (MyEnemy) toErase;
				int row = toEraseB.row;
				int col = toEraseB.col;
				Beegrid[row][col] = false;
			}
            entities.remove(toErase);
        }

    }

    public void render(Graphics g){
        for (BaseEntity entity: entities){
            entity.render(g);
        }
        playerShip.render(g);

    }

}
