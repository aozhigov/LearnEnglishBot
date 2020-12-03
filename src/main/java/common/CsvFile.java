package common;

import vocabulary.Word;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CsvFile {
    private final BufferedReader bufferFile;
    private final String separator;
    Pattern pattern;
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
                    Integer.parseInt(line.get(2).substring(0, line.get(2).length() - 1)));

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

        result.sort(Comparator.comparingInt(Tuple::getValue));

        HashSet<String> result1 = new HashSet<>();
        ArrayList<String> res2 = new ArrayList<>();
        for (Tuple<String, Integer> word: result)
            if (!result1.contains(word.getKey())){
                result1.add(word.getKey());
                res2.add(word.getKey());
            }


        return res2;
    }
}
