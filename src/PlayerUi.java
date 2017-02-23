import java.io.File;
import java.text.DecimalFormat;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PlayerUi extends PlayerSystem {

	private Node spectrum = null; // スペクトル表示ノード
	private Slider volumeSlider;

	private int panelwide = 1200 / 4 - 20;
	private Label name;
	private Media media;

	protected void createPlayerWindow() {
		File file = getfFileName();
		if (!file.exists()){
		    System.out.println("ファイルは存在しません");
		    return;
		}
		createPlayerWindow(file);
	}

	protected void createPlayerWindow(File file) {
		media = new Media(file.toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		name = new Label(String.valueOf(playIndex + 1) + "\t曲名 : "
				+ file.getName());
		name.setFont(Font.font(null, FontWeight.BLACK, 16));
		panel.getChildren().add(name);

		// スペクトル表示

		if (getSuffix(file.toURI().toString()).equals("mp4")) {
			MediaView mediaView = new MediaView(mediaPlayer);
			mediaView.setFitWidth(panelwide);
			panel.getChildren().add(mediaView);
		} else {
			createSpectrum();
			panel.getChildren().add(spectrum);
		}

		// 再生・停止・繰り返しボタン作成
		// 時間表示スライダ作成
		// ボリューム表示スライダ作成
		panel.getChildren().addAll(createButton(mediaPlayer),
				createTimeSlider(), createVolumeSlider(),
				createPlaylistButton(mediaPlayer));

		panel.setBorder(new Border(new BorderStroke(Color.GREEN,
				BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
				new BorderWidths(10))));
	}

	/**
	 * 再生、一時停止、停止、連続再生ボタンを作成
	 *
	 * @param mp
	 * @return
	 */
	private Node createButton(MediaPlayer mp) {
		// 表示コンポーネントを作成
		HBox root = new HBox();
		Button playButton = new Button("Play");
		playButton.setFocusTraversable(false);
		Button pauseButton = new Button("Pause");
		pauseButton.setFocusTraversable(false);
		Button stopButton = new Button("Stop");
		stopButton.setFocusTraversable(false);
		Button setButton = new Button("Open");
		setButton.setFocusTraversable(false);

		Button selectButton = new Button("番号選択");
		selectButton.setFocusTraversable(false);
		root.getChildren().addAll(playButton, pauseButton, stopButton,
				setButton, selectButton);

		// 再生ボタンにイベントを登録
		EventHandler<ActionEvent> playHandler = (e) -> {
			// 再生開始
			mediaPlay();
		};
		playButton.addEventHandler(ActionEvent.ACTION, playHandler);

		// 一時停止ボタンにイベントを登録
		EventHandler<ActionEvent> pauseHandler = (e) -> {
			// 一時停止
			mediaPause();
		};
		pauseButton.addEventHandler(ActionEvent.ACTION, pauseHandler);

		// 停止ボタンにイベントを登録
		EventHandler<ActionEvent> stopHandler = (e) -> {
			// 停止
			mediaStop();
		};
		stopButton.addEventHandler(ActionEvent.ACTION, stopHandler);

		// 選択ボタンにイベントを登録
		EventHandler<ActionEvent> selectHandler = (e) -> {
			// 選択
			mediaSelect();
		};
		selectButton.addEventHandler(ActionEvent.ACTION, selectHandler);

		EventHandler<ActionEvent> setHandler = (e) -> {
			mediaOpen();
		};
		setButton.addEventHandler(ActionEvent.ACTION, setHandler);

		return root;
	}

	/**
	 * スペクトル表示ノードを作成
	 *
	 * @param mp
	 */
	private void createSpectrum() {
		// スペクトル変化時のリスナーを作成
		AudioSpectrumListener spectrumListener = new AudioSpectrumListener() {
			// 定数
			private final int LINE_WIDTH = 15; // 各バンド幅を表す線の線幅

			// 変数
			private Canvas canvas = null; // スペクトルを描写するキャンバス
			private AudioSpectrumListener beforeListener = null; // 既に登録しているリスナーの退避用変数

			// 初期化
			{
				// キャンバスの作成
				canvas = new Canvas(panelwide, 170);

				// 既に登録されているリスナーを退避
				beforeListener = mediaPlayer.getAudioSpectrumListener();

				// スペクトル表示ノードを作成
				// シーングラフに登録
				spectrum = canvas;
			}

			// スペグトログラム更新
			@Override
			public void spectrumDataUpdate(double timestamp, double duration,
					float[] magnitudes, float[] phases) {
				// 以前のリスナーを退避した場合は、先に呼び出す
				if (beforeListener != null) {
					beforeListener.spectrumDataUpdate(timestamp, duration,
							magnitudes, phases);
				}

				// 描写用クラスを取得
				GraphicsContext g = canvas.getGraphicsContext2D();

				// 画面を初期化
				g.setFill(Color.WHITESMOKE);
				g.fillRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight());
				// スペクトログラムを描写
				for (int i = 0; i < phases.length; i += 4) {
					// ドットを描写
					g.setFill(Color.RED);
					float ave = (magnitudes[i] + magnitudes[i + 1]
							+ magnitudes[i + 2] + magnitudes[i + 3]) / 4;
					g.fillRect((double) i * LINE_WIDTH / 4,
							(double) canvas.getHeight() - 30.0 - (ave + 60.0)
									* 4, (double) LINE_WIDTH,
							(double) (ave + 60) * 4);
				}
			}

		};
		mediaPlayer.setAudioSpectrumListener(spectrumListener);
	}

	/**
	 * ボリュームを表示・操作するスライダを作成
	 *
	 * @param mp
	 * @return
	 */
	private Node createVolumeSlider() {
		// 表示コンポーネントを作成
		HBox root = new HBox(5.0);
		Label info = new Label();
		volumeSlider = new Slider();
		volumeSlider.setFocusTraversable(false);
		root.getChildren().add(volumeSlider);
		root.getChildren().add(info);

		// 再生準備完了時に各種情報を取得する関数を登録
		Runnable beforeFunc = mediaPlayer.getOnReady(); // 現在のレディ関数
		Runnable readyFunc = () -> {
			// 先に登録された関数を実行
			if (beforeFunc != null) {
				beforeFunc.run();
			}

			// スライダの値を設定
			volumeSlider.setMin(0.0);
			volumeSlider.setMax(1.0);
			volumeSlider.setValue(mediaPlayer.getVolume());
		};
		mediaPlayer.setOnReady(readyFunc);

		// 再生中にボリュームを表示
		// プレイヤの現在時間が変更されるたびに呼び出されるリスナを登録
		ChangeListener<? super Number> sliderListener = (ov, old, current) -> {
			// 動画の情報をラベル出力
			String infoStr = String
					.format("Vol:%4.2f", mediaPlayer.getVolume());
			info.setText(infoStr);

			// スライダにあわせてボリュームを変更
			mediaPlayer.setVolume(volumeSlider.getValue());
		};
		volumeSlider.valueProperty().addListener(sliderListener);

		return root;
	}

	/**
	 * 再生時間を表示・操作するスライダを作成
	 *
	 * @param mp
	 * @return
	 */
	private Node createTimeSlider() {
		// 表示コンポーネントを作成
		VBox root = new VBox();
		root.setPrefWidth(panelwide);
		Slider slider = new Slider();
		slider.setFocusTraversable(false);
		slider.setPrefSize(panelwide, 50);
		Label info = new Label();
		info.setFont(Font.font(null, 28));
		root.getChildren().add(slider);
		root.getChildren().add(info);

		// 再生準備完了時に各種情報を取得する関数を登録
		Runnable beforeFunc = mediaPlayer.getOnReady(); // 現在のレディ関数
		Runnable readyFunc = () -> {
			// 先に登録された関数を実行
			if (beforeFunc != null) {
				beforeFunc.run();
			}
			// スライダの値を設定
			slider.setMin(mediaPlayer.getStartTime().toSeconds());
			slider.setMax(mediaPlayer.getStopTime().toSeconds());
			slider.setSnapToTicks(true);
		};
		mediaPlayer.setOnReady(readyFunc);

		// 再生中にスライダを移動
		// プレイヤの現在時間が変更されるたびに呼び出されるリスナを登録
		ChangeListener<? super Duration> playListener = (ov, old, current) -> {
			// 動画の情報をラベル出力
			DecimalFormat dformat = new DecimalFormat("00.0");
			String infoStr = String.format("%2.0f:",
					Math.floor(mediaPlayer.getCurrentTime().toMinutes()))
					+ dformat
							.format(mediaPlayer.getCurrentTime().toSeconds() % 60)
					+ "/"
					+ String.format("%2.0f:", Math.floor(mediaPlayer
							.getTotalDuration().toMinutes()))
					+ dformat
							.format(mediaPlayer.getTotalDuration().toSeconds() % 60);
			info.setText(infoStr);

			// スライダを移動
			slider.setValue(mediaPlayer.getCurrentTime().toSeconds());
		};
		mediaPlayer.currentTimeProperty().addListener(playListener);

		// スライダを操作するとシークする
		EventHandler<MouseEvent> sliderHandler = (e) -> {
			// スライダを操作すると、シークする
			mediaPlayer.seek(javafx.util.Duration.seconds(slider.getValue()));

		};
		slider.addEventFilter(MouseEvent.MOUSE_RELEASED, sliderHandler);

		return root;
	}

	private Node createPlaylistButton(MediaPlayer mp) {
		HBox root = new HBox(5.0);

		Button prevButton = new Button("Prev");
		prevButton.setFocusTraversable(false);
		Button nextButton = new Button("Next");
		nextButton.setFocusTraversable(false);
		ToggleButton repeatButton = new ToggleButton("Repeat");
		repeatButton.setFocusTraversable(false);
		ToggleButton x2speedButton = new ToggleButton("x2");
		x2speedButton.setFocusTraversable(false);
		ToggleButton samplerButton = new ToggleButton("ポン出し");
		samplerButton.setFocusTraversable(false);

		root.getChildren().addAll(prevButton, nextButton, repeatButton,
				x2speedButton, samplerButton);

		// purvボタンにイベントを登録
		EventHandler<ActionEvent> playHandler = (e) -> {
			pressPrev();
		};
		prevButton.addEventHandler(ActionEvent.ACTION, playHandler);

		// nextボタンにイベントを登録
		EventHandler<ActionEvent> pauseHandler = (e) -> {
			pressNext();
		};
		nextButton.addEventHandler(ActionEvent.ACTION, pauseHandler);

		// 連続再生設定
		Runnable repeatFunc = () -> {
			// 連続再生ボタンの状態を取得し
			if (repeatButton.isSelected()) {
				// 頭だしして再生
				mp.seek(mp.getStartTime());
				mp.play();
			} else {
				// 頭だしして停止
				mp.seek(mp.getStartTime());
				mp.stop();
			}
		};
		mp.setOnEndOfMedia(repeatFunc);

		EventHandler<ActionEvent> setx2speedHandler = (e) -> {
			// 連続再生ボタンの状態を取得し
			if (x2speedButton.isSelected()) {
				mp.setRate(2.0);
			} else {
				mp.setRate(1.0);
			}
		};
		x2speedButton.addEventHandler(ActionEvent.ACTION, setx2speedHandler);

		return root;
	}

	private void pressNext() {
		do {
			playIndex++;
			if (playIndex >= main.schedules.size() - 1)
				playIndex = 0;
		} while (main.schedules.get(playIndex).getDirectry().isEmpty());
		setPlayer();
	}

	private void pressPrev() {
		do {
			playIndex--;
			if (playIndex <= 0)
				playIndex = main.schedules.size() - 1;
		} while (main.schedules.get(playIndex).getDirectry().isEmpty());
		setPlayer();
	}

	private void mediaSelect() {
		Stage newStage = new Stage();
		newStage.setTitle("番号選択");
		// 新しいウインドウ内に配置するコンテンツを生成
		HBox hbox = new HBox();
		Scene scene = new Scene(hbox, 250, 100);
		Label selectLabel = new Label("番号");

		TextField textField = new TextField(String.valueOf(playIndex + 1));
		Button registrationButton = new Button("登録");
		EventHandler<ActionEvent> registrationHandler = (e) -> {
			if (main.schedules.get(Integer.parseInt(textField.getText()) - 1)
					.getDirectry().isEmpty()) {
				// エラーメッセージ
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("エラー");
				alert.getDialogPane().setContentText("選択した番号は音楽ファイルではありません");
				alert.show();
			} else {
				playIndex = Integer.parseInt(textField.getText()) - 1;
				setPlayer();
				newStage.close();
			}
		};
		registrationButton.addEventHandler(ActionEvent.ACTION,
				registrationHandler);

		EventHandler<KeyEvent> sceneKeyFilter = (event) -> {
			if (event.getCode().toString() == "ENTER") {
				if (main.schedules
						.get(Integer.parseInt(textField.getText()) - 1)
						.getDirectry().isEmpty()) {
				} else {
					playIndex = Integer.parseInt(textField.getText()) - 1;
					setPlayer();
					newStage.close();
				}
			}
		};
		newStage.addEventFilter(KeyEvent.KEY_PRESSED, sceneKeyFilter);

		hbox.getChildren().addAll(selectLabel, textField, registrationButton);
		newStage.setScene(scene);
		newStage.initModality(Modality.APPLICATION_MODAL);
		newStage.show();

	}

	private void setPlayer() {
		mediaPlayer.stop();
		mediaPlayer = null;
		panel.getChildren().clear();
		createPlayerWindow();
	}

	private void setPlayer(File f) {
		mediaPlayer.stop();
		mediaPlayer = null;
		panel.getChildren().clear();
		createPlayerWindow(f);
	}

	public void setPlayer(int playIndex) {
		if (main.schedules.get(playIndex).getDirectry().isEmpty()) {
		} else {
			setPlayIndex(playIndex);
			setPlayer();
		}
	}

	private void mediaOpen() {
		FileChooser fc = new FileChooser();
		fc.setTitle("ファイル選択ダイアログ");
		fc.getExtensionFilters().addAll(
				new ExtensionFilter("support Files", "*.wav", "*.mp3", "*.aac",
						"*.mp4", "*.m4a", "*.flv"),
				new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
				new ExtensionFilter("Video Files", "*.mp4", "*.m4a", "*.flv"),
				new ExtensionFilter("All Files", "*.*")
						);
		File f = null;
		f = fc.showOpenDialog(new Stage());
		if (f != null) {
			setPlayer(f);
		}
	}
}