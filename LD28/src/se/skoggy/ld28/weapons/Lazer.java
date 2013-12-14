package se.skoggy.ld28.weapons;

import se.skoggy.ld28.characters.PlayerCharacter;
import se.skoggy.ld28.maps.Map;
import se.skoggy.utils.Camera2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class Lazer {

	ShapeRenderer shapeRenderer;
	PlayerCharacter player;
	int target;
	boolean active = false;

	public Lazer(PlayerCharacter player) {
		this.player = player;
		shapeRenderer = new ShapeRenderer();
	}

	public boolean isActive() {
		return active;
	}

	private Vector2 _target = new Vector2();
	public Vector2 getTarget(){
		_target.x = target;
		_target.y = player.transform.position.y;
		return _target;
	}

	public void update(float dt, Map map){
		if(Gdx.input.isKeyPressed(Keys.SPACE)){
			active = true;
			target = map.getColumnOfNextCollidableCell((int)Math.floor(player.transform.position.x / map.tilewidth),
													   (int)Math.floor(player.transform.position.y / map.tileheight),
													   player.isFacingRight()) * map.tilewidth;
		}else{
			active = false;
		}
	}

	public void draw(Camera2D cam){
		if(active){
			shapeRenderer.setProjectionMatrix(cam.combined);
			shapeRenderer.setColor(Color.RED);
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.line(player.transform.position, getTarget());
			shapeRenderer.end();
		}
	}
}
