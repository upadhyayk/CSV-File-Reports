//Consume data in a CSV file, and generate reports
package csvfilereports;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author krati
 */

public class CSVFileReports {
    
    static int rows = 13;
    static int columns = 7;
    public static String[][] array = new String [rows][columns];
    static int sharesIndex = 0;
    static int investorIndex = 0;
    static int fundTypeIndex = 0;
    static int buyOrSellTypeIndex = 0;
    static int sharesPriceIndex = 0;
    static int salesRepIndex = 0;
    static int dateIndex = 0;
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        CSVFileReports report = new CSVFileReports();
        report.storeCsvIntoArray();
        report.findIndexes();
        report.salesSummary();
        report.assetsUnderManagementSummary();
        report.breakReport();
        report.investorProfit();
    }
    
    
    //read from CSV File and then store in 2d Array using bufferedreader and reading line by line
    private void storeCsvIntoArray() throws FileNotFoundException, IOException{
        //Use a buffered reader to read each line in from CSV file. Split line into 3 substrings if the file contains quotation marks
        //get rid of commas in quotation marks substring and combine all 3 strings ago (before quotation, quotation, and after quotation)
        //Split line using comma as delimiter and add into 2d array
        String line = "";
        int j = 0;
        int i = 0;
        BufferedReader bufferedReader = new BufferedReader(new FileReader("data.csv"));
        while(j < rows){
            if((line = bufferedReader.readLine()) != null){
                String[] values;
                if(line.contains("\"")){
                    int beginQuote = line.indexOf("\"");
                    int endQuote = line.lastIndexOf("\"");
                    String subStringBeforeQuote = line.substring(0, beginQuote);
                    String subStringQuote = line.substring(beginQuote, endQuote);
                    String subStringAfterQuote = line.substring(endQuote+1);
         
                    String newSubStringQuote = subStringQuote.replace(",", " ");
                    String fullLine = subStringBeforeQuote + newSubStringQuote + subStringAfterQuote;
                   // System.out.println("fullLine is: " + fullLine);
                    values = fullLine.split(",");
                }else{
                    values = line.split(",");
                }
                for( i = 0; i < columns; i++){
                    array[j][i] = values[i];
                }
                j++;
            } 
        }
    }
    
    //For each Sales Rep, generate Year to Date, Month to Date, Quarter to
    //Date, and Inception to Date summary of cash amounts sold across all
    //funds.
    private void salesSummary(){
        Map<String, Double> yearToDate = new HashMap<>();
        Map<String, Double> monthToDate = new HashMap<>();
        Map<String, Double> quarterToDate = new HashMap<>();
        Map<String, Double> inceptionToDate = new HashMap<>();
        
        
        
    }
    
    //For each Sales Rep, generate a summary of the net amount held by
    //investors across all funds.
    private void assetsUnderManagementSummary(){
        
        Map<String, Double> salesRepAllStockFunds = new HashMap<>();
        Map<String, Double> salesRepAllBondFunds = new HashMap<>();
        Map<String, Double> salesRepAllStockShares = new HashMap<>();
        Map<String, Double> salesRepAllBondShares = new HashMap<>(); 
        
       for(int i = 1; i < rows; i++){
           if(array[i][fundTypeIndex].equalsIgnoreCase("stock fund")){
               if(salesRepAllStockFunds.containsKey(array[i][salesRepIndex]) && salesRepAllStockShares.containsKey(array[i][salesRepIndex])){
                   if(array[i][buyOrSellTypeIndex].equalsIgnoreCase("buy")){
                       double sharesValueFromMap = salesRepAllStockFunds.get(array[i][salesRepIndex]);
                       String[] sharesNumberFromCsv = array[i][sharesPriceIndex].split("\\$");
                       Double sharesValueToSubtract = Double.parseDouble(sharesNumberFromCsv[1].trim()) * Double.parseDouble(array[i][sharesIndex].trim());
                       salesRepAllStockFunds.put(array[i][salesRepIndex], sharesValueFromMap - sharesValueToSubtract);
                                            
                       double stockValueFromMap = salesRepAllStockShares.get(array[i][salesRepIndex]);
                       double stockFromCsv = Double.parseDouble(array[i][sharesIndex].trim());
                       salesRepAllStockShares.put(array[i][salesRepIndex], stockValueFromMap + stockFromCsv);
                   }else{ //"sell"
                       double sharesValueFromMap = salesRepAllStockFunds.get(array[i][salesRepIndex]);
                       String[] sharesNumberFromCsv = array[i][sharesPriceIndex].split("\\$");
                       Double sharesValueToAdd = Double.parseDouble(sharesNumberFromCsv[1].trim()) * Double.parseDouble(array[i][sharesIndex].trim());
                       salesRepAllStockFunds.put(array[i][salesRepIndex], sharesValueFromMap + sharesValueToAdd);

                       double stockValueFromMap = salesRepAllStockShares.get(array[i][salesRepIndex]);
                       double stockFromCsv = Double.parseDouble(array[i][sharesIndex].trim());
                       salesRepAllStockShares.put(array[i][salesRepIndex], stockValueFromMap - stockFromCsv);
                   }
               }else{ //doesn't contain key in stock fun
                   if(array[i][buyOrSellTypeIndex].equalsIgnoreCase("buy")){
                       String[] sharesNumberFromCsv = array[i][sharesPriceIndex].split("\\$");
                       Double priceOfSharesToSubtract = Double.parseDouble(sharesNumberFromCsv[1].trim()) * Double.parseDouble(array[i][sharesIndex].trim());
                       salesRepAllStockFunds.put(array[i][salesRepIndex], 0 - priceOfSharesToSubtract);
                                              
                       double stockFromCsv = Double.parseDouble(array[i][sharesIndex].trim());
                       salesRepAllStockShares.put(array[i][salesRepIndex], 0 + stockFromCsv);
                       
                   }else{ //"sell"
                       String[] sharesNumberFromCsv = array[i][sharesPriceIndex].split("\\$");
                       Double priceOfSharesToAdd = Double.parseDouble(sharesNumberFromCsv[1].trim()) * Double.parseDouble(array[i][sharesIndex].trim());
                       salesRepAllStockFunds.put(array[i][salesRepIndex], 0 + priceOfSharesToAdd);
                                              
                       double stockFromCsv = Double.parseDouble(array[i][sharesIndex].trim());
                       salesRepAllStockShares.put(array[i][salesRepIndex], 0 - stockFromCsv); 
                   }
               }
           }else{ //bond fund
               if(salesRepAllBondFunds.containsKey(array[i][salesRepIndex]) && salesRepAllBondShares.containsKey(array[i][salesRepIndex])){
                   if(array[i][buyOrSellTypeIndex].equalsIgnoreCase("buy")){
                        double sharesValueFromMap = salesRepAllBondFunds.get(array[i][salesRepIndex]);
                        String[] sharesNumberFromCsv = array[i][sharesPriceIndex].split("\\$");
                        Double sharesValueToSubtract = Double.parseDouble(sharesNumberFromCsv[1].trim()) * Double.parseDouble(array[i][sharesIndex].trim());
                        salesRepAllBondFunds.put(array[i][salesRepIndex], sharesValueFromMap - sharesValueToSubtract);
                                                
                        double stockValueFromMap = salesRepAllBondShares.get(array[i][salesRepIndex]);
                        double stockFromCsv = Double.parseDouble(array[i][sharesIndex].trim());
                        salesRepAllBondShares.put(array[i][salesRepIndex], stockValueFromMap + stockFromCsv);
                   }else{ //"sell"
                       double sharesValueFromMap = salesRepAllBondFunds.get(array[i][salesRepIndex]);
                       String[] sharesNumberFromCsv = array[i][sharesPriceIndex].split("\\$");
                       Double sharesValueToAdd = Double.parseDouble(sharesNumberFromCsv[1].trim()) * Double.parseDouble(array[i][sharesIndex].trim());
                       salesRepAllBondFunds.put(array[i][salesRepIndex], sharesValueFromMap + sharesValueToAdd);
                                         
                       double stockValueFromMap = salesRepAllBondShares.get(array[i][salesRepIndex]);
                       double stockFromCsv = Double.parseDouble(array[i][sharesIndex].trim());
                       salesRepAllBondShares.put(array[i][salesRepIndex], stockValueFromMap - stockFromCsv);
                   }
               }else{ //doesn't contain key in bond fund
                    if(array[i][buyOrSellTypeIndex].equalsIgnoreCase("buy")){
                       String[] sharesNumberFromCsv = array[i][sharesPriceIndex].split("\\$");
                       Double priceOfSharesToSubtract = Double.parseDouble(sharesNumberFromCsv[1].trim()) * Double.parseDouble(array[i][sharesIndex].trim());
                       salesRepAllBondFunds.put(array[i][salesRepIndex], 0 - priceOfSharesToSubtract);
                                              
                       double stockFromCsv = Double.parseDouble(array[i][sharesIndex].trim());
                       salesRepAllBondShares.put(array[i][salesRepIndex], 0 + stockFromCsv);
                   }else{ //"sell"
                       String[] sharesNumberFromCsv = array[i][sharesPriceIndex].split("\\$");
                       Double priceOfSharesToAdd = Double.parseDouble(sharesNumberFromCsv[1].trim()) * Double.parseDouble(array[i][sharesIndex].trim());
                       salesRepAllBondFunds.put(array[i][salesRepIndex], 0 + priceOfSharesToAdd);
                       
                       double stockFromCsv = Double.parseDouble(array[i][sharesIndex].trim());
                       salesRepAllBondShares.put(array[i][salesRepIndex], 0 - stockFromCsv); 
                   }
               }
           }
       }
       
       System.out.println("ASSETS UNDER MANAGEMENT SUMMARY: ");
     
       //iterate through all 4 hashmaps to print to output
        Iterator stockFundsIterator = salesRepAllStockFunds.keySet().iterator();
        System.out.println("    Stock Fund");
        while(stockFundsIterator.hasNext()){
            String key = stockFundsIterator.next().toString();
            Double value = salesRepAllStockFunds.get(key);
            System.out.print("          Sales Representative: " + key);
            System.out.print("                   Stock Fund Profit/Loss: " + value);
            System.out.println();
           
            //System.out.println("The investor " + key + " has a net profit of " + value + " in a stock fund.");
        }
        
        Iterator bondFundsIterator = salesRepAllBondFunds.keySet().iterator();
        System.out.println("    Bond Fund");

        while(bondFundsIterator.hasNext()){
            String key = bondFundsIterator.next().toString();
            Double value = salesRepAllBondFunds.get(key);
            System.out.print("          Sales Representative: " + key);
            System.out.print("                  Bond Fund Profit/Loss: " + value);
            System.out.println();
           // System.out.println("The investor " + key + " has a net profit of " + value + " in a bond fund");
        }
        
        Iterator stockSharesIterator = salesRepAllStockShares.keySet().iterator();
        System.out.println("    Stock Shares");

        while(stockSharesIterator.hasNext()){
            String key = stockSharesIterator.next().toString();
            Double value = salesRepAllStockShares.get(key);
            System.out.print("          Sales Representative: " + key);
            System.out.print("                  Total Stock Shares: " + value);
            System.out.println();
            //System.out.println("The investor " + key + " has a net profit of " + value + " in a stock fund.");
        }
        
        Iterator bondSharesIterator = salesRepAllBondShares.keySet().iterator();
        System.out.println("    Bond Shares");

        while(bondSharesIterator.hasNext()){
            String key = bondSharesIterator.next().toString();
            Double value = salesRepAllBondShares.get(key);
            System.out.print("           Sales Representative: " + key);
            System.out.print("                   Total Bond Shares: " + value);
            System.out.println();
           // System.out.println("The investor " + key + " has a net profit of " + value + " in a bond fund");
        }
       
    }
    
    //Assuming the information in the data provided is complete and accurate,
    //generate a report that shows any errors (negative cash balances,
    //negative share balance) by investor.
    private void breakReport(){
        //Use hashmaps from assets under management report to show net negative info         
        Map<String, Double> stockFund = new HashMap<>();
        Map<String, Double> bondFund = new HashMap<>();
        Map<String, Double> stockShares = new HashMap<>();
        Map<String, Double> bondShares = new HashMap<>();
        
         for(int i = 1; i < rows; i++){
            if(array[i][fundTypeIndex].equalsIgnoreCase("STOCK FUND")){
                if(stockFund.containsKey(array[i][investorIndex])){
                    if(array[i][buyOrSellTypeIndex].equalsIgnoreCase("buy")){
                        double value = stockFund.get(array[i][investorIndex]);
                        String[] number = array[i][sharesPriceIndex].split("\\$");
                        Double sharesValueToSubtract = Double.parseDouble(number[1].trim()) * Double.parseDouble(array[i][sharesIndex].trim());
                        stockFund.put(array[i][investorIndex], (value - sharesValueToSubtract));
                        
                        double sharesFromMap = stockShares.get(array[i][investorIndex]);
                        double sharesFromCsv = Double.parseDouble(array[i][sharesIndex].trim());
                        stockShares.put(array[i][investorIndex], sharesFromMap + sharesFromCsv);
                    }else{
                        double value = stockFund.get(array[i][investorIndex]);
                        String[] number = array[i][sharesPriceIndex].split("\\$");
                        Double sharesValueToAdd = Double.parseDouble(number[1].trim()) * Double.parseDouble(array[i][sharesIndex].trim());
                        stockFund.put(array[i][investorIndex], (value + sharesValueToAdd));
                        
                        double sharesFromMap = stockShares.get(array[i][investorIndex]);
                        double sharesFromCsv = Double.parseDouble(array[i][sharesIndex].trim());
                        stockShares.put(array[i][investorIndex], sharesFromMap - sharesFromCsv);
                    }
                }else{
                    String[] number = array[i][sharesPriceIndex].split("\\$");
                    Double numberToAdd = 0.0;
                    if(array[i][buyOrSellTypeIndex].equalsIgnoreCase("buy")){
                        numberToAdd = Double.parseDouble(number[1].trim()) * Double.parseDouble(array[i][sharesIndex].trim());
                        stockFund.put(array[i][investorIndex], (0-numberToAdd));
                        
                        double sharesFromCsv = Double.parseDouble(array[i][sharesIndex].trim());
                        stockShares.put(array[i][investorIndex], 0 + sharesFromCsv);
                    }else{
                        numberToAdd = Double.parseDouble(number[1].trim()) * Double.parseDouble(array[i][sharesIndex].trim());
                        stockFund.put(array[i][investorIndex], numberToAdd);
                        
                        double sharesFromCsv = Double.parseDouble(array[i][sharesIndex].trim());
                        stockShares.put(array[i][investorIndex], 0 - sharesFromCsv);

                    }
                }
            }else if(array[i][fundTypeIndex].equalsIgnoreCase("bond fund")){
                if(bondFund.containsKey(array[i][investorIndex])){
                    if(array[i][buyOrSellTypeIndex].equalsIgnoreCase("buy")){
                        double value = bondFund.get(array[i][investorIndex]);
                        String[] number = array[i][sharesPriceIndex].split("\\$");
                        Double sharesValueToSubtract = Double.parseDouble(number[1].trim()) * Double.parseDouble(array[i][sharesIndex].trim());
                        bondFund.put(array[i][investorIndex], (value - sharesValueToSubtract));
                        
                        double sharesFromMap = bondShares.get(array[i][investorIndex]);
                        double sharesFromCsv = Double.parseDouble(array[i][sharesIndex].trim());
                        bondShares.put(array[i][investorIndex], sharesFromMap + sharesFromCsv);
                    }else{
                        double value = bondFund.get(array[i][investorIndex]);
                        String[] number = array[i][sharesPriceIndex].split("\\$");
                        Double sharesValueToAdd = Double.parseDouble(number[1].trim()) * Double.parseDouble(array[i][sharesIndex].trim());
                        bondFund.put(array[i][investorIndex], (value + sharesValueToAdd));
                        
                        double sharesFromMap = bondShares.get(array[i][investorIndex]);
                        double sharesFromCsv = Double.parseDouble(array[i][sharesIndex].trim());
                        bondShares.put(array[i][investorIndex], sharesFromMap - sharesFromCsv);
                    }
                }else{
                    String[] number = array[i][sharesPriceIndex].split("\\$");
                    Double numberToAdd = 0.0;
                    if(array[i][buyOrSellTypeIndex].equalsIgnoreCase("buy")){
                        numberToAdd = Double.parseDouble(number[1].trim()) * Double.parseDouble(array[i][sharesIndex].trim());
                        bondFund.put(array[i][investorIndex], (0-numberToAdd));
                        
                        double sharesFromCsv = Double.parseDouble(array[i][sharesIndex].trim());
                        bondShares.put(array[i][investorIndex], 0 + sharesFromCsv);
                    }else{
                        numberToAdd = Double.parseDouble(number[1].trim()) * Double.parseDouble(array[i][sharesIndex].trim());
                        bondFund.put(array[i][investorIndex], numberToAdd);
                        
                        double sharesFromCsv = Double.parseDouble(array[i][sharesIndex].trim());
                        bondShares.put(array[i][investorIndex], 0 - sharesFromCsv);

                    }
                }
            }else{
                System.out.println("Some kind of error occured or looking at wrong place");
            }
        }
         
        System.out.println("BREAK REPORT: ");

       //iterate through all 4 hashmaps to print to output
        Iterator stockFundsIterator = stockFund.keySet().iterator();
        System.out.println("    Stock Fund");
        while(stockFundsIterator.hasNext()){
            String key = stockFundsIterator.next().toString();
            Double value = stockFund.get(key);
            if(value < 0){
                System.out.print("          Investor: " + key);
                System.out.print("                   Stock Fund Profit/Loss: " + value);
                System.out.println();
            }
        }
        
        Iterator bondFundsIterator = bondFund.keySet().iterator();
        System.out.println("    Bond Fund");

        while(bondFundsIterator.hasNext()){
            String key = bondFundsIterator.next().toString();
            Double value = bondFund.get(key);
            if(value < 0){
                System.out.print("          Investor: " + key);
                System.out.print("                  Bond Fund Profit/Loss: " + value);
                System.out.println();                
            }
        }
        
        Iterator stockSharesIterator = stockShares.keySet().iterator();
        System.out.println("    Stock Shares");

        while(stockSharesIterator.hasNext()){
            String key = stockSharesIterator.next().toString();
            Double value = stockShares.get(key);
            if(value < 0){
                System.out.print("          Investor: " + key);
                System.out.print("                  Total Stock Shares: " + value);
                System.out.println();  
            }
        }
        
        Iterator bondSharesIterator = bondShares.keySet().iterator();
        System.out.println("    Bond Shares");

        while(bondSharesIterator.hasNext()){
            String key = bondSharesIterator.next().toString();
            Double value = bondShares.get(key);
            if(value < 0){
                System.out.print("           Sales Representative: " + key);
                System.out.print("                   Total Bond Shares: " + value);
                System.out.println(); 
            }
        }
    }
    
    //For each Investor and Fund, return net profit or loss on investment.
    private void investorProfit(){
        //make 2 maps, one for stock fund and one for bond fund 
        //parse through 2d array and add investor to map key. then parse and find the type (buy/sell)
        //if buy then subtract from value and if sell then add to value
        //after gone through all, print all the values out properly
        
        Map<String, Double> stockFund = new HashMap();
        Map<String, Double> bondFund = new HashMap();

        for(int i = 1; i < rows; i++){
            if(array[i][fundTypeIndex].equalsIgnoreCase("STOCK FUND")){
                if(stockFund.containsKey(array[i][investorIndex])){
                    if(array[i][buyOrSellTypeIndex].equalsIgnoreCase("buy")){
                        double value = stockFund.get(array[i][investorIndex]);
                        String[] number = array[i][sharesPriceIndex].split("\\$");
                        Double sharesValueToSubtract = Double.parseDouble(number[1].trim()) * Double.parseDouble(array[i][sharesIndex].trim());
                        stockFund.put(array[i][investorIndex], (value - sharesValueToSubtract));
                    }else{
                        double value = stockFund.get(array[i][investorIndex]);
                        String[] number = array[i][sharesPriceIndex].split("\\$");
                        Double sharesValueToAdd = Double.parseDouble(number[1].trim()) * Double.parseDouble(array[i][sharesIndex].trim());
                        stockFund.put(array[i][investorIndex], (value + sharesValueToAdd));
                    }
                }else{
                    String[] number = array[i][sharesPriceIndex].split("\\$");
                    Double numberToAdd = 0.0;
                    if(array[i][buyOrSellTypeIndex].equalsIgnoreCase("buy")){
                        numberToAdd = Double.parseDouble(number[1].trim()) * Double.parseDouble(array[i][sharesIndex].trim());
                        stockFund.put(array[i][investorIndex], (0-numberToAdd));
                    }else{
                        numberToAdd = Double.parseDouble(number[1].trim()) * Double.parseDouble(array[i][sharesIndex].trim());
                        stockFund.put(array[i][investorIndex], numberToAdd);

                    }
                }
            }else if(array[i][fundTypeIndex].equalsIgnoreCase("bond fund")){
                if(bondFund.containsKey(array[i][investorIndex])){
                    if(array[i][buyOrSellTypeIndex].equalsIgnoreCase("buy")){
                        double value = bondFund.get(array[i][investorIndex]);
                        String[] number = array[i][sharesPriceIndex].split("\\$");
                        Double sharesValueToSubtract = Double.parseDouble(number[1].trim()) * Double.parseDouble(array[i][sharesIndex].trim());

                        bondFund.put(array[i][investorIndex], (value - sharesValueToSubtract));
                    }else{
                        double value = bondFund.get(array[i][investorIndex]);
                        String[] number = array[i][sharesPriceIndex].split("\\$");
                        Double sharesValueToAdd = Double.parseDouble(number[1].trim()) * Double.parseDouble(array[i][sharesIndex].trim());

                        bondFund.put(array[i][investorIndex], (value + sharesValueToAdd));
                    }
                }else{
                    String[] number = array[i][sharesPriceIndex].split("\\$");
                    Double numberToAdd = 0.0;
                    if(array[i][buyOrSellTypeIndex].equalsIgnoreCase("buy")){
                        numberToAdd = Double.parseDouble(number[1].trim()) * Double.parseDouble(array[i][sharesIndex].trim());
                        bondFund.put(array[i][investorIndex], (0-numberToAdd));
                    }else{
                        numberToAdd = Double.parseDouble(number[1].trim()) * Double.parseDouble(array[i][sharesIndex].trim());
                        bondFund.put(array[i][investorIndex], numberToAdd);

                    }
                }
            }else{
                System.out.println("Some kind of error occured or looking at wrong place");
            }
        }
        
        System.out.println("INVESTOR PROFIT: " );
        //iterate through both hashmaps to print to output
        Iterator stockIterator = stockFund.keySet().iterator();
        while(stockIterator.hasNext()){
            String key = stockIterator.next().toString();
            Double value = stockFund.get(key);
            System.out.print("    Investor: " + key);
            System.out.print("        Stock Fund Profit/Loss: " + value);
            System.out.println();
            //System.out.println("The investor " + key + " has a net profit of " + value + " in a stock fund.");
        }
        
        Iterator bondIterator = bondFund.keySet().iterator();
        while(bondIterator.hasNext()){
            String key = bondIterator.next().toString();
            Double value = bondFund.get(key);
            System.out.print("    Investor: " + key);
            System.out.print("        Bond Fund Profit/Loss: " + value);
            System.out.println();
           // System.out.println("The investor " + key + " has a net profit of " + value + " in a bond fund");
        }
        
    }

    private void findIndexes(){
        for(int i = 0; i < columns; i++){
            if(array[0][i].equalsIgnoreCase("TXN_TYPE")){
                buyOrSellTypeIndex = i;
            }else if(array[0][i].equalsIgnoreCase("TXN_PRICE")){
                sharesPriceIndex = i;
            }else if(array[0][i].equalsIgnoreCase("FUND")){
                fundTypeIndex = i;
            }else if(array[0][i].equalsIgnoreCase("investor")){
                investorIndex = i;
            }else if(array[0][i].equalsIgnoreCase("txn_shares")){
                sharesIndex = i;
            }else if(array[0][i].equalsIgnoreCase("sales_rep")){
                salesRepIndex = i;
            }else if(array[0][i].equalsIgnoreCase("txn_date")){
                dateIndex = i;
            }
        }
    }
}




