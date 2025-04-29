import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class UserManager {
    private List<Account> accounts; // List of accounts
    private final String fileName = "users.json"; // JSON file for storing user data

    // Banking System Constructor
    public UserManager() {
        accounts = new ArrayList<>(); // initialize list
        loadAccounts(); // load existing accounts from JSON file
    }

    public void updateBalance(Account account) { // find the account in the list and update its balance
        for (Account acc : accounts) {
            if (acc.getAccountNumber().equals(account.getAccountNumber())) {
                acc.setBalance(account.getBalance()); // update balance
                saveAccounts(); // update database
                // System.out.println("Updated balance for account: " + acc.getAccountNumber());
                break;
            }
        }
    }

    public void updatePin(Account account, String newPin) {
        account.setPin(newPin); // update pin
        saveAccounts(); // save changes to JSON
        System.out.println("Updated PIN for account: " + account.getAccountNumber());
    }

    public void addAccount(Account account) {
        if (isUserNameUnique(account.getUserName()) && isAccountNumberUnique(account.getAccountNumber()) == null) {
            accounts.add(account);
            saveAccounts();
            System.out.println("Account added: " + account.getUserName());
        } else {
            System.out.println("Account with this username or account number already exists.");
        }
    }

    public boolean isUserNameUnique(String userName) {
        for (Account account : accounts) {
            if (account.getUserName().equals(userName)) {
                System.out.println("Username is taken: " + userName);
                return false; // username is taken
            }
        }
        return true; // username is unique
    }

    public Account isAccountNumberUnique(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                System.out.println("Account number already exists: " + accountNumber);
                return account; // already exists
            }
        }
        return null; // account number is unique
    }

    public Account login(String userName, String pin) {
        for (Account account : accounts) {
            if (account.getUserName().equals(userName) && account.getPin().equals(pin)) {
                // System.out.println("Login successful for user: " + userName);
                return account; // return account if successful login
            }
        }
        System.out.println("Login failed for user: " + userName);
        return null; // return null if logic failed
    }

    public void saveAccounts() {
        try (FileWriter writer = new FileWriter(fileName)) {
            StringBuilder json = new StringBuilder("[\n");
            
            // Iterate through all accounts
            for (int i = 0; i < accounts.size(); i++) {
                Account account = accounts.get(i);
                json.append("  {\n");
                
                // Write account basic information
                json.append("    \"userName\": \"").append(account.getUserName()).append("\",\n");
                json.append("    \"pin\": \"").append(account.getPin()).append("\",\n");
                json.append("    \"dateOfBirth\": \"").append(account.getDateOfBirth()).append("\",\n");
                json.append("    \"balance\": ").append(account.getBalance()).append(",\n");
                json.append("    \"accountNumber\": \"").append(account.getAccountNumber()).append("\",\n");
                json.append("    \"routingNumber\": \"").append(account.getRoutingNumber()).append("\",\n");
                
                // Write transactions array
                json.append("    \"transactions\": [\n");
                List<Transaction> transactions = account.getTransactions();
                
                for (int j = 0; j < transactions.size(); j++) {
                    Transaction t = transactions.get(j);
                    json.append("      {\n");
                    json.append("        \"type\": \"").append(t.getType()).append("\",\n");
                    json.append("        \"amount\": ").append(t.getAmount()).append(",\n");
                    json.append("        \"dateTime\": \"").append(t.getDateTime()).append("\"\n");
                    
                    // Add comma if not last transaction
                    if (j < transactions.size() - 1) {
                        json.append("      },\n");
                    } else {
                        json.append("      }\n");
                    }
                }
                
                json.append("    ]\n");
                
                // Add comma if not last account
                if (i < accounts.size() - 1) {
                    json.append("  },\n");
                } else {
                    json.append("  }\n");
                }
            }
            json.append("]\n");
            
            // Write the complete JSON to file
            writer.write(json.toString());
            // System.out.println("Accounts saved to JSON file: " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadAccounts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            parseJson(jsonBuilder.toString()); // Convert from JSON to Account objects
            // System.out.println("Accounts loaded from JSON file: " + fileName);
        } catch (FileNotFoundException e) {
            System.out.println("JSON file not found. Starting with an empty account list.");
        } catch (IOException e) {
            System.out.println("Error loading accounts: " + e.getMessage());
        }
    }

    private void parseJson(String json) {
        if (json.isEmpty() || json.equals("[]")) return;
        // System.out.println("Parsing JSON data...");

        try {
            // remove the outer array brackets
            json = json.substring(1, json.length() - 1);

            // split individual account objects while respecting the structure 
            List<String> accountStrings = splitAccountObjects(json);

            // process each account object
            for (String accountString : accountStrings) {
                processAccountObject(accountString);
            }

        } catch (Exception e) {
            System.out.println("Error parsing JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void processAccountObject(String accountString) {

        // find the values of each field in the account
        String userName = extractValue(accountString, "userName");
        String pin = extractValue(accountString, "pin");
        String dateOfBirth = extractValue(accountString, "dateOfBirth");
        double balance = Double.parseDouble(extractValue(accountString, "balance"));
        String accountNumber = extractValue(accountString, "accountNumber");
        String routingNumber = extractValue(accountString, "routingNumber");
        
        // create account instance
        Account account = new Account(pin, userName, LocalDate.parse(dateOfBirth), balance, accountNumber, routingNumber);

        List<Transaction> transactions = parseTransactions(accountString);

        // set transactions and add account to list
        account.setTransactions(transactions);
        accounts.add(account);
        // System.out.println("Account loaded: " + userName);
    }

    private List<Transaction> parseTransactions(String accountString) {
        List<Transaction> transactions = new ArrayList<>();

        // exctract transactions section from the json
        String transactionString = accountString.substring(accountString.indexOf("\"transactions\"")); // extracts from the name to the end of string
        transactionString = transactionString.substring(transactionString.indexOf("["), transactionString.lastIndexOf("]") + 1); // extracts the array of the transaction field

        // process individual transactions if any exist
        if (!transactionString.equals("[]")) {
            String[] transactionArray = transactionString.split("\\{"); // split the array at the bracket and make it an array

            for (String transString : transactionArray) {
                if (transString.contains("type")) { // check if its a valid transaction object
                    String type = extractValue(transString, "type");
                    double amount = Double.parseDouble(extractValue(transString, "amount"));
                    String dateTime = extractValue(transString, "dateTime");
                    transactions.add(new Transaction(type, amount, LocalDateTime.parse(dateTime)));
                }
            }
        }

        return transactions;
    }

    private String extractValue(String json, String key){
        int start = json.indexOf("\"" + key + "\""); // creates json pattern "userName"
        if(start == -1) return null;

        start = json.indexOf(":", start) + 1; // find where the start of the value is after the color
        
        // skip whitespace             // check if theres a space where start is at rn
        while (start < json.length() && Character.isWhitespace(json.charAt(start))) {
            start++;
        }

        boolean isString = json.charAt(start) == '"'; // check if the value is a string (String json starts with quotes)

        if (isString) {
            start++; // skipp the opening quote

            // find the closing quote from the current position
            int end = json.indexOf("\"", start); 
            return json.substring(start, end);
        } else {
            // for numbers, read until it hits a non digit character
            int end = start;
            while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '.')) {
                end++; // keep looking for the end of the value       
            }

            return json.substring(start, end); // returns the value from after the color to the end of the value
        }
    }

    private List<String> splitAccountObjects(String json) {
        List<String> accountStrings = new ArrayList<>();
        int depth = 0;
        StringBuilder currentString = new StringBuilder();

        // parse character by character to handle nested structures
        for (char c : json.toCharArray()) {
            currentString.append(c);
            if (c == '{') depth++; // next indentation - main information
            if (c == '}') {
                depth--;
                if (depth == 0) {
                    // found a complete account object
                    accountStrings.add(currentString.toString()); // convert the whole object to a single string to handle them separetely
                    currentString = new StringBuilder(); // prepare a new string for the next iteration

                }
            }

        }
        return accountStrings; 
        // returns list of accounts converted in strings meaning each index is an account
    }



    public List<Account> getAccounts() {
        return accounts;
    }
}
