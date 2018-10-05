package items;

import engineTester.MainGameLoop;
import models.TexturedModel;
import normalMappingObjConverter.NormalMappedOBJLoader;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import textures.ModelTexture;

public class Item {
	
	private int id;
	private String name;
	private float weight;
	private int maxStack;
	private TexturedModel model;
	
	private Loader loader;
	
	public Item(int id, String name, float weight, int maxStack, String modelName, String textureName, boolean normalMapped) {
		this.loader = MainGameLoop.getLoader();
		
		this.id = id;
		this.name = name;
		this.weight = weight;
		this.maxStack = maxStack;
		if(normalMapped) {
			this.model = new TexturedModel(NormalMappedOBJLoader.loadOBJ(modelName, loader), new ModelTexture(loader.loadTexture(textureName)));
		} else {
			this.model = new TexturedModel(OBJLoader.loadOBJ(modelName, loader), new ModelTexture(loader.loadTexture(textureName)));
		}
	}
	
	public Item(int id, String name, float weight, int maxStack, String modelName, boolean normalMapped) {
		this.loader = MainGameLoop.getLoader();
		
		this.id = id;
		this.name = name;
		this.weight = weight;
		this.maxStack = maxStack;
		if(normalMapped) {
			this.model = new TexturedModel(NormalMappedOBJLoader.loadOBJ(modelName, loader), new ModelTexture(loader.loadTexture(modelName)));
		} else {
			this.model = new TexturedModel(OBJLoader.loadOBJ(modelName, loader), new ModelTexture(loader.loadTexture(modelName)));
		}
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public float getWeight() {
		return weight;
	}

	public int getMaxStack() {
		return maxStack;
	}

	public TexturedModel getModel() {
		return model;
	}
	
	public void onLeftClick() {}
	public void onRightClick() {}
	
}
