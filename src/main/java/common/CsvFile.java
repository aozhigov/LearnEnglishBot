package common;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CsvFile {
    private String path;
    private final BufferedReader bufferFile;
    private final String separator;
    Pattern pattern;
    Matcher matcher;
    private Hashtable<String, Integer> frequencyTable;

    public CsvFile(String path, String separator) throws IOException {
        this.path = System.getProperty("user.dir") + path;
        this.separator = separator;
        bufferFile = new BufferedReader(new FileReader(new File(this.path)));
        prepeareTable();
    }

    private List<String> nextLine() throws IOException {
        String line = bufferFile.readLine();

        if (line == null)
            bufferFile.close();

        return line != null
                ? Arrays.asList(line.split(separator))
                : null;
    }

    public void prepeareTable() throws IOException {
        List<String> line = nextLine();

        Hashtable<String, Integer> result = new Hashtable<>();
        while (line != null) {
                result.put(line.get(1),
                        Integer.parseInt(line.get(2)));

            line = nextLine();
        }

        frequencyTable = result;
    }

    public ArrayList<String> search(String text){
        text = text.toLowerCase();
        pattern = Pattern.compile("[a-zA-Z]+");
        matcher = pattern.matcher(text);
        ArrayList<Tuple<String, Integer>> result = new ArrayList<>();

        while (matcher.find()){
            String findStr = matcher.group();
            result.add(new Tuple<>(findStr, frequencyTable.getOrDefault(findStr, 1)));
        }

        result.sort((o1, o2) -> o2.getValue() - o1.getValue());

        ArrayList<String> result1 = new ArrayList<>();
        for (Tuple<String, Integer> word: result)
            result1.add(word.getKey());

        return result1;
    }
}
