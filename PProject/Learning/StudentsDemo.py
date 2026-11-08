students = {
    "student1": {"name": "张三", "birthplace": "北京", "hobby": "篮球"},
    "student2": {"name": "李四", "birthplace": "北京", "hobby": "滑冰"},
    "student3": {"name": "王二", "birthplace": "广东", "hobby": "小提琴"}
}

for studentInfo in students.values():
    if studentInfo["birthplace"] == "北京":
        print(f"姓名：{studentInfo['name']}；爱好：{studentInfo['hobby']}")