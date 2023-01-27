package functional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("컬렉션과 스트림")
public class CollectionTest {

    private static Stream<Arguments> dataListFactory() {
        return Stream.of(arguments(List.of("matthew", "alison", "jack", "sam", "dean"),
                List.of("MATTHEW", "ALISON", "JACK", "SAM", "DEAN"), "대문자로 바꾸기"));
    }
    // jUnit의 파라미터 테스트 방법은 '팩토리메서드 패턴' 영상을 참고 하세요.

    @ParameterizedTest(name = "{2}")
    @MethodSource("dataListFactory")
    public void 컬렉션을_이터레이트하는_기존의_방법(List<String> source, List<String> expected, String testTitle) {
        List<String> upperNames = new ArrayList<>();
        for (String name : source) {
            upperNames.add(name.toUpperCase());
        }
        assertEquals(expected, upperNames);
    }
    // 일반적인 for문을 사용할 경우 결과를 만들어내야하는 부수적인 변수선언이
    // 불가피하다. 이는 프로그램 수정을 어렵게하고, 버그를 양산시키는 요인이 된다.

    @ParameterizedTest(name = "{2}")
    @MethodSource("dataListFactory")
    public void 컬렉션의_foreach메서드를_사용하는방법(List<String> source, List<String> expected,
            String testTitle) {
        List<String> upperNames = new ArrayList<>();
        source.forEach(name -> upperNames.add(name.toUpperCase()));
        assertEquals(expected, upperNames);
    }
    // foreach를 사용하더라도 List에 대한 변수 선언은 필요하다.

    @ParameterizedTest(name = "{2}")
    @MethodSource("dataListFactory")
    public void 컬렉션의_stream을_사용해보자(List<String> source, List<String> expected, String testTitle) {
        List<String> upperNames = source.stream().map(name -> name.toUpperCase())
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        // collect에 대한 내용은 Collectors를 참고하세요
        // 메서드 레퍼런스 "::" 기호 두 개를 사용한다. 파라미터가 0개 또는 1개인 메서드를 호출할 때 사용할 수 있다. 
        assertEquals(expected, upperNames);
    }
    // stream은 컬렉션에서 람다식을 사용하는 기술이다. 따라서 stream을 사용하면 
    // 서술적으로 프로그래밍을 할 수 있다. 코드를 있는 그대로 읽어보면,
    // source 리스트를 데이터흐름(stream)에 따라 하나씩 매핑(map)하되 대문자로 만들고, 
    // 리스트로 수집(collect) 한 후 upperNames 리스트 변수에 담아라.  

    private static Stream<Arguments> filterDataFactory() {
        return Stream.of(
                arguments(List.of("matthew", "alison", "jack", "sam", "dean"),
                        List.of("matthew", "alison", "jack", "sam", "dean"), "a"),
                arguments(List.of("matthew", "alison", "jack", "sam", "dean"),
                        List.of("alison", "sam"), "s"),
                arguments(List.of("matthew", "alison", "jack", "sam", "dean"),
                        List.of("matthew", "sam"), "m"));
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("filterDataFactory")
    public void 걸러내보자_필터(List<String> source, List<String> expected, String filterString) {
        List<String> filterNames = source.stream().filter(name -> name.contains(filterString))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        assertEquals(expected, filterNames);
    }

    private static Stream<Arguments> reduceDataFactory() {
        return Stream.of(
                arguments(List.of("matthew", "alison", "jack", "sam", "dean"),
                        ",matthew,alison,jack,sam,dean", ","),
                arguments(List.of("matthew", "alison", "jack", "sam", "dean"),
                        "-matthew-alison-jack-sam-dean", "-"));
    }

    @ParameterizedTest(name = "separator : {2}")
    @MethodSource("reduceDataFactory")
    public void 데이터를_하나로_합쳐보자(List<String> source, String expected, String separator) {
        String reduceNames = source.stream().reduce("", (acc, next) -> {
            return acc + separator + next;
        });
        assertEquals(expected, reduceNames);
    }

    @Test
    public void 합계를_구해보자() {
        List<Integer> scores = List.of(89, 78, 37, 98, 100, 63, 78);
        int sum = scores.stream().reduce((a, b) -> a + b).get(); 
        assertEquals(543, sum);
        // 평균은? ㅋㅋ
        double avg = sum / scores.size();
        assertEquals(77, avg);
    }
    // 렉시컬 스코프와 클로저에 대한 내용은 자바스크립트 '클로저' 영상 참조
    // 자바스크립트 '리듀스' 영상보면 더 도움 됩니다.




}
