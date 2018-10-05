package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import hitboxes.Hitbox;
import models.TexturedModel;
import renderEngine.DisplayManager;
import terrain.Terrain;

public class Player extends Entity {

	private static final float RUN_SPEED = 40;
	private static final float STRAFE_SPEED = 30;
	public static final float GRAVITY = -50;
	private static final float JUMP_POWER = 18;

	private float currentSpeed = 0;
	private float strafeSpeed = 0;
	private float upwardsSpeed = 0;
	
	private static float health;

	private boolean isInAir = false;

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		health = 1000f;
	}
	
	public float[] updatePlayer() {
		return new float[] {health/10f};
	}

	public void move(Terrain terrain) {
		checkInputs();
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(getRotY())));
		super.increasePosition(dx, 0, dz);
		distance = strafeSpeed * DisplayManager.getFrameTimeSeconds();
		dx = (float) (distance * Math.cos(Math.toRadians(360 - getRotY())));
		dz = (float) (distance * Math.sin(Math.toRadians(360 - getRotY())));
		super.increasePosition(dx, 0, dz);
		upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float terrainHeight = terrain.getHeightOfTerrain(getPosition().x, getPosition().z);
		if (super.getPosition().y < terrainHeight) {
			upwardsSpeed = 0;
			isInAir = false;
			super.getPosition().y = terrainHeight;
		}
	}

	private void jump() {
		if (!isInAir) {
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}

	private void checkInputs() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_S)) {
			if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
				this.currentSpeed = RUN_SPEED;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				this.currentSpeed = -RUN_SPEED;
			}
		} else {
			this.currentSpeed = 0;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_W) && Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_A)) {
			if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				this.strafeSpeed = -STRAFE_SPEED;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				this.strafeSpeed = STRAFE_SPEED;
			}
		} else {
			this.strafeSpeed = 0;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D) && Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.strafeSpeed = 0;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
	}
	
	public static float getHealth() {
		return health;
	}

}