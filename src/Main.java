import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
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
		ContextMenu menu = createPopupMenu();

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
			// .equals("")) {
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
			// .equals("")) {
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

	/**
	 * ポップアップメニューを作成
	 * 
	 * @return 作成したメニュー
	 */
	public ContextMenu createPopupMenu() {
		// メニュー
		ContextMenu menu = new ContextMenu();

		// メニューFileを、一般メニューとして作成
		// 各メニューにはイメージを付与することが可能
		Menu menu1 = new Menu("この番号をプレイヤーにセット");
		MenuItem menu1_2 = new MenuItem("プレイヤー1");
		MenuItem menu1_3 = new MenuItem("プレイヤー2");
		MenuItem menu1_4 = new MenuItem("プレイヤー3");
		MenuItem menu1_5 = new MenuItem("プレイヤー4");
		MenuItem menu2 = new SeparatorMenuItem();
		MenuItem menu3 = new MenuItem("イベント追加");
		MenuItem menu4 = new MenuItem("イベント削除");
		// menu1.setDisable(true);

		menu1.getItems().addAll(menu1_2, menu1_3, menu1_4, menu1_5);

		menu.getItems().addAll(menu1, menu2, menu3, menu4);
		// イベントハンドラはMenuItemに設定
		menu1_2.addEventHandler(ActionEvent.ACTION, e -> {
			media1.setPlayer(timeTable.table.getSelectionModel()
					.getSelectedIndex());
		});
		menu1_3.addEventHandler(ActionEvent.ACTION, e -> {
			media2.setPlayer(timeTable.table.getSelectionModel()
					.getSelectedIndex());
		});
		menu1_4.addEventHandler(ActionEvent.ACTION, e -> {
			media3.setPlayer(timeTable.table.getSelectionModel()
					.getSelectedIndex());
		});
		menu1_5.addEventHandler(ActionEvent.ACTION, e -> {
			media4.setPlayer(timeTable.table.getSelectionModel()
					.getSelectedIndex());
		});
		menu3.addEventHandler(ActionEvent.ACTION, e -> {
			setNewEventWindow();

		});

		menu4.addEventHandler(
				ActionEvent.ACTION,
				e -> {
					schedules.remove(timeTable.table.getSelectionModel()
							.getSelectedIndex());
					timeTable.timeTableDatas.remove(timeTable.table
							.getSelectionModel().getSelectedIndex());
					for (int i = timeTable.table.getSelectionModel()
							.getSelectedIndex(); i < timeTable.timeTableDatas
							.size(); i++) {
						timeTable.timeTableDatas.get(i).setNumber(
								String.valueOf(i + 1));

					}
				});
		return menu;
	}

	private void setNewEventWindow() {
		Stage newEvent = new Stage();
		HBox hboxDirectory = new HBox();
		HBox hboxFile = new HBox();
		VBox vbox = new VBox();
		Scene scene = new Scene(vbox, 300, 200);

		Label directoryLabel = new Label("ディレクトリ");
		TextField directoryTextField = new TextField();

		Label FileLabel = new Label("ファイル名");
		TextField FileTextField = new TextField();

		Button registrationButton = new Button("登録");

		EventHandler<ActionEvent> registrationHandler = (e) -> {
			schedules.add(timeTable.table.getSelectionModel()
					.getSelectedIndex(), new Schedule(directoryTextField.getText(),
					FileTextField.getText(), 10, 10, 10, 10, 10));
			timeTable.timeTableDatas.add(timeTable.table.getSelectionModel()
					.getSelectedIndex(), timeTable.setTimeTableData(String
					.valueOf(timeTable.table.getSelectionModel()
							.getSelectedIndex() + 1), directoryTextField.getText(),
					FileTextField.getText(), "10", "00"));
			for (int i = timeTable.table.getSelectionModel()// 番号を修正
					.getSelectedIndex(); i < timeTable.timeTableDatas.size(); i++) {
				timeTable.timeTableDatas.get(i)
						.setNumber(String.valueOf(i + 1));
			}
			newEvent.close();
		};
		registrationButton.addEventHandler(ActionEvent.ACTION,
				registrationHandler);

		hboxDirectory.getChildren().addAll(directoryLabel, directoryTextField);
		hboxFile.getChildren().addAll(FileLabel, FileTextField);
		vbox.getChildren().addAll(hboxDirectory,hboxFile,registrationButton);
		newEvent.setScene(scene);
		newEvent.initModality(Modality.APPLICATION_MODAL);
		newEvent.show();

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

		pane.getChildren().addAll(timeSet, programTime);

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

		return pane;
	}

}