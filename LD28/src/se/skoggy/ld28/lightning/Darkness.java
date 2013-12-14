package se.skoggy.ld28.lightning;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import se.skoggy.entity.Entity;

public class Darkness extends Entity{

	float scaleOffset = 1f;
	float total = 0f;

	public Darkness(TextureRegion texture) {
		super(texture);
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		total += dt;

		scaleOffset = 0.6f + (float)Math.sin(total * 0.005f) * 0.01f;
		transform.setScale(scaleOffset);
	}
}
