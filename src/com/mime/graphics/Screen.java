package com.mime.graphics;

import java.util.Random;

import com.mime.project.Display;
import com.mime.project.Game;

public class Screen extends Render {

	private Render3D render;

	
	public Screen(int w, int h) {
		super(w, h);
		Random random = new Random();
		
		//test = new Render(256, 256);
		//gui = new Render2D(width, Display.HEIGHT - Display.GAMEHEIGHT);
		render = new Render3D(width, height);

		
		
		
	}

	public void render(Game game) {
		for (int i = 0; i < width * height; i++) {
			pixels[i] = 0;
		}
		
		
		render.floor(game);
		render.renderDistanceLimiter();
		render.renderWall(.5, 2, 2, 1);
		
		draw(render, 0, 0);
		//draw(gui, 0,0);
	}

}
