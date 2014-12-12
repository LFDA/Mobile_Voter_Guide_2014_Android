package com.voterguide.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.voterguide.R;
import com.voterguide.model.Candidate;
import com.voterguide.model.City;
import com.voterguide.model.Office;
import com.voterguide.model.Position;

public class DataHelper {

	public static ArrayList<City> getCityList(Context context) {
		ArrayList<City> cityList = new ArrayList<City>();
		DatabaseHelper repo = DatabaseHelper.getInstance(context);
		SQLiteDatabase db = repo.getWritableDatabase();
		Cursor cur = db.query("Cities", null, null, null, null, null,
				"CityName ASC");
		cur.moveToFirst();
		while (cur.isAfterLast() == false) {
			City city = new City();
			city.setCityName(cur.getString(0));
			city.setCongressDistrictList(cur.getString(1));
			city.setExecutiveDistrictList(cur.getString(2));
			city.setSenateDistrictList(cur.getString(3));
			city.setHouseDistrictList(cur.getString(4));
			city.setCountyName(cur.getString(5));
			city.setCityType(cur.getString(6));
			cityList.add(city);
			cur.moveToNext();
		}

		return cityList;
	}

	public static ArrayList<Candidate> getCandidateListForCity(Context context,
			City city) {
		ArrayList<Candidate> candidateList = new ArrayList<Candidate>();
		DatabaseHelper repo = DatabaseHelper.getInstance(context);
		SQLiteDatabase db = repo.getWritableDatabase();
		
		StringBuilder builder = new StringBuilder();
		String []mainHouseDistrictForCity = city.getHouseDistrictList().split(",");
		ArrayList<String> params = new ArrayList<String>();
		params.add(city.getExecutiveDistrictList());

		for( int i = 0 ; i < mainHouseDistrictForCity.length; i++ ) {
		    builder.append("?,");
		    params.add(mainHouseDistrictForCity[i].trim());
		}
		params.add(city.getCountyName());
		params.add(city.getSenateDistrictList());
		params.add(city.getCongressDistrictList());
		
		Log.i("city"," HouseDistrictList ="+params.toString());
		Cursor cur = db
				.rawQuery("select LastName, FirstName, Party, Office, Incumbent, District FROM Candidates WHERE Office='Governor' OR ( Office='Executive Councilor' and District in (?) ) OR ( Office='State Representative' and District in ("+builder.deleteCharAt( builder.length() -1 ).toString()+") and County=? ) OR ( Office='State Senator' and District in (?) ) OR ( Office='U.S. Representative' and District in (?) ) OR ( Office='U.S. Senate' ) ORDER BY Office, District, LastName, FirstName",
							params.toArray(new String[params.size()]));
		cur.moveToFirst();
		while (cur.isAfterLast() == false) {
			Candidate candidate = new Candidate();
			candidate.setLastName(cur.getString(0));
			candidate.setFirstName(cur.getString(1));
			candidate.setParty(cur.getString(2));

			candidate.setOffice(cur.getString(3));
			boolean incumbent = cur.getInt(4) > 0;
			candidate.setIncumbent(incumbent);
			candidate.setDistrict(cur.getString(5));
			boolean floterial = false;			
			Log.i("getCandidate List"," office ==> "+candidate.getOffice()+" name ="+candidate.getFirstName());
			//if([[rs stringForColumn:@"Office"] isEqualToString:@"State Representative"] && (![[rs stringForColumn:@"District"] isEqualToString:mainHouseDistrictForCity]) )
			if(candidate.getOffice()!= null && candidate.getOffice().equalsIgnoreCase("State Representative") && (candidate.getDistrict() == null || !candidate.getDistrict().equalsIgnoreCase(mainHouseDistrictForCity[0])))
		            {
		                floterial = true;
		            } else {
		                floterial = false;
		            }
			candidate.setFloterial(floterial);
			

			candidateList.add(candidate);
			cur.moveToNext();
		}
		return candidateList;
	}

	public static ArrayList<Office> getOfficeList(Context context) {
		ArrayList<Office> officeList = new ArrayList<Office>();
		DatabaseHelper repo = DatabaseHelper.getInstance(context);
		SQLiteDatabase db = repo.getWritableDatabase();
		Cursor cur = db.rawQuery("select OfficeName, DistrictColumnNameInCities from Offices order by SortOrder ASC",new String[] {});

		cur.moveToFirst();
		while (cur.isAfterLast() == false) {
			Office office = new Office();
			office.setOfficeName(cur.getString(0));
			office.setDistrictColumnNameInCities(cur.getString(1));
			officeList.add(office);
			cur.moveToNext();
		}

		return officeList;
	}
	
	public static HashMap<String,ArrayList<Candidate>> loadCityCandidatesTableViewForCity(Context context,City city){
		HashMap<String,ArrayList<Candidate>> officeNameWithCandidateList = new HashMap<String, ArrayList<Candidate>>();
			
		ArrayList<Office> officeList = getOfficeList(context);
		ArrayList<Candidate> cityCandidateList = getCandidateListForCity(context,city);
		
		//for(int officeIndex=0,officeListSize=officeList.size();officeIndex < officeListSize; officeIndex++){
		for(Office office : officeList){
			//ArrayList<Candidate> cityCandidateListFilteredForOffice = new ArrayList<Candidate>();
			//String officeName = officeList.get(officeIndex).getOfficeName();
			String officeName = office.getOfficeName();
			
			Collection<Candidate> cityCandidateListFilteredForOfficeCollection =  Collections2.filter(cityCandidateList, offeceNameEqualsTo(officeName));
			ArrayList<Candidate> cityCandidateListFilteredForOffice = new ArrayList<Candidate>();
			cityCandidateListFilteredForOffice.addAll(cityCandidateListFilteredForOfficeCollection);
			Log.i("ListOffice", officeName +" candidatesize ==> "+cityCandidateListFilteredForOffice.size());
			if(cityCandidateListFilteredForOffice.size() == 0){
				Candidate candidate = new Candidate();
				candidate.setFirstName(context.getString(R.string.no_candidates_for_group));
				cityCandidateListFilteredForOffice.add(candidate);
			}
			officeNameWithCandidateList.put(officeName, cityCandidateListFilteredForOffice);
		}
				
		return officeNameWithCandidateList;
	}
	
