/*package pl.tinlink.josu.sound;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import net.beadsproject.beads.analysis.featureextractors.PeakDetector;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.ugens.Glide;
import pl.tinlink.josu.JOsuClient;
import pl.tinlink.josu.resources.FileUtils;
import ddf.minim.AudioListener;
import ddf.minim.AudioOutput;
import ddf.minim.Minim;
import ddf.minim.analysis.BeatDetect;
import ddf.minim.spi.AudioRecordingStream;
import ddf.minim.ugens.FilePlayer;

public class AudioPlayer {
	
	FilePlayer player;
	Minim minim;
	BeatListener listener;
	AudioRecordingStream stream;
	Sample sample;
	float volume = 0.5f;
	Glide pitchGlide;
	Glide speedGlide;
	BeatDetect detect;
	boolean paused;
	AudioOutput out;
	private PeakDetector beatDetector;
	
	static {
		//context = new AudioContext(2048);
		//context.start();
	}
	
	public AudioPlayer(File file) throws FileNotFoundException{
		if(!file.exists()) throw new FileNotFoundException(file.getAbsolutePath());
		
		
			stream = JOsuClient.getClient().minim.loadFileStream(file.getAbsolutePath(), 2048, false);
			
			player = new FilePlayer(stream);
			
			player.patch(out = JOsuClient.getClient().minim.getLineOut());
			
			detect = new BeatDetect(stream.getFormat().getFrameSize(), stream.getFormat().getFrameRate());
			detect.setSensitivity(50);
			
			new Thread(()->{
					if(listener != null){
						detect.detect(out.mix);
						if(detect.isKick() || detect.isSnare() || detect.isHat()){
							listener.onBeat();
						}
					}
			});
			
			
			
			//player = (minim = new Minim(this)).loadFile(file.getAbsolutePath());
			
			//player.setRate(speedGlide = new Glide(context, 1.0f, 0));
			//player.setPitch(pitchGlide = new Glide(context, 1.0f, 0));
			//player.setEnvelopeType(EnvelopeType.FINE);
			//Gain gain = new Gain(context, 1, volumeGlide = new Glide(context, 1.0f, 0));
			//gain.addInput(player);
			
			ShortFrameSegmenter sfs = new ShortFrameSegmenter(context);
			// how large is each chunk?
			sfs.setChunkSize(2048);
			sfs.setHopSize(441);
			// connect the sfs to the AudioContext
			sfs.addInput(context.out);
			
			FFT fft = new FFT();
			PowerSpectrum ps = new PowerSpectrum();
			
			sfs.addListener(fft);
			fft.addListener(ps);
			
			// The SpectralDifference unit generator does exactly what 
			// it sounds like. It calculates the difference between two
			// consecutive spectrums returned by an FFT (through a 
			// PowerSpectrum object).
			SpectralDifference sd = new SpectralDifference(context.getSampleRate());
			ps.addListener(sd);
			
			// we will use the PeakDetector object to actually find our
			// beats
			beatDetector = new PeakDetector();
			sd.addListener(beatDetector);
			 
			// the threshold is the gain level that will trigger the 
			// beat detector - this will vary on each recording
			beatDetector.setThreshold(0.02f);
			beatDetector.setAlpha(.9f);
			// whenever our beat detector finds a beat, set a global 
			// variable 
			
			beatDetector.addMessageListener(new Bead() {
				@Override
				protected void messageReceived(Bead arg0) {
					if(listener != null)
						listener.onBeat();
				}
			});
			 
			// tell the AudioContext that it needs to update the 
			// ShortFrameSegmenter
			context.out.addDependent(sfs);
			// start working with audio data
			
			context.out.addInput(gain);
			
		
		
	}
	
	public void setVolume(float volume){
		this.volume = volume;
		
		if(player != null){
			out.setGain(-80 + 80*volume);
		}
		
	}
	
	public void setSpeed(float speed){
		out.setTempo(speed);
	}
	
	public void setPitch(float pitch){
		pitchGlide.setValue(pitch);
	}
	
	public int getPosition(){
		return (int) player.position();
	}
	
	public int getLength(){
		return (int) player.length();
	}
	
	public void setPosition(int milis){
		player.cue(milis);
	}
	
	public void play(){
		player.play();
	}
	
	public void pause(){
		player.pause();
	}
	
	public boolean hasEnded(){
		return !player.isPlaying();
	}
	
	public void setBeatListener(BeatListener listener){
		this.listener = listener;
	}
	
	public void stop(){
		player.pause();
		player.cue(0);
	}
	
	public void close(){
		player.close();
	}
	
	
	public InputStream createInput(String file){
		return FileUtils.getFile(file).read();
	}
	
	
}*/


