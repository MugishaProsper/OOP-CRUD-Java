// Student class inherits from Person
public class Student extends Person {
    private int id;
    private String combination;

    // Constructor
    public Student(int id, String firstName, String lastName, int age, String gender, String combination) {
        super(firstName, lastName, age, gender);
        this.id = id;
        this.combination = combination;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCombination() {
        return combination;
    }

    public void setCombination(String combination) {
        this.combination = combination;
    }
}
