package viewer;

import controlConnetion.conectaBanco;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.table.DefaultTableCellRenderer;
import modelBeans.Beans;
import modelBeans.Table;
import modelBeans.focusTraversalPolicy;

public final class Cancelamento extends javax.swing.JFrame {

    Beans mod = new Beans();
    conectaBanco conexao = new conectaBanco();
    PreparedStatement stmt = null;
    ResultSet rs = null;
    private String strData1;
    private String strData2;
    String data1;
    String data2;

    public Cancelamento() {
        initComponents();
        controlFocus();
        componentes();
        caixaSelecaoCor();
        tabelaCancelamento.getTableHeader().setDefaultRenderer(new Cancelamento.header());
        readTableCancelamento("select * from cancelar order by idCancelar desc");
    }

    public void componentes() {

        jtablePagamento.getViewport().setBackground(Color.white);
        jtablePagamento.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        total.setText("R$ 0,00");
        getContentPane().setBackground(Color.white);
    }

    public void controlFocus() {
        ArrayList<Component> order = new ArrayList<>(7);
        order.add(this.getContentPane());
        order.add(pesquisa);
        order.add(botPesquisa);
        order.add(botAtualizar);
        order.add(chooser1);
        order.add(chooser2);
        order.add(caixa);
        focusTraversalPolicy policy = new focusTraversalPolicy(order);
        setFocusTraversalPolicy(policy);
    }

    public void caixaSelecaoCor() {

        Object child = caixa.getAccessibleContext().getAccessibleChild(0);
        BasicComboPopup popup = (BasicComboPopup) child;
        JList list = popup.getList();
        list.setSelectionBackground(new Color(64, 64, 64));
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void somarValores() {
        conexao.conecta();
        conexao.executaSQL("select * from historicoentrega");
        try {
            while (conexao.rs.next()) {
                double somaTotal = 0;
                for (int i = 0; i < tabelaCancelamento.getRowCount(); i++) {
                    somaTotal += Double.parseDouble(tabelaCancelamento.getValueAt(i, 4).toString());
                }
                total.setText("" + somaTotal);

                float valor = Float.parseFloat(String.valueOf(somaTotal));
                DecimalFormat df = new DecimalFormat("##,##0.00");
                String s = df.format(valor);
                total.setText("R$ " + s);
            }
        } catch (NumberFormatException | SQLException ex) {
            ex.printStackTrace();
        }
        conexao.desconecta();
    }

    public void dataFormatada() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        strData1 = sdf.format(this.chooser1.getDate());
        strData2 = sdf.format(this.chooser2.getDate());
        data1 = strData1;
        data2 = strData2;
    }

