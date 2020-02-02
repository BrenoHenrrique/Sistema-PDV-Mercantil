package controlViwer;

import controlConnetion.conectaBanco;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import modelBeans.Beans;

public class controlAgenda {
    
    conectaBanco conexao = new conectaBanco();
    PreparedStatement stmt = null;
    ResultSet rs = null;
        
    @SuppressWarnings("CallToPrintStackTrace")
    public void create(Beans mod) {

        conexao.conecta();
        try {
            stmt = conexao.conn.prepareStatement("INSERT INTO agenda(dia, hora, tipo, nome, endereco, cidade, celular, descricao) VALUES(?,?,?,?,?,?,?,?)");
            stmt.setString(1, mod.getDia());
            stmt.setString(2, mod.getHora());
            stmt.setString(3, mod.getTipo());
            stmt.setString(4, mod.getNome());
            stmt.setString(5, mod.getEndereco());
            stmt.setString(6, mod.getCidade());
            stmt.setString(7, mod.getCelular());
            stmt.setString(8, mod.getDescricao());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        conexao.desconecta();
    }
    
    public void delete(Beans mod) {

        conexao.conecta();
        try {
            stmt = conexao.conn.prepareStatement("DELETE FROM agenda WHERE idAgenda=?");
            stmt.setString(1, mod.getIdAgenda());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Excluido com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir!\n" + ex);
        }        
        conexao.desconecta();
    }
}
