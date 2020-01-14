package GUI;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TableModel extends AbstractTableModel {

    private String[] columns;
    private List<Object[]> data;

    public TableModel(List<Object[]> data, String[] columns) {
        this.data = data;
        this.columns = columns;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 3)
            return true;
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    public void addRow(Object[] dat) {
        data.add(dat);
        int row = data.indexOf(dat);
        for (int column = 0; column < dat.length; column++) {
            fireTableCellUpdated(row, column);
        }
        fireTableRowsInserted(row, row);
    }

    public void removeRow(int rowIndex) {
        data.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public String getColumnName(int col) {
        return columns[col];
    }

    public void setValueAt(Object val, int rowIndex, int columnIndex) {
        data.get(rowIndex)[columnIndex] = val;
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}


