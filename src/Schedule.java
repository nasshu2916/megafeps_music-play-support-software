public class Schedule extends Time{
	private String directory;
	private String fileName;
	private StartTime startTime;
	private AllotTime allotTime;
	private String remarks;

	Schedule(String directory, String fileName, int startHour, int startMinute,
			int startSecond, int allotMinute, int allotSecond,String remarks) {
		this.directory = directory;
		this.fileName = fileName;
		startTime = new StartTime(startHour, startMinute, startSecond);
		allotTime = new AllotTime(allotMinute, allotSecond);
		this.remarks = remarks;
	}
	
	Schedule(String directory, String fileName, StartTime startTime, AllotTime allotTime) {
		this.directory = directory;
		this.fileName = fileName;
		this.startTime = startTime;
		this.allotTime = allotTime;
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
	
	public String getRemarks() {
		return remarks;
	}
	
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}
