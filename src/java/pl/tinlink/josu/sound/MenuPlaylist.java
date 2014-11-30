package pl.tinlink.josu.sound;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import pl.tinlink.josu.JOsuClient;
import pl.tinlink.josu.map.BeatMap;
import pl.tinlink.josu.map.BeatMapMetaData;
import pl.tinlink.josu.resources.FileUtils;

import com.badlogic.gdx.math.MathUtils;


public class MenuPlaylist {
	
	static ArrayList<BeatMap> playlist = new ArrayList<BeatMap>();
	
	static int currentId = -1;
	static BeatMap current;
	static float volume = 0f;
	static AudioPlayer player;
	//static BeatDetect detect;
	
	public static void previousSong(){
		loadAndPlay(currentId>0?currentId-=1:0);
	}
	
	public static void start(){
		if(JOsuClient.getClient().beatmaps.size()>0){
			nextSong();
			skipToPreview();
			player.glideVolume(volume = 0.1f, 2000);
		}
	}
	
	public static void nextSong(){
		
		if(playlist.size()-1 == currentId){
			ArrayList<BeatMap> maps = JOsuClient.getClient().beatmaps;
			BeatMap map;
			do{
				map = maps.get(MathUtils.random(maps.size()-1));
			} while(isInPrevious((maps.size()<10?maps.size():10), currentId, map) != -1);
			playlist.add(map);
		}
		loadAndPlay(++currentId);
	}
	
	public static void setVolume(float vol){
		
		volume = vol;
		
		if(player != null){
			player.setVolume(volume);
		}
		
	}
	
	
	public static void setPosition(int milis){
		if(player != null){
			player.setPosition(milis);
		}
	}
	
	public static void skipToPreview(){
		if(current != null){
			if(current.getMetaData().getPreviewTime()>0)
				setPosition(current.getMetaData().getPreviewTime());
		}
	}
	
	
	private static int isInPrevious(int previous, int id, BeatMap map){
		
		previous = (id-previous < 0 ? id : previous);
		BeatMapMetaData mapData = map.getMetaData();
		
		for(int i = id-1;i>id-previous;i--){
			BeatMapMetaData data = playlist.get(i).getMetaData();
			if(data.getArtist().equals(mapData.getArtist()) && data.getTitle().equals(mapData.getTitle())){
				return i;
			}
		}
		
		return -1;
	}
	
	private static int isInNext(int next, int id, BeatMap map){
		
		next = (id+next > playlist.size()-1? playlist.size()-id : next);
		
		BeatMapMetaData mapData = map.getMetaData();
		
		for(int i = id-1;i<id+next;i++){
			BeatMapMetaData data = playlist.get(i).getMetaData();
			if(data.getArtist().equals(mapData.getArtist()) && data.getTitle().equals(mapData.getTitle())){
				return i;
			}
		}
		
		return -1;
	}
	
	public static void replaceCurrent(BeatMap map){
		
		if(current.getMetaData().getAudioFileName().equals(map.getMetaData().getAudioFileName())) return;
		
		int prev = isInPrevious(5, currentId, map);
		int nxt = isInNext(5, currentId, map);
				
		int id = currentId;
		
		if(prev != -1 || nxt != -1){
			id = (prev != -1?prev:(nxt != -1?nxt:currentId));
			return;
		} else {
			playlist.set(id=currentId, map);
		}
		
		loadAndPlay(id);
		
	}
	
	public static AudioPlayer getCurrentPlayer(){
		return player;
	}
	
	public static void update(){
		if(player != null){
			
			//detect.detect(player.mix);
			if(player.hasEnded()){
				nextSong();
			}
			
		}
	}
	
	public static void play(){
		if(player != null){
			player.setVolume(volume);
			player.play();
		}
	}
	
	public static void pause(){
		if(player != null){
			player.pause();
		}
	}
	
	public static void stop(){
		if(player != null){
			player.pause();
			setPosition(0);
		}
	}
	
	private static void load(BeatMap map){
		if(player != null){
			player.close();
		}
		current = map;
		System.out.println(map.getMetaData().getAudioFileName());
		try {
			player = new AudioPlayer(FileUtils.getFile(map.getMetaData().getAudioFileName()).file());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static int getPosition(){
		return (player != null ? player.getPosition() : 0);
	}
	
	public static int getLength(){
		return (player != null ? player.getLength() : 0);
	}
	
	public static int getCurrentId(){
		return currentId;
	}
	
	public static void loadAndPlay(int id){
		stop();
		currentId = id;
		load(playlist.get(id));
		play();
	}
	
	
	public static void replacePreview(BeatMap map){
		replaceCurrent(map);
		skipToPreview();
	}

	public static BeatMap getCurrent() {
		return current;
	}
	
}