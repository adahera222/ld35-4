package se.skoggy.ld28.particles;

import com.badlogic.gdx.graphics.Color;

public class Particle {

	public float x, y, vx, vy;
	public float rotation, scale, current,duration;
	public int type;
	public Color color, startColor, endColor;
	public float startScale, endScale;
	public float rotationSpeed;
	public ParticleEmitter owner;

	public Particle() {
		color = new Color();
		startColor = new Color();
		endColor = new Color();
	}
}
