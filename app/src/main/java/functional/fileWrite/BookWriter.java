package functional.fileWrite;

import java.io.FileWriter;
import java.io.IOException;

public class BookWriter implements AutoCloseable{
    private FileWriter writer;

    public BookWriter(String fileName) throws IOException {
        writer = new FileWriter(fileName);
    }

    public void writeChapter(String chapter) throws IOException {
        writer.write(chapter);
    }

    public void finalize() throws IOException {
        writer.close();
    }

    @Override
    public void close() throws Exception {
       writer.close(); 
    }
    
}
