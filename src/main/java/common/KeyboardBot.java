package common;

import java.util.List;

public class KeyboardBot {
    private Integer countColumn;
    private final List<String> buttons;
    private int maxLenButton;

    public KeyboardBot(List<String> buttons){
        this.countColumn = (buttons.size() / 2) + (buttons.size() % 2);
        this.buttons = buttons;
        maxLenButton = Integer.MIN_VALUE;
    }

    public KeyboardBot(){
        this.countColumn = null;
        this.buttons = null;
        maxLenButton = Integer.MIN_VALUE;
    }

    private void getMax(){
        for (String button: buttons)
            if (maxLenButton < button.length())
                maxLenButton = button.length();
    }

    public Integer getCountColumn(){
        return countColumn;
    }

    public List<String> getButtons() {
        return buttons;
    }

    public void addButtons(String button){
        if (maxLenButton < button.length())
            maxLenButton = button.length();

        buttons.add(button);

        countColumn = maxLenButton > 6
                ? buttons.size() / 2
                : buttons.size() / 3;
    }
}
