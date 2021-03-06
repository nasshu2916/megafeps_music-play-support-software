import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TimeTable extends Time{
	TableView<TimeTableData> table = new TableView<TimeTableData>();

	// データを追加
	ObservableList<TimeTableData> timeTableDatas = FXCollections.observableArrayList();

	public Node creatTimeTable(Main main) {
		// 表ビューを作成

		table.setEditable(false);
		table.setFocusTraversable(false);

		// 列を追加
		// PropertyValueFactoryでBeanクラスの対応するプロパティを指定
		TableColumn<TimeTableData, String> scriptNumber = new TableColumn<TimeTableData, String>("番号");
		TableColumn<TimeTableData, String> file = new TableColumn<TimeTableData, String>("ファイル");
		TableColumn<TimeTableData, String> directryName = new TableColumn<TimeTableData, String>("ディレクトリパス");
		TableColumn<TimeTableData, String> fileName = new TableColumn<TimeTableData, String>("ファイル名");
		TableColumn<TimeTableData, String> startTime = new TableColumn<TimeTableData, String>("開始時間");
		TableColumn<TimeTableData, String> allortTime = new TableColumn<TimeTableData, String>("持ち時間");
		TableColumn<TimeTableData, String> remarks = new TableColumn<TimeTableData, String>("備考");
		scriptNumber.setCellValueFactory(new PropertyValueFactory<>("element1"));
		scriptNumber.setSortable(false);
		directryName.setCellValueFactory(new PropertyValueFactory<>("element2_1"));
		directryName.setSortable(false);
		fileName.setCellValueFactory(new PropertyValueFactory<>("element2_2"));
		fileName.setSortable(false);
		startTime.setCellValueFactory(new PropertyValueFactory<>("element3"));
		startTime.setSortable(false);
		allortTime.setCellValueFactory(new PropertyValueFactory<>("element4"));
		allortTime.setSortable(false);
		remarks.setCellValueFactory(new PropertyValueFactory<>("element5"));
		remarks.setSortable(false);
		file.getColumns().addAll(directryName, fileName);
		table.getColumns().addAll(scriptNumber, startTime, allortTime, file,remarks);

		for (int i = 0; i < main.schedules.size(); i++) {
			timeTableDatas.add(new TimeTableData(dformat.format(i + 1), main.schedules.get(i)));
		}

		// データを登録
		table.setItems(timeTableDatas);

		table.tableMenuButtonVisibleProperty();

		return table;
	}

	public TimeTableData setTimeTableData(String number, String directoryPath, String fileName, String StartTime,
			String allottedTile) {

		return new TimeTableData(number, directoryPath, fileName, StartTime, allottedTile);
	}

	public TimeTableData setTimeTableData(String number, Schedule Schedule) {
		return new TimeTableData(number, Schedule);
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
		private String remarks;

		TimeTableData(String number, String directoryPath, String fileName, String StartTime, String allottedTile) {
			this.number = number;
			this.directoryPath = directoryPath;
			this.fileName = fileName;
			this.StartTime = StartTime;
			this.allottedTime = allottedTile;
		}

		public TimeTableData(Schedule schedule) {
			number = "00";
			directoryPath = schedule.getDirectry();
			fileName = schedule.getFileName();
			StartTime = schedule.getStartTime().getStringTime();
			allottedTime = schedule.getAllotTime().getStringTime();
			remarks = schedule.getRemarks();
		}

		public TimeTableData(String num,Schedule schedule) {
			number = num;
			directoryPath = schedule.getDirectry();
			fileName = schedule.getFileName();
			StartTime = schedule.getStartTime().getStringTime();
			allottedTime = schedule.getAllotTime().getStringTime();
			remarks = schedule.getRemarks();
		}

		public void setNumber(String number) {
			this.number = number;
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

		public String getElement5() {
			return remarks;
		}

		public void setElement5(String remarks) {
			this.remarks = remarks;
		}
	}
}
