public class Main {
    public static void main(String[] args) {
        Employee a = new SalariedEmployee("张三", 8000);
        Employee b = new HourlyEmployee("李四", 50, 160);

        System.out.println(a.getAnnualSalary());
        System.out.println(b.getAnnualSalary());
/////////////////////////////////////////////////////////////////////////////////
        Employee[] employees = new Employee[2];
        employees[0] = a;
        employees[1] = b;

//  for (Employee e : employees) System.out.println(e.name + " 的年薪：" + e.getAnnualSalary());
//////////////////////////////////////////////////////////////////////////////////////////////////
        for (Employee employee : employees) {
            System.out.println(employee.getAnnualSalary());
            employee.printInfo();
        }
    }
}
