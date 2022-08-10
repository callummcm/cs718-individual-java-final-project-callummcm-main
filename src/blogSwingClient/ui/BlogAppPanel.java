package blogSwingClient.ui;

import blogSwingClient.pojos.LoginDetails;
import blogSwingClient.pojos.User;
import blogSwingClient.util.UserTableModel;
import blogSwingClient.web.API;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BlogAppPanel extends JPanel implements ActionListener{

    private JButton loginButton;
    private JButton logoutButton;
    private JButton deleteUserButton;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPanel loginDetailsPanel;
    private JPanel buttonsPanel;
    private JPanel usersTable;
    private UserTableModel model;
    private JTable users;
    private List<User> userList;
    private int selectedUser;
    private int selectedRow;

    public BlogAppPanel() {
        initComponents();
    }

    private void initComponents() {

        usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(10);

        passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(10);

        loginButton = new JButton("Login");
        logoutButton = new JButton("Logout");
        deleteUserButton = new JButton("Delete User");

        loginDetailsPanel = new JPanel();
        buttonsPanel = new JPanel();
        usersTable = new JPanel();

        loginDetailsPanel.add(usernameLabel);
        loginDetailsPanel.add(usernameField);
        loginDetailsPanel.add(passwordLabel);
        loginDetailsPanel.add(passwordField);
        buttonsPanel.add(loginButton);
        buttonsPanel.add(logoutButton);
        buttonsPanel.add(deleteUserButton);

        logoutButton.setEnabled(false);
        deleteUserButton.setEnabled(false);

        loginButton.addActionListener(this);
        logoutButton.addActionListener(this);
        deleteUserButton.addActionListener(this);

        userList = new ArrayList<>();
        model = new UserTableModel(userList);
        users = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(users);

        users.getSelectionModel().addListSelectionListener(e -> {
            if (users.getSelectedRow() > -1) {
                selectedUser = (int) users.getValueAt(users.getSelectedRow(), 0);
                selectedRow = users.getSelectedRow();
                deleteUserButton.setEnabled(true);
            }
        });

        usersTable.add(scrollPane);

        setLayout(new BorderLayout());
        add(loginDetailsPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
        add(usersTable, BorderLayout.NORTH);
        setPreferredSize(new Dimension(500, 500));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == loginButton) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            new apiWorker(true, false ,false).execute();
        }

        if (source == logoutButton) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            new apiWorker(false, true, false).execute();
        }

        if (source == deleteUserButton) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            new apiWorker(false, false, true).execute();
        }

    }

    private class apiWorker extends SwingWorker<Integer, Void> {

        boolean login;
        boolean logout;
        boolean deleteUser;

        public apiWorker(boolean login, boolean logout, boolean deleteUser) {
            this.login = login;
            this.logout = logout;
            this.deleteUser = deleteUser;
        }

        @Override
        protected Integer doInBackground() {

            if (login) {
                String username = usernameField.getText();
                String password = passwordField.getText();

                LoginDetails loginDetails = new LoginDetails(username, password);

                try {
                    int responseCode = API.getInstance().login(loginDetails);

                    if (responseCode == 204) {

                        userList = API.getInstance().getUsers();
                        return 1;
                    }
                    return -1;
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }

            }

            if (logout) {

                try {
                    int responseCode = API.getInstance().logout();

                    if (responseCode == 204) {
                        return 2;
                    }
                    return 0;
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            if (deleteUser) {

                try {

                    int responseCode = API.getInstance().deleteUser(selectedUser);

                    if (responseCode == 204) {
                        return 3;
                    }
                    return 0;
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            return 0;
        }

        @Override
        protected void done() {

            try {

                int status = get();

                if (status == -1) {
                    JOptionPane.showMessageDialog(null, "Error authenticating user");
                }

                else if (status == 1) {
                    loginButton.setEnabled(false);
                    logoutButton.setEnabled(true);
                    usernameField.setText("");
                    passwordField.setText("");
                    for (User user : userList) {
                        model.addUser(user);
                    }
                }

                else if (status == 2) {
                    loginButton.setEnabled(true);
                    logoutButton.setEnabled(false);
                    usernameField.setText("");
                    passwordField.setText("");
                    loginDetailsPanel.setVisible(true);
                    model.clearTable();
                    userList.clear();
                }

                else if (status == 3) {
                    model.clearTableRow(selectedRow);
                    userList.remove(selectedRow);
                    selectedUser = -1;
                    deleteUserButton.setEnabled(false);
                }

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }

            setCursor(Cursor.getDefaultCursor());

        }
    }
}
