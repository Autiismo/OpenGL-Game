package engineTester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;
import hitboxes.Hitbox;
import models.TexturedModel;
import particles.ParticleMaster;
import particles.ParticleSystem;
import particles.ParticleTexture;
import postProcessing.Fbo;
import postProcessing.PostProcessing;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrain.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

public class MainGameLoop {
	
	private static Loader loader;

	public static void main(String[] args) {

		DisplayManager.createDisplay();

		loader = new Loader();
		
		List<Entity> entities = new ArrayList<Entity>();
		List<Entity> normalMapEntities = new ArrayList<Entity>();

		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		
		Terrain terrain = new Terrain(0, 0, loader, texturePack, new TerrainTexture(loader.loadTexture("blendMap")), "heightmap");
		
		TexturedModel playerModel = new TexturedModel(OBJLoader.loadOBJ("player", loader), new ModelTexture(loader.loadTexture("playerTexture")));
		Player player = new Player(playerModel, new Vector3f(400, terrain.getHeightOfTerrain(400, 400), 400), 0, 0, 0, 0.6f);
		entities.add(player);
		Camera camera = new Camera(player);
		
		List<Light> lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(200000, 200000, 200000), new Vector3f(1, 1, 1));
		lights.add(sun);
		//+x = left (west) 90 yRot; -x = right (east) 270 yRot; +z = forward (north) 0 yRot; -z = backwards (south) 180 yRot;
		//lights.add(new Light(new Vector3f(400, 50, 400), new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f)));
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		MasterRenderer renderer = new MasterRenderer(loader, camera);
		ParticleMaster.init(loader, renderer.getProjectionMatrix());
		
		ParticleTexture texture = new ParticleTexture(loader.loadTexture("particleAtlas"), 4, ParticleTexture.ADDITIVE_BLENDING);
		
