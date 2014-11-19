package pl.tinlink.josu.sound;

import java.util.ArrayList;

import pl.tinlink.josu.JOsuClient;
import pl.tinlink.josu.map.BeatMap;
import pl.tinlink.josu.map.BeatMapMetaData;
import pl.tinlink.josu.utils.FileUtils;

import com.badlogic.gdx.math.MathUtils;

import ddf.minim.AudioPlayer;
import ddf.minim.analysis.BeatDetect;

public class MenuPlaylist {
	
	static ArrayList<BeatMap> playlist = new ArrayList<BeatMap>();
	
	static int currentId = -1;
	static BeatMap current;
	static float volume = 0.5f;
	static AudioPlayer player;
	static BeatDetect detect;
	
	public static void previousSong(){
		loadAndPlay(currentId>0?currentId-=1:0);
	}
	
	public static void start(){
		if(JOsuClient.getClient().beatmaps.size()>0){
			nextSong();
			skipToPreview();
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
			player.setGain(-60 + (60 * volume));
		}
		
	}
	
	
	public static void setPosition(int milis){
		if(player != null){
			player.cue(milis);
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
	
	
	public static void update(){
		if(player != null){
			
			detect.detect(player.mix);
			if(player.position()+500 >= player.length()){
				nextSong();
			}
			
		}
	}
	
	public static void play(){
		if(player != null){
			player.setGain(-60 + (60*volume));
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
		player = JOsuClient.getClient().minim.loadFile(FileUtils.getFile(map.getMetaData().getAudioFileName()).path(), 2048);
		detect = new BeatDetect(2048, player.getFormat().getFrameRate());
		detect.setSensitivity(100);
	}
	
	public static boolean isBeat(){
		if(detect != null){
			return detect.isKick() || detect.isSnare() || detect.isHat();
		}
		return false;
	}
	
	public static int getPosition(){
		return (player != null ? player.position() : 0);
	}
	
	public static int getLength(){
		return (player != null ? player.length() : 0);
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
