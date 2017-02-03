import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {

	public List<Schedule> schedules = new ArrayList<Schedule>();
	private ReadFile readFile = new ReadFile(this);
	private Popup popupMenu = new Popup(this);

	private TimeTable timeTable = new TimeTable();

	private Player media1 = new Player(this);
	private Player media2 = new Player(this);
	private Player media3 = new Player(this);
	private Player media4 = new Player(this);

	// private Player media1;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		// ディスプレイサイズを取得
		VBox root = new VBox();
		HBox hbox1 = new HBox();

		root.getChildren().add(createHeadWline());
		ContextMenu menu = popupMenu.createPopupMenu();

		Node tableNode = timeTable.creatTimeTable(this);
		tableNode.setOnMousePressed(e -> {
			if (e.isSecondaryButtonDown()) {
				menu.show(primaryStage, e.getScreenX(), e.getScreenY());
			}
		});
		root.getChildren().add(tableNode);

		timeTable.table.getSelectionModel().select(media1.getPlayIndex());
		// timeTable.table.scrollTo(media1.getPlayIndex());

		hbox1.getChildren().addAll(media1.getPanel(), media2.getPanel(),
				media3.getPanel(), media4.getPanel());
		root.getChildren().add(hbox1);

		/* アクションイベント */
		EventHandler<KeyEvent> sceneKeyFilter = (event) -> {
			System.out.println(event);
			// switch (event.getCode().toString()) {
			// case "S":
			// media1.mediaPlay();
			// break;
			// case "ENTER":
			// media1.mediaPause();
			// break;
			// case "DOWN":
			// media1.pressNext();
			// break;
			// case "UP":
			// media1.pressPrev();
			// break;
			// case "SPACE":
			// if (media1.getMediaPlayer().statusProperty().getValue() ==
			// Status.PLAYING) {
			// if (media1.getPlayerIndex() + 1 > getReadFile()
			// .getMusicFiles().size() - 1) {
			// media1.pressNext();
			// } else {
			// if (getReadFile().getMusicFiles()
			// .get(media1.getPlayerIndex() + 1).getDirectry()
			// .isEmpty()) {
			// media1.pressNext();
			// } else {
			// // フェードアウト
			// fadeOut = new Timer();
			// fadeOut.schedule(new FadeOutTask(), 0, 10);
			// try {
			// Thread.sleep(1000);
			// } catch (Exception e) {
			// }
			// System.out.println("next2");
			// media1.pressNext();
			// media1.mediaPlay();
			// }
			// }
			// } else {
			// if (getReadFile().getMusicFiles()
			// .get(media1.getPlayerIndex()).getDirectry()
			// .isEmpty()) {
			// media1.addPlayerIndex();
			// label2 = setItem(label2, media1.getPlayerIndex());
			// }
			// media1.mediaPlay();
			// }
			//
			// break;
			// case "DIGIT1":
			// if (media1.samplerButton.isSelected()) {
			// media1.mediaPlay();
			// }
			// break;
			// case "DIGIT2":
			// if (media2.samplerButton.isSelected()) {
			// media2.mediaPlay();
			// }
			// break;
			// case "DIGIT3":
			// if (media3.samplerButton.isSelected()) {
			// media3.mediaPlay();
			// }
			// break;
			// case "DIGIT4":
			// if (media4.samplerButton.isSelected()) {
			// media4.mediaPlay();
			// }
			// break;
			// }
		};
		root.addEventFilter(KeyEvent.KEY_PRESSED, sceneKeyFilter);

		Scene scene = new Scene(root, 1200, 800);

		primaryStage.setTitle("使用曲再生ソフト");
		primaryStage.setScene(scene);
		primaryStage.show();
		// media1.mediaPlay();
		// media2.getMediaPlayer().setVolume(0.1);
	}

	private Node createHeadWline() {
		AnchorPane pane = new AnchorPane();

		Button timeSet = new Button("時計合わせ");
		timeSet.setFocusTraversable(false);
		timeSet.setFont(Font.font(null, FontWeight.BLACK, 24));
		timeSet.setMinSize(100, 90);

		Label programTime = new Label("TIME");
		programTime.setFont(Font.font(null, FontWeight.BLACK, 64));
		programTime.setTextFill(Color.GREEN);
		Timecode timecode = new Timecode(programTime);
		AnchorPane.setLeftAnchor(programTime, 150.0);

		Button save = new Button("SAVE");
		save.setFocusTraversable(false);
		save.setFont(Font.font(null, FontWeight.BLACK, 24));
		save.setMinSize(100, 90);
		AnchorPane.setLeftAnchor(save, 450.0);

		pane.getChildren().addAll(timeSet, programTime, save);

		timeSet.addEventHandler(MouseEvent.MOUSE_CLICKED,
				new EventHandler<MouseEvent>() {
					public void handle(MouseEvent e) {
						// 新しいウインドウを生成
						Label correctionBeforTime = new Label("befor time");
						Label correctionAfterTime = new Label("After time");
						new Timecode(correctionBeforTime, "補正前 : ");
						Timecode correctionTimecode = new Timecode(
								correctionAfterTime, "補正後 : ", timecode
										.getCorrectionTime());
						correctionBeforTime.setFont(Font.font(null,
								FontWeight.BLACK, 24));
						correctionBeforTime.setTextFill(Color.GREEN);

						correctionAfterTime.setFont(Font.font(null,
								FontWeight.BLACK, 24));
						correctionAfterTime.setTextFill(Color.RED);

						Stage newStage = new Stage();
						newStage.setTitle("時計合わせ");
						// 新しいウインドウ内に配置するコンテンツを生成
						HBox hbox = new HBox();
						VBox vbox = new VBox();
						Label correctionTimeLabel = new Label("調整時間\n(ミリ秒)");

						TextField textField = new TextField(String
								.valueOf(timecode.getCorrectionTime()));

						Button registrationButton = new Button("登録");
						registrationButton.addEventHandler(
								MouseEvent.MOUSE_CLICKED,
								new EventHandler<MouseEvent>() {
									public void handle(MouseEvent e) {
										timecode.setCorrectionTime(Integer
												.parseInt(textField.getText()));
										correctionTimecode
												.setCorrectionTime(Integer
														.parseInt(textField
																.getText()));
									}
								});

						hbox.getChildren().addAll(correctionTimeLabel,
								textField, registrationButton);
						vbox.getChildren().addAll(hbox, correctionBeforTime,
								correctionAfterTime);
						Scene scene = new Scene(vbox, 250, 100);
						newStage.initModality(Modality.APPLICATION_MODAL);
						newStage.setScene(scene);

						// 新しいウインドウを表示
						newStage.show();
					}
				});

		save.addEventHandler(MouseEvent.MOUSE_CLICKED,
				new EventHandler<MouseEvent>() {
					public void handle(MouseEvent e) {
						printTimetable();
					}
				});

		return pane;
	}

	public Player getMedia(int num) {
		switch (num) {
		case 0:
			return media1;
		case 1:
			return media2;
		case 2:
			return media3;
		case 3:
			return media4;
		default:
			return null;
		}
	}

	public TimeTable getTimeTable() {
		return timeTable;
	}

	public List<Schedule> getSchedules() {
		return schedules;
	}

	public ReadFile getReadFile() {
		return readFile;
	}

	private void printTimetable() {
		try {
			// 出力先を作成する
			FileOutputStream  fw = new FileOutputStream (readFile.getDirectory()
					+ "/test.csv", false);
			OutputStreamWriter osw = new OutputStreamWriter(fw, "SJIS");
			BufferedWriter bw = new BufferedWriter(osw);

			// 内容を指定する
			for (int i = 0; i < schedules.size(); i++) {
				String directryPath = schedules.get(i).getDirectry();
				String directry = directryPath.replace(readFile.getDirectory()
						+ "/", "");
				if (directry == directryPath || directry == "") {
				} else {
					bw.write(directry);
				}
				bw.write(",");
				bw.write(schedules.get(i).getFileName() + ",");
				bw.write(schedules.get(i).getStartTime().getHour() + ",");
				bw.write(schedules.get(i).getStartTime().getMinute() + ",");
				bw.write(schedules.get(i).getStartTime().getSecond() + ",");
				bw.write(schedules.get(i).getAllotTime().getMinute() + ",");
				bw.write(schedules.get(i).getAllotTime().getSecond() + ",");
				bw.newLine();
			}

			// ファイルに書き出す
			bw.close();

			// 終了メッセージを画面に出力する
			System.out.println("出力が完了しました。");

		} catch (IOException ex) {
			// 例外時処理
			ex.printStackTrace();
		}

	}
}
