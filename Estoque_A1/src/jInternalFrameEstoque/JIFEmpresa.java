package jInternalFrameEstoque;

import Exception.ConnectionClosedException;
import bd.DAO.AliquotaSimplesNacional;
import bd.DAO.Cep;
import bd.DAO.ConfigTaxaCartao;
import bd.DAO.Configuracao;
import bd.DAO.ContaBancaria;
import bd.DAO.Contabilista;
import bd.DAO.GupoProduto;
import jMoneyField.JMoneyField;
import bd.DAO.Pardigital;
import bd.DAO.TipoPagamentoCartao;
import bd.DAO.TipoPagamentoCartaoPK;
import bd.DAO.TipoPagamentoPromissoria;
import static bd.conexao.conection.connectionA;
import utilitarios.Biblioteca;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.RollbackException;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.persistence.Query;
import javax.swing.JTable;

public class JIFEmpresa extends javax.swing.JInternalFrame {

    
    public static AliquotaSimplesNacional alq = null;
    Configuracao configuracao;
    
    public JIFEmpresa() {
        try {
            initComponents();
            jLabel72.setVisible(false);
            configuracao = bd.Bd.conection.connectionA.currentEntityManager().find(Configuracao.class, 1);
            fechaCampos();
            setText(0);
            situacaoTributaria(0);
            modelTableSimplesNacional = new DefaultTableModel(
                    new Object[][]{},
                    new String[]{
                        "Impostos",
                        "Valor",
                        "Tipo"
                    }
            ) {
                Class[] types = new Class[]{
                    java.lang.String.class,
                    java.lang.String.class,
                    java.lang.String.class
                };
                boolean[] canEdit = new boolean[]{
                    false,
                    false,
                    false
                };
                
                public Class getColumnClass(int columnIndex) {
                    return types[columnIndex];
                }
                
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit[columnIndex];
                }
            };
            jTable6.setModel(modelTableSimplesNacional);
            
            modelTableFormaPagamento = new DefaultTableModel(
                    new Object[][]{},
                    new String[]{
                        "Forma de pagamento"
                    }
            ) {
                Class[] types = new Class[]{
                    java.lang.String.class
                };
                boolean[] canEdit = new boolean[]{
                    false
                };
                
                public Class getColumnClass(int columnIndex) {
                    return types[columnIndex];
                }
                
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit[columnIndex];
                }
            };
            jTable1.setModel(modelTableFormaPagamento);
            
