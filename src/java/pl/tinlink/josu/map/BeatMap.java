package pl.tinlink.josu.map;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

import com.badlogic.gdx.utils.Logger;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class BeatMap {

	
	public List<String> objectsString;
	public File file;
	public String title;
	public BeatMapMetaData metadata;
	
	private static Logger logger = new Logger("BeatmapParser");
	
	public BeatMap(File file, BeatMapMetaData data){
		this.file = file;
		this.metadata = data;
	}
	
	
	public void prepareBeatMapData(){
		
	}
	
	public BeatMapMetaData getMetaData(){
		return metadata;
	}
	
	public static BeatMap parseBeatmap(File file){
		
		List<String> content;
		try {
			content = Files.readLines(file, Charsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		BeatMapMetaData data = new BeatMapMetaData();
		String[] tokens;
		ListIterator<String> iterator;
		
		for(int i=0;i<content.size();i++){
			
			switch(content.get(i)){
			
				case "[General]":
					
					iterator = content.listIterator(i+1);
						
					while(iterator.hasNext()){
						String string = iterator.next().trim();
						
						if (string.startsWith("["))
							break;
						if ((tokens = tokenize(string, ':')) == null)
							continue;
						
						try {
							switch (tokens[0]) {
								case "AudioFilename":
									
									//OGG's are not supported for now
									String[] tok = tokens[1].split("\\.");
									if(tok[tok.length-1].equals("ogg")){
										return null;
									}
									data.setAudioFileName(file.getParent() + File.separator + tokens[1]);
									break;
								case "AudioLeadIn":
									data.setAudioLeadIn(Integer.parseInt(tokens[1]));
									break;
								case "PreviewTime":
									data.setPreviewTime(Integer.parseInt(tokens[1]));
									break;
								case "Countdown":
									data.setCountdown(Byte.parseByte(tokens[1]));
									break;
								case "SampleSet":
									data.setSampleSet(tokens[1]);
									break;
								case "StackLeniency":
									data.setStackLeniency(Float.parseFloat(tokens[1]));
									break;
								case "Mode":
									data.setMode(Byte.parseByte(tokens[1]));
									
									//If mode is other than osu!standard
									if (data.getMode() != 0)
										return null;
									break;
								case "LetterboxInBreaks":
									data.setLetterboxInBreaks((Integer.parseInt(tokens[1]) == 1));
									break;
								case "WidescreenStoryboard":
									data.setWidescreenStoryboard((Integer.parseInt(tokens[1]) == 1));
									break;
								case "EpilepsyWarning":
									data.setEpilepsyWarning((Integer.parseInt(tokens[1]) == 1));
								default:
									break;
							}
						} catch (Exception e) {
							logger.error(String.format("Failed to read line '%s' for file '%s'.",
									string, file.getAbsolutePath()), e);
						}
					}
					break;
				case "[Metadata]":
					iterator = content.listIterator(i+1);
					
					while(iterator.hasNext()){
						String string = iterator.next().trim();
						
						if (string.startsWith("["))
							break;
						if ((tokens = tokenize(string, ':')) == null)
							continue;
						
						try {
							switch (tokens[0]) {
							case "Title":
								data.setTitle(tokens[1]);
								break;
							case "TitleUnicode":
								data.setTitleUnicode(tokens[1]);
								break;
							case "Artist":
								data.setArtist(tokens[1]);
								break;
							case "ArtistUnicode":
								data.setArtistUnicode(tokens[1]);
								break;
							case "Creator":
								data.setCreator(tokens[1]);
								break;
							case "Version":
								data.setVersion(tokens[1]);
								break;
							case "Source":
								data.setSource(tokens[1]);
								break;
							case "Tags":
								data.setTags(tokens[1].toLowerCase());
								break;
							case "BeatmapID":
								data.setBeatmapID(Integer.parseInt(tokens[1]));
								break;
							case "BeatmapSetID":
								data.setBeatmapSetID(Integer.parseInt(tokens[1]));
								break;
							}
						} catch (Exception e) {
							logger.error(String.format("Failed to read metadata '%s' for file '%s'.",
									string, file.getAbsolutePath()), e);
						}
					}
					break;
				case "[Difficulty]":
					iterator = content.listIterator(i+1);
					
					while(iterator.hasNext()){
						String string = iterator.next().trim();
						
						if (string.startsWith("["))
							break;
						if ((tokens = tokenize(string, ':')) == null)
							continue;
						
						try {
							switch (tokens[0]) {
							case "HPDrainRate":
								data.setHPDrainRate(Float.parseFloat(tokens[1]));
								break;
							case "CircleSize":
								data.setCircleSize(Float.parseFloat(tokens[1]));
								break;
							case "OverallDifficulty":
								data.setOverallDifficulty(Float.parseFloat(tokens[1]));
								break;
							case "ApproachRate":
								data.setApproachRate(Float.parseFloat(tokens[1]));
								break;
							case "SliderMultiplier":
								data.setSliderMultiplier(Float.parseFloat(tokens[1]));
								break;
							case "SliderTickRate":
								data.setSliderTickRate(Float.parseFloat(tokens[1]));
								break;
							}
						} catch (Exception e) {
							logger.error(String.format("Failed to read difficulty '%s' for file '%s'.",
									string, file.getAbsolutePath()), e);
						}
					}
					if (data.getApproachRate() == -1f)  // not in old format
						data.setApproachRate(data.getOverallDifficulty());
					break;
				case "[Events]":
					iterator = content.listIterator(i+1);
					
					while(iterator.hasNext()){
						String string = iterator.next().trim();
						
						if (string.startsWith("["))
							break;
						if ((tokens = tokenize(string, ',')) == null)
							continue;
						
						switch (tokens[0]) {
							case "0":  // background
								tokens[2] = tokens[2].replaceAll("(^\")|(\"$)", "");
								data.setBackgroundName(file.getParent() + File.separator + tokens[2]);
								break;
							case "Video":
								tokens[2] = tokens[2].replaceAll("(^\")|(\"$)", "");
								data.setMovieName(file.getParent() + File.separator + tokens[2]);
								break;
							default:
								/* Not implemented. */
								break;
						}
					}
					break;
			
			}
			
		}
		
		return new BeatMap(file, data);
	}
	
	public static String[] tokenize(String string, char by){
		
		if(string.startsWith("//")) return null;
		if(by == ':'){
			int index = string.indexOf(by);
			if (index < 0) return null;
			String[] tokens = new String[2];
			tokens[0] = string.substring(0,index).trim();
			tokens[1] = string.substring(index+1).trim();
			return tokens;
		} else if(by == ','){
			String[] tokens = string.split(Character.toString(by));
			if(tokens == null) return null;
			for(int i=0;i<tokens.length;i++){
				tokens[i] = tokens[i].trim();
			}
			return tokens;
		}
		return null;
	}
	
	
}
