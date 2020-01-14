package GUI;

import Database.LibraryDBFunctions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import POJO.*;


public class AskFrame {
    private TableModel model;
    private String[] inputData = new String[4];

    public AskFrame(TableModel model) {
        this.model = model;
    }

    public void start() {
        JFrame frame = new JFrame();

        JLabel l1 = new JLabel("Write a path to a book cover: ");
        l1.setFont(new Font("Times New Roman", 1, 20));
        JTextField t1 = getFocusedTextField();
        t1.setFont(new Font("Times New Roman", 1, 20));

        JLabel l2 = new JLabel("Write a name of a book: ");
        l2.setFont(new Font("Times New Roman", 1, 20));
        JTextField t2 = getFocusedTextField();
        t2.setFont(new Font("Times New Roman", 1, 20));

        JLabel l3 = new JLabel("Write an authors name: ");
        l3.setFont(new Font("Times New Roman", 1, 20));
        JTextField t3 = getFocusedTextField();
        t3.setFont(new Font("Times New Roman", 1, 20));

        JLabel l4 = new JLabel("Write a price: ");
        l4.setFont(new Font("Times New Roman", 1, 20));
        JTextField t4 = getFocusedTextField();
        t4.setFont(new Font("Times New Roman", 1, 20));

        JPanel p1 = new JPanel();
        p1.add(l1);
        p1.add(t1);

        JPanel p2 = new JPanel();
        p2.add(l2);
        p2.add(t2);

        JPanel p3 = new JPanel();
        p3.add(l3);
        p3.add(t3);

        JPanel p4 = new JPanel();
        p4.add(l4);
        p4.add(t4);

        JPanel tmpPanel = new JPanel();
        tmpPanel.setLayout(new GridLayout(4, 1));
        tmpPanel.add(p1);
        tmpPanel.add(p2);
        tmpPanel.add(p3);
        tmpPanel.add(p4);

        JPanel labFieldPan = new JPanel();
        labFieldPan.setLayout(new FlowLayout(FlowLayout.LEFT));
        labFieldPan.add(tmpPanel);

        JButton accept = new JButton("Confirm");
        accept.setFont(new Font("Times New Roman", 1, 15));
        accept.addActionListener(e -> {
            if (checkInput(t1, t2, t3, t4)) {
                inputData[0] = t1.getText();
                inputData[1] = t2.getText();
                inputData[2] = t3.getText();
                inputData[3] = t4.getText();

                Book tmpBook = new Book(t2.getText(), t3.getText(), Integer.parseInt(t4.getText()), new ImageIcon(t1.getText()));
                tmpBook.setPathToCover(t1.getText());

                if (LibraryDBFunctions.insertBook(tmpBook))
                    model.addRow(getRow());
                else {
                    JOptionPane.showMessageDialog(null, "Sorry, seems some problem occurred while" +
                            " inserting this book. Please, try again later.", "Warning!", JOptionPane.WARNING_MESSAGE);
                }
                frame.dispose();
            }
        });

        JButton cancel = new JButton("Cancel");
        cancel.setFont(new Font("Times New Roman", 1, 15));
        cancel.addActionListener(e -> frame.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(accept);
        buttonPanel.add(cancel);

        frame.getContentPane().add(BorderLayout.SOUTH, buttonPanel);
        frame.getContentPane().add(labFieldPan);
        frame.setSize(450, 240);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private boolean checkInput(JTextField t1, JTextField t2, JTextField t3, JTextField t4) {
        String def = "Please, type here...";
        if (t1.getText().equals("") || t2.getText().equals("") ||
                t3.getText().equals("") || t4.getText().equals("") || t1.getText().equals(def)
                || t2.getText().equals(def) || t3.getText().equals(def) || t4.getText().equals(def)) {
            JOptionPane.showMessageDialog(null, "All fields should be filled in.", "Warning!", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String extension = getFileExtension(t1);
        File tmpFile = new File(t1.getText());
        boolean r1 = extension.equals("jpg");
        boolean r2 = tmpFile.exists() && !tmpFile.isDirectory();
        if (!r1 || !r2) {
            JOptionPane.showMessageDialog(null, "Path should point to an existing .jpg picture.", "Warning!", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String str = t4.getText();
        if (!str.matches("[0-9]*")) {
            JOptionPane.showMessageDialog(null, "Price should be a number.", "Warning!", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (LibraryDBFunctions.checkIfItemExists(Book.class, t2.getText())) {
            JOptionPane.showMessageDialog(null, "Such book already exists in the library!", "Warning!", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private Object[] getRow() {
        Object[] res = new Object[4];

        res[0] = new CroppedIconLabel(new ImageIcon(inputData[0]), 200, 200);

        for (int i = 1; i < 4; i++) {
            res[i] = inputData[i];
        }
        return res;
    }

    private JTextField getFocusedTextField() {
        JTextField textField = new JTextField("Please, type here...");
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setText("");
            }
        });
        return textField;
    }

    private String getFileExtension(JTextField t) {
        String name = t.getText();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }
}