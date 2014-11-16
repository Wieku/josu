package pl.tinlink.josu.animation.accessors;

import pl.tinlink.josu.animation.api.AnimationAccessor;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteAccessor implements AnimationAccessor<Sprite> {

	public static final int FADEIN = 0;
	public static final int FADEOUT = 1;
	
	@Override
	public int getValues(Sprite target, int tweenType, float[] returnValues) {
		switch(tweenType){
		 case FADEIN:
		 case FADEOUT:
			 returnValues[0] = target.getColor().a;
			 return 1;
		 default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(Sprite target, int tweenType, float[] newValues) {
		switch(tweenType){
		 case FADEIN:
		 case FADEOUT:
			 target.setColor(target.getColor().r, target.getColor().g, target.getColor().b, newValues[0]);
			 return;
		 default:
			assert false;
			return;
		}
		
	}

}
