package functional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import java.awt.Color;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import functional.filter.Camera;

@DisplayName("데코레이터패턴구현 or 람다식체인")
public class DecoratorTest {

    @Test
    public void Function_함수형인터페이스의_compose알아보기() {
        Function<String, String> stepOne = one -> "one:" + one;
        Function<String, String> stepTwo = two -> "two:" + two;

        String result = stepOne.compose(stepTwo).apply("matthew");
        assertEquals("one:two:matthew", result);

        Function<String, String> filters = Stream.of(stepOne, stepTwo)
                .reduce((filter, next) -> filter.compose(next))  
                .orElse(filter -> filter);
                ;
        assertEquals("one:two:matthew", filters.apply("matthew"));
    }

    @ParameterizedTest(name="filterd: {0}")
    @MethodSource("filterDataFactory")
    public void 카메라에_여러개의_필터적용하기(Color expectedColor, List<Function<Color, Color>> filters) {
        Camera sonyA7R5 = new Camera();
        sonyA7R5.setFilters(filters);
        Color capturedColor = sonyA7R5.capture(new Color(10,12,14));

        assertEquals(expectedColor, capturedColor);
    }

    private static Stream<Arguments> filterDataFactory() {
        Function<Color, Color> brighter = color -> color.brighter();
        Function<Color, Color> darker = color -> color.darker();
        return Stream.of(
            arguments(
                /* expected */ new Color(14, 15, 17),
                /* filters */ List.of(brighter, brighter, darker) 
            ),
            arguments(
                /* expected */ new Color(10, 11, 12),
                /* filters */ List.of(brighter,  darker) 
            )
        );
    }

}













