package pl.tinlink.josu.map.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public abstract class OsuObject {
	
	public abstract void render(SpriteBatch batch, ShapeRenderer renderer, float delta);
	
	public abstract boolean update(Vector2 mousePoint, boolean click, int times);
	
}
