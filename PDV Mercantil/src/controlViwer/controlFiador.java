package controlViwer;

import controlConnetion.conectaBanco;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import modelBeans.Beans;

public class controlFiador {

    conectaBanco conexao = new conectaBanco();
    PreparedStatement stmt = null;
    ResultSet rs = null;

    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    String date = new SimpleDateFormat("yyyy-MM-dd").format(timestamp.getTime());
    
    @SuppressWarnings("CallToPrintStackTrace")
    public void insert(Beans mod) {

        conexao.conecta();
        try {
            stmt = conexao.conn.prepareStatement("INSERT INTO fiadores(idVenda, nome, dataCompra, dataPagamento, total) VALUES(?,?,?,?,?)");
            stmt.setString(1, mod.getIdVenda());
            stmt.setString(2, mod.getNome());
            stmt.setString(3, date);
            stmt.setString(4, mod.getDia());
            stmt.setString(5, mod.getTotal());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        conexao.desconecta();
    }
}
