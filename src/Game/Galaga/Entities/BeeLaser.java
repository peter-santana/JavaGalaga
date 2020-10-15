package Game.Galaga.Entities;
// Changed this to be a BeeLaser
import Main.Handler;

import java.awt.image.BufferedImage;

public class BeeLaser extends BaseEntity {

    EntityManager playership;
    int speed = 6;

    public BeeLaser(int x, int y, int width, int height, BufferedImage sprite, Handler handler,EntityManager playership, int row, int col) {
        super(x, y, width, height, sprite, handler);
        this.playership=playership;
    }

    @Override
    public void tick() {
        if (!remove) {
            super.tick();
            y += speed;
            bounds.y = y;
            for (BaseEntity playership : playership.entities) {
            	if (playership instanceof EnemyBee || playership instanceof BeeLaser) {
                    continue;
                }
                if (playership.bounds.intersects(bounds)) {
                    playership.damage(this);
                }
            }
        }
    }
}
