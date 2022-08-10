package blogSwingClient;

import blogSwingClient.ui.BlogAppPanel;
import blogSwingClient.web.API;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class Main extends JFrame{

    public Main() {
        this.setTitle("Admin Interface");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    API.getInstance().logout();
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
            }
        });

        BlogAppPanel view = new BlogAppPanel();
        this.add(view);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.setVisible(true);
        });
    }
}
