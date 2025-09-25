package src;

public class Calculator {

  // Adds two numbers
  public int add(int a, int b) {
    return a + b;
  }

  // Subtracts the second number from the first
  public int subtract(int a, int b) {
    return a - b;
  }

  // Multiplies two numbers
  public int multiply(int a, int b) {
    return a * b;
  }

  // Divides the first number by the second
  // Should throw an ArithmeticException if division by zero is attempted
  public int divide(int a, int b) {
    if (b == 0) {
      throw new ArithmeticException("Division by zero is not allowed");
    }
    return a / b;
  }
}