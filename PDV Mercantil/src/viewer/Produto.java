package viewer;

import modelBeans.Beans;
import modelBeans.focusTraversalPolicy;
import modelBeans.Table;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
import controlViwer.controlProduto;
import java.awt.print.PrinterException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.MessageFormat;
import modelBeans.modelNumeros;

public final class Produto extends javax.swing.JFrame {

    conectaBanco conexao = new conectaBanco();
    private boolean action = true;
    String caminho;
    int valorTotal;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    public Produto() {
        initComponents();
        componentes();
        controlFocus();
        caixaSelecaoCor();
        data.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis())));
        tabelaProduto.getTableHeader().setDefaultRenderer(new Fornecedor.header());
        readTableProduto("select * from produto where idProduto > 0 order by nome");
    }

    public void componentes() {

        id.setForeground(Color.white);
        botVenda.setVisible(false);
        botHistorico.setVisible(false);
        botFornecedor.setVisible(false);
        botAgenda.setVisible(false);
        botSite.setVisible(false);
        jtableProduto.getViewport().setBackground(Color.white);
        jtableProduto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        idProduto.setDocument(new modelNumeros());
    }

    public void caixaSelecaoCor() {

        Object child = caixa.getAccessibleContext().getAccessibleChild(0);
        BasicComboPopup popup = (BasicComboPopup) child;
        JList list = popup.getList();
        list.setSelectionBackground(new Color(064, 064, 064));
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
        ArrayList<Component> order = new ArrayList<>(7);
        order.add(idProduto);
        order.add(nome);
        order.add(marca);
        order.add(compra);
        order.add(venda);
        order.add(quantAtual);
        order.add(pesquisarProduto);
        focusTraversalPolicy policy = new focusTraversalPolicy(order);
        setFocusTraversalPolicy(policy);
    }

    public void lucro() {

        String strCompra = compra.getText().replace("R$", "").replace(",", ".");
        String strVenda = venda.getText().replace("R$", "").replace(",", ".");

        float porcent = (Float.parseFloat(strVenda) - Float.parseFloat(strCompra));
        porcentagem.setText(String.valueOf(porcent));

        float valor = Float.parseFloat(String.valueOf(porcent));
        DecimalFormat df = new DecimalFormat("##,##0.00");
        String s = df.format(valor);
        porcentagem.setText("R$ " + s);
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void imprimirTabela() {

        MessageFormat header = new MessageFormat("Lista de Produtos");
        MessageFormat footer = new MessageFormat("Lýkos System");

        try {
            tabelaProduto.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (PrinterException ex) {
            ex.printStackTrace();
        }
    }

    public void valorEstoque() {

        conexao.conecta();
        conexao.executaSQL("select * from produto");
        try {
            while (conexao.rs.next()) {
                String x = conexao.rs.getString("quantAtual");
                String y = conexao.rs.getString("venda").replace("R$", "").replace(",", ".");
                valorTotal += Float.parseFloat(x) * Float.parseFloat(y);
            }

            float valor = Float.parseFloat(String.valueOf(valorTotal));
            DecimalFormat df = new DecimalFormat("##,##0.00");
            String s = df.format(valor);
            //total.setText("R$ " + s);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        conexao.desconecta();
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void readTableProduto(String Sql) {
        conexao.conecta();
        ArrayList dados = new ArrayList();
        String[] colunas = new String[]{"ID", "ID Produto", "Nome", "Marca", "Quantidade", "Preço Compra", "Preço Venda", "Taxa de Lucro", "Data", "Foto"};
        conexao.executaSQL(Sql);
        try {
            conexao.rs.first();
            do {
                dados.add(new Object[]{
                    conexao.rs.getString("id"), conexao.rs.getString("idProduto"), conexao.rs.getString("nome"),
                    conexao.rs.getString("marca"), conexao.rs.getString("quantAtual"), conexao.rs.getString("compra"),
                    conexao.rs.getString("venda"), conexao.rs.getString("porcentagem"), conexao.rs.getString("diaCompra"),
                    conexao.rs.getSQLXML("foto")});
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

        tabelaProduto.setModel(tabela);
        tabelaProduto.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabelaProduto.getColumnModel().getColumn(0).setResizable(false);
        tabelaProduto.getColumnModel().getColumn(0).setCellRenderer(centralizado);
        tabelaProduto.getColumnModel().getColumn(0).setMinWidth(0);
        tabelaProduto.getColumnModel().getColumn(0).setMaxWidth(0);
        tabelaProduto.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabelaProduto.getColumnModel().getColumn(1).setResizable(false);
        tabelaProduto.getColumnModel().getColumn(1).setCellRenderer(centralizado);
        tabelaProduto.getColumnModel().getColumn(2).setPreferredWidth(450);
        tabelaProduto.getColumnModel().getColumn(2).setResizable(false);
        tabelaProduto.getColumnModel().getColumn(3).setPreferredWidth(125);
        tabelaProduto.getColumnModel().getColumn(3).setResizable(false);
        tabelaProduto.getColumnModel().getColumn(4).setPreferredWidth(127);
        tabelaProduto.getColumnModel().getColumn(4).setResizable(false);
        tabelaProduto.getColumnModel().getColumn(4).setCellRenderer(centralizado);
        tabelaProduto.getColumnModel().getColumn(5).setPreferredWidth(115);
        tabelaProduto.getColumnModel().getColumn(5).setResizable(false);
        tabelaProduto.getColumnModel().getColumn(5).setCellRenderer(centralizado);
        tabelaProduto.getColumnModel().getColumn(6).setPreferredWidth(127);
        tabelaProduto.getColumnModel().getColumn(6).setResizable(false);
        tabelaProduto.getColumnModel().getColumn(6).setCellRenderer(centralizado);
        tabelaProduto.getColumnModel().getColumn(7).setPreferredWidth(127);
        tabelaProduto.getColumnModel().getColumn(7).setResizable(false);
        tabelaProduto.getColumnModel().getColumn(7).setCellRenderer(centralizado);
        tabelaProduto.getColumnModel().getColumn(8).setPreferredWidth(128);
        tabelaProduto.getColumnModel().getColumn(8).setResizable(false);
        tabelaProduto.getColumnModel().getColumn(8).setCellRenderer(centralizado);
        tabelaProduto.getColumnModel().getColumn(9).setPreferredWidth(128);
        tabelaProduto.getColumnModel().getColumn(9).setResizable(false);
        tabelaProduto.getColumnModel().getColumn(9).setCellRenderer(centralizado);
        tabelaProduto.getColumnModel().getColumn(9).setMinWidth(0);
        tabelaProduto.getColumnModel().getColumn(9).setMaxWidth(0);
        tabelaProduto.getTableHeader().setReorderingAllowed(false);
        tabelaProduto.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabelaProduto.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
        botVenda = new javax.swing.JButton();
        botMenu = new javax.swing.JButton();
        botHistorico = new javax.swing.JButton();
        botFornecedor = new javax.swing.JButton();
        botAgenda = new javax.swing.JButton();
        botSite = new javax.swing.JButton();
        nome = new javax.swing.JTextField();
        pesquisarProduto = new javax.swing.JTextField();
        jtableProduto = new javax.swing.JScrollPane();
        tabelaProduto = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        compra = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        venda = new javax.swing.JTextField();
        quantAtual = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        data = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        caixa = new javax.swing.JComboBox<>();
        botPesquisarProdutos = new javax.swing.JLabel();
        botExcluir = new javax.swing.JLabel();
        botAtualizar = new javax.swing.JLabel();
        botLimpar = new javax.swing.JLabel();
        botAdd = new javax.swing.JLabel();
        botSituacao = new javax.swing.JLabel();
        porcentagem = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        marca = new javax.swing.JTextField();
        idProduto = new javax.swing.JTextField();
        id = new javax.swing.JLabel();
        botImprimir = new javax.swing.JLabel();
        botGerar = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuAdd = new javax.swing.JMenuItem();
        menuLimpar = new javax.swing.JMenuItem();
        menuGerar = new javax.swing.JMenuItem();
        menuSituacao = new javax.swing.JMenuItem();
        menuExcluir = new javax.swing.JMenuItem();
        menuAtualizar = new javax.swing.JMenuItem();
        menuAdd1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastro de Produtos", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 24))); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botVenda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/venda preto 32.png"))); // NOI18N
        botVenda.setToolTipText("Vendas");
        botVenda.setContentAreaFilled(false);
        botVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botVendaActionPerformed(evt);
            }
        });
        jPanel1.add(botVenda, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 40, -1));

        botMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/menu 32.png"))); // NOI18N
        botMenu.setToolTipText("Tela de Vendas");
        botMenu.setContentAreaFilled(false);
        botMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botMenuActionPerformed(evt);
            }
        });
        jPanel1.add(botMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 40, 40));

        botHistorico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/historico preto 32.png"))); // NOI18N
        botHistorico.setToolTipText("histórico");
        botHistorico.setContentAreaFilled(false);
        botHistorico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botHistoricoActionPerformed(evt);
            }
        });
        jPanel1.add(botHistorico, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 40, -1));

        botFornecedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/fornecedor preto 32.png"))); // NOI18N
        botFornecedor.setToolTipText("Fornecedor");
        botFornecedor.setContentAreaFilled(false);
        botFornecedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botFornecedorActionPerformed(evt);
            }
        });
        jPanel1.add(botFornecedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 40, -1));

        botAgenda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/agenda preto 32.png"))); // NOI18N
        botAgenda.setToolTipText("Agenda (em breve)");
        botAgenda.setContentAreaFilled(false);
        botAgenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botAgendaActionPerformed(evt);
            }
        });
        jPanel1.add(botAgenda, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 40, -1));

        botSite.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/site preto 32.png"))); // NOI18N
        botSite.setToolTipText("Site (em breve)");
        botSite.setContentAreaFilled(false);
        botSite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botSiteActionPerformed(evt);
            }
        });
        jPanel1.add(botSite, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 40, -1));

        nome.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        nome.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        nome.setDisabledTextColor(java.awt.Color.black);
        nome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nomeKeyPressed(evt);
            }
        });
        jPanel1.add(nome, new org.netbeans.lib.awtextra.AbsoluteConstraints(253, 130, 475, 35));

        pesquisarProduto.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        pesquisarProduto.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pesquisarProduto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pesquisarProdutoKeyPressed(evt);
            }
        });
        jPanel1.add(pesquisarProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 295, 350, 35));

        tabelaProduto.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tabelaProduto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabelaProduto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaProdutoMouseClicked(evt);
            }
        });
        jtableProduto.setViewportView(tabelaProduto);

        jPanel1.add(jtableProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(74, 336, 940, 330));

        jLabel6.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel6.setText("Nome Produto");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(253, 102, 121, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel8.setText("Código");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 102, 80, -1));

        compra.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        compra.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        compra.setDisabledTextColor(java.awt.Color.black);
        compra.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                compraFocusLost(evt);
            }
        });
        compra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                compraKeyPressed(evt);
            }
        });
        jPanel1.add(compra, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 204, 160, 35));

        jLabel7.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel7.setText("Preço de Compra");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 179, 160, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel9.setText("Taxa de Lucro R$");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(431, 176, 160, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel10.setText("Preço Venda");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(253, 176, 119, -1));

        venda.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        venda.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        venda.setDisabledTextColor(java.awt.Color.black);
        venda.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                vendaFocusLost(evt);
            }
        });
        venda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                vendaKeyPressed(evt);
            }
        });
        jPanel1.add(venda, new org.netbeans.lib.awtextra.AbsoluteConstraints(253, 204, 160, 35));

        quantAtual.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        quantAtual.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        quantAtual.setDisabledTextColor(java.awt.Color.black);
        quantAtual.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                quantAtualKeyPressed(evt);
            }
        });
        jPanel1.add(quantAtual, new org.netbeans.lib.awtextra.AbsoluteConstraints(609, 204, 160, 35));

        jLabel11.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel11.setText("Quantidade/Peso Kg");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(609, 176, 175, -1));

        data.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        data.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(data, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 204, 131, 35));

        jLabel12.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel12.setText("Data");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 176, 58, -1));

        jLabel13.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel13.setText("Pesquisa");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(74, 264, -1, -1));

        caixa.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        caixa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "  Nome", "  Marca", "  Código Produto", "  Quantidade", "  Preço de Venda", "  Preço de Compra", "  Lucro", " " }));
        jPanel1.add(caixa, new org.netbeans.lib.awtextra.AbsoluteConstraints(525, 292, 200, 38));

        botPesquisarProdutos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/buscar 32.png"))); // NOI18N
        botPesquisarProdutos.setToolTipText("Pesquisar");
        botPesquisarProdutos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botPesquisarProdutosMouseClicked(evt);
            }
        });
        jPanel1.add(botPesquisarProdutos, new org.netbeans.lib.awtextra.AbsoluteConstraints(435, 295, -1, 35));

        botExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/lixeira 32.png"))); // NOI18N
        botExcluir.setToolTipText("Excluir");
        botExcluir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botExcluirMouseClicked(evt);
            }
        });
        jPanel1.add(botExcluir, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 300, -1, 38));

        botAtualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/atualizar 32.png"))); // NOI18N
        botAtualizar.setToolTipText("Atualizar");
        botAtualizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botAtualizarMouseClicked(evt);
            }
        });
        jPanel1.add(botAtualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(477, 292, -1, 38));

        botLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/limpar 32.png"))); // NOI18N
        botLimpar.setToolTipText("Limpar");
        botLimpar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botLimparMouseClicked(evt);
            }
        });
        jPanel1.add(botLimpar, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 300, 30, 38));

        botAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/adicionar 32.png"))); // NOI18N
        botAdd.setToolTipText("Adicionar");
        botAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botAddMouseClicked(evt);
            }
        });
        jPanel1.add(botAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 300, 30, 40));

        botSituacao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/situacao 32.png"))); // NOI18N
        botSituacao.setToolTipText("Situação");
        botSituacao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botSituacaoMouseClicked(evt);
            }
        });
        jPanel1.add(botSituacao, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 300, -1, 38));

        porcentagem.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        porcentagem.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        porcentagem.setDisabledTextColor(java.awt.Color.black);
        porcentagem.setEnabled(false);
        porcentagem.setOpaque(false);
        porcentagem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                porcentagemKeyPressed(evt);
            }
        });
        jPanel1.add(porcentagem, new org.netbeans.lib.awtextra.AbsoluteConstraints(431, 204, 160, 35));

        jLabel14.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel14.setText("Marca");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(746, 102, 58, -1));

        marca.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        marca.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        marca.setDisabledTextColor(java.awt.Color.black);
        marca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                marcaKeyPressed(evt);
            }
        });
        jPanel1.add(marca, new org.netbeans.lib.awtextra.AbsoluteConstraints(746, 130, 175, 35));

        idProduto.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        idProduto.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        idProduto.setDisabledTextColor(java.awt.Color.black);
        idProduto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                idProdutoKeyPressed(evt);
            }
        });
        jPanel1.add(idProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 130, 160, 35));

        id.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        jPanel1.add(id, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 32, 50, 35));

        botImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/imprimir 35.png"))); // NOI18N
        botImprimir.setToolTipText("Imprimir Tabela");
        botImprimir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botImprimirMouseClicked(evt);
            }
        });
        jPanel1.add(botImprimir, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 300, -1, 38));

        botGerar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/barras 32.png"))); // NOI18N
        botGerar.setToolTipText("Gerar Código");
        botGerar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botGerarMouseClicked(evt);
            }
        });
        jPanel1.add(botGerar, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 300, 30, 38));

        jMenu1.setText("Ferramentas");

        menuAdd.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        menuAdd.setText("Adicionar");
        menuAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddActionPerformed(evt);
            }
        });
        jMenu1.add(menuAdd);

        menuLimpar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        menuLimpar.setText("Limpar");
        menuLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuLimparActionPerformed(evt);
            }
        });
        jMenu1.add(menuLimpar);

        menuGerar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        menuGerar.setText("Gerar Código");
        menuGerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuGerarActionPerformed(evt);
            }
        });
        jMenu1.add(menuGerar);

        menuSituacao.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
        menuSituacao.setText("Situação");
        menuSituacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSituacaoActionPerformed(evt);
            }
        });
        jMenu1.add(menuSituacao);

        menuExcluir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        menuExcluir.setText("Excluir");
        menuExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExcluirActionPerformed(evt);
            }
        });
        jMenu1.add(menuExcluir);

        menuAtualizar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        menuAtualizar.setText("Atualizar");
        menuAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAtualizarActionPerformed(evt);
            }
        });
        jMenu1.add(menuAtualizar);

        menuAdd1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        menuAdd1.setText("Imprimir");
        menuAdd1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAdd1ActionPerformed(evt);
            }
        });
        jMenu1.add(menuAdd1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Opções");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

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

    private void botMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botMenuActionPerformed
        if (action) {
            botVenda.setVisible(true);
            botHistorico.setVisible(true);
            botFornecedor.setVisible(true);
            botAgenda.setVisible(true);
            botSite.setVisible(true);
        } else {
            botVenda.setVisible(false);
            botHistorico.setVisible(false);
            botFornecedor.setVisible(false);
            botAgenda.setVisible(false);
            botSite.setVisible(false);
        }
        action = !action;
    }//GEN-LAST:event_botMenuActionPerformed

    @SuppressWarnings("CallToPrintStackTrace")
    private void tabelaProdutoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaProdutoMouseClicked
        conexao.conecta();
        try {
            String identificador = "" + tabelaProduto.getValueAt(tabelaProduto.getSelectedRow(), 0);
            conexao.executaSQL("select * from produto where id='" + identificador + "'");
            conexao.rs.first();
            id.setText(conexao.rs.getString("id"));
            idProduto.setText(conexao.rs.getString("idProduto"));
            nome.setText(String.valueOf(conexao.rs.getString("nome")));
            marca.setText(String.valueOf(conexao.rs.getString("marca")));
            quantAtual.setText(String.valueOf(conexao.rs.getString("quantAtual")));
            compra.setText(String.valueOf(conexao.rs.getString("compra")));
            venda.setText(String.valueOf(conexao.rs.getString("venda")));
            porcentagem.setText(String.valueOf(conexao.rs.getString("porcentagem")));
            data.setText(String.valueOf(conexao.rs.getString("diaCompra")));
            /**
             * String strCaminho = conexao.rs.getString("foto"); ImageIcon i =
             * new ImageIcon(new
             * ImageIcon(strCaminho).getImage().getScaledInstance(foto.getWidth(),
             * foto.getHeight(), Image.SCALE_SMOOTH)); foto.setIcon(i);*
             */
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        conexao.desconecta();
        readTableProduto("select * from produto where idProduto like '%" + idProduto.getText() + "%'");
    }//GEN-LAST:event_tabelaProdutoMouseClicked

    private void quantAtualKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_quantAtualKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            Beans mod = new Beans();
            controlProduto produto = new controlProduto();

            mod.setIdProduto(idProduto.getText());
            mod.setNome(nome.getText());
            mod.setMarca(marca.getText());
            mod.setQuantAtual(quantAtual.getText().replace(",", "."));
            mod.setCompra(compra.getText().replace("R$", "").replace(",", "."));
            mod.setPorcentagem(porcentagem.getText());
            mod.setVenda(venda.getText().replace("R$", "").replace(",", "."));
            mod.setDia(data.getText());
            mod.setCaminho(caminho);
            produto.createProduto(mod);

            nome.setText("");
            idProduto.setText("");
            compra.setText("");
            quantAtual.setText("");
            venda.setText("");
            pesquisarProduto.setText("");
            porcentagem.setText("");
            marca.setText("");

            readTableProduto("select * from produto where idProduto > 0 order by nome");
            idProduto.grabFocus();
        }

        if (evt.getKeyCode() == KeyEvent.VK_A && evt.isControlDown()) {
            idProdutoKeyPressed(evt);
        }
    }//GEN-LAST:event_quantAtualKeyPressed

    private void pesquisarProdutoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pesquisarProdutoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            if (caixa.getSelectedItem().equals("  Nome")) {
                readTableProduto("select * from produto where nome like '%" + pesquisarProduto.getText() + "%' and idProduto > 0 order by nome");

            } else if (caixa.getSelectedItem().equals("  Código Produto")) {
                readTableProduto("select * from produto where idProduto like '%" + pesquisarProduto.getText() + "%' and idProduto > 0 order by nome");

            } else if (caixa.getSelectedItem().equals("  Marca")) {
                readTableProduto("select * from produto where marca like '%" + pesquisarProduto.getText() + "%' and idProduto > 0 order by nome");

            } else if (caixa.getSelectedItem().equals("  Quantidade")) {
                readTableProduto("select * from produto where quantAtual like '%" + pesquisarProduto.getText() + "%' and idProduto > 0 order by nome");

            } else if (caixa.getSelectedItem().equals("  Preço de Venda")) {
                readTableProduto("select * from produto where venda like '%" + pesquisarProduto.getText() + "%' and idProduto > 0 order by nome");

            } else if (caixa.getSelectedItem().equals("  Preço Compra")) {
                readTableProduto("select * from produto where Compra like '%" + pesquisarProduto.getText() + "%' and idProduto > 0 order by nome");

            } else if (caixa.getSelectedItem().equals("  Lucro")) {
                readTableProduto("select * from produto where porcentagem like '%" + pesquisarProduto.getText() + "%' and idProduto > 0 order by nome");

            }
        }
    }//GEN-LAST:event_pesquisarProdutoKeyPressed

    private void botPesquisarProdutosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botPesquisarProdutosMouseClicked

        if (caixa.getSelectedItem().equals("  Nome")) {
            readTableProduto("select * from produto where nome like '%" + pesquisarProduto.getText() + "%' and idProduto > 0 order by nome");

        } else if (caixa.getSelectedItem().equals("  Código Produto")) {
            readTableProduto("select * from produto where idProduto like '%" + pesquisarProduto.getText() + "%' and idProduto > 0 order by nome");

        } else if (caixa.getSelectedItem().equals("  Marca")) {
            readTableProduto("select * from produto where marca like '%" + pesquisarProduto.getText() + "%' and idProduto > 0 order by nome");

        } else if (caixa.getSelectedItem().equals("  Quantidade")) {
            readTableProduto("select * from produto where quantAtual like '%" + pesquisarProduto.getText() + "%' and idProduto > 0 order by nome");

        } else if (caixa.getSelectedItem().equals("  Preço de Venda")) {
            readTableProduto("select * from produto where venda like '%" + pesquisarProduto.getText() + "%' and idProduto > 0 order by nome");

        } else if (caixa.getSelectedItem().equals("  Preço Compra")) {
            readTableProduto("select * from produto where Compra like '%" + pesquisarProduto.getText() + "%' and idProduto > 0 order by nome");

        } else if (caixa.getSelectedItem().equals("  Lucro")) {
            readTableProduto("select * from produto where porcentagem like '%" + pesquisarProduto.getText() + "%' and idProduto > 0 order by nome");

        }
    }//GEN-LAST:event_botPesquisarProdutosMouseClicked

    private void botExcluirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botExcluirMouseClicked

        Beans mod = new Beans();
        controlProduto produto = new controlProduto();

        int resposta = JOptionPane.showConfirmDialog(rootPane, "Deseja excluir o item?");
        if (resposta == JOptionPane.YES_NO_OPTION) {
            mod.setId(id.getText());
            produto.delete(mod);
        }

        botLimparMouseClicked(evt);
        data.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis())));
        idProduto.grabFocus();
        readTableProduto("select * from produto where idProduto > 0 order by nome");
    }//GEN-LAST:event_botExcluirMouseClicked

    private void botAtualizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botAtualizarMouseClicked

        readTableProduto("select * from produto where idProduto > 0 order by nome");

    }//GEN-LAST:event_botAtualizarMouseClicked

    private void botLimparMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botLimparMouseClicked

        nome.setText("");
        compra.setText("");
        quantAtual.setText("");
        venda.setText("");
        pesquisarProduto.setText("");
        porcentagem.setText("");
        idProduto.setText("");
        marca.setText("");
        data.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis())));
        idProduto.grabFocus();
    }//GEN-LAST:event_botLimparMouseClicked

    private void botAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botAddMouseClicked

        Beans mod = new Beans();
        controlProduto produto = new controlProduto();

        mod.setIdProduto(idProduto.getText());
        mod.setNome(nome.getText());
        mod.setMarca(marca.getText());
        mod.setQuantAtual(quantAtual.getText().replace(",", "."));
        mod.setCompra(compra.getText().replace("R$", "").replace(",", "."));
        mod.setPorcentagem(porcentagem.getText());
        mod.setVenda(venda.getText().replace("R$", "").replace(",", "."));
        mod.setDia(data.getText());
        mod.setCaminho(caminho);
        produto.createProduto(mod);

        nome.setText("");
        idProduto.setText("");
        compra.setText("");
        quantAtual.setText("");
        venda.setText("");
        pesquisarProduto.setText("");
        porcentagem.setText("");
        marca.setText("");

        readTableProduto("select * from produto where idProduto > 0 order by nome");
        idProduto.grabFocus();

    }//GEN-LAST:event_botAddMouseClicked

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

    private void botAgendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botAgendaActionPerformed
        Agenda tela = new Agenda();
        tela.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_botAgendaActionPerformed

    private void botSiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botSiteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botSiteActionPerformed

    private void botSituacaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botSituacaoMouseClicked

        readTableProduto("select * from produto where quantAtual <= 5 and idProduto > 0 order by nome");

    }//GEN-LAST:event_botSituacaoMouseClicked

    private void vendaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_vendaFocusLost
        String valorVenda = venda.getText().replace(",", ".");
        double x = Double.parseDouble(String.valueOf(valorVenda));
        DecimalFormat df = new DecimalFormat("##,##0.00");
        String t = df.format(x);
        venda.setText("R$ " + t);
        lucro();
    }//GEN-LAST:event_vendaFocusLost

    private void porcentagemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_porcentagemKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            String strCompra = compra.getText().replace("R$", "").replace(",", ".");
            float valorCompra = Float.parseFloat(strCompra);

            float x = Float.parseFloat(porcentagem.getText());
            float y = valorCompra;
            float result = x + y;
            venda.setText(String.valueOf(result));

            double valorVenda = Double.parseDouble(String.valueOf(result));
            DecimalFormat df = new DecimalFormat("##,##0.00");
            String t = df.format(valorVenda);
            venda.setText("R$ " + t);

        }
    }//GEN-LAST:event_porcentagemKeyPressed

    private void compraFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_compraFocusLost

        String valorCompra = compra.getText().replace(",", ".");
        double z = Double.parseDouble(String.valueOf(valorCompra));
        DecimalFormat dfm = new DecimalFormat("##,##0.00");
        String t = dfm.format(z);
        compra.setText("R$ " + t);
    }//GEN-LAST:event_compraFocusLost

    private void idProdutoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idProdutoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_A && evt.isControlDown()) {

            Beans mod = new Beans();
            controlProduto produto = new controlProduto();

            mod.setIdProduto(idProduto.getText());
            mod.setNome(nome.getText());
            mod.setMarca(marca.getText());
            mod.setQuantAtual(quantAtual.getText());
            mod.setCompra(compra.getText());
            mod.setVenda(venda.getText());
            mod.setPorcentagem(porcentagem.getText());
            mod.setDia(data.getText());
            mod.setCaminho(caminho);
            mod.setId(id.getText());
            produto.update(mod);

            nome.setText("");
            compra.setText("");
            quantAtual.setText("");
            venda.setText("");
            pesquisarProduto.setText("");
            porcentagem.setText("");
            idProduto.setText("");
            marca.setText("");

            data.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis())));
            readTableProduto("select * from produto where idProduto > 0 order by nome");

        }
    }//GEN-LAST:event_idProdutoKeyPressed

    private void botImprimirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botImprimirMouseClicked
        imprimirTabela();
    }//GEN-LAST:event_botImprimirMouseClicked

    private void botGerarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botGerarMouseClicked
        GerarCodigo tela = new GerarCodigo();
        tela.setVisible(true);
    }//GEN-LAST:event_botGerarMouseClicked

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_formWindowOpened

    private void nomeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomeKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_A && evt.isControlDown()) {
            idProdutoKeyPressed(evt);
        }
    }//GEN-LAST:event_nomeKeyPressed

    private void marcaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_marcaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_A && evt.isControlDown()) {
            idProdutoKeyPressed(evt);
        }
    }//GEN-LAST:event_marcaKeyPressed

    private void compraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_compraKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_A && evt.isControlDown()) {
            idProdutoKeyPressed(evt);
        }
    }//GEN-LAST:event_compraKeyPressed

    private void vendaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_vendaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_A && evt.isControlDown()) {
            idProdutoKeyPressed(evt);
        }
    }//GEN-LAST:event_vendaKeyPressed

    private void menuAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAddActionPerformed
        Beans mod = new Beans();
        controlProduto produto = new controlProduto();

        mod.setIdProduto(idProduto.getText());
        mod.setNome(nome.getText());
        mod.setMarca(marca.getText());
        mod.setQuantAtual(quantAtual.getText().replace(",", "."));
        mod.setCompra(compra.getText().replace("R$", "").replace(",", "."));
        mod.setPorcentagem(porcentagem.getText());
        mod.setVenda(venda.getText().replace("R$", "").replace(",", "."));
        mod.setDia(data.getText());
        mod.setCaminho(caminho);
        produto.createProduto(mod);

        nome.setText("");
        idProduto.setText("");
        compra.setText("");
        quantAtual.setText("");
        venda.setText("");
        pesquisarProduto.setText("");
        porcentagem.setText("");
        marca.setText("");

        readTableProduto("select * from produto where idProduto > 0 order by nome");
        idProduto.grabFocus();
    }//GEN-LAST:event_menuAddActionPerformed

    private void menuLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuLimparActionPerformed
        nome.setText("");
        compra.setText("");
        quantAtual.setText("");
        venda.setText("");
        pesquisarProduto.setText("");
        porcentagem.setText("");
        idProduto.setText("");
        marca.setText("");
        data.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis())));
        idProduto.grabFocus();
    }//GEN-LAST:event_menuLimparActionPerformed

    private void menuGerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuGerarActionPerformed
        GerarCodigo tela = new GerarCodigo();
        tela.setVisible(true);
    }//GEN-LAST:event_menuGerarActionPerformed

    private void menuSituacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSituacaoActionPerformed
        readTableProduto("select * from produto where quantAtual <= 5 and idProduto > 0 order by nome");
    }//GEN-LAST:event_menuSituacaoActionPerformed

    private void menuExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuExcluirActionPerformed
        Beans mod = new Beans();
        controlProduto produto = new controlProduto();

        int resposta = JOptionPane.showConfirmDialog(rootPane, "Deseja excluir o item?");
        if (resposta == JOptionPane.YES_NO_OPTION) {
            mod.setId(id.getText());
            produto.delete(mod);
        }

        nome.setText("");
        compra.setText("");
        quantAtual.setText("");
        venda.setText("");
        pesquisarProduto.setText("");
        porcentagem.setText("");
        idProduto.setText("");
        marca.setText("");
        data.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis())));
        idProduto.grabFocus();

        data.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis())));
        idProduto.grabFocus();
        readTableProduto("select * from produto where idProduto > 0 order by nome");
    }//GEN-LAST:event_menuExcluirActionPerformed

    private void menuAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAtualizarActionPerformed
        Beans mod = new Beans();
        controlProduto produto = new controlProduto();

        mod.setIdProduto(idProduto.getText());
        mod.setNome(nome.getText());
        mod.setMarca(marca.getText());
        mod.setQuantAtual(quantAtual.getText());
        mod.setCompra(compra.getText());
        mod.setVenda(venda.getText());
        mod.setPorcentagem(porcentagem.getText());
        mod.setDia(data.getText());
        mod.setCaminho(caminho);
        mod.setId(id.getText());
        produto.update(mod);

        nome.setText("");
        compra.setText("");
        quantAtual.setText("");
        venda.setText("");
        pesquisarProduto.setText("");
        porcentagem.setText("");
        idProduto.setText("");
        marca.setText("");

        data.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis())));
        readTableProduto("select * from produto where idProduto > 0 order by nome");
    }//GEN-LAST:event_menuAtualizarActionPerformed

    private void menuAdd1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAdd1ActionPerformed
        imprimirTabela();
    }//GEN-LAST:event_menuAdd1ActionPerformed

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
            java.util.logging.Logger.getLogger(Produto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Produto().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel botAdd;
    private javax.swing.JButton botAgenda;
    private javax.swing.JLabel botAtualizar;
    private javax.swing.JLabel botExcluir;
    private javax.swing.JButton botFornecedor;
    private javax.swing.JLabel botGerar;
    private javax.swing.JButton botHistorico;
    private javax.swing.JLabel botImprimir;
    private javax.swing.JLabel botLimpar;
    private javax.swing.JButton botMenu;
    private javax.swing.JLabel botPesquisarProdutos;
    private javax.swing.JButton botSite;
    private javax.swing.JLabel botSituacao;
    private javax.swing.JButton botVenda;
    private javax.swing.JComboBox<String> caixa;
    private javax.swing.JTextField compra;
    private javax.swing.JLabel data;
    private javax.swing.JLabel id;
    private javax.swing.JTextField idProduto;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jtableProduto;
    private javax.swing.JTextField marca;
    private javax.swing.JMenuItem menuAdd;
    private javax.swing.JMenuItem menuAdd1;
    private javax.swing.JMenuItem menuAtualizar;
    private javax.swing.JMenuItem menuExcluir;
    private javax.swing.JMenuItem menuGerar;
    private javax.swing.JMenuItem menuLimpar;
    private javax.swing.JMenuItem menuSituacao;
    private javax.swing.JTextField nome;
    private javax.swing.JTextField pesquisarProduto;
    private javax.swing.JTextField porcentagem;
    private javax.swing.JTextField quantAtual;
    private javax.swing.JTable tabelaProduto;
    private javax.swing.JTextField venda;
    // End of variables declaration//GEN-END:variables
}
