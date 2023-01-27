package functional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import functional.entity.Employee;

public class ComparatorTest {
    private static Stream<Arguments> sortingDataFactory() {
        return Stream.of(
                arguments(List.of("matthew", "alison", "jack", "sam", "dean"),
                        List.of("alison", "dean", "jack", "matthew", "sam"), "정렬1"),
                arguments(List.of("mon", "sum", "tue", "thu", "fri", "wed", "sat"),
                        List.of("fri", "mon", "sat", "sum", "thu", "tue", "wed"), "정렬2"));
    }
// ㅇ여기에 주석...
    @ParameterizedTest(name = "{2}")
    @MethodSource("sortingDataFactory")
    public void 정렬해보자(List<String> source, List<String> expected, String testTitle) {
        List<String> result = source.stream().sorted(String::compareTo).toList();
        assertEquals(expected, result);
    }

    private static Stream<Arguments> employeeDataFactory() {
        return Stream.of(
                arguments(List.of(
                            new Employee("matthew", "sales", 23),
                            new Employee("alison", "marketing", 43),
                            new Employee("jack", "development", 32),
                            new Employee("sam", "sales", 38),
                            new Employee("dean", "development", 23),
                            new Employee("alan", "development", 49),
                            new Employee("jupyter", "development", 23),
                            new Employee("jay", "development", 43)
                            )
                        , "23,23,23,32,38,43,43,49"
                        , "23-dean,23-jupyter,23-matthew,32-jack,38-sam,43-alison,43-jay,49-alan"
                        , "49-alan,43-alison,43-jay,38-sam,32-jack,23-dean,23-jupyter,23-matthew"
            ));
    }

    @ParameterizedTest(name="Collections.sort() 정렬")
    @MethodSource("employeeDataFactory")
    public void Collections_sort메서드를_사용하여_정렬(List<Employee> source, String expected) {
        List<Employee> tempList = new ArrayList<>();
        tempList.addAll(source); 
        // Collections.sort를 사용하면 원본 리스트가 바뀐다.
        // 리스트를 복사한 이유는 원본 리스트를 보전하기 위해서이다. (물론 jUnit에서 파라미터는 무조건 immutable이다. 변경할 수 없다.) 
        // 객체지향프로그래밍에서는 stateless 아주 중요하게 생각한다. 
        // 파라미터를 final로 유지하는 것도 그 중 한가지 방법이다. 
        // 리스트를 제대로 복사하는 것은 아주 어려운 코딩이다.
        Collections.sort(tempList, (e1, e2) -> e1.getAge() - e2.getAge());
        // Employee 클래스에 Comparable.compareTo() 을 구현하면 파라미터를 하나만 쓸수도 있다. 
        String sortedAge = tempList.stream().map(e -> String.valueOf(e.getAge())).collect(Collectors.joining(","));
        assertEquals(expected, sortedAge);
    }
    @ParameterizedTest(name="List.sort() 정렬")
    @MethodSource("employeeDataFactory")
    public void sort메서드를_사용하여_나이순으로_정렬(List<Employee> source, String expected) {
        // source.sort(null); 
        // Collections.sort() 를 사용한것과 별반 다를게 없다. 
        // 구현 생략
    }

    @ParameterizedTest(name="stream() 정렬")
    @MethodSource("employeeDataFactory")
    public void comparator를_사용하여_나이순으로_정렬(List<Employee> source, String expected) {
        String sorted = source.stream()
                .sorted((e1, e2) -> e1.getAge() - e2.getAge())
                .map(e -> String.valueOf(e.getAge()))
                .collect(Collectors.joining(","));
        // Stream.sorted를 사용하면 원본객체를 그대로 둔 상태에서 새로운 결과물을 사용할 수 있다. 
        assertEquals(expected, sorted);
    }

    @ParameterizedTest(name="메서드레퍼런스를 이용한 정렬")
    @MethodSource("employeeDataFactory")
    public void comparator_대신_메서드레퍼런스를_이용해보자(List<Employee> source, String expected) {
        String sorted = source.stream()
                .sorted(Employee::ageGap)
                .map(e -> String.valueOf(e.getAge()))
                .collect(Collectors.joining(","));
        assertEquals(expected, sorted);

        // 어떻게 된 걸까. 
        //  ==> (e1, e2) -> e1.ageGap(e2)
        // 이렇게 해석된 것이다. 정말 똑똑하다. 
        // 컴파일러는 문법에 있어서 당신보다 똑똑하다. 인정하면 편해진다.
    }
// D
    @ParameterizedTest(name="Comparator.comparing().thenComparing()을 이용한 다중 정렬")
    @MethodSource("employeeDataFactory")
    public void 다중_정렬하기(List<Employee> source, String expected, String multiExpected, String reversedExpected) {
        final Function<Employee, Integer> byAge = e -> e.getAge();
        final Function<Employee, String> byName = e -> e.getName();
        String sorted = source.stream()
                .sorted(Comparator.comparing(byAge).thenComparing(byName))
                .map(e -> e.getAge() + "-" + e.getName())
                .collect(Collectors.joining(","));
        assertEquals(multiExpected, sorted);

        Stream.of(sorted.split(",")).forEach(System.out::println);
        
        String reversedSorted = source.stream()
                .sorted(Comparator.comparing(byAge).reversed().thenComparing(byName))
                .map(e -> e.getAge() + "-" + e.getName())
                .collect(Collectors.joining(","));
        assertEquals(reversedExpected, reversedSorted);

        Stream.of(reversedSorted.split(",")).forEach(System.out::println);
    }



}
