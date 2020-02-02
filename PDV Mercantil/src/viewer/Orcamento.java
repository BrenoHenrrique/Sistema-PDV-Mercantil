package viewer;

import controlConnetion.conectaBanco;
import controlViwer.controlOrcamento;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
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

public final class Orcamento extends javax.swing.JFrame {

    conectaBanco conexao = new conectaBanco();
    PreparedStatement stmt = null;
    ResultSet rs = null;
    List<Object> arrayParametros = new ArrayList<>();
    Timer timer = new Timer();
    int interval;
    int delay;

    public Orcamento() {
        initComponents();
        componentes();
        controlFocus();
        mostrar();
        timerPesquisa();
        readTableCompra("select * from orcamento where idOrcamento='" + idOrcamento.getText() + "'");
        tabelaCompra.getTableHeader().setDefaultRenderer(new Orcamento.header());
    }

    public void componentes() {

        titulo.setOpaque(true);
        titulo.setBackground(new Color(024, 131, 215));
        titulo.setForeground(new Color(255, 255, 255));
        idOrcamento.setForeground(Color.white);
        idProduto.setForeground(Color.white);
        total.setText("R$ 0,00");

        barCompra.getViewport().setBackground(Color.white);
    }

    public void mostrar() {

        conexao.conecta();
        conexao.executaSQL("SELECT * FROM orcamento ORDER BY idOrcamento DESC LIMIT 1");
        try {
            conexao.rs.last();
            do {
                idOrcamento.setText(String.valueOf(conexao.rs.getInt("idOrcamento")));
            } while (conexao.rs.next());
        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null, "Erro ao mostrar\n"+ex);
        }
        conexao.desconecta();

