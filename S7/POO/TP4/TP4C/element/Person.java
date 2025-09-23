package element;

public class Person implements Comparable<Person> {
  int age;

  public Person(int age) {
    this.age = age;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public int compareTo(Person other) {
    return this.getAge() - other.getAge();
  }

  public String toString() {
    return "Person: " + this.getAge();
  }
}
