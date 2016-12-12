package FXsample;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {
	private ReadFile readFile = new ReadFile();
	private TimeTable timeTable = new TimeTable();
	private PlayerSystem media1;
	private PlayerSystem media2;
	Label label2 = new Label("現項目:");
	private Timer fadeOut;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		// ディスプレイサイズを取得
		VBox root = new VBox();
		HBox hbox1 = new HBox();

		root.getChildren().add(createWline());

		root.getChildren().add(timeTable.createtimeTable(this));
		media1 = new PlayerSystem(this, 0);
		media2 = new PlayerSystem(this, 1);
		PlayerSystem media3 = new PlayerSystem(this, 2);
		PlayerSystem media4 = new PlayerSystem(this, 3);
		hbox1.getChildren().addAll(media1.getPanel(), media2.getPanel(),
				media3.getPanel(), media4.getPanel());
		root.getChildren().add(hbox1);

		/* アクションイベント */
		EventHandler<KeyEvent> sceneKeyFilter = (event) -> {
			System.out.println(event);
			switch (event.getCode().toString()) {
			case "S":
				media1.mediaPlay();
				break;
			case "ENTER":
				media1.mediaPause();
				break;
			case "DOWN":
				media1.pressNext();
				media2.pressNext();
				break;
			case "UP":
				media1.pressPrev();
				media2.pressPrev();
				break;
			case "SPACE":
				if (media1.getMediaPlayer().statusProperty().getValue() == Status.PLAYING) {
					if (media1.getPlayerIndex() + 1 > getReadFile()
							.getMusicFiles().size() - 1) {
						media1.pressNext();
						media2.pressNext();
						media2.samplerButton.setSelected(true);
					} else {
						if (getReadFile().getMusicFiles()
								.get(media1.getPlayerIndex() + 1).getDirectry()
								.equals("")) {
							media1.pressNext();
							media2.pressNext();
							media2.samplerButton.setSelected(true);
						} else {
							// フェードアウト
							fadeOut = new Timer();
							fadeOut.schedule(new FadeOutTask(), 0, 10);
							try {
								Thread.sleep(1000);
							} catch (Exception e) {
								// TODO: handle exception
							}
							System.out.println("next2");
							media1.pressNext();
							media2.pressNext();
							media2.samplerButton.setSelected(true);
							media1.mediaPlay();
						}
					}
				} else {
					if (getReadFile().getMusicFiles()
							.get(media1.getPlayerIndex()).getDirectry()
							.equals("")) {
						media1.addPlayerIndex();
						media2.addPlayerIndex();
						label2 = setItem(label2, media1.getPlayerIndex());
					}
					

					media1.mediaPlay();
				}

				break;
			case "DIGIT1":
				if (media1.samplerButton.isSelected()) {
					media1.mediaPlay();
				}
				break;
			case "DIGIT2":
				if (media2.samplerButton.isSelected()) {
					media2.mediaPlay();
				}
				break;
			case "DIGIT3":
				if (media3.samplerButton.isSelected()) {
					media3.mediaPlay();
				}
				break;
			case "DIGIT4":
				if (media4.samplerButton.isSelected()) {
					media4.mediaPlay();
				}
				break;
			}
		};
		root.addEventFilter(KeyEvent.KEY_PRESSED, sceneKeyFilter);

		Scene scene = new Scene(root, 1200, 800);

		primaryStage.setTitle("VideoPlay");
		primaryStage.setScene(scene);
		primaryStage.show();
		// media1.mediaPlay();
		// media2.getMediaPlayer().setVolume(0.1);
	}

	private Node createWline() {
		AnchorPane pane = new AnchorPane();

		Button timeSet = new Button("時計合わせ");
		timeSet.setFocusTraversable(false);
		timeSet.setFont(Font.font(null, FontWeight.BLACK, 24));
		timeSet.setMinSize(100, 90);

		Label label = new Label("TIME");
		label.setFont(Font.font(null, FontWeight.BLACK, 64));
		label.setTextFill(Color.GREEN);
		Timecode timecode = new Timecode(label);
		AnchorPane.setLeftAnchor(label, 150.0);

		label2.setFont(Font.font(null, FontWeight.BOLD, 24));
		AnchorPane.setLeftAnchor(label2, 450.0);
		AnchorPane.setTopAnchor(label2, 10.0);

		Button next = new Button("TAKE");
		next.setFont(Font.font(null, FontWeight.BLACK, 34));
		next.setMinSize(200, 90);
		AnchorPane.setRightAnchor(next, 0.0);

		pane.getChildren().addAll(timeSet, label, label2, next);

		timeSet.addEventHandler(MouseEvent.MOUSE_CLICKED,
				new EventHandler<MouseEvent>() {
					public void handle(MouseEvent e) {
						// 新しいウインドウを生成
						Stage newStage = new Stage();
						// 新しいウインドウ内に配置するコンテンツを生成
						HBox hbox = new HBox();
						Label label1 = new Label("調整時間\n(ミリ秒)");

						TextField textField = new TextField(String
								.valueOf(timecode.getCorrectionTime()));

						Button registrationButton = new Button("登録");
						registrationButton.addEventHandler(
								MouseEvent.MOUSE_CLICKED,
								new EventHandler<MouseEvent>() {
									public void handle(MouseEvent e) {
										timecode.setCorrectionTIme(Integer
												.parseInt(textField.getText()));
									}
								});

						hbox.getChildren().addAll(label1, textField,
								registrationButton);
						Scene scene = new Scene(hbox, 250, 100);

						newStage.setScene(scene);

						// 新しいウインドウを表示
						newStage.show();
					}
				});

		return pane;
	}

	public TimeTable getTimeTable() {
		return timeTable;
	}

	public PlayerSystem getPlayerSystemMedia1() {
		return media1;
	}

	public ReadFile getReadFile() {
		return readFile;
	}

	public Label setItem(Label label, int playerIndex) {// 現項目 次項目を登録
		getTimeTable().table.getSelectionModel().select(playerIndex);
		getTimeTable().table.scrollTo(playerIndex);
		DecimalFormat dformat = new DecimalFormat("00");
		if (playerIndex > getReadFile().getMusicFiles().size() - 2) {
			label.setText("現項目:"
					+ getReadFile().getMusicFiles().get(playerIndex)
							.getFileName()
					+ "  ("
					+ getReadFile().getMusicFiles().get(playerIndex)
							.getAllottedMinute()
					+ ":"
					+ dformat.format(getReadFile().getMusicFiles()
							.get(playerIndex).getAllottedSecond()) + ")\n次項目:");
		} else {
			label.setText("現項目:"
					+ getReadFile().getMusicFiles().get(playerIndex)
							.getFileName()
					+ "  ("
					+ getReadFile().getMusicFiles().get(playerIndex)
							.getAllottedMinute()
					+ ":"
					+ dformat.format(getReadFile().getMusicFiles()
							.get(playerIndex).getAllottedSecond())
					+ ")\n次項目:"
					+ getReadFile().getMusicFiles().get(playerIndex + 1)
							.getFileName()
					+ "  ("
					+ getReadFile().getMusicFiles().get(playerIndex + 1)
							.getAllottedMinute()
					+ ":"
					+ dformat.format(getReadFile().getMusicFiles()
							.get(playerIndex + 1).getAllottedSecond()) + ")");
		}
		return label;
	}

	class FadeOutTask extends TimerTask {
		double volume = 1.000;

		public void run() {
			volume -= 0.010;
			if (volume < 0.010) {
				fadeOut.cancel();
			} else {
				media1.getMediaPlayer().setVolume(volume);
				// volumeSlider.setValue(volume);
				// System.out.println(volume);
			}
		}
	}
}