/*    //read from CSV File and then store in 2d Array using bufferedreader and reading line by line
    private static void storeCsvIntoArray() throws FileNotFoundException, IOException{
        String line = "";
        int j = 0;
        BufferedReader bufferedReader = new BufferedReader(new FileReader("data.csv"));
        while(j < rows){
            if((line = bufferedReader.readLine()) != null){
                String[] values = line.split(",");
                for(int i = 0; i < columns; i++){
                    array[j][i] = values[i];
                }
                j++;
            } 
        }
        
        //make sure the 2d array is filled correctly
        for(int i = 0; i < rows; i++){
            for(j = 0; j <columns; j++){
                System.out.print(array[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("value at row 6 column 5 is : " + array[6][5]);
    }
*/




/*
 //make a list of string array. The list can be added to for each investor 
        Map<String, List<String[]>> netAmountSummary = new HashMap();
        List<String[]> listOfInvestorInfo = new ArrayList<String[]>();
        for(int i = 0; i < rows; i++){
            //if map contains sales rep then add new investor or change info as needed 
            //else create new string[] containing investor info and add to map 
            if(netAmountSummary.containsKey(array[i][salesRepIndex])){
                
            }else{
                String[] stringArray= new String[3];
                stringArray[0] = array[i][investorIndex];
                String[] number = array[i][sharesPriceIndex].split("\\$");
                //add/subtract shares and proft based on whether buying/selling
                if(array[i][buyOrSellTypeIndex].equalsIgnoreCase("buy")){
                    Double valueToAdd = 0-Double.parseDouble(number[1].trim());
                    stringArray[1] = "$" + Double.toString(valueToAdd);
                    stringArray[2] = array[i][sharesIndex];
                }else{
                    Double valueToAdd = 0+Double.parseDouble(number[1].trim());
                    stringArray[1] = "$" + Double.toString(valueToAdd);
                    Double shareToAdd = 0 - Double.parseDouble(array[i][sharesIndex]);
                    stringArray[2] = Double.toString(shareToAdd);
                }
                
                listOfInvestorInfo.add(stringArray);
                netAmountSummary.put(array[i][salesRepIndex], listOfInvestorInfo);
            }
        }
        
        
*/