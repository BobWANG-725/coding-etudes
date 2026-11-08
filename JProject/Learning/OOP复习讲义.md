# 面向对象复习讲义（以 JProject/Learning/src 里的代码为例）

> 环境已确认：Temurin JDK 25.0.3，你的 src 全部能编译通过（`-Xlint:all` 只有两条无关紧要的 warning）。
> 也就是说，你不是"不会写"，而是"写对了但不知道自己为什么对"——复习的目标是把直觉变成可解释的规则。

---

## 一、学习地图：六个概念其实是一条线

```
数据 + 行为  ──►  类（class）
     │              封装：谁能看见、谁能改
     ▼
  方法（method）──► 静态 / 实例；重载 vs 重写；值传递
     │
     ▼
  继承（extends）─► is-a；复用 + 扩展；单继承；super
     │
     ▼
  多态（polymorphism）─► 向上转型 + 动态绑定 → 一套代码处理所有子类
     │
     ▼
  接口（interface）──► 能力契约；多实现；解耦
     │
     ▼
  数组（array）─────► 多态的容器：Employee[] 装不同子类
```

这条线的终点是 Main.java 里那三行循环：**用父类型写一次，对所有子类都成立**。

---

## 二、逐个知识点：你的代码 → 规则

### 1. 类与对象

你的 `Employee`：

```java
public abstract class Employee implements Printable {
    String name;          // 默认包访问权限（package-private）
    double salary;
```

- **对象 = 状态（字段）+ 行为（方法）**；`Employee` 有 name/salary 两个状态，`getAnnualSalary()` 一个行为契约。
- 关键结论：**字段不加 `private` 就等于对同包所有人公开**。现在任何同包代码都能写 `a.salary = -1`，绕过你所有校验。封装不是为了好看，是为了让"合法性"只有一个入口（构造器 / setter）。
- `abstract` 类不能 `new`，它的存在意义是：**定义契约 + 存放共同代码**。

### 2. 方法

| 你的代码 | 类型 | 判断标准 |
|---|---|---|
| `Array2DSumEtMax.arraySum(int[][])` | `static` 工具方法 | 不依赖对象状态，只依赖参数 |
| `Employee.getAnnualSalary()` | 实例方法（抽象） | 依赖 `this.salary` |
| `Student.findTopStudent(Student[])` | `static` 工厂式方法 | 只处理参数 |

三个易混点：

1. **重载 vs 重写**：你代码里只有重写（`@Override`），没有重载。重载看参数列表（编译期决定），重写看对象真实类型（运行期决定）。
2. **Java 只有值传递**。`ArrayReverse.reverse(int[] arr)` 能改数组内容，是因为传的是"引用的副本"，两个引用指向同一个数组；但如果你在方法里写 `arr = new int[]{...}`，调用方的数组一点不变。这是从 C 过来最容易错的地方（Java 的引用 ≈ 不能做指针运算的指针，但**是值传递**）。
3. **返回值即设计**。`arrayMaxCoordination` 返回 `StringBuilder`——方法名说"坐标"，实际返回拼好的句子，调用方无法再拿坐标做别的事。更好的设计是返回 `int[] {row, col}` 或一个 `record Position(int row, int col)`，格式化交给打印层。

### 3. 继承

```java
public class HourlyEmployee extends Employee {
    double hoursPerMonth;
    public HourlyEmployee(String name, double salary, double hoursPerMonth) {
        super(name, salary);        // 必须是构造器第一句
        this.hoursPerMonth = hoursPerMonth;
    }
```

- 继承表达 **is-a**：时薪员工 **是** 员工，所以能出现在任何 `Employee` 的位置。
- 子类**扩展**状态（`hoursPerMonth`）并**重写**行为（`getAnnualSalary`）。
- Java **单继承**：一个类只能 `extends` 一个父类（接口才能多实现）。
- 构造器不被继承，子类必须自己写并调用 `super(...)`。
- 你的 `Employee` 实现了 `Printable` 却没实现 `printInfo()`——这合法，因为它是 `abstract`：**抽象类可以把接口的实现义务下推给具体子类**。这是一个很值得记住的细节。

**代码味道**：`SalariedEmployee.printInfo()` 和 `HourlyEmployee.printInfo()` 几乎一模一样，只差"（月薪制）/（时薪制）"。这说明"流程"属于父类，"差异"才属于子类 → **模板方法**（见第六节重构版）。

### 4. 多态（重点）

```java
Employee a = new SalariedEmployee("张三", 8000);   // 向上转型，自动
Employee b = new HourlyEmployee("李四", 50, 160);
System.out.println(a.getAnnualSalary());           // 调用谁？
```