        int x = Integer.parseInt(idOrcamento.getText());
        int y = 1;
        int result;
        result = x + y;
        idOrcamento.setText(String.valueOf(result));
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void delete() {

        conexao.conecta();
        conexao.executaSQL("select * from orcamento");
        try {
            conexao.rs.first();
            stmt = conexao.conn.prepareStatement("delete from orcamento where idOrcamento='" + idOrcamento.getText() + "'");
            stmt.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void timerPesquisa() {

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            @SuppressWarnings("CallToPrintStackTrace")
            public void run() {
                if (!pesquisaProduto.getText().equals("")) {
                    conexao.conecta();
                    conexao.executaSQL("Select * from produto where idProduto='" + pesquisaProduto.getText() + "'");
                    try {
                        conexao.rs.first();
                        nome.setText(conexao.rs.getString("nome"));
                        preco.setText(conexao.rs.getString("venda"));

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    //System.out.println("Esperando");
                }
            }
        }, 0, 1000);
        conexao.desconecta();
    }

    public void controlFocus() {
        ArrayList<Component> order = new ArrayList<>(2);
        order.add(pesquisaProduto);
        order.add(quantVenda);
        focusTraversalPolicy policy = new focusTraversalPolicy(order);
        setFocusTraversalPolicy(policy);
    }

    public void somarValores() {

        conexao.conecta();
        conexao.executaSQL("select * from orcamento");
        try {
            while (conexao.rs.next()) {
                double somaTotal = 0;
                for (int i = 0; i < tabelaCompra.getRowCount(); i++) {
                    somaTotal += Double.parseDouble(tabelaCompra.getValueAt(i, 4).toString());
                }
                total.setText("" + somaTotal);

                float valor = Float.parseFloat(String.valueOf(somaTotal));
                DecimalFormat df = new DecimalFormat("##,##0.00");
                String s = df.format(valor);
                total.setText("R$ " + s);
            }
        } catch (NumberFormatException | SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Erro ao somar:" + ex.getMessage());
        }

        conexao.desconecta();
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
    public void readTableCompra(String sql) {

        conexao.conecta();
        ArrayList dados = new ArrayList();
        String[] colunas = new String[]{"ID Produto", "Nome", "Preço", "Quantidade", "Subtotal"};
        conexao.executaSQL(sql);
        try {
            conexao.rs.first();
            do {
                dados.add(new Object[]{
                    conexao.rs.getString("idProduto"), conexao.rs.getString("nome"),
                    conexao.rs.getString("preco"), conexao.rs.getString("quantVenda"),
                    conexao.rs.getString("total")});
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
        tabelaCompra.getColumnModel().getColumn(0).setPreferredWidth(100);
        tabelaCompra.getColumnModel().getColumn(0).setResizable(false);
        tabelaCompra.getColumnModel().getColumn(0).setCellRenderer(centralizado);
        tabelaCompra.getColumnModel().getColumn(1).setPreferredWidth(300);
        tabelaCompra.getColumnModel().getColumn(1).setResizable(false);
        tabelaCompra.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabelaCompra.getColumnModel().getColumn(2).setResizable(false);
        tabelaCompra.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabelaCompra.getColumnModel().getColumn(3).setResizable(false);
        tabelaCompra.getColumnModel().getColumn(3).setCellRenderer(centralizado);
        tabelaCompra.getColumnModel().getColumn(4).setPreferredWidth(100);
        tabelaCompra.getColumnModel().getColumn(4).setResizable(false);
        tabelaCompra.getColumnModel().getColumn(4).setCellRenderer(centralizado);
        tabelaCompra.getTableHeader().setReorderingAllowed(false);
        tabelaCompra.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabelaCompra.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        conexao.desconecta();
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void imprimirCupom() {

        String parametros = "";

        arrayParametros.add(parametros);

        conexao.conecta();
        conexao.executaSQL("select * from orcamento where idOrcamento='" + idOrcamento.getText() + "'");

        try {
            conexao.rs.first();
            do {
                String nomeProduto = conexao.rs.getString("nome");
                String quantProd = conexao.rs.getString("quantVenda");
                String valorProd = conexao.rs.getString("preco");

                parametros = "DESC" + "  " + nomeProduto + "\n\r"
                        + "QT" + "  " + quantProd + "      " + "PRECO" + "  " + valorProd + "\n\r";

                arrayParametros.add(parametros);

            } while (conexao.rs.next());

            String data = new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis()));
            String hora = new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis()));

            this.impressao("FRIGORIFICO DEUS PROVERA\n\r"
                    + "Data e Hora: " + data + " - " + hora + "\n\r"
                    + "Endereco: Rua 7 1458 Residencial Maracanaú 1\n\r"
                    + "Celular:  (85) 98413 7689\n\r"
                    + "--------------------------------\n\r"
                    + "        CUPOM NAO FISCAL        \n\r"
                    + "--------------------------------\n\r"
                    + arrayParametros + "\n\r"
                    + "--------------------------------\n\r"
                    + "VALOR TOTAL: " + total.getText() + "\n"
                    + "--------------------------------\n\r"
                    + "OBS: So e feita a troca de      produtos com este cupom em maos.\n\r"
                    + "--------------------------------\n\r"
                    + "    OBRIGADO E VOLTE SEMPRE!    \n\r"
                    + "\n\r \n\r \f"
            );

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings({"CallToPrintStackTrace", "UseSpecificCatch", "ConvertToTryWithResources"})
    public void impressao(String pTexto) { // \n\p para pular linhas e \f para fim da pagina

        try {
            InputStream prin = new ByteArrayInputStream(pTexto.getBytes());
            DocFlavor docFlavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            SimpleDoc documentoTexto = new SimpleDoc(prin, docFlavor, null);
            PrintService impressora = PrintServiceLookup.lookupDefaultPrintService(); //pega a impressora padrão            
            PrintRequestAttributeSet printerAttrinutes = new HashPrintRequestAttributeSet();
            printerAttrinutes.add(new JobName("Impressão", null));
            printerAttrinutes.add(OrientationRequested.PORTRAIT);
            printerAttrinutes.add(MediaSizeName.ISO_A4); //informa o tipo de folha
            DocPrintJob printJob = impressora.createPrintJob();

            try {
                printJob.print(documentoTexto, (PrintRequestAttributeSet) printerAttrinutes); //tenta imprimir
            } catch (PrintException e) {
                JOptionPane.showMessageDialog(rootPane, "Não foi possível imprimir erro: " + e.getMessage());
            }

            prin.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
        titulo = new javax.swing.JLabel();
        barCompra = new javax.swing.JScrollPane();
        tabelaCompra = new javax.swing.JTable();
        total = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        pesquisaProduto = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        nome = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        quantVenda = new javax.swing.JTextField();
        preco = new javax.swing.JTextField();
        imprimir = new javax.swing.JButton();
        labelImprimir = new javax.swing.JLabel();
        idOrcamento = new javax.swing.JLabel();
        idProduto = new javax.swing.JLabel();
        botExcluir = new javax.swing.JButton();
        labelExcluir = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(24, 131, 215), 1, true));

        titulo.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titulo.setText("Orçamento");

        tabelaCompra.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        tabelaCompra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabelaCompra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaCompraMouseClicked(evt);
            }
        });
        barCompra.setViewportView(tabelaCompra);

