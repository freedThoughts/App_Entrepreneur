package com.mitti.entrepreneur.model;

public abstract class AbstractDeliverable {

    private ProfitMakingCompany profitMakingCompany;
    protected String companyName;

    protected String productName;
    private double productCost;

    public ProfitMakingCompany getProfitMakingCompany() {
        return profitMakingCompany;
    }

    public void setProfitMakingCompany(final ProfitMakingCompany profitMakingCompany) {
        this.profitMakingCompany = profitMakingCompany;
    }

    public abstract double getCostFactor();

    public abstract void createProduct(AbstractDeliverable deliverable);

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public double getProductCost() {
        return productCost;
    }

    public void setProductCost(double productCost) {
        this.productCost = productCost;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
