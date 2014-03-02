package com.example.mytestapp.db;

public class Alarm {
	private int alarmId;
	private String alarmMessage;
	private String alarmTime;
	
	public Alarm() {
		
	}
	
	public Alarm(int alarmId, String alarmMessage, String alarmTime) {
		this.alarmId = alarmId;
		this.alarmMessage = alarmMessage;
		this.alarmTime = alarmTime;
	}

	public int getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(int alarmId) {
		this.alarmId = alarmId;
	}

	public String getAlarmMessage() {
		return alarmMessage;
	}

	public void setAlarmMessage(String alarmMessage) {
		this.alarmMessage = alarmMessage;
	}

	public String getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(String alarmTime) {
		this.alarmTime = alarmTime;
	}

}
