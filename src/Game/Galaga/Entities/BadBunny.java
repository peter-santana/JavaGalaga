package Game.Galaga.Entities;

import Main.Handler;

import java.awt.image.BufferedImage;

/**
Added a mini easter egg for fun :)
 */
public class BadBunny extends BaseEntity {

    EntityManager enemies;
    int speed = 6;

    public BadBunny(int x, int y, int width, int height, BufferedImage sprite, Handler handler,EntityManager enemies) {
        super(x, y, width, height, sprite, handler);
        this.enemies=enemies;
    }

    @Override
    public void tick() {
        if (!remove) {
            super.tick();
            y -= speed;
            bounds.y = y;
            for (BaseEntity enemy : enemies.entities) {
                if (enemy instanceof PlayerShip || enemy instanceof BadBunny) {
                    continue;
                }
                if (enemy.bounds.intersects(bounds)) {
                    enemy.damage(this);
                }
                
                
            }
        }
    }
}
