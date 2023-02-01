package functional.lazy.thread;

public class HolderNative {
    private Heavy heavy;

    public HolderNative() {
        System.out.println("created holder");
    }

    // synchronized를 사용해 스레드 세이프해졌지만
    // 여전히 동기화에 대한 오버헤드가 존재한다. 
    public synchronized Heavy getHeavy() {
        if(heavy == null) {
            heavy = new Heavy();
        }
        return heavy;
    }
}
