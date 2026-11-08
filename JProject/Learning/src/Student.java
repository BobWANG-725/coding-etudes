public class Student {
    String name;
    int score;

    public Student(String name, int score) {
        this.name = name;
        this.score = score;
        if (score < 0 || score > 100) {
            throw new ScoreInvalidException("成绩必须在 0~100 之间");
        }
    }

    public void isPassed() {
        if (score >= 60) System.out.println(name + "的成绩情况：" + "及格");
        else if (score < 0) System.out.println(name + "的成绩情况：成绩有误，请重新输入！");
        else System.out.println(name + "的成绩情况：" + "不及格");
    }

    public static String findTopStudent(Student[] arr) {
        Student top = arr[0];    //初始化
        for (int i = 1; i < arr.length; i++) {
            if (arr[i].score > top.score) {
                top = arr[i];
            }
        }
        return top.name;
    }

    static class ScoreInvalidException extends RuntimeException {
        public ScoreInvalidException(String message) {
            super(message);
        }
    }

    static void main() {
        Student Alice = new Student("Alice", 75);
        try {
            Alice.isPassed();
            } catch (ScoreInvalidException e) {
                System.out.println(e.getMessage());
            }

        try {
            Student Bob = new Student("Bob", -10);
            Bob.isPassed();
        } catch (ScoreInvalidException e) {
            System.out.println("异常：" + e.getMessage());
        }
//        Alice.isPassed();
//        Bob.isPassed();
/////////////////////////////////////////////////////////////////
        Student[] students = new Student[3];
        String[] names = {"Alice", "Bob", "Charlie"};
        int[] scores = {75, 55, 88};
        for (int i = 0; i < students.length; i++) {
            students[i] = new Student(names[i], scores[i]);
        }
        System.out.println(findTopStudent(students));
    }
}
