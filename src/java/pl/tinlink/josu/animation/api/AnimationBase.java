package pl.tinlink.josu.animation.api;

import pl.tinlink.josu.animation.AnimationManager;

public interface AnimationBase<T> {
	
	public T getTween();
	public void start(AnimationManager manager);
	public void update(float delta);
	public boolean hasEnded();
	public boolean isFinished();
	public AnimationCallback getCallback();
	public T setCallback(AnimationCallback callback);
	public void kill();
	public void dispose();
	
}