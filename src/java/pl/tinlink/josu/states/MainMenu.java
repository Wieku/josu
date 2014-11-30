package pl.tinlink.josu.states;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import pl.tinlink.josu.JOsuClient;
import pl.tinlink.josu.animation.accessors.ActorAccessor;
import pl.tinlink.josu.animation.timeline.Timeline;
import pl.tinlink.josu.api.State;
import pl.tinlink.josu.map.BeatMapMetaData;
import pl.tinlink.josu.resources.FileUtils;
import pl.tinlink.josu.sound.BeatListener;
import pl.tinlink.josu.sound.MenuPlaylist;
import pl.tinlink.josu.utils.GUIHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenu implements State, BeatListener {

	public static MainMenu instance = new MainMenu();
	
	private Stage stage;
	private Image image;
	private Image background;
	private Table table;
	private Stack stack;
	private Table audioControl;
	private Label statistics;
	private Label song;
	private boolean show = true;
	private float delay = 5;
	private Slider position;
	private boolean dragging;
	private boolean alwaysShow;
	private long startTime;
	private DateFormat format = new SimpleDateFormat("HH:mm");
	private DateFormat format1 = new SimpleDateFormat("HH:mm:ss");
	private float menuDelay = -1;
	private float menuWidth;
	private ImageButton[] i;
	private int currentId = -1;
	private ImageButton image2;
	private Timeline tim2;
	
	private MainMenu(){
		stage = new Stage(new ScreenViewport());
		
		format1.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		format.setTimeZone(TimeZone.getDefault());
		
		audioControl = /*GUIHelper.getTable(new Color(0,0,0,0.2f));*/new Table();
		
		i = new ImageButton[6];
		
		audioControl.add(i[0] = new ImageButton(GUIHelper.getImageButtonStyle(new Texture(Gdx.files.internal("assets/forwardl.png"))))).size(32, 32);
		audioControl.add(i[1] = new ImageButton(GUIHelper.getImageButtonStyle(new Texture(Gdx.files.internal("assets/play.png"))))).size(32, 32);
		audioControl.add(i[2] = new ImageButton(GUIHelper.getImageButtonStyle(new Texture(Gdx.files.internal("assets/pause.png"))))).size(32, 32);
		audioControl.add(i[3] = new ImageButton(GUIHelper.getImageButtonStyle(new Texture(Gdx.files.internal("assets/stop.png"))))).size(32, 32);
		audioControl.add(i[4] = new ImageButton(GUIHelper.getImageButtonStyle(new Texture(Gdx.files.internal("assets/forwardr.png"))))).size(32, 32);
		audioControl.add(i[5] = new ImageButton(GUIHelper.getImageButtonStyle(new Texture(Gdx.files.internal("assets/extend.png"))))).size(32, 32).row();
		
		position = new Slider(0, 1, 1, false, GUIHelper.getProgressSliderStyle(new Color(0.5f, 0.5f, 0.5f, 0.5f), new Color(1f, 1f, 1f, 0.9f), 6));
		
		position.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				event.stop();
				return false;
			}
		});
		
		position.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent arg0, Actor arg1) {
				if(!position.isDragging()){
					if(dragging)
						MenuPlaylist.setPosition((int) (position.getValue()*1000));
					dragging=false;
				} else {
					dragging=true;
				}
			}
		});
		
		audioControl.add(position).colspan(6).fillX();
		
		ClickListener lis = new ClickListener(Buttons.LEFT){
			
			public void clicked(InputEvent event, float x, float y) {
				Image target = (Image) event.getTarget();
				showInfo();
				
				if(target.equals(i[0].getImage())){
					MenuPlaylist.previousSong();
				} else if(target.equals(i[1].getImage())){
					MenuPlaylist.play();
				} else if(target.equals(i[2].getImage())){
					MenuPlaylist.pause();
				} else if(target.equals(i[3].getImage())){
					MenuPlaylist.stop();
				} else if(target.equals(i[4].getImage())){
					MenuPlaylist.nextSong();
				} else if(target.equals(i[5].getImage())){
					alwaysShow = !alwaysShow;
					delay = 0;
				}
				
			}
		};
		
		for(ImageButton b : i){
			b.addListener(lis);
		}
		
		audioControl.pack();
		
		image = new Image(new Texture(Gdx.files.internal("assets/josu.png")));
		image.setScaling(Scaling.fit);
		image2 = new ImageButton(GUIHelper.getImageButtonStyle(new Texture(Gdx.files.internal("assets/josu.png"))));
		image2.setColor(1, 1, 1, 0.5f);
		image2.getImage().setScaling(Scaling.fit);
		
		image2.addListener(new ClickListener(Buttons.LEFT){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(new Vector2(x,y).dst(image2.getX()+image2.getWidth()/2, image2.getY()+image2.getHeight()/2) <= image2.getWidth()/2){
					menuDelay = 10;
					new Timeline().beginParallel().push(ActorAccessor.createSineTween(table, ActorAccessor.SIZEX, 0.5f, menuWidth, 0f))
					.push(ActorAccessor.createSineTween(table, ActorAccessor.FADE, 0.3f, 1.0f, 0.f)).end().start(JOsuClient.getClient().getAnimationManager());
					statistics.setVisible(true);
				}
			}
		});
		
		song = GUIHelper.text("Atama no taisou!", new Color(0,0,0,0.2f), Color.WHITE, 12);
		
		background = new Image();
		background.setScaling(Scaling.fit);
		
		stage.addActor(background);
		
		stage.addActor(song = GUIHelper.text("", new Color(0,0,0,0.2f), Color.WHITE, 12));
		stage.addActor(statistics = GUIHelper.text("", new Color(0,0,0,0.2f), Color.WHITE, 12));
		statistics.setVisible(false);
		stack = new Stack();
		stack.add(image);
		stack.add(image2);
		
		table = new Table();
		
		table.add(new ImageButton(GUIHelper.getImageButtonStyle(new Texture(Gdx.files.internal("assets/buttonPlaySolo.png"))))).pad(10).width(400).height(75).padLeft(0).fillX().expandX().row();
		table.add(new ImageButton(GUIHelper.getImageButtonStyle(new Texture(Gdx.files.internal("assets/buttonOptions.png"))))).pad(10).width(400).height(75).padLeft(20).fillX().row();
		table.add(new ImageButton(GUIHelper.getImageButtonStyle(new Texture(Gdx.files.internal("assets/buttonExit.png"))))).pad(10).width(400).height(75).padLeft(0).fillX();
		
		table.pack();
		
		menuWidth = table.getWidth();
		//menu.pack();
		stage.addActor(table);
		stage.addActor(stack);
		
		stage.addActor(audioControl);
		
		stage.getRoot().setColor(1, 1, 1, 0.0f);
		stage.addListener(new InputListener(){
			@Override
			public boolean mouseMoved(InputEvent event, float x, float y) {
				
				for(ImageButton b : i){
					Image target = b.getImage();
					if(b.isOver()){
						float sizeX = target.getWidth()*target.getScaleX();
						float sizeY = target.getHeight()*target.getScaleY();
						target.setScale(1.2f);
						target.setPosition(target.getX() - (target.getWidth()*target.getScaleX()-sizeX)/2, target.getY() - (target.getHeight()*target.getScaleY()-sizeY)/2);
					} else {
						float sizeX = target.getWidth()*target.getScaleX();
						float sizeY = target.getHeight()*target.getScaleY();
						target.setScale(1.0f);
						target.setPosition(target.getX() - (target.getWidth()*target.getScaleX()-sizeX)/2, target.getY() - (target.getHeight()*target.getScaleY()-sizeY)/2);
					}
				}
				return super.mouseMoved(event, x, y);
			}
		});
		startTime = System.currentTimeMillis();
		
		table.setWidth(0);
		table.setColor(1, 1, 1, 0);
	}
	
	Timeline tim;
	
	float dl=1;
	float delta2=0;
	
	@Override
	public void render(float delta) {
		
		MenuPlaylist.update();
		
		stack.setPosition((Gdx.graphics.getWidth() - stack.getWidth()-table.getWidth())/2, (Gdx.graphics.getHeight()-0.7f * Gdx.graphics.getHeight())/2);
		table.setX(stack.getX()+stack.getWidth()*0.80f);
		
		if(menuDelay > -1 && (menuDelay-=delta) <= 0.0f){
			statistics.setVisible(false);
			new Timeline().beginParallel().push(ActorAccessor.createSineTween(table, ActorAccessor.SIZEX, 0.5f, 0, 0f))
			.push(ActorAccessor.createSineTween(table, ActorAccessor.FADE, 0.3f, 0.0f, 0.f)).end().start(JOsuClient.getClient().getAnimationManager());
			menuDelay= -1;
		}
		
		if(!alwaysShow && delay > -1 && (delay-=delta) <= 0.0f){
			new Timeline().beginParallel().push(ActorAccessor.createSineTween(song, ActorAccessor.SLIDEY, 0.5f, Gdx.graphics.getHeight(), 0))
			.push(ActorAccessor.createSineTween(song, ActorAccessor.FADE, 0.5f, 0, 0)).end().setCallback((b)->{show=false;}).start(JOsuClient.getClient().getAnimationManager());
			delay = -1;
		}
		
		if((dl+=delta) >=1){
			BeatMapMetaData data = MenuPlaylist.getCurrent().getMetaData();
			
			position.setRange(0, MenuPlaylist.getLength()/1000);
			if(!position.isDragging()){
				position.setRange(0, MenuPlaylist.getLength()/1000);
				position.setValue(MenuPlaylist.getPosition()/1000);
			}
			
			statistics.setText(JOsuClient.getClient().beatmaps.size() + " beatmaps available!\n"
			+"JOsu! was running for " + format1.format(new Date(System.currentTimeMillis()-startTime)) +"\n"
			+"It's currently " + format.format(new Date()));
			statistics.pack();
			
			song.setText(data.getArtist() + " - " + data.getTitle());
			song.pack();
			
			if(currentId != MenuPlaylist.getCurrentId()){
				currentId = MenuPlaylist.getCurrentId();
				MenuPlaylist.getCurrentPlayer().setBeatListener(this);
				loadBG();
			}
			
			dl=0;
		}
			
		statistics.setPosition(0, Gdx.graphics.getHeight()-statistics.getHeight());
		song.setX(Gdx.graphics.getWidth()-song.getWidth());
		audioControl.setY(song.getY()-audioControl.getHeight());
		stage.act(delta);
		stage.draw();
		
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		if(show){
			song.setPosition(width-song.getWidth(), height-song.getHeight());
		} else {
			song.setPosition(width-song.getWidth(), height);
		}
		audioControl.setPosition(width-audioControl.getWidth()-10, song.getY()-audioControl.getHeight());

		background.setSize(width, height);
		
		stack.setSize(0.7f * height, 0.7f * height);
		table.setHeight(0.7f * height);
		table.setY((height-table.getHeight())/2);
		
	}
	
	@Override
	public void onEnter() {
		delay = 5;
		Gdx.input.setInputProcessor(stage);
		ActorAccessor.createSineTween(stage.getRoot(), ActorAccessor.FADE, 2.f, 1.0f, 2).start(JOsuClient.getClient().getAnimationManager());
		MenuPlaylist.start();
	}

	public void showInfo(){
		delay = 6;
		Timeline timeline = new Timeline();
		timeline.beginParallel().push(ActorAccessor.createSineTween(song, ActorAccessor.SLIDEY, 0.5f, Gdx.graphics.getHeight()-song.getHeight(), 0))
		.push(ActorAccessor.createSineTween(song, ActorAccessor.FADE, 0.5f, 1, 0)).end().setCallback((b)->{show=true;}).start(JOsuClient.getClient().getAnimationManager());
	}
	
	
	@Override
	public void onEscape() {

		ActorAccessor.createSineTween(stage.getRoot(), ActorAccessor.FADE, 0.5f, 0.0f, 0).start(JOsuClient.getClient().getAnimationManager());
		
	}

	public void loadBG(){
		
		background.setDrawable(null);
		BeatMapMetaData data = MenuPlaylist.getCurrent().getMetaData();
		if(data.getBackgroundName() != null){
			System.out.println("Loading: " + data.getBackgroundName());
			
			FileHandle handle = FileUtils.getFile(data.getBackgroundName());
			
			if(handle.exists())
				background.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(handle))));

		}
		
	}
	
	@Override
	public void onBeatLow() {
		if(tim2 != null && !tim2.hasEnded()) tim2.kill();
		
		tim2 = new Timeline().beginSequence().push(ActorAccessor.createSineTween(image2.getImage(), ActorAccessor.SIZEC, 0.1f*(1.025f/image.getScaleX()), 1.025f, 0))
				.push(ActorAccessor.createSineTween(image2.getImage(), ActorAccessor.SIZEC, 0.3f, 1f, 0)).end();
		tim2.start(JOsuClient.getClient().getAnimationManager());
		
	}
	
	@Override
	public void onBeatHigh() {
		if(tim != null && !tim.hasEnded()) tim.kill();
		
		tim = new Timeline().beginSequence().push(ActorAccessor.createSineTween(image, ActorAccessor.SIZEC, 0.04f*(0.96f/image.getScaleX()), 0.96f, 0))
				.push(ActorAccessor.createSineTween(image, ActorAccessor.SIZEC, 0.3f, 1f, 0)).end();
		tim.start(JOsuClient.getClient().getAnimationManager());
		
	}
	
}