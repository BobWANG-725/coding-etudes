public abstract class Employee implements Printable {
    String name;
    double salary;

    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    public abstract double getAnnualSalary();
}