package viewer;

import modelBeans.Beans;
import modelBeans.focusTraversalPolicy;
import modelBeans.Table;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.sql.PreparedStatement;
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
import controlConnetion.conectaBanco;
import controlViwer.controlVenda;
import java.sql.ResultSet;
import javax.swing.DefaultListModel;
import modelBeans.modelNumeros;

public final class Venda extends javax.swing.JFrame {

    conectaBanco conexao = new conectaBanco();
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String recebe;
    DefaultListModel modelo;
    int entrar = 1;
    String[] codig;
    private boolean action = true;
    String totalFormat;

    public Venda() {
        initComponents();
        componentes();
        controlFocus();
        mostrar();
        data.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis())));
        readTableCompra(null);
        tabelaCompra.getTableHeader().setDefaultRenderer(new Venda.header());
    }

    public void componentes() {

        quantAtual.setForeground(new Color(246, 228, 228));
        idVenda.setForeground(new Color(246, 228, 228));
        total.setText("R$ 0,00");
        getContentPane().setBackground(Color.white);

        jtableCompra.getViewport().setBackground(Color.white);
        jtableCompra.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        lista.setOpaque(true);
        lista.setBackground(new Color(255, 255, 255));
        lista.setVisible(false);
        modelo = new DefaultListModel();
        lista.setModel(modelo);
        barList.setOpaque(true);
        barList.setBackground(new Color(255, 255, 255));
        barList.setVisible(false);

        botProduto.setVisible(false);
        botHistorico.setVisible(false);
        botFornecedor.setVisible(false);
        botAgenda.setVisible(false);
        botSite.setVisible(false);

        idProduto.setDocument(new modelNumeros()); //chama o metodo modelNumeros que seta só numeros para o idProduto
    }

    public void atualizar() {

        readTableCompra("");
        mostrar();
        pesquisaProduto.setText("");
        preco.setText("");
        idProduto.setText("");
        quantVenda.setText("");
        pesquisaProduto.setText("");
        quantAtual.setText("");
        marca.setText("");
        total.setText("R$ 0,00");
        idProduto.grabFocus();
    }

    public void somarValores() {
        double somaTotal = 0;
        for (int i = 0; i < tabelaCompra.getRowCount(); i++) {
            somaTotal += Double.parseDouble(tabelaCompra.getValueAt(i, 5).toString().replace(",", "."));
        }
        String resultado = String.format("%.2f", somaTotal); //coloca duas casas decimais
        total.setText("" + resultado);
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void mostrar() {

        conexao.conecta();
        conexao.executaSQL("SELECT * FROM historico ORDER BY idVenda DESC LIMIT 1");
        try {
            conexao.rs.last();
            do {
                idVenda.setText(String.valueOf(conexao.rs.getInt("idVenda")));
            } while (conexao.rs.next());
        } catch (SQLException ex) {
            //ex.printStackTrace();
        }
        conexao.desconecta();

        int x = Integer.parseInt(idVenda.getText());
        int y = 1;
        int result;
        result = x + y;
        idVenda.setText(String.valueOf(result));
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void quantidadeUpdate() {

        conexao.conecta();
        conexao.executaSQL("select * from produto");
        try {
            conexao.rs.first();
            double x = Double.parseDouble(quantAtual.getText());
            double y = Double.parseDouble(quantVenda.getText().replace(",", "."));
            double sub = x - y;
            stmt = conexao.conn.prepareStatement("update produto set quantAtual='" + sub + "' where idProduto='" + idProduto.getText() + "'");
            stmt.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void estorno() {

        conexao.conecta();
        String y = quantVenda.getText();
        String x = quantAtual.getText();
        Double estorno = Double.parseDouble(x) + Double.parseDouble(y);
        quantAtual.setText(String.valueOf(estorno));

        conexao.executaSQL("SELECT * FROM produto where idProduto='" + idProduto.getText() + "'");
        try {
            conexao.rs.first();
            do {
                stmt = conexao.conn.prepareStatement("update produto set quantAtual='" + estorno + "' where idProduto='" + idProduto.getText() + "'");
                stmt.execute();

                stmt = conexao.conn.prepareStatement("delete from historico where idVenda='" + idVenda.getText() + "' and idProduto='" + idProduto.getText() + "'");
                stmt.execute();

                quantAtual.setText("");
            } while (conexao.rs.next());

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        conexao.desconecta();
    }

    public void controlFocus() {

        ArrayList<Component> order = new ArrayList<>(2);
        order.add(idProduto);
        order.add(quantVenda);
        focusTraversalPolicy policy = new focusTraversalPolicy(order);
        setFocusTraversalPolicy(policy);
    }

    public void listaPesquisa() {

        conexao.conecta();
        conexao.executaSQL("select * from produto where nome like '%" + pesquisaProduto.getText() + "%' order by nome");
        modelo.removeAllElements();
        try {
            int v = 0;
            codig = new String[100];
            while (conexao.rs.next() & v < 100) {
                modelo.addElement(conexao.rs.getString("nome"));
                codig[v] = conexao.rs.getString("idProduto");
                v++;
            }
            if (v >= 1) {
                lista.setVisible(true);
                barList.setVisible(true);
            } else {
                lista.setVisible(false);
                barList.setVisible(false);
            }
            resultadoPesquisa();
        } catch (SQLException ex) {

        }
        conexao.desconecta();
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void mostraPesquisa() {

        conexao.conecta();
        int lin = lista.getSelectedIndex();
        if (lin >= 0) {
            conexao.executaSQL("select * from produto where idProduto =" + codig[lin] + "");
            resultadoPesquisa();
        }
        conexao.desconecta();
    }

    public void resultadoPesquisa() {

        conexao.conecta();
        conexao.executaSQL("select * from produto where nome '%" + pesquisaProduto.getText() + "%'");
        try {
            conexao.rs.first();
            idProduto.setText(conexao.rs.getString("idProduto"));
            nome.setText(conexao.rs.getString("nome") + " " + conexao.rs.getString("marca"));
            preco.setText(conexao.rs.getString("venda"));
            quantAtual.setText(conexao.rs.getString("quantAtual"));
        } catch (SQLException ex) {

        }
        conexao.desconecta();
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void pesquisaCodigo() {

        if (!idProduto.getText().equals("")) {
            conexao.conecta();
            conexao.executaSQL("Select * from produto where idProduto='" + idProduto.getText() + "'");
            try {
                if (conexao.rs.next()) { //checa se há registros
                    if (!idProduto.getText().equals("")) {
                        try {
                            conexao.rs.first();
                            nome.setText(conexao.rs.getString("nome") + " " + conexao.rs.getString("marca"));
                            preco.setText(conexao.rs.getString("venda"));
                            quantAtual.setText(conexao.rs.getString("quantAtual"));
                            /**
                             * String strCaminho = conexao.rs.getString("foto");
                             * ImageIcon i = new ImageIcon(new
                             * ImageIcon(strCaminho).getImage().getScaledInstance(foto.getWidth(),
                             * foto.getHeight(), Image.SCALE_SMOOTH));
                             * foto.setIcon(i);*
                             */
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }

                } else {
                    //System.out.println("Esperando");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            conexao.desconecta();
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
            setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            setHorizontalAlignment(JLabel.CENTER);
            return this;
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void readTableCompra(String Sql) {

        conexao.conecta();
        ArrayList dados = new ArrayList();
        String[] colunas = new String[]{"ID Venda", "ID Produto", "Nome", "Preço", "Quantidade Venda", "Subtotal"};
        conexao.executaSQL(Sql);
        try {
            conexao.rs.first();
            do {
                totalFormat = conexao.rs.getString("total").replace(",", ".");
                String resultado = String.format("%.2f", Double.parseDouble(totalFormat));

                dados.add(new Object[]{
                    conexao.rs.getString("idVenda"), conexao.rs.getString("idProduto"), conexao.rs.getString("nome"),
                    conexao.rs.getString("venda"), conexao.rs.getString("quantVenda"), resultado});
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

        tabelaCompra.setModel(tabela);
        tabelaCompra.getColumnModel().getColumn(0).setPreferredWidth(120);
        tabelaCompra.getColumnModel().getColumn(0).setResizable(false);
        tabelaCompra.getColumnModel().getColumn(0).setCellRenderer(centralizado);
        tabelaCompra.getColumnModel().getColumn(0).setMinWidth(0); //ocultando o indice 0 idVenda
        tabelaCompra.getColumnModel().getColumn(0).setMaxWidth(0); //ocultando o indice 0 idVenda
        tabelaCompra.getColumnModel().getColumn(1).setPreferredWidth(170);
        tabelaCompra.getColumnModel().getColumn(1).setResizable(false);
        tabelaCompra.getColumnModel().getColumn(1).setCellRenderer(centralizado);
        tabelaCompra.getColumnModel().getColumn(1).setMinWidth(0); //ocultando o indice 0 idVenda
        tabelaCompra.getColumnModel().getColumn(1).setMaxWidth(0);
        tabelaCompra.getColumnModel().getColumn(2).setPreferredWidth(430);
        tabelaCompra.getColumnModel().getColumn(2).setResizable(false);
        tabelaCompra.getColumnModel().getColumn(3).setPreferredWidth(180);
        tabelaCompra.getColumnModel().getColumn(3).setResizable(false);
        tabelaCompra.getColumnModel().getColumn(3).setCellRenderer(centralizado);
        tabelaCompra.getColumnModel().getColumn(4).setPreferredWidth(180);
        tabelaCompra.getColumnModel().getColumn(4).setResizable(false);
        tabelaCompra.getColumnModel().getColumn(4).setCellRenderer(centralizado);
        tabelaCompra.getColumnModel().getColumn(5).setPreferredWidth(180);
        tabelaCompra.getColumnModel().getColumn(5).setResizable(false);
        tabelaCompra.getColumnModel().getColumn(5).setCellRenderer(centralizado);
        tabelaCompra.getTableHeader().setReorderingAllowed(false);
        tabelaCompra.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabelaCompra.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
        barList = new javax.swing.JScrollPane();
        lista = new javax.swing.JList<>();
        botMenu = new javax.swing.JButton();
        botProduto = new javax.swing.JButton();
        botHistorico = new javax.swing.JButton();
        botFornecedor = new javax.swing.JButton();
        botAgenda = new javax.swing.JButton();
        botSite = new javax.swing.JButton();
        idVenda = new javax.swing.JLabel();
        nome = new javax.swing.JLabel();
        quantVenda = new javax.swing.JTextField();
        preco = new javax.swing.JLabel();
        data = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        total = new javax.swing.JLabel();
        jtableCompra = new javax.swing.JScrollPane();
        tabelaCompra = new javax.swing.JTable();
        pesquisaProduto = new javax.swing.JTextField();
        botEntrega = new javax.swing.JLabel();
        botLimpar = new javax.swing.JLabel();
        botExcluir = new javax.swing.JLabel();
        botFinalizar = new javax.swing.JLabel();
        quantAtual = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        marca = new javax.swing.JTextField();
        botOrcamento = new javax.swing.JLabel();
        idProduto = new javax.swing.JTextField();
        botEstoque = new javax.swing.JLabel();
        botNovo = new javax.swing.JLabel();
        botOrcamento1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu9 = new javax.swing.JMenu();
        menuEstoque = new javax.swing.JMenuItem();
        menuEntrega = new javax.swing.JMenuItem();
        menuOrcamento = new javax.swing.JMenuItem();
        menuFiadores = new javax.swing.JMenuItem();
        menuLimpar = new javax.swing.JMenuItem();
        menuExcluir = new javax.swing.JMenuItem();
        menuFinalizar = new javax.swing.JMenuItem();
        jMenu10 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Lýkos System");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Tela de Vendas", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 24))); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(1366, 738));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lista.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lista.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        lista.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listaMouseClicked(evt);
            }
        });
        barList.setViewportView(lista);

        jPanel1.add(barList, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 210, 430, -1));

        botMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/menu 32.png"))); // NOI18N
        botMenu.setToolTipText("Tela de Vendas");
        botMenu.setContentAreaFilled(false);
        botMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botMenuActionPerformed(evt);
            }
        });
        jPanel1.add(botMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 50, 40));

        botProduto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/produto preto 32.png"))); // NOI18N
        botProduto.setToolTipText("Produtos");
        botProduto.setContentAreaFilled(false);
        botProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botProdutoActionPerformed(evt);
            }
        });
        jPanel1.add(botProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 50, -1));

        botHistorico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/historico preto 32.png"))); // NOI18N
        botHistorico.setToolTipText("Histórico");
        botHistorico.setContentAreaFilled(false);
        botHistorico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botHistoricoActionPerformed(evt);
            }
        });
        jPanel1.add(botHistorico, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 50, -1));

        botFornecedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/fornecedor preto 32.png"))); // NOI18N
        botFornecedor.setToolTipText("Fornecedor");
        botFornecedor.setContentAreaFilled(false);
        botFornecedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botFornecedorActionPerformed(evt);
            }
        });
        jPanel1.add(botFornecedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 50, -1));

        botAgenda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/agenda preto 32.png"))); // NOI18N
        botAgenda.setToolTipText("Agenda (em breve)");
        botAgenda.setContentAreaFilled(false);
        botAgenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botAgendaActionPerformed(evt);
            }
        });
        jPanel1.add(botAgenda, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 230, 50, -1));

        botSite.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/site preto 32.png"))); // NOI18N
        botSite.setToolTipText("Site (em breve)");
        botSite.setContentAreaFilled(false);
        botSite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botSiteActionPerformed(evt);
            }
        });
        jPanel1.add(botSite, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 280, 50, -1));

        idVenda.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        jPanel1.add(idVenda, new org.netbeans.lib.awtextra.AbsoluteConstraints(71, 10, 98, 35));

        nome.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        nome.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(nome, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 100, 360, 35));

        quantVenda.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        quantVenda.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        quantVenda.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        quantVenda.setOpaque(false);
        quantVenda.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                quantVendaFocusGained(evt);
            }
        });
        quantVenda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                quantVendaKeyPressed(evt);
            }
        });
        jPanel1.add(quantVenda, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 100, 120, 35));

        preco.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        preco.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(preco, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 100, 120, 35));

        data.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        jPanel1.add(data, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 20, 110, 35));

        jLabel9.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel9.setText("Nome");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 70, 50, 30));

        jLabel10.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel10.setText("Pesquisar Produto");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 150, 210, 30));

        jLabel11.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel11.setText("Quantidade");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 70, 100, 30));

        jLabel12.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel12.setText("Preço");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 70, 50, 30));

        total.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        total.setForeground(new java.awt.Color(0, 51, 255));
        total.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        total.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Total", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 24))); // NOI18N
        jPanel1.add(total, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 60, 160, 80));

        tabelaCompra.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tabelaCompra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabelaCompra.setGridColor(java.awt.Color.lightGray);
        tabelaCompra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaCompraMouseClicked(evt);
            }
        });
        jtableCompra.setViewportView(tabelaCompra);

        jPanel1.add(jtableCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, 950, 440));

        pesquisaProduto.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        pesquisaProduto.setOpaque(false);
        pesquisaProduto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pesquisaProdutoFocusGained(evt);
            }
        });
        pesquisaProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pesquisaProdutoActionPerformed(evt);
            }
        });
        pesquisaProduto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pesquisaProdutoKeyPressed(evt);
            }
        });
        jPanel1.add(pesquisaProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 180, 430, 35));

        botEntrega.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/entrega 40.png"))); // NOI18N
        botEntrega.setToolTipText("Estoque");
        botEntrega.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botEntregaMouseClicked(evt);
            }
        });
        jPanel1.add(botEntrega, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 180, -1, 40));

        botLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/limpar 32.png"))); // NOI18N
        botLimpar.setToolTipText("Limpar");
        botLimpar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botLimparMouseClicked(evt);
            }
        });
        jPanel1.add(botLimpar, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 180, -1, 40));

        botExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/lixeira 32.png"))); // NOI18N
        botExcluir.setToolTipText("Excluir");
        botExcluir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botExcluirMouseClicked(evt);
            }
        });
        jPanel1.add(botExcluir, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 180, 30, 40));

        botFinalizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/ok 35.png"))); // NOI18N
        botFinalizar.setToolTipText("Finalizar");
        botFinalizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botFinalizarMouseClicked(evt);
            }
        });
        jPanel1.add(botFinalizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 180, -1, 40));
        jPanel1.add(quantAtual, new org.netbeans.lib.awtextra.AbsoluteConstraints(581, 140, 28, 35));

        jLabel14.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel14.setText("ID Produto");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 70, 90, 30));

        marca.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        marca.setForeground(new java.awt.Color(255, 255, 255));
        marca.setBorder(null);
        marca.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        marca.setEnabled(false);
        marca.setOpaque(false);
        jPanel1.add(marca, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 20, 140, 35));

        botOrcamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/orcamento 32.png"))); // NOI18N
        botOrcamento.setToolTipText("Orçamento");
        botOrcamento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botOrcamentoMouseClicked(evt);
            }
        });
        jPanel1.add(botOrcamento, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 180, 30, 40));

        idProduto.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        idProduto.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        idProduto.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        idProduto.setOpaque(false);
        idProduto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                idProdutoKeyPressed(evt);
            }
        });
        jPanel1.add(idProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, 140, 35));

        botEstoque.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/estoque 40.png"))); // NOI18N
        botEstoque.setToolTipText("Estoque");
        botEstoque.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botEstoqueMouseClicked(evt);
            }
        });
        jPanel1.add(botEstoque, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 180, -1, 40));

        botNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/documento 32.png"))); // NOI18N
        botNovo.setToolTipText("Novo");
        botNovo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botNovoMouseClicked(evt);
            }
        });
        jPanel1.add(botNovo, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 180, 32, 40));

        botOrcamento1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/fiadores 32.png"))); // NOI18N
        botOrcamento1.setToolTipText("Orçamento");
        botOrcamento1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botOrcamento1MouseClicked(evt);
            }
        });
        jPanel1.add(botOrcamento1, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 180, 30, 40));

        jMenu9.setText("Funções");
        jMenu9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        menuEstoque.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        menuEstoque.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        menuEstoque.setText("Estoque");
        menuEstoque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEstoqueActionPerformed(evt);
            }
        });
        jMenu9.add(menuEstoque);

        menuEntrega.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        menuEntrega.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        menuEntrega.setText("Entregas");
        menuEntrega.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEntregaActionPerformed(evt);
            }
        });
        jMenu9.add(menuEntrega);

        menuOrcamento.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
        menuOrcamento.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        menuOrcamento.setText("Orçamento");
        menuOrcamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOrcamentoActionPerformed(evt);
            }
        });
        jMenu9.add(menuOrcamento);

        menuFiadores.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        menuFiadores.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        menuFiadores.setText("Fiadores");
        menuFiadores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFiadoresActionPerformed(evt);
            }
        });
        jMenu9.add(menuFiadores);

        menuLimpar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));
        menuLimpar.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        menuLimpar.setText("Limpar");
        menuLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuLimparActionPerformed(evt);
            }
        });
        jMenu9.add(menuLimpar);

        menuExcluir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        menuExcluir.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        menuExcluir.setText("Excluir");
        menuExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExcluirActionPerformed(evt);
            }
        });
        jMenu9.add(menuExcluir);

        menuFinalizar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F7, 0));
        menuFinalizar.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        menuFinalizar.setText("Finalizar");
        menuFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFinalizarActionPerformed(evt);
            }
        });
        jMenu9.add(menuFinalizar);

        jMenuBar1.add(jMenu9);

        jMenu10.setText("Opções");
        jMenu10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuBar1.add(jMenu10);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1024, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1040, 738));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    @SuppressWarnings("CallToPrintStackTrace")
    private void tabelaCompraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaCompraMouseClicked

        int linha = tabelaCompra.getSelectedRow();
        idProduto.setText(tabelaCompra.getValueAt(linha, 1).toString());
        nome.setText(tabelaCompra.getValueAt(linha, 2).toString());
        preco.setText(tabelaCompra.getValueAt(linha, 3).toString());
        quantVenda.setText(tabelaCompra.getValueAt(linha, 4).toString());

        conexao.conecta();
        conexao.executaSQL("select * from produto where idProduto='" + idProduto.getText() + "'");
        try {
            conexao.rs.first();
            quantAtual.setText(String.valueOf(conexao.rs.getString("quantAtual")));
        } catch (SQLException ex) {
            //ex.printStackTrace();
        }
        conexao.desconecta();
    }//GEN-LAST:event_tabelaCompraMouseClicked

    private void botEntregaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botEntregaMouseClicked
        new Entregas(this, idVenda.getText()).setVisible(true);
    }//GEN-LAST:event_botEntregaMouseClicked

    private void botLimparMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botLimparMouseClicked

        pesquisaProduto.setText("");
        preco.setText("");
        idProduto.setText("");
        quantVenda.setText("");
        nome.setText("");
    }//GEN-LAST:event_botLimparMouseClicked

    private void botExcluirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botExcluirMouseClicked
        int resposta = JOptionPane.showConfirmDialog(rootPane, "Deseja excluir este item?");
        if (resposta == JOptionPane.YES_OPTION) {
            estorno();
            readTableCompra("select * from historico where idVenda='" + idVenda.getText() + "'");
            somarValores();
            pesquisaProduto.setText("");
            preco.setText("");
            idProduto.setText("");
            quantVenda.setText("");
            nome.setText("");
            marca.setText("");
        }
    }//GEN-LAST:event_botExcluirMouseClicked

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

    @SuppressWarnings("CallToPrintStackTrace")
    private void quantVendaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_quantVendaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            Double a = Double.parseDouble(quantAtual.getText());
            Double b = Double.parseDouble(quantVenda.getText().replace(",", "."));
            Double result = a - b;

            if (result >= 0) {

                Beans mod = new Beans();
                controlVenda venda = new controlVenda();

                String strPreco = preco.getText().replace("R$", "").replace(",", ".");

                float x = Float.parseFloat(quantVenda.getText().replace(",", "."));
                float y = Float.parseFloat(strPreco);
                float soma = x * y;
                String resultado = String.format("%.2f", soma); //coloca duas casas decimais
                total.setText(String.valueOf(resultado));

                mod.setIdVenda(idVenda.getText());
                mod.setIdProduto(idProduto.getText());
                mod.setNome(nome.getText());
                mod.setMarca(marca.getText());
                mod.setQuantAtual(String.valueOf(result));
                mod.setVenda(preco.getText().replace("R$", "").replace(",", "."));
                mod.setQuantVenda(quantVenda.getText().replace(",", "."));
                mod.setTotal(total.getText());
                venda.createVenda(mod);

                readTableCompra("select * from historico where idVenda='" + idVenda.getText() + "'");
                somarValores();
                quantidadeUpdate();

                pesquisaProduto.setText("");
                nome.setText("");
                quantVenda.setText("");
                preco.setText("");
                marca.setText("");
                idProduto.setText("");

                idProduto.grabFocus();
            } else {
                JOptionPane.showMessageDialog(rootPane, "Não é possível vender está quantidade. Sua quantidade em estoque é " + a + ".");
            }
        }
    }//GEN-LAST:event_quantVendaKeyPressed

    private void botFinalizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botFinalizarMouseClicked

        if (!total.getText().equals("")) {
            new Pagamento(this, total.getText(), idVenda.getText()).setVisible(true); //método para enviar o total para o pagamento pelo construtor
        } else if (total.getText().equals("R$ 0,00")) {
            JOptionPane.showMessageDialog(tabelaCompra, "Venda ao menos um item para finalizar a venda!");
        } else if (total.getText().equals("")) {
            JOptionPane.showMessageDialog(tabelaCompra, "Venda ao menos um item para finalizar a venda!");
        }
    }//GEN-LAST:event_botFinalizarMouseClicked

    private void pesquisaProdutoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pesquisaProdutoKeyPressed
        if (entrar == 0) {
            listaPesquisa();
        } else {
            entrar = 0;
        }
        quantVenda.setText("");
    }//GEN-LAST:event_pesquisaProdutoKeyPressed

    private void pesquisaProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pesquisaProdutoActionPerformed
        lista.setVisible(false);
        barList.setVisible(false);
        entrar = 1;
    }//GEN-LAST:event_pesquisaProdutoActionPerformed

    private void botMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botMenuActionPerformed

        if (action) {
            botProduto.setVisible(true);
            botHistorico.setVisible(true);
            botFornecedor.setVisible(true);
            botAgenda.setVisible(true);
            botSite.setVisible(true);
        } else {
            botProduto.setVisible(false);
            botHistorico.setVisible(false);
            botFornecedor.setVisible(false);
            botAgenda.setVisible(false);
            botSite.setVisible(false);
        }
        action = !action;
    }//GEN-LAST:event_botMenuActionPerformed

    private void quantVendaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_quantVendaFocusGained
        quantVenda.selectAll();
    }//GEN-LAST:event_quantVendaFocusGained

    private void listaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaMouseClicked
        mostraPesquisa();
        lista.setVisible(false);
        barList.setVisible(false);
        quantVenda.setText("");
    }//GEN-LAST:event_listaMouseClicked

    private void pesquisaProdutoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pesquisaProdutoFocusGained
        pesquisaProduto.selectAll();
    }//GEN-LAST:event_pesquisaProdutoFocusGained

    private void botOrcamentoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botOrcamentoMouseClicked
        Orcamento tela = new Orcamento();
        tela.setVisible(true);
    }//GEN-LAST:event_botOrcamentoMouseClicked

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_formWindowOpened

    private void botEstoqueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botEstoqueMouseClicked
        Estoque tela = new Estoque();
        tela.setVisible(true);
    }//GEN-LAST:event_botEstoqueMouseClicked

    private void botNovoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botNovoMouseClicked
        int resposta = JOptionPane.showConfirmDialog(null, "Deseja iniciar uma nova venda?");
        if (resposta == JOptionPane.YES_OPTION) {
            Venda tela = new Venda();
            tela.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_botNovoMouseClicked

    private void menuEstoqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEstoqueActionPerformed
        Estoque tela = new Estoque();
        tela.setVisible(true);
    }//GEN-LAST:event_menuEstoqueActionPerformed

    private void menuEntregaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEntregaActionPerformed
        new Entregas(this, idVenda.getText()).setVisible(true);
    }//GEN-LAST:event_menuEntregaActionPerformed

    private void menuOrcamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOrcamentoActionPerformed
        Orcamento tela = new Orcamento();
        tela.setVisible(true);
    }//GEN-LAST:event_menuOrcamentoActionPerformed

    private void menuFiadoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFiadoresActionPerformed
        new Clientes().setVisible(true);
    }//GEN-LAST:event_menuFiadoresActionPerformed

    private void menuLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuLimparActionPerformed
        pesquisaProduto.setText("");
        preco.setText("");
        idProduto.setText("");
        quantVenda.setText("");
        nome.setText("");
        marca.setText("");
    }//GEN-LAST:event_menuLimparActionPerformed

    private void menuExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuExcluirActionPerformed
        int resposta = JOptionPane.showConfirmDialog(rootPane, "Deseja excluir este item?");
        if (resposta == JOptionPane.YES_OPTION) {
            estorno();
            readTableCompra("select * from historico where idVenda='" + idVenda.getText() + "'");
            somarValores();
            pesquisaProduto.setText("");
            preco.setText("");
            idProduto.setText("");
            quantVenda.setText("");
            nome.setText("");
            marca.setText("");
        }
    }//GEN-LAST:event_menuExcluirActionPerformed

    private void menuFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFinalizarActionPerformed
        if (!total.getText().equals("")) {
            new Pagamento(this, total.getText(), idVenda.getText()).setVisible(true); //método para enviar o total para o pagamento pelo construtor
        } else if (total.getText().equals("R$ 0,00")) {
            JOptionPane.showMessageDialog(tabelaCompra, "Venda ao menos um item para finalizar a venda!");
        } else if (total.getText().equals("")) {
            JOptionPane.showMessageDialog(tabelaCompra, "Venda ao menos um item para finalizar a venda!");
        }
    }//GEN-LAST:event_menuFinalizarActionPerformed

    private void idProdutoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idProdutoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            pesquisaCodigo();
            quantVenda.grabFocus();
        }
    }//GEN-LAST:event_idProdutoKeyPressed

    private void botOrcamento1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botOrcamento1MouseClicked
        new Clientes().setVisible(true);
    }//GEN-LAST:event_botOrcamento1MouseClicked

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
            java.util.logging.Logger.getLogger(Venda.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Venda().setVisible(true);
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane barList;
    private javax.swing.JButton botAgenda;
    private javax.swing.JLabel botEntrega;
    private javax.swing.JLabel botEstoque;
    private javax.swing.JLabel botExcluir;
    private javax.swing.JLabel botFinalizar;
    private javax.swing.JButton botFornecedor;
    private javax.swing.JButton botHistorico;
    private javax.swing.JLabel botLimpar;
    private javax.swing.JButton botMenu;
    private javax.swing.JLabel botNovo;
    private javax.swing.JLabel botOrcamento;
    private javax.swing.JLabel botOrcamento1;
    private javax.swing.JButton botProduto;
    private javax.swing.JButton botSite;
    private javax.swing.JLabel data;
    private javax.swing.JTextField idProduto;
    private javax.swing.JLabel idVenda;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jtableCompra;
    private javax.swing.JList<String> lista;
    private javax.swing.JTextField marca;
    private javax.swing.JMenuItem menuEntrega;
    private javax.swing.JMenuItem menuEstoque;
    private javax.swing.JMenuItem menuExcluir;
    private javax.swing.JMenuItem menuFiadores;
    private javax.swing.JMenuItem menuFinalizar;
    private javax.swing.JMenuItem menuLimpar;
    private javax.swing.JMenuItem menuOrcamento;
    private javax.swing.JLabel nome;
    private javax.swing.JTextField pesquisaProduto;
    private javax.swing.JLabel preco;
    private javax.swing.JLabel quantAtual;
    private javax.swing.JTextField quantVenda;
    private javax.swing.JTable tabelaCompra;
    private javax.swing.JLabel total;
    // End of variables declaration//GEN-END:variables
}