    public void impressao() {

        
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

    public void readTableCancelamento(String Sql) {
        conexao.conecta();
        ArrayList dados = new ArrayList();
        String[] colunas = new String[]{"id", "Mesa", "Produto", "Valor", "Data", "Tipo"};
        conexao.executaSQL(Sql);
        try {
            conexao.rs.first();
            do {
                dados.add(new Object[]{
                    conexao.rs.getString("idCancelar"), conexao.rs.getString("mesa"), conexao.rs.getString("produto"),
                    conexao.rs.getString("valor"), conexao.rs.getString("data"), conexao.rs.getString("tipo")});
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

        tabelaCancelamento.setModel(tabela);
        tabelaCancelamento.getColumnModel().getColumn(0).setPreferredWidth(125);
        tabelaCancelamento.getColumnModel().getColumn(0).setResizable(false);
        tabelaCancelamento.getColumnModel().getColumn(0).setCellRenderer(centralizado);
        tabelaCancelamento.getColumnModel().getColumn(0).setMinWidth(0);
        tabelaCancelamento.getColumnModel().getColumn(0).setMaxWidth(0);
        tabelaCancelamento.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabelaCancelamento.getColumnModel().getColumn(1).setResizable(false);
        tabelaCancelamento.getColumnModel().getColumn(1).setCellRenderer(centralizado);
        tabelaCancelamento.getColumnModel().getColumn(2).setPreferredWidth(375);
        tabelaCancelamento.getColumnModel().getColumn(2).setResizable(false);
        tabelaCancelamento.getColumnModel().getColumn(3).setPreferredWidth(150);
        tabelaCancelamento.getColumnModel().getColumn(3).setResizable(false);
        tabelaCancelamento.getColumnModel().getColumn(3).setCellRenderer(centralizado);
        tabelaCancelamento.getColumnModel().getColumn(4).setPreferredWidth(150);
        tabelaCancelamento.getColumnModel().getColumn(4).setResizable(false);
        tabelaCancelamento.getColumnModel().getColumn(4).setCellRenderer(centralizado);
        tabelaCancelamento.getColumnModel().getColumn(5).setPreferredWidth(103);
        tabelaCancelamento.getColumnModel().getColumn(5).setResizable(false);
        tabelaCancelamento.getColumnModel().getColumn(5).setCellRenderer(centralizado);
        tabelaCancelamento.getTableHeader().setReorderingAllowed(false);
        tabelaCancelamento.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabelaCancelamento.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
        jLabel5 = new javax.swing.JLabel();
        pesquisa = new javax.swing.JTextField();
        jtablePagamento = new javax.swing.JScrollPane();
        tabelaCancelamento = new javax.swing.JTable();
        botPesquisa = new javax.swing.JButton();
        botAtualizar = new javax.swing.JButton();
        botImprimir = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        chooser1 = new com.toedter.calendar.JDateChooser();
        chooser2 = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        caixa = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        total = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        botSair = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 0, 1)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel5.setText("Pesquisar");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(57, 118, 110, -1));

        pesquisa.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        pesquisa.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pesquisa.setDisabledTextColor(java.awt.Color.black);
        pesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pesquisaKeyPressed(evt);
            }
        });
        jPanel1.add(pesquisa, new org.netbeans.lib.awtextra.AbsoluteConstraints(57, 142, 280, 35));

        tabelaCancelamento.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tabelaCancelamento.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabelaCancelamento.setGridColor(java.awt.Color.lightGray);
        jtablePagamento.setViewportView(tabelaCancelamento);

        jPanel1.add(jtablePagamento, new org.netbeans.lib.awtextra.AbsoluteConstraints(57, 182, 930, 500));

        botPesquisa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/buscar 32.png"))); // NOI18N
        botPesquisa.setToolTipText("Pesquisar");
        botPesquisa.setContentAreaFilled(false);
        botPesquisa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botPesquisaMouseClicked(evt);
            }
        });
        jPanel1.add(botPesquisa, new org.netbeans.lib.awtextra.AbsoluteConstraints(347, 142, 32, 35));

        botAtualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/atualizar 32.png"))); // NOI18N
        botAtualizar.setToolTipText("Atualizar Tabela");
        botAtualizar.setContentAreaFilled(false);
        botAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botAtualizarActionPerformed(evt);
            }
        });
        jPanel1.add(botAtualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(381, 142, 34, 35));

        botImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/imprimir 35.png"))); // NOI18N
        botImprimir.setToolTipText("Imprimir Tabela");
        botImprimir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botImprimirMouseClicked(evt);
            }
        });
        jPanel1.add(botImprimir, new org.netbeans.lib.awtextra.AbsoluteConstraints(419, 142, -1, 36));

        jLabel1.setFont(new java.awt.Font("Segoe UI Semilight", 1, 22)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("De");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 140, -1, 36));

        chooser1.setDateFormatString("yyyy-MM-dd");
        chooser1.setFont(new java.awt.Font("Segoe UI Symbol", 0, 13)); // NOI18N
        jPanel1.add(chooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 140, 144, 36));

        chooser2.setDateFormatString("yyyy-MM-dd");
        chooser2.setFont(new java.awt.Font("Segoe UI Symbol", 0, 13)); // NOI18N
        jPanel1.add(chooser2, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 140, 144, 36));

        jLabel2.setFont(new java.awt.Font("Segoe UI Semilight", 1, 22)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Até");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 140, -1, 36));

        caixa.setFont(new java.awt.Font("Segoe UI Semilight", 0, 16)); // NOI18N
        caixa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "  Produto", "  Data Venda", "  Tipo" }));
        caixa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                caixaKeyPressed(evt);
            }
        });
        jPanel1.add(caixa, new org.netbeans.lib.awtextra.AbsoluteConstraints(58, 76, 140, 36));

        jLabel3.setFont(new java.awt.Font("Segoe UI Semilight", 0, 13)); // NOI18N
        jLabel3.setText("Quero pesquisar por:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(57, 56, -1, -1));

        total.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        total.setForeground(new java.awt.Color(0, 51, 255));
        total.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        total.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Total", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 18))); // NOI18N
        jPanel1.add(total, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 60, 160, 70));

        jLabel4.setBackground(new java.awt.Color(226, 0, 1));
        jLabel4.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(226, 0, 1));
        jLabel4.setText("Histórico de Cancelamento");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, 320, -1));

        botSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/sair 32.png"))); // NOI18N
        botSair.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botSairMouseClicked(evt);
            }
        });
        jPanel1.add(botSair, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 0, -1, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1000, 700));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void pesquisaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pesquisaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            if (caixa.getSelectedItem().equals("  produto")) {
                readTableCancelamento("select * from cancelar where produto like '%" + pesquisa.getText() + "%'");
                somarValores();
            } else if (caixa.getSelectedItem().equals("  Tipo")) {
                readTableCancelamento("select * from cancelar where tipo like '%" + pesquisa.getText() + "%'");
                somarValores();
            } else if (caixa.getSelectedItem().equals("  Data Venda")) {
                dataFormatada();
                readTableCancelamento("select * from cancelar where data >='" + data1 + "' and diaVenda <='" + data2 + "'");
                somarValores();
            }
        }
    }//GEN-LAST:event_pesquisaKeyPressed

    private void botPesquisaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botPesquisaMouseClicked
        if (caixa.getSelectedItem().equals("  produto")) {
            readTableCancelamento("select * from cancelar where produto like '%" + pesquisa.getText() + "%'");
            somarValores();
        } else if (caixa.getSelectedItem().equals("  Tipo")) {
            readTableCancelamento("select * from cancelar where tipo like '%" + pesquisa.getText() + "%'");
            somarValores();
        }
    }//GEN-LAST:event_botPesquisaMouseClicked

    private void botAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botAtualizarActionPerformed
        readTableCancelamento("select * from cancelar where idHistoricoEntregaPK > 0");
    }//GEN-LAST:event_botAtualizarActionPerformed

    private void botImprimirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botImprimirMouseClicked
        impressao();
    }//GEN-LAST:event_botImprimirMouseClicked

    private void caixaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_caixaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            if (caixa.getSelectedItem().equals("  produto")) {
                readTableCancelamento("select * from cancelar where produto like '%" + pesquisa.getText() + "%'");
                somarValores();
            } else if (caixa.getSelectedItem().equals("  Tipo")) {
                readTableCancelamento("select * from cancelar where tipo like '%" + pesquisa.getText() + "%'");
                somarValores();
            } else if (caixa.getSelectedItem().equals("  Data Venda")) {
                dataFormatada();
                readTableCancelamento("select * from cancelar where data >='" + data1 + "' and diaVenda <='" + data2 + "'");
                somarValores();
            }
        }
    }//GEN-LAST:event_caixaKeyPressed

    private void botSairMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botSairMouseClicked
        this.dispose();
    }//GEN-LAST:event_botSairMouseClicked

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
            java.util.logging.Logger.getLogger(Cancelamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Cancelamento().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botAtualizar;
    private javax.swing.JLabel botImprimir;
    private javax.swing.JButton botPesquisa;
    private javax.swing.JLabel botSair;
    private javax.swing.JComboBox<String> caixa;
    private com.toedter.calendar.JDateChooser chooser1;
    private com.toedter.calendar.JDateChooser chooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jtablePagamento;
    private javax.swing.JTextField pesquisa;
    private javax.swing.JTable tabelaCancelamento;
    private javax.swing.JLabel total;
    // End of variables declaration//GEN-END:variables
}
