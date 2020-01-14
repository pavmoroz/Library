package GUI;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

public class LabelRenderer implements TableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        TableColumn tC = table.getColumn("Cover");
        tC.setMinWidth(200);
        tC.setMaxWidth(200);
        tC.setMaxWidth(200);
        table.setRowHeight(200);
        return (Component)value;
    }
}