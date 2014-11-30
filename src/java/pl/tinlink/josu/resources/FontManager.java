package pl.tinlink.josu.resources;

import java.awt.Font;
import java.io.BufferedInputStream;
import java.lang.reflect.Field;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.GdxRuntimeException;

public enum FontManager{

	MAIN(/*"PressStart2P"*//*"Minecraftia"*/"Ubuntu-R");
	
	
	protected static HashMap<FontManager, FontData> fonts = new HashMap<FontManager, FontData>();
	
	public static BitmapFont getFont(FontManager val, int size){
		BitmapFontData data = fonts.get(val).data, dataCopy = new BitmapFontData();
		
		try {
			for(Field field : dataCopy.getClass().getFields()){
				field.setAccessible(true);
				field.set(dataCopy, data.getClass().getField(field.getName()).get(data));
			}
		} catch (Exception e) {
			throw new GdxRuntimeException("Failed to create font",e);
		}
		
		BitmapFont font = new BitmapFont(dataCopy, fonts.get(val).regions, false);
		font.setScale((float)size/32);
		font.setMarkupEnabled(false);
		return font;
	}
	
	protected String name;
	
	public static void dispose(){
		fonts.clear();
	}
	
	private FontManager(String name) {
		this.name = name;
	}
	
	static{
		
		for(FontManager val : values()){
			FileHandle file = Gdx.files.internal("assets/fonts/"+val.name+".ttf");
			String chars = "";
			try {
				Font font = Font.createFont(Font.TRUETYPE_FONT, new BufferedInputStream(file.read()));
				
				for (int c = 0; c <= Character.MAX_CODE_POINT; c++){
					if (font.canDisplay(c)){
						chars += (char)c;
					}
				}
				
				FreeTypeFontGenerator generator = new FreeTypeFontGenerator(file);
				
				//PixmapPacker packer = new PixmapPacker(1024, 1024, Pixmap.Format.RGBA8888, 2, false);
				
				FreeTypeFontParameter pam = new FreeTypeFontParameter();
				pam.size = 64;
				pam.genMipMaps = true;
				pam.magFilter = TextureFilter.MipMapLinearNearest;
				pam.minFilter = TextureFilter.MipMapLinearLinear;
				pam.characters = chars;
				
				FreeTypeBitmapFontData d = generator.generateData(pam);
				
				FontData fontData = new FontData();
				fontData.data = d;
				fontData.regions = d.getTextureRegions();
				
				fonts.put(val, fontData);
				
				//packer.dispose();
				generator.dispose();
				
			} catch (Exception e) {
				e.printStackTrace();
			}	
			
		}
	}
	
}
