package com.mitti.entrepreneur.model;

public class BasicProduct extends AbstractDeliverable {
    private final double costFactor = 1;

    public BasicProduct(String companyName, String productName) {
        super();
        this.companyName = companyName;
        this.productName = productName;
    }

    @Override
    public double getCostFactor() {
        return this.costFactor;
    }

    @Override
    public void createProduct(AbstractDeliverable deliverable) {

    }

}
