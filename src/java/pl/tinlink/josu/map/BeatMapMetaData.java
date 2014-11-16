package pl.tinlink.josu.map;

public class BeatMapMetaData {

	private String audioFileName = "";
	public int audioLeadIn = 0;                 // delay before music starts (in ms)
	public int previewTime = 0;                // start position of music preview (in ms)
	public byte countdown = 0;                  // countdown type (0:disabled, 1:normal, 2:half, 3:double)
	public String sampleSet = "";               // sound samples ("None", "Normal", "Soft")
	public float stackLeniency = 0.7f;          // how often closely placed hit objects will be stacked together
	public byte mode = 0;                       // game mode (0:osu!, 1:taiko, 2:catch the beat, 3:osu!mania)
	public boolean letterboxInBreaks = false;   // whether the letterbox (top/bottom black bars) appears during breaks
	public boolean widescreenStoryboard = false;// whether the storyboard should be widescreen
	public boolean epilepsyWarning = false; 
	
	public String title = "";                   // song title
	public String titleUnicode = "";            // song title (unicode)
	public String artist = "";                  // song artist
	public String artistUnicode = "";           // song artist (unicode)
	public String creator = "";                 // beatmap creator
	public String version = "";                 // beatmap difficulty
	public String source = "";                  // song source
	public String tags = "";                    // song tags, for searching
	public int beatmapID = 0;                   // beatmap ID
	public int beatmapSetID = 0;                // beatmap set ID

	public float HPDrainRate = 5f;              // HP drain (0:easy ~ 10:hard)
	public float circleSize = 4f;               // size of circles
	public float overallDifficulty = 5f;        // affects timing window, spinners, and approach speed (0:easy ~ 10:hard)
	public float approachRate = -1f;            // how long circles stay on the screen (0:long ~ 10:short) **not in old format**
	public float sliderMultiplier = 1f;         // slider movement speed multiplier
	public float sliderTickRate = 1f;
	
