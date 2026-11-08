public class HourlyEmployee extends Employee {
    double hoursPerMonth;

    public HourlyEmployee(String name, double salary, double hoursPerMonth) {
        super(name, salary);
        this.hoursPerMonth = hoursPerMonth;
    }

    @Override
    public double getAnnualSalary() {
        return hoursPerMonth * 12 * salary;
    }

    @Override
    public void printInfo() {
        System.out.println("姓名：" + this.name + "（时薪制）\n年薪：" + getAnnualSalary());
    }
}
