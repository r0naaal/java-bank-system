import java.time.LocalDateTime;

public class Transaction {
    private String type; // "Deposit" "Withdraw" or "transfer"
    private double amount; 
    private LocalDateTime dateTime; 

    public Transaction(String type, double amount, LocalDateTime dateTime) {
        this.type = type;
        this.amount = amount;
        this.dateTime = dateTime;
    }

    // Getters
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public LocalDateTime getDateTime() { return dateTime; }

    // handle json
    public void setDateTime(LocalDateTime dateTime){ this.dateTime = dateTime; }
}