- **编译看左边，运行看右边**：能不能写 `a.getAnnualSalary()` 由 `Employee` 的静态类型决定；真正执行哪份代码由 `a` 指向的**实际对象**决定（动态绑定 / 虚方法分派）。
- 所以 `a.hoursPerMonth` **编译不过**——`Employee` 上没有这个字段。要用得先向下转型：

```java
if (a instanceof HourlyEmployee h) {      // Java 16+ 模式匹配
    System.out.println(h.hoursPerMonth);
}
```

- **为什么需要多态**：Main 里那个循环，加第三种员工（比如 `CommissionEmployee` 底薪+提成）时，**一行都不用改**。这就是开闭原则（对扩展开放，对修改关闭）的最小例子。
- **两个经典陷阱**：
  1. **字段不参与多态**。父类引用访问字段，永远拿父类那份。
  2. **`static` 方法不能被重写**，只能被"隐藏"，调用取决于静态类型。

### 5. 接口

```java
public interface Printable {
    void printInfo();
}
```

- 接口 = **能力契约**（"能打印"），抽象类 = **分类**（"是员工"）。`Employee` 同时是抽象类又实现接口，正是最常见的组合用法。
- 一个类可以 `implements` 多个接口；接口可以 `extends` 多个接口。
- 接口里的字段隐式 `public static final`；方法隐式 `public abstract`（Java 8 起还允许 `default` / `static` / 私有方法）。
- 你这个接口只有一个抽象方法 → 是**函数式接口**，可以写 `Printable p = () -> System.out.println("hi");`
- 设计上的小问题：`printInfo()` 直接往 `System.out` 打印，副作用硬编码，没法测试、没法改成写文件。让它返回 `String` 会更好。

### 6. 数组

```java
Employee[] employees = new Employee[2];   // 元素初始为 null
employees[0] = a;
employees[1] = b;

for (Employee employee : employees) { ... }   // 增强 for
```

- **对象数组的每个格子是引用**，`new Employee[2]` 只造了两个 `null` 格子。
- 增强 for 的循环变量是**副本**，`employee = null` 不会影响数组。
- `int[][] data` 在 Java 里是**数组的数组**（可以每行长度不同）：
  ```java
  int[][] data = { {3,5,1}, {9,9}, {4,8,6,7} };   // 合法的锯齿数组
  ```
  所以你写 `arr2D[i].length` 而不是 `arr2D[0].length` 是**对的**，很多人错在这。
- `length` 是字段（`arr.length`），不是方法；`String` 的才是方法（`s.length()`）。
- 越界抛 `ArrayIndexOutOfBoundsException`：`arrayMax` 遇到空数组、`findTopStudent` 遇到空数组都会炸。
- 下一站：`ArrayList`——解决"长度定死"的问题。

---

## 三、五个必须记住的边界

1. **字段隐藏 ≠ 方法重写**：字段看静态类型，方法看运行时类型。
2. **抽象类可以有构造器**（供 `super()` 用），但**不能 `new`**。
3. **`super(...)` 必须是子类构造器第一条语句**。
4. **接口不能有实例字段和构造器**；抽象类两者都可以有。
5. **`static` 方法属于类，不属于对象**，谈不上多态。

---

## 四、自测题（先答，再看答案）

1. `Employee e = new HourlyEmployee("李四", 50, 160); System.out.println(e.hoursPerMonth);` 能编译吗？
2. 若把 `Employee.getAnnualSalary()` 改成普通方法 `return salary * 12;`，并删掉 `HourlyEmployee` 里的重写，"李四"的年薪是多少？
3. 下面输出几？
   ```java
   class A { int x = 1; }
   class B extends A { int x = 2; }
   A a = new B();
   System.out.println(a.x);
   ```
4. `for (Employee e : employees) { e = null; }` 会把数组元素清空吗？
5. `Student.findTopStudent(new Student[0])` 会发生什么？
6. `Printable` 再增加一个抽象方法后，还是函数式接口吗？
7. 抽象类能被 `final` 修饰吗？`abstract` 和 `final` 能一起用吗？
8. `int[][] a = new int[3][];` 合法吗？`new int[][3]` 呢？
9. `System.out.println(a)` 打印 `Employee` 对象，输出什么？怎么改？
10. 为什么 `Employee` 里字段写成 `String name` 而不是 `private String name`，是个问题？

<details>
<summary>答案</summary>

