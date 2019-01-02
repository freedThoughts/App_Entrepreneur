package com.mitti.entrepreneur.model;

public class DurableProduct extends ProductDecorator {
    private final double costFactor = 0.8;

    public DurableProduct(AbstractDeliverable deliverable) {
        super(deliverable);
    }

    @Override
    public double getCostFactor() {
        return super.getCostFactor() * costFactor;
    }
}
