import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Account {
    
    // user logging variables
    private String pin; // store securely
    private String userName; // has to be unique for each passengers
    private LocalDate dateOfBirth; //  yyyy-mm-dd
    
    private double balance;
    private String accountNumber;
    private String routingNumber;

    private List<Transaction> transactions;
    
    public Account(String pin, String userName, LocalDate dateOfBirth, double balance, String accountNumber, String routingNumber){
        this.pin = pin;
        this.userName = userName;
        this.dateOfBirth = dateOfBirth;

        this.balance = balance;
        this.accountNumber = accountNumber;
        this.routingNumber = routingNumber;
        this.transactions = new ArrayList<>();
    }

    public String toString(){
        return "Account{" +
            "userName='" + userName + '\'' +
            ", balance=" + balance +
            ", accountNumber='" + accountNumber + '\'' +
            ", routingNumber='" + routingNumber + '\'' +
            '}' + "\n";
    }
    
    // getter methods
    public String getUserName(){ return userName; }
    public String getPin() { return pin; }
    public double getBalance() { return balance; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getAccountNumber() { return accountNumber; }
    public String getRoutingNumber() { return routingNumber; }
    public List<Transaction> getTransactions() { return transactions; }
    
    // setter methods
    public void setPin(String pin) { this.pin = pin; }
    public void setBalance(double balance) { this.balance = balance; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public void setRoutingNumber(String routingNumber) { this.routingNumber = routingNumber; }
    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }

    public void addTransaction(Transaction transaction){
        transactions.add(transaction);
    }

    public void deposit(double amount){
        if (amount > 0) {
            balance += amount;
        } else {
            System.out.println("Deposit amount must be positive.");
        }
    }

    public void withdraw(double amount){
        if (amount <= balance) {
            balance -= amount;
        } else {
            System.out.println("Insufficient Funds.");
        }
    }
}