1. **不能**。编译期按 `Employee` 类型检查，没有 `hoursPerMonth`。需 `instanceof` 向下转型。
2. **600**。`salary * 12 = 50 * 12`，因为方法体是父类的那份，`salary` 是父类字段（50，即"时薪"）。这题说明：**重写被删掉后，行为退回父类版本**——多态是"重写"带来的，不是"继承"自动送的。
3. **1**。字段访问按静态类型 `A` 解析。
4. **不会**。循环变量是元素引用的副本。
5. `arr[0]` 直接越界，抛 `ArrayIndexOutOfBoundsException`。要先判断 `arr.length == 0`。
6. **不是**。函数式接口要求恰好一个抽象方法（`default` / `static` 不计）。
7. 抽象类**不能**是 `final`（互相矛盾）；`abstract final` 编译错误。
8. `new int[3][]` **合法**（先确定行数）；`new int[][3]` **不合法**（不能只给列）。
9. 打印 `类名@十六进制哈希`，如 `SalariedEmployee@1b6d3586`。重写 `toString()` 即可。
10. 因为默认包权限允许同包任意代码直接改字段，破坏不变量（比如把 salary 改成负数），也让子类/外部强耦合到字段名，日后改内部表示会牵连一片。

</details>

---

## 五、你代码里的问题清单（按优先级）

| 优先级 | 位置 | 现象 | 建议 |
|---|---|---|---|
| 高 | `Employee.java:2-3` | 字段包可见，外部可随意改 | `private final` + getter |
| 高 | `SalariedEmployee/HourlyEmployee` 的 `printInfo` | 两处近乎重复 | 模板方法上提到 `Employee` |
| 中 | `Student.java:15` | `else if (score < 0)` 永远不会执行 | 删掉（构造器已保证 0~100） |
| 中 | `Student.java:37-41` | `try` 包住的是 `isPassed()`，它并不抛异常；真正抛点在 `new Student(...)` | 把构造调用放进 `try` |
| 中 | `Student.java:19-27` | 空数组越界；返回 `String` 丢失对象 | 返回 `Student`，加长度校验 |
| 中 | `Array2DSumEtMax.java:16` | `arr2D[0][0]` 对空数组/空首行越界 | 先判空 |
| 低 | `Main.java:9-11` | 手动 `new Employee[2]` 再逐个赋值 | 用数组字面量 `{ ... }` |
| 低 | 全部实体类 | 没重写 `toString()` | 打印出来全是哈希码 |
| 低 | `HourlyEmployee.salary` | 父类叫 `salary`，子类语义是"时薪" | 改名 `rate`（或 `hourlyRate`） |

另：`BisectionMethodRevised` 没校验 `f(a) * f(b) < 0`，同号时二分法不收敛，会安静地给出错误答案——这类"输入前提"检查值得养成习惯。

关于 `static void main()`：JDK 25 起**是合法的**（JEP 512 定稿，不再需要 `--enable-preview`），所以你这些文件能跑。但要知道两点：① 方法不能是 `private`；② 需要命令行参数时仍得写 `String[] args`。教材、面试、生产代码里依然是 `public static void main(String[] args)`，别只认识一种。

---

## 六、重构示范（完整可运行，已在本机 JDK 25 编译并跑通）

**Employee.java**

```java
public abstract class Employee implements Printable {
    private final String name;
    private final double rate;          // 月薪 或 时薪，由子类语义决定

    protected Employee(String name, double rate) {
        if (rate < 0) throw new IllegalArgumentException("薪资不能为负：" + rate);
        this.name = name;
        this.rate = rate;
    }

    public final String getName() { return name; }
    protected final double getRate() { return rate; }

    public abstract double getAnnualSalary();
    public abstract String getTypeName();   // 只有子类知道自己怎么计薪

    @Override
    public void printInfo() {               // 模板方法：共同流程写在父类
        System.out.printf("%s（%s）的年薪：%.2f%n", name, getTypeName(), getAnnualSalary());
    }

    @Override
    public String toString() {
        return "%s[name=%s, %s, 年薪=%.2f]".formatted(
                getClass().getSimpleName(), name, getTypeName(), getAnnualSalary());
    }
}
```

**SalariedEmployee.java / HourlyEmployee.java**（各自只剩"差异"）

```java
public class SalariedEmployee extends Employee {
    public SalariedEmployee(String name, double monthlySalary) { super(name, monthlySalary); }
    @Override public double getAnnualSalary() { return getRate() * 12; }
    @Override public String getTypeName() { return "月薪制"; }
}

public class HourlyEmployee extends Employee {
    private final double hoursPerMonth;
    public HourlyEmployee(String name, double hourlyRate, double hoursPerMonth) {
        super(name, hourlyRate);
        this.hoursPerMonth = hoursPerMonth;
    }
    @Override public double getAnnualSalary() { return getRate() * hoursPerMonth * 12; }
    @Override public String getTypeName() { return "时薪制"; }
    public double getHoursPerMonth() { return hoursPerMonth; }
}
```

