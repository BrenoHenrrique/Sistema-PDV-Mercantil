package controlViwer;

import controlConnetion.conectaBanco;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelBeans.Beans;


public class controlPagamento {
    
    conectaBanco conexao = new conectaBanco();
    Beans mod = new Beans();    
    public PreparedStatement stmt = null;
    public ResultSet rs = null;
    
    @SuppressWarnings("CallToPrintStackTrace")
    public void create(Beans mod){
        
        conexao.conecta();
        try {
            stmt = conexao.conn.prepareStatement("insert into pagamento (dinheiro, cartCredito, cartDebito, desconto, troco, recebido, total, dia) values (?,?,?,?,?,?,?,?)");
            stmt.setString(1, mod.getDinheiro());
            stmt.setString(2, mod.getCredito());
            stmt.setString(3, mod.getDebito());
            stmt.setString(4, mod.getDesconto());
            stmt.setString(5, mod.getTroco());
            stmt.setString(6, mod.getRecebido());
            stmt.setString(7, mod.getTotal());
            stmt.setString(8, mod.getDia());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
