package main;


//DB Import
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import net.ucanaccess.converters.TypesMap.AccessType;
import net.ucanaccess.ext.FunctionType;
import net.ucanaccess.jdbc.UcanaccessConnection;
import net.ucanaccess.jdbc.UcanaccessDriver;
/**
 *
 * @author Dararii
 */

public class Database {

    private Connection ucaConn;
    private String DBpath;

    public Database(String DBpath) {
        this.DBpath = DBpath;
    }

    private static Connection getUcanaccessConnection(String pathNewDB) throws SQLException, IOException {
        String url = UcanaccessDriver.URL_PREFIX + pathNewDB + ";newDatabaseVersion=V2010";
        return DriverManager.getConnection(url, "sa", "");
    }

    public ArrayList GetRowListFromTable(String kolom, String tabel){
        ArrayList result = new ArrayList();
        try{
            Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+ DBpath);
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT ["+ kolom +"] FROM ["+ tabel +"]");
            while (rs.next()) {
                result.add(rs.getString(1));
            }
            s.close();
            System.out.println(result.toString());
            return result;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public ArrayList GetRowListFromTable(String kolom, String tabel, String matchkolomkondisi, Boolean kondisi){
        ArrayList result = new ArrayList();
        try{
            Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+ DBpath);
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT ["+ kolom +"] FROM ["+ tabel +"] WHERE "+ matchkolomkondisi +" = '"+ kondisi +"'");
            while (rs.next()) {
                result.add(rs.getString(1));
            }
            s.close();
            System.out.println(result.toString());
            return result;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public int GetIDfromName(String nama){
        try{
            Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+ DBpath);
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT ID FROM datasantri WHERE nama = '"+nama+"'");
            int a = 0;
            while (rs.next()) {
                a = rs.getInt(1);
            }
            s.close();
            System.out.println(a);
            return a;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public double GetHargaFromNamaTagihan(String nama){
        try{
            Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+ DBpath);
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT harga FROM daftartagihan WHERE nama_tagihan = '"+nama+"'");
            double a = 0;
            while (rs.next()) {
                a = rs.getInt(1);
            }
            s.close();
            System.out.println(a);
            return a;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return 0;
        }
    }

    private boolean isFound = false;

    public boolean isIsFound() {
        return isFound;
    }

    public void setIsFound(boolean isFound) {
        this.isFound = isFound;
    }

    public boolean isSantriExist(String params){
        try{
            isFound = false;
            Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+ DBpath);
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT nrp FROM datasantri WHERE nama = '"+params+"'");
            while (rs.next()) {
                isFound = true;
                return true;
            }
            if (!isFound){
                rs = s.executeQuery("SELECT nama FROM datasantri WHERE nrp = '"+params+"'");
                while (rs.next()) {
                    isFound = true;
                    return true;
                }
            }
            return isFound;
        }
        catch(Exception e){
            return false;
        }
    }

    public boolean isSantriExistByNrp(String params){
        try{
            isFound = false;
            Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+ DBpath);
            Statement s = conn.createStatement();
            ResultSet rs;
            rs = s.executeQuery("SELECT nama FROM datasantri WHERE nrp = '"+params+"'");
            while (rs.next()) {
                isFound = true;
                return true;
            }
            return isFound;
        }
        catch(Exception e){
            return false;
        }
    }

    public int GetJumlahTagihanByNrp(String params){
        try{
            Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+ DBpath);
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT paid FROM Billing WHERE nrp = '"+params+"'");
            int x = 0;
            while (rs.next()) {
                boolean paid = rs.getBoolean(1);
                if (!paid){
                    x++;
                }
            }
            s.close();
            return x;
        } catch(Exception e){
            return 0;
        }
    }

    public void CreateTableDownTime() throws SQLException {
        Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+ DBpath);
        Statement st =conn.createStatement();
        st.execute("CREATE TABLE DataDownTime (id COUNTER PRIMARY KEY, timestamp bigint,duration bigint,defect text(400),reason text(400),op1 text(400),op2 text(400)) ");
        st.close();
    }

    public void InsertData(String table, String kolom, String data) throws SQLException {
        Statement st = null;
        try {
            Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+ DBpath);
            st = conn.createStatement();
            //String command = "INSERT INTO " + table +" ("+kolom+")  VALUES( '"+data+"')";
            //System.out.println(command);
            st.execute("INSERT INTO " + table +" ("+kolom+") VALUES('"+data+"')");
            System.out.println("Sukses");
        } finally {
            if (st != null)
                st.close();
        }
    }

    public void DeleteData(String table, String kolom, String data)throws SQLException {
        Statement st = null;
        try {
            Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+ DBpath);
            st = conn.createStatement();
            //String command = "DELETE FROM " + table +" WHERE "+kolom+"='"+data+"'";
            //System.out.println(command);
            st.execute("DELETE FROM " + table +" WHERE "+kolom+"='"+data+"'");
            System.out.println("Sukses");
        } finally {
            if (st != null)
                st.close();
        }
    }

    public void UpdateData(String table, String kolomupdate, String kolomparams, String data, String databaru)throws SQLException {
        Statement st = null;
        try {
            Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+ DBpath);
            st = conn.createStatement();//update x  set z = "[new_value]" where z = "y"
            //String command = "UPDATE " + table +" SET "+kolomupdate+"='"+databaru+"' WHERE "+kolomparams+"='"+data+"'";
            //System.out.println(command);
            st.execute("UPDATE " + table +" SET "+kolomupdate+"='"+databaru+"' WHERE "+kolomparams+"='"+data+"'");
            //System.out.println("Sukses");
        } finally {
            if (st != null)
                st.close();
        }
    }
    public void UpdateDataDate(String table, String kolomupdate, String kolomparams, String data, String databaru)throws SQLException {
        Statement st = null;
        try {
            Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+ DBpath);
            st = conn.createStatement();//update x  set z = "[new_value]" where z = "y"
            //String command = "UPDATE " + table +" SET "+kolomupdate+"='"+databaru+"' WHERE "+kolomparams+"='"+data+"'";
            //System.out.println(command);
            st.execute("UPDATE " + table +" SET "+kolomupdate+"=#"+databaru+"# WHERE "+kolomparams+"='"+data+"'");
            //System.out.println("Sukses");
        } finally {
            if (st != null)
                st.close();
        }
    }

}