**Main.java**

```java
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Employee[] employees = {
                new SalariedEmployee("张三", 8000),
                new HourlyEmployee("李四", 50, 160),
        };

        for (Employee e : employees) {
            e.printInfo();
            System.out.println("  toString → " + e);
        }

        double total = 0;
        for (Employee e : employees) total += e.getAnnualSalary();
        System.out.printf("两人年薪合计：%.2f%n", total);

        for (Employee e : employees) {
            if (e instanceof HourlyEmployee h) {
                System.out.println(h.getName() + " 每月工时 " + h.getHoursPerMonth());
            }
        }

        System.out.println(Arrays.stream(employees).mapToDouble(Employee::getAnnualSalary).max().orElse(0));
    }
}
```

运行结果：

```
张三（月薪制）的年薪：96000.00
  toString → SalariedEmployee[name=张三, 月薪制, 年薪=96000.00]
李四（时薪制）的年薪：96000.00
  toString → HourlyEmployee[name=李四, 时薪制, 年薪=96000.00]
两人年薪合计：192000.00
李四 每月工时 160.0
96000.0
```

（张三是 8000×12，李四是 50×160×12，碰巧都是 96000——正好可以用来验证"两条不同的计算路径得到同一结果"。）

对比你原来的版本，改掉了什么：
- 字段 `private final`：封装 + 不可变，`rate` 名字不再撒谎；
- 构造器校验：负数薪资当场失败，而不是算出一个负年薪；
- `printInfo` 上提成模板方法：子类只回答"我是什么计薪方式"；
- 重写 `toString()`：`getClass().getSimpleName()` 自动带出真实类型，这就是多态；
- 数组字面量：长度不用再手数。

---

## 七、下一阶段路线图

| 阶段 | 主题 | 学完你能做什么 |
|---|---|---|
| **0（本周）** | OOP 收口：封装、模板方法、`toString`、`instanceof` | 把自己已有的代码改"干净" |
| 1 | 异常体系：checked vs unchecked、`try-with-resources`、自定义异常族 | 写健壮的输入校验与资源管理 |
| 2 | 集合与泛型：`ArrayList`、`Map`、`Set`、`泛型<T>` | 摆脱定长数组，写"装得下"的程序 |
| 3 | `String` / `StringBuilder` / 包装类 / 枚举 | 处理文本和有限状态 |
| 4 | Lambda + Stream + `Optional` | 用声明式写法替代手写循环 |
| 5 | IO 与文件（`Files`、`BufferedReader`） | 读写真实数据文件 |
| 6 | 多线程基础 / 可选 JDBC | 进阶方向，按兴趣选 |

与课堂 C 的对照：C 的指针 ≈ Java 的引用（但不能运算、不会悬空）；C 的 `struct` ≈ Java 的类（但方法在内）；C 的 `malloc/free` ≈ Java 的 `new` + GC（不需要手动释放）。这条对照能让你两边都学得更快。

---

## 八、本周任务

### 任务 A：重构 Employee 体系（必做）
按第六节把 `Employee` / `SalariedEmployee` / `HourlyEmployee` / `Main` 改完。

**验收标准**
- [ ] 子类无法直接改 `salary`/`rate`（试着在 Main 里写 `a.rate = -1`，应该编译不过）
- [ ] 两个子类里不再出现完整的 `printInfo` 实现
- [ ] `System.out.println(e)` 打印出可读信息而不是哈希码
- [ ] 新建 `Employee("张五", -100)` 时抛出 `IllegalArgumentException`

### 任务 B：修 `Student`（必做）
- 删掉 `isPassed()` 里的死分支；
- 把 `new Student(...)` 放进 `try`，让 `catch` 真的能捕获；
- `findTopStudent` 加空数组校验，并把返回类型改成 `Student`；
- 思考：`ScoreInvalidException` 继承 `RuntimeException` 和继承 `Exception`，调用方体验差在哪？

### 任务 C：加第三种员工（选做，最能验证多态）
新增 `CommissionEmployee`（底薪 + 销售额 × 提成率）。
**要求：`Main` 里的循环一个字都不改**，只往数组里加一个元素。
如果你发现必须改循环，说明多态没设计对。

### 任务 D：思考题（口头答，不用写代码）
如果把 `Printable` 改成 `abstract class Printable`，`Employee` 还能不能同时 `extends`？这说明了接口的什么价值？
