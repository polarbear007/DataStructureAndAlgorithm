package _10.cn.itcast.tree;

public class Teacher implements Comparable<Teacher> {
	private Integer id;
	private String name;
	private Integer age;

	public Teacher(Integer id, String name, Integer age) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
	}

	public Teacher() {
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
		return "Teacher [id=" + id + ", name=" + name + ", age=" + age + "]";
	}

	@Override
	public int compareTo(Teacher t) {
		return this.getId() - t.getId();
	}

}
