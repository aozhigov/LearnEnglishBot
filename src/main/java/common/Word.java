package common;

public class Word {
    public String en;
    public String ru;
    public String example;
    public int frequency;

    public Word(int freq, String en, String ru, String example){
        frequency = freq;
        this.en = en;
        this.ru = ru;
        this.example = example;
    }
}
