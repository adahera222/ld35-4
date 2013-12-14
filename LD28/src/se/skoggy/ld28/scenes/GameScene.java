package se.skoggy.ld28.scenes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

import se.skoggy.animation.Animation;
import se.skoggy.content.ContentManager;
import se.skoggy.entity.Entity;
import se.skoggy.ld28.GameSettings;
import se.skoggy.ld28.behaviors.CharacterKeyboardController;
import se.skoggy.ld28.behaviors.FollowCharacterBehavior;
import se.skoggy.ld28.behaviors.TmxMapCollisionBehavior;
import se.skoggy.ld28.behaviors.WalkBackAndForthBehavior;
import se.skoggy.ld28.characters.Cat;
import se.skoggy.ld28.characters.Enemy;
import se.skoggy.ld28.characters.PlayerCharacter;
import se.skoggy.ld28.maps.Map;
import se.skoggy.ld28.particles.ParticleManager;
import se.skoggy.ld28.weapons.Lazer;
import se.skoggy.scenes.Scene;
import se.skoggy.tmx.TmxMapLoader;
import se.skoggy.utils.Rand;

public class GameScene extends Scene{

	private Matrix4 uiProjectionMatrix;

	Map map;
	PlayerCharacter player;
	ParticleManager particleManager;
	List<Cat> cats;
	Lazer lazer;

	List<Enemy> enemies;

	// prettiness
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
		return 200;
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
		player.transform.setScale(1f);
		player.addBehavior(new CharacterKeyboardController());
		player.addBehavior(new TmxMapCollisionBehavior(map));
		player.transform.position.x = 50;
		player.transform.position.y = 200;

		//laser
		lazer = new Lazer(player);

		cam.position.x = player.transform.position.x;
		cam.position.y = player.transform.position.y;

		enemies = new ArrayList<Enemy>();

		for (int i = 0; i < 6; i++) {
			HashMap<String, Animation> anims = new HashMap<String, Animation>();
			anims.put("idle", new Animation(new int[]{ 0 }, 500f));
			anims.put("walk", new Animation(new int[]{ 1, 2, 0 }, 100f));
			anims.put("jump", new Animation(new int[]{ 1 }, 500f));
			Enemy e = new Enemy(content.loadTexture("gfx/enemy_1"), anims, 16, 2);
			e.teleport(23, 400);
			e.addBehavior(new WalkBackAndForthBehavior(map));
			e.addBehavior(new TmxMapCollisionBehavior(map));
			enemies.add(e);
		}

		super.update(16f);

		cats = new ArrayList<Cat>();

		for (int i = 0; i < 6; i++) {
			Cat c = createCat(content);
			c.teleport(player.transform.position.x, player.transform.position.y);
			cats.add(c);
		}
	}

	public Cat createCat(ContentManager content){
		HashMap<String, Animation> animations = new HashMap<String, Animation>();
		animations.put("idle", new Animation(new int[]{ 0 }, 500f));
		animations.put("walk", new Animation(new int[]{ 1, 2, 3, 0 }, 80f));
		animations.put("jump", new Animation(new int[]{ 2 }, 500f));
		Cat c = new Cat(content.loadTexture("gfx/cat"), animations);
		c.transform.setScale(0.5f);
		//c.setColor(new Color(Rand.rand(), Rand.rand(),Rand.rand(), 1f));
		c.addBehavior(new FollowCharacterBehavior(player.transform.position, map));
		c.addBehavior(new TmxMapCollisionBehavior(map));
		return c;
	}

	private void gameOver(){
		manager.popScene();
	}

	@Override
	public void update(float dt) {

		lazer.update(dt, map);
		if(lazer.isActive()){
			for (int i = 0; i < cats.size(); i++) {
				cats.get(i).setTarget(lazer.getTarget());
			}

		}

		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update(dt);
		}

		player.update(dt);
		for (int i = 0; i < cats.size(); i++) {
			cats.get(i).update(dt);
		}

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
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.draw(sb);
		}
		sb.end();

		lazer.draw(cam);

		sb.begin();
		player.draw(sb);
		for (int i = 0; i < cats.size(); i++) {
			cats.get(i).draw(sb);
		}
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
