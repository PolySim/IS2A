package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import src.User;

public class UserTest {

  @Test
  public void testIsValid() {
    User user = new User("test", "test1234");
    assertTrue(user.isValid());
  }

  @Test
  public void testIsValidUsernameNull() {
    User user = new User(null, "test1234");
    assertFalse(user.isValid());
  }

  @Test
  public void testIsValidUsernameEmpty() {
    User user = new User("", "test1234");
    assertFalse(user.isValid());
  }

  @Test
  public void testIsValidPasswordToShort() {
    User user = new User("test", "hiii");
    assertFalse(user.isValid());
  }

  @Test
  public void testIsValidPasswordNull() {
    User user = new User("test", null);
    assertFalse(user.isValid());
  }

  @Test
  public void testChangePassword() {
    User user = new User("", "test1234");
    assertTrue(user.changePassword("test1234", "test12345"));
  }

  @Test
  public void testChangePasswordBadCurrent() {
    User user = new User("", "test1234");
    assertFalse(user.changePassword("test123", "test12345"));
  }

  @Test
  public void testChangePasswordNewToShort() {
    User user = new User("", "test1234");
    assertFalse(user.changePassword("test1234", "test123"));
  }
}
