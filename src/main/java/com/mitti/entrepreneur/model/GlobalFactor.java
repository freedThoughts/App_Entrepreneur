package com.mitti.entrepreneur.model;

import static com.mitti.entrepreneur.constants.Constant.MAXIMUM_DEGREE;

public class GlobalFactor implements IMarketFactor {
    private final double weight;
    private int factorDegree;

    public GlobalFactor(double weight, int factorDegree) {
        this.weight = weight;
        this.factorDegree = factorDegree;
    }

    public double weightedFactorValue() {
        return factorValue() * this.weight;
    }
    public void printableFactorValue () {
        System.out.println("\n\t\t GLOBAL FACTOR : " + decimalFormat.format(factorValue()) + "  WEIGHT : " + weight);
    }
    public double factorValue(){
        return Math.sin(Math.toRadians(this.factorDegree));
    }
    public double getFactorDegree() {
        return factorDegree;
    }

    public void setFactorDegree(int factorDegree) {
        this.factorDegree = factorDegree;
    }

    public void calculateDegree(int degreeChange) {
        this.factorDegree = (this.factorDegree + degreeChange) % MAXIMUM_DEGREE;
    }
}
