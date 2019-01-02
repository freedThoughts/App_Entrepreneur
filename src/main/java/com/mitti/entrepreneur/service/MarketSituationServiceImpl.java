package com.mitti.entrepreneur.service;

import com.mitti.entrepreneur.model.*;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import static com.mitti.entrepreneur.constants.Constant.*;

@Service("marketSituationService")
public class MarketSituationServiceImpl implements MarketSituationService {
    private static final Random random = new Random();
    private final Set<IMarketFactor> marketFactors;
    private int globalFactorChangeDegree;
    private int domesticFactorChangeDegree;
    private int centralBankInterestChangeDegree;
    private int currencyDepreciationChangeDegree;

    public MarketSituationServiceImpl() {
        this.marketFactors = new HashSet<>(COUNT_OF_MARKET_FACTORS * 4/3 +1);
        this.marketFactors.add(new GlobalFactor(GLOBAL_FACTOR_WEIGHT, random.nextInt(MAXIMUM_DEGREE)));
        this.marketFactors.add(new DomesticFactor(DOMESTIC_FACTOR_WEIGHT, random.nextInt(MAXIMUM_DEGREE)));
        this.marketFactors.add(new CentralBankFactor(CENTRAL_BANK_INTEREST_WEIGHT, random.nextInt(MAXIMUM_DEGREE)));
        this.marketFactors.add(new CurrencyFluctuationFactor(CURRENCY_DEPRECIATION_WEIGHT, random.nextInt(MAXIMUM_DEGREE)));

        this.globalFactorChangeDegree = GLOBAL_FACTOR_CHANGE_DEGREE;
        this.domesticFactorChangeDegree = DOMESTIC_FACTOR_CHANGE_DEGREE;
        this.centralBankInterestChangeDegree = CENTRAL_BANK_INTEREST_FLUCTUATION;
        this.currencyDepreciationChangeDegree = CURRENCY_FLUCTUATION_DEGREE;
    }

    // Visitor Design Pattern
    private MarketSituation accommodateMarketChanges = marketFactor -> {
        if (marketFactor instanceof GlobalFactor) {
            marketFactor.calculateDegree(globalFactorChangeDegree);
        } else if (marketFactor instanceof DomesticFactor) {
            marketFactor.calculateDegree(domesticFactorChangeDegree);
        } else if (marketFactor instanceof CentralBankFactor) {
            marketFactor.calculateDegree(centralBankInterestChangeDegree);
        } else if (marketFactor instanceof CurrencyFluctuationFactor)
            marketFactor.calculateDegree(currencyDepreciationChangeDegree);
    };

    private MarketSituation enquireMarketSituation = marketFactor ->  marketFactor.printableFactorValue();

    private AggregateMarketFactor aggregateMarketFactor = marketFactor -> marketFactor.weightedFactorValue();

    @Override
    public double processMarketSituation() {
        this.accommodateMarketChanges();
        return this.marketFactors.stream().mapToDouble(e -> aggregateMarketFactor.process(e)).reduce(1, (a, b) -> a * b);
    }

    @Override
    public void accommodateMarketChanges() {
        this.marketFactors.stream().forEach(e -> accommodateMarketChanges.process(e));
    }

    @Override
    public void getMarketSituations() {
        System.out.println("\n\t\t MARKET CURRENT SITUATION ");
        this.marketFactors.stream().forEach(e -> enquireMarketSituation.process(e));
    }

    @Override
    public void setDomesticFactorChangeDegree(Scanner scanner) {
        System.out.println("\n\t YOU ARE GOING TO CHANE PACE OF CHANGE OF DOMESTIC FACTOR");
        this.printInstruction();
        this.domesticFactorChangeDegree = Integer.valueOf(scanner.nextLine().split(" ")[0]);
    }

    @Override
    public void setCentralBankInterestChangeDegree(Scanner scanner) {
        System.out.println("\n\t YOU ARE GOING TO CHANE PACE OF CHANGE OF CENTRAL BANK INTEREST RATE");
        this.printInstruction();
        this.centralBankInterestChangeDegree = Integer.valueOf(scanner.nextLine().split(" ")[0]);
    }

    @Override
    public void setCurrencyDepreciationChangeDegree(Scanner scanner) {
        System.out.println("\n\t YOU ARE GOING TO CHANE PACE OF CURRENCY FLUCTUATION");
        this.printInstruction();
        this.currencyDepreciationChangeDegree = Integer.valueOf(scanner.nextLine().split(" ")[0]);
    }

    @Override
    public void setGlobalFactorChangeDegree(Scanner scanner) {
        System.out.println("\n\t YOU ARE GOING TO CHANE PACE OF GLOBAL FACTOR" );
        this.printInstruction();
        this.globalFactorChangeDegree = Integer.valueOf(scanner.nextLine().split(" ")[0]);
    }

    // To Avoid duplicate code
    private void printInstruction(){
        System.out.println("\n\t ENTER A NUMBER BETWEEN 0 TO 180," +
                "\n\t\t\t\t 0 BEING LEAST FLUCTUATION AND 180 BEING MOST FLUCTUATION");
    }
}
