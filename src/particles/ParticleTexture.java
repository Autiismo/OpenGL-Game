package particles;

public class ParticleTexture {
	
	private int textureID;
	private int numberOfRows;
	private int blendingType;
	
	public static final int ALPHA_BLENDING = 0;
	public static final int ADDITIVE_BLENDING = 1;
	
	public ParticleTexture(int textureID, int numberOfRows, int blendingType) {
		super();
		this.textureID = textureID;
		this.numberOfRows = numberOfRows;
		this.blendingType = blendingType;
	}
	
	protected int getBlendingType() {
		return blendingType;
	}
	
	public int getTextureID() {
		return textureID;
	}
	
	public int getNumberOfRows() {
		return numberOfRows;
	}
	
}
