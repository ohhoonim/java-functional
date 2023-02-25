package functional.fileWrite;

import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;

public final class BookWriterEAM implements AutoCloseable {
    private FileWriter writer;

    private BookWriterEAM(String fileName) throws IOException {
        writer = new FileWriter(fileName);
    }

    public void writeChapter(String chapter) throws IOException {
        writer.write(chapter);
    }

    @Override
    public void close() throws Exception {
        System.out.println("closing....");
        writer.close();
    }

    public static void use(Consumer<BookWriterEAM> block, String bookName) {
        try (BookWriterEAM bookWriter = new BookWriterEAM("./bin/" + bookName + ".txt");) {
            block.accept(bookWriter);
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
