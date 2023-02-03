package functional.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Employee {
    private String name;
    private String department; 
    private int age;

    public int ageGap(Employee other) {
        return this.age - other.getAge();
    }

    @Override
    public String toString() {
        return "Employee [name=" + name + ", department=" + department + ", age=" + age + "]";
    }

}
