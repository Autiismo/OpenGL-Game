package entities;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {
	
	private List<Entity> entities = new ArrayList<Entity>();
	
	/**
	 * Adds an entity
	 * @param entity - The entity
	 */
	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	/**
	 * Removes an entity
	 * @param entity - The entity
	 * @return a boolean representing if the entity was removed
	 */
	public boolean removeEntity(Entity entity) {
		if(entities.contains(entity)) {
			entities.remove(entity);
			return true;
		}
		return false;
	}
	
	public Entity[] getEntitiesArray() {
		return (Entity[]) entities.toArray();
	}
	
	public List<Entity> getEntitiesList() {
		return entities;
	}
	
}
