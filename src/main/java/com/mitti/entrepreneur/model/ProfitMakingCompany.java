package com.mitti.entrepreneur.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(
        name = "company",
        uniqueConstraints = @UniqueConstraint(name = "owner_name", columnNames = {"owner_name"})
)
public class ProfitMakingCompany implements ICompany{

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "fund")
    private double fund;

    @Column(name = "interest_rate")
    private double interestRate;  // interest rate on fund per business decision

    @Column(name = "revenue")
    private double revenue;

    @Column(name = "stock_price")
    private double stockPrice = 100.00;

    @Column(name = "reputation_factor")
    private double reputationFactor = 1;

    @Column(name = "liability_factor")
    private double liabilityFactor = 1;

    @Column(name = "engineers")
    private int engineers;

    @Column(name = "executives")
    private int executives;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Double> sharePriceRecord = new ArrayList<Double>();

    @Transient
    private Set<AbstractDeliverable> deliverables = new HashSet<>();

    public ProfitMakingCompany() {
    }

    public void addDeliverable(AbstractDeliverable deliverable) {
        this.deliverables.add(deliverable);
        deliverable.setProfitMakingCompany(this);
    }

    public void removeDeliverable(AbstractDeliverable deliverable) {
        this.deliverables.remove(deliverable);
        deliverable.setProfitMakingCompany(null);
    }

    public List<Double> getSharePriceRecord() {
        return sharePriceRecord;
    }

    public void setSharePriceRecord(List<Double> sharePriceRecord) {
        this.sharePriceRecord = sharePriceRecord;
    }

    private ProfitMakingCompany(CompanyBuilder builder) {
        this.ownerName = builder.ownerName;
        this.companyName = builder.companyName;
        this.fund = builder.fund;
        this.interestRate = builder.interestRate;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getCompanyName() {
        return companyName;
    }


    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }

    public double getReputationFactor() {
        return reputationFactor;
    }

    public void setReputationFactor(double reputationFactor) {
        this.reputationFactor = reputationFactor;
    }

    public double getLiabilityFactor() {
        return liabilityFactor;
    }

    public void setLiabilityFactor(double liabilityFactor) {
        this.liabilityFactor = liabilityFactor;
    }

    public int getEngineers() {
        return engineers;
    }

    public void setEngineers(int engineers) {
        this.engineers = engineers;
    }

    public int getExecutives() {
        return executives;
    }

    public void setExecutives(int executives) {
        this.executives = executives;
    }

    public double getFund() {
        return fund;
    }

    public void setFund(double fund) {
        this.fund = fund;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Set<AbstractDeliverable> getDeliverables() {
        return deliverables;
    }

    public void setDeliverables(Set<AbstractDeliverable> deliverables) {
        this.deliverables = deliverables;
    }

    // Builder Design Pattern
    public static class CompanyBuilder {
        private String ownerName;
        private String companyName;
        private double fund;
        private double interestRate;  // interest rate on fund per business decision

        public CompanyBuilder(String ownerName, String companyName){
            this.ownerName = ownerName;
            this.companyName = companyName;
        }


        public CompanyBuilder setFund(double fund) {
            this.fund = fund;
            return this;
        }

        public CompanyBuilder setInterestRate(double interestRate) {
            this.interestRate = interestRate;
            return this;
        }

        public ProfitMakingCompany build(){
            return new ProfitMakingCompany(this);
        }
    }

}
