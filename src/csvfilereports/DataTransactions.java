//Each Data transaction Object that contains date, type, shares, price, fund, investor, and salesrep 
package csvfilereports;

/**
 *
 * @author krati
 */
public class DataTransactions {
    private String date;
    private String type; 
    private String shares;
    private String price;
    private String fund;
    private String investor;
    private String salesRep;
    
    public DataTransactions(String date, String type, String shares, String price, String fund, String investor, String salesRep){
        this.date = date;
        this.type = type;
        this.shares = shares;
        this.price = price;
        this.fund = fund;
        this.investor = investor;
        this.salesRep = salesRep;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShares() {
        return shares;
    }

    public void setShares(String shares) {
        this.shares = shares;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFund() {
        return fund;
    }

    public void setFund(String fund) {
        this.fund = fund;
    }

    public String getInvestor() {
        return investor;
    }

    public void setInvestor(String investor) {
        this.investor = investor;
    }

    public String getSalesRep() {
        return salesRep;
    }

    public void setSalesRep(String salesRep) {
        this.salesRep = salesRep;
    }
}
