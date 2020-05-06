package sample;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parser.MatchParser;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;


public class ChatBot extends Bot {

    private Random random;
    private Pattern pattern;
    private boolean stupidAnswer;
    private boolean saveFlag;

    public boolean isStupidAnswer() {
        return stupidAnswer;
    }

    public void setStupidAnswer(boolean stupidAnswer) {
        this.stupidAnswer = stupidAnswer;
    }

    final private String[] simplePhrases = {
            "Нет ничего ценнее слов, сказанных к месту и ко времени.",
            "Порой молчание может сказать больше, нежели уйма слов.",
            "Перед тем как писать/говорить всегда лучше подумать.",
            "Вежливая и грамотная речь говорит о величии души.",
            "Приятно когда текст без орфографических ошибок.",
            "Многословие есть признак неупорядоченного ума.",
            "Слова могут ранить, но могут и исцелять.",
            "Записывая слова, мы удваиваем их силу.",
            "Кто ясно мыслит, тот ясно излагает.",
            "Боюсь Вы что-то не договариваете."
    };

    final private String[] illusionAnswer = {
            "Вопрос непростой, прошу тайм-аут на раздумья.",
            "Не уверен, что располагаю такой информацией.",
            "Может лучше поговорим о чём-то другом?",
            "Простите, но это очень личный вопрос.",
            "Не уверен, что Вам понравится ответ.",
            "Поверьте, я сам хотел бы это знать.",
            "Вы действительно хотите это знать?",
            "Уверен, Вы уже догадались сами.",
            "Зачем Вам такая информация?",
            "Давайте сохраним интригу?"
    };

    final private Map<String, String> keys = new HashMap<String, String>() {{
        // hello
        put("хай", "hello");
        put("привет", "hello");
        put("здорово", "hello");
        put("здравствуй", "hello");
        // who
        put("кто\\s.*ты", "who");
        put("ты\\s.*кто", "who");
        // name
        put("как\\s.*зовут", "name");
        put("как\\s.*имя", "name");
        put("есть\\s.*имя", "name");
        put("какое\\s.*имя", "name");
        // howareyou
        put("как\\s.*дела", "howareyou");
        put("как\\s.*жизнь", "howareyou");
        // whatdoyoudoing
        put("зачем\\s.*тут", "whatdoyoudoing");
        put("зачем\\s.*здесь", "whatdoyoudoing");
        put("что\\s.*делаешь", "whatdoyoudoing");
        put("чем\\s.*занимаешься", "whatdoyoudoing");
        // whatdoyoulike
        put("что\\s.*нравится", "whatdoyoulike");
        put("что\\s.*любишь", "whatdoyoulike");
        // iamfeelling
        put("кажется", "iamfeelling");
        put("чувствую", "iamfeelling");
        put("испытываю", "iamfeelling");
        // yes
        put("^да", "yes");
        put("согласен", "yes");
        // whattime
        put("который\\s.*час", "whattime");
        put("сколько\\s.*время", "whattime");
        // bye
        put("пока","bye");
        put("прощай", "bye");
        put("увидимся", "bye");
        put("до\\s.*свидания", "bye");
        // math parser
        put("посчитай\\s?\\d+\\s?","parser");
        // info
        put("/info","information");
        put("/saveon","saveon");
        put("/saveoff","saveoff");
        put("/fileclean","fileclean");
        put("/onstupidbot","onstupidbot");
        put("/offstupidbot","offstupidbot");
        put("/dollar","coursedollar");
        put("/euro","courseeuro");
    }};

