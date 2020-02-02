package viewer;

import controlConnetion.conectaBanco;
import controlViwer.controlAgenda;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import modelBeans.Beans;
import modelBeans.Table;
import modelBeans.focusTraversalPolicy;

public final class Agenda extends javax.swing.JFrame {

    conectaBanco conexao = new conectaBanco();
    private boolean action = true;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String dataBanco;
    String dataViwer;

    public Agenda() {
        initComponents();
        controlFocus();
        tabelaAgenda.getTableHeader().setDefaultRenderer(new Agenda.header());
        readTableAgenda("select * from agenda order by tipo");
        componentes();
    }

    public void componentes() {

        adicionar.setOpaque(true);
        adicionar.setBackground(new Color(000, 141, 063));
        adicionar.setForeground(new Color(255, 255, 255));
        excluir.setOpaque(true);
        excluir.setBackground(new Color(244, 067, 54));
        excluir.setForeground(new Color(255, 255, 255));
        salvar.setOpaque(true);
        salvar.setBackground(new Color(024, 131, 215));
        salvar.setForeground(new Color(255, 255, 255));
        botVenda.setVisible(false);
        botHistorico.setVisible(false);
        botFornecedor.setVisible(false);
        botProduto.setVisible(false);
        botSite.setVisible(false);
        jTableAgenda.getViewport().setBackground(new Color(255, 255, 255));
    }

