package viewer;

import controlConnetion.conectaBanco;
import controlViwer.controlVenda;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import modelBeans.Beans;
import modelBeans.Table;
import modelBeans.focusTraversalPolicy;
import modelBeans.modelNumeros;


public final class VendaFiador extends javax.swing.JFrame {

    conectaBanco conexao = new conectaBanco();
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String recebe;
    DefaultListModel modelo;
    int entrar = 1;
    String[] codig;
    String totalFormat;
    Clientes parent;
    
    public VendaFiador(Clientes c, String n) {
        initComponents();
        this.parent = c;
        nomeCliente.setText(n);
        componentes();
        controlFocus();
        mostrar();
        data.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis())));
        readTableCompra(null);
        tabelaCompra.getTableHeader().setDefaultRenderer(new VendaFiador.header());
    }

    VendaFiador() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        
        idProduto.setDocument(new modelNumeros()); //chama o metodo modelNumeros que seta só numeros para o idProduto
    }

    public void atualizar() {
        this.dispose();
    }

    public void somarValores() {

       conexao.conecta();
        conexao.executaSQL("select sum(total) from historico where idVenda='" + idVenda.getText() + "'");
        try {
            conexao.rs.first();
            double totalCompra = (conexao.rs.getDouble(1));
            total.setText(String.valueOf(totalCompra));
            DecimalFormat df = new DecimalFormat("####0.00");
            total.setText(df.format(totalCompra));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conexao.desconecta();
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
                totalFormat = conexao.rs.getString("total");
                float valor = Float.parseFloat(String.valueOf(totalFormat));
                DecimalFormat df = new DecimalFormat("##,##0.00");
                String format = df.format(valor);

                dados.add(new Object[]{
                    conexao.rs.getString("idVenda"), conexao.rs.getString("idProduto"), conexao.rs.getString("nome"),
                    conexao.rs.getString("venda"), conexao.rs.getString("quantVenda"), format});
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
        botLimpar = new javax.swing.JLabel();
        botExcluir = new javax.swing.JLabel();
        botFinalizar = new javax.swing.JLabel();
        quantAtual = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        marca = new javax.swing.JTextField();
        botOrcamento = new javax.swing.JLabel();
        idProduto = new javax.swing.JTextField();
        botEstoque = new javax.swing.JLabel();
        nomeCliente = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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

        jPanel1.add(barList, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 310, 430, -1));

        idVenda.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        jPanel1.add(idVenda, new org.netbeans.lib.awtextra.AbsoluteConstraints(71, 10, 98, 35));

        nome.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        nome.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(nome, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 200, 390, 35));

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
        jPanel1.add(quantVenda, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 200, 120, 35));

        preco.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        preco.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(preco, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 200, 120, 35));

        data.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        jPanel1.add(data, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 20, 110, 35));

        jLabel9.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel9.setText("Nome");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 170, 50, 30));

        jLabel10.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel10.setText("Pesquisar Produto");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 250, 210, 30));

        jLabel11.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel11.setText("Quantidade");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 170, 100, 30));

        jLabel12.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel12.setText("Preço");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 170, 50, 30));

        total.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        total.setForeground(new java.awt.Color(0, 51, 255));
        total.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        total.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Total", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 24))); // NOI18N
        jPanel1.add(total, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 70, 170, 70));

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

        jPanel1.add(jtableCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 320, 950, 360));

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
        jPanel1.add(pesquisaProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 280, 430, 35));

        botLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/limpar 32.png"))); // NOI18N
        botLimpar.setToolTipText("Limpar");
        botLimpar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botLimparMouseClicked(evt);
            }
        });
        jPanel1.add(botLimpar, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 280, -1, 40));

        botExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/lixeira 32.png"))); // NOI18N
        botExcluir.setToolTipText("Excluir");
        botExcluir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botExcluirMouseClicked(evt);
            }
        });
        jPanel1.add(botExcluir, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 280, 30, 40));

        botFinalizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/ok 35.png"))); // NOI18N
        botFinalizar.setToolTipText("Finalizar");
        botFinalizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botFinalizarMouseClicked(evt);
            }
        });
        jPanel1.add(botFinalizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 280, -1, 40));
        jPanel1.add(quantAtual, new org.netbeans.lib.awtextra.AbsoluteConstraints(581, 140, 28, 35));

        jLabel14.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel14.setText("ID Produto");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 170, 90, 30));

        marca.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        marca.setForeground(new java.awt.Color(255, 255, 255));
        marca.setBorder(null);
        marca.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        marca.setEnabled(false);
        marca.setOpaque(false);
        jPanel1.add(marca, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 30, 140, 35));

        botOrcamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/orcamento 32.png"))); // NOI18N
        botOrcamento.setToolTipText("Orçamento");
        botOrcamento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botOrcamentoMouseClicked(evt);
            }
        });
        jPanel1.add(botOrcamento, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 280, 30, 40));

        idProduto.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        idProduto.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        idProduto.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        idProduto.setOpaque(false);
        idProduto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                idProdutoKeyPressed(evt);
            }
        });
        jPanel1.add(idProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, 140, 35));

        botEstoque.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/estoque 40.png"))); // NOI18N
        botEstoque.setToolTipText("Estoque");
        botEstoque.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botEstoqueMouseClicked(evt);
            }
        });
        jPanel1.add(botEstoque, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 280, -1, 40));

        nomeCliente.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        nomeCliente.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nomeCliente.setEnabled(false);
        nomeCliente.setOpaque(false);
        jPanel1.add(nomeCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 120, 280, 30));

        jLabel15.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel15.setText("Cliente");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, 60, 20));

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

    private void listaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaMouseClicked
        mostraPesquisa();
        lista.setVisible(false);
        barList.setVisible(false);
        quantVenda.setText("");
    }//GEN-LAST:event_listaMouseClicked

    private void quantVendaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_quantVendaFocusGained
        quantVenda.selectAll();
    }//GEN-LAST:event_quantVendaFocusGained

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
                total.setText(String.valueOf(soma));

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

    private void pesquisaProdutoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pesquisaProdutoFocusGained
        pesquisaProduto.selectAll();
    }//GEN-LAST:event_pesquisaProdutoFocusGained

    private void pesquisaProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pesquisaProdutoActionPerformed
        lista.setVisible(false);
        barList.setVisible(false);
        entrar = 1;
    }//GEN-LAST:event_pesquisaProdutoActionPerformed

    private void pesquisaProdutoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pesquisaProdutoKeyPressed
        if (entrar == 0) {
            listaPesquisa();
        } else {
            entrar = 0;
        }
        quantVenda.setText("");
    }//GEN-LAST:event_pesquisaProdutoKeyPressed

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

    private void botFinalizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botFinalizarMouseClicked

        if (!total.getText().equals("")) {
            new PagamentoFiador(this, true, total.getText(), Integer.parseInt(idVenda.getText()), nomeCliente.getText()).setVisible(true); //método para enviar o total para o pagamento pelo construtor
        } else if (total.getText().equals("R$ 0,00")) {
            JOptionPane.showMessageDialog(tabelaCompra, "Venda ao menos um item para finalizar a venda!");
        } else if (total.getText().equals("")) {
            JOptionPane.showMessageDialog(tabelaCompra, "Venda ao menos um item para finalizar a venda!");
        }
    }//GEN-LAST:event_botFinalizarMouseClicked

    private void botOrcamentoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botOrcamentoMouseClicked
        Orcamento tela = new Orcamento();
        tela.setVisible(true);
    }//GEN-LAST:event_botOrcamentoMouseClicked

    private void idProdutoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idProdutoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            pesquisaCodigo();
            quantVenda.grabFocus();
        }
    }//GEN-LAST:event_idProdutoKeyPressed

    private void botEstoqueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botEstoqueMouseClicked
        Estoque tela = new Estoque();
        tela.setVisible(true);
    }//GEN-LAST:event_botEstoqueMouseClicked

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
            java.util.logging.Logger.getLogger(VendaFiador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new VendaFiador().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane barList;
    private javax.swing.JLabel botEstoque;
    private javax.swing.JLabel botExcluir;
    private javax.swing.JLabel botFinalizar;
    private javax.swing.JLabel botLimpar;
    private javax.swing.JLabel botOrcamento;
    private javax.swing.JLabel data;
    private javax.swing.JTextField idProduto;
    private javax.swing.JLabel idVenda;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jtableCompra;
    private javax.swing.JList<String> lista;
    private javax.swing.JTextField marca;
    private javax.swing.JLabel nome;
    private javax.swing.JTextField nomeCliente;
    private javax.swing.JTextField pesquisaProduto;
    private javax.swing.JLabel preco;
    private javax.swing.JLabel quantAtual;
    private javax.swing.JTextField quantVenda;
    private javax.swing.JTable tabelaCompra;
    private javax.swing.JLabel total;
    // End of variables declaration//GEN-END:variables
}
