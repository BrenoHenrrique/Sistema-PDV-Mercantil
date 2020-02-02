package viewer;

import controlConnetion.conectaBanco;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import modelBeans.Table;


public final class ComprasClientes extends javax.swing.JDialog {

    conectaBanco conexao = new conectaBanco();
    String nome;
    JFrame frame;
    JDialog dialog;
    
    public ComprasClientes(java.awt.Frame parent, boolean modal, String n) {
        super(parent, modal);
        initComponents();
        nome = n;
        componentes();
        tabelaHistorico.getTableHeader().setDefaultRenderer(new ComprasClientes.header());
        readTableHistorico("select * from fiadores where nome='" + nome + "'");
        somarValores();
    }

    private ComprasClientes(JFrame jFrame, boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void componentes(){
        scrollTable.getViewport().setBackground(Color.white);
    }
    
    public void somarValores() {
        double somaTotal = 0;
        for (int i = 0; i < tabelaHistorico.getRowCount(); i++) {
            somaTotal += Double.parseDouble(tabelaHistorico.getValueAt(i, 5).toString().replace(",", "."));
        }
        String resultado = String.format("%.2f", somaTotal); //coloca duas casas decimais
        total.setText("" + resultado);
    }
    
    static public class header extends DefaultTableCellRenderer {

        @SuppressWarnings("OverridableMethodCallInConstructor")
        public header() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable jtable, Object value, boolean selected, boolean focused, int row, int column) {
            super.getTableCellRendererComponent(jtable, value, selected, focused, row, column);
            setBackground(new Color(11,128,179));
            setForeground(new Color(255, 255, 255));
            setFont(new Font("Arial", Font.BOLD, 16));
            setHorizontalAlignment(JLabel.CENTER);
            return this;
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void readTableHistorico(String Sql) {

        conexao.conecta();
        ArrayList dados = new ArrayList();
        String[] colunas = new String[]{"ID Fiador", "idVenda", "Nome", "Data Compra", "Data Pagamento", "Total"};
        conexao.executaSQL(Sql);
        try {
            conexao.rs.first();
            do {
                dados.add(new Object[]{
                    conexao.rs.getString("idFiador"), conexao.rs.getString("idVenda"), conexao.rs.getString("nome"),
                    conexao.rs.getString("dataCompra"), conexao.rs.getString("dataPagamento"), conexao.rs.getString("total")});
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

        tabelaHistorico.setModel(tabela);
        tabelaHistorico.getColumnModel().getColumn(0).setPreferredWidth(130);
        tabelaHistorico.getColumnModel().getColumn(0).setResizable(false);
        tabelaHistorico.getColumnModel().getColumn(0).setCellRenderer(centralizado);
        tabelaHistorico.getColumnModel().getColumn(1).setPreferredWidth(130);
        tabelaHistorico.getColumnModel().getColumn(1).setResizable(false);
        tabelaHistorico.getColumnModel().getColumn(1).setCellRenderer(centralizado);
        tabelaHistorico.getColumnModel().getColumn(2).setPreferredWidth(200);
        tabelaHistorico.getColumnModel().getColumn(2).setResizable(false);
        tabelaHistorico.getColumnModel().getColumn(3).setPreferredWidth(140);
        tabelaHistorico.getColumnModel().getColumn(3).setResizable(false);
        tabelaHistorico.getColumnModel().getColumn(3).setCellRenderer(centralizado);
        tabelaHistorico.getColumnModel().getColumn(4).setPreferredWidth(140);
        tabelaHistorico.getColumnModel().getColumn(4).setResizable(false);
        tabelaHistorico.getColumnModel().getColumn(4).setCellRenderer(centralizado);
        tabelaHistorico.getColumnModel().getColumn(5).setPreferredWidth(130);
        tabelaHistorico.getColumnModel().getColumn(5).setResizable(false);
        tabelaHistorico.getColumnModel().getColumn(5).setCellRenderer(centralizado);
        tabelaHistorico.getTableHeader().setReorderingAllowed(false);
        tabelaHistorico.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabelaHistorico.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
        scrollTable = new javax.swing.JScrollPane();
        tabelaHistorico = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        caixa = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        barraPesquisa = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        botPesquisa = new javax.swing.JButton();
        botAtualizar = new javax.swing.JButton();
        chooser1 = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        chooser2 = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        titulo = new javax.swing.JLabel();
        total = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(15, 164, 255)));

        tabelaHistorico.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tabelaHistorico.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        scrollTable.setViewportView(tabelaHistorico);

        jLabel3.setFont(new java.awt.Font("Segoe UI Semilight", 0, 13)); // NOI18N
        jLabel3.setText("Quero pesquisar por:");

        caixa.setFont(new java.awt.Font("Segoe UI Symbol", 0, 16)); // NOI18N
        caixa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "  Produto" }));

