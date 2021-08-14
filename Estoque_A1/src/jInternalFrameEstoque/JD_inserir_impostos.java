/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jInternalFrameEstoque;

import FormePrincipal.JFConfianca_estoque;
import bd.DAO.AliquotaSimplesNacional;
import bd.DAO.ConfigCstAlqSimples;
import bd.DAO.Cst;
import bd.DAO.CstCofins;
import bd.DAO.CstPis;
import bd.conexao.ConnectionA;
import java.awt.Color;
import java.awt.event.KeyEvent;
//import static bd.Bd.conection.connectionA;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import util.ColoracaoTabela;

/**
 *
 * @author Cliente
 */
public class JD_inserir_impostos extends javax.swing.JDialog {
    public static AliquotaSimplesNacional alq = null;
    
    
    public JD_inserir_impostos(
            java.awt.Frame parent, 
            boolean modal, 
            AliquotaSimplesNacional alq
    ){
        super(parent, modal);
        this.alq = alq;
        initComponents();
        TableCellRenderer colorRenderer = new ColoracaoTabela(null);
        jTable1.setDefaultRenderer(String.class, colorRenderer);
        for (int i = 0; i < jTable1.getModel().getColumnCount(); i++) {
            jTable1.getColumnModel().getColumn(i).setHeaderRenderer(new ColoracaoTabela(null).ColorTableHeader(jTable1, Color.BLACK));
        }
        
        abreCampos1(true);
        
        setValue(alq);
    }

    List<ConfigCstAlqSimples> listConfigCstAlqSimples = new ArrayList<>();
    
    
    public void abreCampos1(boolean b){
        jComboBox1.setEnabled(true);
        jComboBox2.setEnabled(true);
        jMoneyField2.setEnabled(true);
        jButton4.setEnabled(true);
        if(b)
        fechaCampos2(false);
    }
    public void fechaCampos1(boolean b){
        jComboBox1.setEnabled(false);
        jComboBox2.setEnabled(false);
        jMoneyField2.setEnabled(false);
        jButton4.setEnabled(false);
        if(b)
        abreCampos2(false);
    }
    public void abreCampos2(boolean b){
        jTextField4.setEnabled(true);
        jMoneyField3.setEnabled(true);
        jButton3.setEnabled(true);
        if(b)
        fechaCampos1(false);
    }
    public void fechaCampos2(boolean b){
        jTextField4.setEnabled(false);
        jMoneyField3.setEnabled(false);
        jButton3.setEnabled(false);
        if(b)
        abreCampos1(false);
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jMoneyField2 = new ClassesEntidades.JMoneyField();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox1 = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jMoneyField3 = new ClassesEntidades.JMoneyField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        jMenuItem1.setText("Remove");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/img/parametrizar.gif")).getImage());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Impostos:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Valor:");