        total.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        total.setForeground(new java.awt.Color(0, 51, 255));
        total.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        total.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Total", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 24))); // NOI18N

        jLabel10.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel10.setText("Nome Produto");

        pesquisaProduto.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel11.setText("ID Produto");

        jLabel13.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel13.setText("Preço");

        nome.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        nome.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nome.setEnabled(false);
        nome.setOpaque(false);

        jLabel14.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel14.setText("Quantidade");

        quantVenda.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        quantVenda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                quantVendaKeyPressed(evt);
            }
        });

        preco.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        preco.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        preco.setEnabled(false);
        preco.setOpaque(false);

        imprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/cupom 50.png"))); // NOI18N
        imprimir.setContentAreaFilled(false);
        imprimir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                imprimirMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                imprimirMouseExited(evt);
            }
        });
        imprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imprimirActionPerformed(evt);
            }
        });

        labelImprimir.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelImprimir.setText("Imprimir");

        botExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/apagar 40.png"))); // NOI18N
        botExcluir.setContentAreaFilled(false);
        botExcluir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botExcluirMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botExcluirMouseExited(evt);
            }
        });
        botExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botExcluirActionPerformed(evt);
            }
        });

        labelExcluir.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelExcluir.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelExcluir.setText("Excluir");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(pesquisaProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(79, 79, 79)
                        .addComponent(idOrcamento, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(259, 259, 259)
                        .addComponent(idProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel10))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(nome, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel13)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(preco, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel14))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(quantVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(109, 109, 109)
                        .addComponent(botExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(98, 98, 98)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(labelExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(imprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelImprimir))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(barCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 639, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 1081, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pesquisaProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(idOrcamento, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(21, 21, 21)
                        .addComponent(idProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(nome, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(preco, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(quantVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(botExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(labelExcluir)
                                .addGap(8, 8, 8)
                                .addComponent(imprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(labelImprimir))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(barCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))))
        );

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

        setSize(new java.awt.Dimension(1040, 566));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void imprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imprimirActionPerformed
        imprimirCupom();
        delete();
        readTableCompra("select * from orcamento where idOrcamento='" + idOrcamento.getText() + "'");
    }//GEN-LAST:event_imprimirActionPerformed

    private void imprimirMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imprimirMouseEntered
        labelImprimir.setText("<html><u>Imprimir</u></html>");
    }//GEN-LAST:event_imprimirMouseEntered

    private void imprimirMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imprimirMouseExited
        labelImprimir.setText("<html>Imprimir</html>");
    }//GEN-LAST:event_imprimirMouseExited

    private void quantVendaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_quantVendaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            controlOrcamento control = new controlOrcamento();
            Beans mod = new Beans();

            String format = preco.getText().replace("R$", "").replace(",", ".");
            float result = Float.parseFloat(format) * Float.parseFloat(quantVenda.getText());
            total.setText(String.valueOf(result));

            mod.setIdOrcamento(idOrcamento.getText());
            mod.setNome(nome.getText());
            mod.setIdProduto(pesquisaProduto.getText());
            mod.setVenda(preco.getText());
            mod.setQuantVenda(quantVenda.getText());
            mod.setTotal(total.getText());
            control.create(mod);

            readTableCompra("select * from orcamento where idOrcamento='" + idOrcamento.getText() + "'");
            somarValores();

            nome.setText("");
            preco.setText("");
            quantVenda.setText("");
            pesquisaProduto.setText("");

            controlFocus();
        }
    }//GEN-LAST:event_quantVendaKeyPressed

    private void botExcluirMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botExcluirMouseEntered
        labelExcluir.setText("<html><u>Excluir</u></html>");
    }//GEN-LAST:event_botExcluirMouseEntered

    private void botExcluirMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botExcluirMouseExited
        labelExcluir.setText("<html>Excluir</html>");
    }//GEN-LAST:event_botExcluirMouseExited

    @SuppressWarnings("CallToPrintStackTrace")
    private void botExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botExcluirActionPerformed
        conexao.conecta();
        conexao.executaSQL("select * from orcamento");
        try {
            conexao.rs.first();
            stmt = conexao.conn.prepareStatement("delete from orcamento where idProduto='" + idProduto.getText() + "'");
            stmt.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        nome.setText("");
        preco.setText("");
        quantVenda.setText("");
        pesquisaProduto.setText("");

        readTableCompra("select * from orcamento where idOrcamento='" + idOrcamento.getText() + "'");
        somarValores();
    }//GEN-LAST:event_botExcluirActionPerformed

    private void tabelaCompraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaCompraMouseClicked
        int linha = tabelaCompra.getSelectedRow();
        idProduto.setText(tabelaCompra.getValueAt(linha, 0).toString());
        nome.setText(tabelaCompra.getValueAt(linha, 1).toString());
        preco.setText(tabelaCompra.getValueAt(linha, 2).toString());
        quantVenda.setText(tabelaCompra.getValueAt(linha, 3).toString());
    }//GEN-LAST:event_tabelaCompraMouseClicked

    @SuppressWarnings("CallToPrintStackTrace")
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int row = tabelaCompra.getRowCount();
        if (row != 0) {
            int resposta = JOptionPane.showConfirmDialog(rootPane, "Tem certeza que deseja cancelar este orçamento?");
            switch (resposta) {
                case JOptionPane.YES_OPTION:
                    conexao.conecta();
                    conexao.executaSQL("select * from orcamento");
                    try {
                        conexao.rs.first();
                        String id = idOrcamento.getText();
                        stmt = conexao.conn.prepareStatement("delete from orcamento where idOrcamento='" + id + "'");
                        stmt.execute();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    conexao.desconecta();
                    break;
                case JOptionPane.NO_OPTION:
                    this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                    break;
                case JOptionPane.CANCEL_OPTION:
                    this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                    break;
                default:
                    break;
            }
        }
    }//GEN-LAST:event_formWindowClosing

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
            java.util.logging.Logger.getLogger(Orcamento.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        }
        //</editor-fold>
        //</editor-fold>
        
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Orcamento().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane barCompra;
    private javax.swing.JButton botExcluir;
    private javax.swing.JLabel idOrcamento;
    private javax.swing.JLabel idProduto;
    private javax.swing.JButton imprimir;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel labelExcluir;
    private javax.swing.JLabel labelImprimir;
    private javax.swing.JTextField nome;
    private javax.swing.JTextField pesquisaProduto;
    private javax.swing.JTextField preco;
    private javax.swing.JTextField quantVenda;
    private javax.swing.JTable tabelaCompra;
    private javax.swing.JLabel titulo;
    private javax.swing.JLabel total;
    // End of variables declaration//GEN-END:variables
}
