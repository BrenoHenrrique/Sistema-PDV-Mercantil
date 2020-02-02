package controlViwer;

import controlConnetion.conectaBanco;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelBeans.Beans;

public class controlOrcamento {
    
    conectaBanco conexao = new conectaBanco();
    public PreparedStatement stmt = null;
    public ResultSet rs = null;

    @SuppressWarnings("CallToPrintStackTrace")
    public void create(Beans mod) {

        conexao.conecta();
        try {
            stmt = conexao.conn.prepareStatement("insert into orcamento(idOrcamento, nome, idProduto, preco, quantVenda, total) VALUES(?,?,?,?,?,?)");
            stmt.setString(1, mod.getIdOrcamento());
            stmt.setString(2, mod.getNome());
            stmt.setString(3, mod.getIdProduto());
            stmt.setString(4, mod.getVenda());
            stmt.setString(5, mod.getQuantVenda());
            stmt.setString(6, mod.getTotal());
            stmt.executeUpdate();
            //stmt.setTimestamp(indiceNoSQL,new java.sql.Timestamp(java.util.Calendar.getInstance().getTimeInMillis())); //da insert na data automaticamente
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        conexao.desconecta();
    }
}
