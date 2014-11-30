package pl.tinlink.josu;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Launcher {

	public static void main(String[] args) {
		startClient();
	}
	
	public static void startClient(){
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "JOsu!";
		cfg.width = 1024;
		cfg.height = 768;
		cfg.resizable = false;
		cfg.vSyncEnabled = false;
		cfg.useGL30 = false;
		cfg.foregroundFPS = 120;
	
		new LwjglApplication(JOsuClient.getClient(), cfg);
	}
	
	
}
