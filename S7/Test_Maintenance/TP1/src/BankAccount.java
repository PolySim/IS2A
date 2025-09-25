package src;

public class BankAccount {

  private double balance;

  public BankAccount(double initialBalance) {
    this.balance = initialBalance;
  }

  public BankAccount() {
    this(0);
  }

  // Returns the current balance
  public double getBalance() {
    return balance;
  }

  // Deposits money into the account, but throws an IllegalArgumentException
  // if the amount is negative
  public void deposit(double amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Cannot deposit negative amounts");
    }
    balance += amount;
  }

  // Withdraws money from the account if enough balance is available
  // Throws an IllegalArgumentException if the amount is negative
  // Throws an IllegalStateException if the amount exceeds the balance
  public void withdraw(double amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Cannot withdraw negative amounts");
    }
    if (amount > balance) {
      throw new IllegalStateException("Insufficient balance");
    }
    balance -= amount;
  }
}