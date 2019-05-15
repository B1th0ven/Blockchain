package com.scor.dataProcessing.dataChecker.functionalChecker;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;

import com.google.common.collect.Ordering;

public class ExpTableCalulator implements InterfaceToFunctionalChecker {

	private static final long serialVersionUID = -9083076409069537437L;

	private static final ExpTableCalulator instance = new ExpTableCalulator();

	private ExpTableCalulator() {
	}

	public static ExpTableCalulator getInstance() {
		return instance;
	};
	

	public HashMap<String, Long> runCalculator(String path, String type) {

		JavaRDD<String> data = sc.textFile(path);
		String header = data.first();
		List<String> names = Arrays.asList(header.toLowerCase().split(";", -1));

		Object ordering = Ordering.natural();
		final Comparator<Long> cmp = (Comparator<Long>) ordering;

		Long max_age = -1L;
		Long min_age = -1L;

		Long max_calendar = -1L;
		Long min_calendar = -1L;

		Long max_duration = -1L;
		Long min_duration = -1L;

		JavaRDD<String> data_without_header = data.filter(line -> !line.equalsIgnoreCase(header) && !line.isEmpty());
		JavaRDD<String[]> data_splitted = data_without_header.map(x -> x.toLowerCase().trim().split(";"));
		data_splitted.cache();

		if (names.contains("insurance_age_attained")) {
			JavaRDD<Long> rdd = data_splitted.map(s -> !(s.length <= names.indexOf("insurance_age_attained"))
					&& s[names.indexOf("insurance_age_attained")].matches("^-?\\d{1,19}$")
							? Long.parseLong(s[names.indexOf("insurance_age_attained")])
							: null)
					.filter(s -> s != null);
			rdd.cache();
			max_age = rdd.max(cmp);
			min_age = rdd.min(cmp);
			rdd.unpersist();
		}
		if (names.contains("age_attained") && max_age == -1L && min_age == -1L) {
			JavaRDD<Long> rdd = data_splitted.map(s -> !(s.length <= names.indexOf("age_attained"))
					&& s[names.indexOf("age_attained")].matches("^-?\\d{1,19}$")
							? Long.parseLong(s[names.indexOf("age_attained")])
							: null)
					.filter(s -> s != null);
			HashMap<String, Long> MinMax = FindMinAndMax(rdd, cmp);
			if (MinMax != null) {
				min_age = MinMax.get("min");
				max_age = MinMax.get("max");
			}

		}
		if (names.contains("age_at_commencement") && max_age == -1L && min_age == -1L) {
			JavaRDD<Long> rdd = data_splitted.map(s -> !(s.length <= names.indexOf("age_at_commencement"))
					&& s[names.indexOf("age_at_commencement")].matches("^-?\\d{1,19}$")
							? Long.parseLong(s[names.indexOf("age_at_commencement")])
							: null)
					.filter(s -> s != null);
			HashMap<String, Long> MinMax = FindMinAndMax(rdd, cmp);
			if (MinMax != null) {
				min_age = MinMax.get("min");
				max_age = MinMax.get("max");
			}
		}
		if (names.contains("duration_year")) {
			JavaRDD<Long> rdd = data_splitted.map(s -> !(s.length <= names.indexOf("duration_year"))
					&& s[names.indexOf("duration_year")].matches("^-?\\d{1,19}$")
							? Long.parseLong(s[names.indexOf("duration_year")])
							: null)
					.filter(s -> s != null);
			HashMap<String, Long> MinMax = FindMinAndMax(rdd, cmp);
			if (MinMax != null) {
				min_duration = MinMax.get("min");
				max_duration = MinMax.get("max");
			}
		}
		if (names.contains("calendar_year")) {
			JavaRDD<Long> rdd = data_splitted.map(s -> !(s.length <= names.indexOf("calendar_year"))
					&& s[names.indexOf("calendar_year")].matches("^-?\\d{1,19}$")
							? Long.parseLong(s[names.indexOf("calendar_year")])
							: null)
					.filter(s -> s != null);
			HashMap<String, Long> MinMax = FindMinAndMax(rdd, cmp);
			if (MinMax != null) {
				min_calendar = MinMax.get("min");
				max_calendar = MinMax.get("max");
			}

		}
		data_splitted.unpersist();

		HashMap<String, Long> hmp = new HashMap<>();
		hmp.put("max_age", max_age);
		hmp.put("min_age", min_age);
		hmp.put("max_calendar", max_calendar);
		hmp.put("min_calendar", min_calendar);
		hmp.put("max_duration", max_duration);
		hmp.put("min_duration", min_duration);

		return hmp;

	}

	private static HashMap<String, Long> FindMinAndMax(JavaRDD<Long> rdd, Comparator<Long> cmp) {

		if (!rdd.isEmpty()) {
			HashMap<String, Long> res = new HashMap<>();
			rdd.cache();
			res.put("max", rdd.max(cmp));
			res.put("min", rdd.min(cmp));
			rdd.unpersist();
			return res;
		}
		return null;
	}

}
