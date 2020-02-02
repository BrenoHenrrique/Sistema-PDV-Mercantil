package viewer;

import modelBeans.Beans;
import modelBeans.focusTraversalPolicy;
import modelBeans.Table;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.table.DefaultTableCellRenderer;
import controlConnetion.conectaBanco;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public final class Fornecedor extends javax.swing.JFrame {

    conectaBanco conexao = new conectaBanco();
    PreparedStatement stmt = null;
    ResultSet rs = null;
    private boolean action = true;

    public Fornecedor() {
        initComponents();
        componentes();
        controlFocus();
        caixaSelecaoCor();
        mostrar();
        tabelaFornecedor.getTableHeader().setDefaultRenderer(new Fornecedor.header());
        readTableFornecedor("select * from fornecedor where idFornecedor > 0");
    }

    public void componentes() {

        botVenda.setVisible(false);
        botProduto.setVisible(false);
        botHistorico.setVisible(false);
        botAgenda.setVisible(false);
        botSite.setVisible(false);
        jtableFornecedor.getViewport().setBackground(Color.white);
        jtableFornecedor.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        getContentPane().setBackground(new Color(64, 64, 64));
    }

    public void caixaSelecaoCor() {

        Object child = caixa.getAccessibleContext().getAccessibleChild(0);
        BasicComboPopup popup = (BasicComboPopup) child;
        JList list = popup.getList();
        list.setSelectionBackground(new Color(64, 64, 64));
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

    public void controlFocus() {
        ArrayList<Component> order = new ArrayList<>(10);
        order.add(this.getContentPane());
        order.add(nome);
        order.add(produto);
        order.add(cnpj);
        order.add(endereco);
        order.add(bairro);
        order.add(celular);
        order.add(telefone);
        order.add(email);
        order.add(pesquisarFornecedor);
        focusTraversalPolicy policy = new focusTraversalPolicy(order);
        setFocusTraversalPolicy(policy);
    }

    public void mostrar() {

        conexao.conecta();
        conexao.executaSQL("SELECT * FROM fornecedor ORDER BY idFornecedor DESC LIMIT 1");
        try {
            conexao.rs.last();
            do {
                idFornecedor.setText(String.valueOf(conexao.rs.getInt("idFornecedor")));
            } while (conexao.rs.next());
        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null, "Erro ao mostrar\n"+ex);
        }
        conexao.desconecta();

        int x = Integer.parseInt(idFornecedor.getText());
        int y = 1;
        int result;
        result = x + y;
        idFornecedor.setText(String.valueOf(result));
    }

    public void readTableFornecedor(String Sql) {
        conexao.conecta();
        ArrayList dados = new ArrayList();
        String[] colunas = new String[]{"ID Fornecedor", "Nome", "Produto", "CNPJ", "Email", "Celular", "Telefone", "Endereço", "Bairro"};
        conexao.executaSQL(Sql);
        try {
            conexao.rs.first();
            do {
                dados.add(new Object[]{
                    conexao.rs.getString("idFornecedor"), conexao.rs.getString("nome"), conexao.rs.getString("produto"),
                    conexao.rs.getString("cnpj"), conexao.rs.getString("email"), conexao.rs.getString("celular"),
                    conexao.rs.getString("telefone"), conexao.rs.getString("endereco"), conexao.rs.getString("bairro")});
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

        tabelaFornecedor.setModel(tabela);
        tabelaFornecedor.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabelaFornecedor.getColumnModel().getColumn(0).setResizable(false);
        tabelaFornecedor.getColumnModel().getColumn(0).setCellRenderer(centralizado);
        tabelaFornecedor.getColumnModel().getColumn(1).setPreferredWidth(300);
        tabelaFornecedor.getColumnModel().getColumn(1).setResizable(false);
        tabelaFornecedor.getColumnModel().getColumn(2).setPreferredWidth(250);
        tabelaFornecedor.getColumnModel().getColumn(2).setResizable(true);
        tabelaFornecedor.getColumnModel().getColumn(2).setCellRenderer(centralizado);
        tabelaFornecedor.getColumnModel().getColumn(3).setPreferredWidth(200);
        tabelaFornecedor.getColumnModel().getColumn(3).setResizable(true);
        tabelaFornecedor.getColumnModel().getColumn(3).setCellRenderer(centralizado);
        tabelaFornecedor.getColumnModel().getColumn(4).setPreferredWidth(350);
        tabelaFornecedor.getColumnModel().getColumn(4).setResizable(true);
        tabelaFornecedor.getColumnModel().getColumn(4).setCellRenderer(centralizado);
        tabelaFornecedor.getColumnModel().getColumn(5).setPreferredWidth(375);
        tabelaFornecedor.getColumnModel().getColumn(5).setResizable(true);
        tabelaFornecedor.getColumnModel().getColumn(5).setCellRenderer(centralizado);
        tabelaFornecedor.getColumnModel().getColumn(6).setPreferredWidth(350);
        tabelaFornecedor.getColumnModel().getColumn(6).setResizable(true);
        tabelaFornecedor.getColumnModel().getColumn(6).setCellRenderer(centralizado);
        tabelaFornecedor.getColumnModel().getColumn(7).setPreferredWidth(350);
        tabelaFornecedor.getColumnModel().getColumn(7).setResizable(true);
        tabelaFornecedor.getColumnModel().getColumn(7).setCellRenderer(centralizado);
        tabelaFornecedor.getColumnModel().getColumn(8).setPreferredWidth(350);
        tabelaFornecedor.getColumnModel().getColumn(8).setResizable(true);
        tabelaFornecedor.getColumnModel().getColumn(8).setCellRenderer(centralizado);
        tabelaFornecedor.getTableHeader().setReorderingAllowed(false);
        tabelaFornecedor.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabelaFornecedor.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
        jtableFornecedor = new javax.swing.JScrollPane();
        tabelaFornecedor = new javax.swing.JTable();
        nome = new javax.swing.JTextField();
        cnpj = new javax.swing.JTextField();
        telefone = new javax.swing.JTextField();
        endereco = new javax.swing.JTextField();
        pesquisarFornecedor = new javax.swing.JTextField();
        bairro = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        celular = new javax.swing.JTextField();
        produto = new javax.swing.JTextField();
        botSite = new javax.swing.JButton();
        botProduto = new javax.swing.JButton();
        botMenu = new javax.swing.JButton();
        botAgenda = new javax.swing.JButton();
        botVenda = new javax.swing.JButton();
        botHistorico = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        idFornecedor = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        botAdd = new javax.swing.JLabel();
        botLimpar = new javax.swing.JLabel();
        botAtualizar = new javax.swing.JLabel();
        botExcluir = new javax.swing.JLabel();
        botPesquisa = new javax.swing.JLabel();
        caixa = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(64, 64, 64));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Cadastro de Fornecedor", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 24))); // NOI18N
        jPanel1.setLayout(null);

        tabelaFornecedor.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tabelaFornecedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabelaFornecedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaFornecedorMouseClicked(evt);
            }
        });
        jtableFornecedor.setViewportView(tabelaFornecedor);

        jPanel1.add(jtableFornecedor);
        jtableFornecedor.setBounds(10, 470, 1000, 210);

        nome.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        nome.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        nome.setDisabledTextColor(java.awt.Color.black);
        jPanel1.add(nome);
        nome.setBounds(80, 140, 350, 35);

        cnpj.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        cnpj.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cnpj.setDisabledTextColor(java.awt.Color.black);
        jPanel1.add(cnpj);
        cnpj.setBounds(80, 270, 350, 35);

        telefone.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        telefone.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        telefone.setDisabledTextColor(java.awt.Color.black);
        jPanel1.add(telefone);
        telefone.setBounds(720, 260, 290, 35);

        endereco.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        endereco.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        endereco.setDisabledTextColor(java.awt.Color.black);
        jPanel1.add(endereco);
        endereco.setBounds(80, 350, 350, 35);

        pesquisarFornecedor.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        pesquisarFornecedor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pesquisarFornecedor.setDisabledTextColor(java.awt.Color.black);
        pesquisarFornecedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pesquisarFornecedorKeyPressed(evt);
            }
        });
        jPanel1.add(pesquisarFornecedor);
        pesquisarFornecedor.setBounds(80, 430, 350, 35);

        bairro.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        bairro.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        bairro.setDisabledTextColor(java.awt.Color.black);
        jPanel1.add(bairro);
        bairro.setBounds(720, 130, 290, 35);

        email.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        email.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        email.setDisabledTextColor(java.awt.Color.black);
        jPanel1.add(email);
        email.setBounds(720, 330, 290, 35);

        celular.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        celular.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        celular.setDisabledTextColor(java.awt.Color.black);
        jPanel1.add(celular);
        celular.setBounds(720, 190, 290, 35);

        produto.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        produto.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        produto.setDisabledTextColor(java.awt.Color.black);
        jPanel1.add(produto);
        produto.setBounds(80, 200, 350, 35);

        botSite.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/site preto 32.png"))); // NOI18N
        botSite.setToolTipText("Site (em breve)");
        botSite.setContentAreaFilled(false);
        botSite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botSiteActionPerformed(evt);
            }
        });
        jPanel1.add(botSite);
        botSite.setBounds(10, 270, 40, 41);

        botProduto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/produto preto 32.png"))); // NOI18N
        botProduto.setToolTipText("Produtos");
        botProduto.setContentAreaFilled(false);
        botProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botProdutoActionPerformed(evt);
            }
        });
        jPanel1.add(botProduto);
        botProduto.setBounds(10, 130, 40, 41);

        botMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/menu 32.png"))); // NOI18N
        botMenu.setToolTipText("Tela de Vendas");
        botMenu.setContentAreaFilled(false);
        botMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botMenuActionPerformed(evt);
            }
        });
        jPanel1.add(botMenu);
        botMenu.setBounds(10, 20, 40, 40);

        botAgenda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/agenda preto 32.png"))); // NOI18N
        botAgenda.setToolTipText("Agenda (em breve)");
        botAgenda.setContentAreaFilled(false);
        botAgenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botAgendaActionPerformed(evt);
            }
        });
        jPanel1.add(botAgenda);
        botAgenda.setBounds(10, 220, 40, 41);

        botVenda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/venda preto 32.png"))); // NOI18N
        botVenda.setToolTipText("Vendas");
        botVenda.setContentAreaFilled(false);
        botVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botVendaActionPerformed(evt);
            }
        });
        jPanel1.add(botVenda);
        botVenda.setBounds(10, 80, 40, 41);

        botHistorico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/historico preto 32.png"))); // NOI18N
        botHistorico.setToolTipText("Histórico");
        botHistorico.setContentAreaFilled(false);
        botHistorico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botHistoricoActionPerformed(evt);
            }
        });
        jPanel1.add(botHistorico);
        botHistorico.setBounds(10, 170, 40, 41);

        jLabel1.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel1.setText("Código");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(80, 40, 56, 22);

        idFornecedor.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        idFornecedor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        idFornecedor.setDisabledTextColor(java.awt.Color.black);
        jPanel1.add(idFornecedor);
        idFornecedor.setBounds(80, 70, 100, 35);

        jLabel2.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel2.setText("Nome Fornecedor");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(80, 110, 138, 22);

        jLabel3.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel3.setText("CNPJ");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(80, 250, 40, 22);

        jLabel4.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel4.setText("Endereço");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(80, 320, 71, 22);

        jLabel5.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel5.setText("Pesquisar Fornecedor");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(80, 400, 167, 22);

        jLabel6.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel6.setText("Produto");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(80, 180, 62, 22);

        jLabel7.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel7.setText("Celular");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(720, 170, 55, 22);

        jLabel8.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel8.setText("Cidade/Bairro");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(720, 100, 107, 22);

        jLabel9.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel9.setText("Telefone");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(720, 240, 67, 22);

        jLabel10.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel10.setText("E-mail");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(720, 300, 49, 22);

        botAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/adicionar 32.png"))); // NOI18N
        botAdd.setToolTipText("Adicionar");
        botAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botAddMouseClicked(evt);
            }
        });
        jPanel1.add(botAdd);
        botAdd.setBounds(860, 430, 32, 32);

        botLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/limpar 32.png"))); // NOI18N
        botLimpar.setToolTipText("Limpar");
        botLimpar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botLimparMouseClicked(evt);
            }
        });
        jPanel1.add(botLimpar);
        botLimpar.setBounds(900, 430, 32, 32);

        botAtualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/atualizar 32.png"))); // NOI18N
        botAtualizar.setToolTipText("Atualizar");
        botAtualizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botAtualizarMouseClicked(evt);
            }
        });
        jPanel1.add(botAtualizar);
        botAtualizar.setBounds(940, 430, 32, 32);

        botExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/lixeira 32.png"))); // NOI18N
        botExcluir.setToolTipText("Excluir");
        botExcluir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botExcluirMouseClicked(evt);
            }
        });
        jPanel1.add(botExcluir);
        botExcluir.setBounds(980, 430, 26, 32);

        botPesquisa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/buscar 32.png"))); // NOI18N
        botPesquisa.setToolTipText("Pesquisar");
        botPesquisa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botPesquisaMouseClicked(evt);
            }
        });
        jPanel1.add(botPesquisa);
        botPesquisa.setBounds(440, 430, 32, 32);

        caixa.setFont(new java.awt.Font("Segoe UI Semilight", 0, 16)); // NOI18N
        caixa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "  Código", "  Nome Fornecedor", "  Produto", "  CNPJ" }));
        caixa.setOpaque(false);
        jPanel1.add(caixa);
        caixa.setBounds(490, 430, 164, 32);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1024, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1040, 739));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tabelaFornecedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaFornecedorMouseClicked

        conexao.conecta();
        try {
            String identificador = "" + tabelaFornecedor.getValueAt(tabelaFornecedor.getSelectedRow(), 0);
            conexao.executaSQL("select * from fornecedor where idFornecedor='" + identificador + "'");
            conexao.rs.first();
            idFornecedor.setText(conexao.rs.getString("idFornecedor"));
            nome.setText(String.valueOf(conexao.rs.getString("nome")));
            produto.setText(String.valueOf(conexao.rs.getString("produto")));
            cnpj.setText(String.valueOf(conexao.rs.getString("cnpj")));
            celular.setText(String.valueOf(conexao.rs.getString("celular")));
            telefone.setText(String.valueOf(conexao.rs.getString("telefone")));
            email.setText(String.valueOf(conexao.rs.getString("email")));
            endereco.setText(String.valueOf(conexao.rs.getString("endereco")));
            bairro.setText(String.valueOf(conexao.rs.getString("bairro")));

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Erro ao selecionar dados!\n" + ex.getMessage());
        }
        conexao.desconecta();
        readTableFornecedor("select * from fornecedor where idFornecedor like '%" + idFornecedor.getText() + "%'");
    }//GEN-LAST:event_tabelaFornecedorMouseClicked

    private void pesquisarFornecedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pesquisarFornecedorKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (caixa.getSelectedItem().equals("  Código")) {
                readTableFornecedor("select * from fornecedor where idFornecedor like '%" + pesquisarFornecedor.getText() + "%'");
            } else if (caixa.getSelectedItem().equals("  Nome Fornecedor")) {
                readTableFornecedor("select * from fornecedor where nome like '%" + pesquisarFornecedor.getText() + "%'");
            } else if (caixa.getSelectedItem().equals("  Produto")) {
                readTableFornecedor("select * from fornecedor where produto like '%" + pesquisarFornecedor.getText() + "%'");
            } else if (caixa.getSelectedItem().equals("  CNPJ")) {
                readTableFornecedor("select * from fornecedor where cnpj like '%" + pesquisarFornecedor.getText() + "%'");
            }
        }
    }//GEN-LAST:event_pesquisarFornecedorKeyPressed

    private void botMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botMenuActionPerformed
        if (action) {
            botVenda.setVisible(true);
            botProduto.setVisible(true);
            botHistorico.setVisible(true);
            botAgenda.setVisible(true);
            botSite.setVisible(true);
        } else {
            botVenda.setVisible(false);
            botProduto.setVisible(false);
            botHistorico.setVisible(false);
            botAgenda.setVisible(false);
            botSite.setVisible(false);
        }
        action = !action;
    }//GEN-LAST:event_botMenuActionPerformed

    private void botAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botAddMouseClicked

        Beans mod = new Beans();
        controlViwer.controlFornecedor fornecedor = new controlViwer.controlFornecedor();

        mod.setIdFornecedor(idFornecedor.getText());
        mod.setNome(nome.getText());
        mod.setProduto(produto.getText());
        mod.setCnpj(cnpj.getText());
        mod.setEmail(email.getText());
        mod.setCelular(celular.getText());
        mod.setTelefone(telefone.getText());
        mod.setEndereco(endereco.getText());
        mod.setBairro(bairro.getText());
        fornecedor.createFornecedor(mod);

        botLimparMouseClicked(evt);
        readTableFornecedor("select * from fornecedor where idFornecedor > 0");
        mostrar();
    }//GEN-LAST:event_botAddMouseClicked

    private void botLimparMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botLimparMouseClicked

        nome.setText("");
        produto.setText("");
        cnpj.setText("");
        celular.setText("");
        telefone.setText("");
        email.setText("");
        endereco.setText("");
        bairro.setText("");
        pesquisarFornecedor.setText("");
        mostrar();
    }//GEN-LAST:event_botLimparMouseClicked

    private void botAtualizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botAtualizarMouseClicked

        Beans mod = new Beans();
        controlViwer.controlFornecedor fornecedor = new controlViwer.controlFornecedor();

        mod.setIdFornecedor(idFornecedor.getText());
        mod.setNome(nome.getText());
        mod.setProduto(produto.getText());
        mod.setCnpj(cnpj.getText());
        mod.setEmail(email.getText());
        mod.setCelular(celular.getText());
        mod.setTelefone(telefone.getText());
        mod.setEndereco(endereco.getText());
        mod.setBairro(bairro.getText());
        fornecedor.update(mod);

        botLimparMouseClicked(evt);
        readTableFornecedor("select * from fornecedor where idFornecedor > 0");
        mostrar();
    }//GEN-LAST:event_botAtualizarMouseClicked

    private void botExcluirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botExcluirMouseClicked
        Beans mod = new Beans();
        controlViwer.controlFornecedor fornecedor = new controlViwer.controlFornecedor();

        int resposta = JOptionPane.showConfirmDialog(rootPane, "Deseja excluir o item?");
        if (resposta == JOptionPane.YES_NO_OPTION) {
            mod.setIdFornecedor(idFornecedor.getText());
            fornecedor.delete(mod);
        }

        botLimparMouseClicked(evt);
        readTableFornecedor("select * from fornecedor where idFornecedor > 0");
        mostrar();
    }//GEN-LAST:event_botExcluirMouseClicked

    private void botPesquisaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botPesquisaMouseClicked
        if (caixa.getSelectedItem().equals("  Código")) {
            readTableFornecedor("select * from fornecedor where idFornecedor like '%" + pesquisarFornecedor.getText() + "%' and idFornecedor > 0");
        } else if (caixa.getSelectedItem().equals("  Nome Fornecedor")) {
            readTableFornecedor("select * from fornecedor where nome like '%" + pesquisarFornecedor.getText() + "%' and idFornecedor > 0");
        } else if (caixa.getSelectedItem().equals("  Produto")) {
            readTableFornecedor("select * from fornecedor where produto like '%" + pesquisarFornecedor.getText() + "%' and idFornecedor > 0");
        } else if (caixa.getSelectedItem().equals("  CNPJ")) {
            readTableFornecedor("select * from fornecedor where cnpj like '%" + pesquisarFornecedor.getText() + "%' and idFornecedor > 0");
        }
    }//GEN-LAST:event_botPesquisaMouseClicked

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

    private void botHistoricoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botHistoricoActionPerformed
        Historico tela = new Historico();
        tela.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_botHistoricoActionPerformed

    private void botAgendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botAgendaActionPerformed
        Agenda tela = new Agenda();
        tela.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_botAgendaActionPerformed

    private void botSiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botSiteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botSiteActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_formWindowOpened

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
            java.util.logging.Logger.getLogger(Fornecedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Fornecedor().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField bairro;
    private javax.swing.JLabel botAdd;
    private javax.swing.JButton botAgenda;
    private javax.swing.JLabel botAtualizar;
    private javax.swing.JLabel botExcluir;
    private javax.swing.JButton botHistorico;
    private javax.swing.JLabel botLimpar;
    private javax.swing.JButton botMenu;
    private javax.swing.JLabel botPesquisa;
    private javax.swing.JButton botProduto;
    private javax.swing.JButton botSite;
    private javax.swing.JButton botVenda;
    private javax.swing.JComboBox<String> caixa;
    private javax.swing.JTextField celular;
    private javax.swing.JTextField cnpj;
    private javax.swing.JTextField email;
    private javax.swing.JTextField endereco;
    private javax.swing.JTextField idFornecedor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jtableFornecedor;
    private javax.swing.JTextField nome;
    private javax.swing.JTextField pesquisarFornecedor;
    private javax.swing.JTextField produto;
    private javax.swing.JTable tabelaFornecedor;
    private javax.swing.JTextField telefone;
    // End of variables declaration//GEN-END:variables
}
