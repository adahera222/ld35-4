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
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		game = new LD28Game();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void render() {
		float dt = Gdx.graphics.getDeltaTime() * 1000f;
		if(dt > GameSettings.DELTA_MAX_MS_CAP)
			dt = GameSettings.DELTA_MAX_MS_CAP;
		game.update(dt);

		Gdx.gl.glClearColor(36f / 255f, 55/ 255f, 70f / 255f, 1f);
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
