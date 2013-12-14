package se.skoggy.ld28.maps;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import se.skoggy.animation.Animation;
import se.skoggy.content.ContentManager;
import se.skoggy.tmx.TmxLayer;
import se.skoggy.tmx.TmxMap;
import se.skoggy.tmx.TmxObject;

public class Map extends TmxMap{

	TextureRegion texture;
	int srcWidth, srcHeight;
	int colsOnSheet, rowsOnSheet;

	public void load(ContentManager content){
		texture = content.loadTexture("gfx/" + tilesets.get(0).name);
		srcWidth = tilesets.get(0).tilewidth;
		srcHeight = tilesets.get(0).tileheight;
		colsOnSheet = tilesets.get(0).imagewidth / tilesets.get(0).tilewidth;
		rowsOnSheet = tilesets.get(0).imageheight / tilesets.get(0).tileheight;

		constructObjects();
	}

	private void constructObjects(){
		List<TmxLayer> objLayers = getObjectLayers();
		for (TmxLayer l : objLayers) {
			for (TmxObject o : l.objects) {
				if(o.type.equals("player")){
					// TODO: set player position
				}
			}
		}
	}

	private TmxLayer collidableLayer;
	public TmxLayer getCollidableLayer(){
		if(collidableLayer != null)
			return collidableLayer;
		for (int i = 0; i < layers.size(); i++) {
			if(layers.get(i).properties.containsKey("solid") && layers.get(i).properties.get("solid").equals("true")){
				collidableLayer = layers.get(i);
				return collidableLayer;
			}
		}
		return null;
	}

	public int getColumnOfNextCollidableCell(int startCol, int row, boolean goRight) {
		if(goRight){
			for (int col = startCol; col < width; col++) {
				if(collides(col, row))
					return col;
			}
		}else{ // left
			for (int col = startCol; col >= 0; col--) {
				if(collides(col, row))
					return col + 1;
			}
		}
		return goRight ? width : 0;
	}

	public void update(float dt){
	}

	/**
	 * Warning: Generates garbage
	 * @return
	 */
	public List<TmxLayer> getObjectLayers(){
		List<TmxLayer> l = new ArrayList<TmxLayer>();
		for (TmxLayer layer : layers) {
			if(layer.type.equals("objectgroup")){
				l.add(layer);
			}
		}
		return l;
	}

	public boolean collides(int col, int row){
		TmxLayer collisionLayer = getCollidableLayer();
		if(col < 0)
			return true;
		if(row < 0)
			return true;
		if(col > collisionLayer.width - 1)
			return true;
		if(row > collisionLayer.height - 1)
			return true;
		return collisionLayer.getCell(col, row) != 0;
	}

	public boolean collides(int col, int row, int[] ignoreIndices){
		TmxLayer collisionLayer = getCollidableLayer();
		if(col < 0)
			return true;
		if(row < 0)
			return true;
		if(col > collisionLayer.width - 1)
			return true;
		if(row > collisionLayer.height - 1)
			return true;
		int cell = collisionLayer.getCell(col, row);

		for (int i = 0; i < ignoreIndices.length; i++) {
			if(ignoreIndices[i]  == cell)
				return false;
		}

		return cell != 0;
	}

	public void draw(SpriteBatch sb){
		for (int l = 0; l < layers.size(); l++) {
			TmxLayer layer = layers.get(l);
			if(layer.type.equals("tilelayer")){
				for (int col = 0; col < layer.width; col++) {
					for (int row = 0; row < layer.height; row++) {
						int cell = layer.data[col + row * layer.width] - 1;
						if(cell > -1){
							float x = (srcWidth * col);
							float y = (srcHeight * row);
							int srcX = (cell % colsOnSheet) * srcWidth;
							int srcY = (cell / rowsOnSheet) * srcHeight;
							texture.setRegion( srcX, srcY, srcWidth, srcHeight);
							texture.flip(false,  true);
							sb.setColor(layer.opacity, layer.opacity, layer.opacity, 1f);
							sb.draw(texture, x,  y);
						}
					}
				}
			}
		}
	}


	public void drawBackground(SpriteBatch sb) {
		for (int l = 0; l < layers.size(); l++) {
			TmxLayer layer = layers.get(l);
			if(layer.type.equals("tilelayer") && !layer.properties.containsKey("foreground")){
				for (int col = 0; col < layer.width; col++) {
					for (int row = 0; row < layer.height; row++) {
						int cell = layer.data[col + row * layer.width] - 1;
						if(cell > -1){
							float x = (srcWidth * col);
							float y = (srcHeight * row);
							int srcX = (cell % colsOnSheet) * srcWidth;
							int srcY = (cell / rowsOnSheet) * srcHeight;
							texture.setRegion( srcX, srcY, srcWidth, srcHeight);
							texture.flip(false,  true);
							sb.setColor(layer.opacity, layer.opacity, layer.opacity, 1f);
							sb.draw(texture, x,  y);
						}
					}
				}
			}
		}
	}

	public void drawForeground(SpriteBatch sb) {
		for (int l = 0; l < layers.size(); l++) {
			TmxLayer layer = layers.get(l);
			if(layer.type.equals("tilelayer") && layer.properties.containsKey("foreground")){
				for (int col = 0; col < layer.width; col++) {
					for (int row = 0; row < layer.height; row++) {
						int cell = layer.data[col + row * layer.width] - 1;
						if(cell > -1){
							float x = (srcWidth * col);
							float y = (srcHeight * row);
							int srcX = (cell % colsOnSheet) * srcWidth;
							int srcY = (cell / rowsOnSheet) * srcHeight;
							texture.setRegion( srcX, srcY, srcWidth, srcHeight);
							texture.flip(false,  true);
							sb.setColor(layer.opacity, layer.opacity, layer.opacity, 1f);
							sb.draw(texture, x,  y);
						}
					}
				}
			}
		}
	}
}
