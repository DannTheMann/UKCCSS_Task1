package root.tomb.mainframe;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class FontManager {
	
	private static FontManager fontManager;
	private static final double standardFontSize = 12;
	private static final double n = 48;
	private final String DEFAULT_FONT;
	private Map<String, Font> fonts;
	
	public FontManager(String defaultFont){
		fonts = new HashMap<String, Font>();
		DEFAULT_FONT = defaultFont;
		loadDefaultFonts();
		fontManager = this;
	}
	
	private void loadDefaultFonts() {
		
		Font.getFamilies().stream()
			.forEach(f->fonts.put(f, Font.font(f, FontWeight.NORMAL, 12)));
				
		fonts.put("DEFAULT_FONT", Font.font(DEFAULT_FONT, FontWeight.NORMAL, standardFontSize));
		fonts.put("HEADER", Font.font(DEFAULT_FONT, FontWeight.NORMAL, n));
		fonts.put("EMPTY_FONT", Font.font(DEFAULT_FONT, FontWeight.NORMAL, 20));

	}

	public Font getFont(String key){
		Optional<Font> res = fonts.entrySet().stream().
					filter(k -> k.getKey().equals(key))
					.limit(1)
					.map(k->k.getValue())
					.findFirst();
		return res.isPresent() ? res.get() : getFont(DEFAULT_FONT);
	}
	
	public Font getFont(String r, FontWeight d, int g){
		return Font.font(r, d, g);
	}
	
	public static FontManager getFontManager(){
		return fontManager;
	}

}
