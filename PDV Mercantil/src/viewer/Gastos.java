package viewer;

import controlConnetion.conectaBanco;
import controlViwer.controlGastos;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import modelBeans.Beans;
import modelBeans.Table;
import modelBeans.focusTraversalPolicy;

public final class Gastos extends javax.swing.JFrame {
    
    conectaBanco conexao = new conectaBanco();
    controlGastos control = new controlGastos();
    Beans mod = new Beans();
    private final FechamentoCaixa parent;
    PreparedStatement stmt = null;
    String dataBanco;
    String dataViwer;
    String dia;
    
    public Gastos(FechamentoCaixa h) {
        initComponents();
        this.parent = h;
        componentes();
        controlFocus();
        tabelaGastos.getTableHeader().setDefaultRenderer(new Gastos.header());
        dia = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
        readTableGastos("select * from gastos where data='" + dia + "'"); //'desc' decrescente 'asc' crescente
        data.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis())));
    }

    private Gastos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void componentes() {
        
        jTable.getViewport().setBackground(Color.white);
    }
    
    public void controlFocus() {
        ArrayList<Component> order = new ArrayList<>(1);
        order.add(descricao);
        focusTraversalPolicy policy = new focusTraversalPolicy(order);
        setFocusTraversalPolicy(policy);
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
            return super.getTableCellRendererComponent(jtable, value, selected, focused, row, column);
        }
    }
    
    @SuppressWarnings("CallToPrintStackTrace")
    public void readTableGastos(String Sql) {
        
        conexao.conecta();
        ArrayList dados = new ArrayList();
        String[] colunas = new String[]{"ID", "Descrição", "Observações", "Data", "Valor"};
        conexao.executaSQL(Sql);
        try {
            conexao.rs.first();
            do {
                dados.add(new Object[]{
                    conexao.rs.getString("idGastos"), conexao.rs.getString("descricao"), conexao.rs.getString("observacoes"),
                    conexao.rs.getString("data"), conexao.rs.getString("valor")});
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
        
        tabelaGastos.setModel(tabela);
        tabelaGastos.getColumnModel().getColumn(0).setPreferredWidth(100);
        tabelaGastos.getColumnModel().getColumn(0).setResizable(false);
        tabelaGastos.getColumnModel().getColumn(0).setCellRenderer(centralizado);
        tabelaGastos.getColumnModel().getColumn(1).setPreferredWidth(250);
        tabelaGastos.getColumnModel().getColumn(1).setResizable(false);
        tabelaGastos.getColumnModel().getColumn(2).setPreferredWidth(400);
        tabelaGastos.getColumnModel().getColumn(2).setResizable(false);
        tabelaGastos.getColumnModel().getColumn(3).setPreferredWidth(155);
        tabelaGastos.getColumnModel().getColumn(3).setResizable(false);
        tabelaGastos.getColumnModel().getColumn(3).setCellRenderer(centralizado);
        tabelaGastos.getColumnModel().getColumn(4).setPreferredWidth(153);
        tabelaGastos.getColumnModel().getColumn(4).setResizable(false);
        tabelaGastos.getColumnModel().getColumn(4).setCellRenderer(centralizado);
        tabelaGastos.getTableHeader().setReorderingAllowed(false);
        tabelaGastos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabelaGastos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        botSair = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        descricao = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        valor = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        observacoes = new javax.swing.JTextArea();
        calendario = new com.toedter.calendar.JCalendar();
        data = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTable = new javax.swing.JScrollPane();
        tabelaGastos = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        botAdicionar = new javax.swing.JButton();
        botExcluir = new javax.swing.JButton();
        botSalvar = new javax.swing.JButton();
        botAtualizar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(47, 114, 201), 1, true));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(226, 0, 1));

        botSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/exit 40.png"))); // NOI18N
        botSair.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botSairMouseClicked(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI Semilight", 1, 26)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("  Gastos");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 862, Short.MAX_VALUE)
                .addComponent(botSair))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(botSair, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(-2, 0, 1000, -1));

        descricao.setFont(new java.awt.Font("Segoe UI Semilight", 0, 16)); // NOI18N
        jPanel1.add(descricao, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 200, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Descrição");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        valor.setFont(new java.awt.Font("Segoe UI Semilight", 0, 16)); // NOI18N
        valor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                valorFocusLost(evt);
            }
        });
        jPanel1.add(valor, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 150, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Valor");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Observações:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 70, -1, -1));

        observacoes.setColumns(20);
        observacoes.setRows(3);
        jScrollPane2.setViewportView(observacoes);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 90, 341, 97));

        calendario.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(calendario, new org.netbeans.lib.awtextra.AbsoluteConstraints(697, 70, 291, 189));

        data.setFont(new java.awt.Font("Segoe UI Semilight", 0, 16)); // NOI18N
        data.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        data.setEnabled(false);
        data.setOpaque(false);
        jPanel1.add(data, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 150, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Data");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, -1, -1));

        tabelaGastos.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tabelaGastos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable.setViewportView(tabelaGastos);

        jPanel1.add(jTable, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 312, 970, 290));

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        jLabel7.setText("Tabela de Gastos:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, -1, -1));

        botAdicionar.setBackground(new java.awt.Color(17, 130, 15));
        botAdicionar.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        botAdicionar.setText("Adicionar");
        botAdicionar.setContentAreaFilled(false);
        botAdicionar.setOpaque(true);
        botAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botAdicionarActionPerformed(evt);
            }
        });
        jPanel1.add(botAdicionar, new org.netbeans.lib.awtextra.AbsoluteConstraints(895, 277, -1, -1));

        botExcluir.setBackground(new java.awt.Color(238, 8, 9));
        botExcluir.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        botExcluir.setText("Excluir");
        botExcluir.setContentAreaFilled(false);
        botExcluir.setOpaque(true);
        botExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botExcluirActionPerformed(evt);
            }
        });
        jPanel1.add(botExcluir, new org.netbeans.lib.awtextra.AbsoluteConstraints(796, 277, 93, -1));

        botSalvar.setBackground(new java.awt.Color(202, 202, 1));
        botSalvar.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        botSalvar.setText("Salvar");
        botSalvar.setContentAreaFilled(false);
        botSalvar.setOpaque(true);
        botSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botSalvarActionPerformed(evt);
            }
        });
        jPanel1.add(botSalvar, new org.netbeans.lib.awtextra.AbsoluteConstraints(697, 277, 93, -1));

        botAtualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/atualizar 32.png"))); // NOI18N
        botAtualizar.setToolTipText("Atualizar Tabela");
        botAtualizar.setContentAreaFilled(false);
        botAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botAtualizarActionPerformed(evt);
            }
        });
        jPanel1.add(botAtualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 270, 40, 40));

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 614, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setSize(new java.awt.Dimension(1000, 614));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void botSairMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botSairMouseClicked
        FechamentoCaixa h = (FechamentoCaixa) parent;
        h.calculoTotal();
        this.dispose();
    }//GEN-LAST:event_botSairMouseClicked

    private void botAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botAdicionarActionPerformed
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dataBanco = sdf.format(this.calendario.getDate());
        
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        dataViwer = df.format(this.calendario.getDate());
        data.setText(dataViwer);
    }//GEN-LAST:event_botAdicionarActionPerformed

    private void botExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botExcluirActionPerformed
        int linha = tabelaGastos.getSelectedRow();
        String id = tabelaGastos.getValueAt(linha, 0).toString();
        
        int resposta = JOptionPane.showConfirmDialog(null, "Deseja excluir este gasto?");
        if (resposta == JOptionPane.YES_OPTION) {
            conexao.conecta();
            try {
                stmt = conexao.conn.prepareStatement("delete from gastos where idGastos='" + id + "'");
                stmt.execute();
                JOptionPane.showMessageDialog(null, "Excluido com sucesso!");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            conexao.desconecta();
            readTableGastos("select * from gastos order by data desc");
        }
    }//GEN-LAST:event_botExcluirActionPerformed

    private void botSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botSalvarActionPerformed
        mod.setDescricao(descricao.getText());
        mod.setObservacoes(observacoes.getText());
        mod.setDia(dia);
        mod.setValor(valor.getText());
        control.insert(mod);
        
        descricao.setText("");
        valor.setText("");
        data.setText("");
        observacoes.setText("");
        descricao.grabFocus();
        
        readTableGastos("select * from gastos where valor > 0 order by data desc");
    }//GEN-LAST:event_botSalvarActionPerformed

    private void valorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_valorFocusLost
        String format = valor.getText().replace(",", ".");
        valor.setText(format);
    }//GEN-LAST:event_valorFocusLost

    private void botAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botAtualizarActionPerformed
        readTableGastos("select * from gastos where valor > 0 order by data desc");
    }//GEN-LAST:event_botAtualizarActionPerformed

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
            java.util.logging.Logger.getLogger(Gastos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Gastos().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botAdicionar;
    private javax.swing.JButton botAtualizar;
    private javax.swing.JButton botExcluir;
    private javax.swing.JLabel botSair;
    private javax.swing.JButton botSalvar;
    private com.toedter.calendar.JCalendar calendario;
    private javax.swing.JTextField data;
    private javax.swing.JTextField descricao;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jTable;
    private javax.swing.JTextArea observacoes;
    private javax.swing.JTable tabelaGastos;
    private javax.swing.JTextField valor;
    // End of variables declaration//GEN-END:variables
}
