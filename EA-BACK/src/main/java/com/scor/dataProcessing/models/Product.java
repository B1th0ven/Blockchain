package com.scor.dataProcessing.models;

import java.io.Serializable;

public class Product implements Serializable {

	private String id;
	private String age_def;
	private String p_start_date;
	private String p_end_date;
	private String max_face_amount;
	private String min_face_amount;
	private String max_age_at_com;
	private String min_age_at_com;
	private String rating_type_1;
	private String rating_type_2;
	private String max_benefit_expiry_age;


	// ..etc
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAge_def() {
		return age_def;
	}

	public void setAge_def(String age_def) {
		this.age_def = age_def;
	}

	public Product(String id, String age_def, String p_start_date, String p_end_date, String max_face_amount, String min_face_amount, String max_age_at_com, String min_age_at_com, String rating_type_1, String rating_type_2,String max_benefit_expiry_age) {
		super();
		this.id = id;
		this.age_def = age_def;
		this.p_start_date = p_start_date;
		this.p_end_date = p_end_date;
		this.max_face_amount = max_face_amount;
		this.min_face_amount = min_face_amount;
		this.max_age_at_com = max_age_at_com;
		this.min_age_at_com = min_age_at_com;
		this.rating_type_1 = rating_type_1;
		this.rating_type_2 = rating_type_2;
		this.max_benefit_expiry_age = max_benefit_expiry_age;
	}

	public String getP_start_date() {
		return p_start_date;
	}

	public void setP_start_date(String p_start_date) {
		this.p_start_date = p_start_date;
	}

	public String getP_end_date() {
		return p_end_date;
	}

	public void setP_end_date(String p_end_date) {
		this.p_end_date = p_end_date;
	}

	public String getMax_face_amount() {
		return max_face_amount;
	}

	public void setMax_face_amount(String max_face_amount) {
		this.max_face_amount = max_face_amount;
	}

	public String getMin_face_amount() {
		return min_face_amount;
	}

	public void setMin_face_amount(String min_face_amount) {
		this.min_face_amount = min_face_amount;
	}

	public String getMax_age_at_com() {
		return max_age_at_com;
	}

	public void setMax_age_at_com(String max_age_at_com) {
		this.max_age_at_com = max_age_at_com;
	}

	public String getMin_age_at_com() {
		return min_age_at_com;
	}

	public void setMin_age_at_com(String min_age_at_com) {
		this.min_age_at_com = min_age_at_com;
	}

	public String getRating_type_1() {
		return rating_type_1;
	}

	public void setRating_type_1(String rating_type_1) {
		this.rating_type_1 = rating_type_1;
	}

	public String getRating_type_2() {
		return rating_type_2;
	}

	public void setRating_type_2(String rating_type_2) {
		this.rating_type_2 = rating_type_2;
	}


	public String getMax_benefit_expiry_age() {
		return max_benefit_expiry_age;
	}

	public void setMax_benefit_expiry_age(String max_benefit_expiry_age) {
		this.max_benefit_expiry_age = max_benefit_expiry_age;
	}
	
}
