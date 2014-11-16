package pl.tinlink.josu.animation.timeline;

import pl.tinlink.josu.animation.AnimationManager;
import pl.tinlink.josu.animation.api.AnimationBase;
import pl.tinlink.josu.animation.api.AnimationCallback;

public class AnimationParallel extends AnimationManager implements AnimationBase<AnimationParallel> {

	private Timeline timeline;
	private boolean finished = false;
	private boolean ended = false;
	private AnimationCallback callback;
	
	public AnimationParallel(Timeline timeline){
		super();
		this.timeline = timeline;
	}
	
	@Override
	public AnimationParallel getTween() {
		return this;
	}

	@Override
	public void start(AnimationManager manager) {
		manager.getTweens().add(this);
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		if(getTweens().size() == 0){
			finished = true;
			ended = true;
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
	public AnimationParallel setCallback(AnimationCallback callback) {
		this.callback = callback;
		return this;
	}

	@Override
	public void kill() {
		finished  = true;
	}

	public AnimationParallel push(AnimationBase<?> tween){
		tween.start(this);
		return this;
	}
	
	public Timeline end(){
		return timeline.push(this);
	}
	
}