     public void controlFocus() {
        ArrayList<Component> order = new ArrayList<>(7);
        order.add(getContentPane());
        order.add(hora);
        order.add(tipo);
        order.add(nome);
        order.add(endereco);
        order.add(cidade);
        order.add(celular);
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
            setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            setHorizontalAlignment(JLabel.CENTER);
            return this;
        }
    }

    public void readTableAgenda(String Sql) {
        conexao.conecta();
        ArrayList dados = new ArrayList();
        String[] colunas = new String[]{"ID Agenda", "Tipo", "Nome", "Data", "Hora", "Celular", "Endereço", "Cidade", "Descrição"};
        conexao.executaSQL(Sql);
        try {
            conexao.rs.first();
            do {
                dados.add(new Object[]{
                    conexao.rs.getString("idAgenda"), conexao.rs.getString("tipo"), conexao.rs.getString("nome"),
                    conexao.rs.getString("dia"), conexao.rs.getString("hora"), conexao.rs.getString("celular"),
                    conexao.rs.getString("endereco"), conexao.rs.getString("cidade"), conexao.rs.getString("descricao")});
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

        tabelaAgenda.setModel(tabela);
        tabelaAgenda.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabelaAgenda.getColumnModel().getColumn(0).setResizable(false);
        tabelaAgenda.getColumnModel().getColumn(0).setCellRenderer(centralizado);
        tabelaAgenda.getColumnModel().getColumn(0).setMinWidth(0);
        tabelaAgenda.getColumnModel().getColumn(0).setMaxWidth(0);
        tabelaAgenda.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabelaAgenda.getColumnModel().getColumn(1).setResizable(false);
        tabelaAgenda.getColumnModel().getColumn(2).setPreferredWidth(300);
        tabelaAgenda.getColumnModel().getColumn(2).setResizable(true);
        tabelaAgenda.getColumnModel().getColumn(2).setCellRenderer(centralizado);
        tabelaAgenda.getColumnModel().getColumn(3).setPreferredWidth(150);
        tabelaAgenda.getColumnModel().getColumn(3).setResizable(true);
        tabelaAgenda.getColumnModel().getColumn(3).setCellRenderer(centralizado);
        tabelaAgenda.getColumnModel().getColumn(4).setPreferredWidth(150);
        tabelaAgenda.getColumnModel().getColumn(4).setResizable(true);
        tabelaAgenda.getColumnModel().getColumn(4).setCellRenderer(centralizado);
        tabelaAgenda.getColumnModel().getColumn(5).setPreferredWidth(375);
        tabelaAgenda.getColumnModel().getColumn(5).setResizable(true);
        tabelaAgenda.getColumnModel().getColumn(5).setCellRenderer(centralizado);
        tabelaAgenda.getColumnModel().getColumn(6).setPreferredWidth(350);
        tabelaAgenda.getColumnModel().getColumn(6).setResizable(true);
        tabelaAgenda.getColumnModel().getColumn(6).setCellRenderer(centralizado);
        tabelaAgenda.getColumnModel().getColumn(7).setPreferredWidth(350);
        tabelaAgenda.getColumnModel().getColumn(7).setResizable(true);
        tabelaAgenda.getColumnModel().getColumn(7).setCellRenderer(centralizado);
        tabelaAgenda.getColumnModel().getColumn(8).setPreferredWidth(450);
        tabelaAgenda.getColumnModel().getColumn(8).setResizable(true);
        tabelaAgenda.getColumnModel().getColumn(8).setCellRenderer(centralizado);
        tabelaAgenda.getTableHeader().setReorderingAllowed(false);
        tabelaAgenda.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabelaAgenda.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
        calendario = new com.toedter.calendar.JCalendar();
        adicionar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        data = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        hora = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        endereco = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cidade = new javax.swing.JTextField();
        celular = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        tipo = new javax.swing.JTextField();
        jTableAgenda = new javax.swing.JScrollPane();
        tabelaAgenda = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        nome = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jDesc = new javax.swing.JScrollPane();
        descricao = new javax.swing.JTextArea();
        pesquisar = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        comboPesquisa = new javax.swing.JComboBox<>();
        excluir = new javax.swing.JButton();
        salvar = new javax.swing.JButton();
        botMenu1 = new javax.swing.JButton();
        botVenda = new javax.swing.JButton();
        botHistorico = new javax.swing.JButton();
        botFornecedor = new javax.swing.JButton();
        botProduto = new javax.swing.JButton();
        botSite = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Agenda", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 24))); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        calendario.setBackground(new java.awt.Color(0, 0, 0));
        calendario.setFont(new java.awt.Font("Trebuchet MS", 0, 13)); // NOI18N
        jPanel1.add(calendario, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 230, 350, 180));

        adicionar.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        adicionar.setText("Adicionar");
        adicionar.setContentAreaFilled(false);
        adicionar.setOpaque(true);
        adicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adicionarActionPerformed(evt);
            }
        });
        jPanel1.add(adicionar, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 420, -1, 35));

        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        jLabel1.setText("Dia");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(58, 61, 35, -1));

        data.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        data.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        data.setEnabled(false);
        data.setOpaque(false);
        jPanel1.add(data, new org.netbeans.lib.awtextra.AbsoluteConstraints(58, 92, 110, 36));

        jLabel3.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        jLabel3.setText("Tipo");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(58, 207, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        jLabel4.setText("Horário");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(58, 134, -1, -1));

        hora.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        jPanel1.add(hora, new org.netbeans.lib.awtextra.AbsoluteConstraints(58, 165, 110, 36));

        jLabel5.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        jLabel5.setText("Endereço");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 60, -1, -1));

        endereco.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        jPanel1.add(endereco, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 90, 260, 36));

        jLabel6.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        jLabel6.setText("Cidade/Bairro");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 130, -1, -1));

        cidade.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        jPanel1.add(cidade, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 160, 260, 36));

        celular.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        jPanel1.add(celular, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 230, 260, 36));

        jLabel7.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        jLabel7.setText("Celular");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 200, -1, -1));

        tipo.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        jPanel1.add(tipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(58, 238, 200, 36));

        tabelaAgenda.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tabelaAgenda.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTableAgenda.setViewportView(tabelaAgenda);

        jPanel1.add(jTableAgenda, new org.netbeans.lib.awtextra.AbsoluteConstraints(58, 465, 940, 220));

        jLabel8.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        jLabel8.setText("Nome/Empresa");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(58, 280, -1, -1));

        nome.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        jPanel1.add(nome, new org.netbeans.lib.awtextra.AbsoluteConstraints(58, 311, 200, 36));

        jLabel9.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        jLabel9.setText("Descrição");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 60, -1, -1));

        descricao.setColumns(20);
        descricao.setFont(new java.awt.Font("Monospaced", 2, 13)); // NOI18N
        descricao.setRows(5);
        descricao.setText("Descreva  seu compromisso...");
        descricao.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                descricaoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                descricaoFocusLost(evt);
            }
        });
        jDesc.setViewportView(descricao);

        jPanel1.add(jDesc, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 90, 350, 133));

        pesquisar.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        jPanel1.add(pesquisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(58, 408, 260, 36));

        jLabel10.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        jLabel10.setText("Pesquisa");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(58, 377, -1, -1));

        comboPesquisa.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        comboPesquisa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "  Data", "  Hora", "  Tipo", "  Nome", "  Endereço" }));
        jPanel1.add(comboPesquisa, new org.netbeans.lib.awtextra.AbsoluteConstraints(328, 408, 150, 36));

        excluir.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        excluir.setText("Excluir");
        excluir.setContentAreaFilled(false);
        excluir.setOpaque(true);
        excluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excluirActionPerformed(evt);
            }
        });
        jPanel1.add(excluir, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 420, 93, 35));

        salvar.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        salvar.setText("Salvar");
        salvar.setContentAreaFilled(false);
        salvar.setOpaque(true);
        salvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salvarActionPerformed(evt);
            }
        });
        jPanel1.add(salvar, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 420, 93, 35));

        botMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/menu 32.png"))); // NOI18N
        botMenu1.setToolTipText("Tela de Vendas");
        botMenu1.setContentAreaFilled(false);
        botMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botMenu1ActionPerformed(evt);
            }
        });
        jPanel1.add(botMenu1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 51, 40));

        botVenda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/venda preto 32.png"))); // NOI18N
        botVenda.setToolTipText("Vendas");
        botVenda.setContentAreaFilled(false);
        botVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botVendaActionPerformed(evt);
            }
        });
        jPanel1.add(botVenda, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 61, 50, -1));

        botHistorico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/historico preto 32.png"))); // NOI18N
        botHistorico.setToolTipText("histórico");
        botHistorico.setContentAreaFilled(false);
        botHistorico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botHistoricoActionPerformed(evt);
            }
        });
        jPanel1.add(botHistorico, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 155, 50, -1));

        botFornecedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/fornecedor preto 32.png"))); // NOI18N
        botFornecedor.setToolTipText("Fornecedor");
        botFornecedor.setContentAreaFilled(false);
        botFornecedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botFornecedorActionPerformed(evt);
            }
        });
        jPanel1.add(botFornecedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 207, 50, -1));

        botProduto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/produto preto 32.png"))); // NOI18N
        botProduto.setToolTipText("Agenda (em breve)");
        botProduto.setContentAreaFilled(false);
        botProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botProdutoActionPerformed(evt);
            }
        });
        jPanel1.add(botProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 108, 50, -1));

        botSite.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/site preto 32.png"))); // NOI18N
        botSite.setToolTipText("Site (em breve)");
        botSite.setContentAreaFilled(false);
        botSite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botSiteActionPerformed(evt);
            }
        });
        jPanel1.add(botSite, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 254, 50, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI Semilight", 0, 13)); // NOI18N
        jLabel2.setText("Até 150 caracteres");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 70, 110, -1));

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 0, 0));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("*");
        jLabel11.setToolTipText("Campo Obrigatório");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 240, 10, 30));

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 0, 0));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("*");
        jLabel12.setToolTipText("Campo Obrigatório");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 90, -1, 40));

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 0, 0));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("*");
        jLabel13.setToolTipText("Campo Obrigatório");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 170, 10, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1024, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setSize(new java.awt.Dimension(1040, 739));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void adicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adicionarActionPerformed

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dataBanco = sdf.format(this.calendario.getDate());

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        dataViwer = df.format(this.calendario.getDate());
        data.setText(dataViwer);

    }//GEN-LAST:event_adicionarActionPerformed

    private void excluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_excluirActionPerformed
        Beans mod = new Beans();
        controlAgenda agenda = new controlAgenda();

        for (int i = 0; i < 1; i++) {
            String idAgenda = tabelaAgenda.getValueAt(i, 0).toString();
            
            int resposta = JOptionPane.showConfirmDialog(rootPane, "Deseja excluir o item?");
            if (resposta == JOptionPane.YES_NO_OPTION) {
                mod.setIdAgenda(idAgenda);
                agenda.delete(mod);
            }
        }
        
        readTableAgenda("select * from agenda order by tipo");
    }//GEN-LAST:event_excluirActionPerformed

    private void salvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salvarActionPerformed
        Beans mod = new Beans();
        controlAgenda agenda = new controlAgenda();

        mod.setDia(dataBanco);
        mod.setHora(hora.getText());
        mod.setTipo(tipo.getText());
        mod.setNome(nome.getText());
        mod.setEndereco(endereco.getText());
        mod.setCidade(cidade.getText());
        mod.setCelular(celular.getText());
        mod.setDescricao(descricao.getText());
        agenda.create(mod);

        data.setText("");
        hora.setText("");
        tipo.setText("");
        nome.setText("");
        endereco.setText("");
        cidade.setText("");
        celular.setText("");
        descricao.setText("");

        readTableAgenda("select * from agenda order by tipo");
    }//GEN-LAST:event_salvarActionPerformed

    private void botMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botMenu1ActionPerformed
        if (action) {
            botVenda.setVisible(true);
            botHistorico.setVisible(true);
            botFornecedor.setVisible(true);
            botProduto.setVisible(true);
            botSite.setVisible(true);
        } else {
            botVenda.setVisible(false);
            botHistorico.setVisible(false);
            botFornecedor.setVisible(false);
            botProduto.setVisible(false);
            botSite.setVisible(false);
        }
        action = !action;
    }//GEN-LAST:event_botMenu1ActionPerformed

    private void botVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botVendaActionPerformed
        Venda tela = new Venda();
        tela.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_botVendaActionPerformed

    private void botHistoricoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botHistoricoActionPerformed
        Historico tela = new Historico();
        tela.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_botHistoricoActionPerformed

    private void botFornecedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botFornecedorActionPerformed
        Fornecedor tela = new Fornecedor();
        tela.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_botFornecedorActionPerformed

    private void botProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botProdutoActionPerformed
        Produto tela = new Produto();
        tela.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_botProdutoActionPerformed

    private void botSiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botSiteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botSiteActionPerformed

    private void descricaoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_descricaoFocusGained
        if (descricao.getText().equals("Descreva  seu compromisso...")) {
            descricao.setText("");
            descricao.setFont(new Font("monospaced", Font.PLAIN, 13));
        }
    }//GEN-LAST:event_descricaoFocusGained

    private void descricaoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_descricaoFocusLost
        if (descricao.getText().equals("")) {
            descricao.setText("Descreva  seu compromisso...");
            descricao.setFont(new Font("monospaced", Font.ITALIC, 13));
        }
    }//GEN-LAST:event_descricaoFocusLost

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
            java.util.logging.Logger.getLogger(Agenda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Agenda().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton adicionar;
    private javax.swing.JButton botFornecedor;
    private javax.swing.JButton botHistorico;
    private javax.swing.JButton botMenu1;
    private javax.swing.JButton botProduto;
    private javax.swing.JButton botSite;
    private javax.swing.JButton botVenda;
    private com.toedter.calendar.JCalendar calendario;
    private javax.swing.JTextField celular;
    private javax.swing.JTextField cidade;
    private javax.swing.JComboBox<String> comboPesquisa;
    private javax.swing.JTextField data;
    private javax.swing.JTextArea descricao;
    private javax.swing.JTextField endereco;
    private javax.swing.JButton excluir;
    private javax.swing.JTextField hora;
    private javax.swing.JScrollPane jDesc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jTableAgenda;
    private javax.swing.JTextField nome;
    private javax.swing.JTextField pesquisar;
    private javax.swing.JButton salvar;
    private javax.swing.JTable tabelaAgenda;
    private javax.swing.JTextField tipo;
    // End of variables declaration//GEN-END:variables
}
