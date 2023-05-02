package report;

public class card {
    
    private String card_no;
    private String issue_date;
    private String expire_date;
    private String card_status;
    private String cust_id;

    public card(String card_no, String issue_date, String expire_date, String card_status, String cust_id) {
        this.card_no = card_no;
        this.issue_date = issue_date;
        this.expire_date = expire_date;
        this.card_status = card_status;
        this.cust_id = cust_id;
    }

   
    
    
    
    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public String getIssue_date() {
        return issue_date;
    }

    public void setIssue_date(String issue_date) {
        this.issue_date = issue_date;
    }

    public String getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(String expire_date) {
        this.expire_date = expire_date;
    }

    public String getCard_status() {
        return card_status;
    }

    public void setCard_status(String card_status) {
        this.card_status = card_status;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }
    
    
    
}
