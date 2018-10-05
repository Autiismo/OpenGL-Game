package bloom;

import shaders.ShaderProgram;

public class CombineShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/bloom/simpleVertex.txt";
	private static final String FRAGMENT_FILE = "/bloom/combineFragment.txt";
	
	private int location_colorTexture;
	private int location_highlightTexture;
	private int location_shadowTexture;
	
	protected CombineShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_colorTexture = super.getUniformLocation("colorTexture");
		location_highlightTexture = super.getUniformLocation("highlightTexture");
		location_shadowTexture = super.getUniformLocation("shadowTexture");
	}
	
	protected void connectTextureUnits(){
		super.loadInt(location_colorTexture, 0);
		super.loadInt(location_highlightTexture, 1);
		super.loadInt(location_shadowTexture, 2);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
}
