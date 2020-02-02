package controlViwer;

import controlConnetion.conectaBanco;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import modelBeans.Beans;

public class controlHistorico {

    conectaBanco conexao = new conectaBanco();
    PreparedStatement stmt = null;
    ResultSet rs = null;

    public void delete(Beans mod) {

        conexao.conecta();
        try {
            stmt = conexao.conn.prepareStatement("delete from historico where idHistorico=?");
            stmt.setString(1, mod.getId());
            stmt.execute();
            JOptionPane.showMessageDialog(null, "Item excluido com sucesso!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
