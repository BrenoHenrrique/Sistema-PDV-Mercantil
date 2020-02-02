package viewer;

import controlConnetion.conectaBanco;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import modelBeans.Table;

public final class ParcelamentoLista extends javax.swing.JDialog {

    conectaBanco conexao = new conectaBanco();

    public ParcelamentoLista(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        jScrollPane1.getViewport().setBackground(Color.white);
        tabelaParcelamento.getTableHeader().setDefaultRenderer(new Fornecedor.header());
        readTableParcelamento("select * from parcelamento order by idParcelamento desc");
    }

    static public class header extends DefaultTableCellRenderer {

        @SuppressWarnings("OverridableMethodCallInConstructor")
        public header() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable jtable, Object value, boolean selected, boolean focused, int row, int column) {
            super.getTableCellRendererComponent(jtable, value, selected, focused, row, column);
            setBackground(new Color(033, 108, 138));
            setForeground(new Color(255, 255, 255));
            setFont(new Font("Arial", Font.BOLD, 16));
            setHorizontalAlignment(JLabel.CENTER);
            return this;
        }
    }

    public void readTableParcelamento(String Sql) {

        conexao.conecta();
        ArrayList dados = new ArrayList();
        String[] colunas = new String[]{"idParcelamento", "Total Parcial", "Quantidade", "Parcela", "Total Final", "Data Compra"};
        conexao.executaSQL(Sql);
        try {
            conexao.rs.first();
            do {
                dados.add(new Object[]{conexao.rs.getString("idParcelamento"), conexao.rs.getString("totalParcial"), 
                    conexao.rs.getString("quantParcela"), conexao.rs.getString("parcela"), conexao.rs.getString("totalFinal"), 
                    conexao.rs.getString("dataCompra")});
            } while (conexao.rs.next());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        Table tabela = new Table(dados, colunas);

        DefaultTableCellRenderer esquerda = new DefaultTableCellRenderer();
        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        DefaultTableCellRenderer direita = new DefaultTableCellRenderer();
        esquerda.setHorizontalAlignment(SwingConstants.LEFT);
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);
        direita.setHorizontalAlignment(SwingConstants.RIGHT);

        tabelaParcelamento.setModel(tabela);
        tabelaParcelamento.getColumnModel().getColumn(0).setPreferredWidth(100);
        tabelaParcelamento.getColumnModel().getColumn(0).setResizable(false);
        tabelaParcelamento.getColumnModel().getColumn(0).setCellRenderer(centralizado);
        tabelaParcelamento.getColumnModel().getColumn(0).setMinWidth(0); //ocultando o indice 0 idVenda
        tabelaParcelamento.getColumnModel().getColumn(0).setMaxWidth(0); //ocultando o indice 0 idVenda
        tabelaParcelamento.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabelaParcelamento.getColumnModel().getColumn(1).setResizable(false);
        tabelaParcelamento.getColumnModel().getColumn(1).setCellRenderer(centralizado);
        tabelaParcelamento.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabelaParcelamento.getColumnModel().getColumn(2).setResizable(false);
        tabelaParcelamento.getColumnModel().getColumn(2).setCellRenderer(centralizado);
        tabelaParcelamento.getColumnModel().getColumn(3).setPreferredWidth(150);
        tabelaParcelamento.getColumnModel().getColumn(3).setResizable(false);
        tabelaParcelamento.getColumnModel().getColumn(3).setCellRenderer(centralizado);
        tabelaParcelamento.getColumnModel().getColumn(4).setPreferredWidth(150);
        tabelaParcelamento.getColumnModel().getColumn(4).setResizable(false);
        tabelaParcelamento.getColumnModel().getColumn(4).setCellRenderer(centralizado);
        tabelaParcelamento.getColumnModel().getColumn(5).setPreferredWidth(150);
        tabelaParcelamento.getColumnModel().getColumn(5).setResizable(false);
        tabelaParcelamento.getColumnModel().getColumn(5).setCellRenderer(centralizado);
        tabelaParcelamento.getTableHeader().setReorderingAllowed(false);
        tabelaParcelamento.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabelaParcelamento.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        conexao.desconecta();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaParcelamento = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(33, 108, 138));
        jLabel1.setFont(new java.awt.Font("Segoe UI Symbol", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("  Lista de Parcelamento");
        jLabel1.setOpaque(true);

        tabelaParcelamento.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tabelaParcelamento.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tabelaParcelamento);

        jLabel2.setFont(new java.awt.Font("Yu Gothic UI Semilight", 0, 20)); // NOI18N
        jLabel2.setText("Tabela Parcelamento");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(766, 512));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ParcelamentoLista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            ParcelamentoLista dialog = new ParcelamentoLista(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabelaParcelamento;
    // End of variables declaration//GEN-END:variables
}
