package functional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.MethodSource;

import functional.entity.Employee;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StreamTest {
    @Test
    public void uuid_generate_test() {
        List<UUID> uuid = Stream.generate(() -> UUID.randomUUID())
            .peek(System.out::println)
            .limit(12).toList();
        assertEquals(12, uuid.size());
    }

    @Test
    public void peek_test() {
        Stream.of("one", "two", "three", "four")
                .filter(e -> e.length() > 3)
                .peek(e -> System.out.println("Filtered value: " + e))
                .map(String::toUpperCase)
                .peek(e -> System.out.println("Mapped value: " + e))
                .toList();
    }

    @Test
    public void nonMatch_test() {
        boolean result = Stream.of("one", "two", "three", "four")
            .noneMatch(stringNum -> stringNum.contains("x"));
        assertTrue(result);
    }

    private static Stream<Arguments> numberStrings() {
        return Stream.of(
            arguments(
                List.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"),
                10
            )
        );
    }

    @ParameterizedTest
    @MethodSource("numberStrings")
    public void isParallel_test(List<String> source, int resultSize) {
        assertFalse(source.stream().isParallel());
        assertTrue(source.parallelStream().isParallel());

        List<String> numbers = source.parallelStream()
            .map(num -> num.toUpperCase())
            .peek((num) -> { System.out.println("stream: " + Thread.currentThread().getName() + ", num=" + num); })
            .toList();
        assertEquals(10, numbers.size());

        List<String> numbersNoparallel = source.stream()
            .map(num -> num.toUpperCase())
            .peek((num) -> { System.out.println("stream2: " + Thread.currentThread().getName() + ", num=" + num); })
            .toList();
        assertEquals(10, numbersNoparallel.size());
    }

    @ParameterizedTest
    @MethodSource("numberStrings")
    public void parallel_ForkJoinPool_test(List<String> source, int resultSize) throws Exception {
        ForkJoinPool pool = new ForkJoinPool(2);
        List<String> numbers = pool.submit(() -> {
            return source.parallelStream()
                .map(num -> num.toUpperCase())
                .peek((num) -> { System.out.println("stream: " + Thread.currentThread().getName() + ", num=" + num); })
                .toList();
        })
        .get();

        assertEquals(10, numbers.size());
            
    }

    @Test
    public void flatMap_test() {
        List<Employee> emps = List.of(
            Employee.builder().name("matthew").age(17).build()
            , Employee.builder().name("alison").age(40).build()
            , Employee.builder().name("jack").age(20).build()
        );
        List<Employee> list = emps.stream().flatMap(emp -> Stream.of(emp)).toList();
        log.debug("{}", list);
    }

    @Test
    public void flatMap2_test() {
        List<List<String>> emps = List.of(
            List.of("a", "b")
            ,List.of("c", "d")
            ,List.of("f", "g")
             
        );
        List<String> list = emps.stream().flatMap(emp -> emp.stream()).toList();
        log.debug("{}", list);
    }

    @Test
    public void mapMulti_test_zeroToOne() {
        Stream.of("Java", "Valhalla", "Panama", "Loom", "Amber")
            .mapMulti( (s, mapper) -> {
                if (s.length() > 5) {
                    mapper.accept(s);
                }
            }).peek(s -> log.debug("{}", s))
            .toList();
    }

    @Test
    public void mapMulti_test_oneToOne() {
        Stream.of("Java", "Valhalla", "Panama", "Loom", "Amber")
            .mapMulti( (s, mapper) -> {
                    mapper.accept(s);
            }).peek(s -> log.debug("{}", s))
            .toList();
    }
    @Test
    public void mapMulti_test_oneToMany() {
        List<Object> list = Stream.of("Java", "Valhalla", "Panama", "Loom", "Amber")
            .mapMulti( (s, mapper) -> {
                for(int i = 0; i < s.length(); i++) {
                    mapper.accept(s.length());
                }
            })
            .toList();

        log.debug("{}", list);
    }
}
