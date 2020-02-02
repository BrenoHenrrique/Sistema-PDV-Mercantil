package viewer;

import controlViwer.controlParcelamento;
import java.io.IOException;
import java.sql.Date;
import javax.swing.JFrame;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.swing.JOptionPane;
import modelBeans.Beans;

public final class Parcelamento extends javax.swing.JDialog {

    controlParcelamento control = new controlParcelamento();
    Beans mod = new Beans();
    String status;
    String data;
    int verificarParcela;

    public Parcelamento(java.awt.Frame parent, boolean modal, String total) {
        super(parent, modal);
        initComponents();
        totalParcial.setText(total);
        data = new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis()));
        valorParcela();
    }

    Parcelamento(JFrame jFrame, boolean b) {
        initComponents();
        totalParcial.setText("1250.50");
        parcela.setText("0.00");
        totalFinal.setText("0.00");
        data = new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis()));
        valorParcela();
    }

    public void valorParcela() {
        Double total = Double.parseDouble(totalParcial.getText());

        valor1.setText(String.valueOf((total * 0.02) + total));
        String p1 = String.format("%.2f", Double.parseDouble(valor1.getText())); //coloca duas casas decimais
        valor1.setText("Valor parcela " + p1);

        valor2.setText(String.valueOf((total * 0.04) + total));
        String p2 = String.format("%.2f", Double.parseDouble(valor2.getText()));
        valor2.setText("Valor parcela " + p2);

        valor3.setText(String.valueOf((total * 0.06) + total));
        String p3 = String.format("%.2f", Double.parseDouble(valor3.getText()));
        valor3.setText("Valor parcela " + p3);

        valor4.setText(String.valueOf((total * 0.08) + total));
        String p4 = String.format("%.2f", Double.parseDouble(valor4.getText()));
        valor4.setText("Valor parcela " + p4);

        valor5.setText(String.valueOf((total * 0.10) + total));
        String p5 = String.format("%.2f", Double.parseDouble(valor5.getText()));
        valor5.setText("Valor parcela " + p5);

        valor6.setText(String.valueOf((total * 0.12) + total));
        String p6 = String.format("%.2f", Double.parseDouble(valor6.getText()));
        valor6.setText("Valor parcela " + p6);

        valor7.setText(String.valueOf((total * 0.14) + total));
        String p7 = String.format("%.2f", Double.parseDouble(valor7.getText()));
        valor7.setText("Valor parcela " + p7);

        valor8.setText(String.valueOf((total * 0.16) + total));
        String p8 = String.format("%.2f", Double.parseDouble(valor8.getText()));
        valor8.setText("Valor parcela " + p8);

        valor9.setText(String.valueOf((total * 0.18) + total));
        String p9 = String.format("%.2f", Double.parseDouble(valor9.getText()));
        valor9.setText("Valor parcela " + p9);

        valor10.setText(String.valueOf((total * 0.20) + total));
        String p10 = String.format("%.2f", Double.parseDouble(valor10.getText()));
        valor10.setText("Valor parcela " + p10);

        valor11.setText(String.valueOf((total * 0.22) + total));
        String p11 = String.format("%.2f", Double.parseDouble(valor11.getText()));
        valor11.setText("Valor parcela " + p11);

        valor12.setText(String.valueOf((total * 0.24) + total));
        String p12 = String.format("%.2f", Double.parseDouble(valor12.getText()));
        valor12.setText("Valor parcela " + p12);
    }

    public void juros(double x) {
        NumberFormat nf;
        nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        nf.setMinimumFractionDigits(2);
        double result = Double.parseDouble(totalFinal.getText().replace(",", "."));
        result /= x;
        parcela.setText(String.valueOf(nf.format(result)));
    }

    public void salvar() {

        mod.setTotalParcial(totalParcial.getText());
        mod.setParcela(parcela.getText());
        mod.setTotalFinal(totalFinal.getText());
        mod.setDia(data);
        mod.setQuantParcela(String.valueOf(verificarParcela));
        control.insert(mod);
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
        totalParcial = new javax.swing.JLabel();
        parcela = new javax.swing.JLabel();
        checkbox1 = new java.awt.Checkbox();
        checkbox2 = new java.awt.Checkbox();
        checkbox3 = new java.awt.Checkbox();
        checkbox4 = new java.awt.Checkbox();
        checkbox5 = new java.awt.Checkbox();
        checkbox6 = new java.awt.Checkbox();
        checkbox7 = new java.awt.Checkbox();
        checkbox8 = new java.awt.Checkbox();
        checkbox9 = new java.awt.Checkbox();
        checkbox10 = new java.awt.Checkbox();
        checkbox11 = new java.awt.Checkbox();
        checkbox12 = new java.awt.Checkbox();
        valor1 = new javax.swing.JLabel();
        valor2 = new javax.swing.JLabel();
        valor3 = new javax.swing.JLabel();
        valor4 = new javax.swing.JLabel();
        valor5 = new javax.swing.JLabel();
        valor6 = new javax.swing.JLabel();
        valor7 = new javax.swing.JLabel();
        valor8 = new javax.swing.JLabel();
        valor9 = new javax.swing.JLabel();
        valor10 = new javax.swing.JLabel();
        valor11 = new javax.swing.JLabel();
        valor12 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        totalFinal = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jSeparator9 = new javax.swing.JSeparator();
        jSeparator10 = new javax.swing.JSeparator();
        jSeparator11 = new javax.swing.JSeparator();
        jSeparator12 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        calculadora = new javax.swing.JLabel();
        finalizar = new javax.swing.JLabel();
        cancelar = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(500, 380));
        jPanel1.setLayout(null);

        totalParcial.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        totalParcial.setForeground(new java.awt.Color(0, 100, 18));
        totalParcial.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalParcial.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Total", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 24))); // NOI18N
        jPanel1.add(totalParcial);
        totalParcial.setBounds(330, 90, 210, 79);

        parcela.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        parcela.setForeground(new java.awt.Color(190, 5, 5));
        parcela.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        parcela.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Parcela", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 24))); // NOI18N
        jPanel1.add(parcela);
        parcela.setBounds(330, 190, 210, 79);

        checkbox1.setFont(new java.awt.Font("Segoe UI Symbol", 0, 13)); // NOI18N
        checkbox1.setLabel("1x ");
        checkbox1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkbox1MouseClicked(evt);
            }
        });
        jPanel1.add(checkbox1);
        checkbox1.setBounds(20, 90, 40, 22);

        checkbox2.setFont(new java.awt.Font("Segoe UI Symbol", 0, 13)); // NOI18N
        checkbox2.setLabel("2x ");
        checkbox2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkbox2MouseClicked(evt);
            }
        });
        jPanel1.add(checkbox2);
        checkbox2.setBounds(20, 120, 40, 22);

        checkbox3.setFont(new java.awt.Font("Segoe UI Symbol", 0, 13)); // NOI18N
        checkbox3.setLabel("3x ");
        checkbox3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkbox3MouseClicked(evt);
            }
        });
        jPanel1.add(checkbox3);
        checkbox3.setBounds(20, 150, 40, 22);

        checkbox4.setFont(new java.awt.Font("Segoe UI Symbol", 0, 13)); // NOI18N
        checkbox4.setLabel("4x ");
        checkbox4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkbox4MouseClicked(evt);
            }
        });
        jPanel1.add(checkbox4);
        checkbox4.setBounds(20, 180, 40, 22);

        checkbox5.setFont(new java.awt.Font("Segoe UI Symbol", 0, 13)); // NOI18N
        checkbox5.setLabel("5x ");
        checkbox5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkbox5MouseClicked(evt);
            }
        });
        jPanel1.add(checkbox5);
        checkbox5.setBounds(20, 210, 40, 22);

        checkbox6.setFont(new java.awt.Font("Segoe UI Symbol", 0, 13)); // NOI18N
        checkbox6.setLabel("6x ");
        checkbox6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkbox6MouseClicked(evt);
            }
        });
        jPanel1.add(checkbox6);
        checkbox6.setBounds(20, 240, 40, 22);

        checkbox7.setFont(new java.awt.Font("Segoe UI Symbol", 0, 13)); // NOI18N
        checkbox7.setLabel("7x ");
        checkbox7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkbox7MouseClicked(evt);
            }
        });
        jPanel1.add(checkbox7);
        checkbox7.setBounds(20, 270, 40, 22);

        checkbox8.setFont(new java.awt.Font("Segoe UI Symbol", 0, 13)); // NOI18N
        checkbox8.setLabel("8x ");
        checkbox8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkbox8MouseClicked(evt);
            }
        });
        jPanel1.add(checkbox8);
        checkbox8.setBounds(20, 300, 40, 22);

        checkbox9.setFont(new java.awt.Font("Segoe UI Symbol", 0, 13)); // NOI18N
        checkbox9.setLabel("9x ");
        checkbox9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkbox9MouseClicked(evt);
            }
        });
        jPanel1.add(checkbox9);
        checkbox9.setBounds(20, 330, 40, 22);

        checkbox10.setFont(new java.awt.Font("Segoe UI Symbol", 0, 13)); // NOI18N
        checkbox10.setLabel("10x ");
        checkbox10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkbox10MouseClicked(evt);
            }
        });
        jPanel1.add(checkbox10);
        checkbox10.setBounds(20, 360, 40, 22);

        checkbox11.setFont(new java.awt.Font("Segoe UI Symbol", 0, 13)); // NOI18N
        checkbox11.setLabel("11x ");
        checkbox11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkbox11MouseClicked(evt);
            }
        });
        jPanel1.add(checkbox11);
        checkbox11.setBounds(20, 390, 40, 22);

        checkbox12.setFont(new java.awt.Font("Segoe UI Symbol", 0, 13)); // NOI18N
        checkbox12.setLabel("12x ");
        checkbox12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkbox12MouseClicked(evt);
            }
        });
        jPanel1.add(checkbox12);
        checkbox12.setBounds(20, 420, 40, 22);

        valor1.setFont(new java.awt.Font("Segoe UI Semilight", 0, 15)); // NOI18N
        valor1.setForeground(new java.awt.Color(6, 0, 255));
        jPanel1.add(valor1);
        valor1.setBounds(70, 90, 180, 20);

        valor2.setFont(new java.awt.Font("Segoe UI Semilight", 0, 15)); // NOI18N
        valor2.setForeground(new java.awt.Color(6, 0, 255));
        jPanel1.add(valor2);
        valor2.setBounds(70, 120, 180, 20);

        valor3.setFont(new java.awt.Font("Segoe UI Semilight", 0, 15)); // NOI18N
        valor3.setForeground(new java.awt.Color(6, 0, 255));
        jPanel1.add(valor3);
        valor3.setBounds(70, 150, 180, 20);

        valor4.setFont(new java.awt.Font("Segoe UI Semilight", 0, 15)); // NOI18N
        valor4.setForeground(new java.awt.Color(6, 0, 255));
        jPanel1.add(valor4);
        valor4.setBounds(70, 180, 180, 20);

        valor5.setFont(new java.awt.Font("Segoe UI Semilight", 0, 15)); // NOI18N
        valor5.setForeground(new java.awt.Color(6, 0, 255));
        jPanel1.add(valor5);
        valor5.setBounds(70, 210, 180, 20);

        valor6.setFont(new java.awt.Font("Segoe UI Semilight", 0, 15)); // NOI18N
        valor6.setForeground(new java.awt.Color(6, 0, 255));
        jPanel1.add(valor6);
        valor6.setBounds(70, 240, 180, 20);

        valor7.setFont(new java.awt.Font("Segoe UI Semilight", 0, 15)); // NOI18N
        valor7.setForeground(new java.awt.Color(6, 0, 255));
        jPanel1.add(valor7);
        valor7.setBounds(70, 270, 180, 20);

        valor8.setFont(new java.awt.Font("Segoe UI Semilight", 0, 15)); // NOI18N
        valor8.setForeground(new java.awt.Color(6, 0, 255));
        jPanel1.add(valor8);
        valor8.setBounds(70, 300, 180, 20);

        valor9.setFont(new java.awt.Font("Segoe UI Semilight", 0, 15)); // NOI18N
        valor9.setForeground(new java.awt.Color(6, 0, 255));
        jPanel1.add(valor9);
        valor9.setBounds(70, 330, 180, 20);

        valor10.setFont(new java.awt.Font("Segoe UI Semilight", 0, 15)); // NOI18N
        valor10.setForeground(new java.awt.Color(6, 0, 255));
        jPanel1.add(valor10);
        valor10.setBounds(70, 360, 180, 20);

        valor11.setFont(new java.awt.Font("Segoe UI Semilight", 0, 15)); // NOI18N
        valor11.setForeground(new java.awt.Color(6, 0, 255));
        jPanel1.add(valor11);
        valor11.setBounds(70, 390, 180, 20);

        valor12.setFont(new java.awt.Font("Segoe UI Semilight", 0, 15)); // NOI18N
        valor12.setForeground(new java.awt.Color(6, 0, 255));
        jPanel1.add(valor12);
        valor12.setBounds(70, 420, 180, 20);

        jLabel28.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(215, 2, 2));
        jLabel28.setText("Taxa de 2% por parcela");
        jPanel1.add(jLabel28);
        jLabel28.setBounds(400, 420, 140, 20);

        totalFinal.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        totalFinal.setForeground(new java.awt.Color(0, 0, 108));
        totalFinal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalFinal.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Total Final", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 24))); // NOI18N
        jPanel1.add(totalFinal);
        totalFinal.setBounds(330, 290, 210, 79);
        jPanel1.add(jSeparator1);
        jSeparator1.setBounds(20, 110, 230, 10);
        jPanel1.add(jSeparator2);
        jSeparator2.setBounds(20, 140, 230, 10);
        jPanel1.add(jSeparator3);
        jSeparator3.setBounds(20, 170, 230, 10);
        jPanel1.add(jSeparator4);
        jSeparator4.setBounds(20, 200, 230, 10);
        jPanel1.add(jSeparator5);
        jSeparator5.setBounds(20, 230, 230, 10);
        jPanel1.add(jSeparator6);
        jSeparator6.setBounds(20, 260, 230, 10);
        jPanel1.add(jSeparator7);
        jSeparator7.setBounds(20, 290, 230, 10);
        jPanel1.add(jSeparator8);
        jSeparator8.setBounds(20, 320, 230, 10);
        jPanel1.add(jSeparator9);
        jSeparator9.setBounds(20, 350, 230, 10);
        jPanel1.add(jSeparator10);
        jSeparator10.setBounds(20, 380, 230, 10);
        jPanel1.add(jSeparator11);
        jSeparator11.setBounds(20, 410, 230, 10);
        jPanel1.add(jSeparator12);
        jSeparator12.setBounds(20, 440, 230, 10);

        jLabel1.setBackground(new java.awt.Color(0, 101, 12));
        jLabel1.setFont(new java.awt.Font("Segoe UI Semilight", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("  Parcelamento");
        jLabel1.setOpaque(true);
        jPanel1.add(jLabel1);
        jLabel1.setBounds(0, 0, 550, 40);

        calculadora.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/calculadora 40.png"))); // NOI18N
        calculadora.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                calculadoraMouseClicked(evt);
            }
        });
        jPanel1.add(calculadora);
        calculadora.setBounds(500, 370, 40, 40);

        finalizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/ok 35.png"))); // NOI18N
        finalizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                finalizarMouseClicked(evt);
            }
        });
        jPanel1.add(finalizar);
        finalizar.setBounds(460, 370, 35, 40);

        cancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/sair 40.png"))); // NOI18N
        cancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cancelarMouseClicked(evt);
            }
        });
        jPanel1.add(cancelar);
        cancelar.setBounds(414, 370, 40, 40);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setSize(new java.awt.Dimension(566, 489));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void checkbox1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkbox1MouseClicked
        Double total = Double.parseDouble(totalParcial.getText());
        totalFinal.setText(String.valueOf(String.format("%.2f", (total * 0.02) + total)));
        juros(1);
        status = "checkbox1";
        verificarParcela = 1;
    }//GEN-LAST:event_checkbox1MouseClicked

    private void checkbox2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkbox2MouseClicked
        Double total = Double.parseDouble(totalParcial.getText());
        totalFinal.setText(String.valueOf(String.format("%.2f", (total * 0.04) + total)));
        juros(2);
        status = "checkbox2";
        verificarParcela = 2;
    }//GEN-LAST:event_checkbox2MouseClicked

    private void checkbox3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkbox3MouseClicked
        Double total = Double.parseDouble(totalParcial.getText());
        totalFinal.setText(String.valueOf(String.format("%.2f", (total * 0.06) + total)));
        juros(3);
        status = "checkbox3";
        verificarParcela = 3;
    }//GEN-LAST:event_checkbox3MouseClicked

    private void checkbox4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkbox4MouseClicked
        Double total = Double.parseDouble(totalParcial.getText());
        totalFinal.setText(String.valueOf(String.format("%.2f", (total * 0.08) + total)));
        juros(4);
        status = "checkbox4";
        verificarParcela = 4;
    }//GEN-LAST:event_checkbox4MouseClicked

    private void checkbox5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkbox5MouseClicked
        Double total = Double.parseDouble(totalParcial.getText());
        totalFinal.setText(String.valueOf(String.format("%.2f", (total * 0.10) + total)));
        juros(5);
        status = "checkbox5";
        verificarParcela = 5;
    }//GEN-LAST:event_checkbox5MouseClicked

    private void checkbox6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkbox6MouseClicked
        Double total = Double.parseDouble(totalParcial.getText());
        totalFinal.setText(String.valueOf(String.format("%.2f", (total * 0.12) + total)));
        juros(6);
        status = "checkbox6";
        verificarParcela = 6;
    }//GEN-LAST:event_checkbox6MouseClicked

    private void checkbox7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkbox7MouseClicked
        Double total = Double.parseDouble(totalParcial.getText());
        totalFinal.setText(String.valueOf(String.format("%.2f", (total * 0.14) + total)));
        juros(7);
        status = "checkbox7";
        verificarParcela = 7;
    }//GEN-LAST:event_checkbox7MouseClicked

    private void checkbox8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkbox8MouseClicked
        Double total = Double.parseDouble(totalParcial.getText());
        totalFinal.setText(String.valueOf(String.format("%.2f", (total * 0.16) + total)));
        juros(8);
        status = "checkbox8";
        verificarParcela = 8;
    }//GEN-LAST:event_checkbox8MouseClicked

    private void checkbox9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkbox9MouseClicked
        Double total = Double.parseDouble(totalParcial.getText());
        totalFinal.setText(String.valueOf(String.format("%.2f", (total * 0.18) + total)));
        juros(9);
        status = "checkbox9";
        verificarParcela = 9;
    }//GEN-LAST:event_checkbox9MouseClicked

    private void checkbox10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkbox10MouseClicked
        Double total = Double.parseDouble(totalParcial.getText());
        totalFinal.setText(String.valueOf(String.format("%.2f", (total * 0.20) + total)));
        juros(10);
        status = "checkbox10";
        verificarParcela = 10;
    }//GEN-LAST:event_checkbox10MouseClicked

    private void checkbox11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkbox11MouseClicked
        Double total = Double.parseDouble(totalParcial.getText());
        totalFinal.setText(String.valueOf(String.format("%.2f", (total * 0.22) + total)));
        juros(11);
        status = "checkbox11";
        verificarParcela = 11;
    }//GEN-LAST:event_checkbox11MouseClicked

    private void checkbox12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkbox12MouseClicked
        Double total = Double.parseDouble(totalParcial.getText());
        totalFinal.setText(String.valueOf(String.format("%.2f", (total * 0.24) + total)));
        juros(12);
        status = "checkbox12";
        verificarParcela = 12;
    }//GEN-LAST:event_checkbox12MouseClicked

    private void calculadoraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_calculadoraMouseClicked
        try {
            Runtime.getRuntime().exec("calc");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_calculadoraMouseClicked

    private void finalizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_finalizarMouseClicked
        int resposta = JOptionPane.showConfirmDialog(null, "Encerrar está venda?");
        if (resposta == JOptionPane.YES_OPTION) {
            salvar();
            this.dispose();
        }
    }//GEN-LAST:event_finalizarMouseClicked

    private void cancelarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelarMouseClicked
        this.dispose();
    }//GEN-LAST:event_cancelarMouseClicked

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
            java.util.logging.Logger.getLogger(Parcelamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            Parcelamento dialog = new Parcelamento(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel calculadora;
    private javax.swing.JLabel cancelar;
    private java.awt.Checkbox checkbox1;
    private java.awt.Checkbox checkbox10;
    private java.awt.Checkbox checkbox11;
    private java.awt.Checkbox checkbox12;
    private java.awt.Checkbox checkbox2;
    private java.awt.Checkbox checkbox3;
    private java.awt.Checkbox checkbox4;
    private java.awt.Checkbox checkbox5;
    private java.awt.Checkbox checkbox6;
    private java.awt.Checkbox checkbox7;
    private java.awt.Checkbox checkbox8;
    private java.awt.Checkbox checkbox9;
    private javax.swing.JLabel finalizar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JLabel parcela;
    private javax.swing.JLabel totalFinal;
    private javax.swing.JLabel totalParcial;
    private javax.swing.JLabel valor1;
    private javax.swing.JLabel valor10;
    private javax.swing.JLabel valor11;
    private javax.swing.JLabel valor12;
    private javax.swing.JLabel valor2;
    private javax.swing.JLabel valor3;
    private javax.swing.JLabel valor4;
    private javax.swing.JLabel valor5;
    private javax.swing.JLabel valor6;
    private javax.swing.JLabel valor7;
    private javax.swing.JLabel valor8;
    private javax.swing.JLabel valor9;
    // End of variables declaration//GEN-END:variables
}