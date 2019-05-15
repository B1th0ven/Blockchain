package com.scor.dataProcessing.dataChecker.functionalChecker.Controls;

import org.apache.spark.api.java.function.VoidFunction;

import com.scor.dataProcessing.models.Product;

import java.util.List;
import java.util.Map;

public class MainControls implements VoidFunction<String> {
    List<IRule> rules;
    String header;
    List<String> names;
    Map<String, Product> products;

    public MainControls(List<IRule> rules, String header, List<String> names,Map<String, Product> products){
        this.rules = rules;
        this.header = header;
        this.names = names;
        this.products = products;
    }
    /**
     *
     */
    private static final long serialVersionUID = -7136621943484045923L;

    @Override
    public void call(String row) throws Exception {
        if (!row.equals("") & !row.equalsIgnoreCase(header)) {
            String[] row_arr = row.toLowerCase().trim().split(";", -1);
            Product product = products.get(row_arr[names.indexOf("product_id")].trim());
            for ( IRule rule : rules){
                rule.validate(row_arr,product,names);
            }
        }
    }
}
