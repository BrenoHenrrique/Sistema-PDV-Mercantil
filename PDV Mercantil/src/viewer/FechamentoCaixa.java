package viewer;

import controlConnetion.conectaBanco;
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
import java.util.Locale;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import modelBeans.Table;

public final class FechamentoCaixa extends javax.swing.JFrame {

    conectaBanco conexao = new conectaBanco();
    String data;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    List<Object> arrayParametros = new ArrayList<>();

    String responsavel;
    double dinheiro, fiadores, caixaInicial, aporte, gastos, total, debito, credito, troco;

    public FechamentoCaixa() {
        initComponents();
        componentes();
        data = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
        readTablePagamento("select * from pagamento where dia='" + data + "'");
        tabelaPagamento.getTableHeader().setDefaultRenderer(new FechamentoCaixa.header());
        calculoTotal();
    }

    public void componentes() {

        totalFinal.setText("0.00");
        totalEntrega.setText("0.00");
        totalTroco.setText("0.00");
        totalVenda.setText("0.00");
        barTable.getViewport().setBackground(Color.white);
    }

    public void zerarDados() {

        conexao.conecta();
        conexao.executaSQL("select * from caixa");
        try {
            conexao.rs.first();
            String v = conexao.rs.getString("verificador");
            if (v.equals("aberto")) {
                totalFinal.setText("0.00");
                totalEntrega.setText("0.00");
                totalTroco.setText("0.00");
                totalVenda.setText("0.00");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conexao.desconecta();
    }
    
    public void calculoTotal() {

        conexao.conecta();
        conexao.executaSQL("select sum(total) from historico where diaVenda='" + data + "'");
        try {
            Locale.setDefault(new Locale("pt", "BR"));
            DecimalFormat df = new DecimalFormat();
            df.applyPattern("####0.00");
            conexao.rs.first();
            double sumTotal = (conexao.rs.getDouble(1));
            totalVenda.setText(df.format(sumTotal));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        conexao.executaSQL("select * from caixa");
        try {
            conexao.rs.first();
            Locale.setDefault(new Locale("pt", "BR"));
            DecimalFormat df = new DecimalFormat();
            df.applyPattern("####0.00");
            double b = conexao.rs.getDouble("total");
            double a = conexao.rs.getDouble("aporte");
            totalTroco.setText(df.format(b + a));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        conexao.executaSQL("select sum(valor) from gastos where data='" + data + "'");
        try {
            Locale.setDefault(new Locale("pt", "BR"));
            DecimalFormat df = new DecimalFormat();
            df.applyPattern("####0.00");
            conexao.rs.first();
            double totalCompra = (conexao.rs.getDouble(1));
            totalGastos.setText(df.format(totalCompra));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Locale.setDefault(new Locale("pt", "BR"));
        DecimalFormat df = new DecimalFormat();
        df.applyPattern("####0.00");
        String x = totalTroco.getText().replace(",", ".");
        String y = totalEntrega.getText().replace(",", ".");
        String z = totalGastos.getText().replace(",", ".");
        String k = totalVenda.getText().replace(",", ".");
        double sub = ((Double.parseDouble(k) + Double.parseDouble(y) + Double.parseDouble(x)) - Double.parseDouble(z));
        totalFinal.setText(df.format(sub));

        conexao.desconecta();
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void imprimirCupom() {

        String parametros = "";
        arrayParametros.add(parametros);

        //select pega o responsavel por abrir o caixa
        conexao.conecta();
        conexao.executaSQL("select * from caixa");
        try {
            conexao.rs.first();
            responsavel = conexao.rs.getString("responsavel");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //select para pegar a quantidade de abertura de caixa e nome da pessoa que abriu
        conexao.executaSQL("select * from caixa");
        try {
            conexao.rs.first();
            caixaInicial = conexao.rs.getDouble("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //select para pegar a quantidade aporte
        conexao.executaSQL("select * from caixa");
        try {
            conexao.rs.first();
            aporte = conexao.rs.getDouble("aporte");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //select para pegar a quantidade paga em dinheiro
        conexao.conecta();
        conexao.executaSQL("select sum(dinheiro) from pagamento where dia='" + data + "'");
        try {
            conexao.rs.first();
            dinheiro = (conexao.rs.getDouble(1));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //select para pegar a quantidade paga em cartão de credito
        conexao.conecta();
        conexao.executaSQL("select sum(totalFinal) from parcelamento where dataCompra='" + data + "'");
        try {
            conexao.rs.first();
            credito = (conexao.rs.getDouble(1));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //select para pegar a quantidade paga em cartão de debito
        conexao.conecta();
        conexao.executaSQL("select sum(cartDebito) from pagamento where dia='" + data + "'");
        try {
            conexao.rs.first();
            debito = (conexao.rs.getDouble(1));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //select para pegar a quantidade do troco total
        conexao.conecta();
        conexao.executaSQL("select sum(troco) from pagamento where dia='" + data + "'");
        try {
            conexao.rs.first();
            troco = (conexao.rs.getDouble(1));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //select para pegar a quantidade paga fiadores
        conexao.conecta();
        conexao.executaSQL("select sum(total) from fiadores where dataCompra='" + data + "'");
        try {
            conexao.rs.first();
            fiadores = (conexao.rs.getDouble(1));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //select para pegar a quantidade paga gastos
        conexao.conecta();
        conexao.executaSQL("select sum(valor) from gastos where data='" + data + "'");
        try {
            conexao.rs.first();
            gastos = (conexao.rs.getDouble(1));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //quantidade total depois dos descontos
        total = ((((caixaInicial + aporte + dinheiro + credito + debito) - troco) - fiadores) - gastos);

        for (int i = 0; i < arrayParametros.size(); i++) { //printa o array, mas não está dando certo, por que?
            System.out.println(arrayParametros.get(i));
        }

        String dia = new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis()));
        String hora = new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis()));

        this.impressao("   FECHAMENTO DE CAIXA   \n\r"
                + "Data e Hora: " + dia + " - " + hora + "\n\r"
                + "-------------------------------\n\r"
                + "Responsaval: " + responsavel + "\n\r"
                + "Caixa Inicial: " + caixaInicial + "\n\r"
                + "Aportes: " + aporte + "\n\r"
                + "Dinheiro: " + dinheiro + "\n\r"
                + "Credito: " + credito + "\n\r"
                + "Debito: " + debito + "\n\r"
                + "Troco: " + troco + "\n\r"
                + "Fiado: " + fiadores + "\n\r"
                + "-------------------------------\n\r"
                + "Total Caixa: " + total + "\n\r"
                + "\n\r \n\r \n\r \n\r\f"
        );

        conexao.desconecta();
    }

    @SuppressWarnings({"CallToPrintStackTrace", "UseSpecificCatch", "ConvertToTryWithResources"})
    public void impressao(String pTexto) { // \n\p para novar linhas e \f para fim da pagina
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
                e.printStackTrace();
            }
            prin.close();
        } catch (Exception ex) {
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
            setBackground(new Color(115, 70, 161));
            setForeground(new Color(255, 255, 255));
            setFont(new Font("Arial", Font.BOLD, 16));
            setHorizontalAlignment(JLabel.CENTER);
            return this;
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void readTablePagamento(String Sql) {

        conexao.conecta();
        ArrayList dados = new ArrayList();
        String[] colunas = new String[]{"ID Pagamento", "Dinheiro", "Crédito", "Débito", "Desconto", "Recebido", "Troco", "Total", "Dia"};
        conexao.executaSQL(Sql);
        try {
            conexao.rs.first();
            do {
                dados.add(new Object[]{
                    conexao.rs.getString("idPagamento"), conexao.rs.getString("dinheiro"), conexao.rs.getString("cartCredito"),
                    conexao.rs.getString("cartDebito"), conexao.rs.getString("desconto"), conexao.rs.getString("recebido"),
                    conexao.rs.getString("troco"), conexao.rs.getString("total"), conexao.rs.getString("dia")});
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

        tabelaPagamento.setModel(tabela);
        tabelaPagamento.getColumnModel().getColumn(0).setPreferredWidth(130);
        tabelaPagamento.getColumnModel().getColumn(0).setResizable(false);
        tabelaPagamento.getColumnModel().getColumn(0).setCellRenderer(centralizado);
        tabelaPagamento.getColumnModel().getColumn(0).setMinWidth(0); //ocultando o indice 0 idVenda
        tabelaPagamento.getColumnModel().getColumn(0).setMaxWidth(0); //ocultando o indice 0 idVenda
        tabelaPagamento.getColumnModel().getColumn(1).setPreferredWidth(130);
        tabelaPagamento.getColumnModel().getColumn(1).setResizable(false);
        tabelaPagamento.getColumnModel().getColumn(1).setCellRenderer(centralizado);
        tabelaPagamento.getColumnModel().getColumn(2).setPreferredWidth(132);
        tabelaPagamento.getColumnModel().getColumn(2).setResizable(false);
        tabelaPagamento.getColumnModel().getColumn(3).setPreferredWidth(132);
        tabelaPagamento.getColumnModel().getColumn(3).setResizable(false);
        tabelaPagamento.getColumnModel().getColumn(3).setCellRenderer(centralizado);
        tabelaPagamento.getColumnModel().getColumn(4).setPreferredWidth(132);
        tabelaPagamento.getColumnModel().getColumn(4).setResizable(false);
        tabelaPagamento.getColumnModel().getColumn(4).setCellRenderer(centralizado);
        tabelaPagamento.getColumnModel().getColumn(5).setPreferredWidth(132);
        tabelaPagamento.getColumnModel().getColumn(5).setResizable(false);
        tabelaPagamento.getColumnModel().getColumn(5).setCellRenderer(centralizado);
        tabelaPagamento.getColumnModel().getColumn(6).setPreferredWidth(132);
        tabelaPagamento.getColumnModel().getColumn(6).setResizable(false);
        tabelaPagamento.getColumnModel().getColumn(6).setCellRenderer(centralizado);
        tabelaPagamento.getColumnModel().getColumn(7).setPreferredWidth(132);
        tabelaPagamento.getColumnModel().getColumn(7).setResizable(false);
        tabelaPagamento.getColumnModel().getColumn(7).setCellRenderer(centralizado);
        tabelaPagamento.getColumnModel().getColumn(8).setPreferredWidth(132);
        tabelaPagamento.getColumnModel().getColumn(8).setResizable(false);
        tabelaPagamento.getColumnModel().getColumn(8).setCellRenderer(centralizado);
        tabelaPagamento.getTableHeader().setReorderingAllowed(false);
        tabelaPagamento.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabelaPagamento.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
        titulo = new javax.swing.JLabel();
        barTable = new javax.swing.JScrollPane();
        tabelaPagamento = new javax.swing.JTable();
        totalFinal = new javax.swing.JLabel();
        botPesquisa = new javax.swing.JButton();
        botAtualizar = new javax.swing.JButton();
        botAtualizar1 = new javax.swing.JButton();
        caixa = new javax.swing.JComboBox<>();
        totalEntrega = new javax.swing.JLabel();
        totalTroco = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        botGastos = new javax.swing.JLabel();
        totalVenda = new javax.swing.JLabel();
        totalGastos = new javax.swing.JLabel();
        botCaixa = new javax.swing.JLabel();
        botFechamento = new javax.swing.JLabel();
        botFechamento1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(24, 131, 215)));

        titulo.setBackground(new java.awt.Color(97, 44, 150));
        titulo.setFont(new java.awt.Font("Arial", 1, 28)); // NOI18N
        titulo.setForeground(new java.awt.Color(255, 255, 255));
        titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titulo.setText("Histórico de Pagamento");
        titulo.setOpaque(true);

        tabelaPagamento.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tabelaPagamento.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        barTable.setViewportView(tabelaPagamento);

        totalFinal.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        totalFinal.setForeground(new java.awt.Color(97, 44, 150));
        totalFinal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalFinal.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Total Final", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 18))); // NOI18N

        botPesquisa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/buscar 32.png"))); // NOI18N
        botPesquisa.setToolTipText("Pesquisar");
        botPesquisa.setContentAreaFilled(false);

        botAtualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/atualizar 32.png"))); // NOI18N
        botAtualizar.setToolTipText("Atualizar Tabela");
        botAtualizar.setContentAreaFilled(false);
        botAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botAtualizarActionPerformed(evt);
            }
        });

        botAtualizar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/hoje 32.png"))); // NOI18N
        botAtualizar1.setToolTipText("Histórico Hoje");
        botAtualizar1.setContentAreaFilled(false);
        botAtualizar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botAtualizar1ActionPerformed(evt);
            }
        });

        caixa.setFont(new java.awt.Font("Segoe UI Semilight", 0, 16)); // NOI18N
        caixa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "  Dinheiro", "  Cartão" }));
        caixa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                caixaKeyPressed(evt);
            }
        });

        totalEntrega.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        totalEntrega.setForeground(new java.awt.Color(6, 82, 31));
        totalEntrega.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalEntrega.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Total Entregas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 18))); // NOI18N

        totalTroco.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        totalTroco.setForeground(new java.awt.Color(159, 81, 19));
        totalTroco.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalTroco.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Troco Inicial", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 18))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Segoe UI Symbol", 0, 12)); // NOI18N
        jLabel1.setText("Quero pesquisar por:");

        botGastos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/gastos 35.png"))); // NOI18N
        botGastos.setToolTipText("Gastos");
        botGastos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botGastosMouseClicked(evt);
            }
        });

        totalVenda.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        totalVenda.setForeground(new java.awt.Color(0, 17, 91));
        totalVenda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalVenda.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Total Venda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 18))); // NOI18N

        totalGastos.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        totalGastos.setForeground(new java.awt.Color(226, 0, 1));
        totalGastos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalGastos.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Total Gastos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 18))); // NOI18N

        botCaixa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/caixa 32.png"))); // NOI18N
        botCaixa.setToolTipText("Aporte");
        botCaixa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botCaixaMouseClicked(evt);
            }
        });

        botFechamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/fechamento 35.png"))); // NOI18N
        botFechamento.setToolTipText("Fechar Caixa");
        botFechamento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botFechamentoMouseClicked(evt);
            }
        });

        botFechamento1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/cartao 35.png"))); // NOI18N
        botFechamento1.setToolTipText("Fechar Caixa");
        botFechamento1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botFechamento1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(titulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(barTable)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(caixa, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(totalTroco, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(totalGastos, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(totalVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(totalEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(totalFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(botPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(botAtualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(botAtualizar1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(botGastos)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(botCaixa)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(botFechamento1)
                                .addGap(5, 5, 5)
                                .addComponent(botFechamento)))
                        .addGap(0, 14, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(totalTroco, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(totalGastos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(totalEntrega, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(totalFinal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(totalVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(caixa, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(botAtualizar1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(botGastos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botAtualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(botPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botCaixa, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botFechamento, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botFechamento1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barTable, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1038, 739));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void botGastosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botGastosMouseClicked
        new Gastos(this).setVisible(true);
    }//GEN-LAST:event_botGastosMouseClicked

    private void botAtualizar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botAtualizar1ActionPerformed
        readTablePagamento("select * from pagamento where dia='" + data + "'");
        calculoTotal();
    }//GEN-LAST:event_botAtualizar1ActionPerformed

    private void botAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botAtualizarActionPerformed
        readTablePagamento("select * from pagamento where idPagamento > 0 order by idPagamento");
    }//GEN-LAST:event_botAtualizarActionPerformed

    private void caixaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_caixaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (caixa.getSelectedItem().equals("  Dinheiro")) {
                readTablePagamento("select * from pagamento where dinheiro > 0");
                calculoTotal();
            }
            if (caixa.getSelectedItem().equals("  Cartão")) {
                readTablePagamento("select * from pagamento where cartCredito > 0 or cartDebito > 0");
                calculoTotal();
            }
        }
    }//GEN-LAST:event_caixaKeyPressed

    private void botCaixaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botCaixaMouseClicked
        new Aporte(this, true).setVisible(true);
    }//GEN-LAST:event_botCaixaMouseClicked

    private void botFechamentoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botFechamentoMouseClicked
        int resposta = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja encerrar o caixa?");
        if (resposta == JOptionPane.YES_OPTION) {
            try {
                imprimirCupom();
                zerarDados();
                conexao.conecta();
                conexao.executaSQL("select * from caixa");
                conexao.rs.first();
                String id = conexao.rs.getString("idCaixa");
                stmt = conexao.conn.prepareStatement("update caixa set verificador='fechado', total='0', aporte='0', responsavel='null' where idCaixa='" + id + "'");
                stmt.execute();
                JOptionPane.showMessageDialog(null, "Caixa fechado com sucesso! Está tela está bloqueada ate abrir o caixa novamente.");
                this.dispose();
                conexao.desconecta();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_botFechamentoMouseClicked

    private void botFechamento1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botFechamento1MouseClicked
        new ParcelamentoLista(this, true).setVisible(true);
    }//GEN-LAST:event_botFechamento1MouseClicked

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
            java.util.logging.Logger.getLogger(FechamentoCaixa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
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
            new FechamentoCaixa().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane barTable;
    private javax.swing.JButton botAtualizar;
    private javax.swing.JButton botAtualizar1;
    private javax.swing.JLabel botCaixa;
    private javax.swing.JLabel botFechamento;
    private javax.swing.JLabel botFechamento1;
    private javax.swing.JLabel botGastos;
    private javax.swing.JButton botPesquisa;
    private javax.swing.JComboBox<String> caixa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTable tabelaPagamento;
    private javax.swing.JLabel titulo;
    private javax.swing.JLabel totalEntrega;
    private javax.swing.JLabel totalFinal;
    private javax.swing.JLabel totalGastos;
    private javax.swing.JLabel totalTroco;
    private javax.swing.JLabel totalVenda;
    // End of variables declaration//GEN-END:variables
}
