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
import pl.tinlink.josu.sound.MenuPlaylist;
import pl.tinlink.josu.utils.GUIHelper;
import pl.tinlink.josu.utils.StateLock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import ddf.minim.ugens.Sampler;

public class MainMenu implements State {

	public static MainMenu instance = new MainMenu();
	
	private Stage stage;
	private Image image;
	private Table menu;
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
	ImageButton[] i;

	private Image image2;
	
	private MainMenu(){
		stage = new Stage(new ScreenViewport());
		
		format1.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		format.setTimeZone(TimeZone.getDefault());
		
		audioControl = GUIHelper.getTable(new Color(0,0,0,0.2f));
		
		i = new ImageButton[6];
		
		audioControl.add(i[0] = new ImageButton(GUIHelper.getImageButtonStyle(new Texture(Gdx.files.internal("assets/forwardl.png"))))).size(32, 32);
		audioControl.add(i[1] = new ImageButton(GUIHelper.getImageButtonStyle(new Texture(Gdx.files.internal("assets/play.png"))))).size(32, 32);
		audioControl.add(i[2] = new ImageButton(GUIHelper.getImageButtonStyle(new Texture(Gdx.files.internal("assets/pause.png"))))).size(32, 32);
		audioControl.add(i[3] = new ImageButton(GUIHelper.getImageButtonStyle(new Texture(Gdx.files.internal("assets/stop.png"))))).size(32, 32);
		audioControl.add(i[4] = new ImageButton(GUIHelper.getImageButtonStyle(new Texture(Gdx.files.internal("assets/forwardr.png"))))).size(32, 32);
		audioControl.add(i[5] = new ImageButton(GUIHelper.getImageButtonStyle(new Texture(Gdx.files.internal("assets/extend.png"))))).size(32, 32).row();
		
		position = new Slider(0, 1, 1, false, GUIHelper.getProgressSliderStyle( new Color(0.5f,0.5f,0.5f,0.7f), Color.WHITE, 6));
		
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
		
		audioControl.add(position).colspan(6);
		
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
		image2 = new Image(new Texture(Gdx.files.internal("assets/josu.png")));
		image2.setColor(1, 1, 1, 0.5f);
		image2.setScaling(Scaling.fit);
		menu = new Table();
		
		menu.center();
		menu.pack();
		
		song = GUIHelper.text("Atama no taisou!", new Color(0,0,0,0.2f), Color.WHITE, 12);
		stage.addActor(song = GUIHelper.text("Atama no taisou!", new Color(0,0,0,0.2f), Color.WHITE, 12));
		stage.addActor(statistics = GUIHelper.text("", new Color(0,0,0,0.2f), Color.WHITE, 12));
		stage.addActor(image);
		stage.addActor(image2);
		stage.addActor(audioControl);
		stage.addActor(menu);
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
		
		image.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		image2.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	Timeline tim;
	Timeline tim2;
	
	float dl=1;
	float delta2=0;
	
	@Override
	public void render(float delta) {
		
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
			dl=0;
		}
		
		if((delta2+=delta)>= 1f/60){
			if(MenuPlaylist.isBeat()){
				if(tim != null && !tim.hasEnded()) tim.kill();
				tim = new Timeline().beginSequence().push(ActorAccessor.createSineTween(image, ActorAccessor.SIZEC, 0.05f*(image.getScaleX()/1.03f), 1.03f, 0))
						.push(ActorAccessor.createSineTween(image, ActorAccessor.SIZEC, 0.05f, 1f, 0)).end();
				
				tim2 = new Timeline().beginSequence().push(ActorAccessor.createSineTween(image2, ActorAccessor.SIZEC, 0.05f*(image.getScaleX()/1.03f), 1.05f, 0))
						.push(ActorAccessor.createSineTween(image2, ActorAccessor.SIZEC, 0.02f, 1f, 0)).end().delay(0.01f);
				
				tim.start(JOsuClient.getClient().getAnimationManager());
				tim2.start(JOsuClient.getClient().getAnimationManager());
				delta2=0;
			}
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
		audioControl.setPosition(width-audioControl.getWidth(), song.getY()-audioControl.getHeight());
		menu.setBounds(0,(Gdx.graphics.getHeight()-0.7f * height)/2,width, 0.7f * height);
		image.setBounds(0,(Gdx.graphics.getHeight()-0.7f * height)/2,width, 0.7f * height);
		image2.setBounds(0,(Gdx.graphics.getHeight()-0.7f * height)/2,width, 0.7f * height);
		//image.setBounds(0, 0, 0.9f*width, 0.9f*height);
	}
	
	@Override
	public void onEnter() {
		delay = 5;
		Gdx.input.setInputProcessor(stage);
	}

	public void showInfo(){
		delay = 6;
		Timeline timeline = new Timeline();
		timeline.beginParallel().push(ActorAccessor.createSineTween(song, ActorAccessor.SLIDEY, 0.5f, Gdx.graphics.getHeight()-song.getHeight(), 0))
		.push(ActorAccessor.createSineTween(song, ActorAccessor.FADE, 0.5f, 1, 0)).end().setCallback((b)->{show=true;}).start(JOsuClient.getClient().getAnimationManager());
	}
	
	
	@Override
	public void onEscape() {

		StateLock lock = new StateLock();
		
		lock.subscribe();
		
	}



}
