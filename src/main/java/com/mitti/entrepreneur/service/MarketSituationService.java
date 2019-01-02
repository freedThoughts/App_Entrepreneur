package com.mitti.entrepreneur.service;

import java.util.Scanner;

public interface MarketSituationService {
    double processMarketSituation();
    void accommodateMarketChanges();
    void getMarketSituations();

    void setDomesticFactorChangeDegree(Scanner scanner);
    void setCentralBankInterestChangeDegree(Scanner scanner);
    void setCurrencyDepreciationChangeDegree(Scanner scanner);
    void setGlobalFactorChangeDegree(Scanner scanner);
}