            modelTableFormaPagamentoAvista = new DefaultTableModel(
                    new Object[][]{},
                    new String[]{
                        "Forma de pagamento"
                    }
            ) {
                Class[] types = new Class[]{
                    java.lang.String.class
                };
                boolean[] canEdit = new boolean[]{
                    false
                };
                
                public Class getColumnClass(int columnIndex) {
                    return types[columnIndex];
                }
                
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit[columnIndex];
                }
            };
            jTable5.setModel(modelTableFormaPagamentoAvista);
            
            modelTableFormaPagamento2 = new DefaultTableModel(
                    new Object[][]{},
                    new String[]{
                        "Forma de pagamento"
                    }
            ) {
                Class[] types = new Class[]{
                    java.lang.String.class
                };
                boolean[] canEdit = new boolean[]{
                    false
                };
                
                public Class getColumnClass(int columnIndex) {
                    return types[columnIndex];
                }
                
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit[columnIndex];
                }
            };
            
            modelTableFormaPagamento3 = new DefaultTableModel(
                    new Object[][]{},
                    new String[]{
                        "Forma de pagamento", "Tipo", "Em", "Remcebimento"
                    }
            ) {
                Class[] types = new Class[]{
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
                };
                boolean[] canEdit = new boolean[]{
                    false, false, false, false
                };
                
                public Class getColumnClass(int columnIndex) {
                    return types[columnIndex];
                }
                
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit[columnIndex];
                }
            };
            jTable3.setModel(modelTableFormaPagamento3);
            
            jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()) {
                        return;
                    }
                    int selected = jTable1.getSelectedRow();
                    if (selected != -1) {
                        try {
                            buscaBanco(list.get(0).getTipoPagamentoList().get(selected).getCodigoBanco().getCodigoBanco());
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(rootPane, "Houve um erro ao consultar a conta bancária.\nVerifica se as forma de pagamento destinada a emissão de boleto tem um conta bancária parametrizada.");
                            System.err.println(ex);
                        }
                    }
                }
            });
            
            jTable2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()) {
                        return;
                    }
                    int selected = jTable2.getSelectedRow();
                    if (selected != -1) {
                        
                    }
                }
            });
            
            jTable3.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()) {
                        return;
                    }
                    int selected = jTable3.getSelectedRow();
                    if (selected != -1) {
                        bd.DAO.TipoPagamento tipoPagamento = pegaTipoPagamento();
                        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
                        model.setNumRows(0);
                        jTable2.setModel(model);
                        for (int i = 0; i < tipoPagamento.getConfigTaxaCartaoList().size(); i++) {
                            model = (DefaultTableModel) jTable2.getModel();
                            model.setNumRows(jTable2.getRowCount() + 1);
                            jTable2.setModel(model);
                            
                            try {
                                jTable2.setValueAt(
                                        tipoPagamento.getConfigTaxaCartaoList().get(i).getAPrazo() == 1 ? "A prazo" : "A vista",
                                        jTable2.getRowCount() - 1, 0);
                            } catch (NullPointerException exx) {
                                jTable2.setValueAt(
                                        "A vista",
                                        jTable2.getRowCount() - 1, 0);
                            } catch (Exception ex) {
                            }
                            
                            try {
                                jTable2.setValueAt(
                                        jComboBox5.getModel().getElementAt(tipoPagamento.getConfigTaxaCartaoList().get(i).getTipoIncidencia()),
                                        i, 1);
                            } catch (Exception ex) {
                            }
                            
                            try {
                                jTable2.setValueAt(tipoPagamento.getConfigTaxaCartaoList().get(i).getDescricao(), i, 2);
                            } catch (Exception ex) {
                            }
                            
                            try {
                                JMoneyField field = new JMoneyField();
                                field.setText(tipoPagamento.getConfigTaxaCartaoList().get(i).getValor().toString());
                                jTable2.setValueAt(field.getText(), i, 3);
                            } catch (Exception ex) {
                            }
                            
                            try {
                                jTable2.setValueAt(tipoPagamento.getConfigTaxaCartaoList().get(i).getId(), i, 5);
                            } catch (Exception ex) {
                            }
                            
                        }
                    }
                }
            });
            
            for (int i = 0; i < list.get(0).getAliquotaSimplesNacionalList().size(); i++) {
                modelTableSimplesNacional.setNumRows(jTable6.getRowCount() + 1);
                jTable6.setModel(modelTableSimplesNacional);
                try {
                    jTable6.setValueAt(list.get(0).getAliquotaSimplesNacionalList().get(i).getImposto(), jTable6.getRowCount() - 1, 0);
                } catch (Exception e) {
                }
                try {
                    jTable6.setValueAt(list.get(0).getAliquotaSimplesNacionalList().get(i).getValor().toString(), jTable6.getRowCount() - 1, 1);
                } catch (Exception e) {
                }
                try {
                    jTable6.setValueAt(String.valueOf(list.get(0).getAliquotaSimplesNacionalList().get(i).getTipo()), jTable6.getRowCount() - 1, 2);
                } catch (Exception e) {
                }
            }
            
            for (int i = 0; i < list.get(0).getTipoPagamentoList().size(); i++) {
                modelTableFormaPagamento.setNumRows(jTable1.getRowCount() + 1);
                jTable1.setModel(modelTableFormaPagamento);
                jTable1.setValueAt(list.get(0).getTipoPagamentoList().get(i).getDescricaoTipoPagamento(), jTable1.getRowCount() - 1, 0);
            }
            
            for (int i = 0; i < list.get(0).getTipoPagamentoAvistaList().size(); i++) {
                modelTableFormaPagamentoAvista.setNumRows(jTable5.getRowCount() + 1);
                jTable5.setModel(modelTableFormaPagamentoAvista);
                try {
                    jTable5.setValueAt(list.get(0).getTipoPagamentoAvistaList().get(i).getDescricaoTipoPagamento(), jTable5.getRowCount() - 1, 0);
                } catch (ArrayIndexOutOfBoundsException a) {
                }
            }
            
            for (int i = 0; i < list.get(0).getTipoPagamentoPromissoriaList().size(); i++) {
                modelTableFormaPagamento2.setNumRows(jTable4.getRowCount() + 1);
                jTable4.setModel(modelTableFormaPagamento2);
                jTable4.setValueAt(list.get(0).getTipoPagamentoPromissoriaList().get(i).getTipoPagamento().getDescricaoTipoPagamento(), jTable4.getRowCount() - 1, 0);
            }
            
            for (int i = 0; i < list.get(0).getTipoPagamentoCartaoList().size(); i++) {
                modelTableFormaPagamento3.setNumRows(jTable3.getRowCount() + 1);
                jTable3.setModel(modelTableFormaPagamento3);
                jTable3.setValueAt(list.get(0).getTipoPagamentoCartaoList().get(i).getTipoPagamento().getDescricaoTipoPagamento(), jTable3.getRowCount() - 1, 0);
                jTable3.setValueAt(list.get(0).getTipoPagamentoCartaoList().get(i).getTipo(), jTable3.getRowCount() - 1, 1);
                if (list.get(0).getTipoPagamentoCartaoList().get(i).getTipo().equals("Dia Fixo")) {
                    jTable3.setValueAt(list.get(0).getTipoPagamentoCartaoList().get(i).getDiaFixo(), jTable3.getRowCount() - 1, 2);
                } else if (list.get(0).getTipoPagamentoCartaoList().get(i).getTipo().equals("Intervalo de dias")) {
                    jTable3.setValueAt(list.get(0).getTipoPagamentoCartaoList().get(i).getQtdDia(), jTable3.getRowCount() - 1, 2);
                }
                jTable3.setValueAt(
                        list.get(0).getTipoPagamentoCartaoList().get(i).getRecebimentoPacelado() == 0 ? "Parcelado" : "A vista",
                        jTable3.getRowCount() - 1, 3
                );
                
                
            }
            
            trataComboBoxGrupo();
            trataComboBoxPromissoria();
            trataComboBoxBoleto();
            trataComboBoxCartao();
            TabelaDesconto(0);
            
            //grupo produto
            if (ConsultaDescontoMaximoGrupo()) {
                jCheckBox17.setSelected(true);
                SetDescontoMaximoGrupo();
            } else {
                FecharCamposDescontoMaximoGrupo();
            }
            
            //Desconto maximo
            if (ConsultaDescontoMaximo()) {
                //AbrirCamposDescontoMaximo();2
                SetDescontoMaximo();
            } else {
                FecharCamposDescontoMaximo();
            }
            
            
            List<AliquotaSimplesNacional> aliquotaSimplesNacionalList =
                connectionA.currentEntityManager().createNamedQuery("AliquotaSimplesNacional.findAll")
                    .getResultList();
                        if(aliquotaSimplesNacionalList.isEmpty()){
                    try {
                        if (!connectionA.currentEntityManager().isOpen() || !connectionA.currentEntityManager().getTransaction().isActive()) {
                            connectionA.currentEntityManager().getTransaction().begin();
                        }
                        String sql = "ALTER TABLE aliquota_simples_nacional ALTER COLumn valor TYPE numeric(10,2)";
                        connectionA.currentEntityManager().createNativeQuery(sql).executeUpdate();
                    } catch (Exception ex) {
                        connectionA.currentEntityManager().getTransaction().rollback();
                    }
                    connectionA.currentEntityManager().getTransaction().commit();
                }
        } catch (Exception ex) {
            Logger.getLogger(JIFEmpresa.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            bd.Bd.conection.connectionA.closeEntityManager();
        }

    }

    DefaultTableModel modelTableFormaPagamento;
    DefaultTableModel modelTableFormaPagamento2;
    DefaultTableModel modelTableFormaPagamento3;
    DefaultTableModel modelTableFormaPagamentoAvista;
    DefaultTableModel modelTableSimplesNacional;
    DefaultTableModel modelTableGrupoDesconto;
    Pardigital pardigital = null;

    int row = 0;

    public void AbrirCamposDescontoMaximoProduto() {
        jComboBox12.setEnabled(true);
        jMoneyField1.setEnabled(true);
        jButton9.setEnabled(true);
        jTable7.setEnabled(true);
    }

    public void AbrirCamposDescontoMaximo() {
        jMoneyField2.setEnabled(true);
        jButton10.setEnabled(true);
    }

    public void FecharCamposDescontoMaximoGrupo() {
        jComboBox12.setEnabled(false);
        jMoneyField1.setEnabled(false);
        jButton9.setEnabled(false);
        jTable7.setEnabled(false);
    }

    public void FecharCamposDescontoMaximo() {
        jMoneyField2.setEnabled(false);
        jButton10.setEnabled(false);
        jLabel72.setVisible(false);
    }

    public boolean ConsultaDescontoMaximoGrupo() {
        boolean retorno = false;
        if (configuracao!=null) {
            try {
                if (configuracao.getDescontoMaximoGrupo() == 1) {
                    retorno = true;
                }
            } catch (Exception ex) {
            }
        }
        return retorno;
    }

    public boolean ConsultaDescontoMaximo() {
        boolean retorno = false;
        if (configuracao != null) {
            try {
                if (configuracao.getDescontoMaximoVendedor() != BigDecimal.ZERO) {
                    retorno = true;
                }
            } catch (Exception ex) {
            }
        }
        return retorno;
    }

    public void TabelaDesconto(int row) {
        modelTableGrupoDesconto = new DefaultTableModel(
                new String[]{
                    "Grupo",
                    "Desconto (%)",
                    ""
                }, row
        ) {
            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                GupoProduto.class
            };
            boolean[] canEdit = new boolean[]{
                false,
                false,
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        jTable7.setModel(modelTableGrupoDesconto);
    }

    public void situacaoTributaria(int index) {
        //jCheckBox8.setEnabled(false);
        if (list.get(index).getCrt() != '1') {
            jCheckBox9.setEnabled(true);
            jCheckBox10.setEnabled(true);
            jCheckBox11.setEnabled(true);
            jCheckBox12.setEnabled(true);
            jCheckBox13.setEnabled(true);
            jCheckBox14.setEnabled(true);
        } else {
            jCheckBox9.setEnabled(false);
            jCheckBox10.setEnabled(false);
            jCheckBox11.setEnabled(false);
            jCheckBox12.setEnabled(false);
            jCheckBox13.setEnabled(false);
            jCheckBox14.setEnabled(false);
        }
    }

    public void setText(int index) {

        try {
            if (list.get(0).getExibirCodigoProduto() == 0) {
                jRadioButton3.setSelected(true);
            } else {
                jRadioButton4.setSelected(true);
            }
        } catch (Exception ex) {
            jRadioButton3.setSelected(true);
        }

        try {
            if (list.get(index).getTpEmissao() == 0) {
                jRadioButton1.setSelected(true);
            } else if (list.get(index).getTpEmissao() == 1) {
                jRadioButton2.setSelected(true);
            }
        } catch (NullPointerException ex) {
            jRadioButton1.setSelected(true);
        }

        try {
            if (list.get(index).getAtividade().isEmpty()) {
                list.get(index).setAtividade(" ");
            } else {
                jComboBox4.setSelectedItem(list.get(index).getAtividade());
            }
        } catch (NullPointerException e) {

        }

        try {
            if (list.get(index).getTef() == 1) {
                jRadioButton7.setSelected(true);
            } else {
                jRadioButton8.setSelected(true);
            }
        } catch (NullPointerException e) {
            list.get(index).setTef(0);
            jRadioButton8.setSelected(true);
        }
        if (list.get(index).getEmissaoBoleto() == 1) {
            jCheckBox1.setSelected(true);
        } else {
            jCheckBox1.setSelected(false);
        }
        if (list.get(index).getEmissaoPromissoria() == 1) {
            jCheckBox13.setSelected(true);
        } else {
            jCheckBox13.setSelected(false);
        }

        jComboBox1.setSelectedItem(list.get(index).getEnquadramento());
        jComboBox2.setSelectedItem(list.get(index).getTipoTributario());
        if (list.get(index).getTpEmissao() == 0) {
            jRadioButton1.setSelected(true);
        } else if (list.get(index).getTpEmissao() == 1) {
            jRadioButton1.setSelected(false);
        }
        if (list.get(index).getEmissaoBoleto() == 0) {
            jCheckBox1.setSelected(true);
        } else {
            jCheckBox1.setSelected(false);
        }
        try {
            if (list.get(index).getGasGlp() == 1) {
                jCheckBox15.setSelected(true);
            } else {
                jCheckBox15.setSelected(false);
            }
        } catch (Exception e) {
            jCheckBox15.setSelected(false);
        }

        try {
            if (list.get(index).getPrestadorServico() == 1) {
                jCheckBox16.setSelected(true);
            } else {
                jCheckBox16.setSelected(false);
            }
        } catch (Exception e) {
            jCheckBox16.setSelected(false);
        }
        
        try {
            if (list.get(index).getConfiguracao().getControlaEstoqueServico() == 1) {
                jRadioButton9.setSelected(true);
            } else {
                jRadioButton10.setSelected(true);
            }
        } catch (Exception e) {
            jRadioButton10.setSelected(true);
        }
        
        try {
            jTextField1.setText(String.valueOf(list.get(index).getCodigoEmpresa()));
        } catch (Exception e) {
        }
        try {
            jTextField_razao_social1.setText(String.valueOf(list.get(index).getNomeEmpresa()));
        } catch (Exception e) {
        }
        try {
            jTextField_nome_fantazia1.setText(String.valueOf(list.get(index).getFantasiaEmpresa()));
        } catch (Exception e) {
        }
        try {
            jTextField4.setText(String.valueOf(list.get(index).getCnpjEmpresa()));
        } catch (Exception e) {
        }
        try {
            jTextField5.setText(String.valueOf(list.get(index).getIeEmpresa()));
        } catch (Exception e) {
        }
        try {
            jTextField6.setText(String.valueOf(list.get(index).getImEmpresa()));
        } catch (Exception e) {
        }
        try {
            jTextField7.setText(String.valueOf(list.get(index).getCepEmpresa()));
        } catch (Exception e) {
        }
        try {
            jTextField_endereco.setText(String.valueOf(list.get(index).getEnderecoEmpresa()));
        } catch (Exception e) {
        }
        try {
            jTextField_numero_endereco1.setText(String.valueOf(list.get(index).getNumeroEmpresa()));
        } catch (Exception e) {
        }
        try {
            jTextField8.setText(String.valueOf(list.get(index).getBairroEmpresa()));
        } catch (Exception e) {
        }
        try {
            jTextField12.setText(String.valueOf(list.get(index).getComplemento()));
        } catch (Exception e) {
        }
        try {
            jTextField9.setText(String.valueOf(list.get(index).getCodigoMunicipio().getCodigo()));
        } catch (Exception e) {
        }
        try {
            jTextField.setText(String.valueOf(list.get(index).getCodigoMunicipio().getNome()));
        } catch (Exception e) {
        }
        try {
            jTextField2.setText(String.valueOf(list.get(index).getCodigoMunicipio().getCodigoUf()));
        } catch (Exception e) {
        }
        try {
            jTextField3.setText(String.valueOf(list.get(index).getCodigoMunicipio().getUf()));
        } catch (Exception e) {
        }
        try {
            jTextField13.setText(String.valueOf(list.get(index).getTelefone()));
        } catch (Exception e) {
        }
        try {
            jTextField14.setText(String.valueOf(list.get(index).getEmail()));
        } catch (Exception e) {
        }
        try {
            jDateChooser_data_cadastro1.setDate(list.get(index).getDataCadastro());
        } catch (Exception e) {
        }
        try {
            jTextField11.setText(String.valueOf(list.get(index).getCodigoMunicipio().getCodigoPais()));
        } catch (Exception e) {
        }
        try {
            jTextField10.setText("BRASIL");
        } catch (Exception e) {
        }
        try {
            jTextField21.setText(String.valueOf(list.get(index).getCrt()));
        } catch (Exception e) {
        }
        try {
            jTextField35.setText(list.get(index).getSmtp());
        } catch (Exception e) {
        }
        try {
            switch (jTextField35.getText()) {
                case "smtp.gmail.com":
                    jComboBox10.setSelectedItem("Gmail");
                    break;
                case "smtp-mail.outlook.com":
                    jComboBox10.setSelectedItem("Hotmail");
                    break;
                default:
                    jComboBox10.setSelectedItem("Outros");
                    break;
            }
        } catch (Exception e) {
        }
        try {
            jTextField36.setText(list.get(index).getPorta());
        } catch (Exception e) {
        }
        try {
            jTextField37.setText(list.get(index).getEmail2());
        } catch (Exception e) {
        }
        try {
            jPasswordField1.setText(list.get(index).getSenhaEmail());
        } catch (Exception e) {
        }
        try {
            jPasswordField2.setText(list.get(index).getSenhaEmail());
        } catch (Exception e) {
        }
        try {
            jTextField38.setText(String.valueOf(list.get(index).getContabilista().getNomeContabilista()));
        } catch (Exception ex) {
        }
        try {
            jTextField39.setText(String.valueOf(list.get(index).getContabilista().getCpfContabilista()));
        } catch (Exception ex) {
        }
        try {
            jTextField40.setText(String.valueOf(list.get(index).getContabilista().getCnpjContabilista()));
        } catch (Exception ex) {
        }
        try {
            jTextField41.setText(String.valueOf(list.get(index).getContabilista().getInscCrc()));
        } catch (Exception ex) {
        }
        try {
            jTextField42.setText(String.valueOf(list.get(index).getContabilista().getCep().getCep()));
        } catch (Exception ex) {
        }
        try {
            jTextField43.setText(String.valueOf(list.get(index).getContabilista().getLogradouro()));
        } catch (Exception ex) {
        }
        try {
            jTextField44.setText(String.valueOf(list.get(index).getContabilista().getNumero()));
        } catch (Exception ex) {
        }
        try {
            jTextField45.setText(String.valueOf(list.get(index).getContabilista().getComplemento()));
        } catch (Exception ex) {
        }
        try {
            jTextField46.setText(String.valueOf(list.get(index).getContabilista().getBairro()));
        } catch (Exception ex) {
        }
        try {
            jTextField47.setText(String.valueOf(list.get(index).getContabilista().getTelefone()));
        } catch (Exception ex) {
        }
        try {
            jTextField48.setText(String.valueOf(list.get(index).getContabilista().getFax()));
        } catch (Exception ex) {
        }
        try {
            jTextField49.setText(String.valueOf(list.get(index).getContabilista().getEmail()));
        } catch (Exception ex) {
        }
        try {
            bd.DAO.CidadeCodigo cidade = bd.Bd.conection.connectionA.currentEntityManager().find(
                    bd.DAO.CidadeCodigo.class, Integer.parseInt(list.get(index).getContabilista().getCodigoCidade())
            );
            if(cidade!=null){
                jComboBox11.setSelectedItem(cidade.getNome() + " - " + cidade.getUf());
            }
        } catch (Exception ex) {
            jComboBox11.setSelectedItem(null);
        }

        try {
            if (list.get(index).getConfiguracao().getCalcComissao() == 0) {
                jCheckBox20.setSelected(true);
                    if(list.get(index).getConfiguracao().getTipoComissao()==null ){
                        jRadioButton11.setSelected(false);
                        jRadioButton12.setSelected(false);
                    } else if(list.get(index).getConfiguracao().getTipoComissao()==1){
                        jRadioButton12.setSelected(true);
                    }else{
                        jRadioButton11.setSelected(false);
                    }
                    
            } else {
                jCheckBox20.setSelected(false);
            }
            
            
            if (list.get(index).getConfiguracao().getTipoCusteio() == null ?
                    true : list.get(index).getConfiguracao().getTipoCusteio() == 0) {
                jCheckBox21.setSelected(false);
            }else {
                jCheckBox21.setSelected(true);
            }
            
            
            
            
        } catch (Exception e) {
            jRadioButton11.setSelected(false);
            jRadioButton12.setSelected(false);
        }
        
        
        homologacaoProducao();
        
        if (ConsultaDescontoMaximo()) {
            SetDescontoMaximo();
            
        }

    }

    public void homologacaoProducao() {
        
            pardigital = 
                list.get(0).getPardigitalList().get(0).getEmpresa().getTpEmissao()!=1 
                    ? list.get(0).getPardigitalList().get(1) : list.get(0).getPardigitalList().get(0);
        
        try {
            jTextField28.setText(pardigital.getUltimanf65());
        } catch (Exception e) {
        }
        try {
            jTextField29.setText(pardigital.getUltimanf55());
        } catch (Exception e) {
        }
        try {
            jTextField22.setText(pardigital.getSerie55());
        } catch (Exception e) {
        }
        try {
            jTextField23.setText(pardigital.getSerie65());
        } catch (Exception e) {
        }
        try {
            jTextField27.setText(pardigital.getCodigo() == 1 ? pardigital.getCsc() : "");
        } catch (Exception e) {
        }
        try {
            jTextField24.setText(pardigital.getCodigo() == 2 ? pardigital.getCsc() : "");
        } catch (Exception e) {
        }
        //jTextField28.setText(pardigital.getSenhaKeystore());
        //jTextField29.setText(pardigital.getSenhaToken());

    }

    public void centralizar() {
        Dimension d = this.getDesktopPane().getSize();  
        this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 2);
    }

    public void abreCampos() {

        if(jCheckBox18.isSelected()){
            AbrirCamposDescontoMaximo();
        }
        
        if(jCheckBox17.isSelected()){
            AbrirCamposDescontoMaximoProduto();
        }
        
        if(jCheckBox16.isSelected()){
            jRadioButton9.setEnabled(true);
            jRadioButton10.setEnabled(true);
        }else{
            jRadioButton9.setEnabled(false);
            jRadioButton10.setEnabled(false);
        }
        
        jCheckBox20.setEnabled(true);
        
        jTable5.setEnabled(true);
        jTable1.setEnabled(true);
        jTable4.setEnabled(true);
        
        
        jRadioButton11.setEnabled(true);
        jRadioButton12.setEnabled(true);
        
        jCheckBox21.setEnabled(true);
        jTable2.setEnabled(true);
        jCheckBox18.setEnabled(true);
        jCheckBox17.setEnabled(true);
        jTextField29.setEnabled(true);
        jTextField28.setEnabled(true);
        jTextField22.setEnabled(true);
        jTextField23.setEnabled(true);
        jTextField24.setEnabled(true);
        jCheckBox14.setEnabled(true);

        switch (jComboBox4.getSelectedItem().toString()) {
            case "Varejista em geral":
                jCheckBox15.setEnabled(true);
                jCheckBox16.setEnabled(true);
                break;
            case "Mercado, Supermercado, Hipermercado":
                jCheckBox15.setEnabled(true);
                jCheckBox16.setEnabled(false);
                break;
            case "Autopeças e Oficinas":
                jCheckBox16.setEnabled(true);
                jCheckBox15.setEnabled(false);
                break;
            default:
                jCheckBox15.setEnabled(false);
                jCheckBox16.setEnabled(false);
                break;
        }

        jTextField25.setEnabled(true);
        jTextField26.setEnabled(true);
        jTextField27.setEnabled(true);

        jComboBox9.setEnabled(true);
        jComboBox4.setEnabled(true);
        jComboBox8.setEnabled(true);
        jRadioButton1.setEnabled(true);
        jRadioButton2.setEnabled(true);
        jComboBox6.setEnabled(true);
        jButton3.setEnabled(true);
        jButton5.setEnabled(true);

        jCheckBox13.setEnabled(true);
        jComboBox7.setEnabled(true);
        jButton4.setEnabled(true);
        jTable4.setEnabled(true);
        jTextField1.setEnabled(true);
        jTextField_razao_social1.setEnabled(true);
        jTextField_nome_fantazia1.setEnabled(true);

        jTextField7.setEnabled(true);
        jTextField_endereco.setEnabled(true);
        jTextField_numero_endereco1.setEnabled(true);
        jTextField8.setEnabled(true);
        jTextField9.setEnabled(true);
        jTextField2.setEnabled(true);
        jDateChooser_data_cadastro1.setEnabled(true);
        jComboBox1.setEnabled(true);
        jTextField14.setEnabled(true);
        jTextField13.setEnabled(true);
        jTextField12.setEnabled(true);
        jRadioButton1.setEnabled(true);
        jRadioButton2.setEnabled(true);
        jCheckBox1.setEnabled(true);
        jComboBox10.setEnabled(true);
        jTextField35.setEnabled(true);
        jTextField36.setEnabled(true);
        jComboBox2.setEnabled(true);
        jTextField35.setEnabled(true);
        jTextField36.setEnabled(true);
        jTextField37.setEnabled(true);
        jPasswordField1.setEnabled(true);
        jPasswordField2.setEnabled(true);

        jComboBox5.setEnabled(true);
        jTextField32.setEnabled(true);
        jTextField33.setEnabled(true);
        jButton2.setEnabled(true);
        jTextField38.setEnabled(true);
        jTextField39.setEnabled(true);
        jTextField40.setEnabled(true);
        jTextField41.setEnabled(true);
        jTextField42.setEnabled(true);
        jTextField43.setEnabled(true);
        jTextField44.setEnabled(true);
        jTextField45.setEnabled(true);
        jTextField46.setEnabled(true);
        jTextField47.setEnabled(true);
        jTextField48.setEnabled(true);
        jTextField49.setEnabled(true);
        jComboBox11.setEnabled(true);
        jTable3.setEnabled(true);
    }

    public void fechaCampos() {

        jCheckBox18.setEnabled(false);
        
        if(jCheckBox18.isSelected()){
            FecharCamposDescontoMaximo();
        }
        
        if(jCheckBox17.isSelected()){
            FecharCamposDescontoMaximoGrupo();
        }
        
        jCheckBox20.setEnabled(false);
        jTable5.setEnabled(false);
        jTable1.setEnabled(false);
        jTable4.setEnabled(false);
        
        jRadioButton11.setEnabled(false);
        jRadioButton12.setEnabled(false);
        
        jCheckBox21.setEnabled(false);
        jComboBox4.setEnabled(false);
        jRadioButton9.setEnabled(false);
        jRadioButton10.setEnabled(false);
        jTable2.setEnabled(false);
        jCheckBox17.setEnabled(false);
        jTextField29.setEnabled(false);
        jTextField28.setEnabled(false);
        jTextField22.setEnabled(false);
        jTextField23.setEnabled(false);
        jTextField24.setEnabled(false);
        jCheckBox14.setEnabled(false);
        jCheckBox15.setEnabled(false);
        jCheckBox16.setEnabled(false);

        jTextField25.setEnabled(false);
        jTextField26.setEnabled(false);
        jTextField27.setEnabled(false);

        jCheckBox6.setEnabled(false);
        jCheckBox4.setEnabled(false);
        jCheckBox9.setEnabled(false);

        jComboBox8.setEnabled(false);
        jRadioButton1.setEnabled(false);
        jRadioButton2.setEnabled(false);
        jTextField34.setEnabled(false);
        jComboBox6.setEnabled(false);
        jButton3.setEnabled(false);
        jButton5.setEnabled(false);

        jTable3.setEnabled(false);
        jRadioButton7.setEnabled(false);
        jRadioButton8.setEnabled(false);
        jComboBox5.setEnabled(false);
        jComboBox2.setEnabled(false);
        jTextField32.setEnabled(false);
        jTextField33.setEnabled(false);
        jButton2.setEnabled(false);
        jTable2.setEnabled(false);
        jComboBox6.setEnabled(false);

        jCheckBox1.setEnabled(false);
        jComboBox3.setEnabled(false);
        jButton1.setEnabled(false);
        jTable1.setEnabled(false);

        jCheckBox13.setEnabled(false);
        jCheckBox7.setEnabled(false);
        jButton4.setEnabled(false);
        jTable4.setEnabled(false);

        jTextField1.setEnabled(false);
        jTextField_razao_social1.setEnabled(false);
        jTextField_nome_fantazia1.setEnabled(false);
        jTextField7.setEnabled(false);
        jTextField_endereco.setEnabled(false);
        jTextField_numero_endereco1.setEnabled(false);
        jTextField8.setEnabled(false);
        jTextField9.setEnabled(false);
        jTextField2.setEnabled(false);
        jDateChooser_data_cadastro1.setEnabled(false);
        jComboBox1.setEnabled(false);
        jTextField14.setEnabled(false);
        jTextField13.setEnabled(false);
        jTextField12.setEnabled(false);
        jRadioButton1.setEnabled(false);
        jRadioButton2.setEnabled(false);
        jComboBox10.setEnabled(false);
        jTextField35.setEnabled(false);
        jTextField36.setEnabled(false);
        jTextField35.setEnabled(false);
        jTextField36.setEnabled(false);
        jTextField37.setEnabled(false);
        jPasswordField1.setEnabled(false);
        jPasswordField2.setEnabled(false);

        jTextField38.setEnabled(false);
        jTextField39.setEnabled(false);
        jTextField40.setEnabled(false);
        jTextField41.setEnabled(false);
        jTextField42.setEnabled(false);
        jTextField43.setEnabled(false);
        jTextField44.setEnabled(false);
        jTextField45.setEnabled(false);
        jTextField46.setEnabled(false);
        jTextField47.setEnabled(false);
        jTextField48.setEnabled(false);
        jTextField49.setEnabled(false);
        jComboBox11.setEnabled(false);

    }

    int cancelar = 0;

    public void consulta(int codigo) {
        try {
            cidadeCodigoQuery = bd.Bd.conection.connectionA.currentEntityManager().createNamedQuery("CidadeCodigo.findByCodigo");
            cidadeCodigoQuery.setParameter("codigo", codigo);
            List<bd.DAO.CidadeCodigo> codcidade = cidadeCodigoQuery.getResultList();
            if(!codcidade.isEmpty()){
                try{
                jTextField.setText(codcidade.get(0).getNome());
                }catch(Exception ex){}
                try{
                jTextField2.setText(codcidade.get(0).getCodigoUf().toString());
                }catch(Exception ex){}
                try{
                jTextField3.setText(codcidade.get(0).getUf());
                }catch(Exception ex){}
                try{
                jTextField11.setText(codcidade.get(0).getCodigoPais().toString());
                }catch(Exception ex){
                }
            }
        } catch (Exception ex) {
        } finally {
            bd.Bd.conection.connectionA.closeEntityManager();
        }
    }

    public void buscaBanco(Integer codigo) {
        try {
            contaBancariaQuery = bd.Bd.conection.connectionA.currentEntityManager().createNamedQuery("ContaBancaria.findByCodigoBanco");
            contaBancariaQuery.setParameter("codigoBanco", codigo);
            List<ContaBancaria> contaBancaria = contaBancariaQuery.getResultList();
            if(!contaBancaria.isEmpty()){
                try{
                jTextField16.setText(String.valueOf(contaBancaria.get(0).getContaBanco()));
                }catch(Exception ex){
                }
                try{
                jTextField17.setText(String.valueOf(contaBancaria.get(0).getDigitoConta()));
                }catch(Exception ex){
                }
                try{
                jTextField19.setText(String.valueOf(contaBancaria.get(0).getAgenciaBanco()));
                }catch(Exception ex){
                }
                try{
                jTextField18.setText(String.valueOf(contaBancaria.get(0).getDigitoAgencia()));
                }catch(Exception ex){
                }
                try{
                jTextField20.setText(String.valueOf(contaBancaria.get(0).getNomeBanco()));
                }catch(Exception ex){
                }
            }
        } catch (Error e) {
            JOptionPane.showMessageDialog(this, "Erro na consulta do banco.\n\n" + e);
        } catch (ConnectionClosedException ex) {
            Logger.getLogger(JIFEmpresa.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            bd.Bd.conection.connectionA.closeEntityManager();
        }
    }

    public void formatarTabelaDesconto() {

        if (jTable7.getColumnModel().getColumnCount() > 0) {
            jTable7.getColumnModel().getColumn(0).setMinWidth(350);
            jTable7.getColumnModel().getColumn(0).setPreferredWidth(350);
            jTable7.getColumnModel().getColumn(0).setMaxWidth(350);

            jTable7.getColumnModel().getColumn(1).setMinWidth(100);
            jTable7.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTable7.getColumnModel().getColumn(1).setMaxWidth(100);

            jTable7.getColumnModel().getColumn(2).setMinWidth(0);
            jTable7.getColumnModel().getColumn(2).setPreferredWidth(0);
            jTable7.getColumnModel().getColumn(2).setMaxWidth(0);
        }
    }

    public void SetDescontoMaximo() {
        if (configuracao.getDescontoMaximoVendedor() != null
                ? configuracao.getDescontoMaximoVendedor().compareTo(BigDecimal.ZERO) != 0 : false) {
            jMoneyField2.setText(configuracao.getDescontoMaximoVendedor().toString());
            jCheckBox18.setSelected(true);
            if(!jMoneyField2.getText().equals("0,00")){
                jLabel72.setVisible(true);
            }
        }
    }

    public void SetDescontoMaximoGrupo() {
        try {
            Query q = bd.Bd.conection.connectionA.currentEntityManager().createNamedQuery("GupoProduto.findByDescontoMaximo");
            q.setParameter("desc", new BigDecimal(0.00));
            List<GupoProduto> listGrupo = q.getResultList();
            TabelaDesconto(listGrupo.size());
            for (int i = 0; i < listGrupo.size(); i++) {
                try {
                    jTable7.setValueAt(listGrupo.get(i).getDescricaoGrupo(), i, 0);
                } catch (Exception ex) {
                }
                try {
                    JMoneyField money = new JMoneyField();
                    money.setText(listGrupo.get(i).getDesconto().toString());
                    jTable7.setValueAt(money.getText(), i, 1);
                } catch (Exception ex) {
                }
                try {
                    jTable7.setValueAt(listGrupo.get(i), i, 2);
                } catch (Exception ex) {
                }
            }
        } catch (Exception ex) {
        } finally {
            bd.Bd.conection.connectionA.closeEntityManager();
        }
    }

    public bd.DAO.TipoPagamento pegaTipoPagamento() {
        bd.DAO.TipoPagamento tipoPagamento = null;
        try {
            Query q = bd.Bd.conection.connectionA.currentEntityManager().createNamedQuery("TipoPagamento.findByDescricaoTipoPagamento");
            try {
                q.setParameter("descricaoTipoPagamento", jTable3.getValueAt(jTable3.getSelectedRow(), 0));
            } catch (Exception ex) {
            }
            tipoPagamento = (bd.DAO.TipoPagamento) q.getSingleResult();
        } catch (ConnectionClosedException ex) {
            Logger.getLogger(JIFEmpresa.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            bd.Bd.conection.connectionA.closeEntityManager();
        }
        return tipoPagamento;
    }
    
    public void CriaConfiguracaoTaxaCartao() {
        try {
            bd.DAO.TipoPagamento tipoPagamento = pegaTipoPagamento();
            
            ConfigTaxaCartao cartao = new ConfigTaxaCartao();
            try {
                cartao.setDescricao(jTextField32.getText().toUpperCase());
            } catch (Exception ex) {
            }
            try {
                cartao.setTipoIncidencia(jComboBox5.getSelectedIndex());
            } catch (Exception ex) {
            }
            try {
                cartao.setTipoPagamento(tipoPagamento);
            } catch (Exception ex) {
            }
            try{
                cartao.setValor(new BigDecimal(jTextField33.getText().replace(",", ".")));
            }catch(Exception ex){
            }
            try{
                cartao.setAPrazo(jCheckBox19.isSelected()? 1 : 0);
            }catch(Exception ex){}
            
            
            bd.Bd.conection.connectionA.currentEntityManager().persist(cartao);
            if( !bd.Bd.conection.connectionA.currentEntityManager().isOpen() || !bd.Bd.conection.connectionA.currentEntityManager().getTransaction().isActive()){
                bd.Bd.conection.connectionA.currentEntityManager().getTransaction().begin();
            }
            bd.Bd.conection.connectionA.currentEntityManager().getTransaction().commit();
            
            DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
            model.setNumRows(jTable2.getRowCount() + 1);
            jTable2.setModel(model);
            
            try{
                jTable2.setValueAt(
                        jCheckBox19.isSelected() ? "A prazo" : "A vista"
                        , jTable2.getRowCount()-1, 0);
            }catch(Exception ex){
            }
            
            try{
                jTable2.setValueAt(
                        jComboBox5.getModel().getElementAt(jComboBox5.getSelectedIndex())
                        , jTable2.getRowCount()-1, 1);
            }catch(Exception ex){
            }
            
            try{
                jTable2.setValueAt(jTextField32.getText(), jTable2.getRowCount()-1, 2);
            }catch(Exception ex){}
            
            try{
                JMoneyField field = new JMoneyField();
                field.setText(jTextField33.getText());
                jTable2.setValueAt(field.getText(), jTable2.getRowCount()-1, 3);
            }catch(Exception ex){}
        
            try{
                jTable2.setValueAt(cartao.getId(), jTable2.getRowCount()-1, 5);
            }catch(Exception ex){}
            
        } catch (ConnectionClosedException ex) {
            Logger.getLogger(JIFEmpresa.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            bd.Bd.conection.connectionA.closeEntityManager();
        }
            
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        try{
            query = java.beans.Beans.isDesignTime() ? null : connectionA.currentEntityManager().createQuery("SELECT e FROM Empresa e");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            list = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : org.jdesktop.observablecollections.ObservableCollections.observableList(query.getResultList());
            cidadeCodigoQuery = java.beans.Beans.isDesignTime() ? null : connectionA.currentEntityManager().createQuery("SELECT c FROM CidadeCodigo c");
            cidadeCodigoList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : cidadeCodigoQuery.getResultList();
            contaBancariaQuery = java.beans.Beans.isDesignTime() ? null : connectionA.currentEntityManager().createQuery("SELECT c FROM ContaBancaria c");
            contaBancariaList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : contaBancariaQuery.getResultList();
            buttonGroup1 = new javax.swing.ButtonGroup();
            tipoPagamentoQuery = java.beans.Beans.isDesignTime() ? null : connectionA.currentEntityManager().createQuery("SELECT t FROM TipoPagamento t");
            tipoPagamentoList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : org.jdesktop.observablecollections.ObservableCollections.observableList(tipoPagamentoQuery.getResultList());
            buttonGroup2 = new javax.swing.ButtonGroup();
            jDialog1 = new javax.swing.JDialog();
            jPanel15 = new javax.swing.JPanel();
            jDayChooser1 = new com.toedter.calendar.JDayChooser();
            buttonGroup3 = new javax.swing.ButtonGroup();
            codigoProdutoDANFE = new javax.swing.ButtonGroup();
            jPopupMenu_remove_promossoria = new javax.swing.JPopupMenu();
            jMenuItem1 = new javax.swing.JMenuItem();
            gupoProdutoQuery = java.beans.Beans.isDesignTime() ? null : connectionA.currentEntityManager().createQuery("SELECT g FROM GupoProduto g order by g.codigoGrupo asc");
            gupoProdutoList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : gupoProdutoQuery.getResultList();
        } catch (Exception ex) {
        } finally {
            bd.Bd.conection.connectionA.closeEntityManager();
        }
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        buttonGroup4 = new javax.swing.ButtonGroup();
        Remove1 = new javax.swing.JPopupMenu();
        Remov1 = new javax.swing.JMenuItem();
        Remove2 = new javax.swing.JPopupMenu();
        Remov2 = new javax.swing.JMenuItem();
        buttonGroup5 = new javax.swing.ButtonGroup();
        jPanel7 = new javax.swing.JPanel();
        refreshButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jTextField_nome_fantazia1 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel_ie1 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel_cnpj1 = new javax.swing.JLabel();
        jTextField_endereco = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jTextField_razao_social1 = new javax.swing.JTextField();
        jTextField_numero_endereco1 = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jTextField9 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jTextField11 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextField21 = new javax.swing.JTextField();
        jDateChooser_data_cadastro1 = new com.toedter.calendar.JDateChooser();
        jPanel8 = new javax.swing.JPanel();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel11 = new javax.swing.JPanel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jLabel34 = new javax.swing.JLabel();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jCheckBox6 = new javax.swing.JCheckBox();
        jCheckBox7 = new javax.swing.JCheckBox();
        jLabel35 = new javax.swing.JLabel();
        jCheckBox8 = new javax.swing.JCheckBox();
        jCheckBox9 = new javax.swing.JCheckBox();
        jCheckBox10 = new javax.swing.JCheckBox();
        jCheckBox11 = new javax.swing.JCheckBox();
        jCheckBox12 = new javax.swing.JCheckBox();
        jSeparator3 = new javax.swing.JSeparator();
        jCheckBox15 = new javax.swing.JCheckBox();
        jCheckBox16 = new javax.swing.JCheckBox();
        jPanel29 = new javax.swing.JPanel();
        jRadioButton9 = new javax.swing.JRadioButton();
        jRadioButton10 = new javax.swing.JRadioButton();
        jLabel41 = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jRadioButton11 = new javax.swing.JRadioButton();
        jRadioButton12 = new javax.swing.JRadioButton();
        jLabel75 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jCheckBox20 = new javax.swing.JCheckBox();
        jCheckBox21 = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jTextField22 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jTextField23 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jTextField24 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jTextField28 = new javax.swing.JTextField();
        jTextField29 = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jTextField25 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jTextField26 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jTextField27 = new javax.swing.JTextField();
        jLabel73 = new javax.swing.JLabel();
        jTextField30 = new javax.swing.JTextField();
        jTextField31 = new javax.swing.JTextField();
        jLabel74 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        jComboBox10 = new javax.swing.JComboBox<>();
        jLabel45 = new javax.swing.JLabel();
        jTextField35 = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jTextField36 = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jTextField37 = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel49 = new javax.swing.JLabel();
        jPasswordField2 = new javax.swing.JPasswordField();
        jPanel9 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel25 = new javax.swing.JPanel();
        jTabbedPane6 = new javax.swing.JTabbedPane();
        jPanel27 = new javax.swing.JPanel();
        jCheckBox17 = new javax.swing.JCheckBox();
        jPanel26 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();
        jComboBox12 = new javax.swing.JComboBox<>();
        jButton9 = new javax.swing.JButton();
        jMoneyField1 = new ClassesEntidades.JMoneyField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jLabel68 = new javax.swing.JLabel();
        jMoneyField2 = new ClassesEntidades.JMoneyField();
        jButton10 = new javax.swing.JButton();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jCheckBox18 = new javax.swing.JCheckBox();
        jLabel72 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jComboBox9 = new javax.swing.JComboBox<>();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jTextField17 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jTextField19 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel13 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jComboBox7 = new javax.swing.JComboBox<>();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jCheckBox13 = new javax.swing.JCheckBox();
        jPanel12 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jTextField32 = new javax.swing.JTextField();
        jComboBox5 = new javax.swing.JComboBox<>();
        jLabel36 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        jRadioButton8 = new javax.swing.JRadioButton();
        jLabel39 = new javax.swing.JLabel();
        jRadioButton7 = new javax.swing.JRadioButton();
        jTextField33 = new javax.swing.JTextField();
        jCheckBox19 = new javax.swing.JCheckBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jComboBox6 = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jLabel42 = new javax.swing.JLabel();
        jComboBox8 = new javax.swing.JComboBox<>();
        jTextField34 = new javax.swing.JTextField();
        jCheckBox14 = new javax.swing.JCheckBox();
        jPanel18 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        jTextField38 = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        jTextField39 = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jTextField40 = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        jTextField41 = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jTextField42 = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        jTextField43 = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        jTextField44 = new javax.swing.JTextField();
        jTextField45 = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jTextField46 = new javax.swing.JTextField();
        jLabel62 = new javax.swing.JLabel();
        jTextField47 = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        jTextField48 = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        jTextField49 = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jComboBox11 = new javax.swing.JComboBox<>();
        jLabel67 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        jButton8 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jPanel20 = new javax.swing.JPanel();
        jTabbedPane5 = new javax.swing.JTabbedPane();
        jPanel21 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jPanel22 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jButton7 = new javax.swing.JButton();

        jDialog1.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialog1.setTitle("Escolha o dia de recebimento");
        jDialog1.setBackground(new java.awt.Color(255, 255, 255));
        jDialog1.setModal(true);
        jDialog1.setResizable(false);

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jDayChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jDayChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDialog1.getAccessibleContext().setAccessibleParent(this);

        jMenuItem1.setText("Remover forma de pagamento");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu_remove_promossoria.add(jMenuItem1);

        jMenuItem2.setText("Remover");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        jMenuItem3.setText("Remover");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jMenuItem3);

        Remov1.setText("Remover forma de pagamento");
        Remov1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Remov1ActionPerformed(evt);
            }
        });
        Remove1.add(Remov1);

        Remov2.setText("Remover forma de pagamento");
        Remov2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Remov2ActionPerformed(evt);
            }
        });
        Remove2.add(Remov2);

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setTitle("Cadastro de Empresas");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/img/parametrizar.gif"))); // NOI18N
        setVisible(true);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        refreshButton.setText("Editar");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        saveButton.setText("Gravar");
        saveButton.setEnabled(false);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jTextField_nome_fantazia1.setDoubleBuffered(true);
        jTextField_nome_fantazia1.setEnabled(false);
        jTextField_nome_fantazia1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_nome_fantazia1ActionPerformed(evt);
            }
        });

        jLabel14.setText("Data do Cad.:");

        jLabel_ie1.setText("IE:");
        jLabel_ie1.setPreferredSize(new java.awt.Dimension(18, 14));

        jLabel22.setText("Razão social:");

        jLabel28.setText("Nome Fantazia:");

        jLabel_cnpj1.setText("CNPJ:");

        jTextField_endereco.setDoubleBuffered(true);
        jTextField_endereco.setEnabled(false);
        jTextField_endereco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_enderecoActionPerformed(evt);
            }
        });

        jLabel17.setText("Código:");

        jLabel15.setText("Nº:");

        jLabel29.setText("CEP:");

        jLabel30.setText("Endereço:");

        jLabel31.setText("Bairro:");

        jTextField_razao_social1.setDoubleBuffered(true);
        jTextField_razao_social1.setEnabled(false);
        jTextField_razao_social1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_razao_social1ActionPerformed(evt);
            }
        });

        jTextField_numero_endereco1.setDoubleBuffered(true);
        jTextField_numero_endereco1.setEnabled(false);
        jTextField_numero_endereco1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_numero_endereco1ActionPerformed(evt);
            }
        });

        jLabel33.setText("Cidade:");

        jTextField1.setBackground(new java.awt.Color(255, 255, 204));
        jTextField1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jTextField4.setEnabled(false);

        jTextField5.setEnabled(false);
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jLabel1.setText("IM:");

        jTextField6.setEnabled(false);
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jTextField7.setEnabled(false);

        jTextField8.setEnabled(false);

        jLabel2.setText("UF:");

        jTextField.setBackground(new java.awt.Color(255, 255, 204));
        jTextField.setEnabled(false);
        jTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldActionPerformed(evt);
            }
        });

        jTextField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField2.setEnabled(false);
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel3.setText("Enquadramento:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Micro empresa - ME", "Empresa de pequeno porte - EPP", "Empresa normal" }));
        jComboBox1.setEnabled(false);
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jLabel4.setText("Tipo tributário:");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Simples nacional", "Lucro presumido", "Lucro real" }));
        jComboBox2.setEnabled(false);
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jTextField9.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField9.setEnabled(false);
        jTextField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField9ActionPerformed(evt);
            }
        });
        jTextField9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField9KeyPressed(evt);
            }
        });

        jTextField3.setBackground(new java.awt.Color(255, 255, 204));
        jTextField3.setEnabled(false);

        jLabel5.setText("Pais:");

        jTextField10.setBackground(new java.awt.Color(255, 255, 204));
        jTextField10.setEnabled(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Nota fiscal eletrônica - NF-e", 2, 0, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(255, 0, 0))); // NOI18N

        buttonGroup3.add(jRadioButton1);
        jRadioButton1.setText("Homologação");
        jRadioButton1.setEnabled(false);
        jRadioButton1.setOpaque(false);
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        buttonGroup3.add(jRadioButton2);
        jRadioButton2.setText("Produção");
        jRadioButton2.setEnabled(false);
        jRadioButton2.setOpaque(false);
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton2)
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2))
                .addGap(0, 5, Short.MAX_VALUE))
        );

        jTextField11.setEnabled(false);

        jLabel6.setText("Complemento:");

        jTextField12.setEnabled(false);

        jLabel7.setText("Tel:");

        jTextField13.setEnabled(false);

        jLabel8.setText("E-mail:");

        jTextField14.setEnabled(false);

        jLabel13.setText("CRT :");

        jTextField21.setEnabled(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(18, 18, 18)
                        .addComponent(jDateChooser_data_cadastro1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel31)
                                    .addGap(53, 53, 53))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel33)
                                    .addGap(35, 35, 35)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(67, 67, 67)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(48, 48, 48)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel22)
                                .addGap(4, 4, 4)
                                .addComponent(jTextField_razao_social1, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel28)
                                .addGap(10, 10, 10)
                                .addComponent(jTextField_nome_fantazia1, javax.swing.GroupLayout.PREFERRED_SIZE, 524, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel_cnpj1)
                                .addGap(56, 56, 56)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(44, 44, 44)
                                .addComponent(jLabel_ie1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(13, 13, 13)
                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel1)
                                .addGap(6, 6, 6)
                                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel29)
                                .addGap(62, 62, 62)
                                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel30)
                                .addGap(4, 4, 4)
                                .addComponent(jTextField_endereco, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(jLabel15)
                                .addGap(4, 4, 4)
                                .addComponent(jTextField_numero_endereco1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(4, 4, 4)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(334, 334, 334)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addGap(20, 20, 20))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabel3)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel4))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_razao_social1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(jLabel22))))
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel28))
                    .addComponent(jTextField_nome_fantazia1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel_cnpj1)
                            .addComponent(jLabel_ie1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))))
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_endereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_numero_endereco1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29)
                            .addComponent(jLabel15))))
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)
                        .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel31)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel33))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)
                        .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5))
                    .addComponent(jDateChooser_data_cadastro1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(134, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Configurar Empresa", jPanel3);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jTabbedPane4.setForeground(new java.awt.Color(204, 0, 0));
        jTabbedPane4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mercado, Supermercado, Hipermercado", "Farmacias, Drogarias, Perfumaria", "Autopeças e Oficinas", "Atacadista", "Bares, Restaurantes, Lanchonete", "Varejista em geral" }));
        jComboBox4.setEnabled(false);
        jComboBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox4ItemStateChanged(evt);
            }
        });

        jLabel34.setText("Atividade comercial:");

        jCheckBox2.setSelected(true);
        jCheckBox2.setText("Controle de Estoque e Administração");
        jCheckBox2.setEnabled(false);
        jCheckBox2.setOpaque(false);

        jCheckBox3.setSelected(true);
        jCheckBox3.setText("PDV - NFC-e ");
        jCheckBox3.setEnabled(false);
        jCheckBox3.setOpaque(false);

        jCheckBox4.setSelected(true);
        jCheckBox4.setText("Terminal de Venda");
        jCheckBox4.setEnabled(false);
        jCheckBox4.setOpaque(false);

        jCheckBox5.setSelected(true);
        jCheckBox5.setText("Contas a Pagar");
        jCheckBox5.setEnabled(false);
        jCheckBox5.setOpaque(false);

        jCheckBox6.setSelected(true);
        jCheckBox6.setText("Contas a Receber");
        jCheckBox6.setEnabled(false);
        jCheckBox6.setOpaque(false);

        jCheckBox7.setSelected(true);
        jCheckBox7.setText("PDV - Não Fiscal");
        jCheckBox7.setEnabled(false);
        jCheckBox7.setOpaque(false);

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("Funções");

        jCheckBox8.setSelected(true);
        jCheckBox8.setText("SNGPC - Sistema Nacional de Gerenciamento de Produtos Controlados");
        jCheckBox8.setEnabled(false);
        jCheckBox8.setOpaque(false);

        jCheckBox9.setSelected(true);
        jCheckBox9.setText("SINTEGRA");
        jCheckBox9.setEnabled(false);
        jCheckBox9.setOpaque(false);

        jCheckBox10.setSelected(true);
        jCheckBox10.setText("SPED Fiscal");
        jCheckBox10.setEnabled(false);
        jCheckBox10.setOpaque(false);

        jCheckBox11.setSelected(true);
        jCheckBox11.setText("ECF - Escrituração Contábil Fiscal");
        jCheckBox11.setEnabled(false);
        jCheckBox11.setOpaque(false);

        jCheckBox12.setSelected(true);
        jCheckBox12.setText("Bloco K");
        jCheckBox12.setEnabled(false);
        jCheckBox12.setOpaque(false);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jCheckBox15.setText("Venda de Gás GLP");
        jCheckBox15.setEnabled(false);
        jCheckBox15.setOpaque(false);

        jCheckBox16.setText("Prestador de serviço");
        jCheckBox16.setEnabled(false);
        jCheckBox16.setOpaque(false);
        jCheckBox16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCheckBox16MouseClicked(evt);
            }
        });

        jPanel29.setBackground(new java.awt.Color(255, 255, 255));
        jPanel29.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Controlar estoque com pedido NÃO FATURADO", 2, 0, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jRadioButton9.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup4.add(jRadioButton9);
        jRadioButton9.setText("SIM");
        jRadioButton9.setEnabled(false);

        jRadioButton10.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup4.add(jRadioButton10);
        jRadioButton10.setText("NÃO");
        jRadioButton10.setEnabled(false);

        jLabel41.setForeground(new java.awt.Color(153, 0, 0));
        jLabel41.setText("<html>Controla estoque com pedido não FATURADO<br> se existir serviço no pedido</html>");

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jRadioButton10, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                            .addComponent(jRadioButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton10)
                .addContainerGap())
        );

        jPanel30.setBackground(new java.awt.Color(255, 255, 255));
        jPanel30.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Modelos de comissionamento", 2, 0, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jRadioButton11.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup5.add(jRadioButton11);
        jRadioButton11.setEnabled(false);

        jRadioButton12.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup5.add(jRadioButton12);
        jRadioButton12.setEnabled(false);

        jLabel75.setText("Comissão única aplicada a todos os");
        jLabel75.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel75.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jLabel75.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel75MouseClicked(evt);
            }
        });

        jLabel76.setText("PRODUTOS");
        jLabel76.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel76.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jLabel76.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel76MouseClicked(evt);
            }
        });

        jLabel77.setText("Comissão variável, aplicada por");
        jLabel77.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel77MouseClicked(evt);
            }
        });

        jLabel78.setText("GRUPOS DE PRODUTOS");
        jLabel78.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel78.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jLabel78.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel78MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addComponent(jRadioButton12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel77, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel78, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addComponent(jRadioButton11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                                .addComponent(jLabel76, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                            .addComponent(jLabel75, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addComponent(jLabel75, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel76, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jRadioButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addComponent(jLabel77)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel78, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jRadioButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jCheckBox20.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox20.setText("Calculadora de comissão");
        jCheckBox20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCheckBox20MouseClicked(evt);
            }
        });

        jCheckBox21.setText("Aplicar Custo médio na entrada");
        jCheckBox21.setEnabled(false);
        jCheckBox21.setOpaque(false);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBox8)
                                    .addComponent(jCheckBox9)
                                    .addComponent(jCheckBox10)
                                    .addComponent(jCheckBox11)
                                    .addComponent(jCheckBox12))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBox20, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 555, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel11Layout.createSequentialGroup()
                                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jCheckBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jCheckBox2)
                                        .addComponent(jCheckBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jCheckBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jCheckBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jCheckBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(71, 71, 71)
                                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jCheckBox16, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel11Layout.createSequentialGroup()
                                            .addComponent(jCheckBox15)
                                            .addGap(18, 18, 18)
                                            .addComponent(jCheckBox21, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(119, 119, 119))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jCheckBox2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox6))
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBox15)
                            .addComponent(jCheckBox21))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel35)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jCheckBox8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox12))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jCheckBox20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jTabbedPane4.addTab("Confiança Sistemas Comercial", jPanel11);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane4)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane4)
                .addContainerGap())
        );

        jTabbedPane4.getAccessibleContext().setAccessibleName("");

        jTabbedPane1.addTab("Software", jPanel8);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel16.setText("Série NF-e:");

        jTextField22.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField22FocusLost(evt);
            }
        });

        jLabel18.setText("Série NFC-e:");

        jTextField23.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField23FocusLost(evt);
            }
        });
        jTextField23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField23ActionPerformed(evt);
            }
        });

        jLabel19.setText("CSC NFC-e (65) :");

        jTextField24.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField24FocusLost(evt);
            }
        });

        jLabel24.setText("Número NF-e:");

        jLabel25.setText("Número NFC-e:");

        jTextField28.setEnabled(false);
        jTextField28.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField28FocusLost(evt);
            }
        });
        jTextField28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField28ActionPerformed(evt);
            }
        });

        jTextField29.setEnabled(false);
        jTextField29.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField29FocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addComponent(jLabel16)
                    .addComponent(jLabel18))
                .addGap(41, 41, 41)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField24)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextField23, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField22, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextField28, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField29, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 163, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextField22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField23, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextField29, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField28, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Certificado Digital - Homologação", jPanel5);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel20.setText("Série NF-e:");

        jLabel21.setText("Série NFC-e:");

        jLabel23.setText("CSC NFC-e (65) :");

        jLabel73.setText("Número NF-e:");

        jTextField30.setEnabled(false);
        jTextField30.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField30FocusLost(evt);
            }
        });

        jTextField31.setEnabled(false);
        jTextField31.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField31FocusLost(evt);
            }
        });
        jTextField31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField31ActionPerformed(evt);
            }
        });

        jLabel74.setText("Número NFC-e:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21))
                .addGap(41, 41, 41)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField27)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel74)
                            .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextField31, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField30, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 177, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextField30, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel74, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jTextField27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Certificado Digital - Produção", jPanel6);

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "   Configurar E-MAIL   ", 2, 0));

        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel44.setText("Provedor:");

        jComboBox10.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Gmail", "Hotmail", "Outros" }));
        jComboBox10.setEnabled(false);
        jComboBox10.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox10ItemStateChanged(evt);
            }
        });

        jLabel45.setText("Host SMTP:");

        jTextField35.setEnabled(false);

        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel46.setText("Porta:");

        jTextField36.setEnabled(false);

        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel47.setText("E-mail:");

        jTextField37.setEnabled(false);

        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel48.setText("Senha:");

        jPasswordField1.setEnabled(false);

        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel49.setText("Confirmar senha:");

        jPasswordField2.setEnabled(false);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel49, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(17, 17, 17)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel45)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField35))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jTextField36, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jTextField37)
                    .addComponent(jPasswordField1)
                    .addComponent(jPasswordField2))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel45)
                    .addComponent(jTextField35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel46)
                    .addComponent(jTextField36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel47)
                    .addComponent(jTextField37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel49)
                    .addComponent(jPasswordField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Certificado Digital / Config. E-mail", jPanel4);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));

        jCheckBox17.setText("Aplicar desconto maximo a grupo de produtos");
        jCheckBox17.setEnabled(false);
        jCheckBox17.setOpaque(false);
        jCheckBox17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox17ActionPerformed(evt);
            }
        });

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));
        jPanel26.setBorder(javax.swing.BorderFactory.createBevelBorder(0, null, new java.awt.Color(204, 204, 204), null, null));

        jTable7.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTable7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable7.setEnabled(false);
        jTable7.setRowHeight(20);
        jTable7.setSelectionBackground(new java.awt.Color(102, 153, 255));
        jScrollPane8.setViewportView(jTable7);

        jComboBox12.setMaximumRowCount(10);
        jComboBox12.setEnabled(false);

        jButton9.setText("Confirmar");
        jButton9.setEnabled(false);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jMoneyField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jMoneyField1.setEnabled(false);

        jLabel26.setText("Grupo:");

        jLabel27.setText("Desconto:");

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox12, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jMoneyField1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel26Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jMoneyField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27)
                    .addComponent(jButton9))
                .addContainerGap(275, Short.MAX_VALUE))
            .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel26Layout.createSequentialGroup()
                    .addGap(68, 68, 68)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(jCheckBox17)
                        .addGap(0, 330, Short.MAX_VALUE))
                    .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane6.addTab("Desconto por grupo de produtos", jPanel27);

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));

        jLabel68.setText("Desconto máximo (%):");

        jMoneyField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jMoneyField2.setEnabled(false);
        jMoneyField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jMoneyField2KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jMoneyField2KeyReleased(evt);
            }
        });

        jButton10.setText("Confirmar");
        jButton10.setEnabled(false);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jLabel69.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(255, 0, 0));
        jLabel69.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel69.setText("Informe um valor referente ao desconto máximo possível para ser aplicado em uma venda.");

        jLabel70.setText("Obs.: Essa configuração de desconto só se aplica a produtos sem configuração de desconto. Caso o produto faça parte");

        jLabel71.setText("de uma promoção ou de um grupo que já tenha uma configuração de desconto esta configuração não funcionará.");

        jCheckBox18.setText("Configurar desconto máximo");
        jCheckBox18.setEnabled(false);
        jCheckBox18.setOpaque(false);
        jCheckBox18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox18ActionPerformed(evt);
            }
        });

        jLabel72.setForeground(new java.awt.Color(0, 153, 0));
        jLabel72.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel72.setText("Desconto configurado com sucesso!!");

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel70)
                    .addComponent(jLabel71))
                .addContainerGap())
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox18)
                    .addComponent(jLabel69, javax.swing.GroupLayout.PREFERRED_SIZE, 574, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel68)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel72)
                            .addGroup(jPanel28Layout.createSequentialGroup()
                                .addComponent(jMoneyField2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton10)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox18)
                .addGap(88, 88, 88)
                .addComponent(jLabel69, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel68)
                    .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jMoneyField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton10)))
                .addGap(0, 0, 0)
                .addComponent(jLabel72)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 111, Short.MAX_VALUE)
                .addComponent(jLabel70)
                .addGap(0, 0, 0)
                .addComponent(jLabel71)
                .addGap(46, 46, 46))
        );

        jTabbedPane6.addTab("Configurar desconto máximo", jPanel28);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane6)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Configuração Geral", jPanel25);

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel43.setText("Forma de pagamento que influencia na movimentação de dinheiro a vista:");

        jComboBox9.setMaximumRowCount(10);
        jComboBox9.setEnabled(false);

        org.jdesktop.swingbinding.JComboBoxBinding jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, tipoPagamentoList, jComboBox9);
        bindingGroup.addBinding(jComboBoxBinding);

        jTable5.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable5.setComponentPopupMenu(Remove1);
        jTable5.setRowHeight(20);
        jTable5.setSelectionBackground(new java.awt.Color(102, 153, 255));
        jScrollPane6.setViewportView(jTable5);

        jButton5.setText("Confirmar");
        jButton5.setEnabled(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel43)
                            .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("A vista / Movimnto de caixa", jPanel16);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        jLabel32.setText("Forma de pagamento que utilizarar Boleto de cobrança:");

        jComboBox3.setMaximumRowCount(10);
        jComboBox3.setEnabled(false);

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable1.setComponentPopupMenu(Remove2);
        jTable1.setRowHeight(20);
        jTable1.setSelectionBackground(new java.awt.Color(102, 153, 255));
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Confirmar");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Boleto Bancário", 2, 0, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(255, 0, 0))); // NOI18N

        jLabel9.setText("Cod.:");

        jTextField15.setBackground(new java.awt.Color(255, 255, 204));
        jTextField15.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField15.setEnabled(false);
        jTextField15.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField15KeyPressed(evt);
            }
        });

        jLabel10.setText("Conta bancária DV:");

        jTextField16.setBackground(new java.awt.Color(255, 255, 204));
        jTextField16.setEnabled(false);

        jTextField17.setBackground(new java.awt.Color(255, 255, 204));
        jTextField17.setEnabled(false);

        jLabel11.setText("Agencia DV:");

        jTextField18.setBackground(new java.awt.Color(255, 255, 204));
        jTextField18.setEnabled(false);

        jTextField19.setBackground(new java.awt.Color(255, 255, 204));
        jTextField19.setEnabled(false);

        jLabel12.setText("Banco:");

        jTextField20.setBackground(new java.awt.Color(255, 255, 204));
        jTextField20.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(159, Short.MAX_VALUE)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField20))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12)
                        .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel11))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        jCheckBox1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheckBox1.setText("Gerar Boleto Bancário");
        jCheckBox1.setOpaque(false);
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 593, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jCheckBox1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Boleto de Cobrança", jPanel10);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        jLabel40.setText("Forma de pagamento que utilizarar Promissória de cobrança:");

        jComboBox7.setMaximumRowCount(10);
        jComboBox7.setEnabled(false);

        jTable4.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable4.setComponentPopupMenu(jPopupMenu_remove_promossoria);
        jTable4.setRowHeight(20);
        jTable4.setSelectionBackground(new java.awt.Color(102, 153, 255));
        jScrollPane5.setViewportView(jTable4);

        jButton4.setText("Confirmar");
        jButton4.setEnabled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jCheckBox13.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheckBox13.setText("Gerar Promissória");
        jCheckBox13.setOpaque(false);
        jCheckBox13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox13ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 593, Short.MAX_VALUE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jCheckBox13)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox7, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jCheckBox13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Promissória", jPanel13);

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel37.setText("Selecione a incidência:");

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Taxa fixa por operação", "Taxa variável por quantidade de venda", "Taxa variável valor de venda" }));

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 0, 51));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("Valores incidentes em operações com cartão");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Pagamento", "Incidência", "Descrição", "Valor", "tipoPagamento", "codigo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setComponentPopupMenu(jPopupMenu2);
        jTable2.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setMinWidth(100);
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(100);
            jTable2.getColumnModel().getColumn(0).setMaxWidth(100);
            jTable2.getColumnModel().getColumn(1).setMinWidth(200);
            jTable2.getColumnModel().getColumn(1).setPreferredWidth(200);
            jTable2.getColumnModel().getColumn(1).setMaxWidth(200);
            jTable2.getColumnModel().getColumn(2).setMinWidth(230);
            jTable2.getColumnModel().getColumn(2).setPreferredWidth(230);
            jTable2.getColumnModel().getColumn(2).setMaxWidth(230);
            jTable2.getColumnModel().getColumn(3).setMinWidth(100);
            jTable2.getColumnModel().getColumn(3).setPreferredWidth(100);
            jTable2.getColumnModel().getColumn(3).setMaxWidth(100);
            jTable2.getColumnModel().getColumn(4).setMinWidth(0);
            jTable2.getColumnModel().getColumn(4).setPreferredWidth(0);
            jTable2.getColumnModel().getColumn(4).setMaxWidth(0);
            jTable2.getColumnModel().getColumn(5).setMinWidth(0);
            jTable2.getColumnModel().getColumn(5).setPreferredWidth(0);
            jTable2.getColumnModel().getColumn(5).setMaxWidth(0);
        }

        jButton2.setText("Confirmar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel38.setText("Descrição:");

        buttonGroup2.add(jRadioButton8);
        jRadioButton8.setText("Maquina (PinPad)");
        jRadioButton8.setOpaque(false);
        jRadioButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton8ActionPerformed(evt);
            }
        });

        jLabel39.setText("Valor (%):");

        buttonGroup2.add(jRadioButton7);
        jRadioButton7.setText("TEF - Transferência Eletrônica de Fundos");
        jRadioButton7.setOpaque(false);
        jRadioButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton7ActionPerformed(evt);
            }
        });

        jCheckBox19.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox19.setText("A prazo");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel37)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField32, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel38))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel39)
                            .addComponent(jTextField33, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jRadioButton7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButton8))
                            .addComponent(jCheckBox19, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton7)
                    .addComponent(jRadioButton8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel37)
                        .addGap(0, 0, 0)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2)))
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel14Layout.createSequentialGroup()
                            .addComponent(jLabel39)
                            .addGap(0, 0, 0)
                            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jLabel38)))
                .addGap(4, 4, 4)
                .addComponent(jCheckBox19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable3.setComponentPopupMenu(jPopupMenu1);
        jTable3.getTableHeader().setReorderingAllowed(false);
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);

        jComboBox6.setEnabled(false);

        jButton3.setText("Confirmar");
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jScrollPane4.setBackground(new java.awt.Color(255, 255, 255));

        jTextPane1.setBorder(null);
        jTextPane1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTextPane1.setText("Selecione as forma de pagamento para utilizar as função cartão");
        jTextPane1.setDisabledTextColor(new java.awt.Color(255, 0, 51));
        jTextPane1.setEnabled(false);
        jScrollPane4.setViewportView(jTextPane1);

        jLabel42.setText("Tipo:");

        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "Dia Fixo", "Intervalo de dias" }));
        jComboBox8.setEnabled(false);
        jComboBox8.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox8ItemStateChanged(evt);
            }
        });

        jTextField34.setEnabled(false);

        jCheckBox14.setText("Recebimento das operações parcelada, dividido na mesma quantidade de vezes que o cliente pacelar");
        jCheckBox14.setEnabled(false);
        jCheckBox14.setOpaque(false);
        jCheckBox14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox14ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel12Layout.createSequentialGroup()
                                .addComponent(jLabel42)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField34, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jComboBox6, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel12Layout.createSequentialGroup()
                        .addComponent(jCheckBox14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel42)
                            .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                        .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(1, 1, 1)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox14)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jTabbedPane3.addTab("Operações com cartões", jPanel12);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 618, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane3)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Financeiro", jPanel9);

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));

        jLabel50.setText("Nome do Contador:");

        jTextField38.setEnabled(false);

        jLabel51.setText("CPF:");

        jTextField39.setEnabled(false);

        jLabel52.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(255, 0, 0));
        jLabel52.setText("Campo obrigatório");

        jLabel53.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(255, 0, 0));
        jLabel53.setText("Campo obrigatório");

        jLabel54.setText("CNPJ:");

        jTextField40.setEnabled(false);

        jLabel56.setText("Inscrição do CRC:");

        jTextField41.setEnabled(false);

        jLabel57.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(255, 0, 0));
        jLabel57.setText("Campo obrigatório");

        jLabel55.setText("CEP:");

        jTextField42.setEnabled(false);
        jTextField42.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField42KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField42KeyReleased(evt);
            }
        });

        jLabel58.setText("Logradouro:");

        jTextField43.setEnabled(false);

        jLabel59.setText("Número:");

        jTextField44.setEnabled(false);

        jTextField45.setEnabled(false);

        jLabel60.setText("Complemento:");

        jLabel61.setText("Bairro:");

        jTextField46.setEnabled(false);

        jLabel62.setText("Telefône:");

        jTextField47.setEnabled(false);

        jLabel63.setText("Fax:");

        jTextField48.setEnabled(false);

        jLabel64.setText("E-mail:");

        jTextField49.setEnabled(false);

        jLabel65.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(255, 0, 0));
        jLabel65.setText("Campo obrigatório");

        jLabel66.setText("Cidade:");

        jComboBox11.setEnabled(false);

        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cidadeCodigoList, jComboBox11);
        bindingGroup.addBinding(jComboBoxBinding);

        jLabel67.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(255, 0, 0));
        jLabel67.setText("Campo obrigatório");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel66)
                    .addComponent(jLabel64)
                    .addComponent(jLabel50)
                    .addComponent(jLabel51)
                    .addComponent(jLabel55)
                    .addComponent(jLabel58)
                    .addComponent(jLabel59)
                    .addComponent(jLabel61)
                    .addComponent(jLabel62))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBox11, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel18Layout.createSequentialGroup()
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextField42, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel53, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                                    .addComponent(jTextField39, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField44, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel18Layout.createSequentialGroup()
                                        .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField40, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel56)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextField41, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel57)))
                                    .addGroup(jPanel18Layout.createSequentialGroup()
                                        .addComponent(jLabel60)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField45))))
                            .addComponent(jTextField43)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel18Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextField46, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel18Layout.createSequentialGroup()
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jTextField38, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel18Layout.createSequentialGroup()
                                        .addComponent(jTextField47, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel63)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField48, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jTextField49))
                        .addGap(21, 21, 21))))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel50)
                    .addComponent(jTextField38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jLabel52)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel51)
                            .addComponent(jTextField39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addComponent(jLabel53))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel54)
                            .addComponent(jLabel56)
                            .addComponent(jTextField41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addComponent(jLabel57)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(jTextField42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel58)
                    .addComponent(jTextField43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel59)
                    .addComponent(jTextField44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel60)
                    .addComponent(jTextField45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel61)
                    .addComponent(jTextField46, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel62)
                    .addComponent(jTextField47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel63)
                    .addComponent(jTextField48, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel64)
                    .addComponent(jTextField49, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jLabel65)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel66)
                    .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jLabel67)
                .addContainerGap(162, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Contador", jPanel18);

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));

        jButton6.setText("Inserir Impostos");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jScrollPane7.setBackground(new java.awt.Color(255, 255, 255));

        jTable6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane7.setViewportView(jTable6);

        jButton8.setText("Editar Impostos");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton11.setText("Remover Impostos");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6)
                    .addComponent(jButton8)
                    .addComponent(jButton11))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Composição de preço", jPanel19);

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));

        jPanel24.setBackground(new java.awt.Color(255, 255, 255));
        jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Tipo de papel", 2, 0, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        codigoProdutoDANFE.add(jRadioButton5);
        jRadioButton5.setText("Bobina 88 mm");
        jRadioButton5.setOpaque(false);

        codigoProdutoDANFE.add(jRadioButton6);
        jRadioButton6.setText("A4");
        jRadioButton6.setOpaque(false);

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton6)
                    .addComponent(jRadioButton5))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jRadioButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton5)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(439, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(292, Short.MAX_VALUE))
        );

        jTabbedPane5.addTab("DANFE NFC-e - MOD 65", jPanel21);

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));
        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Código do Produto no DANFE", 2, 0, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        codigoProdutoDANFE.add(jRadioButton4);
        jRadioButton4.setText("Código de barras / Ref. do produto");
        jRadioButton4.setOpaque(false);

        codigoProdutoDANFE.add(jRadioButton3);
        jRadioButton3.setText("Código do produto");
        jRadioButton3.setOpaque(false);

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton3)
                    .addComponent(jRadioButton4))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jRadioButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton4)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(335, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(295, Short.MAX_VALUE))
        );

        jTabbedPane5.addTab("DANFE NF-e - MOD 55", jPanel22);

        jButton7.setText("Configurar DANFE");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane5)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Configuração do DANFE", jPanel20);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 645, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField_nome_fantazia1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_nome_fantazia1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_nome_fantazia1ActionPerformed

    private void jTextField_numero_endereco1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_numero_endereco1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_numero_endereco1ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed

        if (cancelar == 0) {
            abreCampos();
            refreshButton.setText("Cancelar");
            cancelar = 1;
            saveButton.setEnabled(true);

        } else {
            try {
                FecharCamposDescontoMaximo();
                FecharCamposDescontoMaximoGrupo();
                saveButton.setEnabled(false);
                refreshButton.setEnabled(true);
                refreshButton.setText("Editar");
                cancelar = 0;
                if(!bd.Bd.conection.connectionA.currentEntityManager().isOpen()||
                        !bd.Bd.conection.connectionA.currentEntityManager().getTransaction().isActive()){
                    bd.Bd.conection.connectionA.currentEntityManager().getTransaction().begin();
                }
                java.util.Collection data = query.getResultList();
                for (Object entity : data) {
                    bd.Bd.conection.connectionA.currentEntityManager().refresh(entity);
                }
                list.clear();
                list.addAll(data);
            } catch (ConnectionClosedException ex) {
                Logger.getLogger(JIFEmpresa.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                bd.Bd.conection.connectionA.closeEntityManager();
            }
        }

    }//GEN-LAST:event_refreshButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        Salvar();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField_enderecoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_enderecoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_enderecoActionPerformed

    private void jTextField_razao_social1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_razao_social1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_razao_social1ActionPerformed

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9ActionPerformed

    private void jTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldActionPerformed

    private void jTextField9KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField9KeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            consulta(Integer.parseInt(jTextField9.getText()));

        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {
            JD_cliente cidade = new JD_cliente(null, true);
            cidade.setVisible(true);
            cidade.toFront();

            setVisible(true);
        }

    }//GEN-LAST:event_jTextField9KeyPressed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed

        if (jRadioButton1.isSelected()) {
            list.get(0).setTpEmissao(0);
        } else if (jRadioButton2.isSelected()) {
            list.get(0).setTpEmissao(1);
        }
        homologacaoProducao();

    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        if (jRadioButton1.isSelected()) {
            list.get(0).setTpEmissao(0);
        } else if (jRadioButton2.isSelected()) {
            list.get(0).setTpEmissao(1);
        }
        homologacaoProducao();

    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed

        if (jCheckBox1.isSelected()) {
            try{
            jComboBox3.setEnabled(true);
            jTable1.setEnabled(true);
            jButton1.setEnabled(true);
            list.get(0).setEmissaoBoleto(1);
            bd.Bd.conection.connectionA.currentEntityManager().persist(list.get(0));
            }catch(Exception ex){
            }finally{
                bd.Bd.conection.connectionA.closeEntityManager();
            }
        } else {
            jComboBox3.setEnabled(false);
            jTable1.setEnabled(false);
            jButton1.setEnabled(false);
            list.get(0).setEmissaoBoleto(0);
        }

    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jTextField15KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField15KeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            //buscaBanco(Integer.parseInt(jTextField15.getText()));
        } else if (evt.getKeyCode() == KeyEvent.VK_F2) {

            JD_conta_bancaria contaBancaria = new JD_conta_bancaria(null, true);
            contaBancaria.setVisible(true);

        }

    }//GEN-LAST:event_jTextField15KeyPressed

    private void jTextField24FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField24FocusLost
        pardigital.setCsc(jTextField24.getText());
    }//GEN-LAST:event_jTextField24FocusLost

    private void jTextField23FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField23FocusLost
        pardigital.setSerie65(jTextField23.getText());        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField23FocusLost

    private void jTextField22FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField22FocusLost
        pardigital.setSerie55(jTextField22.getText());
    }//GEN-LAST:event_jTextField22FocusLost

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (!contem()) {
            try {
                modelTableFormaPagamento.setNumRows(jTable1.getRowCount() + 1);
                jTable1.setModel(modelTableFormaPagamento);
                jTable1.setValueAt(jComboBox3.getSelectedItem(), jTable1.getRowCount() - 1, 0);
                list.get(0).getTipoPagamentoList().add(tipoPagamentoList.get(jComboBox3.getSelectedIndex()));
                bd.Bd.conection.connectionA.currentEntityManager().persist(list.get(0));
                if (!bd.Bd.conection.connectionA.currentEntityManager().isOpen() || !bd.Bd.conection.connectionA.currentEntityManager().getTransaction().isActive()) {
                    bd.Bd.conection.connectionA.currentEntityManager().getTransaction().begin();
                }
                bd.Bd.conection.connectionA.currentEntityManager().getTransaction().commit();
            } catch (ConnectionClosedException ex) {
                Logger.getLogger(JIFEmpresa.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                bd.Bd.conection.connectionA.closeEntityManager();
            }
        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        if (!contem3()) {
            try {
                if (VerificaCampos()) {
                    TipoPagamentoCartaoPK tipoPagamentoCartaoPK = new TipoPagamentoCartaoPK(
                            tipoPagamentoList.get(jComboBox6.getSelectedIndex()).getCodigoTipoPagamento(),
                            list.get(0).getCodigoEmpresa()
                    );
                    TipoPagamentoCartao tipoPagamentoCartao = new TipoPagamentoCartao();

                    if (jComboBox8.getSelectedItem().toString().equals("Dia Fixo")) {
                        if (jTextField34.getText().equals("0")) {
                            jDialog1.setSize(293, 227);
                            jDialog1.setLocationRelativeTo(null);
                            jDialog1.setVisible(true);
                        }
                        if (!jTextField34.getText().equals("0")) {
                            tipoPagamentoCartao.setTipoPagamentoCartaoPK(tipoPagamentoCartaoPK);
                            tipoPagamentoCartao.setTipo(jComboBox8.getSelectedItem().toString());
                            tipoPagamentoCartao.setDiaFixo(Integer.parseInt(jTextField34.getText()));
                            tipoPagamentoCartao.setQtdDia(0);

                        } else {
                            JOptionPane.showMessageDialog(rootPane, "Dê um duplo click no dia em que receberar os valores referênte as vendas realizadas via cartão.");
                            jDialog1.setSize(293, 227);
                            jDialog1.setLocationRelativeTo(null);
                            jDialog1.setVisible(true);
                        }
                    } else if (jComboBox8.getSelectedItem().toString().equals("Intervalo de dias")) {
                        try {
                            try {
                                if (Integer.parseInt(jTextField34.getText()) >= 1) {
                                    tipoPagamentoCartao.setDiaFixo(0);
                                    tipoPagamentoCartao.setTipoPagamentoCartaoPK(tipoPagamentoCartaoPK);
                                    tipoPagamentoCartao.setTipo(jComboBox8.getSelectedItem().toString());
                                    tipoPagamentoCartao.setDiaFixo(Integer.parseInt(jTextField34.getText()));
                                    tipoPagamentoCartao.setQtdDia(Integer.parseInt(jTextField34.getText()));
                                } else {
                                    JOptionPane.showMessageDialog(rootPane, "Informe o intervalo de dias após a venda que receberar os valores referênte as vendas realizadas via cartão.");
                                    jTextField34.requestFocus();
                                }
                            } catch (NumberFormatException nex) {
                                JOptionPane.showMessageDialog(rootPane, "Informe o intervalo de dias após a venda que receberar os valores referênte as vendas realizadas via cartão.");
                                jTextField34.requestFocus();
                            }
                        } catch (Exception e) {
                        }

                        tipoPagamentoCartao.setQtdDia(Integer.parseInt(jTextField34.getText()));
                    }
                    if (jCheckBox14.isSelected()) {
                        tipoPagamentoCartao.setRecebimentoPacelado(0);
                    } else {
                        tipoPagamentoCartao.setRecebimentoPacelado(1);
                    }
                    bd.Bd.conection.connectionA.currentEntityManager().persist(tipoPagamentoCartao);
                    if (!bd.Bd.conection.connectionA.currentEntityManager().isOpen() || !bd.Bd.conection.connectionA.currentEntityManager().getTransaction().isActive()) {
                        bd.Bd.conection.connectionA.currentEntityManager().getTransaction().begin();
                    }
                    bd.Bd.conection.connectionA.currentEntityManager().getTransaction().commit();

                    modelTableFormaPagamento3.setNumRows(jTable3.getRowCount() + 1);
                    jTable3.setModel(modelTableFormaPagamento3);

                    ///jTable3.setValueAt(jTextField.getText(), jTable3.getRowCount() - 1, 2);
                    jTable3.setValueAt(jComboBox6.getSelectedItem(), jTable3.getRowCount() - 1, 0);
                    jTable3.setValueAt(jComboBox8.getSelectedItem(), jTable3.getRowCount() - 1, 1);
                    jTable3.setValueAt(jTextField34.getText(), jTable3.getRowCount() - 1, 2);
                    jTable3.setValueAt(
                        jCheckBox14.isSelected() ? "Parcelado" : "A vista",
                            jTable3.getRowCount() - 1, 3
                    );
                    
                }
            } catch (NullPointerException e) {
            } catch (Exception ex){
            } finally {
                bd.Bd.conection.connectionA.closeEntityManager();
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Esta forma de pagamento já está sendo utilizada,\nEscolha outra.", "Erro forma de pagamento", 0);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        if (!contem2()) {
            try {
                modelTableFormaPagamento2.setNumRows(jTable4.getRowCount() + 1);
                jTable4.setModel(modelTableFormaPagamento2);
                jTable4.setValueAt(jComboBox7.getSelectedItem(), jTable4.getRowCount() - 1, 0);
                list.get(0).getTipoPagamentoPromissoriaList().add(new TipoPagamentoPromissoria(list.get(0), tipoPagamentoList.get(jComboBox7.getSelectedIndex())));
                bd.Bd.conection.connectionA.currentEntityManager().persist(list.get(0));
                
                if(!bd.Bd.conection.connectionA.currentEntityManager().isOpen() || !bd.Bd.conection.connectionA.currentEntityManager().getTransaction().isActive()){
                    bd.Bd.conection.connectionA.currentEntityManager().getTransaction().begin();
                }
                bd.Bd.conection.connectionA.currentEntityManager().getTransaction().commit();
            } catch (ConnectionClosedException ex) {
                Logger.getLogger(JIFEmpresa.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                bd.Bd.conection.connectionA.closeEntityManager();
            }
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jCheckBox13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox13ActionPerformed

        if (jCheckBox13.isSelected()) {
            try{
            jComboBox7.setEnabled(true);
            jTable4.setEnabled(true);
            jButton4.setEnabled(true);
            list.get(0).setEmissaoPromissoria(1);
            bd.Bd.conection.connectionA.currentEntityManager().persist(list.get(0));
            }catch(Exception ex){
            }finally{
                bd.Bd.conection.connectionA.closeEntityManager();
            }
        } else {
            jComboBox7.setEnabled(false);
            jTable4.setEnabled(false);
            jButton4.setEnabled(false);
            list.get(0).setEmissaoPromissoria(0);
        }

    }//GEN-LAST:event_jCheckBox13ActionPerformed

    private void jComboBox8ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox8ItemStateChanged
        if (evt.getItem().equals("Dia Fixo")) {
            if (jComboBox8.getSelectedItem().toString().equals("Dia Fixo")) {
                jTextField34.setText("0");
                jTextField34.setEnabled(false);
                jDialog1.setSize(293, 227);
                jDialog1.setLocationRelativeTo(null);
                jDialog1.setVisible(true);
            }
        } else if (evt.getItem().equals("Intervalo de dias")) {
            jTextField34.setEnabled(true);
            jTextField34.requestFocus();
            //jLabel41.setText("0");
        }
    }//GEN-LAST:event_jComboBox8ItemStateChanged

    private void jDayChooser1AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jDayChooser1AncestorAdded

    }//GEN-LAST:event_jDayChooser1AncestorAdded

    private void jDayChooser1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jDayChooser1MouseClicked
        if (evt.getClickCount() == 2) {
            jTextField34.setText(String.valueOf(jDayChooser1.getDay()));
            jDialog1.dispose();
        }
    }//GEN-LAST:event_jDayChooser1MouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (!contem()) {
            try{
            modelTableFormaPagamentoAvista.setNumRows(jTable5.getRowCount() + 1);
            jTable5.setModel(modelTableFormaPagamentoAvista);
            jTable5.setValueAt(jComboBox9.getSelectedItem(), jTable5.getRowCount() - 1, 0);
            
            list.get(0).getTipoPagamentoAvistaList().add(
                tipoPagamentoList.get(jComboBox9.getSelectedIndex())
            );
            bd.Bd.conection.connectionA.currentEntityManager().persist(
                list.get(0).getTipoPagamentoAvistaList().get(jComboBox9.getSelectedIndex())
            );
            
            if(!bd.Bd.conection.connectionA.currentEntityManager().isOpen() || 
               !bd.Bd.conection.connectionA.currentEntityManager().getTransaction().isActive()){
                    bd.Bd.conection.connectionA.currentEntityManager().getTransaction().begin();
                }
                bd.Bd.conection.connectionA.currentEntityManager().getTransaction().commit();
            }catch(Exception ex){
            }finally{
                bd.Bd.conection.connectionA.closeEntityManager();
            }
            
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jRadioButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton7ActionPerformed
        list.get(0).setTef(1);
    }//GEN-LAST:event_jRadioButton7ActionPerformed

    private void jRadioButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton8ActionPerformed
        list.get(0).setTef(2);
    }//GEN-LAST:event_jRadioButton8ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        CriaConfiguracaoTaxaCartao();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        if (!evt.getItem().toString().equals("")) {
            list.get(0).setEnquadramento(evt.getItem().toString());
            if (evt.getItem().toString().equals("Micro empresa - ME")) {
                jComboBox2.setEnabled(true);
                jTextField21.setText("1");
                list.get(0).setCrt('1');
            } else if (evt.getItem().toString().equals("Empresa de pequeno porte - EPP")) {
                jComboBox2.setEnabled(true);
                jTextField21.setText("1");
                list.get(0).setCrt('1');
            } else {
                jComboBox2.setEnabled(true);
                jTextField21.setText("3");
                list.get(0).setCrt('3');
            }
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        if (!evt.getItem().toString().equals("")) {
            list.get(0).setTipoTributario(evt.getItem().toString());
            if (!evt.getItem().toString().equals("Simples nacional")) {
                list.get(0).setCrt('3');
                jTextField21.setText("3");
            } else {
                list.get(0).setCrt('1');
                jTextField21.setText("1");
            }
        }
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jComboBox10ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox10ItemStateChanged
        if (evt.getItem().equals("Gmail")) {
            jTextField35.setText("smtp.gmail.com");
            jTextField35.setEnabled(false);
            jTextField36.setText("465");
            jTextField36.setEnabled(false);
        } else if (evt.getItem().equals("Hotmail")) {
            jTextField35.setText("smtp-mail.outlook.com");
            jTextField35.setEnabled(false);
            jTextField36.setText("587");
            jTextField36.setEnabled(false);
        } else if (evt.getItem().equals("Outros")) {
            //jTextField35.setText("");
            jTextField35.setEnabled(true);
            jTextField36.setText("");
            jTextField36.setEnabled(true);
        }
        {

        }

    }//GEN-LAST:event_jComboBox10ItemStateChanged

    private void jTextField23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField23ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField23ActionPerformed

    private void jTextField42KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField42KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField42KeyPressed

    private void jTextField42KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField42KeyReleased
        if (jTextField42.getText().length() == 9) {
            try{
            javax.persistence.Query q = bd.Bd.conection.connectionA.currentEntityManager().createNamedQuery("Cep.findByCep");
            q.setParameter("cep", jTextField42.getText());
            List<Cep> listCep = q.getResultList();
            jTextField43.setText(listCep.get(0).getLogradouro());
            jTextField46.setText(listCep.get(0).getBairro());
            jComboBox11.setSelectedItem(listCep.get(0).getCidade() + " - " + listCep.get(0).getUf());
            }catch(Exception ex){
            }finally{
                bd.Bd.conection.connectionA.closeEntityManager();
            }
        }
    }//GEN-LAST:event_jTextField42KeyReleased

    private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged
        if (evt.getItem().toString().equals("Varejista em geral")) {
            jCheckBox15.setEnabled(true);
            jCheckBox16.setEnabled(true);
        } else if (evt.getItem().toString().equals("Mercado, Supermercado, Hipermercado")) {
            jCheckBox15.setEnabled(true);
            jCheckBox16.setEnabled(false);
            jCheckBox16.setSelected(false);
        } else if (evt.getItem().toString().equals("Autopeças e Oficinas")) {
            jCheckBox16.setEnabled(true);
            jCheckBox15.setEnabled(false);
            jCheckBox15.setSelected(false);
        } else {
            jCheckBox15.setEnabled(false);
            jCheckBox15.setSelected(false);
            jCheckBox16.setEnabled(false);
            jCheckBox16.setSelected(false);
        }
    }//GEN-LAST:event_jComboBox4ItemStateChanged

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try{
        alq = new AliquotaSimplesNacional();
        alq.setEmpresa(list.get(0));
        JD_inserir_impostos jd_impostos = new JD_inserir_impostos(null, true, alq);
        jd_impostos.setVisible(true);
        if (alq.getEmpresa() != null
                && alq.getImposto() != null
                && alq.getTipo() != null
                && alq.getValor() != null) {
            if (!bd.Bd.conection.connectionA.currentEntityManager().isOpen() || !bd.Bd.conection.connectionA.currentEntityManager().getTransaction().isActive()) {
                bd.Bd.conection.connectionA.currentEntityManager().getTransaction().begin();
            }
                bd.Bd.conection.connectionA.currentEntityManager().persist(alq);
                bd.Bd.conection.connectionA.currentEntityManager().getTransaction().commit();
                JOptionPane.showMessageDialog(null, "Configuração do Simples Nacional configurada com sucesso!");
                modelTableSimplesNacional.setNumRows(jTable6.getRowCount() + 1);
                jTable6.setModel(modelTableSimplesNacional);
                jTable6.setValueAt(alq.getImposto(), jTable6.getRowCount() - 1, 0);
                jTable6.setValueAt(alq.getValor().toString(), jTable6.getRowCount() - 1, 1);
                jTable6.setValueAt(String.valueOf(alq.getTipo()), jTable6.getRowCount() - 1, 2);
            
        }
        }catch(Exception ex){
        }finally{
            bd.Bd.conection.connectionA.closeEntityManager();
        }
        
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jTextField28FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField28FocusLost
        
    }//GEN-LAST:event_jTextField28FocusLost

    private void jTextField28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField28ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField28ActionPerformed

    private void jTextField29FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField29FocusLost

        pardigital.setUltimanf55(jTextField29.getText().trim());
    }//GEN-LAST:event_jTextField29FocusLost

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {

            if (!bd.Bd.conection.connectionA.currentEntityManager().isOpen() || 
                !bd.Bd.conection.connectionA.currentEntityManager().getTransaction().isActive()) {
                bd.Bd.conection.connectionA.currentEntityManager().getTransaction().begin();
            }
            
            Object ob = bd.Bd.conection.connectionA.currentEntityManager().merge(
                    list.get(0).getTipoPagamentoPromissoriaList().get(jTable4.getSelectedRow())
            );
            bd.Bd.conection.connectionA.currentEntityManager().remove(ob);
            modelTableFormaPagamento2 = (DefaultTableModel) jTable4.getModel();
            modelTableFormaPagamento2.removeRow(jTable4.getSelectedRow());
            jTable4.setModel(modelTableFormaPagamento2);
            bd.Bd.conection.connectionA.currentEntityManager().getTransaction().commit();
        } catch (Exception ex) {
        } finally {
            bd.Bd.conection.connectionA.closeEntityManager();
        }

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        if (!contemDescontoMaximo() && !jMoneyField1.getText().equals("0,00")) {

            try {
                
                if (!bd.Bd.conection.connectionA.currentEntityManager().isOpen() || !bd.Bd.conection.connectionA.currentEntityManager().getTransaction().isActive()) {
                    bd.Bd.conection.connectionA.currentEntityManager().getTransaction().begin();
                }
                
                Query q = bd.Bd.conection.connectionA.currentEntityManager().createNativeQuery("ALTER TABLE gupo_produto "
                        + "ALTER COLUMN desconto TYPE numeric(9,2);");
                q.executeUpdate();

                if (!bd.Bd.conection.connectionA.currentEntityManager().getTransaction().isActive()) {
                    bd.Bd.conection.connectionA.currentEntityManager().getTransaction().begin();
                }
                bd.Bd.conection.connectionA.currentEntityManager().getTransaction().commit();

            } catch (Exception ex) {
            } finally {
                bd.Bd.conection.connectionA.closeEntityManager();
            }

            try {

                GupoProduto g = gupoProdutoList.get(jComboBox12.getSelectedIndex());
                GupoProduto grupo = bd.Bd.conection.connectionA.currentEntityManager().find(GupoProduto.class, g.getCodigoGrupo());
                grupo.setDesconto(new BigDecimal(jMoneyField1.getText().replace(",", ".")));
                bd.Bd.conection.connectionA.currentEntityManager().merge(grupo);
                if (!bd.Bd.conection.connectionA.currentEntityManager().isOpen() || !bd.Bd.conection.connectionA.currentEntityManager().getTransaction().isActive()) {
                    bd.Bd.conection.connectionA.currentEntityManager().getTransaction().begin();
                }
                bd.Bd.conection.connectionA.currentEntityManager().getTransaction().commit();

                //Configuracao con = bd.Bd.conection.connectionA.currentEntityManager().find(Configuracao.class, 1);
                configuracao.setDescontoMaximoGrupo(1);
                bd.Bd.conection.connectionA.currentEntityManager().merge(configuracao);
                if (!bd.Bd.conection.connectionA.currentEntityManager().getTransaction().isActive()) {
                    bd.Bd.conection.connectionA.currentEntityManager().getTransaction().begin();
                }
                bd.Bd.conection.connectionA.currentEntityManager().getTransaction().commit();

                modelTableGrupoDesconto.setNumRows(jTable7.getRowCount() + 1);
                jTable7.setModel(modelTableGrupoDesconto);
                jTable7.setValueAt(grupo.getDescricaoGrupo(), jTable7.getRowCount() - 1, 0);
                jTable7.setValueAt(jMoneyField1.getText(), jTable7.getRowCount() - 1, 1);
                jTable7.setValueAt(grupo, jTable7.getRowCount() - 1, 2);
            } catch (Exception ex) {
            } finally {
                bd.Bd.conection.connectionA.closeEntityManager();
            }
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jCheckBox17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox17ActionPerformed
        if (jCheckBox17.isSelected()) {
            TabelaDesconto(0);
            formatarTabelaDesconto();
            AbrirCamposDescontoMaximoProduto();
        } else {
            jTable7 = new JTable(null);
            fechaCampos();
        }

    }//GEN-LAST:event_jCheckBox17ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        try {
            if (!bd.Bd.conection.connectionA.currentEntityManager().isOpen() || !bd.Bd.conection.connectionA.currentEntityManager().getTransaction().isActive()) {
                bd.Bd.conection.connectionA.currentEntityManager().getTransaction().begin();
            }
            configuracao = bd.Bd.conection.connectionA.currentEntityManager().find(Configuracao.class, 1);

            configuracao.setDescontoMaximoVendedor(new BigDecimal(jMoneyField2.getText().replace(".", "").replace(",", ".")));
            bd.Bd.conection.connectionA.currentEntityManager().persist(configuracao);
            bd.Bd.conection.connectionA.currentEntityManager().getTransaction().commit();
            if (ConsultaDescontoMaximo()) {
                SetDescontoMaximo();
                jLabel72.setVisible(true);
                JOptionPane.showMessageDialog(null, "Desconto configurado com sucesso!!");
            }
        } catch (Exception ex) {
        } finally {
            bd.Bd.conection.connectionA.closeEntityManager();
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jCheckBox18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox18ActionPerformed
        if(jCheckBox18.isSelected()){
            AbrirCamposDescontoMaximo();
        }else{
            jMoneyField2.setText("0,00");
            jButton10.doClick();
            FecharCamposDescontoMaximo();
            
        }
    }//GEN-LAST:event_jCheckBox18ActionPerformed

    private void jMoneyField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jMoneyField2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMoneyField2KeyPressed

    private void jMoneyField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jMoneyField2KeyReleased
        jLabel72.setVisible(false);
        if(new BigDecimal(jMoneyField2.getText().replace(".", "").replace(",", ".")).compareTo(new BigDecimal(100.00)) != -1){
            jMoneyField2.setText("0,00");
            JOptionPane.showMessageDialog(null, "Desconto inválido.", "Erro", 0);
        }
    }//GEN-LAST:event_jMoneyField2KeyReleased

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        
    }//GEN-LAST:event_jTable3MouseClicked

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {
            if(!bd.Bd.conection.connectionA.currentEntityManager().isOpen()||
               !bd.Bd.conection.connectionA.currentEntityManager().getTransaction().isActive()){
                bd.Bd.conection.connectionA.currentEntityManager().getTransaction().begin();
            }
            bd.DAO.TipoPagamentoCartao ob = bd.Bd.conection.connectionA.currentEntityManager().merge(
                    list.get(0).getTipoPagamentoCartaoList().get(jTable3.getSelectedRow())
            );
            
            if (ob != null) {
                
                for (int i = 0; i < ob.getTipoPagamento().getConfigTaxaCartaoList().size(); i++) {
                    ConfigTaxaCartao obj = ob.getTipoPagamento().getConfigTaxaCartaoList().get(i);
                    bd.Bd.conection.connectionA.currentEntityManager().remove(obj);
                    //bd.Bd.conection.connectionA.currentEntityManager().getTransaction().commit();
                }
                
                bd.Bd.conection.connectionA.currentEntityManager().remove(ob);
                bd.Bd.conection.connectionA.currentEntityManager().getTransaction().commit();

                DefaultTableModel model = (DefaultTableModel) jTable3.getModel();
                model.removeRow(jTable3.getSelectedRow());
                jTable3.setModel(model);
                list.get(0).getTipoPagamentoCartaoList().remove(ob);
            }
        } catch (Exception ex) {
        } finally {
            bd.Bd.conection.connectionA.closeEntityManager();
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        
        Integer codigoConfigTaxaCartao = null;
        if (jTable2.getSelectedRow() > -1) {
            try {
                codigoConfigTaxaCartao = (Integer) jTable2.getValueAt(jTable2.getSelectedRow(), 5);
                if (codigoConfigTaxaCartao > 0) {
                    ConfigTaxaCartao cartao = bd.Bd.conection.connectionA.currentEntityManager().find(ConfigTaxaCartao.class, codigoConfigTaxaCartao);

                    if (cartao != null) {
                        bd.Bd.conection.connectionA.currentEntityManager().remove(cartao);
                        if (!bd.Bd.conection.connectionA.currentEntityManager().getTransaction().isActive()) {
                            bd.Bd.conection.connectionA.currentEntityManager().getTransaction().begin();
                        }
                        bd.Bd.conection.connectionA.currentEntityManager().getTransaction().commit();

                        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
                        model.removeRow(jTable2.getSelectedRow());
                        jTable2.setModel(model);
                    }
                }
            } catch (Exception x) {
            } finally {
                bd.Bd.conection.connectionA.closeEntityManager();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Selecione uma taxa para ser removida!", "Confiança Sistemas", 0);
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jCheckBox16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBox16MouseClicked
        if(jCheckBox16.isSelected()){
            jRadioButton9.setEnabled(true);
            jRadioButton10.setEnabled(true);
        }else{
            jRadioButton9.setEnabled(false);
            jRadioButton10.setEnabled(false);
        }
    }//GEN-LAST:event_jCheckBox16MouseClicked

    private void jCheckBox14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox14ActionPerformed

    private void jTextField30FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField30FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField30FocusLost

    private void jTextField31FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField31FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField31FocusLost

    private void jTextField31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField31ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField31ActionPerformed

    private void Remov1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Remov1ActionPerformed
        try {
            if(!bd.Bd.conection.connectionA.currentEntityManager().isOpen()||
               !bd.Bd.conection.connectionA.currentEntityManager().getTransaction().isActive()){
                bd.Bd.conection.connectionA.currentEntityManager().getTransaction().begin();
            }
            Object ob = bd.Bd.conection.connectionA.currentEntityManager().merge(
                    list.get(0).getTipoPagamentoAvistaList().get(jTable5.getSelectedRow())
            );
            bd.Bd.conection.connectionA.currentEntityManager().remove(
                ob
            );
            modelTableFormaPagamentoAvista = (DefaultTableModel) jTable5.getModel();
            modelTableFormaPagamentoAvista.removeRow(jTable5.getSelectedRow());
            jTable5.setModel(modelTableFormaPagamentoAvista);
            bd.Bd.conection.connectionA.currentEntityManager().getTransaction().commit();
        } catch (Exception ex) {
            Logger.getLogger(JIFEmpresa.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            bd.Bd.conection.connectionA.closeEntityManager();
        }
    }//GEN-LAST:event_Remov1ActionPerformed

    private void Remov2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Remov2ActionPerformed
        try {
            if(!bd.Bd.conection.connectionA.currentEntityManager().isOpen()||
               !bd.Bd.conection.connectionA.currentEntityManager().getTransaction().isActive()){
                bd.Bd.conection.connectionA.currentEntityManager().getTransaction().begin();
            }
            
            Object ob = bd.Bd.conection.connectionA.currentEntityManager().merge(
                    list.get(0).getTipoPagamentoList().get(jTable1.getSelectedRow())
            );
            bd.Bd.conection.connectionA.currentEntityManager().remove(
                ob
            );
            modelTableFormaPagamento = (DefaultTableModel) jTable1.getModel();
            modelTableFormaPagamento.removeRow(jTable1.getSelectedRow());
            jTable1.setModel(modelTableFormaPagamento);
            bd.Bd.conection.connectionA.currentEntityManager().getTransaction().commit();
        } catch (Exception ex) {
        } finally {
            bd.Bd.conection.connectionA.closeEntityManager();
        }
    }//GEN-LAST:event_Remov2ActionPerformed

    private void jCheckBox20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBox20MouseClicked
        
        jRadioButton11.setSelected(jCheckBox20.isSelected());
        jRadioButton12.setSelected(jCheckBox20.isSelected());
        
    }//GEN-LAST:event_jCheckBox20MouseClicked

    private void jLabel75MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel75MouseClicked
        jRadioButton11.doClick();
    }//GEN-LAST:event_jLabel75MouseClicked

    private void jLabel76MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel76MouseClicked
        jRadioButton11.doClick();
    }//GEN-LAST:event_jLabel76MouseClicked

    private void jLabel77MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel77MouseClicked
        jRadioButton12.doClick();
    }//GEN-LAST:event_jLabel77MouseClicked

    private void jLabel78MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel78MouseClicked
        jRadioButton12.doClick();
    }//GEN-LAST:event_jLabel78MouseClicked

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem Remov1;
    private javax.swing.JMenuItem Remov2;
    private javax.swing.JPopupMenu Remove1;
    private javax.swing.JPopupMenu Remove2;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private java.util.List<bd.DAO.CidadeCodigo> cidadeCodigoList;
    private javax.persistence.Query cidadeCodigoQuery;
    private javax.swing.ButtonGroup codigoProdutoDANFE;
    private java.util.List<bd.DAO.ContaBancaria> contaBancariaList;
    private javax.persistence.Query contaBancariaQuery;
    private java.util.List<bd.DAO.GupoProduto> gupoProdutoList;
    private javax.persistence.Query gupoProdutoQuery;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox10;
    private javax.swing.JCheckBox jCheckBox11;
    private javax.swing.JCheckBox jCheckBox12;
    private javax.swing.JCheckBox jCheckBox13;
    private javax.swing.JCheckBox jCheckBox14;
    private javax.swing.JCheckBox jCheckBox15;
    private javax.swing.JCheckBox jCheckBox16;
    private javax.swing.JCheckBox jCheckBox17;
    private javax.swing.JCheckBox jCheckBox18;
    private javax.swing.JCheckBox jCheckBox19;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox20;
    private javax.swing.JCheckBox jCheckBox21;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JCheckBox jCheckBox9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox<String> jComboBox10;
    private javax.swing.JComboBox<String> jComboBox11;
    private javax.swing.JComboBox<String> jComboBox12;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JComboBox<String> jComboBox8;
    private javax.swing.JComboBox<String> jComboBox9;
    private com.toedter.calendar.JDateChooser jDateChooser_data_cadastro1;
    private com.toedter.calendar.JDayChooser jDayChooser1;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_cnpj1;
    private javax.swing.JLabel jLabel_ie1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private ClassesEntidades.JMoneyField jMoneyField1;
    private ClassesEntidades.JMoneyField jMoneyField2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JPopupMenu jPopupMenu_remove_promossoria;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton10;
    private javax.swing.JRadioButton jRadioButton11;
    private javax.swing.JRadioButton jRadioButton12;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JRadioButton jRadioButton8;
    private javax.swing.JRadioButton jRadioButton9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTabbedPane jTabbedPane5;
    private javax.swing.JTabbedPane jTabbedPane6;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JTable jTable7;
    public static javax.swing.JTextField jTextField;
    private javax.swing.JTextField jTextField1;
    public static javax.swing.JTextField jTextField10;
    public static javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    public static javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    public static javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField27;
    private javax.swing.JTextField jTextField28;
    private javax.swing.JTextField jTextField29;
    public static javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField30;
    private javax.swing.JTextField jTextField31;
    private javax.swing.JTextField jTextField32;
    private javax.swing.JTextField jTextField33;
    private javax.swing.JTextField jTextField34;
    private javax.swing.JTextField jTextField35;
    private javax.swing.JTextField jTextField36;
    private javax.swing.JTextField jTextField37;
    private javax.swing.JTextField jTextField38;
    private javax.swing.JTextField jTextField39;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField40;
    private javax.swing.JTextField jTextField41;
    private javax.swing.JTextField jTextField42;
    private javax.swing.JTextField jTextField43;
    private javax.swing.JTextField jTextField44;
    private javax.swing.JTextField jTextField45;
    private javax.swing.JTextField jTextField46;
    private javax.swing.JTextField jTextField47;
    private javax.swing.JTextField jTextField48;
    private javax.swing.JTextField jTextField49;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    public static javax.swing.JTextField jTextField9;
    private javax.swing.JTextField jTextField_endereco;
    private javax.swing.JTextField jTextField_nome_fantazia1;
    private javax.swing.JTextField jTextField_numero_endereco1;
    private javax.swing.JTextField jTextField_razao_social1;
    private javax.swing.JTextPane jTextPane1;
    private java.util.List<bd.DAO.Empresa> list;
    private javax.persistence.Query query;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton saveButton;
    private java.util.List<bd.DAO.TipoPagamento> tipoPagamentoList;
    private javax.persistence.Query tipoPagamentoQuery;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    public void AlimentaTabelaSimplesNacional() {
        for (int i = 0; i < list.get(0).getTipoPagamentoCartaoList().size(); i++) {
            modelTableFormaPagamento3.setNumRows(jTable3.getRowCount() + 1);
            jTable3.setModel(modelTableFormaPagamento3);
            jTable3.setValueAt(list.get(0).getTipoPagamentoCartaoList().get(i).getTipoPagamento().getDescricaoTipoPagamento(), jTable3.getRowCount() - 1, 0);
            jTable3.setValueAt(list.get(0).getTipoPagamentoCartaoList().get(i).getTipo(), jTable3.getRowCount() - 1, 1);
            if (list.get(0).getTipoPagamentoCartaoList().get(i).getTipo().equals("Dia Fixo")) {
                jTable3.setValueAt(list.get(0).getTipoPagamentoCartaoList().get(i).getDiaFixo(), jTable3.getRowCount() - 1, 2);
            } else if (list.get(0).getTipoPagamentoCartaoList().get(i).getTipo().equals("Intervalo de dias")) {
                jTable3.setValueAt(list.get(0).getTipoPagamentoCartaoList().get(i).getQtdDia(), jTable3.getRowCount() - 1, 2);
            }
            
            jTable3.setValueAt(
                list.get(0).getTipoPagamentoCartaoList().get(i).getRecebimentoPacelado() == 0 ? "Parcelado" : "A vista",
                    jTable3.getRowCount() - 1, 3
            );
            
            

        }
    }

    public void trataComboBoxBoleto() {
        ArrayList<String> formasPagamentoUtilizada = new ArrayList<>();
        for (int j = 0; j < jTable1.getRowCount(); j++) {
            formasPagamentoUtilizada.add(jTable1.getValueAt(j, 0).toString());
        }

        for (int k = 0; k < jTable3.getRowCount(); k++) {
            formasPagamentoUtilizada.add(jTable3.getValueAt(k, 0).toString());
        }

        for (int i = 0; i < tipoPagamentoList.size(); i++) {
            if (!formasPagamentoUtilizada.contains(tipoPagamentoList.get(i).getDescricaoTipoPagamento())) {
                jComboBox3.addItem(tipoPagamentoList.get(i).getDescricaoTipoPagamento());
            }
        }
    }

    public void trataComboBoxGrupo() {
        for (int i = 0; i < gupoProdutoList.size(); i++) {
            jComboBox12.addItem(gupoProdutoList.get(i).getDescricaoGrupo());
        }
    }

    public void trataComboBoxPromissoria() {
        ArrayList<String> formasPagamentoUtilizada = new ArrayList<>();
        for (int j = 0; j < jTable1.getRowCount(); j++) {
            formasPagamentoUtilizada.add(jTable1.getValueAt(j, 0).toString());
        }

        for (int k = 0; k < jTable3.getRowCount(); k++) {
            formasPagamentoUtilizada.add(jTable3.getValueAt(k, 0).toString());
        }

        for (int i = 0; i < tipoPagamentoList.size(); i++) {
            if (!formasPagamentoUtilizada.contains(tipoPagamentoList.get(i).getDescricaoTipoPagamento())) {
                jComboBox7.addItem(tipoPagamentoList.get(i).getDescricaoTipoPagamento());
            }
        }
    }

    public void trataComboBoxCartao() {
        ArrayList<String> formasPagamentoUtilizada = new ArrayList<>();
        for (int j = 0; j < jTable1.getRowCount(); j++) {
            formasPagamentoUtilizada.add(jTable1.getValueAt(j, 0).toString());
        }

        for (int k = 0; k < jTable4.getRowCount(); k++) {
            formasPagamentoUtilizada.add(jTable4.getValueAt(k, 0).toString());
        }

        for (int i = 0; i < tipoPagamentoList.size(); i++) {
            if (!formasPagamentoUtilizada.contains(tipoPagamentoList.get(i).getDescricaoTipoPagamento())) {
                jComboBox6.addItem(tipoPagamentoList.get(i).getDescricaoTipoPagamento());
            }
        }
    }

    public boolean contemDescontoMaximo() {
        boolean contem = false;
        for (int i = 0; i < jTable7.getRowCount(); i++) {
            //System.out.println("1 "+list.get(0).getTipoPagamentoList().get(i).getDescricaoTipoPagamento());
            //System.out.println("2 "+jComboBox3.getSelectedItem());
            if (jTable7.getValueAt(i, 2).equals(jComboBox12.getSelectedItem())) {
                contem = true;
            }
        }
        if (contem) {
            JOptionPane.showMessageDialog(null, "Este Grupo Selecionado já foi selecionado e incluido!");
        }

        return contem;
    }

    public boolean contem() {
        boolean contem = false;
        for (int i = 0; i < list.get(0).getTipoPagamentoList().size(); i++) {
            System.out.println("1 " + list.get(0).getTipoPagamentoList().get(i).getDescricaoTipoPagamento());
            System.out.println("2 " + jComboBox3.getSelectedItem());
            if (list.get(0).getTipoPagamentoList().get(i).getDescricaoTipoPagamento().equals(jComboBox3.getSelectedItem().toString())) {
                contem = true;
            }
        }

        if (contem) {
            JOptionPane.showMessageDialog(null, "Esta forma de pagamento já foi selecionada e incluida!");
        }

        return contem;
    }

    public boolean contem2() {
        boolean contem = false;
        for (int i = 0; i < list.get(0).getTipoPagamentoPromissoriaList().size(); i++) {
            if (list.get(0).getTipoPagamentoPromissoriaList().get(i).getTipoPagamento().getDescricaoTipoPagamento().equals(jComboBox7.getSelectedItem().toString())) {
                contem = true;
            }
        }

        if (contem) {
            JOptionPane.showMessageDialog(null, "Esta forma de pagamento já foi selecionada e incluida!");
        }

        return contem;
    }

    public boolean VerificaCampos(){
        boolean retorno = true;
            StringBuilder sb = new StringBuilder();
            try{
                if(jComboBox8.getSelectedItem().toString().isEmpty()){
                    retorno = false;
                    sb.append("Selecione um tipo");
                    sb.append(System.getProperty("line.separator"));
                }
            }catch(Exception ex){
                retorno  = false;
                sb.append("Selecione um tipo");
                sb.append(System.getProperty("line.separator"));
            }
            
            try{
                if(jTextField34.getText().isEmpty()){
                    retorno = false;
                    sb.append("Informe uma quantidade de dias");
                    sb.append(System.getProperty("line.separator"));
                }
            }catch(Exception ex){
                retorno = false;
                sb.append("Informe uma quantidade de dias");
                sb.append(System.getProperty("line.separator"));
            }
            
            try{
                if(jComboBox6.getSelectedItem().toString().isEmpty()){
                    retorno = false;
                    sb.append("Selecione uma forma de pagamento");
                    sb.append(System.getProperty("line.separator"));
                }
            }catch(Exception ex){
                retorno = false;
                sb.append("Selecione uma forma de pagamento");
                sb.append(System.getProperty("line.separator"));
            }
            
            if(!retorno){
                JOptionPane.showMessageDialog(null, sb);
            }
            
        return retorno;
    }
    
    public void Salvar() {

        
        try{
            /**
            * ******************************************************
            * SALVAR MODEL PARDIGITAL
            ********************************************************
            */
            SalvaParDigital();
            
            /**
            * ******************************************************
            * SALVAR MODEL CONFIGURACAO
            ********************************************************
            */
            SalvaConfiguracao();
            
            
            /**
            * ******************************************************
            * SALVAR MODEL CONTABILISTA
            ********************************************************
            */
            SalvarContabilista();
            
            /**
            * ******************************************************
            * SALVAR MODEL EMPRESA
            ********************************************************
            */
            SalvaEmpresa();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage(), "", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        
        
        

        
        
        fechaCampos();
        JOptionPane.showMessageDialog(null, "Alterações realizada com sucesso!");
        saveButton.setEnabled(false);
        refreshButton.setText("Editar");
        cancelar = 0;
    }
    
    public void setCEP(bd.DAO.Cep cep, String situacao) {
        if (situacao.equalsIgnoreCase("contabilista")) {
            br.com.parg.viacep.CEP cepWeb = new br.com.parg.viacep.CEP();
            if (jTextField42.getText().length() == 8) {
                br.com.parg.viacep.Start st = new br.com.parg.viacep.Start();
                st.run(jTextField42.getText());
                cepWeb = st.getCep();
                if (cepWeb != null) {
                    Cep c = null;
                    try {
                        if (cep == null) {
                            c = bd.Bd.conection.connectionA.currentEntityManager().
                                    find(Cep.class, formataCep(st.getCep().CEP));
                            if (c != null) {
                                setCEP(c, situacao);
                            }
                        }
                    } catch (Exception ex) {
                    }

                } else {

                }
                /*if (cep != null) {
                jTextField_endereco.setText(cep.Logradouro);
                jTextField_bairro.setText(cep.Bairro);
                jTextField1.setText(cep.Localidade);
                jTextField_cidade.setText(cep.Ibge);
                jTextField2.setText(cep.Uf);
            } else {
                jTextField_endereco.setText("");
                jTextField_bairro.setText("");
                jTextField1.setText("");
                jTextField_cidade.setText("");
                jTextField2.setText("");
            }*/
            }
        }
    }
    
    
    public String formataCep(String cep){
        String retorno = "";
        if(!cep.isEmpty()){
            if(cep.length() == 8){
                retorno = cep.substring(0, 5) + "-" + cep.substring(5);
            }
            if(cep.length()==9){
                retorno = cep;
            }
        }
        return retorno;
    }
    
    public boolean contem3() {
        boolean contem = false;
        for (int i = 0; i < list.get(0).getTipoPagamentoCartaoList().size(); i++) {
            try {
                if (list.get(0).getTipoPagamentoCartaoList().get(i).getTipoPagamento().getDescricaoTipoPagamento().equals(jComboBox6.getSelectedItem().toString())) {
                    contem = true;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, "Escolha uma forma de pagamento válida", "Erro", 0);
                break;
            }
        }

        if (contem) {
            JOptionPane.showMessageDialog(null, "Esta forma de pagamento já foi selecionada e incluida!");
        }

        return contem;
    }

    private Cep getCep(String cep, String situacao) {
        bd.DAO.Cep c = null;
        br.com.parg.viacep.CEP cepWeb = new br.com.parg.viacep.CEP();
        if (cep.length() == 8) {
            br.com.parg.viacep.Start st = new br.com.parg.viacep.Start();
            st.run(cep);
            cepWeb = st.getCep();
            if (cepWeb != null) {
                try {
                    c = bd.Bd.conection.connectionA.currentEntityManager().
                            find(Cep.class, formataCep(st.getCep().CEP));
                    if (c != null) {
                        setCEP(c, situacao);
                    }
                } catch (Exception ex) {
                }
            }
        }
        return c;
    }
    
    private bd.DAO.Pardigital SalvaParDigital() throws Exception{
        try {
            
            if (!bd.Bd.conection.connectionA.currentEntityManager().isOpen()||
                !bd.Bd.conection.connectionA.currentEntityManager().getTransaction().isActive()) {
                    bd.Bd.conection.connectionA.currentEntityManager().getTransaction().begin();
            }
            
            try {
                if (pardigital.getCodigo() == 1) {
                    pardigital.setCsc(jTextField27.getText());
                }
            } catch (Exception ex) {
            }
            try {
                if (pardigital.getCodigo() == 2) {
                    pardigital.setCsc(jTextField24.getText());
                }
            } catch (Exception ex) {
            }
            
            try{
                if(pardigital.getCodigo() == 2 ){
                    pardigital.setUltimanf65(jTextField28.getText().trim());
                }
            }catch(Exception ex){
            }
            bd.Bd.conection.connectionA.currentEntityManager().merge(pardigital);
            bd.Bd.conection.connectionA.currentEntityManager().getTransaction().commit();
        } catch (ConnectionClosedException ex) {
            Logger.getLogger(JIFEmpresa.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            bd.Bd.conection.connectionA.closeEntityManager();
        }
        
        list.set(0, pardigital.getEmpresa());
        
        return pardigital;
    }
    
    private bd.DAO.Configuracao SalvaConfiguracao() throws Exception {
        try {
            if (!bd.Bd.conection.connectionA.currentEntityManager().isOpen()
                    || !bd.Bd.conection.connectionA.currentEntityManager().getTransaction().isActive()) {
                bd.Bd.conection.connectionA.currentEntityManager().getTransaction().begin();
            }
            try {
                if (jRadioButton9.isSelected()) {
                    configuracao.setControlaEstoqueServico(1);
                } else {
                    configuracao.setControlaEstoqueServico(0);
                }
            } catch (Exception ex) {
                configuracao.setControlaEstoqueServico(0);
            }
            
            try{
                configuracao.setCalcComissao(jCheckBox20.isSelected()?0:1);
            }catch(Exception ex){
            }
            
            try{
                configuracao.setTipoComissao(jRadioButton11.isSelected()?0:1);
            }catch(Exception ex){
            }
            
            
            configuracao = bd.Bd.conection.connectionA.currentEntityManager().merge(configuracao);
            
            bd.Bd.conection.connectionA.currentEntityManager().getTransaction().commit();
        } catch (ConnectionClosedException ex) {
        } finally {
            bd.Bd.conection.connectionA.closeEntityManager();
        }
        
        list.set(0, configuracao.getEmpresa());
        
        return configuracao;
    }
    
    private bd.DAO.Empresa SalvaEmpresa() throws Exception{
        bd.DAO.Empresa empresa = list.get(0);
        empresa.setNomeEmpresa(jTextField_razao_social1.getText().toUpperCase());
        empresa.setFantasiaEmpresa(jTextField_nome_fantazia1.getText().toUpperCase());
        empresa.setCnpjEmpresa(jTextField4.getText());
        empresa.setIeEmpresa(jTextField5.getText());
        empresa.setImEmpresa(jTextField6.getText());
        empresa.setCepEmpresa(jTextField7.getText().replace("-", "").replace(".", ""));
        empresa.setEnderecoEmpresa(jTextField_endereco.getText());
        empresa.setNumeroEmpresa(jTextField_numero_endereco1.getText());
        empresa.setBairroEmpresa(jTextField8.getText());
        empresa.setComplemento(jTextField8.getText());
        
        if(!jTextField9.getText().isEmpty()){
            try {
                bd.DAO.CidadeCodigo codigo = bd.Bd.conection.connectionA.currentEntityManager()
                        .find(bd.DAO.CidadeCodigo.class, jTextField9.getText());
                empresa.setCodigoMunicipio(codigo);
            } catch (Exception ex) {
            } finally {
                bd.Bd.conection.connectionA.closeEntityManager();
            }
        }
        
        empresa.setCodigoUf(Integer.parseInt(jTextField2.getText()));
        empresa.setUfEmpresa(jTextField3.getText());
        empresa.setTelefone(Integer.parseInt(jTextField13.getText()));
        empresa.setEmail(jTextField14.getText());
        
        try {
            try {
                if (jRadioButton3.isSelected()) {
                    empresa.setExibirCodigoProduto(0);
                } else {
                    empresa.setExibirCodigoProduto(1);
                }
            } catch (Exception ex) {
                empresa.setExibirCodigoProduto(0);
            }
            
            empresa.setSmtp(jTextField35.getText());
            empresa.setPorta(jTextField36.getText());
            empresa.setEmail2(jTextField37.getText());
            try {
                if (jCheckBox15.isSelected()) {
                    empresa.setGasGlp(1);
                } else {
                    empresa.setGasGlp(0);
                }
            } catch (Exception e) {
            }

            try {
                if (jCheckBox16.isSelected()) {
                    empresa.setPrestadorServico(1);
                } else {
                    empresa.setPrestadorServico(0);
                }
            } catch (Exception e) {
            }
            if (jPasswordField1.getText().equals(jPasswordField2.getText())) {
                empresa.setSenhaEmail(jPasswordField1.getText());
            } else {
                JOptionPane.showMessageDialog(null, "As senhas informada para o E-mail são diferente.", "Erro ao validar senha", 0);
                throw new IllegalStateException("As senhas informada para o E-mail são diferente.");
            }
            
            empresa.setAtividade(jComboBox4.getSelectedItem().toString());
            empresa = bd.Bd.conection.connectionA.currentEntityManager().merge(empresa);

            //bd.Bd.conection.connectionA.currentEntityManager().merge();
            if (!bd.Bd.conection.connectionA.currentEntityManager().isOpen()
                    || !bd.Bd.conection.connectionA.currentEntityManager().getTransaction().isActive()) {
                bd.Bd.conection.connectionA.currentEntityManager().getTransaction().begin();
            }
            bd.Bd.conection.connectionA.currentEntityManager().getTransaction().commit();
            
        } catch (RollbackException rex) {
            rex.printStackTrace();
            try {
                bd.Bd.conection.connectionA.currentEntityManager().getTransaction().begin();
                List<bd.DAO.Empresa> merged = new ArrayList<bd.DAO.Empresa>(list.size());
                for (bd.DAO.Empresa e : list) {
                    merged.add(bd.Bd.conection.connectionA.currentEntityManager().merge(e));
                }
                list.clear();
                list.addAll(merged);
            } catch (Exception ex) {
            }
        } catch (ConnectionClosedException ex) {
            Logger.getLogger(JIFEmpresa.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            bd.Bd.conection.connectionA.closeEntityManager();
        }
        list.clear();
        list.add(empresa);
        return empresa;
    }
    
    public bd.DAO.Contabilista SalvarContabilista() throws Exception {
        
            bd.DAO.Contabilista contabilista = list.get(0).getContabilista();
        try {    
            try {
                if (contabilista == null) {
                    contabilista = new Contabilista();
                    list.get(0).setContabilista(contabilista);
                }
            } catch (NullPointerException e) {
                contabilista = new Contabilista();
                list.get(0).setContabilista(contabilista);
            }
            contabilista.setNomeContabilista(jTextField38.getText().toUpperCase());
            
            if (!jTextField39.getText().isEmpty() && jTextField39.getText().length() == 11) {
                if (Biblioteca.validaCpfCnpj(jTextField39.getText())) {
                    contabilista.setCpfContabilista(jTextField39.getText());
                } else {
                    JOptionPane.showMessageDialog(null, "Informe um CPF válido para o contador.", "Contador", 0);
                    throw new IllegalStateException("Informe um CPF válido para o contador");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Informe um CPF válido para o contador.", "Contador", 0);
                throw new IllegalStateException("Informe um CPF válido para o contador");
            }
            
            if (!jTextField40.getText().isEmpty() && jTextField40.getText().length() == 14) {
                if (Biblioteca.validaCpfCnpj(jTextField40.getText())) {
                    contabilista.setCnpjContabilista(jTextField40.getText());
                } else {
                    JOptionPane.showMessageDialog(null, "Informe um CNPJ válido para o contador.", "Contador", 0);
                    throw new IllegalStateException("Informe um CNPJ válido para o contador");
                }
            }
            
            if (!jTextField41.getText().isEmpty()) {
                contabilista.setInscCrc(jTextField41.getText());
            } else {
                JOptionPane.showMessageDialog(null, "Informe um número de CRC válido para o contador.", "Contador", 0);
                throw new IllegalStateException("Informe um CRC válido para o contador");
            }
            
            contabilista.setCep(getCep(jTextField42.getText(), "contabilista"));
            
            contabilista.setLogradouro(jTextField43.getText());
            contabilista.setNumero(jTextField44.getText());
            contabilista.setComplemento(jTextField45.getText());
            contabilista.setBairro(jTextField46.getText());
            contabilista.setTelefone(jTextField47.getText());
            contabilista.setFax(jTextField48.getText());
            if (!jTextField49.getText().isEmpty()) {
                if (jTextField49.getText().contains("@")) {
                    contabilista.setEmail(jTextField49.getText());
                } else {
                    JOptionPane.showMessageDialog(null, "Informe um número de E-MAIL válido para o contador.", "Contador", 0);
                    throw new IllegalStateException("Informe um E-MAIL válido para o contador");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Informe um número de E-MAIL válido para o contador.", "Contador", 0);
                throw new IllegalStateException("Informe um E-MAIL válido para o contador");
            }
            try {
                contabilista.setCodigoCidade(String.valueOf(cidadeCodigoList.get(jComboBox11.getSelectedIndex()).getCodigo()));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(rootPane, "Verifique a cidade do contador antes de continuar.", "", 0);
            }
            
            if (!bd.Bd.conection.connectionA.currentEntityManager().isOpen()
                    || !bd.Bd.conection.connectionA.currentEntityManager().getTransaction().isActive()) {
                bd.Bd.conection.connectionA.currentEntityManager().getTransaction().begin();
            }
            if (configuracao.getCod() != null
                    ? configuracao.getCod() > 0 : false) {
                contabilista = bd.Bd.conection.connectionA.currentEntityManager().merge(contabilista);
            } else {
                bd.Bd.conection.connectionA.currentEntityManager().persist(contabilista);
            }
            bd.Bd.conection.connectionA.currentEntityManager().getTransaction().commit();
        } catch (ConnectionClosedException ex) {
            Logger.getLogger(JIFEmpresa.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            bd.Bd.conection.connectionA.closeEntityManager();
        }
        return contabilista;
    }
    

}

