package com.cpjd.stayfrosty.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.cpjd.stayfrosty.audio.AudioPlayer;
import com.cpjd.stayfrosty.audio.SKeys;
import com.cpjd.stayfrosty.gamestate.GameState;
import com.cpjd.stayfrosty.gamestate.GameStateManager;
import com.cpjd.stayfrosty.main.GamePanel;
import com.cpjd.stayfrosty.util.Center;

public class Credits extends GameState {
	
	// Display
	String[] items = {
			"Bryan Simulator",
			"by Cats Pajamas Developers",
			"",
			"Team",
			"Will (techguy9984)",
			"Daniel (boblop)",
			"",
			"",
			"This program is not intended for commerical distribution",
			"",
			"All logos shown or represented in this game",
			"are copyright and or trademark of their respective",
			"corporations",
			"",
	};
	
	// Technical
	private int y;
	
	Font font;
	
	BufferedImage cpjd;
	
	int escX;

	long startTime;
	long elapsed;
	
	public Credits(GameStateManager gsm) {
		super(gsm);

		y = GamePanel.HEIGHT;
		AudioPlayer.playMusic(SKeys.Credits);
		
		try {
			cpjd = ImageIO.read(getClass().getResourceAsStream("/CPJD/cpjdlogosmall.png"));
			
			escX = 5;
			
			startTime = System.nanoTime();
			
			InputStream inStream = getClass().getResourceAsStream("/Fonts/8-BIT WONDER.TTF");
			Font rawFont = Font.createFont(Font.TRUETYPE_FONT, inStream);
			font = rawFont.deriveFont(15.0f);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void update() {
		elapsed = (System.nanoTime() - startTime) / 1_000_000_000;
		if(elapsed > 3) {
			if(escX > -500) escX-=12;
		}
		
		y--;
		
		if(y + (30 * items.length) < -30) {
			//AudioPlayer.stopAll();
			gsm.setState(GameStateManager.MENU);
		}
	}

	
	public void draw(Graphics2D g) {
		// Rendeirng
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// Clear the screen
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		// Draw items
		g.setColor(Color.BLACK);
		g.setFont(font);
		
		g.drawImage(cpjd, Center.centeri(cpjd.getWidth()), y - cpjd.getHeight(), null);
		
		g.drawString("Press ESC to exit", escX, 15);
		
		for(int i = 0, j = 1; i < items.length; i++) {
			if(i == items.length - 1) g.setColor(Color.MAGENTA);
			g.drawString(items[i], Center.center(g, items[i]), y + (30 * j));
			j++;
		}
		
	}
	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ESCAPE) {
			//AudioPlayer.stopAll();
			gsm.setState(GameStateManager.MENU);
		}
		
	}

	
	public void keyReleased(int k) {}
	
}
