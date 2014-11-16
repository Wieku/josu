package pl.tinlink.josu.animation;

import pl.tinlink.josu.animation.api.AnimationEquation;

public class AnimationEquations {

	protected static final float back_s = 1.70158f;
	protected static float elastic_a;
    protected static float elastic_p;
    protected static boolean elastic_setA = false;
    protected static boolean elastic_setP = false;
	
	/*
	 * Back equations
	 */
	
	public static AnimationEquation easeInBack = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			float s = back_s;
			return t*t*((s+1)*t - s);
			}
	};

	public static AnimationEquation easeOutBack = new AnimationEquation()  {
		@Override
		public final float compute(float t) {
			float s = back_s;
			return (t-=1)*t*((s+1)*t + s) + 1;
		}
	};

	public static AnimationEquation easeInOutBack = new AnimationEquation()  {
		@Override
		public final float compute(float t) {
			float s = back_s;
			if ((t*=2) < 1) return 0.5f*(t*t*(((s*=(1.525f))+1)*t - s));
			return 0.5f*((t-=2)*t*(((s*=(1.525f))+1)*t + s) + 2);
		}
	};

	/*
	 * Bounce equations
	 */
	
	public static AnimationEquation easeInBounce = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			return 1 - easeOutBounce.compute(1-t);
		}
	};

	public static AnimationEquation easeOutBounce = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			if (t < (1/2.75)) {
				return 7.5625f*t*t;
			} else if (t < (2/2.75)) {
				return 7.5625f*(t-=(1.5f/2.75f))*t + .75f;
			} else if (t < (2.5/2.75)) {
				return 7.5625f*(t-=(2.25f/2.75f))*t + .9375f;
			} else {
				return 7.5625f*(t-=(2.625f/2.75f))*t + .984375f;
			}
		}
	};

	public static AnimationEquation easeInOutBounce = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			if (t < 0.5f) return easeInBounce.compute(t*2) * .5f;
			else return easeOutBounce.compute(t*2-1) * .5f + 0.5f;
		}
	};
	
	/*
	 * Circ equations
	 */
	
	public static AnimationEquation easeInCirc = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			return (float) -Math.sqrt(1 - t*t) + 1;
		}
	};
	
	public static AnimationEquation easeOutCirc = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			return (float) Math.sqrt(1 - (t-=1)*t);
		}
	};
	
	public static AnimationEquation easeInOutCirc = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			if ((t*=2) < 1) return -0.5f * ((float)Math.sqrt(1 - t*t) - 1);
            return 0.5f * ((float)Math.sqrt(1 - (t-=2)*t) + 1);
		}
	};
	
	/*
	 * Cubic equations
	 */
	
	public static AnimationEquation easeInCubic = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			return t*t*t;
		}
	};
	
	public static AnimationEquation easeOutCubic = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			return (t-=1)*t*t + 1;
		}
	};
	
	public static AnimationEquation easeInOutCubic = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			if ((t*=2) < 1) return 0.5f*t*t*t;
			return 0.5f * ((t-=2)*t*t + 2);
		}
	};
	
	/*
	 * Elastic equations
	 */
	
	public static AnimationEquation easeInElastic = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			float a = elastic_a;
			float p = elastic_p;
			if (t==0) return 0;  if (t==1) return 1; if (!elastic_setP) p=.3f;
			float s;
			if (!elastic_setA || a < 1) { a=1; s=p/4; }
			else s = p/(2*(float)Math.PI) * (float)Math.asin(1/a);
			return -(a*(float)Math.pow(2,10*(t-=1)) * (float)Math.sin( (t-s)*(2*Math.PI)/p ));
		}
	};
	
	public static AnimationEquation easeOutElastic = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			float a = elastic_a;
			float p = elastic_p;
			if (t==0) return 0;  if (t==1) return 1; if (!elastic_setP) p=.3f;
			float s;
			if (!elastic_setA || a < 1) { a=1; s=p/4; }
			else s = p/(2*(float)Math.PI) * (float)Math.asin(1/a);
			return a*(float)Math.pow(2,-10*t) * (float)Math.sin( (t-s)*(2*Math.PI)/p ) + 1;
		}
	};
	
	public static AnimationEquation easeInOutElastic = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			float a = elastic_a;
			float p = elastic_p;
			if (t==0) return 0;  if ((t*=2)==2) return 1; if (!elastic_setP) p=.3f*1.5f;
			float s;
			if (!elastic_setA || a < 1) { a=1; s=p/4; }
			else s = p/(2*(float)Math.PI) * (float)Math.asin(1/a);
			if (t < 1) return -.5f*(a*(float)Math.pow(2,10*(t-=1)) * (float)Math.sin( (t-s)*(2*Math.PI)/p ));
			return a*(float)Math.pow(2,-10*(t-=1)) * (float)Math.sin( (t-s)*(2*Math.PI)/p )*.5f + 1;
		}
	};
	
	/*
	 * Expo equations
	 */
	
	public static AnimationEquation easeInExpo = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			return (t==0) ? 0 : (float) Math.pow(2, 10 * (t - 1));
		}
	};
	
	public static AnimationEquation easeOutExpo = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			return (t==1) ? 1 : -(float) Math.pow(2, -10 * t) + 1;
		}
	};
	
	public static AnimationEquation easeInOutExpo = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			if (t==0) return 0;
			if (t==1) return 1;
			if ((t*=2) < 1) return 0.5f * (float) Math.pow(2, 10 * (t - 1));
			return 0.5f * (-(float)Math.pow(2, -10 * --t) + 2);
		}
	};
	
	/*
	 * Quad equations
	 */
	
	public static AnimationEquation easeInQuad = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			return t*t;
		}
	};
	
	public static AnimationEquation easeOutQuad = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			return -t*(t-2);
		}
	};
	
	public static AnimationEquation easeInOutQuad = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			if ((t*=2) < 1) return 0.5f*t*t;
			return -0.5f * ((--t)*(t-2) - 1);
		}
	};
	
	/*
	 * Quart equations
	 */
	
	public static AnimationEquation easeInQuart = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			return t*t*t*t;
		}
	};
	
	public static AnimationEquation easeOutQuart = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			return -((t-=1)*t*t*t - 1);
		}
	};
	
	public static AnimationEquation easeInOutQuart = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			if ((t*=2) < 1) return 0.5f*t*t*t*t;
			return -0.5f * ((t-=2)*t*t*t - 2);
		}
	};
	
	/*
	 * Quint equations
	 */
	
	public static AnimationEquation easeInQuint = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			return t*t*t*t*t;
		}
	};
	
	public static AnimationEquation easeOutQuint = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			return (t-=1)*t*t*t*t + 1;
		}
	};
	
	public static AnimationEquation easeInOutQuint = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			if ((t*=2) < 1) return 0.5f*t*t*t*t*t;
			return 0.5f*((t-=2)*t*t*t*t + 2);
		}
	};
	
	/*
	 * Sine equations
	 */
	
	public static AnimationEquation easeInSine = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			return (float) -Math.cos(t * (Math.PI/2)) + 1;
		}
	};
	
	public static AnimationEquation easeOutSine = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			return (float) Math.sin(t * (Math.PI/2));
		}
	};
	
	public static AnimationEquation easeInOutSine = new AnimationEquation() {
		@Override
		public final float compute(float t) {
			return -0.5f * ((float) Math.cos(Math.PI*t) - 1);
		}
	};
	
    /*
	 * Linear equations
	 */
    
	   public static AnimationEquation easeLinear = new AnimationEquation(){
			@Override
			public float compute(float t) {
				return t;
			}
	   };
	

	public static enum Equations{
		Linear(easeLinear),
		InBack(easeInBack),
		OutBack(easeOutBack),
		InOutBack(easeInOutBack),
		InBounce(easeInBounce),
		OutBounce(easeOutBounce),
		InOutBounce(easeInOutBounce),
		InCirc(easeInCirc),
		OutCirc(easeOutCirc),
		InOutCirc(easeInOutCirc),
		InCubic(easeInCubic),
		OutCubic(easeOutCubic),
		InOutCubic(easeInOutCubic),
		InElastic(easeInElastic),
		OutElastic(easeOutElastic),
		InOutElastic(easeInOutElastic),
		InExpo(easeInExpo),
		OutExpo(easeOutExpo),
		InOutExpo(easeInOutExpo),
		InQuad(easeInQuad),
		OutQuad(easeOutQuad),
		InOutQuad(easeInOutQuad),
		InQuart(easeInQuart),
		OutQuart(easeOutQuart),
		InOutQuart(easeInOutQuart),
		InQuint(easeInQuint),
		OutQuint(easeOutQuint),
		InOutQuint(easeInOutQuint),
		InSine(easeInSine),
		OutSine(easeOutSine),
		InOutSine(easeInOutSine);
		
		Equations(AnimationEquation e){
			eq = e;
		}
		
		AnimationEquation eq;
		
		public AnimationEquation getEquation(){
			return eq;
		}
	}
	
}
