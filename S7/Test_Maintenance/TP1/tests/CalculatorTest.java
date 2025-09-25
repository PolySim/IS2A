package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import src.Calculator;

public class CalculatorTest {

  @Test
  public void testAdd() {
    Calculator calc = new Calculator();
    assertEquals(5, calc.add(2, 3), "2 + 3 should equal 5");
  }

  @Test
  public void testSubtract() {
    Calculator calc = new Calculator();
    assertEquals(1, calc.subtract(3, 2), "3 - 2 should equal 1");
  }

  @Test
  public void testMultiply() {
    Calculator calc = new Calculator();
    assertEquals(6, calc.multiply(2, 3), "2 * 3 should equal 6");
  }

  @Test
  public void testDivide() {
    Calculator calc = new Calculator();
    assertEquals(2, calc.divide(6, 3), "6 / 3 should equal 2");
  }

  @Test
  public void testDivideByZero() {
    Calculator calc = new Calculator();
    assertThrows(ArithmeticException.class, () -> calc.divide(6, 0), "6 / 0 should throw an ArithmeticException");
  }
}