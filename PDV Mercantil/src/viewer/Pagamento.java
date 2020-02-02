package viewer;

import com.barcodelib.barcode.QRCode;
import controlConnetion.conectaBanco;
import controlViwer.controlPagamento;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
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
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import modelBeans.Beans;

public final class Pagamento extends javax.swing.JFrame {

    conectaBanco conexao = new conectaBanco();
    private Venda parent; //Declara o form1 - global
    List<Object> arrayParametros = new ArrayList<>();
    controlPagamento control = new controlPagamento();
    Beans mod = new Beans();
    PreparedStatement stmt = null;
    ResultSet rs = null;
    int udm = 0, resol = 72, rot = 0;
    float mi = 0.000f, md = 0.000f, ms = 0.000f, min = 0.000f, tam = 5.00f;

    public Pagamento(Venda p, String totalVenda, String id) { //métodos para receber o total e fazer eventos de outras telas
        initComponents();
        componentes();
        this.parent = p; //cria uma herença venda/pagamento
        total.setText(totalVenda); //pega o valor total passado pelo botão finalizar
        idVenda.setText(id);
        dia.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())));
    }

    private Pagamento() {
        initComponents();
    }

    public void componentes() {
        titulo.setOpaque(true);
        titulo.setBackground(new Color(24, 131, 215));
        titulo.setForeground(Color.WHITE);
        total.setBackground(Color.WHITE);
        total.setForeground(Color.BLUE);
        troco.setForeground(Color.RED);
        mensagem.setForeground(Color.red);
        idVenda.setForeground(new Color(255, 255, 255));
        dia.setForeground(Color.white);
        getContentPane().setBackground(Color.white);
    }

    public void calculos() {

        if (dinheiro.getText().equals("")) {
            dinheiro.setText("0");
        }
        if (credito.getText().equals("")) {
            credito.setText("0");
        }
        if (debito.getText().equals("")) {
            debito.setText("0");
        }

        float somaTotal = (Float.parseFloat(dinheiro.getText().replace(",", ".")) + Float.parseFloat(credito.getText().replace(",", ".")) + Float.parseFloat(debito.getText().replace(",", ".")));
        pago.setText(String.valueOf(somaTotal));

        Locale.setDefault(new Locale("pt", "BR"));
        DecimalFormat df = new DecimalFormat();
        df.applyPattern("####0.00");
        double p = Double.parseDouble(pago.getText().replace(",", "."));
        double t = Double.parseDouble(total.getText().replace(",", "."));
        troco.setText(df.format(p - t));
    }

    public void salvarPagamento() {

        if (desconto.getText().equals("")) {
            desconto.setText("0");
        }

        String totalFormatado = total.getText().replace(",", ".");
        String trocoFormatado = troco.getText().replace(",", ".");
        String pagoFormatado = pago.getText().replace(",", ".");

        mod.setDinheiro(dinheiro.getText().replace(",", "."));
        mod.setCredito(credito.getText().replace(",", "."));
        mod.setDebito(debito.getText().replace(",", "."));
        mod.setDesconto(desconto.getText().replace(",", "."));
        mod.setTroco(trocoFormatado);
        mod.setRecebido(pagoFormatado);
        mod.setTotal(totalFormatado);
        mod.setDia(dia.getText());
        control.create(mod);
    }

    public void gerarQR(String d) {
        try {
            QRCode c = new QRCode();
            c.setData(d);
            c.setDataMode(QRCode.MODE_BYTE);
            c.setUOM(udm);
            c.setLeftMargin(mi);
            c.setRightMargin(md);
            c.setTopMargin(ms);
            c.setBottomMargin(min);
            c.setResolution(resol);
            c.setRotate(rot);
            c.setModuleSize(tam);

            String arquivo = System.getProperty("user.home") + "/teste.gif";
            c.renderBarcode(arquivo);

            Desktop dk = Desktop.getDesktop();
            dk.open(new File(arquivo));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void imprimir(String str) {
        // cria um frame temporário, onde será desenhado o texto a ser impresso
        Frame f = new Frame("Frame temporário");
        f.pack();
        // pega o Toolkit do Frame
        Toolkit tk = f.getToolkit();
        // Pega os serviços de impressão existentes no computador,

        PrintJob pj = tk.getPrintJob(f, " print", null);
        // Aqui se inicia a impressão
        if (pj != null) {
            Graphics g = pj.getGraphics();
            g.drawImage(new ImageIcon("C:/A.jpg").getImage(), 0, 0, f);
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void imprimirCupom() {

        String parametros = "";

        arrayParametros.add(parametros);

        conexao.conecta();
        conexao.executaSQL("select * from historico where idVenda='" + idVenda.getText() + "'");

        try {
            conexao.rs.first();
            do {

                String nomeProduto = conexao.rs.getString("nome");
                String quantProd = conexao.rs.getString("quantVenda");
                String valorProd = conexao.rs.getString("total");

                if (nomeProduto.length() > 15) {
                    nomeProduto = nomeProduto.substring(0, 15);

                    parametros = nomeProduto + " R$ " + valorProd + "   " + quantProd + "\n\r";

                    arrayParametros.add(parametros);

                } else {
                    nomeProduto = (String.format("%-15s", nomeProduto));
                    nomeProduto = nomeProduto.substring(0, 15);

                    parametros = nomeProduto + " R$ " + valorProd + "   " + quantProd + "\n\r";

                    arrayParametros.add(parametros);
                }

            } while (conexao.rs.next());

            for (int i = 0; i < arrayParametros.size(); i++) { //printa o array, mas não está dando certo, por que?
                System.out.println(arrayParametros.get(i));
            }

            conexao.executaSQL("select * from caixa");
            conexao.rs.first();
            String nome = conexao.rs.getString("responsavel");

            String data = new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis()));
            String hora = new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis()));

            Locale.setDefault(new Locale("pt", "BR"));
            DecimalFormat df = new DecimalFormat();
            df.applyPattern("####0.00");

            this.impressao("   MERCADINHO PITAGUARY   \n\r"
                    + "Data e Hora: " + data + " - " + hora + "\n\r"
                    + "Endereco: R. Manuel Pereira 505\n\r"
                    + " Celular:(85) 98684-6927\n\r"
                    + "Telefone:(85) 3384-6282\n\r"
                    + "-------------------------------\n\r"
                    + "        CUPOM NAO FISCAL\n\r"
                    + "-------------------------------\n\r"
                    + "         LISTA DE ITENS\n\r"
                    + "-------------------------------\n\r"
                    + "DESCRICAO       PRECO      QT  \n\r"
                    + arrayParametros.toString().replace("[", "").replace("]", "") + "\n\r"
                    + "-------------------------------\n\r"
                    + " VALOR TOTAL: " + total.getText() + "\n\r"
                    + "    DINHEIRO: " + dinheiro.getText() + "\n\r"
                    + "     CREDITO: " + credito.getText() + "\n\r"
                    + "      DEBITO: " + debito.getText() + "\n\r"
                    + "    RECEBIDO: " + pago.getText() + "\n\r"
                    + "       TROCO: " + troco.getText() + "\n\r"
                    + "-------------------------------\n\r"
                    + "OBS:So e feita a troca de produtos com este cupom em maos.\r\n"
                    + "-------------------------------\n\r"
                    + "Balcao 1 Responsavel " + nome + "\n\r"
                    + "-------------------------------\n\r"
                    + "     OBRIGADO PELA PREFERENCIA!\n\r"
                    + "\n\r \n\r \n\r \n\r \n\r\f"
            );

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        arrayParametros.clear();//limpa o array, para não acumular itens
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
        dinheiro = new javax.swing.JTextField();
        total = new javax.swing.JLabel();
        pago = new javax.swing.JLabel();
        credito = new javax.swing.JTextField();
        debito = new javax.swing.JTextField();
        troco = new javax.swing.JLabel();
        mensagem = new javax.swing.JLabel();
        botSair = new javax.swing.JLabel();
        botOk = new javax.swing.JLabel();
        desconto = new javax.swing.JTextField();
        idVenda = new javax.swing.JLabel();
        dia = new javax.swing.JLabel();
        botParcelamento = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(24, 131, 215)));

        titulo.setBackground(new java.awt.Color(0, 0, 255));
        titulo.setFont(new java.awt.Font("Arial", 1, 28)); // NOI18N
        titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titulo.setText("Tela de Pagamento");

        dinheiro.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        dinheiro.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dinheiro", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N
        dinheiro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dinheiroKeyPressed(evt);
            }
        });

        total.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        total.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        total.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Total", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 18))); // NOI18N

        pago.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        pago.setForeground(new java.awt.Color(0, 153, 0));
        pago.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pago.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Valor Recebido", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 18))); // NOI18N

        credito.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        credito.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cartão Crédito", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N
        credito.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                creditoKeyPressed(evt);
            }
        });

        debito.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        debito.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cartão Débito", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N
        debito.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                debitoKeyPressed(evt);
            }
        });

        troco.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        troco.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        troco.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Troco", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 18))); // NOI18N

        mensagem.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        mensagem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mensagem.setText("Obrigado e volte sempre!");

        botSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/sair 40.png"))); // NOI18N
        botSair.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        botSair.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botSairMouseClicked(evt);
            }
        });

        botOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/ok 35.png"))); // NOI18N
        botOk.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        botOk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botOkMouseClicked(evt);
            }
        });

        desconto.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        desconto.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Desconto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N
        desconto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                descontoKeyPressed(evt);
            }
        });

        botParcelamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/cartao 40.png"))); // NOI18N
        botParcelamento.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        botParcelamento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botParcelamentoMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(mensagem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(idVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dia, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botOk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(botParcelamento)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(botSair))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(credito, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(debito, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dinheiro, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(48, 48, 48)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pago, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(desconto, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(48, 48, 48)
                                .addComponent(troco, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 8, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(titulo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dinheiro, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(credito, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(debito, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(pago, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(troco, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(desconto, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mensagem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botSair, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(idVenda, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dia, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(botParcelamento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(botOk, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(590, 438));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void debitoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_debitoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculos();
        }

        if (evt.getKeyCode() == KeyEvent.VK_F7) {
            if (!dinheiro.getText().equals("") || (!credito.getText().equals("")) || (!debito.getText().equals(""))) {
                int resposta = JOptionPane.showConfirmDialog(rootPane, "Finalizar compra?");
                switch (resposta) {
                    case JOptionPane.YES_OPTION:
                        imprimirCupom();
                        salvarPagamento();
                        this.dispose();
                        Venda v = (Venda) parent;
                        v.atualizar();
                        break;
                    case JOptionPane.NO_OPTION:
                        break;
                    case JOptionPane.CANCEL_OPTION:
                        break;
                    default:
                        break;
                }
            } else {
                JOptionPane.showMessageDialog(rootPane, "Nenhum valor foi informado. Informe o valor dado pelo cliente!");
            }
        }
    }//GEN-LAST:event_debitoKeyPressed

    private void creditoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_creditoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculos();
        }

        if (evt.getKeyCode() == KeyEvent.VK_F7) {
            if (!dinheiro.getText().equals("") || (!credito.getText().equals("")) || (!debito.getText().equals(""))) {
                int resposta = JOptionPane.showConfirmDialog(rootPane, "Finalizar compra?");
                switch (resposta) {
                    case JOptionPane.YES_OPTION:
                        imprimirCupom();
                        salvarPagamento();
                        this.dispose();
                        Venda v = (Venda) parent;
                        v.atualizar();
                        break;
                    case JOptionPane.NO_OPTION:
                        break;
                    case JOptionPane.CANCEL_OPTION:
                        break;
                    default:
                        break;
                }
            } else {
                JOptionPane.showMessageDialog(rootPane, "Nenhum valor foi informado. Informe o valor dado pelo cliente!");
            }
        }
    }//GEN-LAST:event_creditoKeyPressed

    private void dinheiroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dinheiroKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculos();
        }

        if (evt.getKeyCode() == KeyEvent.VK_F7) {
            if (!dinheiro.getText().equals("") || (!credito.getText().equals("")) || (!debito.getText().equals(""))) {
                int resposta = JOptionPane.showConfirmDialog(rootPane, "Finalizar compra?");
                switch (resposta) {
                    case JOptionPane.YES_OPTION:
                        imprimirCupom();
                        salvarPagamento();
                        this.dispose();
                        Venda v = (Venda) parent;
                        v.atualizar();
                        break;
                    case JOptionPane.NO_OPTION:
                        break;
                    case JOptionPane.CANCEL_OPTION:
                        break;
                    default:
                        break;
                }
            } else {
                JOptionPane.showMessageDialog(rootPane, "Nenhum valor foi informado. Informe o valor dado pelo cliente!");
            }
        }
    }//GEN-LAST:event_dinheiroKeyPressed

    private void botSairMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botSairMouseClicked

        this.dispose();
    }//GEN-LAST:event_botSairMouseClicked

    private void botOkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botOkMouseClicked

        if (!dinheiro.getText().equals("") || (!credito.getText().equals("")) || (!debito.getText().equals(""))) {
            int resposta = JOptionPane.showConfirmDialog(rootPane, "Finalizar compra?");
            switch (resposta) {
                case JOptionPane.YES_OPTION:
                    imprimirCupom();
                    salvarPagamento();
                    this.dispose();
                    Venda v = (Venda) parent;
                    v.atualizar();
                    break;
                case JOptionPane.NO_OPTION:
                    break;
                case JOptionPane.CANCEL_OPTION:
                    break;
                default:
                    break;
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Nenhum valor foi informado. Informe o valor dado pelo cliente!");
        }
    }//GEN-LAST:event_botOkMouseClicked

    private void descontoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descontoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            String strT = total.getText().replace("R$", "").replace(",", ".");
            String strD = desconto.getText();

            float valorT = Float.parseFloat(strT);
            float valorD = Float.parseFloat(strD);

            valorT = valorT - (valorT * (valorD / 100));
            total.setText(String.valueOf(valorT));

            float totalFormat = Float.parseFloat(String.valueOf(valorT));
            DecimalFormat df = new DecimalFormat("##,##0.00");
            String s = df.format(totalFormat);
            total.setText("R$ " + s);
        }
        calculos();

        if (evt.getKeyCode() == KeyEvent.VK_F7) {
            if (!dinheiro.getText().equals("") || (!credito.getText().equals("")) || (!debito.getText().equals(""))) {
                int resposta = JOptionPane.showConfirmDialog(rootPane, "Finalizar compra?");
                switch (resposta) {
                    case JOptionPane.YES_OPTION:
                        imprimirCupom();
                        salvarPagamento();
                        this.dispose();
                        Venda v = (Venda) parent;
                        v.atualizar();
                        break;
                    case JOptionPane.NO_OPTION:
                        break;
                    case JOptionPane.CANCEL_OPTION:
                        break;
                    default:
                        break;
                }
            } else {
                JOptionPane.showMessageDialog(rootPane, "Nenhum valor foi informado. Informe o valor dado pelo cliente!");
            }
        }
    }//GEN-LAST:event_descontoKeyPressed

    private void botParcelamentoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botParcelamentoMouseClicked
        new Parcelamento(this, true, total.getText().replace(",", ".")).setVisible(true);
    }//GEN-LAST:event_botParcelamentoMouseClicked

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
            java.util.logging.Logger.getLogger(Pagamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Pagamento().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel botOk;
    private javax.swing.JLabel botParcelamento;
    private javax.swing.JLabel botSair;
    private javax.swing.JTextField credito;
    private javax.swing.JTextField debito;
    private javax.swing.JTextField desconto;
    private javax.swing.JLabel dia;
    private javax.swing.JTextField dinheiro;
    private javax.swing.JLabel idVenda;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel mensagem;
    private javax.swing.JLabel pago;
    private javax.swing.JLabel titulo;
    private javax.swing.JLabel total;
    private javax.swing.JLabel troco;
    // End of variables declaration//GEN-END:variables
}
