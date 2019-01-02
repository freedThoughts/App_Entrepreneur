package com.mitti.entrepreneur.model;

public class ExtensibleProduct extends ProductDecorator{

    private final double costFactor = 1.2;

    public ExtensibleProduct(AbstractDeliverable deliverable) {
        super(deliverable);
    }


    @Override
    public double getCostFactor() {
        return super.getCostFactor() * costFactor;
    }
}
