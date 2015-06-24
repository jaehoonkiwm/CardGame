package com.iot.cardgame;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hooni on 2015. 6. 21..
 */
public class UserData implements Parcelable {
	private String name;
	private int stage;
	private int score;

	public UserData(String name, int stage, int score) {
		this.name = name;
		this.stage = stage;
		this.score = score;
	}

	public UserData(Parcel source) {
		this.name = source.readString();
		this.stage = source.readInt();
		this.score = source.readInt();
	}

	public String getName() {
		return name;
	}

	public int getStage() {
		return stage;
	}

	public int getScore() {
		return score;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

		@Override
		public UserData createFromParcel(Parcel source) {
			return new UserData(source);
		}

		@Override
		public UserData[] newArray(int size) {
			return new UserData[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeInt(stage);
		dest.writeInt(score);
	}
}
