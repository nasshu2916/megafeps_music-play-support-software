import java.io.File;
import java.text.DecimalFormat;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Popup {

	Main main;
	private DecimalFormat dformat = new DecimalFormat("00");

	Popup(Main main) {
		this.main = main;
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
		MenuItem menu3 = new MenuItem("イベントの追加");
		MenuItem menu4 = new MenuItem("イベントの削除");
		MenuItem menu5 = new MenuItem("イベントの変更");
		// menu1.setDisable(true);

		menu1.getItems().addAll(menu1_2, menu1_3, menu1_4, menu1_5);

		menu.getItems().addAll(menu1, menu2, menu3, menu4, menu5);
		// イベントハンドラはMenuItemに設定
		menu1_2.addEventHandler(
				ActionEvent.ACTION,
				e -> {
					main.getMedia(0).setPlayer(
							main.getTimeTable().table.getSelectionModel()
									.getSelectedIndex());
				});
		menu1_3.addEventHandler(
				ActionEvent.ACTION,
				e -> {
					main.getMedia(1).setPlayer(
							main.getTimeTable().table.getSelectionModel()
									.getSelectedIndex());
				});
		menu1_4.addEventHandler(
				ActionEvent.ACTION,
				e -> {
					main.getMedia(2).setPlayer(
							main.getTimeTable().table.getSelectionModel()
									.getSelectedIndex());
				});
		menu1_5.addEventHandler(
				ActionEvent.ACTION,
				e -> {
					main.getMedia(3).setPlayer(
							main.getTimeTable().table.getSelectionModel()
									.getSelectedIndex());
				});
		menu3.addEventHandler(ActionEvent.ACTION, e -> {
			setNewEventWindow();
		});

		menu4.addEventHandler(
				ActionEvent.ACTION,
				e -> {
					main.getSchedules().remove(
							main.getTimeTable().table.getSelectionModel()
									.getSelectedIndex());
					main.getTimeTable().timeTableDatas.remove(main
							.getTimeTable().table.getSelectionModel()
							.getSelectedIndex());
					for (int i = main.getTimeTable().table.getSelectionModel()
							.getSelectedIndex(); i < main.getTimeTable().timeTableDatas
							.size(); i++) {
						main.getTimeTable().timeTableDatas.get(i).setNumber(
								dformat.format(i + 1));

					}
				});

		menu5.addEventHandler(ActionEvent.ACTION, e -> {
			chengeEventData();
		});
		return menu;
	}

	private void setNewEventWindow() {

		FileChooser fc = new FileChooser();
		fc.setTitle("ファイルを選択してください");
		fc.getExtensionFilters().addAll(
				new ExtensionFilter("support Files", "*.wav", "*.mp3", "*.aac",
						"*.mp4", "*.m4a", "*.flv"),
				new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
				new ExtensionFilter("Video Files", "*.mp4", "*.m4a", "*.flv"),
				new ExtensionFilter("All Files", "*.*"));
		fc.setInitialDirectory(new File(main.getReadFile().getDirectory()));

		File file = fc.showOpenDialog(new Stage());

		if (file == null) {
			// ファイルが選択されてない場合
			return;
		}

		Stage newEvent = new Stage();
		HBox hboxDirectory = new HBox(10);
		HBox hboxFile = new HBox(10);
		HBox hboxStartTime = new HBox(10);
		HBox hboxAllotTime = new HBox(10);
		HBox hboxBottumSelect = new HBox(10);
		VBox vbox = new VBox(5);
		Scene scene = new Scene(vbox, 300, 200);

		Label directoryLabel = new Label("ディレクトリ\t");
		Label fileLabel = new Label("ファイル名\t");

		TextField directoryTextField = new TextField(file.getParent());
		TextField fileTextField = new TextField(file.getName());

		Label startTimeLabel = new Label("開始時間\t");
		Label allotTimeLabel = new Label("持ち時間\t");
		final TextField startTimeTextField = new TextField() {
			@Override
			public void replaceText(int start, int end, String text) {
				if (!text.matches("[a-z, A-Z]")) {
					super.replaceText(start, end, text);
				}
			}

			@Override
			public void replaceSelection(String text) {
				if (!text.matches("[a-z, A-Z]")) {
					super.replaceSelection(text);
				}
			}
		};
		startTimeTextField.setPrefWidth(100);

		final TextField allotTimeTextField = new TextField() {
			@Override
			public void replaceText(int start, int end, String text) {
				if (!text.matches("[a-z, A-Z]")) {
					super.replaceText(start, end, text);
				}
			}

			@Override
			public void replaceSelection(String text) {
				if (!text.matches("[a-z, A-Z]")) {
					super.replaceSelection(text);
				}
			}
		};
		allotTimeTextField.setPrefWidth(100);

		Button fileChangeButton = new Button("ファイル変更");
		Button registrationButton = new Button("登録");
		Button CancelButton = new Button("キャンセル");

		EventHandler<ActionEvent> fileChangeHandler = (e) -> {

			File f = fc.showOpenDialog(new Stage());
			if (f == null) {
				return;
			}
			directoryTextField.setText(f.getParent());
			fileTextField.setText(f.getName());
		};
		fileChangeButton.addEventHandler(ActionEvent.ACTION, fileChangeHandler);

		EventHandler<ActionEvent> registrationHandler = (e) -> {
			int startTime, startSecond, startMinute, startHour;
			if (startTimeTextField.getText().isEmpty()) {
				startTime = 0;
			} else {
				startTime = Integer.parseInt(startTimeTextField.getText());
			}
			startSecond = startTime % 100;
			startMinute = startTime / 100 % 100;
			startHour = startTime / 10000;

			int allotTime, allotSecond, allotMinute;
			if (allotTimeTextField.getText().isEmpty()) {
				allotTime = 0;
			} else {
				allotTime = Integer.parseInt(allotTimeTextField.getText());
			}
			allotSecond = allotTime % 100;
			allotMinute = allotTime / 100;

			main.getSchedules().add(
					main.getTimeTable().table.getSelectionModel()
							.getSelectedIndex(),
					new Schedule(directoryTextField.getText(), fileTextField
							.getText(), startHour, startMinute, startSecond,
							allotMinute, allotSecond));
			main.getTimeTable().timeTableDatas.add(
					main.getTimeTable().table.getSelectionModel()
							.getSelectedIndex(),
					main.getTimeTable()
							.setTimeTableData(
									dformat.format(main.getTimeTable().table
											.getSelectionModel()
											.getSelectedIndex() + 1),
									directoryTextField.getText(),
									fileTextField.getText(),
									String.valueOf(startHour) + ":"
											+ dformat.format(startMinute) + ":"
											+ dformat.format(startSecond),
									String.valueOf(allotMinute) + ":"
											+ dformat.format(allotSecond)));
			for (int i = main.getTimeTable().table.getSelectionModel()// 番号を修正
					.getSelectedIndex(); i < main.getTimeTable().timeTableDatas
					.size(); i++) {
				main.getTimeTable().timeTableDatas.get(i).setNumber(
						dformat.format(i + 1));
			}
			newEvent.close();
		};
		registrationButton.addEventHandler(ActionEvent.ACTION,
				registrationHandler);

		EventHandler<ActionEvent> CancelButtonHandler = (e) -> {
			newEvent.close();
		};
		CancelButton.addEventHandler(ActionEvent.ACTION, CancelButtonHandler);

		hboxDirectory.getChildren().addAll(directoryLabel, directoryTextField);
		hboxFile.getChildren().addAll(fileLabel, fileTextField);
		hboxStartTime.getChildren().addAll(startTimeLabel, startTimeTextField);
		hboxAllotTime.getChildren().addAll(allotTimeLabel, allotTimeTextField);
		hboxBottumSelect.getChildren().addAll(registrationButton, CancelButton);
		vbox.getChildren().addAll(hboxDirectory, hboxFile, fileChangeButton,
				hboxStartTime, hboxAllotTime, hboxBottumSelect);
		newEvent.setScene(scene);
		newEvent.initModality(Modality.APPLICATION_MODAL);
		newEvent.show();
	}

	private void chengeEventData() {
		FileChooser fc = new FileChooser();
		fc.setTitle("ファイルを選択してください");
		fc.getExtensionFilters().addAll(
				new ExtensionFilter("support Files", "*.wav", "*.mp3", "*.aac",
						"*.mp4", "*.m4a", "*.flv"),
				new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
				new ExtensionFilter("Video Files", "*.mp4", "*.m4a", "*.flv"),
				new ExtensionFilter("All Files", "*.*"));

		Stage newEvent = new Stage();
		HBox hboxDirectory = new HBox(10);
		HBox hboxFile = new HBox(10);
		HBox hboxStartTime = new HBox(10);
		HBox hboxAllotTime = new HBox(10);
		HBox hboxBottumSelect = new HBox(10);
		VBox vbox = new VBox(5);
		Scene scene = new Scene(vbox, 300, 200);

		Label directoryLabel = new Label("ディレクトリ\t");
		Label fileLabel = new Label("ファイル名\t");

		TextField directoryTextField = new TextField(
				main.getTimeTable().timeTableDatas.get(
						main.getTimeTable().table.getSelectionModel()
								.getSelectedIndex()).getElement2_1());
		TextField fileTextField = new TextField(
				main.getTimeTable().timeTableDatas.get(
						main.getTimeTable().table.getSelectionModel()
								.getSelectedIndex()).getElement2_2());

		Label startTimeLabel = new Label("開始時間\t");
		Label allotTimeLabel = new Label("持ち時間\t");

		final TextField startTimeTextField = new TextField(
				String.valueOf(main
						.getSchedules()
						.get(main.getTimeTable().table.getSelectionModel()
								.getSelectedIndex()).getStartTime().getHour())
						+ dformat.format(main
								.getSchedules()
								.get(main.getTimeTable().table
										.getSelectionModel().getSelectedIndex())
								.getStartTime().getMinute())
						+ dformat.format(main
								.getSchedules()
								.get(main.getTimeTable().table
										.getSelectionModel().getSelectedIndex())
								.getStartTime().getSecond())) {
			@Override
			public void replaceText(int start, int end, String text) {
				if (!text.matches("[a-z, A-Z]")) {
					super.replaceText(start, end, text);
				}
			}

			@Override
			public void replaceSelection(String text) {
				if (!text.matches("[a-z, A-Z]")) {
					super.replaceSelection(text);
				}
			}
		};
		startTimeTextField.setPrefWidth(100);

		final TextField allotTimeTextField = new TextField(
				String.valueOf(main
						.getSchedules()
						.get(main.getTimeTable().table.getSelectionModel()
								.getSelectedIndex()).getAllotTime().getMinute())
						+ dformat.format(main
								.getSchedules()
								.get(main.getTimeTable().table
										.getSelectionModel().getSelectedIndex())
								.getAllotTime().getSecond())) {
			@Override
			public void replaceText(int start, int end, String text) {
				if (!text.matches("[a-z, A-Z]")) {
					super.replaceText(start, end, text);
				}
			}

			@Override
			public void replaceSelection(String text) {
				if (!text.matches("[a-z, A-Z]")) {
					super.replaceSelection(text);
				}
			}
		};
		allotTimeTextField.setPrefWidth(100);

		Button fileChangeButton = new Button("ファイル変更");
		Button registrationButton = new Button("登録");
		Button CancelButton = new Button("キャンセル");

		EventHandler<ActionEvent> fileChangeHandler = (e) -> {

			File f = fc.showOpenDialog(new Stage());
			if (f == null) {
				return;
			}

			directoryTextField.setText(f.getParent());
			fileTextField.setText(f.getName());
		};
		fileChangeButton.addEventHandler(ActionEvent.ACTION, fileChangeHandler);

		EventHandler<ActionEvent> registrationHandler = (e) -> {
			int startTime, startSecond, startMinute, startHour;
			if (startTimeTextField.getText().isEmpty()) {
				startTime = 0;
			} else {
				startTime = Integer.parseInt(startTimeTextField.getText());
			}
			startSecond = startTime % 100;
			startMinute = startTime / 100 % 100;
			startHour = startTime / 10000;

			int allotTime, allotSecond, allotMinute;
			if (allotTimeTextField.getText().isEmpty()) {
				allotTime = 0;
			} else {
				allotTime = Integer.parseInt(allotTimeTextField.getText());
			}
			allotSecond = allotTime % 100;
			allotMinute = allotTime / 100;

			main.getSchedules().set(
					main.getTimeTable().table.getSelectionModel()
							.getSelectedIndex(),
					new Schedule(directoryTextField.getText(), fileTextField
							.getText(), startHour, startMinute, startSecond,
							allotMinute, allotSecond));
			main.getTimeTable().timeTableDatas.set(
					main.getTimeTable().table.getSelectionModel()
							.getSelectedIndex(),
					main.getTimeTable()
							.setTimeTableData(
									dformat.format(main.getTimeTable().table
											.getSelectionModel()
											.getSelectedIndex() + 1),
									directoryTextField.getText(),
									fileTextField.getText(),
									String.valueOf(startHour) + ":"
											+ dformat.format(startMinute) + ":"
											+ dformat.format(startSecond),
									String.valueOf(allotMinute) + ":"
											+ dformat.format(allotSecond)));
			newEvent.close();
		};
		registrationButton.addEventHandler(ActionEvent.ACTION,
				registrationHandler);

		EventHandler<ActionEvent> CancelButtonHandler = (e) -> {
			newEvent.close();
		};
		CancelButton.addEventHandler(ActionEvent.ACTION, CancelButtonHandler);

		hboxDirectory.getChildren().addAll(directoryLabel, directoryTextField);
		hboxFile.getChildren().addAll(fileLabel, fileTextField);
		hboxStartTime.getChildren().addAll(startTimeLabel, startTimeTextField);
		hboxAllotTime.getChildren().addAll(allotTimeLabel, allotTimeTextField);
		hboxBottumSelect.getChildren().addAll(registrationButton, CancelButton);
		vbox.getChildren().addAll(hboxDirectory, hboxFile, fileChangeButton,
				hboxStartTime, hboxAllotTime, hboxBottumSelect);
		newEvent.setScene(scene);
		newEvent.initModality(Modality.APPLICATION_MODAL);
		newEvent.show();
	}
}
