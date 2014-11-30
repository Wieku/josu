package pl.tinlink.josu;

import java.io.InputStream;
import java.util.ArrayList;

import pl.tinlink.josu.animation.AnimationManager;
import pl.tinlink.josu.animation.accessors.ActorAccessor;
import pl.tinlink.josu.animation.accessors.CellAccessor;
import pl.tinlink.josu.animation.accessors.SpriteAccessor;
import pl.tinlink.josu.animation.animations.Animation;
import pl.tinlink.josu.api.State;
import pl.tinlink.josu.map.BeatMap;
import pl.tinlink.josu.states.Splash;
import pl.tinlink.josu.utils.CursorRenderer;
import pl.tinlink.josu.utils.GUIHelper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class JOsuClient extends Game{

	boolean ne;
	int currentbyte = 0;
	long size =0;
	private static JOsuClient client = new JOsuClient();
	State state;
	State tempState;
	boolean stateChanged;
	BeatMap current;
	Pixmap blankCursor;
	Stage stage;
	boolean focus;
	Label fps;
	AnimationManager manager;
	
	CursorRenderer cursor;
	
	Logger logger = new Logger("JOsuClient");
	public ArrayList<BeatMap> beatmaps;
	private JOsuClient(){
		manager = new AnimationManager();
	}
	
	
	@Override
	public void create() {
		Animation.addAccessor(Actor.class, new ActorAccessor());
		Animation.addAccessor(Sprite.class, new SpriteAccessor());
		Animation.addAccessor(Cell.class, new CellAccessor());
		beatmaps = new ArrayList<BeatMap>();
		stage = new Stage(new ScreenViewport());
		
		fps = GUIHelper.text("", new Color(0,0,0,0.4f), Color.WHITE, 8);
		
		stage.addActor(fps);
		
		cursor = new CursorRenderer();
		
		state = Splash.instance;
		state.onEnter();
		
	}
	
	private float delta=1;
	@Override
	public void render() {
		
		if(stateChanged){
			state = tempState;
			state.onEnter();
			state.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			stateChanged = false;
		}
		
		if((delta+= Gdx.graphics.getDeltaTime())>=1){
			fps.setText(Gdx.graphics.getFramesPerSecond() + " FPS");
			fps.pack();
			delta=0;
		}
		
		manager.update(Gdx.graphics.getDeltaTime());
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		
		fps.setPosition(Gdx.graphics.getWidth() - fps.getWidth(), 0);
		
		if(state != null)
			state.render(Gdx.graphics.getDeltaTime());
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
		cursor.updateDraw((SpriteBatch) stage.getBatch(), Gdx.graphics.getDeltaTime());
		
	}
	
	public static JOsuClient getClient(){
		return client;
	}


	public AnimationManager getAnimationManager() {
		return manager;
	}

	@Override
	public void resize(int width, int height){
		super.resize(width, height);
		stage.getViewport().update(width, height, true);
		if(state != null)
			state.resize(width, height);
		
	}
	
	@Override
	public void dispose() {
		
		if(state != null) state.onEscape();
		
		
		super.dispose();
		System.out.println("dispose");
		stage.dispose();
		
	}

	public void changeState(State newState){
			if(state != null){
				state.onEscape();
				tempState = newState;
			}
	}
	
	public void completeChange(){
		stateChanged = true;
	}
	
	public InputStream createInput(String file){
		return Gdx.files.absolute(file).read();
	}
	
}
