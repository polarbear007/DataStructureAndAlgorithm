package _04.cn.itcast.linkedList;

public class Student implements Comparable<Student>{
	private Integer id;
	private String name;
	private Integer age;

	public Student(Integer id, String name, Integer age) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
	}

	public Student() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", age=" + age + "]";
	}

	@Override
	public int compareTo(Student stu) {
		int num =  this.id - stu.id;
		int num2 = num == 0 ? this.age - stu.age : num;
		int num3 = num2 == 0? this.name.compareTo(stu.name) : num2;
		return num3;
	}

}
