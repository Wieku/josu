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
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.SamplePlayer;
import net.beadsproject.beads.ugens.SamplePlayer.EnvelopeType;

public class AudioPlayer {
	
	SamplePlayer player;
	static AudioContext context;
	Sample sample;
	Glide volumeGlide;
	Glide pitchGlide;
	Glide speedGlide;
	boolean paused;
	private boolean playing;
	private PeakDetector beatDetector;
	private boolean beat;
	
	static {
		context = new AudioContext();
		context.start();
	}
	
	public AudioPlayer(File file) throws FileNotFoundException{
		if(!file.exists()) throw new FileNotFoundException(file.getAbsolutePath());
		
		try {
			
			
			player = new SamplePlayer(context, sample = new Sample(file.getAbsolutePath()));
			player.setRate(speedGlide = new Glide(context, 1.0f, 0));
			player.setPitch(pitchGlide = new Glide(context, 1.0f, 0));
			player.setEnvelopeType(EnvelopeType.FINE);
			Gain gain = new Gain(context, 1, volumeGlide = new Glide(context, 1.0f, 0));
			gain.addInput(player);
			
			
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
					beat = true;
				}
			});
			 
			// tell the AudioContext that it needs to update the 
			// ShortFrameSegmenter
			context.out.addDependent(sfs);
			// start working with audio data
			
			context.out.addInput(gain);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setVolume(float volume){
		volumeGlide.setValue(volume);
	}
	
	public void setSpeed(float speed){
		speedGlide.setValue(speed);
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
			player = new SamplePlayer(context, sample);
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
	
	public boolean isBeat(){
		return (beat?!(beat = false):false);
	}
	
	public void stop(){
		playing = false;
		player.reset();
	}
	
	public void close(){
		player.kill();
	}
	
}
