package controlViwer;

import controlConnetion.conectaBanco;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import modelBeans.Beans;

public class controlParcelamento {

    conectaBanco conexao = new conectaBanco();
    PreparedStatement stmt = null;

    public void insert(Beans mod) {
        conexao.conecta();
        try {
            stmt = conexao.conn.prepareStatement("insert into parcelamento(totalParcial, quantParcela, parcela, totalFinal, dataCompra) values(?,?,?,?,?)");
            stmt.setString(1, mod.getTotalParcial());
            stmt.setString(2, mod.getQuantParcela());
            stmt.setString(2, mod.getParcela());
            stmt.setString(3, mod.getTotalFinal());
            stmt.setString(4, mod.getDia());
            stmt.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        conexao.desconecta();
    }
}
