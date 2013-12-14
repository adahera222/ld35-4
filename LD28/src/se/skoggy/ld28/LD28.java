package se.skoggy.ld28;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class LD28 implements ApplicationListener {

	LD28Game game;

	@Override
	public void create() {
		// Game Settings
		Texture.setEnforcePotImages(false);
		Gdx.graphics.setVSync(true);

		game = new LD28Game();
		game.load();
	}

	@Override
	public void dispose() {
		game.dispose();
	}

	@Override
	public void render() {
		float dt = Gdx.graphics.getDeltaTime() * 1000f;
		if(dt > GameSettings.DELTA_MAX_MS_CAP)
			dt = GameSettings.DELTA_MAX_MS_CAP;
		game.update(dt);

		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		game.draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
