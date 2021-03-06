package postProcessing;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import bloom.BrightFilter;
import bloom.CombineFilter;
import gaussianBlur.HorizontalBlur;
import gaussianBlur.VerticalBlur;
import models.RawModel;
import renderEngine.Loader;

public class PostProcessing {
	
	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private static RawModel quad;
	private static HorizontalBlur hBlur;
	private static VerticalBlur vBlur;
	private static HorizontalBlur shadowHBlur;
	private static VerticalBlur shadowVBlur;
	private static CombineFilter combineFilter;

	public static void init(Loader loader){
		quad = loader.loadToVAO(POSITIONS, 2);
		hBlur = new HorizontalBlur(Display.getWidth()/5, Display.getHeight()/5);
		vBlur = new VerticalBlur(Display.getWidth()/5, Display.getHeight()/5);
		shadowHBlur = new HorizontalBlur(Display.getWidth(), Display.getHeight());
		shadowVBlur = new VerticalBlur(Display.getWidth(), Display.getHeight());
		combineFilter = new CombineFilter();
	}
	
	public static void doPostProcessing(int colorTexture, int brightTexture, int shadowTexture){
		start();
		hBlur.render(brightTexture);
		vBlur.render(hBlur.getOutputTexture());
		shadowHBlur.render(shadowTexture);
		shadowVBlur.render(shadowHBlur.getOutputTexture());
		combineFilter.render(colorTexture, vBlur.getOutputTexture(), shadowVBlur.getOutputTexture());
		end();
	}
	
	public static void cleanUp(){
		hBlur.cleanUp();
		vBlur.cleanUp();
		shadowHBlur.cleanUp();
		shadowVBlur.cleanUp();
		combineFilter.cleanUp();
	}
	
	private static void start(){
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private static void end(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}


}
