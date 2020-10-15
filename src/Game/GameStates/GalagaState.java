package Game.GameStates;

import Game.Galaga.Entities.EnemyBee;
import Game.Galaga.Entities.MyEnemy;
import Game.Galaga.Entities.EntityManager;
import Game.Galaga.Entities.PlayerShip;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Created by AlexVR on 1/24/2020.
 */
public class GalagaState extends State {

	public EntityManager entityManager;
	public String Mode = "Menu";
	private Animation titleAnimation;
	public int selectPlayers = 1;
	public int EnemySpawnCount = 60*2;
	public int mySpawnC = 0;
	public int spawner = 0;
	public int startCooldown = 60*7; //seven seconds for the music to finish



	public GalagaState(Handler handler){
		super(handler);
		refresh();
		entityManager = new EntityManager(new PlayerShip(handler.getWidth()/2-64,handler.getHeight()- handler.getHeight()/7,64,64,Images.galagaPlayer[0],handler));
		titleAnimation = new Animation(256,Images.galagaLogo);
	}

	@Override
	public void tick() {
		
		spawn();
		
		if (Mode.equals("Stage")){
			if (startCooldown<=0) {
				entityManager.tick();
			}else{
				startCooldown--;
			}
		}else{
			titleAnimation.tick();
			if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP)){
				selectPlayers=1;

			}else if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN)){
				selectPlayers=2;
			}
			if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER)){
				Mode = "Stage";
				handler.getMusicHandler().playEffect("Galaga.wav");
			}
		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.CYAN);
		g.fillRect(0,0,handler.getWidth(),handler.getHeight());
		g.setColor(Color.BLACK);
		g.fillRect(handler.getWidth()/4,0,handler.getWidth()/2,handler.getHeight());
		Random random = new Random(System.nanoTime());

		for (int j = 1;j < random.nextInt(15)+60;j++) {
			switch (random.nextInt(6)) {
			case 0:
				g.setColor(Color.RED);
				break;
			case 1:
				g.setColor(Color.BLUE);
				break;
			case 2:
				g.setColor(Color.YELLOW);
				break;
			case 3:
				g.setColor(Color.GREEN);
				break;
			case 4:
				g.setColor(Color.MAGENTA);
				break;
			case 5:
				g.setColor(Color.WHITE);
				break;
			}
			int randX = random.nextInt(handler.getWidth() - handler.getWidth() / 2) + handler.getWidth() / 4;
			int randY = random.nextInt(handler.getHeight());
			g.fillRect(randX, randY, 2, 2);

		}
		if (Mode.equals("Stage")) {
			g.setColor(Color.MAGENTA);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 62));
			g.drawString("HIGH",handler.getWidth()-handler.getWidth()/4,handler.getHeight()/16);
			g.drawString("SCORE",handler.getWidth()-handler.getWidth()/4+handler.getWidth()/48,handler.getHeight()/8);
			g.setColor(Color.MAGENTA);
			g.drawString(String.valueOf(handler.getScoreManager().getGalagaHighScore()),handler.getWidth()-handler.getWidth()/4+handler.getWidth()/48,handler.getHeight()/5);
			g.setColor(Color.RED);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
			g.drawString("SCORE",handler.getWidth()-handler.getWidth()/4,handler.getHeight()/16+190);
			g.drawString(String.valueOf(handler.getScoreManager().getGalagaCurrentScore()),handler.getWidth()-handler.getWidth()/4+handler.getWidth()/48,handler.getHeight()/5+120);

			for (int i = 0; i< entityManager.playerShip.getHealth();i++) {
				g.drawImage(Images.galagaPlayer[0], (handler.getWidth() - handler.getWidth() / 4 + handler.getWidth() / 48) + ((entityManager.playerShip.width*2)*i), handler.getHeight()-handler.getHeight()/4, handler.getWidth() / 18, handler.getHeight() / 18, null);

			} 
			if (startCooldown<=0) {
				entityManager.render(g);

			}else{
				g.setFont(new Font("TimesRoman", Font.PLAIN, 48));
				g.setColor(Color.MAGENTA);
				g.drawString("START",handler.getWidth()/2-handler.getWidth()/18,handler.getHeight()/2);
			}
		}else{

			g.setFont(new Font("TimesRoman", Font.PLAIN, 32));

			g.setColor(Color.MAGENTA);
			g.drawString("HIGH-SCORE:",handler.getWidth()/2-handler.getWidth()/18,32);

			g.setColor(Color.MAGENTA);
			g.drawString(String.valueOf(handler.getScoreManager().getGalagaHighScore()),handler.getWidth()/2-32,64);

			g.drawImage(titleAnimation.getCurrentFrame(),handler.getWidth()/2-(handler.getWidth()/12),handler.getHeight()/2-handler.getHeight()/3,handler.getWidth()/6,handler.getHeight()/7,null);

			g.drawImage(Images.galagaCopyright,handler.getWidth()/2-(handler.getWidth()/8),handler.getHeight()/2 + handler.getHeight()/3,handler.getWidth()/4,handler.getHeight()/8,null);

			g.setFont(new Font("TimesRoman", Font.PLAIN, 48));
			g.drawString("1   PLAYER",handler.getWidth()/2-handler.getWidth()/16,handler.getHeight()/2);
			g.drawString("2   PLAYER",handler.getWidth()/2-handler.getWidth()/16,handler.getHeight()/2+handler.getHeight()/12);
			if (selectPlayers == 1){
				g.drawImage(Images.galagaSelect,handler.getWidth()/2-handler.getWidth()/12,handler.getHeight()/2-handler.getHeight()/32,32,32,null);
			}else{
				g.drawImage(Images.galagaSelect,handler.getWidth()/2-handler.getWidth()/12,handler.getHeight()/2+handler.getHeight()/18,32,32,null);
			}
		}
	}	
	
	 public void spawn() {
			
		 mySpawnC ++;
		 if (mySpawnC >= 1) {
			 spawner += 1;
			 mySpawnC = 0;
		 }
		 
		 if (spawner >= 1) {
		 boolean EBOccuppied = false;  // this boolean makes sure that a position will be false, thus finding one will be easier.
			int count = 0;
			
			// these random ints generate a position that bees are allowed in 
			Random i = new Random();
			Random j = new Random();
			int row = i.nextInt(1)+3;
			int col = j.nextInt(8);
			if (handler.getGalagaState().entityManager.Beegrid[row][col] == false) {count += 1;} // checking if a position is open using those random ints
         while(!EBOccuppied && count >=1) {
         	if(handler.getGalagaState().entityManager.Beegrid[row][col] == false) {
         		new EnemyBee(0, 0, 25,25, handler, row, col);
         		handler.getGalagaState().entityManager.Beegrid[row][col] = true; // if spot is found, set it to true
         		count += 1;
         		EBOccuppied = true;
         		break;
         	}

         } 
         
         boolean MEOccuppied = false; // this boolean makes sure that a position will be false, thus finding one will be easier.
     	// these random ints generate a position that bees are allowed in 
     	
         Random h = new Random();
			Random k = new Random();
			int row1 = h.nextInt(2);
			int col1 = k.nextInt(6)+1;
			int track = 0;
			if (handler.getGalagaState().entityManager.Beegrid[row1][col1] == false) {track += 1;} // checking if a position is open using those random ints
         while(!MEOccuppied && track >=1) {
         	if(handler.getGalagaState().entityManager.Beegrid[row1][col1] == false) {
         		new MyEnemy(0, 0, 25, 25, handler, row1, col1);
         		handler.getGalagaState().entityManager.Beegrid[row1][col1] = true; // if spot is found, set it to true
         		track += 1;
         		MEOccuppied = true;
         		break;
         	}
         }
         spawner = 0;
     }
		 }

	@Override
	public void refresh() {

	}
}
