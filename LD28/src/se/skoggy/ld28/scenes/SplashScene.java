package se.skoggy.ld28.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import se.skoggy.content.ContentManager;
import se.skoggy.entity.Entity;
import se.skoggy.ld28.GameSettings;
import se.skoggy.scenes.Scene;
import se.skoggy.utils.Helpers;
import se.skoggy.utils.TimerTrig;

public class SplashScene extends Scene {

	Vector2 screenCenter, startPosition;
	Entity logo;
	float targetScale = 10f;

	TimerTrig durationTrig;

	public SplashScene(float width, float height) {
		super(width, height);
		this.screenCenter = new Vector2(width * 0.5f, height * 0.5f);
		startPosition = new Vector2(width * 0.2f, height * 0.2f);
	}

	@Override
	public float transitionInDuration() {
		return 800f;
	}
	@Override
	public float transitionOutDuration() {
		return 800f;
	}
	@Override
	public boolean isPopup() {
		return false;
	}

	@Override
	public void load(ContentManager content) {
		logo = new Entity(content.loadTexture("gfx/skoggy_logo"));
		durationTrig = new TimerTrig(2000f);
	}

	@Override
	protected void initCam() {
		createCam(new Rectangle(0, 0, width, height));
	}

	private void onClickEndScene(){
		if(!manager.isTransitioningOut()){
			if(Gdx.input.isKeyPressed(Keys.SPACE)){
				endScene();
			}
		}
	}

	@Override
	public void update(float dt) {
		logo.transform.setScale(targetScale);
		logo.transform.rotation = 0f;
		logo.transform.position.x = screenCenter.x;
		logo.transform.position.y = screenCenter.y;

		if(durationTrig.isTrigged(dt)){
			endScene();
		}
		onClickEndScene();
	}

	private void endScene(){
		manager.popScene();
		GameScene gameScene = new GameScene(width,  height);
		manager.pushScene(gameScene);
		gameScene.load(new ContentManager(GameSettings.CONTENT_ROOT, true));
	}

	@Override
	public void updateTransitionIn(float dt, float progress) {
		logo.transform.setScale(Helpers.lerp(0f, targetScale, progress));
		logo.transform.rotation = Helpers.lerp(0f, (float)Math.toRadians(720f), progress);
		logo.transform.position.x = Helpers.lerp(startPosition.x, screenCenter.x, progress);
		logo.transform.position.y = Helpers.lerp(startPosition.y, screenCenter.y, progress);
		onClickEndScene();
	}

	@Override
	public void updateTransitionOut(float dt, float progress) {
		logo.transform.setScale(Helpers.lerp(targetScale, 0f, progress));
		logo.transform.rotation = Helpers.lerp((float)Math.toRadians(720f), 0f, progress);
		logo.transform.position.x = Helpers.lerp(screenCenter.x, startPosition.x, progress);
		logo.transform.position.y = Helpers.lerp(screenCenter.y, startPosition.y, progress);
		onClickEndScene();
	}

	@Override
	public void draw(SpriteBatch sb) {
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		logo.draw(sb);
		sb.end();
	}

	@Override
	public void drawTransitionIn(SpriteBatch sb, float progress) {
		draw(sb);
	}

	@Override
	public void drawTransitionOut(SpriteBatch sb, float progress) {
		draw(sb);
	}
}
