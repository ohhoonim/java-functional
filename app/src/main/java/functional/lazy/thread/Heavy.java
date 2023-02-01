package functional.lazy.thread;

public class Heavy {
    public Heavy() {
        System.out.println("created Heavy");
    }

    @Override
    public String toString() {
        return " quite heavy";
    }
}
