import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ReadFile {

	private String directory = "H:\\メガフェプス\\2016 大学祭\\メイン１日目";

	ReadFile(Main main) {
		try {
			File f = null;
			// ディレクトリ選択ダイアログを表示し、選択したファイルパスを取得
			DirectoryChooser dc = new DirectoryChooser();
			dc.setTitle("フォルダを選択してください");
			f = dc.showDialog(new Stage());
			if (f != null) {
				directory = f.toString();
			} else {
				System.out.println("ファイルが選択されていません");
			}

			// ファイルを読み込む
			File csvFile = new File(directory + "\\hogehoge.csv"); // 読み込み対象のCSVファイル

			FileChooser fc = new FileChooser();
			fc.setInitialDirectory(f);
			fc.setTitle("台本を選択してください");
			fc.getExtensionFilters().addAll(
					new ExtensionFilter("タイムテーブル", "*.csv"));

			f = fc.showOpenDialog(new Stage());
			if (f != null) {
				csvFile = f;
				System.out.print("hoge");
			} else {
				System.out.println("台本が選択されていません");
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
					main.schedules.add(new Schedule("", fruit[1], Integer
							.parseInt(fruit[2]), Integer.parseInt(fruit[3]),
							Integer.parseInt(fruit[4]), Integer
									.parseInt(fruit[5]), Integer
									.parseInt(fruit[6])));
				} else {
					main.schedules.add(new Schedule(
							directory + "\\" + fruit[0], fruit[1], Integer
									.parseInt(fruit[2]), Integer
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

	public String getDirectory() {
		return directory;
	}
}
