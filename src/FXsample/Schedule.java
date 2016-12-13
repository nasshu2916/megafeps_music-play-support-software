package FXsample;

public class Schedule {
	private String directory;
	private String fileName;
	private StartTime startTime;
	private AllotTime allotTime;

	Schedule(String directory, String fileName, int startHour, int startMinute,
			int startSecond, int allotMinute, int allotSecond) {
		this.directory = directory;
		this.fileName = fileName;
		startTime = new StartTime(startHour, startMinute, startSecond);
		allotTime = new AllotTime(allotMinute, allotSecond);
	}

	public String getDirectry() {
		return directory;
	}

	public String getFileName() {
		return fileName;
	}

	public StartTime getStartTime() {
		return startTime;
	}

	public AllotTime getAllotTime() {
		return allotTime;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	class AllotTime {
		private int minute;
		private int second;

		AllotTime(int minute, int second) {
			this.minute = minute;
			this.second = second;

		}

		public int getMinute() {
			return minute;
		}

		public int getSecond() {
			return second;
		}

		void setMinute(int minute) {
			this.minute = minute;
		}

		void setSecond(int second) {
			this.second = second;
		}

	}

	class StartTime {
		private int hour;
		private int minute;
		private int second;

		StartTime(int hour, int minute, int second) {
			this.hour = hour;
			this.minute = minute;
			this.second = second;

		}

		public int getHour() {
			return hour;
		}

		public int getMinute() {
			return minute;
		}

		public int getSecond() {
			return second;
		}

		void setHour(int hour) {
			this.hour = hour;
		}

		void setMinute(int minute) {
			this.minute = minute;
		}

		void setSecond(int second) {
			this.second = second;
		}
	}
}
