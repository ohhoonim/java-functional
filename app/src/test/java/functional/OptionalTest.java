package functional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import functional.entity.Employee;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OptionalTest {

    private static Stream<Arguments> nameFactory() {
        return Stream.of(
            Arguments.arguments(null, ""), 
            Arguments.arguments("matthew", "MATTHEW")
        );
    }

    @ParameterizedTest
    @MethodSource("nameFactory")
    public void null_체크하는_전통적인방법(String name, String expected) {
        String upperName = "";
        if (name != null) {
            upperName = name.toUpperCase();
        }
        assertEquals(expected, upperName);
    }

    @ParameterizedTest
    @MethodSource("nameFactory")
    public void optional로_null체크하기(String name, String expected) {
        String upperName = Optional.ofNullable(name)
            .map(mapper -> mapper.toUpperCase())
            .orElse("");
        assertEquals(expected, upperName);
        // Optional은 null체크를 하려고 만든 클래스가 아니다. 
        // 결과가 없음을 명확히 하고자 만들어졌다. 
        // Optional is primarily intended for use as a method 
        // return type where there is a clear need to represent "no result," 
        // and where using null is likely to cause errors.
    }

    private static Stream<Arguments> listFactory() {
        List<Employee> employees =
                List.of(Employee.builder().name("jack").department("sales").build(),
                        Employee.builder().name("sam").department("sales").build(),
                        Employee.builder().name("dean").department("development").build(),
                        Employee.builder().name("mary").department("sales").build(),
                        Employee.builder().name("simon").department("development").build());
        return Stream.of(
                Arguments.arguments(employees, "jack", "sales"), 
                Arguments.arguments(employees, "dean", "development"),
                Arguments.arguments(employees, "__blank__", "ceo")
            );
    }

    @ParameterizedTest
    @MethodSource("listFactory")
    public void 부서별로조회해서_목록의_첫번째직원정보를_가져오자(List<Employee> emps, String expected, String dept) {
       String empName =  emps.stream()
                    .filter(emp -> emp.getDepartment().equals(dept))
                    .peek((emp) -> log.debug("{}", emp))
                    .findFirst().map(emp -> emp.getName()).orElse("__blank__");
        assertEquals(expected, empName);
    }


    @ParameterizedTest
    @MethodSource("listFactory")
    public void optional의_올바른사용법(List<Employee> emps, String expected, String dept) {
        Optional<Employee> emp = findFirstEmployeeByDepartment(emps, dept);
        if (emp.isEmpty()) {
            assertEquals(expected, "__blank__");
        } else {
            assertEquals(expected, emp.get().getName());
        }
    }

    private Optional<Employee> findFirstEmployeeByDepartment(final List<Employee> emps, final String dept) {
        List<Employee> empByDept = new ArrayList<>();
        for (Employee emp: emps) {
            if (emp.getDepartment().equals(dept)) {
                empByDept.add(emp);
            }
        }
        return empByDept.size() == 0 ? Optional.empty(): Optional.of(empByDept.get(0));
    }
}