	public static Predicate<Candidate> offeceNameEqualsTo(final String officeName) {
	    return new Predicate<Candidate>() {

	        public boolean apply(Candidate candidate) {
	            return candidate.getOffice().equalsIgnoreCase(officeName);
	        }
	    };
	}
	
	
	public static ArrayList<Candidate> getAllCandidateList(Context context) {
		ArrayList<Candidate> candidateList = new ArrayList<Candidate>();
		DatabaseHelper repo = DatabaseHelper.getInstance(context);
		SQLiteDatabase db = repo.getWritableDatabase();
		Cursor cur = db
				.rawQuery(
						"select LastName, FirstName, Party, Office, Incumbent, District FROM Candidates ORDER BY  LastName, FirstName",
						new String[] {});
		//String mainHouseDistrictForCity = city.getHouseDistrictList().split(",")[0];
		cur.moveToFirst();
		while (cur.isAfterLast() == false) {
			Candidate candidate = new Candidate();
			candidate.setLastName(cur.getString(0));
			candidate.setFirstName(cur.getString(1));
			candidate.setParty(cur.getString(2));

			candidate.setOffice(cur.getString(3));
			boolean incumbent = cur.getInt(4) > 0;
			candidate.setIncumbent(incumbent);
			boolean floterial = false;			
			if(candidate.getOffice() != null && candidate.getOffice().equalsIgnoreCase("State Representative") ) //&& candidate.getDistrict() != null && !candidate.getDistrict().equalsIgnoreCase(mainHouseDistrictForCity))
		            {
		                floterial = true;
		            } else {
		                floterial = false;
		            }
			candidate.setFloterial(floterial);
			candidate.setDistrict(cur.getString(5));

			candidateList.add(candidate);
			cur.moveToNext();
		}
		return candidateList;
	}
	
	public static ArrayList<City> getCityByCandidate(Context context,Candidate candidate) {
		ArrayList<City> cityList = new ArrayList<City>();
		DatabaseHelper repo = DatabaseHelper.getInstance(context);
		SQLiteDatabase db = repo.getWritableDatabase();
		Cursor cur = db.query("Cities", null, null, null, null, null,
				"CityName ASC");
		cur.moveToFirst();
		while (cur.isAfterLast() == false) {
			City city = new City();
			city.setCityName(cur.getString(0));
			city.setCongressDistrictList(cur.getString(1));
			city.setExecutiveDistrictList(cur.getString(2));
			city.setSenateDistrictList(cur.getString(3));
			city.setHouseDistrictList(cur.getString(4));
			city.setCountyName(cur.getString(5));
			city.setCityType(cur.getString(6));
			cityList.add(city);
			cur.moveToNext();
		}

		return cityList;
	}
	
	public static Candidate getCandidateDetail(Context context,Candidate candidate){
		//Candidate candidateDetailObj = new Candidate();
		
		DatabaseHelper repo = DatabaseHelper.getInstance(context);
		SQLiteDatabase db = repo.getWritableDatabase();
		Cursor cur = db
				.rawQuery(
						"select Email, Website, Phone, District, County, FloterialDistrictList, Incumbent, LFDAProfileURL, LFDAPhotoUrl, Experience, Education, Family FROM Candidates WHERE LastName=? and FirstName=?",
						new String[] {candidate.getLastName(), candidate.getFirstName()});
		//String mainHouseDistrictForCity = city.getHouseDistrictList().split(",")[0];
		//cur.moveToFirst();
		if (cur.moveToFirst()) {
			candidate.setEmail(cur.getString(0));
			candidate.setWebsite(cur.getString(1));
			candidate.setPhone(cur.getString(2));
			candidate.setDistrict(cur.getString(3));
			candidate.setCounty(cur.getString(4));
			candidate.setFloterialDistrictList(cur.getString(5));
			
			boolean incumbent = cur.getInt(6) > 0;
			candidate.setIncumbent(incumbent);
			
			candidate.setLfdaProfileUrl(cur.getString(7));
			candidate.setLfdaPhotoUrl(cur.getString(8));
			candidate.setExperience(cur.getString(9));
			candidate.setEducation(cur.getString(10));
			candidate.setFamily(cur.getString(11));

		}
		
		return candidate;
	}
	
	public static ArrayList<Position> getPositionListForCandidate(Context context, Candidate candidate){
		ArrayList<Position> candidatePositionList = new ArrayList<Position>();
		
		DatabaseHelper repo = DatabaseHelper.getInstance(context);
		SQLiteDatabase db = repo.getWritableDatabase();
		Cursor cur = db.rawQuery("SELECT Issue, Position FROM Positions WHERE LastName=? AND FirstName=? ORDER BY Issue",new String[] {candidate.getLastName(), candidate.getFirstName()});
		
		cur.moveToFirst();
		while (cur.isAfterLast() == false) {
			Position position = new Position();
			position.setIssue(cur.getString(0));
			position.setPosition(cur.getString(1));
			
			candidatePositionList.add(position);
			cur.moveToNext();
		}
		
		return candidatePositionList;
	}
	

}
