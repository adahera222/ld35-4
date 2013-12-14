package se.skoggy.ld28;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import se.skoggy.content.ContentManager;
import se.skoggy.ld28.scenes.GameScene;
import se.skoggy.ld28.scenes.SplashScene;
import se.skoggy.scenes.SceneManager;
import se.skoggy.utils.BaseGame;

public class LD28Game extends BaseGame{

	SceneManager sceneManager;

	public LD28Game() {
		sceneManager = new SceneManager();
	}

	@Override
	public void load() {
		/*
		SplashScene splashScene = new SplashScene(GameSettings.WIDTH, GameSettings.HEIGHT);
		sceneManager.pushScene(splashScene);
		splashScene.load(new ContentManager(GameSettings.CONTENT_ROOT, true));
		*/

//		/*
		GameScene gameScene = new GameScene(GameSettings.WIDTH, GameSettings.HEIGHT);
		sceneManager.pushScene(gameScene);
		gameScene.load(new ContentManager(GameSettings.CONTENT_ROOT, true));
	//	*/
	}

	@Override
	public void update(float dt) {
		sceneManager.update(dt);

		// TODO: remove
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)){
			Gdx.app.exit();
		}
	}

	public void draw() {
		sceneManager.draw(getSpriteBatch());
	}
}
