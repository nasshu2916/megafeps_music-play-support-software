package FXsample;

import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.util.Duration;

/*
 * 時計に関する関数
 * 
 */
public class Timecode {

	private int correctionTime = 000;
	private DecimalFormat dformat = new DecimalFormat("00");
	private Label label;

	public Timecode(Label label) {
		Timeline timer = new Timeline(new KeyFrame(Duration.millis(100),
				new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						LocalTime time = LocalTime.now();
						time = time.plus(correctionTime, ChronoUnit.MILLIS);
						int h = time.getHour(); // 0..23
						int min = time.getMinute(); // 0..59
						int sec = time.getSecond(); // 0..59
						int ms = time.getNano() / 100000000; // 0..999
						label.setText(dformat.format(h) + ":"
								+ dformat.format(min) + ":"
								+ dformat.format(sec) + " " + ms);
						Timecode.this.label = label;
					}
				}));
		timer.setCycleCount(Timeline.INDEFINITE);
		timer.play();
	}

	public int getCorrectionTime() {
		return correctionTime;
	}

	public void setCorrectionTIme(int CorrectionTIme) {
		this.correctionTime = CorrectionTIme;
	}

	public Label getTimeLabel() {
		return label;
	}
}