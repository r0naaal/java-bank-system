import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Runner {
    
    private UserManager userManager;
    private Scanner scan = new Scanner(System.in);
    private Random rand = new Random();
        
    public boolean isUserLogged = false; // Flag to send user to the welcome page
    private Account currentAccount; // Field to track the account that's logged in
    
    public Runner() {
        userManager = new UserManager();
        showWelcomeMenu();
        showMainMenu();
    }
    
    public void registerUser() {
        // Username
        String userName;
        boolean unique = false;

        do {
            System.out.println("\n------ Registration Section ------");
            typeAnim("Welcome to the Registration process! Please provide the following details to create your account.\n", 15);
            typeAnim("Enter a unique Username (5-10 characters, no digits only): ", 15);
            userName = scan.nextLine().trim();
    
            // Validate username
            if (userName.length() < 5) {
                System.out.println("❌ Username must be at least 5 characters long. Please try again.");
                continue;
            } else if (userName.length() > 10) {
                System.out.println("❌ Username cannot exceed 10 characters. Please try again.");
                continue;
            } else if (userName.matches("\\d+")) {
                System.out.println("❌ Username cannot consist of digits only. Please choose a different username.");
                continue;
            }
    
            // Check if username is unique
            unique = userManager.isUserNameUnique(userName);
            if (!unique) {
                System.out.println("❌ Username is already taken. Please choose another.");
            }
        } while (!unique); // Repeat until a unique username is entered
    
        // PIN 
        String pin;
        while (true) {
            typeAnim("Enter a secure 4-Digit PIN: ", 15);
            pin = scan.nextLine().trim();
    
            if (pin.length() == 4 && pin.matches("\\d{4}")) {
                break; // Valid PIN
            } else {
                System.out.println("❌ PIN must be a 4-digit number.");
            }
        }
    
        // Date of birth
        LocalDate localDate = null;
        while (true) {
            System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
            String dateString = scan.nextLine().trim();
    
            // Check for empty input
            if (dateString.isEmpty()) {
                System.out.println("❌ Input cannot be empty. Please enter a valid date.");
                continue; // Ask for input again
            }
    
            try {
                localDate = LocalDate.parse(dateString);
                break; // Exit the loop if parsing was successful
            } catch (DateTimeParseException e) {
                System.out.println("❌ Invalid date format. Please use the format YYYY-MM-DD.");
            }
        }
    
        // Generate account number and routing number
        String accountNumber = generateUniqueAccountNumber();
        String routingNumber = generateRoutingNumber();
        
        // Create new account
        Account newAccount = new Account(pin, userName, localDate, 0.0, accountNumber, routingNumber);
        userManager.addAccount(newAccount);

        typeAnim("✅ Registration successful! You can now log in to your account.\n", 15);
    }
    
    public void userLogin() {
        int maxAttempts = 3; // Maximum allowed attempts
        int attempts = 0; // Counter for attempts

        System.out.println("\n------- Welcome Back! -------\n");
        System.out.print("Have you registered with us before? (yes/no): ");
        String registeredResponse = scan.nextLine().trim();
        
        if (registeredResponse.equalsIgnoreCase("no") || registeredResponse.equalsIgnoreCase("n") || registeredResponse.equalsIgnoreCase("false")) {
            registerUser(); // redirect to registration
            return; // exit login method
        } else if (!registeredResponse.equalsIgnoreCase("yes") && !registeredResponse.equalsIgnoreCase("y") && !registeredResponse.equalsIgnoreCase("true")) {
            System.out.println("❌ Invalid response. Please try again and with 'yes' or 'no'. ");
            return; // return to the last menu without logging in
        }

        System.out.println("\n----- Login Section -----\n");
        typeAnim("Please enter your credentials to access your account.\n", 15);
        while (attempts < maxAttempts) {
            typeAnim("Enter your Username: ", 15);
            String userName = scan.nextLine().trim();
            typeAnim("Enter your PIN: ", 15);
            String pin = scan.nextLine().trim();

            // Check login logic
            Account account = userManager.login(userName, pin);
            if (account != null) {
                typeAnim("Login Successful! Welcome, " + userName + "!\n", 15);
                isUserLogged = true;
                currentAccount = account; // set the current account
                return; // exit login method
            } else {
                System.out.println("❌ Login failed. Please check your username and PIN.");
                attempts++;
            }
        }
        System.out.println("🚫 Maximum login attempts exceeded. Please try again later.");
    }

    public void showWelcomeMenu() {
        while (!isUserLogged) {
            System.out.println("\n=================================================");
            System.out.println("|     Welcome to J.M Financial Bank System!     |");
            System.out.println("=================================================\n");

            typeAnim("Please select an option from the menu below:\n", 15);
            typeAnim("1. Register a new account\n", 15);
            typeAnim("2. Login to your existing account\n", 15);
            typeAnim("3. Exit the application\n", 15);
            typeAnim("Choose an option: ", 15);

            int choice;
            try {
                choice = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input, please enter a number.");
                continue; // Go back to the start of the loop
            }

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    userLogin();
                    break;
                case 3:
                    typeAnim("Exiting the application. Thank you for visiting!\n", 20);
                    typeAnim("\nMade by Ronal :)\n", choice);
                    return; // Exit the loop and the program
                default:
                    System.out.println("❌ Invalid option. Please try again.");
            }
        }
    }

    public void showMainMenu() {
        while (isUserLogged) {
            System.out.println("\n----------------------------");
            System.out.println("|      Your Main Menu      |");
            System.out.println("----------------------------\n");
            typeAnim("Please select an option from the menu below:\n", 15);
            typeAnim("1. View Account Overview\n", 15);
            typeAnim("2. Deposit Funds into your account\n", 15);
            typeAnim("3. Withdraw Funds from your account\n", 15);
            typeAnim("4. Transfer Funds to another account\n", 15);
            typeAnim("5. View Transaction History\n", 15);
            typeAnim("6. Change your Account PIN\n", 15);
            typeAnim("7. Logout from your account\n", 15);
            typeAnim("Choose an option: ", 15);

            int userResponse;
            try {
                userResponse = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input, please enter a valid number.");
                continue; // Go back to the start of the loop
            }

            switch (userResponse) {
                case 1:
                    displayAccountOverview();
                    break;
                case 2:
                    deposit();
                    break;
                case 3:
                    withdraw();
                    break;
                case 4:
                    fundTransfer();
                    break;
                case 5:
                    transactionsHistory();
                    break;
                case 6:
                
                    System.out.println("\n-----------------------");
                    System.out.println("|      Change Pin     |");
                    System.out.println("-----------------------\n");

                    typeAnim("Enter your new 4-digit PIN: ", 20);
                    String newPinInput = scan.nextLine().trim();
                    if (newPinInput.length() == 4 && newPinInput.matches("\\d{4}")) {
                        System.out.print("Updating your PIN");
                        typeAnim("...", 1000); // simulate loading
                        userManager.updatePin(currentAccount, newPinInput); // call method in userManager to update PIN
                        System.out.println("✅ PIN changed successfully.");
                    } else {
                        System.out.println("❌ Invalid PIN format. Please enter a valid 4-digit numeric PIN.");
                    }
                    break;
                case 7:
                    typeAnim("You have chosen to Logout. Thank you for using our services!\n", 20);
                    isUserLogged = false; // Logout logic
                    showWelcomeMenu();
                    break;
                default:
                    System.out.println("❌ Invalid option. Please try again.");
                    break;
            }
        }
    }

    private void transactionsHistory() {
        List<Transaction> transactions = currentAccount.getTransactions();
        
        System.out.println("\n-------------------------------");
        System.out.println("|     Transaction History     |");
        System.out.println("-------------------------------\n");

        if (transactions.isEmpty()) {
            System.out.println("❌ No transactions found.");
        } else {
            System.out.println("----------------------------------------------------------");
            System.out.printf("%-15s | $%-9s | %-15s | %s%n", "Transaction Type", "Amount", "Date", "Time");
            System.out.println("----------------------------------------------------------");
            
            for (Transaction transaction : transactions) {
                String date = transaction.getDateTime().toLocalDate().toString();
                String time = transaction.getDateTime().toLocalTime().toString().substring(0, 8); // Format time
                
                System.out.printf("%-15s | $%.2f    | %s      | %s%n",
                        transaction.getType(),
                        transaction.getAmount(),
                        date,
                        time);
            }
            
            System.out.println("----------------------------------------------------------");
        }
    }

    private void fundTransfer() {
        System.out.println("\n---------------------------");
        System.out.println("|      Fund Transfer      |");
        System.out.println("---------------------------\n");
        typeAnim("You are initiating a Fund Transfer. Please provide the recipient's account details and the amount.\n", 15);
        typeAnim("Enter the amount to transfer: ", 15);
        double amount;
    
        try {
            amount = Double.parseDouble(scan.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Please enter a valid numeric amount.");
            return; // Exit the method if input is invalid
        }
    
        if (amount <= 0) {
            System.out.println("❌ Transfer amount must be positive.");
            return;
        }
    
        if (amount > currentAccount.getBalance()) {
            System.out.println("❌ Insufficient funds for this transfer.");
            return;
        }
    
        typeAnim("Enter recipient's account number: ", 15);
        String recipientAccountNumber = scan.nextLine().trim();
        Account recipientAccount = userManager.isAccountNumberUnique(recipientAccountNumber); // Retrieve the account
    
        if (recipientAccount == null) {
            System.out.println("❌ Recipient account not found.");
            return; // Exit if the recipient doesn't exist
        }
    
        typeAnim("You are sending $" + amount + " to " + recipientAccount.getUserName() + ".\n", 15);
        typeAnim("Are you sure you want to proceed? (yes/no): ", 15);
        String sendMoney = scan.nextLine().trim();
        
        if (sendMoney.equalsIgnoreCase("yes") || sendMoney.equalsIgnoreCase("y")) {
            typeAnim("Transferring money", 20);
            typeAnim("...\n", 1000); // Simulate loading
            currentAccount.withdraw(amount);
            recipientAccount.deposit(amount);
            userManager.updateBalance(currentAccount); // Update balance in UserManager
            userManager.updateBalance(recipientAccount); // Update balance in UserManager
            currentAccount.addTransaction(new Transaction("Transfer", amount, LocalDateTime.now())); // Transfer user made
            recipientAccount.addTransaction(new Transaction("Incoming Transfer", amount, LocalDateTime.now())); // Incoming transfer for recipient
            userManager.saveAccounts();
            System.out.printf("✅ $%.2f was successfully transferred to %s.\n", amount, recipientAccount.getUserName());
        } else {
            System.out.println("Transfer canceled.");
        }
    }

    private void withdraw() {
        System.out.println("\n--------------------");
        System.out.println("|     Withdraw     |");
        System.out.println("--------------------\n");
        typeAnim("Enter withdrawal amount: ", 20);
        double amount;
    
        try {
            amount = Double.parseDouble(scan.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Please enter a valid numeric amount.");
            return; // Exit the method if input is invalid
        }
    
        if (amount > 0) {
            if (currentAccount.getBalance() >= amount) {
                currentAccount.withdraw(amount);
                userManager.updateBalance(currentAccount);
                currentAccount.addTransaction(new Transaction("Withdraw", amount, LocalDateTime.now()));
                userManager.saveAccounts();
                System.out.printf("✅ Withdrew: $%.2f successfully.\n", amount);
                System.out.printf("Current Balance: $%.2f\n", currentAccount.getBalance());
            } else {
                System.out.println("❌ Insufficient funds.");
            }
        } else {
            System.out.println("❌ Withdrawal amount must be positive.");
        }
    }

    public void deposit() {
        System.out.println("\n---------------------");
        System.out.println("|      Deposit      |");
        System.out.println("---------------------\n");
        typeAnim("Enter the amount you wish to deposit: ", 20);
        double amount;
        
        try {
            amount = Double.parseDouble(scan.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Please enter a valid numeric amount.");
            return; // Exit the method if input is invalid
        }
        
        if (amount > 0) {
            currentAccount.deposit(amount);
            userManager.updateBalance(currentAccount); // Update database
            currentAccount.addTransaction(new Transaction("Deposit", amount, LocalDateTime.now()));
            userManager.saveAccounts();
            System.out.printf("✅ Deposited: $%.2f successfully.\n", amount);
            System.out.printf("Current Balance: $%.2f\n", currentAccount.getBalance());
        } else {
            System.out.println("❌ Deposit amount must be positive.");
        }
    }
    
    private void displayAccountOverview() {
        System.out.println("\n----------------------------------");
        System.out.println("|       Account Overview         |");
        System.out.println("----------------------------------");
        System.out.println("Account Type:   Savings");
        System.out.println("Account Owner:  " + currentAccount.getUserName());
        System.out.println("Date of Birth:  " + currentAccount.getDateOfBirth());
        System.out.println("PIN:            ####"); // Masked PIN for security
        System.out.printf("Balance:        $%.2f%n", currentAccount.getBalance());
    }
    
    private String generateUniqueAccountNumber() {
        String accountNumber; // Using account number as ID
        boolean unique = false;

        do {
            accountNumber = String.format("%09d", rand.nextInt(1000000000)); // Generate a 9-digit number
            if (userManager.isAccountNumberUnique(accountNumber) == null) {
                unique = true;
            }
        } while (!unique);

        return accountNumber;
    }

    private String generateRoutingNumber() {
        return String.format("%09d", rand.nextInt(1000000000)); // Generate a 9-digit number
    }

    public void typeAnim(String message, int delay) {
        for (char c : message.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        new Runner();
    }
}
