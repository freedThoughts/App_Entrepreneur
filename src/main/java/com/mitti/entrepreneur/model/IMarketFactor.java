package com.mitti.entrepreneur.model;

import java.text.DecimalFormat;

public interface IMarketFactor {
    DecimalFormat decimalFormat = new DecimalFormat(".####");
    default void accept(MarketSituation marketSituation) {
        marketSituation.process(this);
    }
    double factorValue();
    double getFactorDegree();
    void setFactorDegree(int factorDegree);
    void calculateDegree(int degreeChange);
    void printableFactorValue ();
    double weightedFactorValue();

}
