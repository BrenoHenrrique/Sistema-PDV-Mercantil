/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewer;

import controlConnetion.conectaBanco;
import controlViwer.controlCliente;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

public final class AddClientes extends javax.swing.JDialog {    
    
    conectaBanco conexao = new conectaBanco();
    controlCliente control = new controlCliente();
    Beans mod = new Beans();
    
    public AddClientes(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        componentes();
        controlFocus();
        mostrar();
        readTableCliente("select * from cliente where idCliente > 0 order by nome");
        tabelaCliente.getTableHeader().setDefaultRenderer(new AddClientes.header());
    }

    AddClientes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void componentes() {

        barTable.getViewport().setBackground(Color.WHITE);
    }

    public void controlFocus() {
        ArrayList<Component> order = new ArrayList<>(2);
        order.add(nome);
        order.add(celular);
        order.add(endereco);
        focusTraversalPolicy policy = new focusTraversalPolicy(order);
        setFocusTraversalPolicy(policy);
    }

    public void mostrar() {

        conexao.conecta();
        conexao.executaSQL("SELECT * FROM cliente ORDER BY idCliente DESC LIMIT 1");
        try {
            conexao.rs.last();
            do {
                idCliente.setText(String.valueOf(conexao.rs.getInt("idCliente")));
            } while (conexao.rs.next());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        conexao.desconecta();

        int x = Integer.parseInt(idCliente.getText());
        int y = 1;
        int result;
        result = x + y;
        idCliente.setText(String.valueOf(result));
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

    public void readTableCliente(String Sql) {
        conexao.conecta();
        ArrayList dados = new ArrayList();
        String[] colunas = new String[]{"ID", "Nome", "Celular", "Endereço"};
        conexao.executaSQL(Sql);
        try {
            conexao.rs.first();
            do {
                dados.add(new Object[]{
                    conexao.rs.getString("idCliente"), conexao.rs.getString("nome"),
                    conexao.rs.getString("celular"), conexao.rs.getString("endereco")});
            } while (conexao.rs.next());
        } catch (SQLException ex) {
            //ex.printStackTrace();
        }
        Table tabela = new Table(dados, colunas);

        DefaultTableCellRenderer esquerda = new DefaultTableCellRenderer();
        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        DefaultTableCellRenderer direita = new DefaultTableCellRenderer();
        esquerda.setHorizontalAlignment(SwingConstants.LEFT);
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);
        direita.setHorizontalAlignment(SwingConstants.RIGHT);

        tabelaCliente.setModel(tabela);
        tabelaCliente.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabelaCliente.getColumnModel().getColumn(0).setResizable(false);
        tabelaCliente.getColumnModel().getColumn(0).setCellRenderer(centralizado);
        tabelaCliente.getColumnModel().getColumn(1).setPreferredWidth(300);
        tabelaCliente.getColumnModel().getColumn(1).setResizable(false);
        tabelaCliente.getColumnModel().getColumn(1).setCellRenderer(centralizado);
        tabelaCliente.getColumnModel().getColumn(2).setPreferredWidth(250);
        tabelaCliente.getColumnModel().getColumn(2).setResizable(false);
        tabelaCliente.getColumnModel().getColumn(3).setPreferredWidth(368);
        tabelaCliente.getColumnModel().getColumn(3).setResizable(false);
        tabelaCliente.getTableHeader().setReorderingAllowed(false);
        tabelaCliente.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabelaCliente.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
        jLabel2 = new javax.swing.JLabel();
        nome = new javax.swing.JTextField();
        barTable = new javax.swing.JScrollPane();
        tabelaCliente = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        celular = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        endereco = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        idCliente = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        ferramentas = new javax.swing.JMenu();
        menuAdicionar = new javax.swing.JMenuItem();
        menuLimpar = new javax.swing.JMenuItem();
        menuAtualizar = new javax.swing.JMenuItem();
        menuExcluir = new javax.swing.JMenuItem();
        opcoes = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(24, 131, 215));
        jLabel1.setFont(new java.awt.Font("Segoe UI Symbol", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("  Cadastro Cliente");
        jLabel1.setOpaque(true);

        jLabel2.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel2.setText("Nome");

        nome.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        nome.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        nome.setOpaque(false);

        tabelaCliente.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tabelaCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tabelaCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaClienteMouseClicked(evt);
            }
        });
        barTable.setViewportView(tabelaCliente);

        jLabel3.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel3.setText("Celular");

        celular.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        celular.setText("9");
        celular.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        celular.setOpaque(false);

        jLabel4.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel4.setText("Endereço");

        endereco.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        endereco.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        endereco.setOpaque(false);
        endereco.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                enderecoKeyPressed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel6.setText("ID Cliente");

