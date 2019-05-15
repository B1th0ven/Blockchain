package com.scor.dataProcessing.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scor.dataProcessing.models.Product;

public class DataProduct implements Serializable {

	private List<Product> productsDuplicated = null;
	private Map<String, Product> products = null;

	private void getProducts(String path_prod) throws IOException {
		String age_def = null;
		String p_start_date = null;
		String p_end_date = null;
		String max_face_amount = null;
		String min_face_amount = null;
		String max_age_at_com = null;
		String min_age_at_com = null;
		String max_benf_exp_age = null;

		productsDuplicated = new ArrayList<>();
		products = new HashMap<>();
		String row = null;

		FileReader fr = new FileReader(path_prod);
		BufferedReader br = new BufferedReader(fr);

		String header = br.readLine();
		List<String> names = Arrays.asList(header.toLowerCase().trim().split(";", -1));

		boolean isAgeAtCommencementDefinitionPresent = names.contains("age_at_commencement_definition");
		boolean isProductStartDatePresent = names.contains("product_start_date");
		boolean isProductEndDatePresent = names.contains("product_end_date");
		boolean isMaxFaceAmountPresent = names.contains("max_face_amount");
		boolean isMinFaceAmountPresent = names.contains("min_face_amount");
		boolean isMaxAgeAtCommencementPresent = names.contains("max_age_at_commencement");
		boolean isMinAgeAtCommencementPresent = names.contains("min_age_at_commencement");
		boolean isRatingType1 = names.contains("rating_type_1");
		boolean isRatingType2 = names.contains("rating_type_2");
		boolean isMaxBenExpAge = names.contains("max_benefit_expiry_age");

		while ((row = br.readLine()) != null) {
			if (!"".equals(row)) {
				String ratingType1 = null;
				String ratingType2 = null;
				String[] row_arr = row.toLowerCase().trim().split(";", -1);
				if (isAgeAtCommencementDefinitionPresent)
					age_def = row_arr[names.indexOf("age_at_commencement_definition")].trim();
				if (isProductStartDatePresent)
					p_start_date = row_arr[names.indexOf("product_start_date")].trim();
				if (isProductEndDatePresent)
					p_end_date = row_arr[names.indexOf("product_end_date")].trim();
				if (isMaxFaceAmountPresent)
					max_face_amount = row_arr[names.indexOf("max_face_amount")].trim();
				if (isMinFaceAmountPresent)
					min_face_amount = row_arr[names.indexOf("min_face_amount")].trim();
				if (isMaxAgeAtCommencementPresent)
					max_age_at_com = row_arr[names.indexOf("max_age_at_commencement")].trim();
				if (isMinAgeAtCommencementPresent)
					min_age_at_com = row_arr[names.indexOf("min_age_at_commencement")].trim();
				
				if (isRatingType1)
					ratingType1 = row_arr[names.indexOf("rating_type_1")].trim();
				if (isRatingType2)
					ratingType2 = row_arr[names.indexOf("rating_type_2")].trim();
				if(isMaxBenExpAge)
					max_benf_exp_age = row_arr[names.indexOf("max_benefit_expiry_age")].trim();

				Product product = new Product(row_arr[names.indexOf("product_id")], age_def, p_start_date, p_end_date,
						max_face_amount, min_face_amount, max_age_at_com, min_age_at_com,ratingType1,ratingType2,max_benf_exp_age);
				if (products != null
						&& products.get(row_arr[names.indexOf("product_id")].trim()) != null) {
					productsDuplicated.add(product);
				}
				products.put(row_arr[names.indexOf("product_id")].trim(), product);
			}
		}

	}

	public List<Product> getDuplicatedProduct(String path_prod) throws IOException {
		if (this.products == null) {
			getProducts(path_prod);
		}
		return productsDuplicated;
	}

	public Map<String, Product> getProduct(String path_prod) throws IOException {
		if (this.products == null) {
			getProducts(path_prod);
		}
		return this.products;
	}

}
