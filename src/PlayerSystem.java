import java.io.File;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

public class PlayerSystem {
	protected Main main;
	protected MediaPlayer mediaPlayer;
	protected VBox playerPanel = new VBox();
	protected Node spectrum = null; // スペクトル表示ノード
	protected Slider volumeSlider;
	protected ToggleButton samplerButton;

	protected int playerNumber;
	protected int playIndex = 0;
	protected Label fileName;
	protected Media media;

	protected File getFileName() {
		Schedule schedule = main.schedules.get(playIndex);
		return new File(schedule.getDirectry() + "/" + schedule.getFileName());
	}

	//拡張子を取得
	protected static String getSuffix(String fileName) {
		if (fileName == null)
			return null;
		int point = fileName.lastIndexOf(".");
		if (point != -1) {
			return fileName.substring(point + 1);
		}
		return fileName;
	}

	protected void setBorderColor(Color color) {
		playerPanel.setBorder(new Border(new BorderStroke(color, BorderStrokeStyle.SOLID,
				CornerRadii.EMPTY, new BorderWidths(10))));
	}

	protected void mediaPlay() {
		mediaPlayer.play();
		setBorderColor(Color.RED);
	}

	protected void mediaStop() {
		mediaPlayer.stop();
		setBorderColor(Color.GRAY);
	}

	protected void mediaPause() {
		mediaPlayer.pause();
		setBorderColor(Color.BLUE);
	}

	protected void setPlayIndex(int playIndex) {
		this.playIndex = playIndex;
	}

	protected int getPlayIndex() {
		return playIndex;
	}

	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}
	


}