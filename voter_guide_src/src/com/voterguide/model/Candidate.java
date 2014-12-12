package com.voterguide.model;

import java.io.Serializable;

public class Candidate implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String lastName;
	private String firstName;
	private String email;
	private String website;
	private String phone;
	private String party;
	private String office;
	private String district;
	private String county;
	private String floterialDistrictList;
	private boolean incumbent;
	private boolean floterial;
	private String lfdaProfileUrl;
	private String lfdaPhotoUrl;
	private String experience;
	private String education;
	private String family;
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getParty() {
		return party;
	}
	public void setParty(String party) {
		this.party = party;
	}
	public String getOffice() {
		return office;
	}
	public void setOffice(String office) {
		this.office = office;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getFloterialDistrictList() {
		return floterialDistrictList;
	}
	public void setFloterialDistrictList(String floterialDistrictList) {
		this.floterialDistrictList = floterialDistrictList;
	}
	public boolean isIncumbent() {
		return incumbent;
	}
	public void setIncumbent(boolean incumbent) {
		this.incumbent = incumbent;
	}
	public boolean isFloterial() {
		return floterial;
	}
	public void setFloterial(boolean floterial) {
		this.floterial = floterial;
	}
	public String getLfdaProfileUrl() {
		return lfdaProfileUrl;
	}
	public void setLfdaProfileUrl(String lfdaProfileUrl) {
		this.lfdaProfileUrl = lfdaProfileUrl;
	}
	public String getLfdaPhotoUrl() {
		return lfdaPhotoUrl;
	}
	public void setLfdaPhotoUrl(String lfdaPhotoUrl) {
		this.lfdaPhotoUrl = lfdaPhotoUrl;
	}
	public String getExperience() {
		return experience;
	}
	public void setExperience(String experience) {
		this.experience = experience;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getFamily() {
		return family;
	}
	public void setFamily(String family) {
		this.family = family;
	}

}
