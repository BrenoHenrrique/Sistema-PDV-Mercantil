package controlViwer;

import controlConnetion.conectaBanco;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import modelBeans.Beans;

public class controlEntragas {

    conectaBanco conexao = new conectaBanco();
    PreparedStatement stmt = null;
    ResultSet rs = null;

    public void insert(Beans mod) {

        conexao.conecta();
        try {
            stmt = conexao.conn.prepareStatement("insert into entregas(nome, bairro, endereco, complemento, observacoes) values(?,?,?,?,?)");
            stmt.setString(1, mod.getNome());
            stmt.setString(2, mod.getBairro());
            stmt.setString(3, mod.getEndereco());
            stmt.setString(4, mod.getComplemento());
            stmt.setString(5, mod.getObservacoes());
            stmt.execute();
            JOptionPane.showMessageDialog(null, "Entrega inserida com sucesso!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        conexao.desconecta();
    }

    public void update(Beans mod) {

        conexao.conecta();
        try {
            stmt = conexao.conn.prepareStatement("update entregas set nome=?, bairro=?, endereco=?, complemento=?, observacoes=? where idEntregas=?");
            stmt.setString(1, mod.getNome());
            stmt.setString(2, mod.getBairro());
            stmt.setString(3, mod.getEndereco());
            stmt.setString(4, mod.getComplemento());
            stmt.setString(5, mod.getObservacoes());
            stmt.setString(6, mod.getId());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Entrega atualizado com sucesso!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        conexao.desconecta();
    }

    public void delete(Beans mod) {

        conexao.conecta();
        try {
            stmt = conexao.conn.prepareStatement("delete from entregas where idEntregas=?");
            stmt.setString(1, mod.getId());
            stmt.execute();
            JOptionPane.showMessageDialog(null, "Pedido deletado!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        conexao.desconecta();
    }
}
