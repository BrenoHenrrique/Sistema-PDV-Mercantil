package controlViwer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import modelBeans.Beans;
import controlConnetion.conectaBanco;

public class controlFornecedor {

    conectaBanco conexao = new conectaBanco();
    public PreparedStatement stmt = null;
    public ResultSet rs = null;

    public void createFornecedor(Beans mod) {

        conexao.conecta();
        try {
            stmt = conexao.conn.prepareStatement("INSERT INTO fornecedor( idFornecedor, produto, nome, cnpj, email, celular, telefone, endereco, bairro) values (?,?,?,?,?,?,?,?,?)");
            stmt.setString(1, mod.getIdFornecedor());
            stmt.setString(2, mod.getProduto());
            stmt.setString(3, mod.getNome());
            stmt.setString(4, mod.getCnpj());
            stmt.setString(5, mod.getEmail());
            stmt.setString(6, mod.getCelular());
            stmt.setString(7, mod.getTelefone());
            stmt.setString(8, mod.getEndereco());
            stmt.setString(9, mod.getBairro());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Fornecedor inserido!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao inserir fornecedor!\n" + ex);
        }        
        conexao.desconecta();
    }

    public void update(Beans mod) {

        conexao.conecta();
        try {
            stmt = conexao.conn.prepareStatement("UPDATE fornecedor SET  produto=?, nome=?, cnpj=?, email=?, celular=?, telefone=?, endereco=?, bairro=? WHERE idFornecedor=?");
            stmt.setString(1, mod.getProduto());
            stmt.setString(2, mod.getNome());
            stmt.setString(3, mod.getCnpj());
            stmt.setString(4, mod.getEmail());
            stmt.setString(5, mod.getCelular());
            stmt.setString(6, mod.getTelefone());
            stmt.setString(7, mod.getEndereco());
            stmt.setString(8, mod.getBairro());
            stmt.setString(9, mod.getIdFornecedor());
            stmt.execute();
            JOptionPane.showMessageDialog(null, "Registro atualizado!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar!\n" + ex);
        }        
        conexao.desconecta();
    }
   
    public void delete(Beans mod) {

        conexao.conecta();
        try {
            stmt = conexao.conn.prepareStatement("DELETE FROM fornecedor WHERE idFornecedor=?");
            stmt.setString(1, mod.getIdFornecedor());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Excluido com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir!\n" + ex);
        }        
        conexao.desconecta();
    }
}