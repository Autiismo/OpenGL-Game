package items;

public class BaseGun extends Item {
	
	public float damage;
	
	public BaseGun(int id, String name, float weight, int maxStack, String modelName, boolean normalMapped, float damage) {
		super(id, name, weight, maxStack, modelName, normalMapped);
		init();
	}
	
	public BaseGun(int id, String name, float weight, int maxStack, String modelName, String textureName, boolean normalMapped, float damage) {
		super(id, name, weight, maxStack, modelName, textureName, normalMapped);
		init();
	}
	
	private void init() {
		
	}
	
	
}
