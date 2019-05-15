package com.scor.dataProcessing.dataChecker;

import com.scor.dataProcessing.dataChecker.functionalChecker.InterfaceToFunctionalChecker;
import com.scor.dataProcessing.dataChecker.integrityChecker.InterfaceToIntegrityChecker;
import com.scor.dataProcessing.dataChecker.notExecutedRuleChecker.InterfaceToNotExecutedRuleChecker;
import com.scor.dataProcessing.dataChecker.schemaChecker.InterfaceToSchemaChecker;

public class DCFactory {

	private static InterfaceToSchemaChecker schemaChecker = new InterfaceToSchemaChecker() {
		private static final long serialVersionUID = -3163597412942379560L;
	};
	private static InterfaceToIntegrityChecker integrityChecker = new InterfaceToIntegrityChecker() {
		private static final long serialVersionUID = -4757594305711837174L;
	};
	private static InterfaceToNotExecutedRuleChecker notExecutedRuleChecker = new InterfaceToNotExecutedRuleChecker() {
		private static final long serialVersionUID = -2907760453448453177L;
	};
	private static InterfaceToFunctionalChecker functionalChecker= new InterfaceToFunctionalChecker() {
		private static final long serialVersionUID = -7304499379326551517L;
	};

	public static InterfaceToSchemaChecker getSchemaChecker() {
		return schemaChecker ;
	}

	public static InterfaceToIntegrityChecker getIntegrityChecker() {
		return integrityChecker;
	}

	public static InterfaceToNotExecutedRuleChecker getNotExecutedRuleChecker() {
		return notExecutedRuleChecker;
	}

	public static InterfaceToFunctionalChecker getFunctionalChecker() {
		return functionalChecker;
	}

}
