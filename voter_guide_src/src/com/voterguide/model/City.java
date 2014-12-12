package com.voterguide.model;

import android.os.Parcel;
import android.os.Parcelable;

public class City implements Parcelable {

	private String cityName;
	private String countyName;
	private String congressDistrictList;
	private String executiveDistrictList;
	private String senateDistrictList;
	private String houseDistrictList;
	private String cityType;
	
	public City(){}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public String getCongressDistrictList() {
		return congressDistrictList;
	}

	public void setCongressDistrictList(String congressDistrictList) {
		this.congressDistrictList = congressDistrictList;
	}

	public String getExecutiveDistrictList() {
		return executiveDistrictList;
	}

	public void setExecutiveDistrictList(String executiveDistrictList) {
		this.executiveDistrictList = executiveDistrictList;
	}

	public String getSenateDistrictList() {
		return senateDistrictList;
	}

	public void setSenateDistrictList(String senateDistrictList) {
		this.senateDistrictList = senateDistrictList;
	}

	public String getHouseDistrictList() {
		return houseDistrictList;
	}

	public void setHouseDistrictList(String houseDistrictList) {
		this.houseDistrictList = houseDistrictList;
	}

	public String getCityType() {
		return cityType;
	}

	public void setCityType(String cityType) {
		this.cityType = cityType;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flag) {
		// TODO Auto-generated method stub
		dest.writeString(cityName);
		dest.writeString(countyName);
		dest.writeString(congressDistrictList);
		dest.writeString(executiveDistrictList);
		dest.writeString(senateDistrictList);
		dest.writeString(houseDistrictList);
		dest.writeString(cityType);
	}

	public City(Parcel in) {
		this.cityName = in.readString();
		this.countyName = in.readString();
		this.congressDistrictList = in.readString();
		this.executiveDistrictList = in.readString();
		this.senateDistrictList = in.readString();
		this.houseDistrictList = in.readString();
		this.cityType = in.readString();
	}

	@SuppressWarnings("unchecked")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

		public City createFromParcel(Parcel in)

		{

			return new City(in);

		}

		public City[] newArray(int size)

		{

			return new City[size];

		}

	};

}
