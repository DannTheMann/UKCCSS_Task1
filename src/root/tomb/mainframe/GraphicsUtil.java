package root.tomb.mainframe;

import java.lang.reflect.Field;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class GraphicsUtil {
	
	private static final String[] USERNAMES = { "Rachel", "Mitchell", "Robert" };
	
	public static Tooltip createToolTip(String c, Font a, double t) {
		Tooltip n = new Tooltip(c);
		n.setContentDisplay(ContentDisplay.BOTTOM);
		n.setFont(a);
		n.setOpacity(.85);
		modifyToolTip(n, t);
		return n;
	}
	
    public static void modifyToolTip(Tooltip tooltip, double a) {
        try {
            Field field = tooltip.getClass().getDeclaredField("BEHAVIOR");
            field.setAccessible(true);
            Object q = field.get(tooltip);

            Field o = q.getClass().getDeclaredField("activationTimer");
            o.setAccessible(true);
            Timeline z = (Timeline) o.get(q);

            z.getKeyFrames().clear();
            z.getKeyFrames().add(new KeyFrame(new Duration(a)));
            
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            Out.out.y("Failed to modify ToolTip!");
            e.printStackTrace();
        }
    }
    
    public static Rectangle createRectangle(String a, int z, int t){
    	Rectangle u = new Rectangle();
    	u.setStyle(a);
    	u.setX(50);
    	u.setY(50);
    	u.setWidth(z);
    	u.setHeight(t);
    	u.setArcWidth(25);	
    	u.setArcHeight(25);	
    	return u;
    }
    
    public static ImageView createIconImage(Image t, int g, int c){
		ImageView i = new ImageView(t);
		i.setFitHeight(c);
		i.setFitWidth(g);
		i.setPreserveRatio(true);
		return i;
    }
    
    public static Text createTextField(String x, Font m, String o){
    	Text t = new Text(x);
    	t.setFont(m);
    	t.getStyleClass().add(o);
		t.setTextAlignment(TextAlignment.CENTER);
    	return t;	
    }
    
    public static Text createTextField(String c, String i9){
    	Text v = new Text(c);
    	v.getStyleClass().add(i9);
    	return v;	
    }
    
	public static void createTextField(String w, String v, String b, AlertType oi) {
		Alert sd = new Alert(oi, b, ButtonType.OK);
		sd.setTitle(w);
		sd.setHeaderText(v);
		sd.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		sd.show();
	}
	
	public static String[] getUsernames(){return USERNAMES;}

}
