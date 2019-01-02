package com.mitti.entrepreneur.model;

// Demonstrate Decorator Design Pattern
public class ProductDecorator extends AbstractDeliverable{

    private AbstractDeliverable deliverable;
    ProductDecorator(AbstractDeliverable deliverable){
        super();
        this.deliverable = deliverable;
        if (deliverable.getProductName() != null)
            this.productName = deliverable.productName;
        if (deliverable.getCompanyName() != null)
            this.companyName = deliverable.companyName;
    }

    public double getCostFactor() {
        return deliverable.getCostFactor();
    }

    public void createProduct(AbstractDeliverable deliverable) {

    }
}
