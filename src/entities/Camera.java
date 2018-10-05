package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch = 0;
	private float yaw = 180;
	private float roll = 0;
	
	private boolean mouseLocked = true;
	private boolean justSwitched = true;
	private float mouseX;
	private float mouseY;
	private float screenX;
	private float screenY;
	private float horizSensitivity = 0.2f;
	private float vertSensitivity = 0.1f;
	
	private Player player;
	
	public Camera(Player player) {
		this.player = player;
		this.mouseX = Mouse.getX();
		this.mouseY = Mouse.getY();
	}
	
	public void move() {
		checkInputs();
		
		position.x = player.getPosition().getX();
		position.y = player.getPosition().getY() + 16;
		position.z = player.getPosition().getZ();
		
		mouseX = Mouse.getX();
		mouseY = Mouse.getY();
		Mouse.setGrabbed(mouseLocked);
		if(mouseLocked) {
			Mouse.setCursorPosition(Display.getWidth()/2, Display.getHeight()/2);
			screenX = Display.getWidth()/2;
			screenY = Display.getHeight()/2;
			
			float dx = (mouseX - screenX) * horizSensitivity;
			float dy = (mouseY - screenY) * vertSensitivity;
			
			if(justSwitched) {
				dx = 0;
				dy = 0;
			}
			
			yaw += dx;
			if(yaw > 360) yaw -= 360;
			if(yaw < 0) yaw += 360;
			if(pitch - dy < 80 && pitch - dy > -80) pitch -= dy;
			
			player.setRotY(360 - (getYaw() - 180));
			if(player.getRotY() > 360) player.increaseRotation(0, -360, 0);
			if(player.getRotY() < 0) player.increaseRotation(0, 360, 0);
		}
		justSwitched = false;
	}
	
	private void checkInputs() {
		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState()) {
				if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
					mouseLocked = !mouseLocked;
					justSwitched = true;
				}
			}
		}
	}
	
	public void invertPitch() {
		this.pitch = -pitch;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public float getYaw() {
		return yaw;
	}
	
	public float getRoll() {
		return roll;
	}
	
}