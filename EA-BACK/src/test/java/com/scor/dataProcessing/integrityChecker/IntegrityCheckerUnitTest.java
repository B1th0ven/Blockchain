package com.scor.dataProcessing.integrityChecker;

import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.api.java.JavaRDD;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.io.Resources;
import com.scor.dataProcessing.common.DataPivot;
import com.scor.dataProcessing.dataChecker.DCFactory;
import com.scor.dataProcessing.dataChecker.integrityChecker.FormatControlUtility;
import com.scor.dataProcessing.dataChecker.integrityChecker.IntegrityCheckerRegister;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResults;
import com.scor.dataProcessing.sparkConnection.Connection;
@Ignore
public class IntegrityCheckerUnitTest {
	private Map<String, List<String>> refData = new HashMap<>();

	@Before
	public void init() {
		List<String> region_of_residence = Arrays.asList("fra_auvergne-rhône-alpes", "fra_bourgogne-franche-comté",
				"fra_bretagne", "fra_centre-val-de-loire", "fra_corse");
		List<String> treaty_number_omega = Arrays.asList("tra017030", "tr0010761", "tra018360", "tr0007113",
				"tra006380", "tr0010765", "tr0007114");
		refData.put("region_of_residence", region_of_residence);
		refData.put("treaty_number_omega", treaty_number_omega);
		DataPivot.setProductPivotFile("pivot/ProductPivotTest.csv");
		DataPivot.setPolicyPivotFile("pivot/PolicyPivotTest.csv");
	}

	@Test
	public void productTestRunner() throws URISyntaxException {
		ControlResults result = DCFactory.getIntegrityChecker().run(refData,
				Resources.getResource("data/TestData.csv").getPath(), "product", IntegrityCheckerRegister.PRODUCT,"","");

		for (AffectedColumn i : result.getControlResultsList().get(0).getAffectedColumns()) {
			if ("Date".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 6);

			if ("DateMandatory".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 7);
		}

		for (AffectedColumn i : result.getControlResultsList().get(1).getAffectedColumns()) {

			if ("SimpleNum".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 2);

			if ("NumInterger".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 4);

			if ("NumIntergerNotNull".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 2);

			if ("NumNotNull".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 2);

			if ("NumLessThanOne".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 6);

			if ("NumLessThanOneNotNull".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 5);

			if ("simpleNumPos".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 3);

			if ("NumPosInteger".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 6);

			if ("NumPosIntegerNotNull".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 4);

			if ("NumPosLessThanOne".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 7);

			if ("Age".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 4);

			if ("AgeMandatory".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 5);

		}

		for (AffectedColumn i : result.getControlResultsList().get(2).getAffectedColumns()) {
			if ("Code".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 6);

			if ("CodeMandatory".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 5);
		}

		for (AffectedColumn i : result.getControlResultsList().get(3).getAffectedColumns()) {
			if ("region_of_residence".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 6);

			if ("treaty_number_omega".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 3);
		}

	}

	@Test
	public void policyTestRunner() throws URISyntaxException {
		ControlResults result = DCFactory.getIntegrityChecker().run(refData,
				Resources.getResource("data/TestData.csv").getPath(), "product", IntegrityCheckerRegister.POLICY,"","");

		for (AffectedColumn i : result.getControlResultsList().get(0).getAffectedColumns()) {
			if ("Date".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 6);

			if ("DateMandatory".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 7);
		}

		for (AffectedColumn i : result.getControlResultsList().get(1).getAffectedColumns()) {

			if ("SimpleNum".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 2);

			if ("NumInterger".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 4);

			if ("NumIntergerNotNull".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 2);

			if ("NumNotNull".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 2);

			if ("NumLessThanOne".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 6);

			if ("NumLessThanOneNotNull".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 5);

			if ("simpleNumPos".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 3);

			if ("NumPosInteger".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 6);

			if ("NumPosIntegerNotNull".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 4);

			if ("NumPosLessThanOne".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 7);

			if ("Age".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 4);

			if ("AgeMandatory".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 5);

		}

		for (AffectedColumn i : result.getControlResultsList().get(2).getAffectedColumns()) {
			if ("Code".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 6);

			if ("CodeMandatory".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 5);
		}

		for (AffectedColumn i : result.getControlResultsList().get(3).getAffectedColumns()) {
			if ("region_of_residence".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 6);

			if ("treaty_number_omega".equalsIgnoreCase(i.getName()))
				assertEquals(i.getErrorsNumber(), 3);
		}
	}

	@Test
	public void DecimalSeparatorRunner() throws URISyntaxException {
		JavaRDD<String> data = Connection.getContext().textFile(
				IntegrityCheckerUnitTest.class.getClassLoader().getResource("data/DecimalSeparator.csv").getPath());
		String header = "SimpleNum;SimpleNum2;SimpleNum3;SimpleNum4;SimpleNum5;SimpleNum6;SimpleNum7;SimpleNum8;SimpleNum9";
		List<String> types = Arrays.asList("numeric", "numeric+", "numeric", "numeric+", "numeric", "numeric+",
				"numeric", "numeric", "age");
		List<String> names = Arrays.asList("SimpleNum", "SimpleNum2", "SimpleNum3", "SimpleNum4", "SimpleNum5",
				"SimpleNum6", "SimpleNum7", "SimpleNum8", "SimpleNum9");
		Map<String, String> resultMap = FormatControlUtility.getDecimalSeparator(data, header, types, names);
		Map<String, String> expectedResults = new HashMap<String, String>();
		expectedResults.put("SimpleNum4", ".");
		expectedResults.put("SimpleNum3", ",");
		expectedResults.put("SimpleNum6", ".");
		expectedResults.put("SimpleNum8", ".");
		expectedResults.put("SimpleNum2", ".");
		expectedResults.put("SimpleNum5", ".");
		expectedResults.put("SimpleNum", ",");
		expectedResults.put("SimpleNum7", ",");
		assertEquals(resultMap, expectedResults);
	}
}
