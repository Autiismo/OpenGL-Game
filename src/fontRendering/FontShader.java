package fontRendering;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import shaders.ShaderProgram;

public class FontShader extends ShaderProgram{

	private static final String VERTEX_FILE = "/fontRendering/fontVertex.txt";
	private static final String FRAGMENT_FILE = "/fontRendering/fontFragment.txt";
	
	private int location_color;
	private int location_translation;
	private int location_effect;
	private int location_width;
	private int location_edge;
	private int location_borderWidth;
	private int location_borderEdge;
	private int location_bubbleWidth;
	private int location_bubbleEdge;
	private int location_glowWidth;
	private int location_glowEdge;
	private int location_dropWidth;
	private int location_dropEdge;
	private int location_offset;
	private int location_outlineColor;
	
	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_color = getUniformLocation("color");
		location_translation = getUniformLocation("translation");
		location_effect = getUniformLocation("effect");
		location_width = getUniformLocation("width");
		location_edge = getUniformLocation("edge");
		location_borderWidth = getUniformLocation("borderWidth");
		location_borderEdge = getUniformLocation("borderEdge");
		location_bubbleWidth = getUniformLocation("bubbleWidth");
		location_bubbleEdge = getUniformLocation("bubbleEdge");
		location_glowWidth = getUniformLocation("glowWidth");
		location_glowEdge = getUniformLocation("glowEdge");
		location_dropWidth = getUniformLocation("dropWidth");
		location_dropEdge = getUniformLocation("dropEdge");
		location_offset = getUniformLocation("offset");
		location_outlineColor = getUniformLocation("outlineColor");
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
		bindAttribute(1, "textureCoords");
	}
	
	protected void loadColor(Vector3f color) {
		loadVector(location_color, color);
	}
	
	protected void loadTranslation(Vector2f translation) {
		load2DVector(location_translation, translation);
	}
	
	public void loadEffect(int effectID) {
		loadInt(location_effect, effectID);
	}
	
	public void loadVars(float width, float edge, float borderWidth, float borderEdge, float bubbleWidth, float bubbleEdge, float glowWidth, float glowEdge, float dropWidth, float dropEdge, Vector2f offset, Vector3f outlineColor) {
		loadFloat(location_width, width);
		loadFloat(location_edge, edge);
		loadFloat(location_borderWidth, borderWidth);
		loadFloat(location_borderEdge, borderEdge);
		loadFloat(location_bubbleWidth, bubbleWidth);
		loadFloat(location_bubbleEdge, bubbleEdge);
		loadFloat(location_glowWidth, glowWidth);
		loadFloat(location_glowEdge, glowEdge);
		loadFloat(location_dropWidth, dropWidth);
		loadFloat(location_dropEdge, dropEdge);
		load2DVector(location_offset, offset);
		loadVector(location_outlineColor, outlineColor);
	}
	
}
