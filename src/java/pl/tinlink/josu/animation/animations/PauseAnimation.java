package pl.tinlink.josu.animation.animations;

import pl.tinlink.josu.animation.AnimationManager;
import pl.tinlink.josu.animation.api.AnimationBase;
import pl.tinlink.josu.animation.api.AnimationCallback;

public class PauseAnimation implements AnimationBase<PauseAnimation> {

	private float elapsed = 0.0f;
	private float duration = 0.0f;
	private boolean ended = false;
	private boolean finished = false;
	private AnimationCallback callback;
	
	
	public PauseAnimation(float time){
		duration = time;
	}
	
	@Override
	public PauseAnimation getTween() {
		return this;
	}
	
	@Override
	public void start(AnimationManager manager) {
		if(manager != null){
			manager.getTweens().add(this);
		}
	}

	@Override
	public void update(float delta) {
		
		elapsed += delta;
		
		if(elapsed >= duration){
			finished = true;
			ended = true;
			
		}
		
	}

	@Override
	public AnimationCallback getCallback(){
		return callback;
	}
	
	@Override
	public PauseAnimation setCallback(AnimationCallback callback){
		this.callback = callback;
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
	public void kill() {
		finished = true;
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	
	
	
}
