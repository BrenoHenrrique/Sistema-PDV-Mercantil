package controlViwer;

import controlConnetion.conectaBanco;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import modelBeans.Beans;

public class controlGastos {
    
    conectaBanco conexao = new conectaBanco();
    PreparedStatement stmt = null;
    
    public void insert(Beans mod){
        
        conexao.conecta();
        try {
            stmt = conexao.conn.prepareStatement("insert into gastos(descricao, observacoes, data, valor) values (?,?,?,?)");
            stmt.setString(1, mod.getDescricao());
            stmt.setString(2, mod.getObservacoes());
            stmt.setString(3, mod.getDia());
            stmt.setString(4, mod.getValor());
            stmt.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        conexao.desconecta();
    }
}
