package pl.tinlink.josu.map.algs;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class CircularCurve {

	public Vector2 point1, point2, point3, center;
	
	private float angleDifference, startAngle, radius;
	
	
	public CircularCurve(Vector2[] points){
		this(points[0], points[1], points[2]);
	}
	
	public CircularCurve(Vector2 point1, Vector2 point2, Vector2 point3){

		float aSlope = (point2.y - point1.y) / (point2.x - point1.x);
		float bSlope = (point3.y - point2.y) / (point3.x - point2.x);  
		
		float x;
		center = new Vector2(x = (aSlope*bSlope*(point1.y - point3.y) + bSlope*(point1.x + point2.x) - aSlope*(point2.x+point3.x) )/(2* (bSlope-aSlope) ),
				-1*(x - (point1.x+point2.x)/2)/aSlope +  (point1.y+point2.y)/2);
		
		radius = center.dst(point2);
		
		startAngle = point1.cpy().sub(center).angle();
		angleDifference = getAngle(point2, point1) + getAngle(point2, point3);

		if(((point2.x-point1.x)*(point2.y+point1.y)) + ((point3.x-point2.x)*(point3.y+point2.y)) + ((point1.x-point3.x)*(point1.y+point3.y))<0)
			angleDifference = -angleDifference;
		
	}
	
	private float getAngle(Vector2 point1, Vector2 point2){
		
		float a = center.dst(point1),
		b = center.dst(point2),
		c = point1.dst(point2);

	    return (float) Math.toDegrees(Math.acos((a*a + b*b - c*c)/(2*a*b)));
	}
	
	
	public Vector2 get(float atLength){
		atLength = (atLength<0?0:(atLength>1?1:atLength));
		return new Vector2(center.x + radius * MathUtils.cosDeg(startAngle-angleDifference*atLength), center.y + radius * MathUtils.sinDeg(startAngle-angleDifference*atLength));
	}
	
}
