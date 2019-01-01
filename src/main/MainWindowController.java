package main;

import com.healthmarketscience.jackcess.DatabaseBuilder;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.DefaultStringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    @FXML
    public Label textTime;
    @FXML
    public Label textDate;
    @FXML
    public ComboBox cbSelectPort;
    @FXML
    public TableView<DownTime> listData;
    @FXML
    public TextField etNameLL;
    @FXML
    public TextField etNameGL;
    @FXML
    public ComboBox<String> cbShiftTIme;
    @FXML
    public Button btnStartTimer;
    @FXML
    public Button btnStopTimer;
    @FXML
    public Button btnExportExcel;
    @FXML
    public TableColumn<DownTime, String> colNo;
    @FXML
    public TableColumn<DownTime, String> colTime;
    @FXML
    public TableColumn<DownTime, String> colDuration;
    @FXML
    public TableColumn<DownTime, String> colDefect;
    @FXML
    public TableColumn<DownTime, String> colReason;

    private static String path;

    private Database db;
    ObservableList<DownTime> dt = FXCollections.observableArrayList();
    ObservableList<String> defectType = FXCollections.observableArrayList();
    ObservableList<String> shiftType = FXCollections.observableArrayList();

    Calendar currentDateTime = Calendar.getInstance();
    LocalDateTime currentCounterTime;
    Timeline clock = new Timeline(new KeyFrame(javafx.util.Duration.ZERO, e -> {
        currentCounterTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        textTime.setText(currentCounterTime.format(formatter));
        textDate.setText(currentCounterTime.format(formatter2));
    }),
            new KeyFrame(javafx.util.Duration.seconds(1))
    );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        path = Engine.GetCurrentDir() + "\\database.accdb";
        File f = new File(path);
        if (!f.exists() || f.isDirectory()) {
            try (com.healthmarketscience.jackcess.Database db = DatabaseBuilder.create(com.healthmarketscience.jackcess.Database.FileFormat.V2010, new File(path))) {
                System.out.println("The database file has been created.");
            } catch (IOException ioe) {
                ioe.printStackTrace(System.err);
            }
        }

        db = new Database(path);
        try {
            db.CreateTableDownTime();
        } catch (SQLException ex) {
            if (ex.getMessage().indexOf("already exists:") <= 0) {
                System.out.println(ex.getMessage());
            }
        }

        //init data
        currentDateTime.setTimeInMillis(System.currentTimeMillis());

        //cbSelectPort
        shiftType.addAll("A", "B");
        defectType.addAll("A", "B", "C", "D");
        cbShiftTIme.getItems().addAll(shiftType);

        dt.add(new DownTime(1, 1546278220000L, 3666, "A", "HAHA"));
        dt.add(new DownTime(2, 4, 6, "B", "HAHAHA"));

        initTableDownTime();
    }

    @FXML
    public void startTimer() {
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    @FXML
    public void stopTimer() {
        clock.stop();
    }

    private void initTableDownTime() {
        colNo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTime.setCellValueFactory(downTime -> {
            if (downTime.getValue().getTimestamp() == 0)
                return null;
            SimpleStringProperty property = new SimpleStringProperty();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            Date time = new Date(downTime.getValue().getTimestamp());
            property.setValue(dateFormat.format(time));
            return property;
        });

        colDuration.setCellValueFactory(downTime -> {
            if (downTime.getValue().getDurationInSecond() == 0)
                return null;
            Duration dur = Duration.ofSeconds(downTime.getValue().getDurationInSecond());
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(formatDuration(dur));
            return property;
        });

        colDefect.setCellValueFactory(new PropertyValueFactory<>("defect"));
        colDefect.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), defectType));
        colDefect.setOnEditCommit(event -> {
            event.getRowValue().setDefect(event.getNewValue());
            System.out.println(dt.get(0).getDefect());
        });

        colReason.setCellValueFactory(new PropertyValueFactory<>("reason"));
        colReason.setCellFactory(TextAreaTableCell.forTableColumn(new DefaultStringConverter()));
        colReason.setOnEditCommit(event -> {
            event.getRowValue().setReason(event.getNewValue());
            System.out.println(dt.get(0).getReason());
        });

        listData.setEditable(true);
        listData.setItems(dt);
    }

    public static String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        String positive = String.format(
                "%d:%02d:%02d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }
}
