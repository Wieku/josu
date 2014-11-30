package pl.tinlink.josu.utils;

import java.util.ArrayList;

import pl.tinlink.josu.JOsuClient;
import pl.tinlink.josu.animation.accessors.SpriteAccessor;
import pl.tinlink.josu.animation.animations.Animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

//TODO: better cursor and trail behaviour
public class CursorRenderer {
	
	Sprite cursorOrigin;
	Sprite trailOrigin;
	private boolean focus;
	Pixmap blankCursor;
	int oldX = 0, oldY = 0;
	ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	Vector2 tmp = new Vector2();
	Vector2 old = new Vector2();
	Vector2 current = new Vector2();
	
	public CursorRenderer(){
		cursorOrigin = new Sprite(new Texture(Gdx.files.internal("assets/skin/cursor.png")));
		trailOrigin = new Sprite(new Texture(Gdx.files.internal("assets/skin/cursortrail.png")));
		blankCursor = new Pixmap(4,4,Format.RGBA8888);
	}
	
	public void createPoint(int x, int y){
		Sprite sprite = new Sprite(trailOrigin);
		sprite.setCenter(x, y);
		sprite.setPosition(MathUtils.floor(sprite.getX()), MathUtils.floor(sprite.getY()));
		new Animation(sprite, SpriteAccessor.FADEOUT, (11/sprite.getWidth())*0.6f).target(0f).setCallback((b)->{sprites.remove(((Animation)b.getTween()).getObject());}).start(JOsuClient.getClient().getAnimationManager());
		sprites.add(sprite);
	}
	
	float upd = 1;
	
	public void updateDraw(SpriteBatch batch, float delta){
		boolean drw = batch.isDrawing();
		if(!drw)
			batch.begin();
		
		if((upd+=delta) >= 1f/120){
			current.set(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY());
			current.set(current.x, current.y);
			if(current.x > 0 && current.x < Gdx.graphics.getWidth() && current.y > 0 && current.y < Gdx.graphics.getHeight()){
				if(!focus){
					Gdx.input.setCursorImage(blankCursor, 0, 0);
					focus = true;
				}
			} else {
				if(focus){
					Gdx.input.setCursorImage(null, 0, 0);
					focus = false;
				}
			}
			
			createPoint((int)current.x, (int)current.y);
			
			/*for(float i=1;i<=6;calculatePoint(tmp, old, cur, i++/6)){
				createPoint((int)tmp.x, (int)tmp.y);
			}*/
			
			old.set(current.x,current.y);
			
			cursorOrigin.setCenter(current.x, current.y);
			cursorOrigin.setPosition(MathUtils.floor(cursorOrigin.getX()), MathUtils.floor(cursorOrigin.getY()));
			upd=0;
		}
		
		for(Sprite sprite : sprites){
			sprite.draw(batch);
		}
		
		cursorOrigin.draw(batch);
		
		if(!drw)
			batch.end();
	}
	
	public void down(){
		trailOrigin.setScale(1.2f);
		cursorOrigin.setScale(1.2f);
	}
	
	public void up(){
		trailOrigin.setScale(1f);
		cursorOrigin.setScale(1f);
	}
	
	@SuppressWarnings("unused")
	private Vector2 calculatePoint(Vector2 cal, Vector2 point1, Vector2 point2, float dist){

		float vx = point2.x - point1.x; // x vector
		float vy = point2.y - point1.y; // y vector
		float mag = point1.dst(point2); // length

		vx /= mag;
		vy /= mag;
		
		float distance = mag*dist;
		cal.set(point2.x + vx * distance, point2.y + vy * distance);
		return cal;
	}
	
}
