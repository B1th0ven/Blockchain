package com.scor.dataProcessing.accumulators;

import java.util.HashMap;

import org.apache.spark.util.AccumulatorV2;

public class HashMapAccumulator extends AccumulatorV2<HashMap<String, String>, HashMap<String, String>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<String, String> myHM = new HashMap<>();

	public HashMapAccumulator() {
		this(new HashMap<>());
	}

	public HashMapAccumulator(HashMap<String, String> myHM_) {
		if (myHM_ != null) {
			this.myHM = myHM_;
		}
	}

	@Override
	public void add(HashMap<String, String> to_add) {
		if (to_add != null) {
			myHM.putAll(to_add);
		}
	}

	@Override
	public AccumulatorV2<HashMap<String, String>, HashMap<String, String>> copy() {
		return new HashMapAccumulator(myHM);
	}

	@Override
	public boolean isZero() {
		return (myHM.isEmpty());
	}

	@Override
	public void merge(AccumulatorV2<HashMap<String, String>, HashMap<String, String>> other) {
		add(other.value());
	}

	@Override
	public void reset() {
		myHM = new HashMap<String, String>();
	}

	@Override
	public HashMap<String, String> value() {
		return myHM;
	}

}
