package com.scor.dataProcessing.common;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MarketScopes {

	private static List<RefMarketScope> refMarketScopes = null;

	public static List<RefMarketScope> getMarketScopes() {
		if (refMarketScopes == null) {
			refMarketScopes = initializeMarketScopes();
		}
		return refMarketScopes;
	}

	private static List<RefMarketScope> initializeMarketScopes() {
		List<RefMarketScope> countryScopes = new ArrayList<>();
		Stream<String> lines = null;
		try {
			lines = Files.lines(
					Paths.get(DataPivot.class.getClassLoader().getResource("scope/MarketScopes.csv").toURI()),
					Charset.forName("Cp1252"));
			lines.skip(1).forEach(line -> {
				String[] arr = line.toLowerCase().split(";", -1);
				RefMarketScope countryScope = new RefMarketScope(arr[0], arr[1], arr[2]);
				countryScopes.add(countryScope);
			});
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		return countryScopes;
	}

	public static String getRegionalScopes(String scopeName, String scopeType) {
		List<RefMarketScope> refMarketScopes = getMarketScopes();
		for (RefMarketScope refMarketScope : refMarketScopes) {
			if (scopeName.trim().equalsIgnoreCase(refMarketScope.getScopeName())
					&& scopeType.trim().equalsIgnoreCase(refMarketScope.getScopeType())) {
				return refMarketScope.getRegionalScope();
			}
		}
		return null;
	}
}