    final Map<String, String> answersByKeys = new HashMap<String, String>() {{
        put("hello", "Здравствуйте, рад Вас видеть.");
        put("who", "Я обычный чат-бот.");
        put("name", "Меня зовут Боб.");
        put("howareyou", "Спасибо, что интересуетесь. У меня всё хорошо.");
        put("whatdoyoudoing", "Я пробую общаться с людьми.");
        put("whatdoyoulike", "Мне нравиться думать что я не просто программа.");
        put("iamfeelling", "Как давно это началось? Расскажите чуть подробнее.");
        put("yes", "Согласие есть продукт при полном непротивлении сторон.");
        put("bye", "До свидания. Надеюсь, ещё увидимся.");
        put("information","\nСписок команд : \n" + "/info - информация о всех командах.\n" +
                "/saveon - включить сохранение диалога.\n" + "/saveoff - отключить сохранение диалога.\n" +
                 "/fileclean - очищение текстового файла.\n" + "/onstupidbot - включает глупого бота.\n" +
                "/offstupidbot - выключает глупого бота.\n" + "/loaddialog - загрузить диалог из файла.\n" + "/dollar - курс доллара.\n" + "/euro - курс евро.\n");
        put("saveon","Сохранение диалога включено.");
        put("saveoff","Сохранение диалога выключено.");
        put("fileclean","Текстовый файл очищен.");
        put("onstupidbot","Глупый бот включен.");
        put("offstupidbot","Глупый бот выключен.");
        try {
            put("coursedollar",readCourse(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            put("courseeuro",readCourse(1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }};

    public ChatBot() {
        super();
        random = new Random();
        stupidAnswer = false;
    }

    @Override
    public String say(String message) {
        if(stupidAnswer) {
            String say = message.trim().endsWith("?")?illusionAnswer[random.nextInt(illusionAnswer.length)]:simplePhrases[random.nextInt(simplePhrases.length)];
            return say;
        }
        /*Split("[ {,|.}?]+")) - съедаем все знаки препинания и т.п.
        * toLowerCase - опускаем все в нижний регистр для удобства работы
        * join - опять объединяем в*/
        else {
            String temp = String.join(" ", message.toLowerCase().split("[ {,|.}?]+"));
            for (Map.Entry<String, String> i : keys.entrySet()) {
                pattern = Pattern.compile(i.getKey());
                /*Есть ли такой ключ*/
                if (pattern.matcher(temp).find())
                    if (i.getValue().equals("whattime")) return new Date().toString();
                    else if(i.getValue().equals("parser")) {
                        String[] valueMassive = message.split(" ");
                        String value = valueMassive[1];
                        MatchParser parser = new MatchParser();
                        double result = 0;
                        try {
                            result = parser.Parse(value);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return "Результат = " + result;
                    } else
                        return answersByKeys.get(i.getValue());
            }
        }
        return "Я вас не понимаю.";
    }

    public String checkFunction(String message) throws IOException {
        String temp = new String();
        if(message.equals("/saveon")) {
            saveFlag = true;
        }
        if(message.equals("/saveoff")) {
            saveFlag = false;
        }
        if(message.equals("/fileclean")) {
            FileWriter fstream1 = new FileWriter(getUserName()+".txt");// конструктор с одним параметром - для перезаписи
            BufferedWriter out1 = new BufferedWriter(fstream1); //  создаём буферезированный поток
            out1.write(""); // очищаем, перезаписав поверх пустую строку
            out1.close(); // закрываем
        }
        if(message.equals("/onstupidbot")) {
            stupidAnswer=true;
        }
        if(message.equals("/offstupidbot")) {
            stupidAnswer=false;
        }
        if(message.equals("/loaddialog")) {
            return loadHistory();
        }
        return temp;
    }

    @Override
    public String loadName() throws FileNotFoundException {
        File file = new File("information.csv");
        Scanner scanner = new Scanner(file);
        String name = null;
        while(scanner.hasNextLine()) {
            name = scanner.nextLine();
        }
        return name;
    }

    @Override
    public void addHistory(String message) {
        try(FileWriter writer = new FileWriter(getUserName()+".txt", true))
        {
            // запись всей строки
            Date date = new Date();
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy" );
            String text = "Дата переписки : " + formatForDateNow.format(date) + "\n" + message;
            writer.write(text);
            writer.flush();
            writer.close();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }

    @Override
    public String loadHistory() {
        String[] temp = new String[1];
        String result = new String();
        FileReader fr= null;
        try {
            fr = new FileReader(getUserName()+".txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner scan = new Scanner(fr);
        while (scan.hasNextLine()) {
            temp[0] = scan.nextLine();
            result+=temp[0] +"\n";
        }
        return result;
    }

    public boolean isSaveFlag() {
        return saveFlag;
    }

    public void setSaveFlag(boolean saveFlag) {
        this.saveFlag = saveFlag;
    }

    public String readCourse(int i) throws IOException {
        Document doc = Jsoup.connect("http://mfd.ru/currency/?currency=USD").get();
        Elements elements = doc.getElementsByAttributeValue("class", "mfd-master-header-left");
        Element element1 = elements.get(0);
        String url = elements.attr("href");
        String title = element1.child(i).text();
        return title;
    }
}
