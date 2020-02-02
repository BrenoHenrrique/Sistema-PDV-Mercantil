package viewer;

import controlConnetion.conectaBanco;
import controlViwer.controlEntragas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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

public final class Entregas extends javax.swing.JFrame {

    conectaBanco conexao = new conectaBanco();
    Beans mod = new Beans();
    controlEntragas control = new controlEntragas();
    private final Venda parent;
    List<Object> arrayParametros = new ArrayList<>();
    String sumTotal;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    public Entregas(Venda p, String id) {
        initComponents();
        this.parent = p;
        idVenda.setText(id);
        componentes();
        controlFocus();
        tabelaEntragas.getTableHeader().setDefaultRenderer(new Entregas.header());
        readTableEntregas("select * from entregas");
    }

    private Entregas() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void componentes() {

        titulo.setOpaque(true);
        titulo.setBackground(new Color(024, 131, 215));
        titulo.setForeground(new Color(255, 255, 255));
        barTable.getViewport().setBackground(Color.white);
    }

    public void limparCampos() {

        nome.setText("");
        bairro.setText("");
        bairro.setText("");
        endereco.setText("");
        complemento.setText("");
        obs.setText("");
        nome.grabFocus();
    }

    public void controlFocus() {
        ArrayList<Component> order = new ArrayList<>(4);
        order.add(nome);
        order.add(bairro);
        order.add(endereco);
        order.add(complemento);
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

    @SuppressWarnings("CallToPrintStackTrace")
    public void readTableEntregas(String Sql) {

        conexao.conecta();
        ArrayList dados = new ArrayList();
        String[] colunas = new String[]{"Nome", "Bairro", "Endereço", "Complemento", "Observações"};
        conexao.executaSQL(Sql);
        try {
            conexao.rs.first();
            do {
                dados.add(new Object[]{
                    conexao.rs.getString("nome"), conexao.rs.getString("bairro"), conexao.rs.getString("endereco"),
                    conexao.rs.getString("complemento"), conexao.rs.getString("observacoes")});
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

        tabelaEntragas.setModel(tabela);
        tabelaEntragas.getColumnModel().getColumn(0).setPreferredWidth(200);
        tabelaEntragas.getColumnModel().getColumn(0).setResizable(false);
        tabelaEntragas.getColumnModel().getColumn(0).setCellRenderer(centralizado);
        tabelaEntragas.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabelaEntragas.getColumnModel().getColumn(1).setResizable(false);
        tabelaEntragas.getColumnModel().getColumn(1).setCellRenderer(centralizado);
        tabelaEntragas.getColumnModel().getColumn(2).setPreferredWidth(200);
        tabelaEntragas.getColumnModel().getColumn(2).setResizable(false);
        tabelaEntragas.getColumnModel().getColumn(3).setPreferredWidth(200);
        tabelaEntragas.getColumnModel().getColumn(3).setResizable(false);
        tabelaEntragas.getColumnModel().getColumn(3).setCellRenderer(centralizado);
        tabelaEntragas.getColumnModel().getColumn(4).setPreferredWidth(200);
        tabelaEntragas.getColumnModel().getColumn(4).setResizable(false);
        tabelaEntragas.getColumnModel().getColumn(4).setCellRenderer(centralizado);
        tabelaEntragas.getTableHeader().setReorderingAllowed(false);
        tabelaEntragas.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabelaEntragas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        conexao.desconecta();
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
                String valorProd = conexao.rs.getString("venda");

                if (nomeProduto.length() > 15) {
                    nomeProduto = nomeProduto.substring(0, 15);

                    parametros = nomeProduto + "  R$ " + valorProd + "     " + quantProd + "  " + "\n\r";

                    arrayParametros.add(parametros);

                } else {
                    nomeProduto = (String.format("%-15s", nomeProduto));
                    nomeProduto = nomeProduto.substring(0, 15);

                    parametros = nomeProduto + " R$ " + valorProd + "     " + quantProd + "  " + "\n\r";

                    arrayParametros.add(parametros);
                }

            } while (conexao.rs.next());

            conexao.executaSQL("select sum(total) from historico where idVenda='" + idVenda.getText() + "'");
            //metodo para somar todos os valores de uma coluna obs:sem espaço do 'sum()'
            conexao.rs.first();
            sumTotal = (String.valueOf(conexao.rs.getDouble(1)));
            //converte ele para o registro de n° '1' obs. conversão obrigatoria
            total.setText(sumTotal);

            String data = new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis()));
            String hora = new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis()));

            this.impressao("   MERCADINHO PITAGUARY   \n\r"
                    + "Data e Hora: " + data + " - " + hora + "\n\r"
                    + "Endereco: R. Manuel Pereira 505\n\r"
                    + " Celular:(85) 98684-6927\n\r"
                    + "Telefone:(85) 3384-6282\n\r"
                    + "-------------------------------\n\r"
                    + "        CUPOM NAO FISCAL\n\r"
                    + "-------------------------------\n\r"
                    + "        DADOS DO CLIENTE\n\r"
                    + "-------------------------------\n\r"
                    + "       NOME: " + nome.getText() + "\n\r"
                    + "     CIDADE: " + bairro.getText() + "\n\r"
                    + "   ENDERECO: " + endereco.getText() + "\n\r"
                    + "COMPLEMENTO: " + complemento.getText() + "\n\r"
                    + "OBSERVACOES: " + obs.getText() + "\n\r"
                    + "-------------------------------\n\r"
                    + "         LISTA DE ITENS\n\r"
                    + "-------------------------------\n\r"
                    + "DESCRICAO          PRECO     QT \n\r"
                    + arrayParametros.toString().replace("[", "").replace("]", "") + "\n\r"
                    + "-------------------------------\n\r"
                    + " VALOR TOTAL: " + total.getText() + "\n\r"
                    + "-------------------------------\n\r"
                    + "OBS:So e feita a troca de produtos com este cupom em maos.\r\n"
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
    public void impressao(String pTexto) {
        // \n\p para pular linhas e \f para fim da pagina

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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jScrollObs = new javax.swing.JScrollPane();
        obs = new javax.swing.JTextArea();
        barTable = new javax.swing.JScrollPane();
        tabelaEntragas = new javax.swing.JTable();
        adicionar = new javax.swing.JButton();
        atualizar = new javax.swing.JButton();
        cancelar = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        complemento = new javax.swing.JTextField();
        nome = new javax.swing.JTextField();
        bairro = new javax.swing.JTextField();
        endereco = new javax.swing.JTextField();
        titulo = new javax.swing.JLabel();
        imprimir = new javax.swing.JButton();
        cancelar1 = new javax.swing.JButton();
        idVenda = new javax.swing.JLabel();
        total = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel2.setText("Nome");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 56, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel3.setText("Cidade/Bairro");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 144, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel5.setText("Endereço");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 232, -1, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel6.setText("Complemento");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, -1, -1));

        jLabel16.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 16)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Observações:");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 396, -1, -1));

        obs.setColumns(20);
        obs.setFont(new java.awt.Font("Monospaced", 2, 13)); // NOI18N
        obs.setRows(5);
        obs.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jScrollObs.setViewportView(obs);

        jPanel1.add(jScrollObs, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 426, 300, -1));

        tabelaEntragas.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tabelaEntragas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tabelaEntragas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaEntragasMouseClicked(evt);
            }
        });
        barTable.setViewportView(tabelaEntragas);

        jPanel1.add(barTable, new org.netbeans.lib.awtextra.AbsoluteConstraints(356, 77, 650, 612));

        adicionar.setBackground(new java.awt.Color(255, 255, 255));
        adicionar.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        adicionar.setForeground(new java.awt.Color(255, 255, 255));
        adicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/add 48.png"))); // NOI18N
        adicionar.setToolTipText("Adicionar");
        adicionar.setContentAreaFilled(false);
        adicionar.setOpaque(true);
        adicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adicionarActionPerformed(evt);
            }
        });
        jPanel1.add(adicionar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 531, 48, 48));

        atualizar.setBackground(new java.awt.Color(255, 255, 255));
        atualizar.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        atualizar.setForeground(new java.awt.Color(255, 255, 255));
        atualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/atualizar 48.png"))); // NOI18N
        atualizar.setToolTipText("Atualizar");
        atualizar.setContentAreaFilled(false);
        atualizar.setOpaque(true);
        atualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                atualizarActionPerformed(evt);
            }
        });
        jPanel1.add(atualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(139, 531, 48, 48));

        cancelar.setBackground(new java.awt.Color(255, 255, 255));
        cancelar.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        cancelar.setForeground(new java.awt.Color(255, 255, 255));
        cancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/cancelar 48.png"))); // NOI18N
        cancelar.setToolTipText("Cancelar");
        cancelar.setContentAreaFilled(false);
        cancelar.setOpaque(true);
        cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarActionPerformed(evt);
            }
        });
        jPanel1.add(cancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(193, 531, 48, 48));

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 0, 0));
        jLabel20.setText("*");
        jLabel20.setToolTipText("Obrigatório");
        jPanel1.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 270, -1, -1));

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 0, 0));
        jLabel21.setText("*");
        jLabel21.setToolTipText("Obrigatório");
        jPanel1.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 100, -1, -1));

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 0, 0));
        jLabel22.setText("*");
        jLabel22.setToolTipText("Obrigatório");
        jPanel1.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 180, 10, 30));

        complemento.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        jPanel1.add(complemento, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, 280, 40));

        nome.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        jPanel1.add(nome, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 86, 280, 40));

        bairro.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        bairro.setText("Maracanaú");
        jPanel1.add(bairro, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 174, 280, 40));

        endereco.setFont(new java.awt.Font("Segoe UI Symbol", 0, 18)); // NOI18N
        jPanel1.add(endereco, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 262, 280, 40));

        titulo.setBackground(new java.awt.Color(0, 0, 255));
        titulo.setFont(new java.awt.Font("Arial", 1, 28)); // NOI18N
        titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titulo.setText("Tela de Entregas");
        jPanel1.add(titulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1154, 50));

        imprimir.setBackground(new java.awt.Color(255, 255, 255));
        imprimir.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        imprimir.setForeground(new java.awt.Color(255, 255, 255));
        imprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/cupom 40.png"))); // NOI18N
        imprimir.setToolTipText("Imprimir");
        imprimir.setContentAreaFilled(false);
        imprimir.setOpaque(true);
        imprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imprimirActionPerformed(evt);
            }
        });
        jPanel1.add(imprimir, new org.netbeans.lib.awtextra.AbsoluteConstraints(84, 531, 49, 48));

        cancelar1.setBackground(new java.awt.Color(255, 255, 255));
        cancelar1.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        cancelar1.setForeground(new java.awt.Color(255, 255, 255));
        cancelar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/deletar 48.png"))); // NOI18N
        cancelar1.setToolTipText("Cancelar");
        cancelar1.setContentAreaFilled(false);
        cancelar1.setOpaque(true);
        cancelar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelar1ActionPerformed(evt);
            }
        });
        jPanel1.add(cancelar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(247, 531, 48, 48));

        idVenda.setBackground(new java.awt.Color(255, 255, 255));
        idVenda.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(idVenda, new org.netbeans.lib.awtextra.AbsoluteConstraints(302, 656, 48, 33));

        total.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        total.setForeground(new java.awt.Color(0, 51, 255));
        total.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        total.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Total", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 24))); // NOI18N
        jPanel1.add(total, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 609, 170, 80));

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

    private void adicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adicionarActionPerformed
        mod.setNome(nome.getText());
        mod.setBairro(bairro.getText());
        mod.setEndereco(endereco.getText());
        mod.setComplemento(complemento.getText());
        mod.setObservacoes(obs.getText());
        control.insert(mod);

        limparCampos();
        readTableEntregas("select * from entregas");
    }//GEN-LAST:event_adicionarActionPerformed

    private void atualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_atualizarActionPerformed
        conexao.conecta();
        conexao.executaSQL("select * from entregas");
        try {
            conexao.rs.first();
            String id = conexao.rs.getString("idEntregas");
            mod.setNome(nome.getText());
            mod.setBairro(bairro.getText());
            mod.setEndereco(endereco.getText());
            mod.setComplemento(complemento.getText());
            mod.setObservacoes(obs.getText());
            mod.setId(id);
            control.update(mod);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        limparCampos();
        readTableEntregas("select * from entregas");
    }//GEN-LAST:event_atualizarActionPerformed

    private void cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarActionPerformed
        limparCampos();
    }//GEN-LAST:event_cancelarActionPerformed

    private void imprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imprimirActionPerformed
        imprimirCupom();
    }//GEN-LAST:event_imprimirActionPerformed

    private void tabelaEntragasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaEntragasMouseClicked
        int linha = tabelaEntragas.getSelectedRow();
        nome.setText(tabelaEntragas.getValueAt(linha, 0).toString());
        bairro.setText(tabelaEntragas.getValueAt(linha, 1).toString());
        endereco.setText(tabelaEntragas.getValueAt(linha, 2).toString());
        complemento.setText(tabelaEntragas.getValueAt(linha, 3).toString());
        obs.setText(tabelaEntragas.getValueAt(linha, 4).toString());
    }//GEN-LAST:event_tabelaEntragasMouseClicked

    private void cancelar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelar1ActionPerformed

        int resposta = 0;
        JOptionPane.showMessageDialog(rootPane, "Deseja excluir este pedido?");
        switch (resposta) {
            case JOptionPane.YES_OPTION:
                conexao.conecta();
                conexao.executaSQL("select * from entregas");
                try {
                    conexao.rs.first();
                    String id = conexao.rs.getString("idEntregas");
                    mod.setId(id);
                    control.delete(mod);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case JOptionPane.NO_OPTION:
                break;
            case JOptionPane.CANCEL_OPTION:
                break;
            default:
                break;
        }

        limparCampos();
        readTableEntregas("select * from entregas");
    }//GEN-LAST:event_cancelar1ActionPerformed

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
            java.util.logging.Logger.getLogger(Entregas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Entregas().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton adicionar;
    private javax.swing.JButton atualizar;
    private javax.swing.JTextField bairro;
    private javax.swing.JScrollPane barTable;
    private javax.swing.JButton cancelar;
    private javax.swing.JButton cancelar1;
    private javax.swing.JTextField complemento;
    private javax.swing.JTextField endereco;
    private javax.swing.JLabel idVenda;
    private javax.swing.JButton imprimir;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollObs;
    private javax.swing.JTextField nome;
    private javax.swing.JTextArea obs;
    private javax.swing.JTable tabelaEntragas;
    private javax.swing.JLabel titulo;
    private javax.swing.JLabel total;
    // End of variables declaration//GEN-END:variables
}
