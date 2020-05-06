package sample;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public abstract class Bot {
    private String botName;

    public Bot() {
    }

    private String userName;
    private String pathHistory; // Путь к истории сообщений
    private ArrayList<String> historyList; // Массив сообщений

    public abstract String loadName() throws FileNotFoundException;
    public abstract void addHistory(String message);
    public abstract String loadHistory();
    public abstract String say(String message);

    public ArrayList<String> getHistory() {
        return this.historyList;
    }

    public void setHistory(ArrayList<String> historyList) {
        this.historyList = historyList;
    }

    public String getBotName() {
        return botName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPathHistory() {
        return pathHistory;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPathHistory(String pathHistory) {
        this.pathHistory = pathHistory;
    }
}
