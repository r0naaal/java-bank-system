# Java Bank System
A fully functional, console-based banking system built in Java.

This project was initially created as a school project, but I decided to step it up and challenge myself by learning about databases. To practice, I tried to mock a simple database for storing user and transaction data.

## Features

- **User Registration & Login:** Secure registration with unique username and 4-digit PIN.
- **Account Management:** View account details, balance, and masked PIN.
- **Deposits & Withdrawals:** Deposit or withdraw funds with transaction tracking.
- **Fund Transfers:** Transfer money between accounts using unique account numbers.
- **Transaction History:** View a detailed history of all account transactions.
- **PIN Change:** Securely update your account PIN.
- **Persistent Storage:** All user data and transactions are saved in `users.json`.

### Running the Application
- Java 8 or higher installed on your system.

1. **Clone or Download the Repository**
2. **Compile the Source Files:**
    ```sh
    javac src/*.java
    ```
3. **Run the Application:**
    ```sh
    java -cp src Runner
    ```
### File Structure

- `src/Account.java` - Account model
- `src/Transaction.java` - Transaction model
- `src/UserManager.java` - Handles user data and persistence
- `src/Runner.java` - Main application logic and user interface
- `users.json` - Stores all user and transaction data

## Usage

- **Register:** Create a new account with a unique username and PIN.
- **Login:** Access your account using your credentials.
- **Main Menu:** Choose actions like deposit, withdraw, transfer, view history, or change PIN.
- **Logout:** Securely log out when finished.

## License

- MIT
---

Thanks for checking out my project! If you like it, please give it a ⭐ on GitHub.
