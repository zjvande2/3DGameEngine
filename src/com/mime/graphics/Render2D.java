package com.mime.graphics;

import com.mime.project.Game;

public class Render2D extends Render {

	public Render2D(int w, int h) {
		super(w, h);
		
	}
	
	public void randomPixels(Render test, Game game) {
		for (int i = 0; i < width * height; i++) {
			pixels[i] = 0;
		}
			
		for (int i = 0; i < 50; i++) {
			
			int anim = (int) (Math.sin((game.time + i * 5) % 2000.0 / 200) * 200);
			int anim2 = (int) (Math.cos((game.time + i * 5) % 2000.0 / 200) * 200);
			
			draw(test, (width - 256) / 2 + anim, (height - 256) / 2 + anim2);
		}
		
		
	}
}
