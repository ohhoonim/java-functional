package functional;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import functional.entity.Employee;

public class CollectorsTest {

    private static Stream<Arguments> dataListFactory() {
        return Stream.of(arguments(List.of("matthew", "alison", "jack", "sam", "dean"),
                List.of("MATTHEW", "ALISON", "JACK", "SAM", "DEAN"), "대문자로 바꾸기"));
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("dataListFactory")
    public void 최종결과물을_컬렉션으로_만들어보자(List<String> source, List<String> expected, String testTitle) {
        List<String> upperNames = source.stream().map(String::toUpperCase).toList();
        // toList() 메서드는 jdk 16이상부터 사용가능하다.
        assertEquals(expected, upperNames);
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("dataListFactory")
    public void collect_메서드는_최종결과물을_컬렉션으로_만들때_사용한다(List<String> source, List<String> expected,
            String testTitle) {
        List<String> upperNames = source.stream().map(String::toUpperCase).collect(toList());
        // collect() 메서드는 Collect 인터페이스를 인자로 받는다.
        // Collect 인터페이스를 구현한 java.util.stream.Collectors 클래스에는
        // 구현되어있다.
        assertEquals(expected, upperNames);
    }

    private static Stream<Arguments> joiningDataFactory() {
        return Stream.of(arguments(List.of("matthew", "alison", "jack", "sam", "dean"),
                "MATTHEW,ALISON,JACK,SAM,DEAN", "대문자로 바꾸고 join하기"));
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("joiningDataFactory")
    public void 쉼표로_join해보자(List<String> source, String expected, String testTitle) {
        String result = source.stream().map(String::toUpperCase).collect(joining(","));
        assertEquals(expected, result);
    }

    private static Stream<Arguments> setDataFactory() {
        return Stream.of(
                arguments(List.of("matthew", "alison", "jack", "sam", "dean"), 5, "set으로 만들기1"),
                arguments(List.of("matthew", "alison", "matthew", "dean", "dean"), 3,
                        "set으로 만들기2"));
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("setDataFactory")
    public void set으로_묶어보자(List<String> source, int expected, String testTitle) {
        Set<String> result = source.stream().map(String::toUpperCase).collect(toSet());
        assertEquals(expected, result.size());
    }

    private static Stream<Arguments> employeeDataFactory() {
        List<Employee> employees =
                List.of(Employee.builder().name("jack").department("sales").build(),
                        Employee.builder().name("sam").department("sales").build(),
                        Employee.builder().name("dean").department("development").build(),
                        Employee.builder().name("mary").department("sales").build(),
                        Employee.builder().name("simon").department("development").build());
        return Stream.of(arguments(employees, 3, "sales"), 
                        arguments(employees, 2, "development"));
    }

    @ParameterizedTest(name = "{2} 부서의 사원수 : {1}")
    @MethodSource("employeeDataFactory")
    public void sql의_groupby를_구현해볼까(List<Employee> employees, int countOfDepartment,
            String department) {
        Map<String, Long> totalByDept = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()));
        assertEquals(countOfDepartment, totalByDept.get(department));
    }
}
