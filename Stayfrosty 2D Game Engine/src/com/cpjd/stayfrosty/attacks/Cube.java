package com.cpjd.stayfrosty.attacks;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.cpjd.stayfrosty.entity.Sprite;
import com.cpjd.stayfrosty.tilemap.TileMap;

/*
 * Will Davies
 * Superior Throwing Simulation
 * 
 * Call new Cube() with the following parameters
 * -tilemap - the active tilemap
 * -px and py - the player's x and y location on the tilemap
 * -tx and ty - the x and y difference between the player's x and y
 * -actualx - the actual (tilemap location) x of the mouse
 */
public class Cube extends Sprite {
	// Identity
	BufferedImage cube;
	private boolean remove; // If the cube is ready to be removed from memory
	private boolean facingRight;
	
	// Constants
	private final double GRAVITY = 0.3; // pixels per tick (with a tick rate of 60 fps)
	private double POWER; // upwards velocity in pixels per tick
	
	// Variables
	private double elapsedTicks; // The amount of ticks that have passed
	private double distance; // Distance between player and target
	private double time; // Precalculated time for cube to reach ty (while falling)
	private double py; // Player x and y
	private double px;
	
	/*
	 * @param px & py - player x and y
	 * @param tx & ty - target x and y (not exactly a target location, but the distance between player loc and target loc)
	 * @param actualx - the actual target x location
	 *
	 */
	public Cube(TileMap tm, double px, double py, double tx, double ty, double actualx) {
		super(tm);		
		setPosition(px, py);
		this.py = py; this.px = px;
		
		// Calculate which side of the player the target is on
		if(actualx < px) right = false;
		else right = true;
		facingRight = right;
		
		// Calculate the distance between the player and the taret
		distance = calculateDifference(px,py,tx,ty);
		
		// A rough power calculation for how hard the player will throw the cube upwards
		POWER = -(Math.abs(distance) / 20);
		
		// Using the quadratic formula, calculated the estimated time until the cube will hit the required y value while falling
		double a = 0.5*GRAVITY;
		double b = POWER;
		double c = py - ty;
		
		time = (-b + Math.sqrt((b * b) + (-4 * a *c))) / (2 * a); // The time, in ticks, for the eta of the cube
		
		dx = distance / time; // Calculate the required acceleration per tick to reach the required x value when the y values are equal
		
		// Drawing
		width = 25;
		height = 25;
		
		// Collision
		cwidth = 25;
		cheight = 25;
		
		// sprites
		try {
			cube = ImageIO.read(getClass().getResourceAsStream("/Weapons/rubiks.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean shouldRemove() {
		return remove;
	}
	
	public void setPosition(double x, double y) {
		super.setPosition(x, y);
	}
	
	public void update() {

		// Update the total ticks
		elapsedTicks++;
		
		// Update the x position
		if(right) px += dx;
		if(!right) px -= dx;
		xtemp = px;
		
		// Calculate the y position based of the falling bodies equation, the only changing value here is elapsedTicks
		ytemp = -(-0.5 * GRAVITY * (elapsedTicks * elapsedTicks)) + (POWER * elapsedTicks) + (py);

		// Set the position
		setPosition(xtemp, ytemp);
	}
	public void draw(Graphics2D g) {
		setMapPosition();

		// Draw the cube
		if (facingRight) {
			g.drawImage(cube, (int) (x + xmap - width / 2), (int) (y + ymap - height / 2), null);
		} else {
			g.drawImage(cube, (int) (x + xmap - width / 2 + width), (int) (y + ymap - height / 2),
					-cube.getWidth(), cube.getHeight(), null);
		}

	}
	// Calculates the distance between two points in a cartesian plane
	protected double calculateDifference(double x1, double y1, double x2, double y2) {
		return Math.hypot(Math.abs(x2 - x1), Math.abs(y2 - y1));
	}
}
