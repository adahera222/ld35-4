package se.skoggy.ld28.scenes;

import java.util.HashMap;

import se.skoggy.animation.Animation;
import se.skoggy.content.ContentManager;
import se.skoggy.entity.Entity;
import se.skoggy.ld28.GameSettings;
import se.skoggy.ld28.characters.PlayerCharacter;
import se.skoggy.scenes.Scene;
import se.skoggy.scenes.SceneManager;
import se.skoggy.utils.Helpers;
import se.skoggy.utils.TimerTrig;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MenuScene extends Scene {

	Vector2 screenCenter, startPosition;
	BitmapFont font;
	Entity logo;
	float targetScale = 8f;
	Entity blackOverlay;
	float brightness = 1f;
	PlayerCharacter player;

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
		blackOverlay = new Entity(content.loadTexture("gfx/black"));
		blackOverlay.teleport(width * 0.5f, height * 0.5f);

		HashMap<String, Animation> animations = new HashMap<String, Animation>();
		animations.put("idle", new Animation(new int[]{ 0, 1, 4 }, 40f));
		animations.put("walk", new Animation(new int[]{ 0, 1, 4 }, 40f));
		player = new PlayerCharacter(content.loadTexture("gfx/player"), animations, 32, 4);
		player.transform.setScale(4f);
		player.teleport(width * 0.5f, height * 0.8f);
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
		player.updateWithoutBehaviors(dt);
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
		player.updateWithoutBehaviors(dt);
		logo.transform.setScale(targetScale);
		logo.transform.position.x = Helpers.lerp(startPosition.x, screenCenter.x, 1f);
		logo.transform.position.y = Helpers.lerp(startPosition.y, screenCenter.y, 1f);
		logo.color.a = progress;
		player.transform.position.y = Helpers.lerp(1000f, height * 0.8f, progress);
		onClickEndScene();
	}

	@Override
	public void updateTransitionOut(float dt, float progress) {
		player.updateWithoutBehaviors(dt);
		logo.transform.setScale(Helpers.lerp(targetScale, 0f, progress));
		player.transform.position.y = Helpers.lerp(height * 0.8f, -200f, progress);
		onClickEndScene();
	}

	@Override
	public void draw(SpriteBatch sb) {
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		logo.color = Color.RED;
		logo.draw(sb);
		if(manager.getState() == SceneManager.ACTIVE){
			font.draw(sb, "Press SPACE to jump", width * 0.43f, height * 0.65f);
			font.draw(sb, "Use arrow keys to move, remember you only get one parachute", width * 0.27f, height * 0.7f);
		}

		player.draw(sb);

		blackOverlay.color.a = 1f - brightness;
		blackOverlay.draw(sb);
		sb.end();
	}

	@Override
	public void drawTransitionIn(SpriteBatch sb, float progress) {
		brightness = progress;
		draw(sb);
	}

	@Override
	public void drawTransitionOut(SpriteBatch sb, float progress) {
		brightness = 1f - progress;
		draw(sb);
	}
}
