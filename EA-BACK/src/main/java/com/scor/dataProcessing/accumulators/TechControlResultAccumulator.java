package com.scor.dataProcessing.accumulators;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.util.AccumulatorV2;

import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.TechControlResult;

public class TechControlResultAccumulator extends AccumulatorV2<TechControlResult, TechControlResult> {

	private TechControlResult res = new TechControlResult();

	public TechControlResultAccumulator() {
		this(new TechControlResult());
	}

	public TechControlResultAccumulator(TechControlResult res) {
		if (res != null) {
			this.res = res;
		}
	}

	@Override
	public void add(TechControlResult arg) {
		List<AffectedColumn> affectedColumns = arg.getAffectedColumns();
		List<AffectedColumn> curr_affectedColumns = this.res.getAffectedColumns();
		for (AffectedColumn ac : affectedColumns) {
			boolean found = false;
			for (AffectedColumn curr_ac : curr_affectedColumns) {
				if (curr_ac.getName().equals(ac.getName())) {
					found = true;
					curr_ac.setErrorsNumber(curr_ac.getErrorsNumber() + ac.getErrorsNumber());
					if (curr_ac.getExamples().size() < 5) {
						// if (true) {
						ArrayList<String> examples = curr_ac.getExamples();
						examples.addAll(ac.getExamples());
						curr_ac.setExamples(examples);
					}
					break;
				}
			}
			if (!found) {
				curr_affectedColumns.add(ac);
				this.res.setAffectedColumns(curr_affectedColumns);
			}
		}
	}

	@Override
	public AccumulatorV2<TechControlResult, TechControlResult> copy() {
		return new TechControlResultAccumulator(res);
	}

	@Override
	public boolean isZero() {
		return res.getAffectedColumns().isEmpty();
	}

	@Override
	public void merge(AccumulatorV2<TechControlResult, TechControlResult> arg0) {
		add(arg0.value());
	}

	@Override
	public void reset() {
		res = new TechControlResult();
	}

	@Override
	public TechControlResult value() {
		return res;
	}
}
