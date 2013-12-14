package se.skoggy.ld28.scenes;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

public class GameScene extends Scene{

	private Matrix4 uiProjectionMatrix;

	Map map;
	Darkness darkness;
	PlayerCharacter player;
	ParticleManager particleManager;
	Entity blackOverlay;
	float brightness = 1f;

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
		return 300;
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

		darkness = new Darkness(content.loadTexture("gfx/darkness"));
		darkness.transform.setScale(cam.zoom);

		blackOverlay = new Entity(content.loadTexture("gfx/black"));
		blackOverlay.teleport(width * 0.5f, height * 0.5f);

		// TODO: move to factory
		HashMap<String, Animation> animations = new HashMap<String, Animation>();
		animations.put("idle", new Animation(new int[]{ 1 }, 500f));
		animations.put("walk", new Animation(new int[]{ 4, 5, 6, 5 }, 150f));
		animations.put("jump", new Animation(new int[]{ 4 }, 500f));
		player = new PlayerCharacter(content.loadTexture("gfx/player"), animations, 16, 4);
		player.addBehavior(new CharacterKeyboardController());
		player.addBehavior(new TmxMapCollisionBehavior(map));
		player.addBehavior(new TorchBehavior(content.loadTexture("gfx/torch"), particleManager));
		player.transform.position.x = 50;
		player.transform.position.y = 200;

	}

	@Override
	public void update(float dt) {

		player.update(dt);

		cam.position.x = player.transform.position.x;
		cam.position.y = player.transform.position.y;

		darkness.transform.position.x = cam.position.x;
		darkness.transform.position.y = cam.position.y;
		darkness.update(dt);

		particleManager.update(dt);
		map.update(dt);

		// TODO: fix
		brightness = (float)Math.sin(dt * 0.03f);

		super.update(dt);
	}

	@Override
	public void draw(SpriteBatch sb) {
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		map.drawBackground(sb);
		player.draw(sb);
		particleManager.draw(sb);
		map.drawForeground(sb);
		darkness.draw(sb);
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
		update(2f);
		draw(sb);
	}

	@Override
	public void drawTransitionOut(SpriteBatch sb, float progress) {
		brightness = 1f - progress;
		draw(sb);
	}
}
