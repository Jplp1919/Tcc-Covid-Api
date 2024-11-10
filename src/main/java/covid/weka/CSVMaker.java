package covid.weka;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CSVMaker {
    public void sqlToCSV(String filename, Connection con, int id) {
        try {
            Statement statement = con.createStatement();
            FileWriter fw = new FileWriter(filename + ".csv");
            String sql = "SELECT * FROM pessoa WHERE id = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            int cols = rs.getMetaData().getColumnCount();
            for (int i = 1; i <= cols; i++) {
                fw.append(rs.getMetaData().getColumnLabel(i));
                if (i < cols) {
                    fw.append(',');
                } else {
                    fw.append('\n');
                }
            }

            while (rs.next()) {
                for (int i = 1; i <= cols; i++) {
                    if (rs.getString(i) == null) {
                        fw.append("?");
                    } else {
                        fw.append(rs.getString(i));
                    }
                    if (i < cols) {
                        fw.append(',');
                    }
                }
                fw.append('\n');
            }
            fw.flush();
            fw.close();

        } catch (IOException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
