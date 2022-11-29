package bg.sofia.uni.fmi.mjt.markdown;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.Files.delete;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MarkdownConverterTest {
    private static final MarkdownConverter DEFAULT_CONVERTER = new MarkdownConverter();
    private static final String DEFAULT_BEGINNING = "<html>" + System.lineSeparator() + "<body>" + System.lineSeparator();
    private static final String DEFAULT_ENDING = System.lineSeparator() + "</body>" + System.lineSeparator() + "</html>" + System.lineSeparator();
    private  Writer DEFAULT_WRITER;
    private static Path from;
    private static Path to;

    @BeforeAll
    static void initPaths() throws IOException{
        File fromFile = new File("." +File.separator + "First.md");
        from = fromFile.toPath();
        File toFile = new File("." +File.separator + "Second.html");
        to = toFile.toPath();
        try (var bufferedWriter = Files.newBufferedWriter(from)) {
            bufferedWriter.write("###### Heading6" + System.lineSeparator() + "`.close()` *your* **eyes**");
        }
    }

    @AfterAll
    static void destructPaths() throws IOException {
        delete(from);
        delete(to);
    }

    @BeforeEach
    void init() {
        DEFAULT_WRITER = new StringWriter();
    }
    @AfterEach
    void cleanUp() throws IOException {
        DEFAULT_WRITER.close();
    }

    @Test
    void testConvertMarkdownStreamsStrong() throws IOException {
        try (Reader rd = new StringReader("AB**C**")) {
            DEFAULT_CONVERTER.convertMarkdown(rd, DEFAULT_WRITER);

            assertEquals(DEFAULT_BEGINNING + "AB<strong>C</strong>" + DEFAULT_ENDING, DEFAULT_WRITER.toString(),
                    "Expected AB<strong>C</strong> when line AB**C** is added");
        }

    }

    @Test
    void testConvertMarkdownStreamsItalic() throws IOException {
        try (Reader rd = new StringReader("AB*C*")) {

            DEFAULT_CONVERTER.convertMarkdown(rd, DEFAULT_WRITER);

            assertEquals(DEFAULT_BEGINNING + "AB<em>C</em>" + DEFAULT_ENDING, DEFAULT_WRITER.toString(),
                    "Expected AB<em>C</em> when line AB*C* is added");
        }
    }

    @Test
    void testConvertMarkdownStreamsHeading1() throws IOException {
        try (Reader rd = new StringReader("# Heading1")) {
            DEFAULT_CONVERTER.convertMarkdown(rd, DEFAULT_WRITER);

            assertEquals(DEFAULT_BEGINNING + "<h1>Heading1</h1>" + DEFAULT_ENDING, DEFAULT_WRITER.toString(),
                    "Expected <h1>Heading1</h1> when line # Heading1 is added");
        }
    }

    @Test
    void testConvertMarkdownStreamsHeading2() throws IOException {
        try (Reader rd = new StringReader("## Heading2")) {
            DEFAULT_CONVERTER.convertMarkdown(rd, DEFAULT_WRITER);

            assertEquals(DEFAULT_BEGINNING + "<h2>Heading2</h2>" + DEFAULT_ENDING, DEFAULT_WRITER.toString(),
                    "Expected <h2>Heading2</h2> when line ## Heading2 is added");
        }
    }

    @Test
    void testConvertMarkdownStreamsHeading3() throws IOException {
        try (Reader rd = new StringReader("### Heading3")) {
            DEFAULT_CONVERTER.convertMarkdown(rd, DEFAULT_WRITER);

            assertEquals(DEFAULT_BEGINNING + "<h3>Heading3</h3>" + DEFAULT_ENDING, DEFAULT_WRITER.toString(),
                    "Expected <h3>Heading3</h3> when line ### Heading3 is added");
        }
    }

    @Test
    void testConvertMarkdownStreamsHeading4() throws IOException {
        try (Reader rd = new StringReader("#### Heading4")) {
            DEFAULT_CONVERTER.convertMarkdown(rd, DEFAULT_WRITER);

            assertEquals(DEFAULT_BEGINNING + "<h4>Heading4</h4>" + DEFAULT_ENDING, DEFAULT_WRITER.toString(),
                    "Expected <h4>Heading4</h4> when line #### Heading4is added");
        }
    }

    @Test
    void testConvertMarkdownStreamsHeading5() throws IOException {
        try (Reader rd = new StringReader("##### Heading5")) {
            DEFAULT_CONVERTER.convertMarkdown(rd, DEFAULT_WRITER);

            assertEquals(DEFAULT_BEGINNING + "<h5>Heading5</h5>" + DEFAULT_ENDING, DEFAULT_WRITER.toString(),
                    "Expected <h5>Heading5</h5> when line ##### Heading5 is added");
        }
    }

    @Test
    void testConvertMarkdownStreamsHeading6() throws IOException {
        try (Reader rd = new StringReader("###### Heading6")) {
            DEFAULT_CONVERTER.convertMarkdown(rd, DEFAULT_WRITER);

            assertEquals(DEFAULT_BEGINNING + "<h6>Heading6</h6>" + DEFAULT_ENDING, DEFAULT_WRITER.toString(),
                    "Expected <h6>Heading6</h6> when line ###### Heading6 is added");
        }
    }

    @Test
    void testConvertMarkdownStreamsHeading6CodeStrongItalic() throws IOException {
        try (Reader rd = new StringReader("###### Heading6" + System.lineSeparator() + "`.close()` *your* **eyes**")) {
            DEFAULT_CONVERTER.convertMarkdown(rd, DEFAULT_WRITER);

            assertEquals(DEFAULT_BEGINNING + "<h6>Heading6</h6>" + System.lineSeparator()
                            + "<code>.close()</code> <em>your</em> <strong>eyes</strong>" + DEFAULT_ENDING , DEFAULT_WRITER.toString(),
                    "Expected <h6>Heading6</h6><code>.close()</code> <em>your</em> <strong>eyes</strong> when " +
                            "line ###### Heading6" + System.lineSeparator() + "`.close()` *your* **eyes** is added");
        }
    }

    @Test
    void testConvertMarkdownStreamsWithClosedReader() throws IOException{
        try (Reader rd = new StringReader("###### Heading6" + System.lineSeparator() + "`.close()` *your* **eyes**");
            Writer wr2 = new StringWriter()) {
            rd.close();
            assertThrows(IllegalStateException.class,() -> DEFAULT_CONVERTER.convertMarkdown(rd, wr2)
                    ,"IllegalStateException was expected when Reader arg is closed");
        }


    }

    @Test
    void testConvertMarkdownStreamsWithNullWriter() throws IOException{
        try (Reader rd = new StringReader("###### Heading6" + System.lineSeparator() + "`.close()` *your* **eyes**")) {

            assertThrows(IllegalArgumentException.class,() -> DEFAULT_CONVERTER.convertMarkdown(rd, null)
                    ,"IllegalStateException was expected when Writer arg is null");
        }

    }

    @Test
    void testConvertMarkdownStreamsWithNullReader() {

        assertThrows(IllegalArgumentException.class,() -> DEFAULT_CONVERTER.convertMarkdown(null, DEFAULT_WRITER)
                ,"IllegalStateException was expected when Reader arg is null");


    }

    @Test
    void testConvertMarkdownPathsWithNullFrom() {

        assertThrows(IllegalArgumentException.class,() -> DEFAULT_CONVERTER.convertMarkdown(null, Path.of(new File(".").getCanonicalPath()))
                ,"IllegalStateException was expected when Reader arg is null");


    }
    @Test
    void testConvertMarkdownPathsWithNullTo() {

        assertThrows(IllegalArgumentException.class,() -> DEFAULT_CONVERTER.convertMarkdown(Path.of(new File(".").getCanonicalPath()),null)
                ,"IllegalStateException was expected when Reader arg is null");
    }

    @Test
    void testConvertMarkdownPathsWithHeading6CodeStrongItalic() throws IOException{
        DEFAULT_CONVERTER.convertMarkdown(from, to);
        String test = Files.readString(to);
        assertEquals(DEFAULT_BEGINNING + "<h6>Heading6</h6>" + System.lineSeparator()
                        + "<code>.close()</code> <em>your</em> <strong>eyes</strong>" + DEFAULT_ENDING,test,
                    "html file output unexpected");
    }
    @Test
    void testConvertAllMarkdownFilesWithHeading6CodeStrongItalic() throws IOException{
        DEFAULT_CONVERTER.convertAllMarkdownFiles(from.getParent(), from.getParent());
        String test = Files.readString(to);
        assertEquals(DEFAULT_BEGINNING + "<h6>Heading6</h6>" + System.lineSeparator()
                        + "<code>.close()</code> <em>your</em> <strong>eyes</strong>" + DEFAULT_ENDING,test,
                "html file output unexpected");
        String pathTemp = from.getFileName().toString().replace(".md","") + ".html";
        Path path = Path.of(pathTemp);
        delete(path);
    }
}
