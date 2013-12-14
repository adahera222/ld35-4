package se.skoggy.ld28.scenes;

import se.skoggy.content.ContentManager;
import se.skoggy.entity.Entity;
import se.skoggy.ld28.GameSettings;
import se.skoggy.scenes.Scene;
import se.skoggy.scenes.SceneManager;
import se.skoggy.utils.Helpers;
import se.skoggy.utils.TimerTrig;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MenuScene extends Scene {

	Vector2 screenCenter, startPosition;
	BitmapFont font;
	Entity logo;
	float targetScale = 4f;

	public MenuScene(float width, float height) {
		super(width, height);
		this.screenCenter = new Vector2(width * 0.5f, height * 0.5f);
		startPosition = new Vector2(width * 0.5f, 0f);
	}

	@Override
	public float transitionInDuration() {
		return 3000f;
	}
	@Override
	public float transitionOutDuration() {
		return 2000f;
	}
	@Override
	public boolean isPopup() {
		return false;
	}

	@Override
	public void load(ContentManager content) {
		logo = new Entity(content.loadTexture("gfx/torch_logo"));
		font = content.loadFont("monaco_32");
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
		logo.transform.setScale(targetScale);
		logo.transform.position.x = Helpers.lerp(startPosition.x, screenCenter.x, 1f);
		logo.transform.position.y = Helpers.lerp(startPosition.y, screenCenter.y, 1f);
		logo.color.a = progress;
		onClickEndScene();
	}

	@Override
	public void updateTransitionOut(float dt, float progress) {
		logo.transform.setScale(Helpers.lerp(targetScale, 0f, progress));
		onClickEndScene();
	}

	@Override
	public void draw(SpriteBatch sb) {
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		logo.draw(sb);
		if(manager.getState() == SceneManager.ACTIVE){
			font.draw(sb, "Press SPACE to light the torch", width * 0.4f, height * 0.7f);
		}
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
