package bg.sofia.uni.fmi.mjt.markdown;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class MarkdownConverter implements MarkdownConverterAPI {

    @Override
    public void convertMarkdown(Reader source, Writer output) {
        if (source == null || output == null) {
            throw new IllegalArgumentException("A problem occurred while accessing streams check if null");
        }

        try (var bufferedReader = new BufferedReader(source);
             var bufferedWriter = new BufferedWriter(output)) {
            convertToHTML(bufferedReader, bufferedWriter);
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while reading from Reader source", e);
        }
    }

    @Override
    public void convertMarkdown(Path from, Path to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("A problem occurred while accessing streams check if null");
        }

        try (var bufferedReader = Files.newBufferedReader(from);
             var bufferedWriter = Files.newBufferedWriter(to)) {
            convertToHTML(bufferedReader, bufferedWriter);
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while reading from file", e);
        }
    }

    @Override
    public void convertAllMarkdownFiles(Path sourceDir, Path targetDir) {
        String filename;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(sourceDir, "*.md")) {
            for (Path file: stream) {
                filename = String.valueOf(file.getFileName());
                filename = filename.replace(".md", "");
                File to = new File(targetDir.toString() + File.separator + filename + ".html");
                convertMarkdown(file, to.toPath());
            }
        } catch (IOException e) {
            throw new IllegalStateException("File directory/File error that can't be handled ", e);
        }
    }

    private void convertToHTML(BufferedReader bufferedReader, BufferedWriter bufferedWriter) throws IOException {
        String line;
        int lineCount = 0;
        bufferedWriter.write("<html>" + System.lineSeparator() + "<body>" + System.lineSeparator());
        while ((line = bufferedReader.readLine()) != null) {
            if (lineCount > 0) {
                bufferedWriter.write(System.lineSeparator());
            }
            bufferedWriter.write(convertLineToHTML(line));
            lineCount++;
        }
        bufferedWriter.write(System.lineSeparator() + "</body>" + System.lineSeparator() +
                "</html>" + System.lineSeparator());
        bufferedWriter.flush();
    }

    private String convertLineToHTML(String line) {
        int counter = 0;
        if ((counter = checkHeading(line)) != 0) {
            line = line.replaceFirst("#{1,6} ", "<h" + counter + ">");
            line += ("</h" + counter + ">");
        }

        line = line.replaceFirst("`", "<code>");
        line = line.replaceFirst("`", "</code>");

        line = line.replaceFirst("\\*\\*", "<strong>");
        line = line.replaceFirst("\\*\\*", "</strong>");

        line = line.replaceFirst("\\*", "<em>");
        line = line.replaceFirst("\\*", "</em>");

        return line;
    }
    private int checkHeading(String line) {
        int counter = 0;
        for (char ch : line.toCharArray()) {
            if (ch != '#') {
                break;
            }
            counter++;

        }
        return counter;
    }
}
