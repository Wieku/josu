package pl.tinlink.josu.utils;

import java.io.File;

import com.badlogic.gdx.files.FileHandle;

public class FileUtils {
	public static FileHandle getFile(String path){
		for(File file : new File(path).getParentFile().listFiles()){
			if(file.getAbsolutePath().equalsIgnoreCase(path)){
				return new FileHandle(file);
			}
		}
		
		return new FileHandle(new File(path));
	}
}
