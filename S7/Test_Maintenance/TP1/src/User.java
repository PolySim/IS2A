package src;

public class User {

  private String username;
  private String password;

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  // Returns true if the username and password are valid
  public boolean isValid() {
    return username != null && !username.isEmpty() && password != null && password.length() >= 8;
  }

  // Changes the password if the current password is correct
  public boolean changePassword(String currentPassword, String newPassword) {
    if (currentPassword.equals(this.password) && newPassword.length() >= 8) {
      this.password = newPassword;
      return true;
    }
    return false;
  }
}