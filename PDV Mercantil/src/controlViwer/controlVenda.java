package controlViwer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelBeans.Beans;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import controlConnetion.conectaBanco;

public class controlVenda {

    conectaBanco conexao = new conectaBanco();
    public PreparedStatement stmt = null;
    public ResultSet rs = null;

    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    String date = new SimpleDateFormat("yyyy-MM-dd").format(timestamp.getTime());

    @SuppressWarnings("CallToPrintStackTrace")
    public void createVenda(Beans mod) {

        conexao.conecta();
        try {
            stmt = conexao.conn.prepareStatement("insert into historico(idVenda, idProduto, nome, marca, quantAtual, venda, quantVenda, diaVenda, total) VALUES(?,?,?,?,?,?,?,?,?)");
            stmt.setString(1, mod.getIdVenda());
            stmt.setString(2, mod.getIdProduto());
            stmt.setString(3, mod.getNome());
            stmt.setString(4, mod.getMarca());
            stmt.setString(5, mod.getQuantAtual());
            stmt.setString(6, mod.getVenda());
            stmt.setString(7, mod.getQuantVenda());
            stmt.setString(8, date);
            stmt.setString(9, mod.getTotal());
            stmt.executeUpdate();
            //stmt.setTimestamp(indiceNoSQL,new java.sql.Timestamp(java.util.Calendar.getInstance().getTimeInMillis())); //da insert na data automaticamente
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        conexao.desconecta();
    }
}
