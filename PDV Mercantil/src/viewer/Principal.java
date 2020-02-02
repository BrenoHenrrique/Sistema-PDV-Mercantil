package viewer;

import modelBeans.focusTraversalPolicy;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import controlConnetion.conectaBanco;
import java.awt.Toolkit;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public final class Principal extends javax.swing.JFrame {

    conectaBanco conexao = new conectaBanco();
    private boolean action = true;
    private boolean flag = false;
    public String strUsuario;
    Venda vendaEnvia;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    public Principal() {
        initComponents();
        color();
        controlFocus();
        mostrarVerificador();
        data.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())));
    }

    public void color() {

        venda.setForeground(new Color(231, 134, 47));
        produtos.setForeground(new Color(231, 134, 47));
        fornecedor.setForeground(new Color(231, 134, 47));
        historico.setForeground(new Color(231, 134, 47));
        agenda.setForeground(new Color(231, 134, 47));
        site.setForeground(new Color(231, 134, 47));
        labelUsuario.setForeground(new Color(231, 125, 30));
        labelSenha.setForeground(new Color(231, 125, 30));
        esqueci.setForeground(new Color(231, 125, 30));
        entrar.setOpaque(true);
        entrar.setForeground(new Color(255, 255, 255));
        entrar.setBackground(new Color(231, 125, 30));
        entrar.setFont(new Font("Segoe UI Semilight", Font.BOLD, 22));
        bloquear.setVisible(false);
        pesquisa.setEnabled(false);
        botSobre.setVisible(false);
        data.setForeground(new Color(255, 255, 255));
    }

    public void controlFocus() {
        ArrayList<Component> order = new ArrayList<>(6);
        order.add(this.getContentPane());
        order.add(usuario);
        order.add(senha);
        order.add(entrar);
        order.add(pesquisa);
        order.add(botPesquisa);
        focusTraversalPolicy policy = new focusTraversalPolicy(order);
        setFocusTraversalPolicy(policy);
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void salvarVerificador() {

        conexao.conecta();
        try {
            stmt = conexao.conn.prepareStatement("update login set verificador='" + usuario.getText() + "' where idLogin='1'");
            stmt.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        conexao.desconecta();
    }

    public void mostrarVerificador() {

        conexao.conecta();
        try {
            conexao.executaSQL("select * from login where verificador != ''");
            conexao.rs.first();
            String verificador = conexao.rs.getString("verificador");
            usuario.setFont(new Font("Serif", Font.PLAIN, 18));
            usuario.setForeground(new Color(0, 0, 0));
            usuario.setText(verificador);

            //usuario.setEnabled(false);
        } catch (SQLException ex) {
            Logger.getLogger(Venda.class.getName()).log(Level.SEVERE, null, ex);
        }
        conexao.desconecta();
    }

    public void checarAgenda() {

        conexao.conecta();
        conexao.executaSQL("select * from agenda where dia='" + data.getText() + "'");
        try {
            conexao.rs.first();
            String diaAgenda = conexao.rs.getString("dia");
            if (data.getText().equals(diaAgenda)) {
                Toolkit.getDefaultToolkit().beep(); //aviso sonoro
                JOptionPane.showMessageDialog(rootPane, "Você tem um compromisso hoje, vá até á Agenda e veja.");
            }
        } catch (SQLException ex) {
            //ex.printStackTrace();
        }
    }

    public void mostrarFiador() {

        conexao.conecta();
        conexao.executaSQL("select * from fiadores where dataPagamento='" + data.getText() + "'");
        try {
            conexao.rs.first();
            String fiador = conexao.rs.getString("nome");
            String total = conexao.rs.getString("total");
            JOptionPane.showMessageDialog(rootPane, "Você tem um fiador: " + fiador + " com o valor: " + total + ", para hoje.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        conexao.desconecta();
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void contadorIncremento() {

        conexao.conecta();
        conexao.executaSQL("select * from login");
        try {
            conexao.rs.first();
            int count = conexao.rs.getInt("contador");
            count++;
            stmt = conexao.conn.prepareStatement("update login set contador='" + count + "' where idLogin='1'");
            stmt.execute();
        } catch (SQLException ex) {
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
        jSeparator1 = new javax.swing.JSeparator();
        venda = new javax.swing.JLabel();
        produtos = new javax.swing.JLabel();
        fornecedor = new javax.swing.JLabel();
        historico = new javax.swing.JLabel();
        agenda = new javax.swing.JLabel();
        site = new javax.swing.JLabel();
        botSair = new javax.swing.JLabel();
        usuario = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jlabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jlabel3 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        senha = new javax.swing.JPasswordField();
        labelUsuario = new javax.swing.JLabel();
        labelSenha = new javax.swing.JLabel();
        entrar = new javax.swing.JButton();
        esqueci = new javax.swing.JLabel();
        copyright = new javax.swing.JLabel();
        botMenu = new javax.swing.JLabel();
        pesquisa = new javax.swing.JTextField();
        bloquear = new javax.swing.JLabel();
        botVenda = new javax.swing.JButton();
        botProduto = new javax.swing.JButton();
        botFornecedor = new javax.swing.JButton();
        botHistorico = new javax.swing.JButton();
        botSite = new javax.swing.JButton();
        botAgenda = new javax.swing.JButton();
        minimizar = new javax.swing.JLabel();
        botPesquisa = new javax.swing.JButton();
        botSobre = new javax.swing.JLabel();
        data = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(24, 131, 215)));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        venda.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        venda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        venda.setText("Venda");

        produtos.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        produtos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        produtos.setText("Produto");

        fornecedor.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        fornecedor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fornecedor.setText("Fornecedor");

        historico.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        historico.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        historico.setText("Histórico");

        agenda.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        agenda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        agenda.setText("Agenda");

        site.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        site.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        site.setText("Site");

        botSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/sair 40.png"))); // NOI18N
        botSair.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        botSair.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botSairMouseClicked(evt);
            }
        });

        usuario.setFont(new java.awt.Font("Serif", 2, 18)); // NOI18N
        usuario.setForeground(new java.awt.Color(109, 109, 109));
        usuario.setText("Digite seu usuário");
        usuario.setBorder(null);
        usuario.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        usuario.setOpaque(false);
        usuario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                usuarioFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                usuarioFocusLost(evt);
            }
        });

        jlabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/user 32.png"))); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/logo teste tamanho 150x150.png"))); // NOI18N

        jlabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/senha 32.png"))); // NOI18N

        senha.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        senha.setForeground(new java.awt.Color(109, 109, 109));
        senha.setText("123456789");
        senha.setBorder(null);
        senha.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        senha.setOpaque(false);
        senha.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                senhaFocusGained(evt);
            }
        });
        senha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                senhaKeyPressed(evt);
            }
        });

        labelUsuario.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        labelUsuario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelUsuario.setText("Usuário:");

        labelSenha.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        labelSenha.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelSenha.setText("Senha:");

        entrar.setFont(new java.awt.Font("Segoe UI Semilight", 0, 22)); // NOI18N
        entrar.setText("Entrar");
        entrar.setBorder(null);
        entrar.setContentAreaFilled(false);
        entrar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        entrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                entrarActionPerformed(evt);
            }
        });

        esqueci.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        esqueci.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        esqueci.setText("Esqueceu sua senha?");
        esqueci.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                esqueciMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                esqueciMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                esqueciMouseExited(evt);
            }
        });

        copyright.setFont(new java.awt.Font("Serif", 2, 13)); // NOI18N
        copyright.setForeground(new java.awt.Color(109, 109, 109));
        copyright.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        copyright.setText("Copyright Lýkos");

        botMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/menu 32.png"))); // NOI18N
        botMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        botMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botMenuMouseClicked(evt);
            }
        });

        pesquisa.setFont(new java.awt.Font("Serif", 2, 18)); // NOI18N
        pesquisa.setForeground(new java.awt.Color(109, 109, 109));
        pesquisa.setText(" Pesquise aqui...");
        pesquisa.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 134, 47)));
        pesquisa.setEnabled(false);
        pesquisa.setOpaque(false);
        pesquisa.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pesquisaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                pesquisaFocusLost(evt);
            }
        });
        pesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pesquisaKeyPressed(evt);
            }
        });

        bloquear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/bloquear 30.png"))); // NOI18N
        bloquear.setToolTipText("Bloquear");
        bloquear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bloquearMouseClicked(evt);
            }
        });

        botVenda.setFont(new java.awt.Font("Segoe UI Semilight", 0, 22)); // NOI18N
        botVenda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/venda laranja 50.png"))); // NOI18N
        botVenda.setToolTipText("");
        botVenda.setBorder(null);
        botVenda.setContentAreaFilled(false);
        botVenda.setEnabled(false);
        botVenda.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botVenda.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botVendaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botVendaMouseExited(evt);
            }
        });
        botVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botVendaActionPerformed(evt);
            }
        });

        botProduto.setFont(new java.awt.Font("Segoe UI Semilight", 0, 22)); // NOI18N
        botProduto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/produto laranja 50.png"))); // NOI18N
        botProduto.setToolTipText("");
        botProduto.setBorder(null);
        botProduto.setContentAreaFilled(false);
        botProduto.setEnabled(false);
        botProduto.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botProduto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botProdutoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botProdutoMouseExited(evt);
            }
        });
        botProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botProdutoActionPerformed(evt);
            }
        });

        botFornecedor.setFont(new java.awt.Font("Segoe UI Semilight", 0, 22)); // NOI18N
        botFornecedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/fornecedor laranja 50.png"))); // NOI18N
        botFornecedor.setToolTipText("");
        botFornecedor.setBorder(null);
        botFornecedor.setContentAreaFilled(false);
        botFornecedor.setEnabled(false);
        botFornecedor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botFornecedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botFornecedorMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botFornecedorMouseExited(evt);
            }
        });
        botFornecedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botFornecedorActionPerformed(evt);
            }
        });

        botHistorico.setFont(new java.awt.Font("Segoe UI Semilight", 0, 22)); // NOI18N
        botHistorico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/historico laranja 50.png"))); // NOI18N
        botHistorico.setToolTipText("");
        botHistorico.setBorder(null);
        botHistorico.setContentAreaFilled(false);
        botHistorico.setEnabled(false);
        botHistorico.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botHistorico.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botHistoricoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botHistoricoMouseExited(evt);
            }
        });
        botHistorico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botHistoricoActionPerformed(evt);
            }
        });

        botSite.setFont(new java.awt.Font("Segoe UI Semilight", 0, 22)); // NOI18N
        botSite.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/site laranja 50.png"))); // NOI18N
        botSite.setToolTipText("Em breve!");
        botSite.setBorder(null);
        botSite.setContentAreaFilled(false);
        botSite.setEnabled(false);
        botSite.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botSite.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botSiteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botSiteMouseExited(evt);
            }
        });
        botSite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botSiteActionPerformed(evt);
            }
        });

        botAgenda.setFont(new java.awt.Font("Segoe UI Semilight", 0, 22)); // NOI18N
        botAgenda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/agenda laranja 50.png"))); // NOI18N
        botAgenda.setToolTipText("Em breve!");
        botAgenda.setBorder(null);
        botAgenda.setContentAreaFilled(false);
        botAgenda.setEnabled(false);
        botAgenda.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botAgenda.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botAgendaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botAgendaMouseExited(evt);
            }
        });
        botAgenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botAgendaActionPerformed(evt);
            }
        });

        minimizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/minimizar 30.png"))); // NOI18N
        minimizar.setToolTipText("Minimizar");
        minimizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                minimizarMouseClicked(evt);
            }
        });

        botPesquisa.setFont(new java.awt.Font("Segoe UI Semilight", 0, 22)); // NOI18N
        botPesquisa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/procurar.png"))); // NOI18N
        botPesquisa.setToolTipText("");
        botPesquisa.setBorder(null);
        botPesquisa.setContentAreaFilled(false);
        botPesquisa.setEnabled(false);
        botPesquisa.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botPesquisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botPesquisaActionPerformed(evt);
            }
        });

        botSobre.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/sobre 32.png"))); // NOI18N
        botSobre.setToolTipText("Sobre");
        botSobre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botSobreMouseClicked(evt);
            }
        });

        data.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botMenu)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(botSobre, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(bloquear))))
                        .addGap(27, 27, 27)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(labelUsuario))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jlabel2)
                        .addGap(8, 8, 8)
                        .addComponent(usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(labelSenha))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jlabel3)
                        .addGap(8, 8, 8)
                        .addComponent(senha, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(89, 89, 89)
                        .addComponent(entrar, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(esqueci, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(111, 111, 111)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(venda, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botHistorico, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(historico, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(57, 57, 57)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(copyright, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(212, 212, 212)
                        .addComponent(data, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(75, 75, 75)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(botAgenda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(botProduto, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                                    .addComponent(produtos, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                                    .addComponent(agenda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(10, 10, 10)
                        .addComponent(botPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fornecedor)
                            .addComponent(botSite, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(site, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44)
                        .addComponent(minimizar)
                        .addGap(6, 6, 6)
                        .addComponent(botSair)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(minimizar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botSair))
                        .addGap(400, 400, 400)
                        .addComponent(data, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(botMenu)
                                    .addGap(6, 6, 6)
                                    .addComponent(bloquear)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(botSobre))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(19, 19, 19)
                                    .addComponent(jLabel1)))
                            .addGap(20, 20, 20)
                            .addComponent(labelUsuario)
                            .addGap(8, 8, 8)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jlabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(10, 10, 10)
                            .addComponent(labelSenha)
                            .addGap(8, 8, 8)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jlabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(senha, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(30, 30, 30)
                            .addComponent(entrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(10, 10, 10)
                            .addComponent(esqueci))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(11, 11, 11)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(116, 116, 116)
                            .addComponent(botFornecedor)
                            .addGap(6, 6, 6)
                            .addComponent(fornecedor)
                            .addGap(76, 76, 76)
                            .addComponent(botSite, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6, 6, 6)
                            .addComponent(site))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(44, 44, 44)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(botVenda)
                                    .addGap(6, 6, 6)
                                    .addComponent(venda))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(2, 2, 2)
                                            .addComponent(pesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(botPesquisa))
                                    .addGap(39, 39, 39)
                                    .addComponent(botProduto)
                                    .addGap(6, 6, 6)
                                    .addComponent(produtos)))
                            .addGap(76, 76, 76)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(botAgenda, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(agenda, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(botHistorico, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(6, 6, 6)
                                    .addComponent(historico)))
                            .addGap(104, 104, 104)
                            .addComponent(copyright))))
                .addGap(0, 10, Short.MAX_VALUE))
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

        setSize(new java.awt.Dimension(980, 475));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void botSairMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botSairMouseClicked
        System.exit(0);
    }//GEN-LAST:event_botSairMouseClicked

    private void usuarioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_usuarioFocusGained
        if (usuario.getText().equals("Digite seu usuário")) {
            usuario.setText("");
            usuario.setFont(new Font("Serif", Font.PLAIN, 18));
            usuario.setForeground(new Color(0, 0, 0));
        }
    }//GEN-LAST:event_usuarioFocusGained

    private void usuarioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_usuarioFocusLost
        if (usuario.getText().equals("")) {
            usuario.setFont(new Font("Serif", Font.ITALIC, 18));
            usuario.setText("Digite seu usuário");
            usuario.setForeground(new Color(109, 109, 109));
        }
    }//GEN-LAST:event_usuarioFocusLost

    private void senhaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_senhaFocusGained
        senha.selectAll();
        senha.setFont(new Font("Serif", Font.PLAIN, 18));
        senha.setForeground(new Color(0, 0, 0));
    }//GEN-LAST:event_senhaFocusGained

    private void pesquisaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pesquisaFocusGained
        if (pesquisa.getText().equals(" Pesquise aqui...")) {
            pesquisa.setText("");
            pesquisa.setFont(new Font("Serif", Font.PLAIN, 18));
            pesquisa.setForeground(new Color(0, 0, 0));
        }
    }//GEN-LAST:event_pesquisaFocusGained

    private void pesquisaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pesquisaFocusLost
        if (pesquisa.getText().equals("")) {
            pesquisa.setText(" Pesquise aqui...");
            pesquisa.setFont(new Font("Serif", Font.ITALIC, 18));
            pesquisa.setForeground(new Color(109, 109, 109));
        }
    }//GEN-LAST:event_pesquisaFocusLost

    @SuppressWarnings("CallToPrintStackTrace")
    private void entrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_entrarActionPerformed
        conexao.conecta();
        conexao.executaSQL("select * from login");
        try {
            conexao.rs.first();
            try {
                conexao.executaSQL("select * from login where usuario='" + usuario.getText() + "'");
                salvarVerificador();
                conexao.rs.first();
                if (conexao.rs.getString("senha").equals(new String(senha.getPassword()))) {
                    JOptionPane.showMessageDialog(rootPane, "Bem vindo " + usuario.getText());
                    botVenda.setEnabled(true);
                    botProduto.setEnabled(true);
                    botFornecedor.setEnabled(true);
                    botHistorico.setEnabled(true);
                    botAgenda.setEnabled(true);
                    botSite.setEnabled(true);
                    usuario.setEnabled(false);
                    senha.setEnabled(false);
                    botPesquisa.setEnabled(true);
                    flag = true;
                    pesquisa.setEnabled(true);
                    contadorIncremento();
                    AbrirCaixa tela = new AbrirCaixa();
                    tela.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Usuário ou senha errados.");
                    usuario.setText("");
                    senha.setText("");
                }
                checarAgenda();
                mostrarFiador();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        conexao.desconecta();
    }//GEN-LAST:event_entrarActionPerformed

    @SuppressWarnings("CallToPrintStackTrace")
    private void senhaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_senhaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            conexao.conecta();
            conexao.executaSQL("select * from login");
            try {
                conexao.rs.first();
                try {
                    conexao.executaSQL("select * from login where usuario='" + usuario.getText() + "'");
                    salvarVerificador();
                    conexao.rs.first();
                    if (conexao.rs.getString("senha").equals(new String(senha.getPassword()))) {
                        JOptionPane.showMessageDialog(rootPane, "Bem vindo " + usuario.getText());
                        botVenda.setEnabled(true);
                        botProduto.setEnabled(true);
                        botFornecedor.setEnabled(true);
                        botHistorico.setEnabled(true);
                        botAgenda.setEnabled(true);
                        botSite.setEnabled(true);
                        usuario.setEnabled(false);
                        senha.setEnabled(false);
                        botPesquisa.setEnabled(true);
                        flag = true;
                        pesquisa.setEnabled(true);
                        AbrirCaixa tela = new AbrirCaixa();
                        tela.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Usuário ou senha errados.");
                        usuario.setText("");
                        senha.setText("");
                    }
                    checarAgenda();
                    mostrarFiador();

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            conexao.desconecta();
        }
    }//GEN-LAST:event_senhaKeyPressed

    @SuppressWarnings("CallToPrintStackTrace")
    private void bloquearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bloquearMouseClicked

        if (flag == true) {
            conexao.conecta();
            try {
                conexao.executaSQL("select * from login where usuario='" + usuario.getText() + "'");
                conexao.rs.first();
                if (conexao.rs.getString("senha").equals(new String(senha.getPassword()))) {
                    botVenda.setEnabled(true);
                    botProduto.setEnabled(true);
                    botFornecedor.setEnabled(true);
                    botHistorico.setEnabled(true);
                    botAgenda.setEnabled(true);
                    botSite.setEnabled(true);
                    usuario.setEnabled(false);
                    senha.setEnabled(false);
                    botPesquisa.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Senha errada!");
                    senha.setText("");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            conexao.desconecta();

            senha.setText("");
            senha.setEnabled(true);
            senha.setFont(new Font("Serif", Font.PLAIN, 18));
            botVenda.setEnabled(false);
            botProduto.setEnabled(false);
            botFornecedor.setEnabled(false);
            botHistorico.setEnabled(false);
            botAgenda.setEnabled(false);
            botSite.setEnabled(false);
            JOptionPane.showMessageDialog(rootPane, "Usuario " + usuario.getText() + " bloqueado, coloque sua senha para desbloquear.");
        } else {
            JOptionPane.showMessageDialog(rootPane, "Faça o login primeiro!");
        }
    }//GEN-LAST:event_bloquearMouseClicked

    private void botMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botMenuMouseClicked

        if (action) {
            bloquear.setVisible(true);
            botSobre.setVisible(true);
        } else {
            bloquear.setVisible(false);
            botSobre.setVisible(false);
        }
        action = !action;
    }//GEN-LAST:event_botMenuMouseClicked

    private void botVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botVendaActionPerformed
        Venda tela = new Venda();
        tela.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_botVendaActionPerformed

    private void botProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botProdutoActionPerformed
        Produto tela = new Produto();
        tela.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_botProdutoActionPerformed

    private void botFornecedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botFornecedorActionPerformed
        Fornecedor tela = new Fornecedor();
        tela.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_botFornecedorActionPerformed

    private void botHistoricoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botHistoricoActionPerformed
        Historico tela = new Historico();
        tela.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_botHistoricoActionPerformed

    private void botSiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botSiteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botSiteActionPerformed

    private void botAgendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botAgendaActionPerformed
        Agenda tela = new Agenda();
        tela.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_botAgendaActionPerformed

    private void minimizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimizarMouseClicked
        setExtendedState(Principal.ICONIFIED);
    }//GEN-LAST:event_minimizarMouseClicked

    private void esqueciMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_esqueciMouseEntered
        esqueci.setText("<html><u>Esqueceu sua senha?</u></html>");

    }//GEN-LAST:event_esqueciMouseEntered

    private void esqueciMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_esqueciMouseExited
        esqueci.setText("<html><b>Esqueceu sua senha?</b></html>");
    }//GEN-LAST:event_esqueciMouseExited

    private void esqueciMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_esqueciMouseClicked
        Recuperacao tela = new Recuperacao();
        tela.setVisible(true);
    }//GEN-LAST:event_esqueciMouseClicked

    private void botVendaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botVendaMouseEntered
        venda.setText("<html><u>Venda</u></html>");
    }//GEN-LAST:event_botVendaMouseEntered

    private void botVendaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botVendaMouseExited
        venda.setText("<html>Venda</html>");
    }//GEN-LAST:event_botVendaMouseExited

    private void botProdutoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botProdutoMouseEntered
        produtos.setText("<html><u>Produto</u></html>");
    }//GEN-LAST:event_botProdutoMouseEntered

    private void botProdutoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botProdutoMouseExited
        produtos.setText("<html>Produto</html>");
    }//GEN-LAST:event_botProdutoMouseExited

    private void botFornecedorMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botFornecedorMouseEntered
        fornecedor.setText("<html><u>Fornecedor</u></html>");
    }//GEN-LAST:event_botFornecedorMouseEntered

    private void botFornecedorMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botFornecedorMouseExited
        fornecedor.setText("<html>Fornecedor</html>");
    }//GEN-LAST:event_botFornecedorMouseExited

    private void botHistoricoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botHistoricoMouseEntered
        historico.setText("<html><u>Histórico</u></html>");
    }//GEN-LAST:event_botHistoricoMouseEntered

    private void botHistoricoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botHistoricoMouseExited
        historico.setText("<html>Histórico</html>");
    }//GEN-LAST:event_botHistoricoMouseExited

    private void botAgendaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botAgendaMouseEntered
        agenda.setText("<html><u>Agenda</u></html>");
    }//GEN-LAST:event_botAgendaMouseEntered

    private void botAgendaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botAgendaMouseExited
        agenda.setText("<html>Agenda</html>");
    }//GEN-LAST:event_botAgendaMouseExited

    private void botSiteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botSiteMouseEntered
        site.setText("<html><u>Site</u></html>");
    }//GEN-LAST:event_botSiteMouseEntered

    private void botSiteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botSiteMouseExited
        site.setText("<html>Site</html>");
    }//GEN-LAST:event_botSiteMouseExited

    private void pesquisaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pesquisaKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (pesquisa.getText().equalsIgnoreCase("venda")) {
                Venda tela = new Venda();
                tela.setVisible(true);
                this.dispose();
            } else if (pesquisa.getText().equalsIgnoreCase("produto")) {
                Produto tela = new Produto();
                tela.setVisible(true);
                this.dispose();
            } else if (pesquisa.getText().equalsIgnoreCase("fornecedor")) {
                Fornecedor tela = new Fornecedor();
                tela.setVisible(true);
                this.dispose();
            } else if (pesquisa.getText().equalsIgnoreCase("fornecedor")) {
                Historico tela = new Historico();
                tela.setVisible(true);
                this.dispose();
            } else if (pesquisa.getText().equalsIgnoreCase("agenda")) {
                Agenda tela = new Agenda();
                tela.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(rootPane, "Tela não encontrada. Tente novamente ou clique no ícone.");
            }
        }
    }//GEN-LAST:event_pesquisaKeyPressed

    private void botPesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botPesquisaActionPerformed
        if (pesquisa.getText().equalsIgnoreCase("venda")) {
            Venda tela = new Venda();
            tela.setVisible(true);
            this.dispose();
        } else if (pesquisa.getText().equalsIgnoreCase("produto")) {
            Produto tela = new Produto();
            tela.setVisible(true);
            this.dispose();
        } else if (pesquisa.getText().equalsIgnoreCase("fornecedor")) {
            Fornecedor tela = new Fornecedor();
            tela.setVisible(true);
            this.dispose();
        } else if (pesquisa.getText().equalsIgnoreCase("fornecedor")) {
            Historico tela = new Historico();
            tela.setVisible(true);
            this.dispose();
        } else if (pesquisa.getText().equalsIgnoreCase("agenda")) {
            Agenda tela = new Agenda();
            tela.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(rootPane, "Tela não encontrada. Tente novamente ou clique no ícone.");
        }
    }//GEN-LAST:event_botPesquisaActionPerformed

    private void botSobreMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botSobreMouseClicked

        Sobre tela = new Sobre();
        tela.setVisible(true);
    }//GEN-LAST:event_botSobreMouseClicked

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
            java.util.logging.Logger.getLogger(Principal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Principal().setVisible(true);            
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel agenda;
    private javax.swing.JLabel bloquear;
    private javax.swing.JButton botAgenda;
    private javax.swing.JButton botFornecedor;
    private javax.swing.JButton botHistorico;
    private javax.swing.JLabel botMenu;
    private javax.swing.JButton botPesquisa;
    private javax.swing.JButton botProduto;
    private javax.swing.JLabel botSair;
    private javax.swing.JButton botSite;
    private javax.swing.JLabel botSobre;
    private javax.swing.JButton botVenda;
    private javax.swing.JLabel copyright;
    private javax.swing.JLabel data;
    private javax.swing.JButton entrar;
    private javax.swing.JLabel esqueci;
    private javax.swing.JLabel fornecedor;
    private javax.swing.JLabel historico;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JLabel jlabel2;
    private javax.swing.JLabel jlabel3;
    private javax.swing.JLabel labelSenha;
    private javax.swing.JLabel labelUsuario;
    private javax.swing.JLabel minimizar;
    private javax.swing.JTextField pesquisa;
    private javax.swing.JLabel produtos;
    private javax.swing.JPasswordField senha;
    private javax.swing.JLabel site;
    private javax.swing.JTextField usuario;
    private javax.swing.JLabel venda;
    // End of variables declaration//GEN-END:variables
}
