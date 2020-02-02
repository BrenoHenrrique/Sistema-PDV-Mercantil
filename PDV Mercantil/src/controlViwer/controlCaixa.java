package controlViwer;

import controlConnetion.conectaBanco;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelBeans.Beans;

public class controlCaixa {

    conectaBanco conexao = new conectaBanco();
    PreparedStatement stmt = null;
    ResultSet rs = null;
    
    public void update(Beans mod) {

        conexao.conecta();

        try {
            stmt = conexao.conn.prepareStatement("update caixa set data=?, hora=?, turno=?, responsavel=?, total=?, aporte=?, verificador=? where idCaixa=?");
            stmt.setString(1, mod.getDia());
            stmt.setString(2, mod.getHora());
            stmt.setString(3, mod.getTurno());
            stmt.setString(4, mod.getNome());
            stmt.setString(5, mod.getTotal());
            stmt.setString(6, mod.getAporte());
            stmt.setString(7, mod.getVerificador());
            stmt.setString(8, mod.getId());
            stmt.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
