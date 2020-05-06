package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PrimaryWindow {

    private static int width = 343;
    private static int height = 484;

    private ChatBot bob;

    @FXML
    private AnchorPane mainAnchor;

    @FXML
    private TextField messageField;

    @FXML
    private TextArea textArea;

    @FXML
    private void send() {
        if(messageField.getText().equals("") ) {
            return;
        }
        Date date = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("hh:mm:ss");
        String userMessage = "[" + formatForDateNow.format(date) + "]" + " " + bob.getUserName()+" : " + messageField.getText() + "\n";
        textArea.setText(textArea.getText() + userMessage); // Отправка сообщения пользователя
        if(messageField.getText().equals("/saveon") || messageField.getText().equals("/saveoff") ||
                messageField.getText().equals("/fileclean") || messageField.getText().equals("/onstupidbot") ||
                messageField.getText().equals("/offstupidbot") || messageField.getText().equals("/loaddialog")) {
            if(messageField.getText().equals("/loaddialog")) {
                try {
                    textArea.setText(bob.checkFunction(messageField.getText()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                messageField.setText("");
                return;
            }
            else {
                try {
                    bob.checkFunction(messageField.getText());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String botMessage = "[" + formatForDateNow.format(date) + "]" + " " + bob.getBotName() + " : " + bob.say(messageField.getText()) + "\n";
        textArea.setText(textArea.getText() + botMessage);
        messageField.setText("");
    }

    public void start(Stage primaryStage) throws Exception {
        /*Создаем окно*/
        Parent root = FXMLLoader.load(getClass().getResource("primaryWindow.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED); // Убираем кнопки (Закрыть,свернуть и т.п)
        primaryStage.setTitle("Бот по имени Боб");
        primaryStage.setScene(new Scene(root, width, height));
        primaryStage.show();
    }

    @FXML
    void initialize() {
        bob = new ChatBot();
        bob.setBotName("Боб");
        try {
            bob.setUserName(bob.loadName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //Обработка клавиш
        messageField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    send();
                }
            }
        });
    }

    @FXML
    private void close() { // Закрытие программы
        if(bob.isSaveFlag())
            bob.addHistory(textArea.getText());
        Stage stage = (Stage) mainAnchor.getScene().getWindow();
        stage.close();
    }
}