        jMoneyField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jMoneyField2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jMoneyField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMoneyField2ActionPerformed(evt);
            }
        });
        jMoneyField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jMoneyField2KeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Tipo:");

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Check.png"))); // NOI18N
        jButton1.setText("Confirmar");
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setOpaque(false);
        jButton1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Delete1.png"))); // NOI18N
        jButton2.setText("Cancelar");
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setOpaque(false);
        jButton2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jComboBox2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ICMS", "PIS/PASEP", "COFINS", "CPP", "CSLL", "IRPJ" }));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jComboBox1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Percentual (%)", "Valor ($)" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CST", "CST", "Red. Base calc"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable1.setComponentPopupMenu(jPopupMenu1);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMinWidth(100);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(100);
            jTable1.getColumnModel().getColumn(1).setMinWidth(350);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(350);
            jTable1.getColumnModel().getColumn(1).setMaxWidth(350);
            jTable1.getColumnModel().getColumn(2).setMinWidth(100);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(2).setMaxWidth(100);
        }

        jTextField4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField4FocusLost(evt);
            }
        });
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField4KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField4KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField4KeyTyped(evt);
            }
        });

        jTextField5.setBackground(new java.awt.Color(255, 255, 204));
        jTextField5.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField5.setEnabled(false);
        jTextField5.setFocusable(false);
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("CST:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Red. Base calc.:");

        jMoneyField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jMoneyField3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jMoneyField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMoneyField3ActionPerformed(evt);
            }
        });
        jMoneyField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jMoneyField3KeyReleased(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Downloads-icon.png"))); // NOI18N
        jButton3.setText("Adicionar");
        jButton3.setFocusable(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/documentoOrigem16.png"))); // NOI18N
        jButton4.setText("Informar CST");
        jButton4.setFocusable(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jMoneyField3, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(112, 112, 112)
                                .addComponent(jButton3))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBox2, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jMoneyField2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(10, 10, 10))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jMoneyField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jMoneyField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton3, jTextField5});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        alq.setImposto(jComboBox2.getSelectedItem().toString());
        if(jComboBox1.getSelectedItem().toString().contains("%")){
            alq.setTipo('%');
        }else if(jComboBox1.getSelectedItem().toString().contains("$")){
            alq.setTipo('$');
        }
        alq.setValor(new BigDecimal(jMoneyField2.getText().replace(".", "").replace(",", ".")));
        
        //alq.setConfigCstAlqSimplesList(listConfigCstAlqSimples);
        
        JIFEmpresa.alq = alq;
        dispose();
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMoneyField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jMoneyField2KeyReleased
        
    }//GEN-LAST:event_jMoneyField2KeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jMoneyField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMoneyField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMoneyField2ActionPerformed

    private void jTextField4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField4FocusLost

    }//GEN-LAST:event_jTextField4FocusLost

    private void jTextField4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyPressed
        
        try {
            switch (jComboBox2.getSelectedIndex()) {
                case 0:
                    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                        Query q = bd.Bd.conection.connectionA.currentEntityManager().createNamedQuery("Cst.findByNumeroCst");
                        q.setParameter("numeroCst", (jTextField4.getText()));
                        List<Cst> listCst = q.getResultList();
                        if (!listCst.isEmpty()) {
                            jTextField5.setText(listCst.get(0).getDescricaoCst());
                        } else {
                            jTextField5.setText("");
                        }
                    }   if (evt.getKeyCode() == KeyEvent.VK_F2) {
                        keyPressed(evt);
                    }   break;
                case 1:
                    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                        
                        if (!jTextField4.getText().equals("")) {
                            Query cstPisQuery = bd.Bd.conection.connectionA.currentEntityManager().createNamedQuery("CstPis.findByNumeroCst");
                            cstPisQuery.setParameter("numeroCst", jTextField4.getText());
                            List<CstPis> data;
                            data = cstPisQuery.getResultList();
                            jTextField5.setText(data.get(0).getDescricaoCst());
                        }
                    }   if (evt.getKeyCode() == KeyEvent.VK_F2) {
                        
                        keyPressedPis(evt);
                        
                    }   break;
                case 2:
                    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                        
                        if (!jTextField4.getText().equals("")) {
                            Query cstCofinsQuery = bd.Bd.conection.connectionA.currentEntityManager().createNamedQuery("CstCofins.findByNumeroCst");
                            cstCofinsQuery.setParameter("numeroCst", jTextField4.getText());
                            List<CstCofins> data;
                            data = cstCofinsQuery.getResultList();
                            jTextField5.setText(data.get(0).getDescricaoCst());
                        }
                    }   if (evt.getKeyCode() == KeyEvent.VK_F2) {
                        
                        keyPressedCofins(evt);
                        
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            Logger.getLogger(JD_inserir_impostos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            bd.Bd.conection.connectionA.closeEntityManager();
        }

    }//GEN-LAST:event_jTextField4KeyPressed

    private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyReleased
        
    }//GEN-LAST:event_jTextField4KeyReleased

    private void jTextField4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyTyped
        int ascii = evt.getKeyChar();

        if (!(ascii >= 48 && ascii <=57) && !(ascii == evt.VK_BACK_SPACE)){

            evt.consume() ;

        }

        switch (jComboBox2.getSelectedIndex()) {
            case 0:
                if (bd.Bd.conection.empresa.getCrt() == '1') {
                    if (jTextField4.getText().length() >= 4) {
                        evt.consume();
                    }
                } else {
                    if (jTextField4.getText().length() >= 3) {
                        evt.consume();
                    }
                }
                break;
            case 1:
                if (jTextField4.getText().length() > 2) {
                    evt.consume();
                }
                break;
            case 2:
                if (jTextField4.getText().length() > 2) {
                    evt.consume();
                }
                break;
            default:
                break;
        }
        
        
    }//GEN-LAST:event_jTextField4KeyTyped

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jMoneyField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMoneyField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMoneyField3ActionPerformed

    private void jMoneyField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jMoneyField3KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jMoneyField3KeyReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        addCstTable(jTextField4.getText(), jTextField5.getText(), jMoneyField3.getText());
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        removeCstTable();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if(jButton4.getText().equals("Informar CST")){
            abreCampos2(true);
        }else{
            fechaCampos2(true);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton jButton1;
    public javax.swing.JButton jButton2;
    public javax.swing.JButton jButton3;
    public javax.swing.JButton jButton4;
    public javax.swing.JComboBox<String> jComboBox1;
    public javax.swing.JComboBox<String> jComboBox2;
    public javax.swing.JLabel jLabel1;
    public javax.swing.JLabel jLabel2;
    public javax.swing.JLabel jLabel3;
    public javax.swing.JLabel jLabel4;
    public javax.swing.JLabel jLabel5;
    public javax.swing.JMenuItem jMenuItem1;
    public ClassesEntidades.JMoneyField jMoneyField2;
    public ClassesEntidades.JMoneyField jMoneyField3;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JPopupMenu jPopupMenu1;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTable jTable1;
    public static javax.swing.JTextField jTextField4;
    public static javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables

     public void keyPressed(KeyEvent e) {
        // a tecla F2 foi pressionada
        if (e.getKeyCode() == KeyEvent.VK_F2) {
            JDcst cst = new JDcst(null, true);
            cst.temp = 2;
            cst.setVisible(true);

        }
    }
     
    public void keyPressedPis(KeyEvent e) {
        // a tecla F2 foi pressionada
        if (e.getKeyCode() == KeyEvent.VK_F2) {
            JDcst_pis cst = new JDcst_pis(null, true, 2);
            cst.setVisible(true);

        }
    }
    
    public void keyPressedCofins(KeyEvent e) {
        // a tecla F2 foi pressionada
        if (e.getKeyCode() == KeyEvent.VK_F2) {
            JDcst_cofins cst = new JDcst_cofins(null, true);
            cst.temp = 1;
            cst.setVisible(true);

        }
    }
    
    public void addCstTable(String codigo, String cst, String red){
        
        if (addlistConfigCstAlqSimples(codigo, red)) {
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

            model.addRow(new Vector(model.getRowCount() + 1));
            jTable1.setModel(model);
            jTable1.setRowSelectionInterval(jTable1.getRowCount() - 1, jTable1.getRowCount() - 1);
            jTable1.setValueAt(codigo, jTable1.getSelectedRow(), 0);
            jTable1.setValueAt(cst, jTable1.getSelectedRow(), 1);
            jTable1.setValueAt(red, jTable1.getSelectedRow(), 2);
        }
        
    }
    
    public boolean addlistConfigCstAlqSimples(String cst, String red){
        
        ConfigCstAlqSimples config = new ConfigCstAlqSimples(getCST(cst), new BigDecimal(red.replace(".", "").replace(",", ".")));
        if(config!=null){
            try{
                config.setCodigoAlqSimples(alq);
                ConnectionA.currentEntityManager().persist(config);
            }catch(Exception ex){
                Logger.getLogger(JD_inserir_impostos.class.getName()).log(Level.SEVERE, null, ex);
            }
            listConfigCstAlqSimples.add(config);
            return true;
        }
        return false;
                
    }
    public void removeCstTable(){
        DefaultTableModel model = (DefaultTableModel)
                jTable1.getModel();
        
        model.removeRow(jTable1.getSelectedRow());
        jTable1.setModel(model);
    }
    
    public Object getCST(String cst){
        
        try{
        if(jComboBox2.getSelectedItem().toString().equalsIgnoreCase("ICMS")){
            return  ConnectionA.currentEntityManager().find(Cst.class, cst);
        }else if(jComboBox2.getSelectedItem().toString().equalsIgnoreCase("PIS/PASEP")||jComboBox2.getSelectedItem().toString().equalsIgnoreCase("COFINS")){
            return ConnectionA.currentEntityManager().find(CstPis.class, cst);
        }
        }catch(Exception ex){
            Logger.getLogger(JD_inserir_impostos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void setValue(AliquotaSimplesNacional alq){
        if(alq!=null){
            jComboBox2.setSelectedItem(alq.getImposto()!=null ? alq.getImposto() : "");
            jComboBox1.setSelectedItem(alq.getTipo() != null ? String.valueOf(alq.getTipo()) : "");
            jMoneyField2.setText(alq.getValor() != null ? String.format("%.2f", alq.getValor().doubleValue()) : "0,00");
        }
    }
    
}
