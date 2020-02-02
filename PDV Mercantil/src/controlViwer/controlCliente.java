package controlViwer;

import controlConnetion.conectaBanco;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import modelBeans.Beans;


public class controlCliente {
 
    conectaBanco conexao = new conectaBanco();
    PreparedStatement stmt = null;
    
    public void insertCliente(Beans mod){
        
        conexao.conecta();
        try{
            stmt = conexao.conn.prepareStatement("insert into cliente(nome, endereco, celular) values (?,?,?)");
            stmt.setString(1, mod.getNome());
            stmt.setString(2, mod.getEndereco());
            stmt.setString(3, mod.getCelular());
            stmt.execute();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        conexao.desconecta();
    }
    
    public void update(Beans mod){
        
        conexao.conecta();
        try{
            stmt = conexao.conn.prepareStatement("update cliente set nome=?, endereco=?, celular=? where idCliente=?");
            stmt.setString(1, mod.getNome());
            stmt.setString(2, mod.getEndereco());
            stmt.setString(3, mod.getCelular());
            stmt.setString(4, mod.getId());
            stmt.execute();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
}
