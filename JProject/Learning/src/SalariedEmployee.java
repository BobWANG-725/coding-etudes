public class SalariedEmployee extends Employee {
    public SalariedEmployee(String name, double salary) {
        super(name, salary);
    }

    @Override
    public double getAnnualSalary() {
        return salary * 12;
    }

    @Override
    public void printInfo() {
        System.out.println("姓名：" + this.name + "（月薪制）\n年薪：" + getAnnualSalary());
    }

}
