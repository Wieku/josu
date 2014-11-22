package pl.tinlink.josu.map.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import pl.tinlink.josu.map.BeatMapSettings;

public class Circle extends OsuObject {

	static Sprite basic;
	static BeatMapSettings sett;
	
	Vector2 pos;
	Sprite sprite;
	
	public static void init(BeatMapSettings settings){
		basic = new Sprite(new Texture(Gdx.files.internal("assets/skin/hitcircle.png")), settings.calcCircleSize, settings.calcCircleSize);
		sett = settings;
	}
	
	public Circle(int time, int x, int y, Color comboColor, int sample){
		sprite = new Sprite(basic);
		pos = new Vector2(sett.minX+x*sett.multX, sett.minY + y*sett.multY);
		sprite.setCenter(pos.x, pos.y);
		sprite.setColor(comboColor.r, comboColor.g, comboColor.b, 0.5f);
	}
	
	@Override
	public void render(SpriteBatch batch, ShapeRenderer renderer, float delta) {
		sprite.draw(batch);
		
	}

	@Override
	public boolean update(Vector2 mousePoint, boolean click, int time) {
		
		if(click){
			
			
			
			return true;
		}
		
		//if()
		
		return false;
	}
	
	
	public static void fadeCircle(){
		
	}
	
	
}
