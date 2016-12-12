package FXsample;

import java.text.DecimalFormat;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TimeTable {
	TableView<TimeTableData> table = new TableView<TimeTableData>();

	public Node createtimeTable(Main main) {
		// 表ビューを作成

		table.setEditable(false);
		table.setFocusTraversable(false);

		// 列を追加
		// PropertyValueFactoryでBeanクラスの対応するプロパティを指定
		TableColumn<TimeTableData, String> col1 = new TableColumn<TimeTableData, String>(
				"番号");
		TableColumn<TimeTableData, String> col2 = new TableColumn<TimeTableData, String>(
				"ファイル");
		TableColumn<TimeTableData, String> col2_1 = new TableColumn<TimeTableData, String>(
				"ディレクトリパス");
		TableColumn<TimeTableData, String> col2_2 = new TableColumn<TimeTableData, String>(
				"ファイル名");
		TableColumn<TimeTableData, String> col3 = new TableColumn<TimeTableData, String>(
				"開始時間");
		TableColumn<TimeTableData, String> col4 = new TableColumn<TimeTableData, String>(
				"持ち時間");
		col1.setCellValueFactory(new PropertyValueFactory<>("element1"));
		col1.setSortable(false);
		col2_1.setCellValueFactory(new PropertyValueFactory<>("element2_1"));
		col2_1.setSortable(false);
		col2_2.setCellValueFactory(new PropertyValueFactory<>("element2_2"));
		col2_2.setSortable(false);
		col3.setCellValueFactory(new PropertyValueFactory<>("element3"));
		col3.setSortable(false);
		col4.setCellValueFactory(new PropertyValueFactory<>("element4"));
		col4.setSortable(false);
		col2.getColumns().addAll(col2_1, col2_2);
		table.getColumns().addAll(col1, col3, col4, col2);

		// データを追加
		ObservableList<TimeTableData> data = FXCollections
				.observableArrayList();

		DecimalFormat dformat = new DecimalFormat("00");

		for (int i = 0; i < main.getReadFile().getMusicFiles().size(); i++) {
			data.add(new TimeTableData(dformat.format(i + 1), main
					.getReadFile().getMusicFiles().get(i).getDirectry(), main
					.getReadFile().getMusicFiles().get(i).getFileName(), main
					.getReadFile().getMusicFiles().get(i).getHour()
					+ ":"
					+ dformat.format(main.getReadFile().getMusicFiles().get(i)
							.getMinute())
					+ ":"
					+ dformat.format(main.getReadFile().getMusicFiles().get(i)
							.getSecond()), main.getReadFile().getMusicFiles()
					.get(i).getAllottedMinute()
					+ ":"
					+ dformat.format(main.getReadFile().getMusicFiles().get(i)
							.getAllottedSecond())));
		}

		// データを登録
		table.setItems(data);

		table.tableMenuButtonVisibleProperty();

		return table;
	}

	/**
	 * テーブル表示用のローカルクラス
	 * 
	 * @author
	 *
	 */
	public class TimeTableData {
		private String number;
		private String directoryPath;
		private String fileName;
		private String StartTime;
		private String allottedTime;

		public TimeTableData(String number, String directoryPath,
				String fileName, String StartTime, String allottedTile) {
			this.number = number;
			this.directoryPath = directoryPath;
			this.fileName = fileName;
			this.StartTime = StartTime;
			this.allottedTime = allottedTile;
		}

		public String getElement1() {
			return number;
		}

		public void setElement1(String number) {
			this.number = number;
		}

		public String getElement2_1() {
			return directoryPath;
		}

		public void setElement2_1(String directoryPath) {
			this.directoryPath = directoryPath;
		}

		public String getElement2_2() {
			return fileName;
		}

		public void setElement2_2(String fileName) {
			this.fileName = fileName;
		}

		public String getElement3() {
			return StartTime;
		}

		public void setElement3(String time) {
			this.StartTime = time;
		}

		public String getElement4() {
			return allottedTime;
		}

		public void setElement4(String time) {
			this.allottedTime = time;
		}
	}
}
