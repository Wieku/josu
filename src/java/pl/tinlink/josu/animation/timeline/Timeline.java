package pl.tinlink.josu.animation.timeline;

import pl.tinlink.josu.animation.AnimationManager;
import pl.tinlink.josu.animation.animations.PauseAnimation;
import pl.tinlink.josu.animation.api.AnimationBase;
import pl.tinlink.josu.animation.api.AnimationCallback;

public class Timeline implements AnimationBase<Timeline> {

	private AnimationManager manager;
	private AnimationCallback callback;
	private boolean finished = false;
	private boolean ended = false;
	private int currentTween = 0;
	private float delay = 0.0f;
	
	public Timeline(){
		manager = new AnimationManager();
	}
	
	@Override
	public Timeline getTween() {
		return this;
	}

	@Override
	public void start(AnimationManager manager) {
		manager.getTweens().add(this);
	}

	public Timeline delay(float delay){
		this.delay = delay;
		return this;
	}
	
	@Override
	public void update(float delta) {
		
		if(delay == 0){
			
			updateElement(delta);
			
		} else {
			
			if((delay -= delta) < 0){
				
				updateElement(Math.abs(delay));
				delay = 0;
				return;
			}
		}
	}

	private void updateElement(float delta){
		if(manager.getTweens().size() <= currentTween){
			finished = true;
			ended = true;
			manager.dispose();
			return;
		}
		
		AnimationBase<?> tween = manager.getTweens().get(currentTween );
		
		if(tween.isFinished()){
			if(tween.hasEnded()){
				if(tween.getCallback() != null){
					tween.getCallback().onEvent(tween);
				}
			}
			
			++currentTween;
			
		}else {
			tween.update(delta);
		}
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
	public Timeline setCallback(AnimationCallback callback) {
		this.callback = callback;
		return this;
	}

	@Override
	public void kill() {
		finished = true;
	}

	public Timeline push(AnimationParallel parallel){
		parallel.start(manager);
		return this;
	}
	
	public Timeline push(AnimationSequence sequence){
		sequence.start(manager);
		return this;
	}
	
	public Timeline pushPause(float time){
		PauseAnimation pause = new PauseAnimation(time);
		pause.start(manager);
		return this;
	}
	
	public AnimationSequence beginSequence(){
		return new AnimationSequence(this);
	}
	
	public AnimationParallel beginParallel(){
		return new AnimationParallel(this);
	}
	
}
