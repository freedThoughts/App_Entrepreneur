package com.mitti.entrepreneur.util;

import com.mitti.entrepreneur.model.AbstractDeliverable;

public class Adaptor extends AbstractDeliverable {

    private AbstractDeliverable deliverable;

    public void createProduct(final AbstractDeliverable deliverable) {
        this.deliverable = deliverable;
    }

    @Override
    public double getCostFactor() {
        return deliverable.getCostFactor();
    }
}
