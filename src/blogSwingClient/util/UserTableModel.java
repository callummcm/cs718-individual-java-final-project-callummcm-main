package blogSwingClient.util;

import blogSwingClient.pojos.User;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class UserTableModel extends AbstractTableModel {

    private List<User> userData = new ArrayList<>();
    private String[] columnNames = {"User ID", "Username", "First Name", "Last Name", "Admin"};

    public UserTableModel() {}

    public UserTableModel (List<User> userData) {
        this.userData = userData;
    }

    @Override
    public int getRowCount() {
        return userData.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void clearTable() {
        int rows = getRowCount();
        if (rows == 0) {
            return;
        }
        userData.clear();
        fireTableRowsDeleted(0, rows - 1);
    }

    public int getRowNumber(User user) {
        userData.indexOf(user);

        return 0;
    }

    public void clearTableRow(int row) {
        userData.remove(row);
        fireTableRowsDeleted(row, row);
    }

    @Override
    public Object getValueAt(int row, int column) {
        Object userAttribute = null;
        User userObject = userData.get(row);
        switch (column) {
            case 0: userAttribute = userObject.getID(); break;
            case 1: userAttribute = userObject.getUsername(); break;
            case 2: userAttribute = userObject.getFirstName(); break;
            case 3: userAttribute = userObject.getLastName(); break;
            case 4: userAttribute = userObject.getAdmin(); break;
            default: break;
        }
        return userAttribute;
    }

    public void addUser(User user) {
        userData.add(user);
        fireTableDataChanged();
    }
}
