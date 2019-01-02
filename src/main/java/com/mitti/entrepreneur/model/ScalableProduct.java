package com.mitti.entrepreneur.model;

public class ScalableProduct extends ProductDecorator{

    private final double costFactor = 1.1;

    public ScalableProduct(AbstractDeliverable deliverable) {
        super(deliverable);
    }

    @Override
    public double getCostFactor() {
        return super.getCostFactor() * costFactor;
    }
}
