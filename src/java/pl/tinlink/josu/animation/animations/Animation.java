package pl.tinlink.josu.animation.animations;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
import java.util.logging.Logger;

import pl.tinlink.josu.animation.AnimationEquations;
import pl.tinlink.josu.animation.AnimationManager;
import pl.tinlink.josu.animation.api.AnimationAccessor;
import pl.tinlink.josu.animation.api.AnimationBase;
import pl.tinlink.josu.animation.api.AnimationCallback;
import pl.tinlink.josu.animation.api.AnimationEquation;

import com.google.common.base.Preconditions;


public class Animation implements AnimationBase<Animation>{
	
	private static class AnimationPool{
		
		static LinkedList<Animation> pool = new LinkedList<Animation>();
		
		public static Animation getObjectPool(Object element, int type, float duration){
			
				
			Preconditions.checkNotNull(element, "Element cannot be null!");
			//this.element = element;
			//this.duration = duration;
			//this.type = type;
			//accessor = (AnimationAccessor<Object>) findAccessor(element);
			
			return null;
		}
		
		
	}
	
	
	AnimationCallback callBack = null;
	
	private boolean started = false;
	private boolean finished = false;
	
	private float duration = 0.0f;
	private float elapsed = 0.0f;
	private int type = 0;
	private Object element;
	private float delay = 0.0f;
	private AnimationAccessor<Object> accessor;
	private AnimationEquation equation = AnimationEquations.easeLinear;
	private float[] targets;
	private float[] starts;
	private float[] values;
	private float[] lengths;
	private boolean ended = false;
	private static float[] buffer = new float[1000];
	
	private static HashMap<Class<?>, AnimationAccessor<?>> accessors = new HashMap<Class<?>, AnimationAccessor<?>>();
	
	private static Logger log = Logger.getLogger("AnimationEngine");
	
	public static void addAccessor(Class<?> element, AnimationAccessor<?> accessor){
		accessors.put(element, accessor);
		log.info("Added accessor: " + accessor.getClass().getName());
	}

	@SuppressWarnings("unchecked")
	public Animation(Object element, int type, float duration){
		Preconditions.checkNotNull(element, "Element cannot be null!");
		this.element = element;
		this.duration = duration;
		this.type = type;
		accessor = (AnimationAccessor<Object>) findAccessor(element);
	}
	
	@Override
	public Animation getTween(){
		return this;
	}
	
	/**
	 * Sets delay for animation (default 0.0f)
	 * @param delay
	 * @return {@link Animation}
	 */
	public Animation delay(float delay){
		this.delay = delay;
		return this;
	}
	
	/**
	 * Sets {@link AnimationEquation} for animation (default TweenEquations.easeLinear)
	 * @return {@link Animation}
	 */
	public Animation ease(AnimationEquation eq){
		equation = eq;
		return this;
	}
	
	/**
	 * Finds {@link AnimationAccessor} for given element class
	 * @return {@link AnimationAccessor}
	 */
	private AnimationAccessor<?> findAccessor(Object element) {
		if (accessors.containsKey(element.getClass())) return accessors.get(element.getClass());

		Class<?> parentClass = element.getClass();
		do {
			parentClass = parentClass.getSuperclass();
		} while(parentClass != null && !accessors.containsKey(parentClass));
		
		return accessors.get(parentClass);
	}
	
	/**
	 * Returns TweenAccessor for Tween element class
	 * @return {@link AnimationAccessor}
	 */
	public AnimationAccessor<Object> getAccessor(){
		return accessor;
	}
	
	/**
	 * Returns Callback for animation (default null)
	 * @return {@link AnimationCallback}
	 */
	@Override
	public AnimationCallback getCallback(){
		return callBack;
	}
	
	/**
	 * Returns Easing for animation (default TweenEquation.easeLinear)
	 * @return {@link AnimationEquation}
	 */
	private AnimationEquation getEquation() {
		return equation;
	}
	
	/**
	 * Checks if animation is ended
	 * @return {@link Boolean}
	 */
	@Override
	public boolean hasEnded(){
		return ended;
	}
	
	/**
	 * Checks if animation is killed or ended
	 * @return {@link Boolean}
	 */
	@Override
	public boolean isFinished(){
		return finished;
	}
	
	/**
	 * Checks if method <i>Tween.start(TweenManager man)</i> is called 
	 * @return {@link Boolean}
	 */
	public boolean isStarted(){
		return started;
	}
	
	/**
	 * Kills animation but Callback (if it's set) won't called
	 */
	@Override
	public void kill(){
		finished = true;
	}
	
	/**
	 * Sets Callback for animation
	 * @param cl
	 * @return {@link Animation}
	 */
	@Override
	public Animation setCallback(AnimationCallback cl){
		callBack = cl;
		return this;
	}

	/**
	 * Creates arrays for type and adds animation to given TweenManager
	 * @param man
	 */
	@Override
	public void start(AnimationManager man){
		
		int i = getAccessor().getValues(element, type, buffer);
		
		if(i < 0){
			throw new RuntimeException("Given animationType doesn't exist!");
		}
		
		starts = new float[i];
		values = new float[i];
		lengths = new float[i];
		
		for(int j=0;j<i;j++){
			
			float buf = buffer[j];
			
			starts[j] = buf;
			values[j] = buf;
			lengths[j] = targets[j] - buf;
			
		}
		
		
		if(man != null){
			man.getTweens().add(this);
		}
		started = true;
	}
	
	/**
	 * Sets target for type
	 * @param target
	 * @return {@link Animation}
	 */
	public Animation target(float... target){
		targets = target;
		return this;
	}
	
	/**
	 * This method is called by <pre>TweenManager.update(float delta)</pre>
	 * @param delta
	 */
	@Override
	public void update(float delta){
		
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
	
	/**
	 * Computes the percentage value for Accessor<br>
	 * &nbspby percentage value of elapsed time
	 * @param delta
	 */
	private void updateElement(float delta){
		elapsed += delta;
		
		if(elapsed > duration)
			elapsed = duration;
		
		
		float percent = elapsed / duration;
		
		float percentStep = getEquation().compute(percent);
		
		for(int j=0;j<values.length;j++){
			
			values[j] = starts[j] + lengths[j] * percentStep;
			
		}
		
		getAccessor().setValues(element, type, values);
		
		if(percent == 1.0f){
			finished = true;
			ended = true;
		}
	}
	
	
	
	@Override
	public void dispose() {
		
		
	}
	
	
}
