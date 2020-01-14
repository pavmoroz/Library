package GUI;

import Database.LibraryDBFunctions;
import POJO.*;


import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class TableFrame {
    private List<Book> books;
    private TableModel model;

    public TableFrame(List<Book> books) {
        this.books = books;
    }

    public void start() {
        JFrame frameForTable = new JFrame();
        frameForTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String[] columns = {"Cover", "Name", "Author", "Price"};
        List<Object[]> data = new ArrayList<>();

        for (int i = 0; i < books.size(); i++) {
            Object[] tmpOb = new Object[4];
            tmpOb[0] = new CroppedIconLabel(books.get(i).getCover(), 200, 200);
            tmpOb[1] = books.get(i).getName();
            tmpOb[2] = books.get(i).getAuthorName();
            tmpOb[3] = books.get(i).getPrice();
            data.add(tmpOb);
        }

        model = new TableModel(data, columns);

        JTable table = new JTable(model) {
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return JLabel.class;
                    case 3:
                        return Integer.class;
                }
                return String.class;
            }
        };
        model.addTableModelListener((e) -> {
            if (table.isEditing()) {
                int row = e.getFirstRow();
                if (LibraryDBFunctions.updateBookPrice(table.getValueAt(row, 1).toString(), Integer.parseInt(table.getValueAt(row, 3).toString())))
                    JOptionPane.showMessageDialog(null, "Price was successfully updated.", "", JOptionPane.INFORMATION_MESSAGE);
                else {
                    data.get(row)[3] = books.get(row).getPrice();
                    JOptionPane.showMessageDialog(null, "Sorry, seems some problem occurred while" +
                            " updating. Please, try again later.", "Warning!", JOptionPane.WARNING_MESSAGE);
                }

            }

        });

        table.setFont(new Font("Times New Roman", 1, 20));
        table.getColumn("Cover").setCellRenderer(new LabelRenderer());

        JScrollPane tabelPanel = new JScrollPane(table);

        JButton create = new JButton("Create");
        create.setFont(new Font("Times New Roman", 1, 20));
        create.addActionListener((e) -> new AskFrame(model).start());

        JButton delete = new JButton("Delete");
        delete.setFont(new Font("Times New Roman", 1, 20));
        delete.addActionListener((e) -> {
            if (table.getSelectedRow() != -1) {
                if (LibraryDBFunctions.removeBook(table.getValueAt(table.getSelectedRow(), 1).toString()))
                    model.removeRow(table.getSelectedRow());
                else {
                    JOptionPane.showMessageDialog(null, "Sorry, seems some problem occurred while" +
                            " deleting this book. Please, try again later.", "Warning!", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(create);
        buttonPanel.add(delete);

        frameForTable.getContentPane().add(BorderLayout.SOUTH, buttonPanel);
        frameForTable.getContentPane().add(tabelPanel);
        frameForTable.setSize(900, 700);
        frameForTable.setLocationRelativeTo(null);
        frameForTable.setVisible(true);
    }
}