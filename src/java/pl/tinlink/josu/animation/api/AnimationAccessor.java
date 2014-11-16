package pl.tinlink.josu.animation.api;

public interface AnimationAccessor<T> {

	public int getValues(T element, int tweenType, float[] values);
	public void setValues(T element, int tweenType, float[] newValues);
	
}
