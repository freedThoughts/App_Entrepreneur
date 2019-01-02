package com.mitti.entrepreneur.service;

import com.mitti.entrepreneur.exception.InSufficientFund;
import com.mitti.entrepreneur.exception.InsufficientResource;
import com.mitti.entrepreneur.exception.InvalidInputException;
import com.mitti.entrepreneur.model.*;
import com.mitti.entrepreneur.repository.ProfitMakingCompanyRepository;
import com.mitti.entrepreneur.util.Adaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.Scanner;

import static com.mitti.entrepreneur.constants.Constant.*;

@Service("mainMenuService")
public class MainMenuServiceImpl implements MainMenuService {

    private static final DecimalFormat decimalFormat = new DecimalFormat(".##");

    @Autowired
    private ProfitMakingCompanyRepository profitMakingCompanyRepository;
    @Autowired
    private MarketSituationService marketSituationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MainMenuServiceImpl.class);
    private final Scanner scanner;

    // It can be done easier way.
    // Added adaptor to demonstrate Adaptor design pattern through Composition
    private final AbstractDeliverable adaptor;

    public MainMenuServiceImpl(){
        this.scanner = new Scanner(System.in);
        this.adaptor = new Adaptor();
    }

    @Override
    public void initializer() {
        System.out.println("\t  WELCOME TO ENTREPRENEUR GAME " +
                "\n \t  LIKE START YOUR OWN BUSINESS ");
        this.preProcess();
    }

    private void preProcess(){
        try {
            this.process();
        } catch (Exception e) {
            LOGGER.error("Error encountered ", e);
            System.out.println("\t  WARNING: " + e.getMessage() +
                    "\n\t\t YOU ARE WILLING TO LEAVE THE GAME + " +
                    "\n\t PRESS Y TO CONTINUE OR ANY KEY TO EXIT THE GAME");
            if ("Y".equalsIgnoreCase(this.scanner.nextLine().split(" ")[0]))
                preProcess();
        }
    }

    private void process(){
        System.out.println("\n \t  PRESS 1 TO START THE GAME  OR 0 TO EXIT");
        Integer input = Integer.valueOf(scanner.nextLine().split(" ")[0]);
        if (input == 0) return;
        if (input != 1) throw new InvalidInputException(" WRONG VALUE ENTERED : " + input);
        System.out.println(" ENTER YOUR NAME, [Hint : Only Text]");
        final String ownerName = scanner.nextLine();
        ProfitMakingCompany profitMakingCompany = profitMakingCompanyRepository.findByOwnerName(ownerName);
        if (profitMakingCompany != null) {
            System.out.println("\n \t GIVEN NAME IS ALREADY REGISTERED, HAVING COMPANY : " + profitMakingCompany.getCompanyName() +
                    "\n\t WOULD YOU LIKE TO RESUME THE GAME" +
                    "\n \t PRESS Y TO RESUME OR N TO START NEW GAME");
            if ("N".equalsIgnoreCase(scanner.nextLine().split(" ")[0]))
                profitMakingCompany = createCompany(ownerName);
        } else profitMakingCompany = createCompany(ownerName);

        while (true) {
            System.out.println("PRESS 1 TO MAKE A BUSINESS DECISION" +
                    "\n \t\t 2 TO SAVE THE GAME TO PLAY LATTER" +
                    "\n \t\t 3 TO QUIT THE GAME");
            input = Integer.valueOf(scanner.nextLine().split(" ")[0]);
            if (input == 1)
                makeBusinessDecision(profitMakingCompany);
            else if (input == 2) {
                profitMakingCompanyRepository.save(profitMakingCompany);
                break;
            } else if (input == 3) {
                profitMakingCompanyRepository.delete(profitMakingCompany);
                break;
            }
            else throw new InvalidInputException(" WRONG VALUE ENTERED : " + input);
        }
    }

    private ProfitMakingCompany createCompany(final String ownerName){
        System.out.println(" ENTER NAME OF COMPANY, YOU WOULD LIKE OF START, [Hint : Only Text]");
        final String companyName = scanner.nextLine();
        System.out.println(" DO YOU NEED FINANCE FOR YOUR STARTUP \n\t Press Y for Yes or N for No ");

        // Demonstrate Builder Design Pattern
        ProfitMakingCompany.CompanyBuilder companyBuilder = new ProfitMakingCompany.CompanyBuilder(ownerName, companyName);
        if ("Y".equalsIgnoreCase(scanner.nextLine().split(" ")[0])) {
            System.out.println("ENTER THE AMOUNT, YOU NEED FOR YOUR STARTUP, [Hint : Only Number]");
            companyBuilder.setFund(Double.valueOf(scanner.nextLine().split(" ")[0]));
            System.out.println("ENTER THE INTEREST RATE PER BUSINESS DECISION LEVIED ON FINANCE, [Hint : Only Number] ");
            companyBuilder.setInterestRate(Double.valueOf(scanner.nextLine().split(" ")[0]));
        }

        final ProfitMakingCompany profitMakingCompany = companyBuilder.build();
        profitMakingCompanyRepository.save(profitMakingCompany);
        return profitMakingCompany;
    }

    private void makeBusinessDecision(ProfitMakingCompany profitMakingCompany) {
        System.out.println("YOUR COMPANY STATISTICS :- " +
                "\n\t YOUR NAME : " + profitMakingCompany.getOwnerName() +
                "\n\t COMPANY NAME : " + profitMakingCompany.getCompanyName() + ", PRESS 1 TO CHANGE " +
                "\n\t FUNDS AVAILABLE : " + profitMakingCompany.getFund() + ", PRESS 2 TO TAKE MORE FUND, IT WOULD IMPACT COMPANY REPUTATION" +
                "\n\t INTEREST ON FUNDS : " + profitMakingCompany.getInterestRate() +
                "\n\t NET REVENUE COLLECTED : " + profitMakingCompany.getRevenue() +
                "\n\t ENGINEERS WORKING FOR COMPANY : " + profitMakingCompany.getEngineers() + ", PRESS 3 TO HIRE MORE ENGINEER, IT WOULD COST : " + ENGINEER_COST + " PER ENGINEER" +
                "\n\t\t\t\t\t PRESS 4 TO LAYOFF ENGINEER, IT WOULD IMPACT COMPANY REPUTATION" +
                "\n\t EXECUTIVES WORKING FOR COMPANY : " + profitMakingCompany.getExecutives() + ", PRESS 5 TO HIRE MORE EXECUTIVE, IT WOULD COST : " + EXECUTIVE_COST + " PER EXECUTIVE" +
                "\n\t\t\t\t\t PRESS 6 TO LAYOFF EXECUTIVE, IT WOULD IMPACT COMPANY REPUTATION" +
                "\n\t REPUTATION IN FACTOR TERM : " + decimalFormat.format(profitMakingCompany.getReputationFactor()) + ", IT IMPACTS YOUR REVENUE AND STOCK PRICE" +
                "\n\t LIABILITY IN FACTOR TERM : " + decimalFormat.format(profitMakingCompany.getLiabilityFactor()) + ", IT IMPACTS REPUTATION POSITIVELY AND REVENUE NEGATIVELY " +
                "\n\t PRODUCTS YOU HAVE : " + profitMakingCompany.getDeliverables().stream().map(e -> e.getProductName()).reduce("", (a, b) -> a.concat(" ").concat(b).concat(" ")) +
                "\n\t\t\t\t PRESS 7 TO ADD PRODUCTS" +
                "\n\t ");
        this.marketSituationService.getMarketSituations();
        System.out.println( "\n\t PRESS 8 TO CHANGE PACE OF CHANGE OF MARKET FLUCTUATION" +
                "\n\t PRESS 10 TO RETURN MAIN MENU" +
                "\n\t STATISTIC OF YOUR STOCK PRICE : " + decimalFormat.format(profitMakingCompany.getStockPrice()));

        this.printStockValueChangeRecord(profitMakingCompany);
        System.out.println("\n\n");
        final int input = Integer.valueOf(scanner.nextLine().split(" ")[0]);
        try {
            switch (input) {
                case 1 : companyNameChange(profitMakingCompany); break;
                case 2 : setFund(profitMakingCompany); break;
                case 3 : hireEngineer(profitMakingCompany); break;
                case 4 : layOffEngineer(profitMakingCompany); break;
                case 5 : hireExecutive(profitMakingCompany); break;
                case 6 : layOffExecutive(profitMakingCompany); break;
                case 7 : addProduct(profitMakingCompany); break;
                case 8 : changePaceOfMarketFluctuation(); break;
                case 10 : return;
                default: throw new InvalidInputException(" WRONG VALUE ENTERED : " + input);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            System.out.println(e.getMessage());
            System.out.println("PRESS 0 TO RETRY,  1 TO RETURN MAIN MENU");
            if (0 == Integer.valueOf(this.scanner.nextLine().split(" ")[0]))
                this.makeBusinessDecision(profitMakingCompany);
        }
        this.marketSituationService.accommodateMarketChanges();
        calculateStockValue(profitMakingCompany);
    }

    private void changePaceOfMarketFluctuation() {
        System.out.println("\n\t PRESS 1 TO CHANGE PACE OF GLOBAL FACTOR" +
                "\n\t PRESS 2 TO CHANGE PACE OF DOMESTIC FACTOR" +
                "\n\t PRESS 3 TO CHANGE PACE OF CURRENCY FLUCTUATION" +
                "\n\t PRESS 4 TO CHANGE PACE OF CENTRAL BANK INTEREST RATE" +
                "\n\t PRESS 9 TO RETURN MAIN MENU");
        final int input = Integer.valueOf(this.scanner.nextLine().split(" ")[0]);
        switch (input) {
            case 1 : marketSituationService.setGlobalFactorChangeDegree(this.scanner); break;
            case 2 : marketSituationService.setDomesticFactorChangeDegree(this.scanner); break;
            case 3 : marketSituationService.setCurrencyDepreciationChangeDegree(this.scanner); break;
            case 4 : marketSituationService.setCentralBankInterestChangeDegree(this.scanner); break;
            case 9 : return;
            default: throw new InvalidInputException("INVALID INPUT : " + input);
        }
        this.changePaceOfMarketFluctuation();
    }

    private void printStockValueChangeRecord(ProfitMakingCompany company) {
        System.out.println(" YOUR COMPANY STOCK PRICE CHANGES RECORDS AS GRAPH :- ");
        company.getSharePriceRecord().stream().forEachOrdered(e -> {
            System.out.println("\t\t\t");
            for (int i =0 ; i < e; i+=20)
                System.out.print("*");
        });
    }

    @Transactional
    private void calculateStockValue(ProfitMakingCompany profitMakingCompany){
        final double stockValue = profitMakingCompany.getStockPrice()
                + (profitMakingCompany.getFund() +  profitMakingCompany.getRevenue()) / profitMakingCompany.getFund() *10
                + profitMakingCompany.getReputationFactor() * 50
                + 5 * ((1 - profitMakingCompany.getLiabilityFactor()) == 0 ? 1 : (1 - profitMakingCompany.getLiabilityFactor()))
                * (5+ marketSituationService.processMarketSituation());

        profitMakingCompany.setStockPrice(stockValue);
        profitMakingCompany.getSharePriceRecord().add(stockValue);
        profitMakingCompanyRepository.save(profitMakingCompany);
    }

    @Transactional
    private void companyNameChange(ProfitMakingCompany profitMakingCompany){
        System.out.println("ENTER THE NEW NAME FOR YOUR COMPANY");
        final String companyName = this.scanner.nextLine().split(" ")[0];
        profitMakingCompany.setCompanyName(companyName);
        this.profitMakingCompanyRepository.save(profitMakingCompany);

        // Observer Design pattern. Company acts as subject and Products acts as Observer
        profitMakingCompany.getDeliverables().stream().forEach(e -> e.setCompanyName(companyName));
    }

    @Transactional
    private void setFund(ProfitMakingCompany company) {
        System.out.println("\n ENTER THE FUND YOU NEED FOR YOUR BUSINESS");
        final double newFund = Double.valueOf(scanner.nextLine().split(" ")[0]);
        System.out.println("\n ENTER THE THE RATE OF INTEREST PER BUSINESS DECISION");
        final double interestRate = Double.valueOf(scanner.nextLine().split(" ")[0]);
        company.setFund(company.getFund() + newFund);
        company.setInterestRate(interestRate);
        company.setReputationFactor(company.getReputationFactor() * newFund * REPUTATION_FACTOR_ON_LOAN);
        profitMakingCompanyRepository.save(company);
    }

    @Transactional
    private void hireEngineer(ProfitMakingCompany profitMakingCompany) {
        System.out.println("HOW MANY ENGINEER YOU WOULD LIKE TO HIRE, ENTER THE NUMBER");
        final int input = Integer.valueOf(scanner.nextLine().split(" ")[0]);
        profitMakingCompany.setEngineers(profitMakingCompany.getEngineers() + input);
        profitMakingCompany.setRevenue(profitMakingCompany.getRevenue() - input * ENGINEER_COST);
        profitMakingCompany.setReputationFactor(profitMakingCompany.getReputationFactor() * input * REPUTATION_FACTOR_ON_HIRE);
        profitMakingCompanyRepository.save(profitMakingCompany);
    }

    @Transactional
    private void layOffEngineer(ProfitMakingCompany profitMakingCompany){
        System.out.println("HOW MANY ENGINEER YOU WOULD LIKE TO LAYOFF, ENTER THE NUMBER");
        final int input = Integer.valueOf(scanner.nextLine().split(" ")[0]);
        profitMakingCompany.setEngineers(profitMakingCompany.getEngineers() - input);
        profitMakingCompany.setRevenue(profitMakingCompany.getRevenue() + input * ENGINEER_COST);
        profitMakingCompany.setReputationFactor(profitMakingCompany.getReputationFactor() * input * REPUTATION_FACTOR_ON_LAYOFF);
        profitMakingCompanyRepository.save(profitMakingCompany);
    }

    @Transactional
    private void hireExecutive(ProfitMakingCompany profitMakingCompany) {
        System.out.println("HOW MANY EXECUTIVES YOU WOULD LIKE TO HIRE, ENTER THE NUMBER");
        final int input = Integer.valueOf(scanner.nextLine().split(" ")[0]);
        profitMakingCompany.setExecutives(profitMakingCompany.getExecutives() + input);
        profitMakingCompany.setRevenue(profitMakingCompany.getRevenue() - input * EXECUTIVE_COST);
        profitMakingCompany.setReputationFactor(profitMakingCompany.getReputationFactor() * input * REPUTATION_FACTOR_ON_HIRE);
        profitMakingCompanyRepository.save(profitMakingCompany);
    }

    @Transactional
    private void layOffExecutive(ProfitMakingCompany profitMakingCompany) {
        System.out.println("HOW MANY EXECUTIVE YOU WOULD LIKE TO LAYOFF, ENTER THE NUMBER");
        final int input = Integer.valueOf(scanner.nextLine().split(" ")[0]);
        profitMakingCompany.setExecutives(profitMakingCompany.getExecutives() - input);
        profitMakingCompany.setRevenue(profitMakingCompany.getRevenue() + input * EXECUTIVE_COST);
        profitMakingCompany.setReputationFactor(profitMakingCompany.getReputationFactor() * input * REPUTATION_FACTOR_ON_LAYOFF);
        profitMakingCompanyRepository.save(profitMakingCompany);
    }

    private void addProduct(ProfitMakingCompany profitMakingCompany) {
        System.out.println(" HOW MANY ENGINEER REQUIRE FOR THE PRODUCT DEVELOPMENT, ENTER THE NUMBER ");
        final int engineersRequirement = Integer.valueOf(this.scanner.nextLine().split(" ")[0]);
        if (engineersRequirement > profitMakingCompany.getEngineers())
            throw new InsufficientResource(" PLEASE ADD ENGINEERS. COMPANY DON'T HAVE SUFFICIENT ENGINEERS ");
        System.out.println(" HOW MANY EXECUTIVE REQUIRE FOR THE PRODUCT MARKETING, ENTER THE NUMBER ");
        final int executivesRequirement = Integer.valueOf(this.scanner.nextLine().split(" ")[0]);
        if (executivesRequirement > profitMakingCompany.getExecutives())
            throw new InsufficientResource(" PLEASE ADD EXECUTIVES. COMPANY DON'T HAVE SUFFICIENT EXECUTIVES ");
        System.out.println(" ENTER THE PRODUCT NAME ");
        final String productName = this.scanner.nextLine().split(" ")[0];

        // Demonstrate Decorator Design pattern. Product is being decorated
        AbstractDeliverable product = new BasicProduct(profitMakingCompany.getCompanyName(), productName);
        double productCost = (engineersRequirement * ENGINEER_COST + executivesRequirement * executivesRequirement) * PRODUCT_COST_PERCENTAGE_PER_RESOURCE/100;
        System.out.println("\n ADDING BASIC PRODUCT, WOULD YOU LIKE TO ADD FEATURES TO YOUR PRODUCT " +
                "\n PRESS Y TO MAKE YOUR PRODUCT SCALABLE, IT WILL COST 10 PERCENT MORE " +
                "\n PRESS N TO STICK WITH BASIC PRODUCT ");
        if ("Y".equalsIgnoreCase(this.scanner.nextLine().split(" ")[0]))
            product = new ScalableProduct(product);
        System.out.println("\n PRESS Y TO MAKE YOUR PRODUCT DURABLE, IT WILL COST 10 PERCENT MORE " +
                "\n PRESS N TO STICK WITH EXISTING PRODUCT ");
        if ("Y".equalsIgnoreCase(this.scanner.nextLine().split(" ")[0]))
            product = new DurableProduct(product);
        System.out.println("\n PRESS Y TO MAKE YOUR PRODUCT EXTENSIBLE, IT WILL COST 10 PERCENT MORE " +
                "\n PRESS N TO STICK WITH EXISTING PRODUCT ");
        if ("Y".equalsIgnoreCase(this.scanner.nextLine().split(" ")[0]))
            product = new ExtensibleProduct(product);

        // Adaptor used to demonstrate Adaptor design pattern
        adaptor.createProduct(product);
        productCost *= adaptor.getCostFactor();
        if (profitMakingCompany.getFund() + profitMakingCompany.getRevenue() < productCost)
            throw new InSufficientFund(" YOU DON'T HAVE SUFFICIENT FUND. YOU MAY TAKE FINANCE " +
                    "\n\t YOU NEED CAPITAL OF " + productCost);
        profitMakingCompany.setFund(profitMakingCompany.getFund() - productCost);
        if (profitMakingCompany.getFund() < 0) {
            profitMakingCompany.setRevenue(profitMakingCompany.getRevenue() + profitMakingCompany.getFund());
            profitMakingCompany.setFund(0);
        }
        product.setProductCost(productCost);
        profitMakingCompany.addDeliverable(product);
        adjustParamsWithAddedProduct(profitMakingCompany, product);
        System.out.println("PRODUCT ADDED");
    }

    @Transactional
    private void adjustParamsWithAddedProduct(ProfitMakingCompany company, AbstractDeliverable product){
        company.setRevenue(company.getRevenue() + product.getProductCost() * REVENUE_FACTOR_ON_PRODUCT_ADDITION);
        company.setReputationFactor(company.getReputationFactor() * REPUTATION_FACTOR_ON_PRODUCT_ADDITION);
        profitMakingCompanyRepository.save(company);
    }

    public void setMarketSituationService(MarketSituationService marketSituationService) {
        this.marketSituationService = marketSituationService;
    }
}
