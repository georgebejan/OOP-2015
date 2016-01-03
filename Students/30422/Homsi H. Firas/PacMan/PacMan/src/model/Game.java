package model;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import UI.SpriteSheet;
import UI.Texture;

public class Game extends Canvas implements Runnable, KeyListener {

	private static final long serialVersionUID = 1L;
	
	private boolean isRunning = false;
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;

	private Thread thread;

	public Player player ;
	public Level level;
	public SpriteSheet spritesheet;
	
	public static final int PAUSE_SCREEN = 0, GAME = 1;
	public static int STATE = -1;
	
	public boolean isEnter = false;
	
	private int time = 2;
	private int targetTime = 20;
	private boolean showText = true;

	public Game() {
		Dimension dimension = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(dimension);
		setMinimumSize(dimension);
		setMaximumSize(dimension);
		
		addKeyListener(this);
		
		STATE = PAUSE_SCREEN;
		
		player = new Player(WIDTH/2, HEIGHT/2);
		level = new Level("/PacMan/src/model/map.png");
		spritesheet = new SpriteSheet("/PacMan/src/model/Sprites.png");
		
		new Texture(this);
	}

	public synchronized void start() {
		if (isRunning) 
			return;
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop() {
		if (isRunning)
			return;
		isRunning = false;
		// thread.join();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void tick(){
		if(STATE == GAME){
			player.tick();
			level.tick();
		}else if(STATE == PAUSE_SCREEN){
			time ++;
			if(time == targetTime){
				time = 0;
				if(showText)
					showText = false;
			}else {
				showText = true;
			}
			if(isEnter){
				isEnter = false;
				player = new Player(WIDTH/2, HEIGHT/2);
				level = new Level("map.png");
				spritesheet = new SpriteSheet("Sprites.png");
				STATE = GAME;
			}
		}
	}

	private void render() {
		BufferStrategy bs = getBufferStrategy(); // this is for optimization
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		if(STATE == GAME){
			player.render(g);
			level.render(g);
		}else if(STATE == PAUSE_SCREEN){
			int boxWidht = 500;
			int boxHeight = 200;
			int x = WIDTH/2 - boxWidht/2;
			int y = HEIGHT/2 - boxHeight/2;
			g.setColor(Color.BLUE);
			g.fillRect(x, y, boxWidht, boxHeight);
			
			g.setColor(Color.WHITE);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 26));
			if(showText){
				g.drawString("Press enter to start the game!", x + 70, y + 50);
				if(Player.weWin){
					g.drawString("Congrats! You WON!", x + 130, y + 120);
				}else if(Player.weLost) g.drawString("You lost! ", x + 180, y + 120);
				else g.drawString("Good Luck !", x + 165, y + 120);
			}
		}
		g.dispose();
		bs.show();
		
	}

	public void run() {
		
		requestFocus();
		int fps = 0; // counter for fps
		double timer = System.currentTimeMillis();
		long lastTime = System.nanoTime();
		double targetTick = 60.0;
		double delta = 0;
		double ns = 1000000000 / targetTick;

		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick();
				render();
				fps++;
				delta--;
			}
			
			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println(fps);
				fps = 0;
				timer += 1000;
			}
		}

		stop();
	}

	public static void main(String[] args) {
		Game game = new Game();
		JFrame frame = new JFrame();
		frame.setTitle("Pac-Man"); 
		frame.add(game); // am adaugat clasa Game
		frame.setResizable(false);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		frame.setVisible(true);
		
		game.start(); // start my game loop calling my thread

	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if(STATE == GAME){
			if(e.getKeyCode() == KeyEvent.VK_RIGHT) //if my current key pressed = RIGHT
				player.right = true ;
			if(e.getKeyCode() == KeyEvent.VK_LEFT)
				player.left = true;
			if(e.getKeyCode() == KeyEvent.VK_UP)
				player.up = true;
			if(e.getKeyCode() == KeyEvent.VK_DOWN)
				player.down = true;
		}else if(STATE == PAUSE_SCREEN){
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				isEnter = true;
				STATE = GAME;
			}
		}
			
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) //if my current key pressed = RIGHT
			player.right = false ;
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
			player.left = false;
		if(e.getKeyCode() == KeyEvent.VK_UP)
			player.up = false;
		if(e.getKeyCode() == KeyEvent.VK_DOWN)
			player.down = false; 
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}