package pl.tinlink.josu.states;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import pl.tinlink.josu.JOsuClient;
import pl.tinlink.josu.animation.accessors.ActorAccessor;
import pl.tinlink.josu.animation.timeline.Timeline;
import pl.tinlink.josu.api.State;
import pl.tinlink.josu.map.BeatMap;
import pl.tinlink.josu.utils.GUIHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Splash implements State{

	public static Splash instance = new Splash();
	private Stage stage;
	private Table loadTable;
	private Label status;
	private ProgressBar bar;
	private boolean ended = false;
	private String text = "";
	private Pixmap map;
	private Texture starsTexture;
	private Image starsImage;
	
	private Splash(){
		
		stage = new Stage(new ScreenViewport());
		
		loadTable = new Table();
		loadTable.setBackground(GUIHelper.getTxRegion(new Color(0.1f,0.1f,0.1f,0.5f)));
		loadTable.top();
		
		
		for(Cell<?> cell : loadTable.getCells()){
			((Actor)cell.getActor()).setVisible(false);
		}
		
		map = new Pixmap(1920, 1080, Format.RGBA8888);
		for(int i=1;i<=400;i++){
			map.setColor(MathUtils.random(198, 255)/(float)255, MathUtils.random(184, 255)/(float)255, 1, 1);
			int s = 0;
			map.fillRectangle(MathUtils.random(map.getWidth()), MathUtils.random(map.getHeight()), s = MathUtils.random(1,4), s);
		}
		
		starsTexture = new Texture(map);
		starsTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);starsImage = new Image(starsTexture);
		starsImage.setVisible(false);
		starsImage.setScaling(Scaling.fill);
		stage.addActor(starsImage);
		
		loadTable.add(new Label("JOsu!", GUIHelper.getLabelStyle(Color.WHITE, 120))).expand().top().padTop(100).row();
		loadTable.add(status = new Label("Parsing...", GUIHelper.getLabelStyle(Color.WHITE, 10))).left().padLeft(10).bottom().row();
		loadTable.add(bar = new ProgressBar(0,100,1,false,GUIHelper.getProgressBarStyle(Color.DARK_GRAY,Color.WHITE,10))).fillX().center().bottom().pad(10).padBottom(50).height(10).colspan(2).row();

		stage.addActor(loadTable);
	}
	
	float delta1 = 1.0f;
	
	@Override
	public void render(float delta) {
		
		if((delta1+=delta) >= 1f/60){
			status.setText(text);
			//status.pack();
			//change  = false;
			delta1 = 0;
			if(ended){
				JOsuClient.getClient().changeState(MainMenu.instance);
				ended = false;
			}
		}
		
		stage.act(delta);
		stage.draw();
		
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		starsImage.setSize(width, height);
		loadTable.setBounds(0, 0, width, height);
	}

	
	
	@Override
	public void onEnter() {
		
		starsImage.setColor(1, 1, 1, 0);
		starsImage.setVisible(true);
		
		new Timeline().beginSequence().push(ActorAccessor.createFadeTween(starsImage, 1, 0, 1))/*.push(ActorAccessor.createBackTween(loadTable, ActorAccessor.SHOWSPLASHBG, 1.6f, Gdx.graphics.getHeight()/1.5f, 0))*/.end().setCallback((source)->{
						
			
			
			for(Cell<?> cell : loadTable.getCells()){
				((Actor)cell.getActor()).setVisible(true);
			}
			
			
			
			new Thread(){
				public void run() {
					
					ArrayList<File> toParse = new ArrayList<File>();
					
					for(File folder : new File("/home/wiek/PlayOnLinux's virtual drives/osu_on_linux/drive_c/Program Files/osu!/Songs/").listFiles()){
						
						if(folder.isDirectory()){
							File[] files = folder.listFiles(new FilenameFilter() {
									@Override
									public boolean accept(File dir, String name) {
										return name.toLowerCase().endsWith(".osu");
									}	
								});
							
							for(File file : files){
								toParse.add(file);
							}
						
						}
					}
					
					
					bar.setRange(0, toParse.size());
					
					for(File file : toParse){
						
						setStatus("Parsing: " + file.getName());
						
						BeatMap map = BeatMap.parseBeatmap(file);
						
						if(map != null){
							JOsuClient.getClient().beatmaps.add(map);
						}
						
						bar.setValue(bar.getValue()+1);
					}
					
					
					//status.setText("Loading finished");
					setStatus("Loading finished");
					System.out.println("finish");
					
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					
					
					ended = true;
				};
								
			}.start();
					
		}).start(JOsuClient.getClient().getAnimationManager());
			
	}

	@Override
	public void onEscape() {
		
		ActorAccessor.createSineTween(stage.getRoot(), ActorAccessor.FADE, 1.f, 0.0f, 0).setCallback((a)->{
			starsTexture.dispose();
			map.dispose();
			stage.dispose();
			JOsuClient.getClient().completeChange();
		}).start(JOsuClient.getClient().getAnimationManager());
		
		
	}
	
	public void setStatus(String text){
		this.text = text.replaceAll("\\[", "\\[\\[").replaceAll("\\]", "\\]\\]");
	}
	
}