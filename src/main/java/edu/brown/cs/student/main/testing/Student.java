package edu.brown.cs.student.main.testing;

/** A Student class with three initial fields */
public class Student {
  String name;
  String age;
  String major;

  public Student(String name, String age, String major) {
    this.name = name;
    this.age = age;
    this.major = major;
  }

  /**
   * This is used for the testing where we chekc the content
   *
   * @param student1
   * @return
   */
  public boolean studentEquals(Student student1) {
    return this.name.equals(student1.name)
        && this.age.equals(student1.age)
        && this.major.equals(student1.major);
  }
}
