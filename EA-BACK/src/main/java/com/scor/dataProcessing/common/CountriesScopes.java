package com.scor.dataProcessing.common;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class CountriesScopes {

	private static Map<String,RefCountryScope> refCountryScopes = null;

	public static Map<String,RefCountryScope> getCountriesScopes() {
		if (refCountryScopes == null) {
			refCountryScopes = initializeCountriesScopes();
		}
		return refCountryScopes;
	}
	
	public static RefCountryScope getCountryScope(String countryCode) {
		Map<String,RefCountryScope> countryScopes = getCountriesScopes();
		return countryScopes.get(countryCode.toLowerCase());
	}

	private static Map<String,RefCountryScope> initializeCountriesScopes() {
		Map<String,RefCountryScope> countryScopes = new HashMap<>();

		Stream<String> lines = null;
		try {
			lines = Files.lines(Paths.get(DataPivot.class.getClassLoader().getResource("scope/countriesScopes.csv").toURI()),
					Charset.forName("Cp1252"));
			lines.skip(1).forEach(line -> {
				String[] arr = line.toLowerCase().split(";", -1);
				RefCountryScope countryScope = new RefCountryScope(arr[0], arr[1], arr[2], arr[3]);
				countryScopes.put(arr[0],countryScope);
			});
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		return countryScopes;
	}

}
