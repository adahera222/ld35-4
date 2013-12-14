package se.skoggy.ld28.scenes;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import se.skoggy.animation.Animation;
import se.skoggy.content.ContentManager;
import se.skoggy.ld28.GameSettings;
import se.skoggy.ld28.behaviors.CharacterKeyboardController;
import se.skoggy.ld28.behaviors.TmxMapCollisionBehavior;
import se.skoggy.ld28.behaviors.TorchBehavior;
import se.skoggy.ld28.characters.PlayerCharacter;
import se.skoggy.ld28.lightning.Darkness;
import se.skoggy.ld28.maps.Map;
import se.skoggy.scenes.Scene;
import se.skoggy.tmx.TmxMapLoader;

public class GameScene extends Scene{

	Map map;
	Darkness darkness;
	PlayerCharacter player;

	public GameScene(float width, float height) {
		super(width, height);
	}

	@Override
	protected void initCam() {
		createCam(new Rectangle(0, 0, (int)width, (int)height));
		cam.zoom = GameSettings.ZOOM;
	}

	@Override
	public float transitionInDuration() {
		return 0;
	}

	@Override
	public float transitionOutDuration() {
		return 0;
	}

	@Override
	public boolean isPopup() {
		return false;
	}

	@Override
	public void load(ContentManager content) {
		map = TmxMapLoader.load(Gdx.files.internal("maps/testmap_1.json").reader(), Map.class);
		map.load(content);

		darkness = new Darkness(content.loadTexture("gfx/darkness"));
		darkness.transform.setScale(cam.zoom);

		HashMap<String, Animation> animations = new HashMap<String, Animation>();
		animations.put("idle", new Animation(new int[]{ 1 }, 500f));
		animations.put("walk", new Animation(new int[]{ 1 }, 500f));
		animations.put("jump", new Animation(new int[]{ 0 }, 500f));
		player = new PlayerCharacter(content.loadTexture("gfx/player"), animations, 16, 4);
		player.addBehavior(new CharacterKeyboardController());
		player.addBehavior(new TmxMapCollisionBehavior(map));
		player.addBehavior(new TorchBehavior(content.loadTexture("gfx/torch")));
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

		super.update(dt);
	}

	@Override
	public void draw(SpriteBatch sb) {
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		map.draw(sb);
		player.draw(sb);
		darkness.draw(sb);
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
