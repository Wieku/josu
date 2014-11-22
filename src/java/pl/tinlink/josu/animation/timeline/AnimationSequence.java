package pl.tinlink.josu.animation.timeline;

import java.util.ArrayList;
import java.util.List;

import pl.tinlink.josu.animation.AnimationManager;
import pl.tinlink.josu.animation.animations.PauseAnimation;
import pl.tinlink.josu.animation.api.AnimationBase;
import pl.tinlink.josu.animation.api.AnimationCallback;

public class AnimationSequence implements AnimationBase<AnimationSequence> {

	private int currentTween = 0;
	private AnimationCallback callback;
	private boolean finished = false;
	private boolean ended = false;
	private Timeline timeline;
	private List<AnimationBase<?>> tweens = new ArrayList<AnimationBase<?>>();
	
	
	public AnimationSequence(Timeline timeline){
		this.timeline = timeline;
	}
	
	@Override
	public AnimationSequence getTween() {
		return this;
	}

	@Override
	public void start(AnimationManager manager) {
		manager.getTweens().add(this);
		if(tweens.size() > 0){
			tweens.get(0).start(null);
		}
	}
	
	@Override
	public void update(float delta) {
		
		if(tweens.size() <= currentTween){
			
			finished = true;
			ended = true;
			tweens.clear();
			return;
		}
		
		AnimationBase<?> tween = tweens.get(currentTween);
		
		if(tween.isFinished()){
			if(tween.hasEnded()){
				if(tween.getCallback() != null){
					tween.getCallback().onEvent(tween);
				}
			}
			
			++currentTween;
			
			if(tweens.size() > currentTween){
				tweens.get(currentTween).start(null);
			}
			
		}else {
			tween.update(delta);
		}
		
	}

	public AnimationSequence push(AnimationBase<?> tween){
		tweens.add(tween);
		return this;
	}
	
	public AnimationSequence pushPause(float time){
		push(new PauseAnimation(time));
		return this;
	}

	@Override
	public boolean hasEnded() {
		return ended;
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

	@Override
	public AnimationCallback getCallback() {
		return callback;
	}

	@Override
	public AnimationSequence setCallback(AnimationCallback callback) {
		this.callback = callback;
		return this;
	}

	@Override
	public void kill() {
		finished = true;
	}
	
	public Timeline end(){
		return timeline.push(this);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}
