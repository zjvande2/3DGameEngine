package com.mime.project;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.mime.graphics.Screen;
import com.mime.input.InputHandler;

public class Display extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final int GAMEHEIGHT = HEIGHT - (HEIGHT / 4);

	public static int GAMEBRIGHTNESS = 255;
	
	public static final String TITLE = "Graphics Demo";

	private Thread thread;
	private Screen screen;
	private Game game;
	private BufferedImage img;
	private boolean running = false;
	private int[] pixels;
	private InputHandler input;
	private int fps;

	public Display() {
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);

		screen = new Screen(WIDTH, HEIGHT);
		game = new Game();
		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

		input = new InputHandler();
		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);

	}

	private void start() {
		if (running) {
			return;
		} else {

			running = true;
			thread = new Thread(this);
			thread.start();
			System.out.println("Thread created...");
		}
	}

	private void stop() {
		if (!running) {
			return;
		} else {
			running = false;
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	public void run() {
		int frames = 0;
		double unprocessedSeconds = 0;
		long previousTime = System.nanoTime();
		double secondsPerTick = 1 / 60.0;
		int tickCount = 0;
		boolean ticked = false;

		while (running) {
			long currentTime = System.nanoTime();
			long passedTime = currentTime - previousTime;
			previousTime = currentTime;
			unprocessedSeconds += passedTime / 1000000000.0;

			while (unprocessedSeconds > secondsPerTick) {
				tick();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;
				tickCount++;

				if (tickCount % 60 == 0) {
					//System.out.println("Frames " + frames);
					fps = frames;
					previousTime += 1000;
					frames = 0;
				}
			}
			if (ticked == true) {
				frames++;
			}
			render(frames);
		}
	}

	private void tick() {
		game.tick(input.key);
	}

	private void render(int frames) {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		screen.render(game);

		for (int i = 0; i < (WIDTH * HEIGHT); i++) {
			pixels[i] = screen.pixels[i];
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(img, 0, 0, WIDTH + 10, HEIGHT + 10, null);
		g.setColor(Color.YELLOW);
		g.setFont(new Font("Verdana", 0, 50));
		g.drawString(fps + "", 20, 50);
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		Display game = new Display();
		JFrame frame = new JFrame();

		Rectangle testButtonBounds = new Rectangle(50, 50, 50, 50);
		JLabel button = new JLabel("TestButton");
		
		
		frame.add(game);
		
		frame.pack();
		button.setBounds(testButtonBounds);
		frame.add(button);
		
		frame.setTitle(TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(true);
		frame.setSize(WIDTH, HEIGHT);
		frame.setVisible(true);

		
		

		System.out.println("Frame created...");

		game.start();
		
	}
}
