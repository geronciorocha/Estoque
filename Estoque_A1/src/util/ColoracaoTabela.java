/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Cliente
 */
public class ColoracaoTabela extends DefaultTableCellRenderer {

    List<Object> list = new ArrayList<>();

    public ColoracaoTabela(List<Object> list) {
        this.list = list;
    }
    
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        try {
            table.setValueAt(list.get(row), row, 0);
        } catch (Exception ex) {
        }

        try {
            switch (row % 2) {
                case 0://par
                    setBackground(new java.awt.Color(222, 221, 238));
                    setForeground(Color.BLACK);
                    break;
                default:
                    setBackground(Color.WHITE);
                    setForeground(Color.BLACK);
                    setForeground(Color.BLACK);
            }
        } catch (NullPointerException ex) {
        }
        if (row == table.getSelectedRow()) {
            if (isSelected) {//[][255,255,254]
                setBackground(new java.awt.Color(0, 120, 215));
                setForeground(new java.awt.Color(255, 255, 255));
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
        }
        return c;
    }

    public DefaultTableCellRenderer ColorTableHeader(JTable table) {
        DefaultTableCellRenderer header = new DefaultTableCellRenderer();
        header.setBackground(Color.BLACK);
        header.setSize(table.getWidth(), 61);
        header.setFont(new Font("Arial", 1, 16));
        header.setForeground(Color.WHITE);
        header.setOpaque(true);
        return header;
    }
    public DefaultTableCellRenderer ColorTableHeader(JTable table, java.awt.Color background) {
        DefaultTableCellRenderer header = new DefaultTableCellRenderer();
        header.setBackground(background);
        header.setSize(table.getWidth(), 61);
        header.setFont(new Font("Arial", 1, 16));
        header.setForeground(Color.WHITE);
        header.setOpaque(true);
        return header;
    }
}