        idCliente.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        idCliente.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        idCliente.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        idCliente.setEnabled(false);
        idCliente.setOpaque(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(barTable)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(idCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(nome, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(celular)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(0, 85, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(endereco, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(idCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nome, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(celular, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endereco, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(barTable, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        ferramentas.setText("Ferramentas");
        ferramentas.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        menuAdicionar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        menuAdicionar.setText("Adicionar");
        menuAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAdicionarActionPerformed(evt);
            }
        });
        ferramentas.add(menuAdicionar);

        menuLimpar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        menuLimpar.setText("Limpar");
        menuLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuLimparActionPerformed(evt);
            }
        });
        ferramentas.add(menuLimpar);

        menuAtualizar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        menuAtualizar.setText("Atualizar");
        menuAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAtualizarActionPerformed(evt);
            }
        });
        ferramentas.add(menuAtualizar);

        menuExcluir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        menuExcluir.setText("Excluir");
        menuExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExcluirActionPerformed(evt);
            }
        });
        ferramentas.add(menuExcluir);

        jMenuBar1.add(ferramentas);

        opcoes.setText("Opções");
        opcoes.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuBar1.add(opcoes);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setSize(new java.awt.Dimension(1040, 540));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void menuAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAdicionarActionPerformed
        mod.setNome(nome.getText());
        mod.setCelular(celular.getText());
        mod.setEndereco(endereco.getText());
        control.insertCliente(mod);
        readTableCliente("select * from cliente where idCliente > 0 order by nome");
        menuLimparActionPerformed(evt);
    }//GEN-LAST:event_menuAdicionarActionPerformed

    private void menuLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuLimparActionPerformed
        nome.setText("");
        celular.setText("");
        endereco.setText("");
    }//GEN-LAST:event_menuLimparActionPerformed

    private void menuAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAtualizarActionPerformed
        conexao.conecta();
        conexao.executaSQL("select * from cliente where celular='" + celular.getText() + "'");
        try {
            conexao.rs.first();
            String id = conexao.rs.getString("idCliente");
            mod.setNome(nome.getText());
            mod.setCelular(celular.getText());
            mod.setEndereco(endereco.getText());
            mod.setId(id);
            control.update(mod);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        readTableCliente("select * from cliente where idCliente > 0 order by nome");
        menuLimparActionPerformed(evt);
    }//GEN-LAST:event_menuAtualizarActionPerformed

    private void menuExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuExcluirActionPerformed

        int resposta = JOptionPane.showConfirmDialog(null, "Deseja realmente excluir este cliente?");
        if (resposta == JOptionPane.YES_OPTION) {
            conexao.conecta();
            conexao.executaSQL("select * from cliente");
            try {
                conexao.rs.first();
                String id = conexao.rs.getString("idCliente");
                PreparedStatement stmt = conexao.conn.prepareStatement("delete from cliente where idCliente='" + id + "'");
                stmt.execute();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            conexao.desconecta();
            menuLimparActionPerformed(evt);
            readTableCliente("select * from cliente where idCliente > 0 order by nome");
        }
    }//GEN-LAST:event_menuExcluirActionPerformed

    private void tabelaClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaClienteMouseClicked
        int linha = tabelaCliente.getSelectedRow();
        nome.setText(tabelaCliente.getValueAt(linha, 1).toString());
        celular.setText(tabelaCliente.getValueAt(linha, 2).toString());
        endereco.setText(tabelaCliente.getValueAt(linha, 3).toString());
    }//GEN-LAST:event_tabelaClienteMouseClicked

    private void enderecoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_enderecoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            mod.setNome(nome.getText());
            mod.setCelular(celular.getText());
            mod.setEndereco(endereco.getText());
            control.insertCliente(mod);

            readTableCliente("select * from cliente where idCliente > 0 order by nome");
            nome.setText("");
            celular.setText("");
            endereco.setText("");
        }
    }//GEN-LAST:event_enderecoKeyPressed

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
            java.util.logging.Logger.getLogger(AddClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            AddClientes dialog = new AddClientes(new javax.swing.JFrame(), true);
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
    private javax.swing.JScrollPane barTable;
    private javax.swing.JTextField celular;
    private javax.swing.JTextField endereco;
    private javax.swing.JMenu ferramentas;
    private javax.swing.JTextField idCliente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JMenuItem menuAdicionar;
    private javax.swing.JMenuItem menuAtualizar;
    private javax.swing.JMenuItem menuExcluir;
    private javax.swing.JMenuItem menuLimpar;
    private javax.swing.JTextField nome;
    private javax.swing.JMenu opcoes;
    private javax.swing.JTable tabelaCliente;
    // End of variables declaration//GEN-END:variables
}
