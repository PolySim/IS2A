package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import src.BankAccount;

public class BankAccountTest {

  @Test
  public void TestDepotPositif() {
    BankAccount bankAccount = new BankAccount();
    bankAccount.deposit(1);
    assertEquals(1, bankAccount.getBalance());
  }

  @Test
  public void TestDepotNegatif() {
    BankAccount bankAccount = new BankAccount();
    assertThrows(IllegalArgumentException.class, () -> bankAccount.deposit(-1));
  }

  @Test
  public void TestRetraitPositif() {
    BankAccount bankAccount = new BankAccount(2);
    bankAccount.withdraw(1);
    assertEquals(1, bankAccount.getBalance());
  }

  @Test
  public void TestRetraitSup() {
    BankAccount bankAccount = new BankAccount();
    assertThrows(IllegalStateException.class, () -> bankAccount.withdraw(1));
  }
}