		ParticleSystem system = new ParticleSystem(texture, 50, 25, 0.3f, 4, 1);
		system.setDirection(new Vector3f(0, 1, 0), 0.1f);
		system.setLifeError(0.1f);
		system.setSpeedError(0.4f);
		system.setScaleError(0.8f);
		
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);
		
		WaterFrameBuffers waterFbos = new WaterFrameBuffers();
		
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), waterFbos);
		List<WaterTile> waters = new ArrayList<WaterTile>();
		//waters.add(new WaterTile(400, 400, 0));
		
		TextMaster.init(loader);
		
		float[] playerHealthStats = player.updatePlayer();
		
		FontType font = new FontType(loader.loadFontTexture("comfortaa"), new File("res/comfortaa.fnt"));
		//VALUES HERE ARE IRRELEVANT
		GUIText healthBar = new GUIText("", 1, font, toGuiCoords(0, 0), 1f, true, 0);
		
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		/*GuiTexture shadowMap = new GuiTexture(renderer.getShadowMapTexture(), new Vector2f(0.5f, 0.5f), new Vector2f(0.5f, 0.5f));
		guis.add(shadowMap);*/
		
		Fbo multisampleFbo = new Fbo(Display.getWidth(), Display.getHeight(), (float) 3);
		Fbo[] fbos = new Fbo[3];
		for(int i = 0; i < fbos.length; i++) {
			fbos[i] = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);
		}
		PostProcessing.init(loader);
		
		// MODELS BELOW
		
		TexturedModel lantern = new TexturedModel(OBJLoader.loadOBJ("lantern", loader), new ModelTexture(loader.loadTexture("lantern")));
		lantern.getTexture().setExtraInfoMap(loader.loadTexture("lanternSpecular"));
		Entity lanternEntity = new Entity(lantern, new Vector3f(400, terrain.getHeightOfTerrain(400, 410), 410), 0, 0, 0, 1f);
		entities.add(lanternEntity);
		
		TexturedModel barrel = new TexturedModel(OBJLoader.loadOBJ("barrel", loader), new ModelTexture(loader.loadTexture("barrel")));
		Entity pointEntity1 = new Entity(barrel, new Vector3f(400, terrain.getHeightOfTerrain(400, 410), 410), 0, 0, 0, 0.1f);
		Entity pointEntity2 = new Entity(barrel, new Vector3f(400, terrain.getHeightOfTerrain(400, 410), 410), 0, 0, 0, 0.1f);
		Entity pointEntity3 = new Entity(barrel, new Vector3f(400, terrain.getHeightOfTerrain(400, 410), 410), 0, 0, 0, 0.1f);
		Entity pointEntity4 = new Entity(barrel, new Vector3f(400, terrain.getHeightOfTerrain(400, 410), 410), 0, 0, 0, 0.1f);
		Entity pointEntity5 = new Entity(barrel, new Vector3f(400, terrain.getHeightOfTerrain(400, 410), 410), 0, 0, 0, 0.1f);
		Entity pointEntity6 = new Entity(barrel, new Vector3f(400, terrain.getHeightOfTerrain(400, 410), 410), 0, 0, 0, 0.1f);
		Entity pointEntity7 = new Entity(barrel, new Vector3f(400, terrain.getHeightOfTerrain(400, 410), 410), 0, 0, 0, 0.1f);
		Entity pointEntity8 = new Entity(barrel, new Vector3f(400, terrain.getHeightOfTerrain(400, 410), 410), 0, 0, 0, 0.1f);
		entities.add(pointEntity1);
		entities.add(pointEntity2);
		entities.add(pointEntity3);
		entities.add(pointEntity4);
		entities.add(pointEntity5);
		entities.add(pointEntity6);
		entities.add(pointEntity7);
		entities.add(pointEntity8);
		
		Entity pointPlayer1 = new Entity(barrel, new Vector3f(400, terrain.getHeightOfTerrain(400, 410), 410), 0, 0, 0, 0.1f);
		Entity pointPlayer2 = new Entity(barrel, new Vector3f(400, terrain.getHeightOfTerrain(400, 410), 410), 0, 0, 0, 0.1f);
		Entity pointPlayer3 = new Entity(barrel, new Vector3f(400, terrain.getHeightOfTerrain(400, 410), 410), 0, 0, 0, 0.1f);
		Entity pointPlayer4 = new Entity(barrel, new Vector3f(400, terrain.getHeightOfTerrain(400, 410), 410), 0, 0, 0, 0.1f);
		Entity pointPlayer5 = new Entity(barrel, new Vector3f(400, terrain.getHeightOfTerrain(400, 410), 410), 0, 0, 0, 0.1f);
		Entity pointPlayer6 = new Entity(barrel, new Vector3f(400, terrain.getHeightOfTerrain(400, 410), 410), 0, 0, 0, 0.1f);
		Entity pointPlayer7 = new Entity(barrel, new Vector3f(400, terrain.getHeightOfTerrain(400, 410), 410), 0, 0, 0, 0.1f);
		Entity pointPlayer8 = new Entity(barrel, new Vector3f(400, terrain.getHeightOfTerrain(400, 410), 410), 0, 0, 0, 0.1f);
		entities.add(pointPlayer1);
		entities.add(pointPlayer2);
		entities.add(pointPlayer3);
		entities.add(pointPlayer4);
		entities.add(pointPlayer5);
		entities.add(pointPlayer6);
		entities.add(pointPlayer7);
		entities.add(pointPlayer8);
		
		// MODELS ABOVE
		
		while (!Display.isCloseRequested()) {
			player.move(terrain);
			playerHealthStats = player.updatePlayer();
			//healthBar.remove();
			//healthBar = new GUIText("HP: " + playerHealthStats[0], 1, font, toGuiCoords(125, 650), 1f, true, 0);
			//healthBar.setColor(1.0f, 1.0f, 1.0f);
			//TODO: Play around with values of text to polish it a bit
			
			pointEntity1.setPosition(lanternEntity.getHitbox().getBoundingPointByName(Hitbox.FRONT_TOP_LEFT));
			pointEntity2.setPosition(lanternEntity.getHitbox().getBoundingPointByName(Hitbox.FRONT_TOP_RIGHT));
			pointEntity3.setPosition(lanternEntity.getHitbox().getBoundingPointByName(Hitbox.FRONT_BOTTOM_LEFT));
			pointEntity4.setPosition(lanternEntity.getHitbox().getBoundingPointByName(Hitbox.FRONT_BOTTOM_RIGHT));
			pointEntity5.setPosition(lanternEntity.getHitbox().getBoundingPointByName(Hitbox.BACK_TOP_LEFT));
			pointEntity6.setPosition(lanternEntity.getHitbox().getBoundingPointByName(Hitbox.BACK_TOP_RIGHT));
			pointEntity7.setPosition(lanternEntity.getHitbox().getBoundingPointByName(Hitbox.BACK_BOTTOM_LEFT));
			pointEntity8.setPosition(lanternEntity.getHitbox().getBoundingPointByName(Hitbox.BACK_BOTTOM_RIGHT));
			pointPlayer1.setPosition(player.getHitbox().getBoundingPointByName(Hitbox.FRONT_TOP_LEFT));
			pointPlayer2.setPosition(player.getHitbox().getBoundingPointByName(Hitbox.FRONT_TOP_RIGHT));
			pointPlayer3.setPosition(player.getHitbox().getBoundingPointByName(Hitbox.FRONT_BOTTOM_LEFT));
			pointPlayer4.setPosition(player.getHitbox().getBoundingPointByName(Hitbox.FRONT_BOTTOM_RIGHT));
			pointPlayer5.setPosition(player.getHitbox().getBoundingPointByName(Hitbox.BACK_TOP_LEFT));
			pointPlayer6.setPosition(player.getHitbox().getBoundingPointByName(Hitbox.BACK_TOP_RIGHT));
			pointPlayer7.setPosition(player.getHitbox().getBoundingPointByName(Hitbox.BACK_BOTTOM_LEFT));
			pointPlayer8.setPosition(player.getHitbox().getBoundingPointByName(Hitbox.BACK_BOTTOM_RIGHT));
			
			camera.move();
			picker.update();
			ParticleMaster.update(camera);
			//system.generateParticles(new Vector3f(400, 10, 400));
			
			renderer.renderShadowMap(entities, sun);
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			
			//reflection
			waterFbos.bindReflectionFrameBuffer();
			for(WaterTile water:waters) {
				float distance = 2 * (camera.getPosition().y - water.getHeight());
				camera.getPosition().y -= distance;
				camera.invertPitch();
				renderer.renderScene(entities, normalMapEntities, terrain, lights, camera, new Vector4f(0, 1, 0, -water.getHeight() + 1f));
				camera.getPosition().y += distance;
				camera.invertPitch();
			}
			
			//refraction
			waterFbos.bindRefractionFrameBuffer();
			for(WaterTile water:waters) {
				renderer.renderScene(entities, normalMapEntities, terrain, lights, camera, new Vector4f(0, -1, 0, water.getHeight()));
			}
			
			//regular
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			waterFbos.unbindCurrentFrameBuffer();
			
			multisampleFbo.bindFrameBuffer();
			renderer.renderScene(entities, normalMapEntities, terrain, lights, camera, new Vector4f(0, -1, 0, 15));
			waterRenderer.render(waters, camera, sun);
			ParticleMaster.render(camera);
			multisampleFbo.unbindFrameBuffer();
			for(int i = 0; i < fbos.length; i++) {
				multisampleFbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT0 + i, fbos[i]);
			}
			PostProcessing.doPostProcessing(fbos[0].getColorTexture(), fbos[1].getColorTexture(), fbos[2].getColorTexture());
			
			//lanternEntity.getHitbox().isCollided(player.getHitbox());
			
			guiRenderer.render(guis);
			TextMaster.render();
			DisplayManager.updateDisplay();
		}
		
		PostProcessing.cleanUp();
		multisampleFbo.cleanUp();
		for(int i = 0; i < fbos.length; i++) {
			fbos[i].cleanUp();
		}
		ParticleMaster.cleanUp();
		TextMaster.cleanUp();
		waterFbos.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
		
	}
	
	public static Loader getLoader() {
		return loader;
	}
	
	/**
	 * Converts the OpenGL x and y coordinates to a new set of coordinates, relative to the screen.
	 * @param x - The x coordinate in terms of OpenGL.
	 * @param y - The y coordinate in terms of OpenGL.
	 * @return a Vector2f of the screen coordinates.
	 */
	private static Vector2f toScreenCoords(float x, float y) {
		return(new Vector2f((x + 0.5f) * DisplayManager.getWidth(), y * DisplayManager.getHeight()));
	}
	
	/**
	 * Converts the screen x and y coordinates to a new set of coordinates, relative to the OpenGL coordinate system.
	 * @param x - The x coordinate of the screen.
	 * @param y - The y coordinate of the screen.
	 * @return a Vector2f of the OpenGL coordinates.
	 */
	private static Vector2f toGuiCoords(float x, float y) {
		return(new Vector2f((x/DisplayManager.getWidth()) - 0.5f, y/DisplayManager.getHeight()));
	}
	
}