        jLabel2.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel2.setText("Palavra Chave:");

        barraPesquisa.setFont(new java.awt.Font("Segoe UI Semilight", 0, 16)); // NOI18N
        barraPesquisa.setBorder(null);
        barraPesquisa.setOpaque(false);
        barraPesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                barraPesquisaKeyPressed(evt);
            }
        });

        botPesquisa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/buscar 32.png"))); // NOI18N
        botPesquisa.setToolTipText("Pesquisar");
        botPesquisa.setContentAreaFilled(false);
        botPesquisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botPesquisaActionPerformed(evt);
            }
        });

        botAtualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/atualizar 32.png"))); // NOI18N
        botAtualizar.setToolTipText("Atualizar Tabela");
        botAtualizar.setContentAreaFilled(false);

        chooser1.setDateFormatString("yyyy-MM-dd");
        chooser1.setFont(new java.awt.Font("Segoe UI Symbol", 0, 13)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Segoe UI Semilight", 1, 22)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("De");

        chooser2.setDateFormatString("yyyy-MM-dd");
        chooser2.setFont(new java.awt.Font("Segoe UI Symbol", 0, 13)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe UI Semilight", 1, 22)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Até");

        jPanel2.setBackground(new java.awt.Color(11, 128, 179));

        titulo.setFont(new java.awt.Font("Segoe UI Symbol", 0, 24)); // NOI18N
        titulo.setForeground(new java.awt.Color(255, 255, 255));
        titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titulo.setText("  Historico de Compras");
        titulo.setToolTipText("");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(titulo)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(titulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        total.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        total.setForeground(new java.awt.Color(0, 51, 255));
        total.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        total.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Total", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 24))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollTable)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(barraPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addComponent(botPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(botAtualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addGap(10, 10, 10)
                        .addComponent(chooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(jLabel4)
                        .addGap(10, 10, 10)
                        .addComponent(chooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(caixa, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(barraPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(botPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(2, 2, 2)
                        .addComponent(caixa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel2)
                        .addGap(8, 8, 8)
                        .addComponent(botAtualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addComponent(scrollTable, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setSize(new java.awt.Dimension(878, 689));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void barraPesquisaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_barraPesquisaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (caixa.getSelectedItem().equals("  Produto")) {
                readTableHistorico("select * from historicoentrega where descricao='" + barraPesquisa.getText() + "' and nome='" + nome + "'");
            }
        }
    }//GEN-LAST:event_barraPesquisaKeyPressed

    private void botPesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botPesquisaActionPerformed
        if (caixa.getSelectedItem().equals("  Produto")) {
            readTableHistorico("select * from historicoentrega where descricao='" + barraPesquisa.getText() + "' and nome='" + nome + "'");
        }
    }//GEN-LAST:event_botPesquisaActionPerformed

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
            java.util.logging.Logger.getLogger(ComprasClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            ComprasClientes dialog = new ComprasClientes(new javax.swing.JFrame(), true);
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
    private javax.swing.JTextField barraPesquisa;
    private javax.swing.JButton botAtualizar;
    private javax.swing.JButton botPesquisa;
    private javax.swing.JComboBox<String> caixa;
    private com.toedter.calendar.JDateChooser chooser1;
    private com.toedter.calendar.JDateChooser chooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JScrollPane scrollTable;
    private javax.swing.JTable tabelaHistorico;
    private javax.swing.JLabel titulo;
    private javax.swing.JLabel total;
    // End of variables declaration//GEN-END:variables
}
