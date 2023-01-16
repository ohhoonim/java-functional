package functional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import functional.syntax.Plus;

public class SyntaxTest {
    // 람다식이란?
    // 메서드를 수식으로 추상화한 것
    // (input) -> { process; return output ;}

    @Test
    public void 함수형_인터페이스의_정의와_사용법() {
        // 인터페이스 정의시 추상 메소드를 딱 1개만 가져야한다.
        // default, static은 상관 없음.
        // @FunctionalInterface 어노테이션은 옵션

        // Plus인터페이스의 int operator(int x, int y)를 수식만으로
        // 추상화를 하면, (x, y) -> x + y 와 같이 표현할 수 있다.
        // 타입은 모두 제거하고 input과 output 의 형태만 남아 있다.
        // 단지 process (로직)만 정의해주었을 뿐이다.
        Plus plus = (x, y) -> x + y;
        int result = plus.operator(2, 4);
        assertEquals(6, result);
    }

    @Test
    public void 프로세스_정의를_여러줄로_사용하려면() {
        Plus plus = (x, y) -> {
            return x + y;
        };
        int result = plus.operator(2, 4);
        assertEquals(6, result);
    }

    @Test
    public void 람다식으로_파라미터를_전달하면_효과업() {
        int result = calcurator((x, y) -> x + y, 2, 4);
        assertEquals(6, result);
    }

    private int calcurator(Plus plus, int x, int y) {
        return plus.operator(x, y);
    }
}
