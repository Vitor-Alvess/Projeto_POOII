package Utils;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class DialogBox extends JDialog implements ActionListener{
    JLabel label;
    JButton button;

    public DialogBox(JFrame parent, String text, String status) {
        super();
        setTitle(status);
        setSize(300, 125);
        setResizable(false);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Calibri", Font.PLAIN, 15));
        add(label, BorderLayout.CENTER);

        button = new JButton("Fechar");
        button.setFont(new Font("Calibri", Font.PLAIN, 12));
        button.addActionListener(this);
        add(button, BorderLayout.SOUTH);
        
        setVisible(true);
    }


    public DialogBox (JFrame parent) {
        super();
        setTitle("Ajuda");
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        JTextArea helpText = new JTextArea();
        helpText.setEditable(false);
        helpText.setText("Bem-vindo à seção de ajuda!\n\n"
        + "Aqui você encontrará informações sobre como utilizar a aplicação:\n\n"
        + "-------- TEXTO AQUI ----------");
        
        JScrollPane scrollPane = new JScrollPane(helpText);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        JButton closeButton = new JButton("Fechar");
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        dispose();
    }
}
