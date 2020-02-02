package viewer;

import controlConnetion.conectaBanco;
import controlViwer.controlFiador;
import java.awt.Color;
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
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import modelBeans.Beans;

public final class PagamentoFiador extends javax.swing.JDialog {

    conectaBanco conexao = new conectaBanco();
    List<Object> arrayParametros = new ArrayList<>();
    controlFiador control = new controlFiador();
    Beans mod = new Beans();
    PreparedStatement stmt = null;
    ResultSet rs = null;
    JDialog dialog;
    JFrame frame;
    

    public PagamentoFiador(java.awt.Frame parent, boolean modal, String t, int id, String nome) {
        super(parent, modal);
        initComponents();
        this.frame = (JFrame) parent;
        total.setText(t);
        idVenda.setText(String.valueOf(id));
        nomeCliente.setText(nome);
        compra.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis())));
        componentes();
    }

    private PagamentoFiador(JFrame jFrame, boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void componentes() {
        titulo.setOpaque(true);
        titulo.setBackground(new Color(24, 131, 215));
        titulo.setForeground(Color.WHITE);
        total.setBackground(Color.WHITE);
        total.setForeground(Color.BLUE);
        mensagem.setForeground(Color.red);
        idVenda.setForeground(new Color(255, 255, 255));
        dia.setForeground(Color.white);
        getContentPane().setBackground(Color.white);
    }

    public void salvarPagamento() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dataBanco = sdf.format(this.calendar.getDate());
        
        mod.setIdVenda(idVenda.getText());
        mod.setNome(nomeCliente.getText());
        mod.setDia(dataBanco);
        mod.setTotal(total.getText().replace("R$", "").replace(",", "."));
        control.insert(mod);
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
                    + "Cliente: " + nomeCliente.getText() + "\n\r"
                    + "-------------------------------\n\r"
                    + "         LISTA DE ITENS\n\r"
                    + "-------------------------------\n\r"
                    + "DESCRICAO       PRECO      QT  \n\r"
                    + arrayParametros.toString().replace("[", "").replace("]", "") + "\n\r"
                    + "-------------------------------\n\r"
                    + " VALOR TOTAL: " + total.getText() + "\n\r"
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
        total = new javax.swing.JLabel();
        mensagem = new javax.swing.JLabel();
        botSair = new javax.swing.JLabel();
        botOk = new javax.swing.JLabel();
        idVenda = new javax.swing.JLabel();
        dia = new javax.swing.JLabel();
        calendar = new com.toedter.calendar.JCalendar();
        nomeCliente = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        compra = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        pagamento = new javax.swing.JTextField();
        adicionar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(24, 131, 215)));

        titulo.setBackground(new java.awt.Color(0, 0, 255));
        titulo.setFont(new java.awt.Font("Arial", 1, 28)); // NOI18N
        titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titulo.setText("Tela de Pagamento");

        total.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        total.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        total.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Total", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 18))); // NOI18N

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

        calendar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        nomeCliente.setFont(new java.awt.Font("Segoe UI Semilight", 0, 16)); // NOI18N
        nomeCliente.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nomeCliente.setEnabled(false);
        nomeCliente.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI Symbol", 0, 16)); // NOI18N
        jLabel1.setText("Nome");

        jLabel2.setFont(new java.awt.Font("Segoe UI Symbol", 0, 16)); // NOI18N
        jLabel2.setText("Data Compra");

        compra.setFont(new java.awt.Font("Segoe UI Semilight", 0, 16)); // NOI18N
        compra.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        compra.setEnabled(false);
        compra.setOpaque(false);

        jLabel3.setFont(new java.awt.Font("Segoe UI Symbol", 0, 16)); // NOI18N
        jLabel3.setText("Data Pagamento");

        pagamento.setFont(new java.awt.Font("Segoe UI Semilight", 0, 16)); // NOI18N

        adicionar.setBackground(new java.awt.Color(28, 50, 130));
        adicionar.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        adicionar.setForeground(new java.awt.Color(255, 255, 255));
        adicionar.setText("Adicionar");
        adicionar.setContentAreaFilled(false);
        adicionar.setOpaque(true);
        adicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adicionarActionPerformed(evt);
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
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(mensagem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(idVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dia, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 107, Short.MAX_VALUE)
                        .addComponent(botOk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(botSair))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(total, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nomeCliente, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(compra, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pagamento, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(adicionar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(calendar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(compra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(calendar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(adicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botSair, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botOk, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(idVenda, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dia, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(mensagem, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))))
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

        setSize(new java.awt.Dimension(598, 445));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void botSairMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botSairMouseClicked

        this.dispose();
    }//GEN-LAST:event_botSairMouseClicked

    private void botOkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botOkMouseClicked

        int resposta = JOptionPane.showConfirmDialog(rootPane, "Finalizar compra?");
        switch (resposta) {
            case JOptionPane.YES_OPTION:
                imprimirCupom();
                salvarPagamento();
                VendaFiador v = (VendaFiador) frame;
                v.atualizar();
                this.dispose();
                break;
            case JOptionPane.NO_OPTION:
                break;
            case JOptionPane.CANCEL_OPTION:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_botOkMouseClicked

    private void adicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adicionarActionPerformed
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
        pagamento.setText(sd.format(this.calendar.getDate()));
    }//GEN-LAST:event_adicionarActionPerformed

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
            java.util.logging.Logger.getLogger(PagamentoFiador.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            PagamentoFiador dialog = new PagamentoFiador(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton adicionar;
    private javax.swing.JLabel botOk;
    private javax.swing.JLabel botSair;
    private com.toedter.calendar.JCalendar calendar;
    private javax.swing.JTextField compra;
    private javax.swing.JLabel dia;
    private javax.swing.JLabel idVenda;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel mensagem;
    private javax.swing.JTextField nomeCliente;
    private javax.swing.JTextField pagamento;
    private javax.swing.JLabel titulo;
    private javax.swing.JLabel total;
    // End of variables declaration//GEN-END:variables
}
