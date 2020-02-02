package viewer;

import modelBeans.focusTraversalPolicy;
import modelBeans.Table;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
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
import controlConnetion.conectaBanco;
import controlViwer.controlHistorico;
import java.awt.print.PrinterException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.MessageFormat;
import javax.swing.JOptionPane;
import modelBeans.Beans;

public final class Historico extends javax.swing.JFrame {

    controlHistorico control = new controlHistorico();
    Beans mod = new Beans();
    conectaBanco conexao = new conectaBanco();
    PreparedStatement stmt = null;
    ResultSet rs = null;
    private boolean action = true;
    private String strData1;
    private String strData2;
    String id;
    String totalFormat;

    public Historico() {
        initComponents();
        componentes();
        caixaSelecaoCor();
        controlFocus();
        tabelaHistorico.getTableHeader().setDefaultRenderer(new Fornecedor.header());
        readTableHistorico("select * from historico where idVenda > 0");
    }

    public void componentes() {

        botVenda.setVisible(false);
        botProduto.setVisible(false);
        botFornecedor.setVisible(false);
        botAgenda.setVisible(false);
        botSite.setVisible(false);
        jtableHistorico.getViewport().setBackground(Color.white);
        jtableHistorico.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        total.setText("R$ 0,00");
        data1.setForeground(new Color(255, 255, 255));
        data2.setForeground(new Color(255, 255, 255));
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
        conexao.executaSQL("select * from historico");
        try {
            while (conexao.rs.next()) {
                double somaTotal = 0;
                for (int i = 0; i < tabelaHistorico.getRowCount(); i++) {
                    somaTotal += Double.parseDouble(tabelaHistorico.getValueAt(i, 7).toString().replace(",", "."));
                }
                total.setText("" + somaTotal);

                String resultado = String.format("%.2f", somaTotal); //coloca duas casas decimais
                total.setText("" + resultado);
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
        data1.setText(String.valueOf(strData1));
        data2.setText(String.valueOf(strData2));
    }

    public void statusCaixa() {

        conexao.conecta();
        conexao.executaSQL("select * from caixa");
        try {
            conexao.rs.first();
            String v = conexao.rs.getString("verificador");
            if (v.equals("fechado")) {
                JOptionPane.showMessageDialog(null, "Abra o caixa para ter acesso a está tela!");
                pagamento.setEnabled(false);
            } else {
                FechamentoCaixa tela = new FechamentoCaixa();
                tela.setVisible(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conexao.desconecta();
    }
    
    @SuppressWarnings("CallToPrintStackTrace")
    public void estorno() {
        int linha = tabelaHistorico.getSelectedRow();
        id = tabelaHistorico.getValueAt(linha, 1).toString();

        conexao.conecta();
        conexao.executaSQL("select * from historico where idProduto='" + id + "'");
        try {
            conexao.rs.first();
            String y = conexao.rs.getString("quantVenda");
            String x = conexao.rs.getString("quantAtual");
            Double estorno = Double.parseDouble(x) + Double.parseDouble(y);

            stmt = conexao.conn.prepareStatement("update produto set quantAtual='" + estorno + "' where idProduto='" + id + "'");
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conexao.desconecta();
    }

    public void deleteHistorico() {
        int linha = tabelaHistorico.getSelectedRow();
        String idVend = tabelaHistorico.getValueAt(linha, 0).toString();
        String idProd = tabelaHistorico.getValueAt(linha, 1).toString();

        conexao.conecta();
        conexao.executaSQL("select * from historico");
        try {
            conexao.rs.first();
            stmt = conexao.conn.prepareStatement("delete from historico where idVenda='" + idVend + "' and idProduto='" + idProd + "'");
            stmt.execute();
            readTableHistorico("select * from historico where idVenda > 0");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        conexao.desconecta();
    }

    public void imprimirTabela() {

        MessageFormat header = new MessageFormat("Lista de Produtos");
        MessageFormat footer = new MessageFormat("Lýkos System");

        try {
            tabelaHistorico.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (PrinterException ex) {
            ex.printStackTrace();
        }
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

    public void readTableHistorico(String Sql) {

        conexao.conecta();
        ArrayList dados = new ArrayList();
        String[] colunas = new String[]{"ID Venda", "ID Produto", "Nome", "Preço", "Quantidade Venda", "Data", "Subtotal"};
        conexao.executaSQL(Sql);
        try {
            conexao.rs.first();
            do {
                totalFormat = conexao.rs.getString("total").replace(",", ".");
                String resultado = String.format("%.2f", Double.parseDouble(totalFormat));

                dados.add(new Object[]{
                    conexao.rs.getString("idVenda"), conexao.rs.getString("idProduto"), conexao.rs.getString("nome"),
                    conexao.rs.getString("marca"), conexao.rs.getString("venda"), conexao.rs.getString("quantVenda"),
                    conexao.rs.getString("diaVenda"), resultado});
            } while (conexao.rs.next());
        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(rootPane, "Erro ao gerar a tabela compra!\n" + ex.getMessage());
        }
        Table tabela = new Table(dados, colunas);

        DefaultTableCellRenderer esquerda = new DefaultTableCellRenderer();
        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        DefaultTableCellRenderer direita = new DefaultTableCellRenderer();
        esquerda.setHorizontalAlignment(SwingConstants.LEFT);
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);
        direita.setHorizontalAlignment(SwingConstants.RIGHT);

        tabelaHistorico.setModel(tabela);
        tabelaHistorico.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabelaHistorico.getColumnModel().getColumn(0).setResizable(false);
        tabelaHistorico.getColumnModel().getColumn(0).setCellRenderer(centralizado);
        tabelaHistorico.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabelaHistorico.getColumnModel().getColumn(1).setResizable(false);
        tabelaHistorico.getColumnModel().getColumn(1).setCellRenderer(centralizado);
        tabelaHistorico.getColumnModel().getColumn(2).setPreferredWidth(180);
        tabelaHistorico.getColumnModel().getColumn(2).setResizable(false);
        tabelaHistorico.getColumnModel().getColumn(3).setPreferredWidth(180);
        tabelaHistorico.getColumnModel().getColumn(3).setResizable(false);
        tabelaHistorico.getColumnModel().getColumn(4).setPreferredWidth(180);
        tabelaHistorico.getColumnModel().getColumn(4).setResizable(false);
        tabelaHistorico.getColumnModel().getColumn(4).setCellRenderer(centralizado);
        tabelaHistorico.getColumnModel().getColumn(5).setPreferredWidth(170);
        tabelaHistorico.getColumnModel().getColumn(5).setResizable(false);
        tabelaHistorico.getColumnModel().getColumn(5).setCellRenderer(centralizado);
        tabelaHistorico.getColumnModel().getColumn(6).setPreferredWidth(150);
        tabelaHistorico.getColumnModel().getColumn(6).setResizable(false);
        tabelaHistorico.getColumnModel().getColumn(6).setCellRenderer(centralizado);
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
        botMenu = new javax.swing.JButton();
        botVenda = new javax.swing.JButton();
        botProduto = new javax.swing.JButton();
        botFornecedor = new javax.swing.JButton();
        botAgenda = new javax.swing.JButton();
        botSite = new javax.swing.JButton();
        jtableHistorico = new javax.swing.JScrollPane();
        tabelaHistorico = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        pesquisa = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        caixa = new javax.swing.JComboBox<>();
        botPesquisa = new javax.swing.JButton();
        botAtualizar = new javax.swing.JButton();
        total = new javax.swing.JLabel();
        data1 = new javax.swing.JLabel();
        data2 = new javax.swing.JLabel();
        chooser1 = new com.toedter.calendar.JDateChooser();
        chooser2 = new com.toedter.calendar.JDateChooser();
        pagamento = new javax.swing.JButton();
        deletar = new javax.swing.JButton();
        botImprimir = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        botImprimir1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Histórico de Vendas", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 24))); // NOI18N
        jPanel1.setLayout(null);

        botMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/menu 32.png"))); // NOI18N
        botMenu.setToolTipText("Tela de Vendas");
        botMenu.setContentAreaFilled(false);
        botMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botMenuActionPerformed(evt);
            }
        });
        jPanel1.add(botMenu);
        botMenu.setBounds(10, 42, 40, 40);

        botVenda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/venda preto 32.png"))); // NOI18N
        botVenda.setToolTipText("Vendas");
        botVenda.setContentAreaFilled(false);
        botVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botVendaActionPerformed(evt);
            }
        });
        jPanel1.add(botVenda);
        botVenda.setBounds(10, 112, 40, 41);

        botProduto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/produto preto 32.png"))); // NOI18N
        botProduto.setToolTipText("Produtos");
        botProduto.setContentAreaFilled(false);
        botProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botProdutoActionPerformed(evt);
            }
        });
        jPanel1.add(botProduto);
        botProduto.setBounds(10, 162, 40, 41);

        botFornecedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/fornecedor preto 32.png"))); // NOI18N
        botFornecedor.setToolTipText("Fornecedor");
        botFornecedor.setContentAreaFilled(false);
        botFornecedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botFornecedorActionPerformed(evt);
            }
        });
        jPanel1.add(botFornecedor);
        botFornecedor.setBounds(10, 212, 40, 41);

        botAgenda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/agenda preto 32.png"))); // NOI18N
        botAgenda.setToolTipText("Agenda (em breve)");
        botAgenda.setContentAreaFilled(false);
        botAgenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botAgendaActionPerformed(evt);
            }
        });
        jPanel1.add(botAgenda);
        botAgenda.setBounds(10, 252, 40, 41);

        botSite.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/site preto 32.png"))); // NOI18N
        botSite.setToolTipText("Site (em breve)");
        botSite.setContentAreaFilled(false);
        botSite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botSiteActionPerformed(evt);
            }
        });
        jPanel1.add(botSite);
        botSite.setBounds(10, 302, 40, 41);

        tabelaHistorico.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tabelaHistorico.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabelaHistorico.setGridColor(java.awt.Color.lightGray);
        jtableHistorico.setViewportView(tabelaHistorico);

        jPanel1.add(jtableHistorico);
        jtableHistorico.setBounds(61, 173, 950, 510);

        jLabel5.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel5.setText("Pesquisar");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(60, 101, 110, 22);

        pesquisa.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        pesquisa.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pesquisa.setDisabledTextColor(java.awt.Color.black);
        pesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pesquisaKeyPressed(evt);
            }
        });
        jPanel1.add(pesquisa);
        pesquisa.setBounds(60, 131, 280, 35);

        jLabel1.setFont(new java.awt.Font("Segoe UI Semilight", 1, 22)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("De");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(620, 130, 30, 36);

        jLabel2.setFont(new java.awt.Font("Segoe UI Semilight", 1, 22)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Até");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(810, 130, 40, 36);

        caixa.setFont(new java.awt.Font("Segoe UI Semilight", 0, 16)); // NOI18N
        caixa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "  Nome Produto", "  Marca", "  Código Venda", "  Data Venda" }));
        caixa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                caixaKeyPressed(evt);
            }
        });
        jPanel1.add(caixa);
        caixa.setBounds(60, 60, 142, 36);

        botPesquisa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/buscar 32.png"))); // NOI18N
        botPesquisa.setToolTipText("Pesquisar");
        botPesquisa.setContentAreaFilled(false);
        botPesquisa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botPesquisaMouseClicked(evt);
            }
        });
        jPanel1.add(botPesquisa);
        botPesquisa.setBounds(350, 131, 32, 35);

        botAtualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/atualizar 32.png"))); // NOI18N
        botAtualizar.setToolTipText("Atualizar Tabela");
        botAtualizar.setContentAreaFilled(false);
        botAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botAtualizarActionPerformed(evt);
            }
        });
        jPanel1.add(botAtualizar);
        botAtualizar.setBounds(390, 131, 34, 35);

        total.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        total.setForeground(new java.awt.Color(0, 51, 255));
        total.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        total.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Total", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 18))); // NOI18N
        jPanel1.add(total);
        total.setBounds(780, 30, 220, 70);
        jPanel1.add(data1);
        data1.setBounds(897, 69, 15, 24);
        jPanel1.add(data2);
        data2.setBounds(897, 111, 15, 24);

        chooser1.setDateFormatString("yyyy-MM-dd");
        chooser1.setFont(new java.awt.Font("Segoe UI Symbol", 0, 13)); // NOI18N
        jPanel1.add(chooser1);
        chooser1.setBounds(660, 130, 144, 36);

        chooser2.setDateFormatString("yyyy-MM-dd");
        chooser2.setFont(new java.awt.Font("Segoe UI Symbol", 0, 13)); // NOI18N
        jPanel1.add(chooser2);
        chooser2.setBounds(860, 130, 144, 36);

        pagamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/balanco 35.png"))); // NOI18N
        pagamento.setToolTipText("Balanço");
        pagamento.setContentAreaFilled(false);
        pagamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pagamentoActionPerformed(evt);
            }
        });
        jPanel1.add(pagamento);
        pagamento.setBounds(430, 131, 40, 36);

        deletar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/lixeira 32.png"))); // NOI18N
        deletar.setToolTipText("Balanço");
        deletar.setContentAreaFilled(false);
        deletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletarActionPerformed(evt);
            }
        });
        jPanel1.add(deletar);
        deletar.setBounds(476, 131, 35, 36);

        botImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/imprimir 35.png"))); // NOI18N
        botImprimir.setToolTipText("Imprimir Tabela");
        botImprimir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botImprimirMouseClicked(evt);
            }
        });
        jPanel1.add(botImprimir);
        botImprimir.setBounds(516, 131, 32, 38);

        jLabel3.setText("Quero pesquisar por:");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(60, 40, 102, 14);

        botImprimir1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/grafico 35.png"))); // NOI18N
        botImprimir1.setToolTipText("Imprimir Tabela");
        botImprimir1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botImprimir1MouseClicked(evt);
            }
        });
        jPanel1.add(botImprimir1);
        botImprimir1.setBounds(556, 131, 35, 38);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1024, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1040, 739));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void botPesquisaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botPesquisaMouseClicked
        if (caixa.getSelectedItem().equals("  Nome Produto")) {
            readTableHistorico("select * from historico where nome like '%" + pesquisa.getText() + "%' and idProduto > 0");
            somarValores();

        } else if (caixa.getSelectedItem().equals("  Marca")) {
            readTableHistorico("select * from historico where marca like '%" + pesquisa.getText() + "%' and idProduto > 0");
            somarValores();

        } else if (caixa.getSelectedItem().equals("  Código Venda")) {
            readTableHistorico("select * from historico where idVenda like '%" + pesquisa.getText() + "%' and idProduto > 0");
            somarValores();

        } else if (caixa.getSelectedItem().equals("  Data Venda")) {
            dataFormatada();
            readTableHistorico("select * from historico where diaVenda >='" + data1.getText() + "' and diaVenda <='" + data2.getText() + "' and idProduto > 0");
            somarValores();
        }
    }//GEN-LAST:event_botPesquisaMouseClicked

    private void pesquisaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pesquisaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            if (caixa.getSelectedItem().equals("  Nome Produto")) {
                readTableHistorico("select * from historico where nome like '%" + pesquisa.getText() + "%' and idProduto > 0");
                somarValores();

            } else if (caixa.getSelectedItem().equals("  Marca")) {
                readTableHistorico("select * from historico where marca like '%" + pesquisa.getText() + "%' and idProduto > 0");
                somarValores();

            } else if (caixa.getSelectedItem().equals("  Código Venda")) {
                readTableHistorico("select * from historico where idVenda like '%" + pesquisa.getText() + "%' and idProduto > 0");
                somarValores();
            }
        }
    }//GEN-LAST:event_pesquisaKeyPressed

    private void botMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botMenuActionPerformed
        if (action) {
            botVenda.setVisible(true);
            botProduto.setVisible(true);
            botFornecedor.setVisible(true);
            botAgenda.setVisible(true);
            botSite.setVisible(true);
        } else {
            botVenda.setVisible(false);
            botProduto.setVisible(false);
            botFornecedor.setVisible(false);
            botAgenda.setVisible(false);
            botSite.setVisible(false);
        }
        action = !action;
    }//GEN-LAST:event_botMenuActionPerformed

    private void botVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botVendaActionPerformed
        Venda tela = new Venda();
        tela.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_botVendaActionPerformed

    private void botProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botProdutoActionPerformed
        Produto tela = new Produto();
        tela.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_botProdutoActionPerformed

    private void botFornecedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botFornecedorActionPerformed
        Fornecedor tela = new Fornecedor();
        tela.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_botFornecedorActionPerformed

    private void botAgendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botAgendaActionPerformed
        Agenda tela = new Agenda();
        tela.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_botAgendaActionPerformed

    private void botSiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botSiteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botSiteActionPerformed

    private void caixaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_caixaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            if (caixa.getSelectedItem().equals("  Nome Produto")) {
                readTableHistorico("select * from historico where nome like '%" + pesquisa.getText() + "%' and idProduto > 0");
                somarValores();

            } else if (caixa.getSelectedItem().equals("  Marca")) {
                readTableHistorico("select * from historico where marca like '%" + pesquisa.getText() + "%' and idProduto > 0");
                somarValores();

            } else if (caixa.getSelectedItem().equals("  Código Venda")) {
                readTableHistorico("select * from historico where idVenda like '%" + pesquisa.getText() + "%' and idProduto > 0");
                somarValores();
            } else if (caixa.getSelectedItem().equals("  Data Venda")) {
                dataFormatada();
                readTableHistorico("select * from historico where diaVenda >='" + data1.getText() + "' and diaVenda <='" + data2.getText() + "' and idProduto > 0");
                somarValores();
            }
        }
    }//GEN-LAST:event_caixaKeyPressed

    private void botAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botAtualizarActionPerformed
        readTableHistorico("select * from historico where idVenda > 0");
    }//GEN-LAST:event_botAtualizarActionPerformed

    private void pagamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pagamentoActionPerformed
        statusCaixa();
    }//GEN-LAST:event_pagamentoActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_formWindowOpened

    private void deletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletarActionPerformed
        int resposta = 0;
        JOptionPane.showConfirmDialog(rootPane, "Tem certeza que deseja excluir este item?");
        switch (resposta) {
            case JOptionPane.YES_OPTION:
                estorno();
                deleteHistorico();
                break;
            case JOptionPane.NO_OPTION:
                break;
            case JOptionPane.CANCEL_OPTION:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_deletarActionPerformed

    private void botImprimirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botImprimirMouseClicked
        imprimirTabela();
    }//GEN-LAST:event_botImprimirMouseClicked

    private void botImprimir1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botImprimir1MouseClicked
        new Grafico(this, true).setVisible(true);
    }//GEN-LAST:event_botImprimir1MouseClicked

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
            java.util.logging.Logger.getLogger(Historico.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Historico().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botAgenda;
    private javax.swing.JButton botAtualizar;
    private javax.swing.JButton botFornecedor;
    private javax.swing.JLabel botImprimir;
    private javax.swing.JLabel botImprimir1;
    private javax.swing.JButton botMenu;
    private javax.swing.JButton botPesquisa;
    private javax.swing.JButton botProduto;
    private javax.swing.JButton botSite;
    private javax.swing.JButton botVenda;
    private javax.swing.JComboBox<String> caixa;
    private com.toedter.calendar.JDateChooser chooser1;
    private com.toedter.calendar.JDateChooser chooser2;
    private javax.swing.JLabel data1;
    private javax.swing.JLabel data2;
    private javax.swing.JButton deletar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jtableHistorico;
    private javax.swing.JButton pagamento;
    private javax.swing.JTextField pesquisa;
    private javax.swing.JTable tabelaHistorico;
    private javax.swing.JLabel total;
    // End of variables declaration//GEN-END:variables
}
