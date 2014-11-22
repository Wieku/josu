package pl.tinlink.josu.animation;

import java.util.ArrayList;
import java.util.List;

import pl.tinlink.josu.animation.api.AnimationBase;

public class AnimationManager {

	List<AnimationBase<?>> tweens;
	
	public AnimationManager(){
		tweens = new ArrayList<AnimationBase<?>>();
	}
	
	public void update(float delta){
		
		for(int i=0;i<tweens.size();i++){
			AnimationBase<?> tween = tweens.get(i);
			if(tween.isFinished()){
				if(tween.hasEnded()){
					if(tween.getCallback() != null){
						tween.getCallback().onEvent(tween);
					}
				}
				tween.dispose();
				tweens.remove(tween);
				--i;
			} else {
				tween.update(delta);
			}
		}
	}
	
	public List<AnimationBase<?>> getTweens(){
		return tweens;
	}
	
	public void dispose(){
		tweens.clear();
	}
	
}
