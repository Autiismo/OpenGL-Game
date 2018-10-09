package raycast;

import org.lwjgl.util.vector.Vector3f;

import hitboxes.Hitbox;
import toolbox.Maths;

public class Ray {
	
	Vector3f position;
	Vector3f direction;
	float distance;
	float maxDistance;
	
	boolean collided = false;
	Vector3f hit = null;
	
	public Ray(Vector3f position, Vector3f direction, float distance, float maxDistance, Hitbox collider) {
		this.position = position;
		this.direction = getOffset(position, direction, distance);
		this.distance = distance;
		this.maxDistance = maxDistance;
		
		collided = checkCollisions(collider);
	}
	
	private Vector3f getOffset(Vector3f position, Vector3f direction, float hop) {
		float xOff = Math.abs(position.x - direction.x);
		float yOff = Math.abs(position.y - direction.y);
		float zOff = Math.abs(position.z - direction.z);
		float xzDist= Math.sqrt(Math.pow(xOff, 2) + Math.pow(zOff, 2));
		float distance = Math.sqrt(Math.pow(xOff, 2) + Math.pow(yOff, 2) + Math.pow(zOff, 2));
		
		float theta = Math.asin(xOff / xzDist);
		float yTheta = Math.asin(yOff / distance);
		
		distance *= hop;
		xzDist *= hop;
		
		float xOff2 = xzDist * Math.sin(theta);
		float yOff2 = distance * Math.sin(yTheta);
		float zOff2 = xzDist * Math.cos(theta);
		
		return new Vector3f(xOff2, yOff2, zOff2);
	}
	
	public boolean checkCollisions(Hitbox collider) {
		Vector3f currentPos = position;
		Vector3f[] minMax = Hitbox.minMax(collider.getBounds());
		Vector3f min = minMax[0];
		Vector3f max = minMax[1];
		for(int i = 0; i < Math.ceil(maxDistance / distance); i++) {
			if(currentPos.x >= min.x && currentPos.x <= max.x
			&& currentPos.y >= min.y && currentPos.y <= max.y
			&& currentPos.z >= min.z && currentPos.z <= max.z) {
				hit = currentPos;
				return true;
			}
			currentPos = new Vector3f(currentPos.x + (direction.x * distance),
							currentPos.y + (direction.y * distance),
							currentPos.z + (direction.z * distance));
		}
		return false;
	}
	
	public Vector3f getHit() {
		return hit;
	}
	
}
