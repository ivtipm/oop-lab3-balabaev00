package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.FileWriter;

public class Greeting {

    @FXML
    private TextField fieldName;

    @FXML
    private CheckBox checkBox;

    @FXML
    private void start() throws Exception { // Обработка кнопки начать
        // Проверка имени и его длины
        if(!fieldName.getText().equals("") && fieldName.getText().length()>1) {
            if(checkBox.isSelected()) {
                FileWriter writer = new FileWriter("information.csv", true); // true == добавление текста в файл
                writer.write(fieldName.getText() + "\n");
                writer.close();
                this.close();
                PrimaryWindow primaryWindow = new PrimaryWindow();
                primaryWindow.start(new Stage());
            }
            else {
                // Если не проходит проверку, то создаем окно с "Warning"
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Предупреждение");
                alert.setHeaderText("Примите условие политики компании");
                alert.setContentText("Прочитайте условия и попробуйте снова.");
                alert.showAndWait();
            }
        }
        else {
            // Если не проходит проверку, то создаем окно с "Warning"
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Предупреждение");
            alert.setHeaderText("Длина имени меньше 1 символа!");
            alert.setContentText("Введите имя и попробуйте снова.");
            alert.showAndWait();
        }
    }

    @FXML
    private void information() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Политика компании");
        alert.setHeaderText("Пользуясь программой 'Боб', Вы доверяете нам свою личную информацию.\n" +
                "Мы делаем все для обеспечения ее безопасности и в то же время\n" + "предоставляем вам возможность управлять своими данными.");
        alert.setContentText("С уважением, твой Боб");
        alert.showAndWait();
    }

    @FXML
    private void close() { // Закрытие программы
        Stage stage = (Stage) fieldName.getScene().getWindow();
        stage.close();
    }

}