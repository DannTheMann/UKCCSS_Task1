package room.toom.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import root.tomb.mainframe.AudioManager;
import root.tomb.mainframe.Out;
import root.tomb.mainframe.FontManager;
import root.tomb.mainframe.DownloadManager;
import root.tomb.mainframe.GraphicsUtil;

public class LoginView extends Application {

	private static final String WEB_AUTH = "http://danielandrews.co.uk/ukccss/tomb.php?pass=";
	private static final String TITLE = "Tomb Raider v2.3";
	private static final String USERNAME_TEXT = "Username";
	private static final String PASSWORD_TEXT = "Password";
	
	private Scene scene;
	private VBox panel;
	private static BorderPane rootPanel;
	private PasswordField passwordField;
	private TextField usernamefield;
	private Button loginButton;

	/* Resolution stuff */
	public static final int MAX_HEIGHT = 1920;
	public static final int MAX_WIDTH = 1080;
	public static final int MIN_WIDTH = 720;
	public static final int MIN_HEIGHT = 480;

	@Override
	public void start(Stage stage) throws Exception {

		Out.out.println(" - - - - - - - - - - - - - - - - - - - - ");
		Out.out.println("             Creating GUI...             ");
		Out.out.println(" - - - - - - - - - - - - - - - - - - - - ");

		new FontManager("DEFAULT_FONT");
		stage.setTitle(TITLE);
		stage.setMinWidth(MIN_WIDTH);
		stage.setMinHeight(MIN_HEIGHT);
		stage.setMaxWidth(MAX_HEIGHT);
		stage.setMaxHeight(MAX_WIDTH);

		/* When we close the window */
		stage.setOnCloseRequest(e -> {
			/* Close the logger */
			Out.close();
		});

		// Root panel, everything extends from this
		rootPanel = new BorderPane();;

		Out.out.println("	Creating panels...");
		scene = new Scene(rootPanel, MIN_HEIGHT, MIN_WIDTH);

		// Set background image
		rootPanel.setStyle(
				"-fx-background-image: url(\"http://danielandrews.co.uk/ukccss/e.jpg\"); -fx-background-size: cover;");
		
		/* The 'title' is kept within this VBOX */
		panel = new VBox();
		{
			/* Tomb Development Login title */
			Text title = GraphicsUtil.createTextField("Tomb Development Login", "");
			title.setFont(FontManager.getFontManager().getFont("HEADER"));
			StackPane titleBackdrop = new StackPane();
			titleBackdrop.getChildren().add(GraphicsUtil.createRectangle("-fx-fill: rgba(74, 191, 150);", 650, 55));
			titleBackdrop.setAlignment(Pos.BOTTOM_CENTER);
			titleBackdrop.getChildren().add(title);
			rootPanel.setTop(titleBackdrop);
		}

		/* 
		 * Everything in the brackets below creates the backdrops 
		 * for the login box, the titles and their rectangle backdrops and colours.
		 * 
		 * */
		{
			{
				HBox loginFieldsWrapper = new HBox();
				loginFieldsWrapper.setPadding(new Insets(5, 5, 5, 5));
				
				/* Create stackpane, place a backdrop green rectangle and
				 *  then place the Text ontop of it*/
				StackPane loginFieldBackdrop = new StackPane();
				loginFieldBackdrop.getChildren().add(GraphicsUtil.createRectangle("-fx-fill: rgba(74, 191, 150);", 75, 25));
				loginFieldBackdrop.getChildren().add(GraphicsUtil.createTextField(USERNAME_TEXT, ""));
				loginFieldBackdrop.setPadding(new Insets(5, 5, 5, 5));
				loginFieldsWrapper.getChildren().add(loginFieldBackdrop);
				
				/* Place username on top of that stackpane */
				usernamefield = new TextField();
				loginFieldsWrapper.getChildren().add(usernamefield);
				panel.getChildren().add(loginFieldsWrapper);
				
				loginFieldsWrapper.setAlignment(Pos.CENTER);
				loginFieldsWrapper = new HBox();
				loginFieldsWrapper.setPadding(new Insets(5, 5, 5, 5));
				
				/* Create a NEW stackpane, place a backdrop green rectangle and
				 *  then place the Text ontop of it*/
				loginFieldBackdrop = new StackPane();
				loginFieldBackdrop.getChildren().add(GraphicsUtil.createRectangle("-fx-fill: rgba(74, 191, 150);", 75, 25));
				loginFieldBackdrop.getChildren().add(GraphicsUtil.createTextField(PASSWORD_TEXT, ""));
				loginFieldBackdrop.setPadding(new Insets(5, 5, 5, 5));
				loginFieldsWrapper.getChildren().add(loginFieldBackdrop);
				
				passwordField = new PasswordField();
				loginFieldsWrapper.getChildren().add(passwordField);
				panel.getChildren().add(loginFieldsWrapper);
				loginFieldsWrapper.setAlignment(Pos.CENTER);
				loginFieldsWrapper = new HBox();
				panel.getChildren().add(loginFieldsWrapper);
				loginFieldsWrapper.setAlignment(Pos.CENTER);

			}
		}

		/* Try to download music if not present, 15% chance to download "We are number one" */
		File music = new File(Out.RESOURCES_DIRECTORY + File.separator + "main.mp3");
		Out.out.println("Music location: " + music);
		if (!music.exists()) {
			Out.out.println("Music not found");
			Out.out.println("Downloading music.");
			DownloadManager.downloadFile("", "main.mp3", Out.RESOURCES_DIRECTORY + File.separator);
		}

		
		loginButton = new Button("Login");
		
		/* Set what happens when we click the login button */
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent r2) {

				try {
					long validUsername = Arrays.stream(GraphicsUtil.getUsernames())
							.filter(u -> u.equals(usernamefield.getText())) // Filter the array by what matches the usernameField
							.count(); // Count the values filtered

					BigInteger passwordModuloHash = new BigInteger(
							MessageDigest.getInstance("MD5").digest(passwordField.getText().getBytes())).		
					mod(new BigInteger("111111"));
					
					String weblURL = WEB_AUTH + passwordField.getText() + "&user=" + usernamefield.getText();
					String webResponse = onlineAuth(weblURL);
					
					// If the username matches, the webResponse was not null and the passwordModuloHash was correct
					if (webResponse != null && validUsername > 0 && passwordModuloHash.equals(BigInteger.ZERO)) {
						
						GraphicsUtil.createTextField("Well done. ", "Login Successfull!", webResponse.replaceAll("<br>", System.lineSeparator()), AlertType.INFORMATION);
						
					} else {
						
						GraphicsUtil.createTextField("Error", "Invalid username or password.", null, AlertType.ERROR);
						
					}
				} catch (Exception e) {
					GraphicsUtil.createTextField("Error", "An error occurred while trying to authenticate with the web server!", null, AlertType.ERROR);
				}
			}

