package covid.weka;

import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class SavePredictionService {

    public void save(String prediction, String percentage, int id, Connection con) {
        String sql = "INSERT INTO prediction (TemCovid, Porcentagem, idPessoa) VALUES (?, ?, ?);";
        try {
            PreparedStatement pstm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstm.setString(1, prediction);
            pstm.setString(2, percentage);
            pstm.setInt(3, id);
            pstm.execute();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
