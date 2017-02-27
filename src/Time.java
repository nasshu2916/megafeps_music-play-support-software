import java.text.DecimalFormat;

public class Time {
	DecimalFormat dformat = new DecimalFormat("00");

	class AllotTime {
		private int minute;
		private int second;

		AllotTime(int minute, int second) {
			this.minute = minute;
			this.second = second;
		}

		protected int getMinute() {
			return minute;
		}

		protected int getSecond() {
			return second;
		}

		protected void setMinute(int minute) {
			this.minute = minute;
		}

		protected void setSecond(int second) {
			this.second = second;
		}

		protected String getStringTime() {
			return dformat.format(minute) + ":" + dformat.format(second);
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

		protected int getHour() {
			return hour;
		}

		protected int getMinute() {
			return minute;
		}

		protected int getSecond() {
			return second;
		}

		protected void setHour(int hour) {
			this.hour = hour;
		}

		protected void setMinute(int minute) {
			this.minute = minute;
		}

		protected void setSecond(int second) {
			this.second = second;
		}

		protected String getStringTime() {
			return dformat.format(hour) + ":" + dformat.format(minute) + ":"
					+ dformat.format(second);
		}
	}
}
