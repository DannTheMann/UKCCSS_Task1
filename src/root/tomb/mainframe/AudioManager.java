package root.tomb.mainframe;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class AudioManager {

	public static final AudioManager am = new AudioManager();

	private MediaPlayer mediaPlayer;
	private Map<String, Media> songs;
	private boolean playing = true;
	private double volume;
	private boolean muted;
	private boolean failed;

	private AudioManager() {
		try {
			songs = new TreeMap<>();
			Out.out.println("Loading music from: " + new File(Out.RESOURCES_DIRECTORY + File.separator + "main.mp3").getAbsolutePath());
			songs.put("main", new Media(new File(Out.RESOURCES_DIRECTORY + File.separator + "main.mp3").toURI().toString()));
			volume = .05;
			muted = false;
			failed = false;
		} catch (Exception e) {
			GraphicsUtil.createTextField("Warning", "Music player error", "We can continue, but if you want to fix "
					+ "this then be sure to install the package 'ffmpeg' on linux distros.", AlertType.WARNING);
			failed = true;
		}
	}

	public void playSong(String trackName) {
		
		if(failed){
			return;
		}
		
		try {
			Out.out.println("Playing music: " + trackName);
			if (songs.containsKey(trackName)) {
				Media hit = songs.get(trackName);
				startSong(hit);
			} else {
				startSong(songs.get("main"));
			}
		} catch (Exception e) {
			GraphicsUtil.createTextField("Warning", "Music player error", "We can continue, but if you want to fix "
					+ "this then be sure to install the package 'ffmpeg' on linux distros.", AlertType.WARNING);
			failed = true;
		}

	}

	private void startSong(Media media) {

		if (mediaPlayer != null || playing == false) {
			Out.out.println("Error, couldn't play music.");
			return;
		}

		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setVolume(volume);
		mediaPlayer.play();
		Out.out.println("Playing music now. Volume: " + volume);
		mediaPlayer.setOnEndOfMedia(new Runnable() {
			@Override
			public void run() {
				mediaPlayer.seek(Duration.ZERO);
			}
		});
	}

	public void pause() {
		mediaPlayer.pause();
	}

	public boolean loadSong(String track) {

		if (track == null || track.equalsIgnoreCase("NA"))
			return false;

		try {
			String trackName = new File(Out.RESOURCES_DIRECTORY + File.separator + track).toURI().toString();
			Media media = new Media(trackName);
			songs.put(track, media);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean isMuted() {
		return mediaPlayer.isMute();
	}

	public void hasFailed(boolean mute) {
		if(failed){
			return;
		}
		mediaPlayer.setMute(mute);
		muted = mute;
	}

	public void setVolume(double volume) {
		mediaPlayer.setVolume(volume);
		this.volume = volume;
	}

}
