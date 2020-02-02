package viewer;

import controlConnetion.conectaBanco;
import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

public final class Grafico extends javax.swing.JDialog {

    conectaBanco conexao = new conectaBanco();
    String data, produto, strData1, strData2, formatData1, formatData2, dataAtual;
    double dinheiro, fiadores, caixaInicial, aporte, gastos, debito, credito, troco;

    public Grafico(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        data = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
        dataAtual = new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis()));
        gerarGrafico(panel);
    }

    public void dataFormatada() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        strData1 = sdf.format(this.chooser1.getDate());
        strData2 = sdf.format(this.chooser2.getDate());

        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
        formatData1 = sd.format(this.chooser1.getDate());
        formatData2 = sd.format(this.chooser1.getDate());        
    }

    public void gerarGrafico(JPanel p) {

        //select para pegar a quantidade de abertura de caixa e nome da pessoa que abriu
        conexao.conecta();
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
        conexao.executaSQL("select sum(cartCredito) from pagamento where dia='" + data + "'");
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
        conexao.desconecta();

        double somaTotal = ((((caixaInicial + aporte + dinheiro + credito + debito) - troco) - fiadores) - gastos);
        String resultado = String.format("%.2f", somaTotal); //coloca duas casas decimais
        total.setText(resultado);

        DefaultCategoryDataset barra = new DefaultCategoryDataset();
        barra.setValue(caixaInicial, "Caixa Inicial", "");
        barra.setValue(aporte, "Aporte", "");
        barra.setValue(dinheiro, "Dinheiro", "");
        barra.setValue(credito, "Crédito", "");
        barra.setValue(debito, "Debito", "");
        barra.setValue(troco, "Troco", "");
        barra.setValue(fiadores, "Fiadores", "");
        barra.setValue(gastos, "Gastos", "");
        JFreeChart grafico = ChartFactory.createBarChart("Estatísticas dos Lucros/Gastos", dataAtual, "Valores", barra, PlotOrientation.VERTICAL, true, true, false);
        final CategoryPlot plot = grafico.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        ((BarRenderer) plot.getRenderer()).setBarPainter(new StandardBarPainter());
        ChartPanel chartPanel = new ChartPanel(grafico);
        p.add(chartPanel, BorderLayout.CENTER);
    }

    public void gerarGraficoPesquisa(JPanel p) {
        SwingUtilities.updateComponentTreeUI(this); //da um refresh no panel do grafico
        dataFormatada();

        //select para pegar a quantidade paga em dinheiro
        conexao.conecta();
        conexao.executaSQL("select sum(dinheiro) from pagamento where dia='" + strData1 + "' and dia <='" + strData2 + "'");
        try {
            conexao.rs.first();
            dinheiro = (conexao.rs.getDouble(1));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //select para pegar a quantidade paga em cartão de credito
        conexao.conecta();
        conexao.executaSQL("select sum(cartCredito) from pagamento where dia='" + strData1 + "' and dia <='" + strData2 + "'");
        try {
            conexao.rs.first();
            credito = (conexao.rs.getDouble(1));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //select para pegar a quantidade paga em cartão de debito
        conexao.conecta();
        conexao.executaSQL("select sum(cartDebito) from pagamento where dia='" + strData1 + "' and dia <='" + strData2 + "'");
        try {
            conexao.rs.first();
            debito = (conexao.rs.getDouble(1));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //select para pegar a quantidade do troco total
        conexao.conecta();
        conexao.executaSQL("select sum(troco) from pagamento where dia='" + strData1 + "' and dia <='" + strData2 + "'");
        try {
            conexao.rs.first();
            troco = (conexao.rs.getDouble(1));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //select para pegar a quantidade paga fiadores
        conexao.conecta();
        conexao.executaSQL("select sum(total) from fiadores where dataCompra='" + strData1 + "' and dataCompra <='" + strData2 + "'");
        try {
            conexao.rs.first();
            fiadores = (conexao.rs.getDouble(1));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //select para pegar a quantidade paga gastos
        conexao.conecta();
        conexao.executaSQL("select sum(valor) from gastos where data='" + strData1 + "' and data <='" + strData2 + "'");
        try {
            conexao.rs.first();
            gastos = (conexao.rs.getDouble(1));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        conexao.desconecta();

        double somaTotal = ((((dinheiro + credito + debito) - troco) - fiadores) - gastos);
        String resultado = String.format("%.2f", somaTotal); //coloca duas casas decimais
        total.setText(resultado);
        
        DefaultCategoryDataset barra = new DefaultCategoryDataset();
        barra.setValue(dinheiro, "Dinheiro", "");
        barra.setValue(credito, "Crédito", "");
        barra.setValue(debito, "Debito", "");
        barra.setValue(troco, "Troco", "");
        barra.setValue(fiadores, "Fiadores", "");
        barra.setValue(gastos, "Gastos", "");
        JFreeChart grafico = ChartFactory.createBarChart("Estatísticas dos Lucros/Gastos", formatData1 + " Até " + formatData2, "Valores", barra, PlotOrientation.VERTICAL, true, true, false);
        final CategoryPlot plot = grafico.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        ((BarRenderer) plot.getRenderer()).setBarPainter(new StandardBarPainter());
        ChartPanel chartPanel = new ChartPanel(grafico);
        p.add(chartPanel, BorderLayout.CENTER);
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
        panel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        gerar = new javax.swing.JButton();
        total = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        chooser1 = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        chooser2 = new com.toedter.calendar.JDateChooser();
        limpar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        panel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel.setLayout(new java.awt.BorderLayout());

        jLabel1.setBackground(new java.awt.Color(222, 118, 31));
        jLabel1.setFont(new java.awt.Font("Segoe UI Symbol", 0, 26)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Gráficos");
        jLabel1.setOpaque(true);

        gerar.setBackground(new java.awt.Color(13, 121, 29));
        gerar.setFont(new java.awt.Font("Swis721 LtEx BT", 1, 16)); // NOI18N
        gerar.setForeground(new java.awt.Color(255, 255, 255));
        gerar.setText("Gerar");
        gerar.setContentAreaFilled(false);
        gerar.setOpaque(true);
        gerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gerarActionPerformed(evt);
            }
        });

        total.setFont(new java.awt.Font("Tahoma", 1, 26)); // NOI18N
        total.setForeground(new java.awt.Color(0, 51, 255));
        total.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        total.setText("R$ 0,00");
        total.setToolTipText("");
        total.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Segoe UI Semilight", 1, 22)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("De");

        chooser1.setDateFormatString("yyyy-MM-dd");
        chooser1.setFont(new java.awt.Font("Segoe UI Symbol", 0, 13)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI Semilight", 1, 22)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Até");

        chooser2.setDateFormatString("yyyy-MM-dd");
        chooser2.setFont(new java.awt.Font("Segoe UI Symbol", 0, 13)); // NOI18N

        limpar.setBackground(new java.awt.Color(195, 15, 15));
        limpar.setFont(new java.awt.Font("Swis721 LtEx BT", 1, 16)); // NOI18N
        limpar.setForeground(new java.awt.Color(255, 255, 255));
        limpar.setText("Limpar");
        limpar.setContentAreaFilled(false);
        limpar.setOpaque(true);
        limpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limparActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(chooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(chooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(gerar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(limpar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                        .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                        .addComponent(chooser1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(chooser2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(gerar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(limpar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(916, 639));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void gerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gerarActionPerformed
        gerarGraficoPesquisa(panel);
    }//GEN-LAST:event_gerarActionPerformed

    private void limparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limparActionPerformed
        SwingUtilities.updateComponentTreeUI(this);
        panel.removeAll(); //remove tudo do panel do grafico
    }//GEN-LAST:event_limparActionPerformed

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
            java.util.logging.Logger.getLogger(Grafico.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            Grafico dialog = new Grafico(new javax.swing.JFrame(), true);
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
    private com.toedter.calendar.JDateChooser chooser1;
    private com.toedter.calendar.JDateChooser chooser2;
    private javax.swing.JButton gerar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton limpar;
    private javax.swing.JPanel panel;
    private javax.swing.JLabel total;
    // End of variables declaration//GEN-END:variables
}
