import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
	private Popup popupMenu = new Popup(this);
	public List<Schedule> schedules = new ArrayList<Schedule>();
	private ReadFile readFile;
	// private ReadFile readFile = new ReadFile(this);

	private TimeTable timeTable = new TimeTable();

	private Player media1 = new Player(this);
	private Player media2 = new Player(this);
	private Player media3 = new Player(this);
	private Player media4 = new Player(this);

	private Scene mainscene;
	private Timecode timecode;
	private Timer fadeOut;

	// private Player media1;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage mainStage) {
		// ディスプレイサイズを取得
		VBox root = new VBox();
		HBox hbox1 = new HBox();

		root.getChildren().add(createMenuBar());

		root.getChildren().add(createHeadWline());
		ContextMenu menu = popupMenu.createPopupMenu();
		ContextMenu firstMenu = popupMenu.firstPopupMenu();// スケジュールが無い時用のポップアップメニュー
		Node tableNode = timeTable.creatTimeTable(this);
		tableNode.setOnMousePressed(e -> {
			if (e.isSecondaryButtonDown()) {
				int selectNum = getTimeTable().table.getSelectionModel().getSelectedIndex();
				popupMenu.setSelectNum(selectNum);
				if (selectNum >= 0) {
					menu.show(mainStage, e.getScreenX(), e.getScreenY());
				} else {
					firstMenu.show(mainStage, e.getScreenX(), e.getScreenY());
				}
			}
		});
		root.getChildren().add(tableNode);

		hbox1.getChildren().addAll(media1.getPlayerPanel(), media2.getPlayerPanel(),
				media3.getPlayerPanel(), media4.getPlayerPanel());
		root.getChildren().add(hbox1);

		mainscene = new Scene(root, 1200, 800);

		mainStage.setTitle("使用曲再生ソフト");
		mainStage.setScene(mainscene);
		mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				System.out.println("handle " + t.getEventType());
				// t.consume();
			}
		});
		mainStage.show();
		/* アクションイベント */
		EventHandler<KeyEvent> sceneKeyFilter = (event) -> {
			System.out.println(event);
			onKey(event);
		};
		mainStage.addEventFilter(KeyEvent.KEY_PRESSED, sceneKeyFilter);

	}

	private Node createHeadWline() {

		AnchorPane pane = new AnchorPane();

		Label programTime = new Label("TIME");
		programTime.setFont(Font.font(null, FontWeight.BLACK, 64));
		programTime.setTextFill(Color.GREEN);
		timecode = new Timecode(programTime);

		pane.getChildren().addAll(programTime);

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

	private void exportTimetable(File file) {
		try {
			// 出力先を作成する
			FileOutputStream fw = new FileOutputStream(file, false);
			OutputStreamWriter osw = new OutputStreamWriter(fw, "SJIS");
			BufferedWriter bw = new BufferedWriter(osw);

			// 内容を指定する
			for (int i = 0; i < schedules.size(); i++) {
				String directryPath = schedules.get(i).getDirectry();
				String directry = directryPath.replace(readFile.getDirectory() + "/", "");
				System.out.println(directry + " , " + directryPath);
				if (directry.equals(directryPath) || directry == "") {
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
				bw.write(schedules.get(i).getRemarks() + ",");
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

	/**
	 * メニューを作成
	 *
	 * @return 作成したメニュー
	 */
	private Node createMenuBar() {
		// メニュー
		MenuBar menuBar = new MenuBar();
		// メニューFileを、一般メニューとして作成
		// 各メニューにはイメージを付与することが可能
		Menu menu1_1 = new Menu("終了");
		MenuItem menu1_2 = new SeparatorMenuItem();
		MenuItem menu1_3 = new MenuItem("終了");

		menu1_1.getItems().addAll(menu1_2, menu1_3);

		// メニューEditを、チェックメニューで作成
		Menu menu2_1 = new Menu("ファイル");
		MenuItem menu2_2 = new MenuItem("開く");
		MenuItem menu2_3 = new MenuItem("保存");
		MenuItem menu2_4 = new MenuItem("名前を付けて保存");
		MenuItem menu2_5 = new MenuItem("閉じる");
		MenuItem menu2_6 = new SeparatorMenuItem();
		MenuItem menu2_7 = new MenuItem("時計合わせ");
		// menu2_5.setDisable(true);
		menu2_1.getItems().addAll(menu2_2, menu2_3, menu2_4, menu2_5, menu2_6, menu2_7);

		// メニューViewModeを、ラジオメニューで作成
		Menu menu3_1 = new Menu("Help");
		MenuItem menu3_2 = new MenuItem("help");
		menu3_2.setDisable(true);
		menu3_1.getItems().add(menu3_2);

		// メニューにイベントハンドラを登録
		menu1_3.addEventHandler(ActionEvent.ACTION, e -> {
			System.out.println(menu1_3.getText());
			Platform.exit();// 終了させる
		});

		// 開く
		menu2_2.addEventHandler(ActionEvent.ACTION, e -> {
			readFile = new ReadFile(this);

			DecimalFormat dformat = new DecimalFormat("00");

			for (int i = 0; i < schedules.size(); i++) {
				timeTable.timeTableDatas
						.add(timeTable.setTimeTableData(dformat.format(i + 1), schedules.get(i)));
			}

		});

		// 保存
		menu2_3.addEventHandler(ActionEvent.ACTION, e -> {
			exportTimetable(readFile.getProgramDataFile());
		});

		// 名前をつけて保存
		menu2_4.addEventHandler(ActionEvent.ACTION, e -> {
			saveAsFile();
		});

		// 閉じる
		menu2_5.addEventHandler(ActionEvent.ACTION, e -> {
			closeTimeTeble();
		});

		// 時計合わせ
		menu2_7.addEventHandler(ActionEvent.ACTION, e -> {
			setClockWindow();
		});

		// メニューを登録
		menuBar.getMenus().addAll(menu1_1, menu2_1, menu3_1);
		menuBar.setUseSystemMenuBar(true);
		return menuBar;
	}

	private void setClockWindow() {
		// 新しいウインドウを生成
		Label correctionBeforTime = new Label("befor time");
		Label correctionAfterTime = new Label("After time");
		new Timecode(correctionBeforTime, "補正前 : ");
		Timecode correctionTimecode = new Timecode(correctionAfterTime, "補正後 : ",
				timecode.getCorrectionTime());
		correctionBeforTime.setFont(Font.font(null, FontWeight.BLACK, 24));
		correctionBeforTime.setTextFill(Color.GREEN);

		correctionAfterTime.setFont(Font.font(null, FontWeight.BLACK, 24));
		correctionAfterTime.setTextFill(Color.RED);

		Stage newStage = new Stage();
		newStage.setTitle("時計合わせ");
		// 新しいウインドウ内に配置するコンテンツを生成
		HBox hbox = new HBox();
		VBox vbox = new VBox();
		Label correctionTimeLabel = new Label("調整時間\n(ミリ秒)");

		TextField textField = new TextField(String.valueOf(timecode.getCorrectionTime()));

		Button registrationButton = new Button("登録");
		registrationButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
				new EventHandler<MouseEvent>() {
					public void handle(MouseEvent e) {
						timecode.setCorrectionTime(Integer.parseInt(textField.getText()));
						correctionTimecode.setCorrectionTime(Integer.parseInt(textField.getText()));
					}
				});

		hbox.getChildren().addAll(correctionTimeLabel, textField, registrationButton);
		vbox.getChildren().addAll(hbox, correctionBeforTime, correctionAfterTime);
		Scene scene = new Scene(vbox, 250, 100);
		newStage.initModality(Modality.APPLICATION_MODAL);
		newStage.setScene(scene);

		// 新しいウインドウを表示
		newStage.show();
	}

	private void saveAsFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("名前をつけて保存");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("タイムテーブル", "*.csv"));
		File selectedFile = fileChooser.showSaveDialog(null);
		if (selectedFile != null) {
			System.out.println(selectedFile);
			exportTimetable(selectedFile);
		}
	}

	private void closeTimeTeble() {
		readFile = null;
		schedules.clear();
		timeTable.timeTableDatas.clear();
	}

	private void onKey(KeyEvent event) {
		switch (event.getCode().toString()) {
		case "S":
			media1.mediaPlay();
			break;
		case "ENTER":
			media1.mediaPause();
			break;
		case "DOWN":
			media1.pressNext();
			break;
		case "UP":
			media1.pressPrev();
			break;
		case "SPACE":
			// timeTable.table.getSelectionModel().select(media1.getPlayIndex());
			// timeTable.table.scrollTo(media1.getPlayIndex());
			if (media1.getMediaPlayer().statusProperty().getValue() == Status.PLAYING) {
				if (media1.getPlayIndex() + 1 > schedules.size() - 1) {
					media1.pressNext();
				} else {
					if (schedules.get(media1.getPlayIndex() + 1).getDirectry().isEmpty()) {
						media1.pressNext();
					} else { // フェードアウト
						fadeOut = new Timer();
						fadeOut.schedule(new FadeOutTask(), 0, 10);
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
						}
						System.out.println("next2");
						media1.pressNext();
						media1.mediaPlay();
					}
				}
			} else {
				if (schedules.get(media1.getPlayIndex()).getDirectry().isEmpty()) {
					media1.setPlayIndex(media1.getPlayIndex() + 1);
					// label2 = setItem(label2, media1.getPlayIndex());
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
	}

	class FadeOutTask extends TimerTask {
		double volume = 1.000;

		public void run() {
			volume -= 0.010;
			if (volume < 0.050) {
				fadeOut.cancel();
			} else {
				media1.getMediaPlayer().setVolume(volume);
			}
		}
	}
}
