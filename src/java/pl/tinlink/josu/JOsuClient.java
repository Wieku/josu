package pl.tinlink.josu;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.ArrayList;

import pl.tinlink.josu.animation.AnimationManager;
import pl.tinlink.josu.animation.accessors.ActorAccessor;
import pl.tinlink.josu.animation.accessors.CellAccessor;
import pl.tinlink.josu.animation.animations.Animation;
import pl.tinlink.josu.api.State;
import pl.tinlink.josu.map.BeatMap;
import pl.tinlink.josu.map.BeatMapMetaData;
import pl.tinlink.josu.sound.MenuPlaylist;
import pl.tinlink.josu.states.MainMenu;
import pl.tinlink.josu.utils.FileUtils;
import pl.tinlink.josu.utils.GUIHelper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.BSpline;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import ddf.minim.Minim;

public class JOsuClient extends Game{

	boolean ne;
	int currentbyte = 0;
	long size =0;
	private static JOsuClient client = new JOsuClient();
	State state;
	boolean stateChanged;
	public Minim minim;
	BeatMap current;
	Pixmap blankCursor;
	Image cursor;
	Stage stage;
	boolean focus;
	Label fps;
	AnimationManager manager;
	
	Logger logger = new Logger("JOsuClient");
	public ArrayList<BeatMap> beatmaps;
	private JOsuClient(){
		manager = new AnimationManager();
	}
	
	
	@Override
	public void create() {
		minim = new Minim(this);
		Animation.addAccessor(Actor.class, new ActorAccessor());
		Animation.addAccessor(Cell.class, new CellAccessor());
		beatmaps = new ArrayList<BeatMap>();
		stage = new Stage(new ScreenViewport());
		
		for(File folder : new File("/home/wiek/PlayOnLinux's virtual drives/osu_on_linux/drive_c/Program Files/osu!/Songs/").listFiles()){
			
			if(folder.isDirectory()){
				File[] files = folder.listFiles(new FilenameFilter() {
						@Override
						public boolean accept(File dir, String name) {
							return name.toLowerCase().endsWith(".osu");
						}	
					});
			
				for(File file : files){
					BeatMap map = BeatMap.parseBeatmap(file);
					if(map != null){
						beatmaps.add(map);
						
						BeatMapMetaData data = map.getMetaData();
						
						System.out.println("Successfully parsed beatmap: " + data.getArtist() + " - " + data.getTitle() + " [" +data.getVersion()+ "]");
						
					}
				}
			
			}
		}
		fps = GUIHelper.text("", new Color(0,0,0,0.4f), Color.WHITE, 8);
		MenuPlaylist.start();
		
		stage.addActor(fps);
		
		cursor = new Image(new Texture(Gdx.files.internal("assets/skin/cursor.png")));
		stage.addActor(cursor);
		
		blankCursor = new Pixmap(4,4,Format.RGBA8888);
		
		state = MainMenu.instance;
		state.onEnter();
		
	}
	float delta2 = 0f;
	private float delta=1;
	@Override
	public void render() {
		
		MenuPlaylist.update();
		
		if((delta2+=Gdx.graphics.getDeltaTime())> 1f/60){
			int mouseX = Gdx.input.getX();
			int mouseY = Gdx.input.getY();
			
			if(mouseX > 0 && mouseX < Gdx.graphics.getWidth() && mouseY > 0 && mouseY < Gdx.graphics.getHeight()){
				if(!focus){
					Gdx.input.setCursorImage(blankCursor, 0, 0);
					focus = true;
				}
			} else {
				if(focus){
					Gdx.input.setCursorImage(null, 0, 0);
					focus = false;
				}
			}
			
			cursor.setPosition(Math.round(mouseX-(cursor.getWidth()*cursor.getScaleX())/2), Math.round(Gdx.graphics.getHeight()-mouseY-(cursor.getHeight()*cursor.getScaleY())/2));
			delta2=0;
		}
		
		//System.out.println(Gdx.input.getX() + " " + Gdx.input.getY());
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
		
		new Thread(()->{
			if(state != null)
				synchronized(state){
					state.onEscape();
				}
			
			state = newState;
			state.onEnter();
		}).start();
		
	}
	
	public InputStream createInput(String file){
		return Gdx.files.absolute(file).read();
	}
	
}
