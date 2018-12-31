package main;

import javafx.scene.control.Alert;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Dararii
 */
public class Engine {

    public static String GetCurrentDir() {
        return (System.getProperty("user.dir"));
    }

    public static void ShowAlertInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }

    public static void ShowAlertError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Info");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }

    public static String LongToRupiahFormat(double numb) {
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        String s = String.format("%s %n", kursIndonesia.format(numb));
        System.out.println(s);
        return s;
    }

    public static String GetTimeNow() {
        String s = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Calendar.getInstance().getTime());
        return s;
    }

    public static String DateToString(Date date) {
        String s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);
        return s;
    }

    public static Date GetTimeNowDate() {
        Date s = Calendar.getInstance().getTime();
        return s;
    }
}
