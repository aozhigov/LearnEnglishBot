package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CsvFile {
    private final BufferedReader bufferFile;
    private final String separator;
    Pattern pattern = Pattern.compile("[a-zA-Z]+");
    Matcher matcher;
    private Hashtable<String, Integer> frequencyTable;

    public CsvFile(String path, String separator) throws IOException {
        this.separator = separator;
        bufferFile = new BufferedReader(new FileReader(new File(path)));
        prepareTable();
    }

    private List<String> nextLine() throws IOException {
        String line = bufferFile.readLine();

        if (line == null)
            bufferFile.close();

        return line != null
                ? Arrays.asList(line.split(separator))
                : null;
    }

    public void prepareTable() throws IOException {
        List<String> line = nextLine();

        Hashtable<String, Integer> result = new Hashtable<>();
        while (line != null) {
            result.put(line.get(1),
                    Integer.parseInt(line.get(2)
                            .substring(0, line.get(2).length() - 1)));

            line = nextLine();
        }

        frequencyTable = result;
    }

    public ArrayList<String> search(String text) {
        HashSet<String> hashsetWords = new HashSet<>();
        ArrayList<String> resultWords = new ArrayList<>();
        ArrayList<Tuple<String, Integer>> words = new ArrayList<>();

        text = text.toLowerCase();
        matcher = pattern.matcher(text);

        while (matcher.find()) {
            String findStr = matcher.group();
            words.add(new Tuple<>(findStr,
                    frequencyTable.getOrDefault(findStr, 1)));
        }

        words.sort(Comparator.comparingInt(Tuple::getValue));

        for (Tuple<String, Integer> word : words)
            if (!hashsetWords.contains(word.getKey())) {
                hashsetWords.add(word.getKey());
                resultWords.add(word.getKey());
            }

        return resultWords;
    }
}
