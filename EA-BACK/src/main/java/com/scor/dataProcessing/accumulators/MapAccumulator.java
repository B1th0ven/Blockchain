package com.scor.dataProcessing.accumulators;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.spark.util.AccumulatorV2;



public class MapAccumulator extends AccumulatorV2<Map<String, List<String>>, Map<String, List<String>>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, List<String>> myHM = new HashMap<>();

	public MapAccumulator() {
		this(new HashMap<>());
	}

	public MapAccumulator(Map<String, List<String>> myHM_) {
		if (myHM_ != null) {
			this.myHM = myHM_;
		}
	}

	@Override
	public void add(Map<String, List<String>> to_add) {
		if (to_add != null) {
			to_add.entrySet().forEach(entry -> {
				String key = entry.getKey();
				List<String> value = myHM.get(key);
				if (value != null) {
					for (String v : entry.getValue()) {
						if (!value.contains(v)) {
							List<String> valuesList = new ArrayList<>();
							valuesList.addAll(value);
							valuesList.add(v);
							myHM.put(key,valuesList);
						}
					}
				} else {
					myHM.put(key, entry.getValue());
				}
			});
		}
	}

	@Override
	public AccumulatorV2<Map<String, List<String>>, Map<String, List<String>>> copy() {
		return new MapAccumulator(myHM);
	}

	@Override
	public boolean isZero() {
		return (myHM.isEmpty());
	}

	@Override
	public void merge(AccumulatorV2<Map<String, List<String>>, Map<String, List<String>>> other) {
		add(other.value());
	}

	@Override
	public void reset() {
		myHM = new HashMap<String, List<String>>();
	}

	@Override
	public Map<String, List<String>> value() {
		return myHM;
	}

}