	public String backgroundName;               // path to the first image
	public String movieName;                    // path to the video if exists
			
			
	/**
	 * @return the fileName
	 */
	public String getAudioFileName() {
		return audioFileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setAudioFileName(String fileName) {
		this.audioFileName = fileName;
	}
	/**
	 * @return the audioLeadIn
	 */
	public int getAudioLeadIn() {
		return audioLeadIn;
	}
	/**
	 * @param audioLeadIn the audioLeadIn to set
	 */
	public void setAudioLeadIn(int audioLeadIn) {
		this.audioLeadIn = audioLeadIn;
	}
	/**
	 * @return the previewTime
	 */
	public int getPreviewTime() {
		return previewTime;
	}
	/**
	 * @param previewTime the previewTime to set
	 */
	public void setPreviewTime(int previewTime) {
		this.previewTime = previewTime;
	}
	/**
	 * @return the countdown
	 */
	public byte getCountdown() {
		return countdown;
	}
	/**
	 * @param countdown the countdown to set
	 */
	public void setCountdown(byte countdown) {
		this.countdown = countdown;
	}
	/**
	 * @return the sampleSet
	 */
	public String getSampleSet() {
		return sampleSet;
	}
	/**
	 * @param sampleSet the sampleSet to set
	 */
	public void setSampleSet(String sampleSet) {
		this.sampleSet = sampleSet;
	}
	/**
	 * @return the stackLeniency
	 */
	public float getStackLeniency() {
		return stackLeniency;
	}
	/**
	 * @param stackLeniency the stackLeniency to set
	 */
	public void setStackLeniency(float stackLeniency) {
		this.stackLeniency = stackLeniency;
	}
	/**
	 * @return the mode
	 */
	public byte getMode() {
		return mode;
	}
	/**
	 * @param mode the mode to set
	 */
	public void setMode(byte mode) {
		this.mode = mode;
	}
	/**
	 * @return the letterboxInBreaks
	 */
	public boolean isLetterboxInBreaks() {
		return letterboxInBreaks;
	}
	/**
	 * @param letterboxInBreaks the letterboxInBreaks to set
	 */
	public void setLetterboxInBreaks(boolean letterboxInBreaks) {
		this.letterboxInBreaks = letterboxInBreaks;
	}
	/**
	 * @return the widescreenStoryboard
	 */
	public boolean isWidescreenStoryboard() {
		return widescreenStoryboard;
	}
	/**
	 * @param widescreenStoryboard the widescreenStoryboard to set
	 */
	public void setWidescreenStoryboard(boolean widescreenStoryboard) {
		this.widescreenStoryboard = widescreenStoryboard;
	}
	/**
	 * @return the epilepsyWarning
	 */
	public boolean isEpilepsyWarning() {
		return epilepsyWarning;
	}
	/**
	 * @param epilepsyWarning the epilepsyWarning to set
	 */
	public void setEpilepsyWarning(boolean epilepsyWarning) {
		this.epilepsyWarning = epilepsyWarning;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the titleUnicode
	 */
	public String getTitleUnicode() {
		return titleUnicode;
	}
	/**
	 * @param titleUnicode the titleUnicode to set
	 */
	public void setTitleUnicode(String titleUnicode) {
		this.titleUnicode = titleUnicode;
	}
	/**
	 * @return the artist
	 */
	public String getArtist() {
		return artist;
	}
	/**
	 * @param artist the artist to set
	 */
	public void setArtist(String artist) {
		this.artist = artist;
	}
	/**
	 * @return the artistUnicode
	 */
	public String getArtistUnicode() {
		return artistUnicode;
	}
	/**
	 * @param artistUnicode the artistUnicode to set
	 */
	public void setArtistUnicode(String artistUnicode) {
		this.artistUnicode = artistUnicode;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return the tags
	 */
	public String getTags() {
		return tags;
	}
	/**
	 * @param tags the tags to set
	 */
	public void setTags(String tags) {
		this.tags = tags;
	}
	/**
	 * @return the beatmapID
	 */
	public int getBeatmapID() {
		return beatmapID;
	}
	/**
	 * @param beatmapID the beatmapID to set
	 */
	public void setBeatmapID(int beatmapID) {
		this.beatmapID = beatmapID;
	}
	/**
	 * @return the beatmapSetID
	 */
	public int getBeatmapSetID() {
		return beatmapSetID;
	}
	/**
	 * @param beatmapSetID the beatmapSetID to set
	 */
	public void setBeatmapSetID(int beatmapSetID) {
		this.beatmapSetID = beatmapSetID;
	}
	/**
	 * @return the hPDrainRate
	 */
	public float getHPDrainRate() {
		return HPDrainRate;
	}
	/**
	 * @param hPDrainRate the hPDrainRate to set
	 */
	public void setHPDrainRate(float hPDrainRate) {
		HPDrainRate = hPDrainRate;
	}
	/**
	 * @return the circleSize
	 */
	public float getCircleSize() {
		return circleSize;
	}
	/**
	 * @param circleSize the circleSize to set
	 */
	public void setCircleSize(float circleSize) {
		this.circleSize = circleSize;
	}
	/**
	 * @return the overallDifficulty
	 */
	public float getOverallDifficulty() {
		return overallDifficulty;
	}
	/**
	 * @param overallDifficulty the overallDifficulty to set
	 */
	public void setOverallDifficulty(float overallDifficulty) {
		this.overallDifficulty = overallDifficulty;
	}
	/**
	 * @return the approachRate
	 */
	public float getApproachRate() {
		return approachRate;
	}
	/**
	 * @param approachRate the approachRate to set
	 */
	public void setApproachRate(float approachRate) {
		this.approachRate = approachRate;
	}
	/**
	 * @return the sliderMultiplier
	 */
	public float getSliderMultiplier() {
		return sliderMultiplier;
	}
	/**
	 * @param sliderMultiplier the sliderMultiplier to set
	 */
	public void setSliderMultiplier(float sliderMultiplier) {
		this.sliderMultiplier = sliderMultiplier;
	}
	/**
	 * @return the sliderTickRate
	 */
	public float getSliderTickRate() {
		return sliderTickRate;
	}
	/**
	 * @param sliderTickRate the sliderTickRate to set
	 */
	public void setSliderTickRate(float sliderTickRate) {
		this.sliderTickRate = sliderTickRate;
	}
	/**
	 * @return the backgroundName
	 */
	public String getBackgroundName() {
		return backgroundName;
	}
	/**
	 * @param backgroundName the backgroundName to set
	 */
	public void setBackgroundName(String backgroundName) {
		this.backgroundName = backgroundName;
	}
	/**
	 * @return the movieName
	 */
	public String getMovieName() {
		return movieName;
	}
	/**
	 * @param movieName the movieName to set
	 */
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
	
}
