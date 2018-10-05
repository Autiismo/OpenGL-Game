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
		this.direction = Maths.squish(new Vector3f(position.x - direction.x, position.y - direction.y, position.z - direction.z));
		this.distance = distance;
		this.maxDistance = maxDistance;
		
		collided = checkCollisions(collider);
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
