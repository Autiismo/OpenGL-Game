package hitboxes;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import raycast.Ray;

public class Hitbox {
	
	private Vector3f position;
	private Vector3f scale;
	private Vector3f rotation;
	
	private Vector3f[] bounds;
	
	public static final int FRONT_TOP_LEFT = 0;
	public static final int FRONT_TOP_RIGHT = 1;
	public static final int FRONT_BOTTOM_LEFT = 2;
	public static final int FRONT_BOTTOM_RIGHT = 3;
	public static final int BACK_TOP_LEFT = 4;
	public static final int BACK_TOP_RIGHT = 5;
	public static final int BACK_BOTTOM_LEFT = 6;
	public static final int BACK_BOTTOM_RIGHT = 7;
	
	public Hitbox(Vector3f position, Vector3f scale, Vector3f rotation) {
		this.position = position;
		this.scale = new Vector3f(scale.x * 3, scale.y * 3, scale.z * 3);
		this.rotation = rotation;
		this.bounds = getBoundingPoints();
	}
	
	public void update(Vector3f position, Vector3f scale, Vector3f rotation) {
		this.position = position;
		this.scale = new Vector3f(scale.x * 3, scale.y * 3, scale.z * 3);
		this.rotation = rotation;
		this.bounds = getBoundingPoints();
	}
	
	public boolean isCollided(Hitbox collider) {
		Vector3f cPos = collider.getPosition();
		double xOff = Math.abs(position.x - cPos.x);
		double yOff = Math.abs(position.y - cPos.y);
		double zOff = Math.abs(position.z - cPos.z);
		double xzDist = Math.sqrt(Math.pow(xOff, 2) + Math.pow(zOff, 2));
		double distance = Math.sqrt(Math.pow(xOff, 2) + Math.pow(yOff, 2) + Math.pow(zOff, 2));
		
		double theta = Math.toDegrees(Math.acos(xOff / xzDist)) - (rotation.y % 180) + 90;
		double a = scale.x / Math.cos(rotation.y % 90);
		double dist = a / Math.cos(theta);
		double yTheta = Math.toDegrees(Math.acos(xzDist / distance)) - (rotation.x % 90);
		double yA = dist / Math.cos(rotation.x % 90);
		double yDist = yA / Math.cos(yTheta);
		
		double theta2 = Math.toDegrees(Math.acos(xOff / xzDist)) - (collider.rotation.y % 90);
		double a2 = collider.scale.x / Math.cos(collider.rotation.y % 90);
		double dist2 = a2 / Math.cos(theta2);
		double yTheta2 = Math.toDegrees(Math.acos(xzDist / distance)) - (collider.rotation.x % 90);
		double yA2 = dist2 / Math.cos(collider.rotation.x % 90);
		double yDist2 = yA2 / Math.cos(yTheta2);
		
		Ray ray = new Ray(position, collider.position, 0.1f, (float) distance + 1, collider);
		System.out.println(ray.getHit());
		
		return distance <= yDist + yDist2;
	}
	
	public static Vector3f[] minMax(Vector3f[] bounds) {
		Vector3f max = new Vector3f(0, 0, 0);
		Vector3f min = new Vector3f(800, 800, 800);
		for(int i = 0; i < bounds.length; i++) {
			if(bounds[i].x > max.x) max.x = bounds[i].x;
			if(bounds[i].x < min.x) min.x = bounds[i].x;
			
			if(bounds[i].y > max.y) max.y = bounds[i].y;
			if(bounds[i].y < min.y) min.y = bounds[i].y;
			
			if(bounds[i].z > max.z) max.z = bounds[i].z;
			if(bounds[i].z < min.z) min.z = bounds[i].z;
		}
		return new Vector3f[] {min, max};
	}
	
	/**
	 * Calulates the eight bounding points for the box when you are facing down and forwards is positive z.
	 * @return A vector3 array of the eight bounding points in the order of front to back, top left, top right, bottom left, and bottom right.
	 */
	public Vector3f[] getBoundingPoints() {
		Vector2f[] positions = getPositions();
		Vector2f frontLeft = positions[0];
		Vector2f frontRight = positions[1];
		Vector2f backLeft = positions[2];
		Vector2f backRight = positions[3];
		return new Vector3f[] {
			new Vector3f(position.x + frontLeft.x, position.y + scale.y, position.z + frontLeft.y),
			new Vector3f(position.x + frontRight.x, position.y + scale.y, position.z + frontRight.y),
			
			new Vector3f(position.x + frontLeft.x, position.y, position.z + frontLeft.y),
			new Vector3f(position.x + frontRight.x, position.y, position.z + frontRight.y),
			
			new Vector3f(position.x + backLeft.x, position.y + scale.y, position.z + backLeft.y),
			new Vector3f(position.x + backRight.x, position.y + scale.y, position.z + backRight.y),
			
			new Vector3f(position.x + backLeft.x, position.y, position.z + backLeft.y),
			new Vector3f(position.x + backRight.x, position.y, position.z + backRight.y)
		};
	}
	
	public Vector2f[] getPositions() {
		float theta = (float) Math.toRadians(rotation.y);
		float sinTheta = (float) Math.sin(theta);
		float cosTheta = (float) Math.cos(theta);
		
		Vector2f frontLeft = new Vector2f(((scale.z / 2) * sinTheta) + ((scale.x / 2) * cosTheta),
				((scale.z / 2) * cosTheta) + (float) (-(scale.x / 2) * sinTheta));
		
		Vector2f frontRight = new Vector2f(((scale.z / 2) * sinTheta) + (-(scale.x / 2) * cosTheta),
				((scale.z / 2) * cosTheta) + (float) ((scale.x / 2) * sinTheta));
		
		Vector2f backLeft = new Vector2f((-(scale.z / 2) * sinTheta) + ((scale.x / 2) * cosTheta),
				(-(scale.z / 2) * cosTheta) + (float) (-(scale.x / 2) * sinTheta));
		
		Vector2f backRight = new Vector2f((-(scale.z / 2) * sinTheta) + (-(scale.x / 2) * cosTheta),
				(-(scale.z / 2) * cosTheta) + (float) ((scale.x / 2) * sinTheta));
		
		return new Vector2f[] {
				frontLeft, frontRight, backLeft, backRight
		};
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getScale() {
		return scale;
	}

	public Vector3f getRotation() {
		return rotation;
	}
	
	/**
	 * Fetches one of the bounding points.
	 * @param name - The name of the point, using one of the eight public ints.
	 * @return a Vector3f of the point
	 * @see Hitbox.FRONT_TOP_LEFT
	 * @see Hitbox.FRONT_TOP_RIGHT
	 * @see Hitbox.FRONT_BOTTOM_LEFT
	 * @see Hitbox.FRONT_BOTTOM_RIGHT
	 * @see Hitbox.BACK_TOP_LEFT
	 * @see Hitbox.BACK_TOP_RIGHT
	 * @see Hitbox.BACK_BOTTOM_LEFT
	 * @see Hitbox.BACK_BOTTOM_RIGHT
	 */
	public Vector3f getBoundingPointByName(int name) {
		return bounds[name];
	}
	
	public Vector3f[] getBounds() {
		return bounds;
	}
	
}
