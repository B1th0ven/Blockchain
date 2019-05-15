package com.scor.dataProcessing.dataChecker.functionalChecker.Controls;


import java.io.Serializable;
import java.util.List;

import com.scor.dataProcessing.models.Product;

public interface IRule extends Serializable{
    void validate(String[] row, Product product, List<String> headers);
}