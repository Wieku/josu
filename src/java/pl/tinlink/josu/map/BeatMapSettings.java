package pl.tinlink.josu.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

public class BeatMapSettings {
	
	public float circleSize = 4f;               // size of circles
	public int calcCircleSize;
	public float overallDifficulty = 5f;        // affects timing window, spinners, and approach speed (0:easy ~ 10:hard)
	
	public int od300;
	public int od100;
	public int od50;
	
	public float approachRate = -1f;   
	public int calcApproachRate;
	
	public float multX;
	public float multY;
	public float minX;
	public float minY=30;
	
	
	public BeatMapSettings(float circleSize, float approachRate, float overallDifficulty, boolean wideScreenStoryBoard){
		calcCircleSize = MathUtils.round(96 - (this.circleSize = circleSize) * 8);
		calcApproachRate = (MathUtils.round(1950 - (this.approachRate = approachRate) * 150));
		this.overallDifficulty = overallDifficulty;
		
		od300 = MathUtils.round(80 - overallDifficulty * 4);
		od100 = MathUtils.round(140 - overallDifficulty * 4);
		od50 = MathUtils.round(200 - overallDifficulty * 4);
		
		float hi = Gdx.graphics.getHeight()-60;
		
		multY = hi/384;
		if(wideScreenStoryBoard){
			multX = (Gdx.graphics.getWidth()-60)/512;
			minX=30;
		} else {
			multX = hi/512;
			minX=(Gdx.graphics.getWidth()-hi)/2;
		}
	}
	
}
