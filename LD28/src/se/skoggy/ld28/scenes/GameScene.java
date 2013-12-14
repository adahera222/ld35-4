package se.skoggy.ld28.scenes;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;

import se.skoggy.animation.Animation;
import se.skoggy.content.ContentManager;
import se.skoggy.entity.Entity;
import se.skoggy.ld28.GameSettings;
import se.skoggy.ld28.behaviors.CharacterKeyboardController;
import se.skoggy.ld28.behaviors.TmxMapCollisionBehavior;
import se.skoggy.ld28.behaviors.TorchBehavior;
import se.skoggy.ld28.characters.PlayerCharacter;
import se.skoggy.ld28.lightning.Darkness;
import se.skoggy.ld28.maps.Map;
import se.skoggy.ld28.particles.ParticleManager;
import se.skoggy.scenes.Scene;
import se.skoggy.tmx.TmxMapLoader;
import se.skoggy.utils.Helpers;

public class GameScene extends Scene{

	private Matrix4 uiProjectionMatrix;

	Map map;
	PlayerCharacter player;
	ParticleManager particleManager;
	Entity blackOverlay;
	float brightness = 1f;
	BitmapFont font;

	public GameScene(float width, float height) {
		super(width, height);
	}

	@Override
	protected void initCam() {
		createCam(null /* new Rectangle(0, 0, (int)width, (int)height) */);
		uiProjectionMatrix = new Matrix4(cam.combined);
		cam.zoom = GameSettings.ZOOM;
	}

	@Override
	public float transitionInDuration() {
		return 2000;
	}

	@Override
	public float transitionOutDuration() {
		return 3000;
	}

	@Override
	public boolean isPopup() {
		return false;
	}

	@Override
	public void load(ContentManager content) {
		map = TmxMapLoader.load(Gdx.files.internal("maps/testmap_2.json").reader(), Map.class);
		map.load(content);

		particleManager = new ParticleManager();
		particleManager.load(content);


		blackOverlay = new Entity(content.loadTexture("gfx/black"));
		blackOverlay.teleport(width * 0.5f, height * 0.5f);


		//ui
		font = content.loadFont("monaco_32");

		// TODO: move to factory
		HashMap<String, Animation> animations = new HashMap<String, Animation>();
		animations.put("idle", new Animation(new int[]{ 0 }, 500f));
		animations.put("walk", new Animation(new int[]{ 4, 5, 6, 5 }, 150f));
		animations.put("jump", new Animation(new int[]{ 4 }, 500f));
		player = new PlayerCharacter(content.loadTexture("gfx/player"), animations, 16, 4);
		player.addBehavior(new CharacterKeyboardController());
		player.addBehavior(new TmxMapCollisionBehavior(map));
		player.transform.position.x = 50;
		player.transform.position.y = 200;

		cam.position.x = player.transform.position.x;
		cam.position.y = player.transform.position.y;

		super.update(16f);
	}

	private void gameOver(){
		manager.popScene();
	}

	@Override
	public void update(float dt) {
		player.update(dt);

		cam.position.x = player.transform.position.x;
		cam.position.y = player.transform.position.y;

		particleManager.update(dt);
		map.update(dt);


		if(isDead()){
			gameOver();
		}

		super.update(dt);
	}

	private boolean isDead(){
		return false;
	}

	@Override
	public void draw(SpriteBatch sb) {
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		map.drawBackground(sb);
		player.draw(sb);
		particleManager.draw(sb);
		map.drawForeground(sb);
		sb.end();

		sb.setProjectionMatrix(uiProjectionMatrix);
		sb.begin();
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
		brightness = 0f;

	}
}