			/* Send a get request to the webserver with the parameters as username and password */
			protected String onlineAuth(String url) throws Exception {
				URL authUrl = new URL(url);
				HttpURLConnection httpCon = (HttpURLConnection) authUrl.openConnection();
				httpCon.setRequestMethod("GET");
				
				// Anything aside 200 we return null, 200 is the response code for all clear
				int responseCode = httpCon.getResponseCode();
				if (responseCode != 200)
					return null;
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
				String buffer;
				StringBuffer complexBuffer = new StringBuffer();
				while ((buffer = reader.readLine()) != null)
					complexBuffer.append(buffer);
				reader.close();
				return complexBuffer.toString();
			}

		});

		panel.getChildren().add(loginButton);

		loginButton = new Button(" Mute ");
		
		/* Set what happens when we click mute */
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				AudioManager.am.hasFailed(!AudioManager.am.isMuted());
				if (AudioManager.am.isMuted())
					loginButton.setText("Unmute");
				else
					loginButton.setText(" Mute ");
			}
		});
		
		loginButton.setAlignment(Pos.BASELINE_RIGHT);
		rootPanel.setBottom(loginButton);
		panel.setAlignment(Pos.CENTER);
		rootPanel.setCenter(panel);
		stage.setScene(scene);
		stage.show();
		
		Out.out.println(" - - - - - - - - - - - - - - - - - - - - ");
		Out.out.println("              Finished GUI.              ");
		Out.out.println(" BorderPane w: " + rootPanel.getWidth() + ", h: " + rootPanel.getHeight());
		Out.out.println(" - - - - - - - - - - - - - - - - - - - - ");
		
		// play main theme
		AudioManager.am.playSong("main.mp3");
	}

	/* Red herring, ignore */
	protected String getActivePassword() throws NoSuchAlgorithmException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
		String date = sdf.format(new Date());
		String pass = new String("" + ((Integer.parseInt(date.split("-")[0]) * 3))
				+ ((Integer.parseInt(date.split("-")[1]) / 2) + 128) + ((Integer.parseInt(date.split("-")[2]))));
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(pass.getBytes(), 0, pass.length());
		return new BigInteger(1, m.digest()).toString(16);
	}

	public void launch() {
		super.launch();
	}

}