package pl.tinlink.josu.sound;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.beadsproject.beads.analysis.featureextractors.FFT;
import net.beadsproject.beads.analysis.featureextractors.PeakDetector;
import net.beadsproject.beads.analysis.featureextractors.PowerSpectrum;
import net.beadsproject.beads.analysis.featureextractors.SpectralDifference;
import net.beadsproject.beads.analysis.segmenters.ShortFrameSegmenter;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.ugens.Clock;
import net.beadsproject.beads.ugens.Compressor;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.GranularSamplePlayer;
import net.beadsproject.beads.ugens.SamplePlayer;
import net.beadsproject.beads.ugens.SamplePlayer.EnvelopeType;
import net.beadsproject.beads.ugens.Static;

import com.badlogic.gdx.Gdx;

public class AudioPlayer {
	
	GranularSamplePlayer player;
	static AudioContext context;
	BeatListener listener;
	Sample sample;
	Glide volumeGlide;
	Glide pitchGlide;
	Glide speedGlide;
	boolean paused;
	private boolean playing;
	private PeakDetector beatDetector;
	
	static {
		context = new AudioContext(2048);
		context.start();
	}
	
	public AudioPlayer(File file) throws FileNotFoundException{
		if(!file.exists()) throw new FileNotFoundException(file.getAbsolutePath());
		
		try {
			player = new GranularSamplePlayer(context, sample = new Sample(file.getAbsolutePath(), Gdx.files.absolute(file.getAbsolutePath()).extension().equals("ogg")?true:false));
			
			player.setRate(speedGlide = new Glide(context, 1.0f, 0));
			player.setPitch(pitchGlide = new Glide(context, 1.0f, 0));
			player.setEnvelopeType(EnvelopeType.FINE);
			
			Gain gain = new Gain(context, 1, volumeGlide = new Glide(context, 1.0f, 0));
			
			gain.addInput(player);
			context.out.addDependent(getDetector(true));
			context.out.addDependent(getDetector(false));
			
			context.out.addInput(gain);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private ShortFrameSegmenter getDetector(boolean low){
		
		ShortFrameSegmenter sfs = new ShortFrameSegmenter(context);
		
		sfs.setChunkSize(2048);
		sfs.setHopSize(441);
		sfs.addInput(context.out);
		
		FFT fft = new FFT();
		PowerSpectrum ps = new PowerSpectrum();
		
		sfs.addListener(fft);
		fft.addListener(ps);
		
		SpectralDifference sd = new SpectralDifference(context.getSampleRate());
		ps.addListener(sd);
		
		beatDetector = new PeakDetector();
		sd.addListener(beatDetector);
		beatDetector.setNumberOfFeatures(10);
		beatDetector.setThreshold((low?0.8f:0.05f));
		beatDetector.setAlpha((low?0.95f:0.5f));
		
		beatDetector.addMessageListener(new Bead() {
			@Override
			protected void messageReceived(Bead arg0) {
				if(listener != null)
					if(low)
						listener.onBeatLow();
					else
						listener.onBeatHigh();
			}
		});
		 
		return sfs;
	}
	
	public void glideVolume(float volume, float time){
		volumeGlide.setGlideTime(time);
		volumeGlide.setValue(volume);
	}
	
	public void setVolume(float volume){
		volumeGlide.setValueImmediately(volume);
	}
	
	public void glideSpeed(float speed, float time){
		speedGlide.setGlideTime(time);
		speedGlide.setValue(speed);
	}
	
	public void setSpeed(float speed){
		speedGlide.setValueImmediately(speed);
	}
	
	public void glidePitch(float pitch, float time){
		pitchGlide.setGlideTime(time);
		pitchGlide.setValue(pitch);
	}
	
	public void setPitch(float pitch){
		pitchGlide.setValue(pitch);
	}
	
	public int getPosition(){
		return (int) player.getPosition();
	}
	
	public int getLength(){
		return (int) sample.getLength();
	}
	
	public void setPosition(int milis){
		player.setPosition(milis);
	}
	
	public void play(){
		
		if(player.isDeleted()){
			player = new GranularSamplePlayer(context, sample);
		}
		
		if(!player.isPaused()){
			if(!playing){
				player.start(0);
			}
				playing = true;
		} else {
			player.pause(false);
		}
	}
	
	public void pause(){
		player.pause(!(playing = false));
	}
	
	public boolean hasEnded(){
		return player.isDeleted();
	}
	
	public void setBeatListener(BeatListener listener){
		this.listener = listener;
	}
	
	public void stop(){
		playing = false;
		player.reset();
	}
	
	public void close(){
		player.kill();
	}
	
}

