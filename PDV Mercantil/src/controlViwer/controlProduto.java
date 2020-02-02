package controlViwer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import modelBeans.Beans;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import controlConnetion.conectaBanco;

public class controlProduto {

    conectaBanco conexao = new conectaBanco();    
    public PreparedStatement stmt = null;
    public ResultSet rs = null;
    
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    String date = new SimpleDateFormat("yyyy-MM-dd").format(timestamp.getTime());

    @SuppressWarnings("CallToPrintStackTrace")
    public void createProduto(Beans mod) {

        conexao.conecta();
        try {
            stmt = conexao.conn.prepareStatement("INSERT INTO produto(idProduto, nome, marca, quantAtual, compra, porcentagem, venda, diaCompra, foto) VALUES(?,?,?,?,?,?,?,?,?)");
            stmt.setString(1, mod.getIdProduto());
            stmt.setString(2, mod.getNome());
            stmt.setString(3, mod.getMarca());
            stmt.setString(4, mod.getQuantAtual());
            stmt.setString(5, mod.getCompra());
            stmt.setString(6, mod.getPorcentagem());
            stmt.setString(7, mod.getVenda());
            stmt.setString(8, date);
            stmt.setString(9, mod.getCaminho());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Produto inserido!");
        } catch (SQLException ex) {
            //ex.printStackTrace();
        }
        
        conexao.desconecta();
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void update(Beans mod) {

        conexao.conecta();
        try {
            stmt = conexao.conn.prepareStatement("UPDATE produto SET idProduto=?, nome=?, marca=?, quantAtual=?, compra=?, venda=?, porcentagem=?, diaCompra=?, foto=? WHERE id=?");
            stmt.setString(1, mod.getIdProduto());
            stmt.setString(2, mod.getNome());
            stmt.setString(3, mod.getMarca());
            stmt.setString(4, mod.getQuantAtual());
            stmt.setString(5, mod.getCompra());
            stmt.setString(6, mod.getVenda());
            stmt.setString(7, mod.getPorcentagem());
            stmt.setString(8, mod.getDia());
            stmt.setString(9, mod.getCaminho());
            stmt.setString(10, mod.getId());
            stmt.execute();            
            JOptionPane.showMessageDialog(null, "Produto atualizado!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        conexao.desconecta();
    }
   
    @SuppressWarnings("CallToPrintStackTrace")
    public void delete(Beans mod) {

        conexao.conecta();
        try {
            stmt = conexao.conn.prepareStatement("DELETE FROM produto WHERE id=?");
            stmt.setString(1, mod.getId());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Produto excluido!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        conexao.desconecta();
    }
}
