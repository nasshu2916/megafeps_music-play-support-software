package FXsample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class ReadFile {
	private List<MusicFile> musicFiles = new ArrayList<MusicFile>();

	private String directory = "H:\\旧Dドライブ\\メガフェプス\\2016 大学祭\\メイン１日目";

	public ReadFile() {

		try {
			File f = null;
			// ディレクトリ選択ダイアログを表示し、選択したファイルパスを取得
			DirectoryChooser dc = new DirectoryChooser();
			dc.setTitle("フォルダを選択してください");
			f = dc.showDialog(new Stage());
			if (f != null) {
				directory = f.toString();
			}

			// ファイルを読み込む
			File csvFile = new File(
					"H:\\旧Dドライブ\\メガフェプス\\2016 大学祭\\メイン１日目\\hogehoge.csv"); // 読み込み対象のCSVファイル

			FileChooser fc = new FileChooser();
			fc.setInitialDirectory(f);
			fc.setTitle("台本を選択してください");
			fc.getExtensionFilters().addAll(
					new ExtensionFilter("タイムテーブル", "*.csv"));

			f = fc.showOpenDialog(new Stage());
			if (f != null) {
				csvFile = f;
			}

			// SJISを指定
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(csvFile), "SJIS"));

			// 読み込んだファイルを1行ずつ処理する
			String s;
			while ((s = br.readLine()) != null) {
				// 区切り文字","で分割する
				String[] fruit = s.split(",", 0);
				if (fruit[0].equals("")) {
					musicFiles.add(new MusicFile("", fruit[1], Integer
							.parseInt(fruit[2]), Integer.parseInt(fruit[3]),
							Integer.parseInt(fruit[4]), Integer
									.parseInt(fruit[5]), Integer
									.parseInt(fruit[6])));
				} else {
					musicFiles.add(new MusicFile(directory + "/" + fruit[0],
							fruit[1], Integer.parseInt(fruit[2]), Integer
									.parseInt(fruit[3]), Integer
									.parseInt(fruit[4]), Integer
									.parseInt(fruit[5]), Integer
									.parseInt(fruit[6])));
				}
				
			}
			// 終了処理
			br.close();

		} catch (IOException ex) {
			// 例外発生時処理
			ex.printStackTrace();
			
		}
	}

	public List<MusicFile> getMusicFiles() {
		return musicFiles;
	}

	public class MusicFile {
		private String directory;
		private String fileName;
		private int startHour;
		private int startMinute;
		private int startSecond;
		private int allottedMinute;
		private int allottedSecond;

		MusicFile(String directory, String fileName, int startHour,
				int startMinute, int startSecond, int allottedMinute,
				int allottedSecond) {
			this.directory = directory;
			this.fileName = fileName;
			this.startHour = startHour;
			this.startMinute = startMinute;
			this.startSecond = startSecond;
			this.allottedMinute = allottedMinute;
			this.allottedSecond = allottedSecond;
		}

		public String getDirectry() {
			return directory;
		}

		public String getFileName() {
			return fileName;
		}

		public void setDirectory(String directory) {
			this.directory = directory;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public int getHour() {
			return startHour;
		}

		public int getMinute() {
			return startMinute;
		}

		public int getSecond() {
			return startSecond;
		}

		public int getAllottedMinute() {
			return allottedMinute;
		}

		public int getAllottedSecond() {
			return allottedSecond;
		}
	}
}
