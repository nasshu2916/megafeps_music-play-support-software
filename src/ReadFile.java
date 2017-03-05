import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ReadFile {

	private String directory = "H:\\メガフェプス\\2016 大学祭\\メイン１日目";
	private File programDataFile;

	ReadFile(Main main) {
		try {
			File f = null;

			// ファイルを読み込む
			// 読み込み対象のCSVファイル
			FileChooser fc = new FileChooser();
			fc.setTitle("台本を選択してください");
			fc.getExtensionFilters().addAll(new ExtensionFilter("タイムテーブル", "*.csv"));

			f = fc.showOpenDialog(new Stage());
			if (f != null) {
				programDataFile = f;
				directory = f.getParent();
				System.out.println(f);
				System.out.println(directory);
			} else {
				System.out.println("台本が選択されていません");
			}

			// SJISを指定
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(
					programDataFile), "SJIS"));

			// 読み込んだファイルを1行ずつ処理する
			String s;
			while ((s = br.readLine()) != null) {
				// 区切り文字","で分割する
				String[] fruit = s.split(",", -1);
				if (fruit[0].equals("")) {
					main.schedules.add(new Schedule("", fruit[1], Integer.parseInt(fruit[2]),
							Integer.parseInt(fruit[3]), Integer.parseInt(fruit[4]), Integer
									.parseInt(fruit[5]), Integer.parseInt(fruit[6]), fruit[7]));
				} else {
					main.schedules.add(new Schedule(directory + "/" + fruit[0], fruit[1], Integer
							.parseInt(fruit[2]), Integer.parseInt(fruit[3]), Integer
							.parseInt(fruit[4]), Integer.parseInt(fruit[5]), Integer
							.parseInt(fruit[6]), fruit[7]));
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

	public File getProgramDataFile() {
		return programDataFile;
	}
}
