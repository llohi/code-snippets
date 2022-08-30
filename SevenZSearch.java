import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class illustrates how to handle files within 7zip file.
 * @author J.L.
 */
public class SevenZSearch {

    /**
     * Loop over a 7zip file given by argument and search for the given word.
     * @param args The given commandline arguments. First word is the search word. Second is the filename.
     * @throws IOException if file not found.
     */
    public static void main(String[] args)
            throws IOException {

        try (SevenZFile input = new SevenZFile(new File(args[0]))) {

            SevenZArchiveEntry entry;
            String searchWord = args[1].toUpperCase();

            while ((entry = input.getNextEntry()) != null) {
                String name = entry.getName();
                if (!name.endsWith(".txt")) {
                    continue;
                }
                System.out.println(name);
                Scanner scanner = new Scanner(input.getInputStream(entry));
                int n = 1;
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    Matcher matcher = Pattern
                            .compile(searchWord, Pattern.CASE_INSENSITIVE)
                            .matcher(line);

                    if (matcher.find()) {
                        System.out.format("%d: %s\n", n, matcher.replaceAll(searchWord));
                    }
                    n++;
                }
                System.out.println();
            }
        }
    }

}
