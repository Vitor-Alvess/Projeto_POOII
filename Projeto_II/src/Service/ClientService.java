package Service;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Properties;

import javax.swing.*;

import Models.Candidate;
import Models.User;
import Utils.DialogBox;
import Utils.HelpAboutMenuBar;

public class ClientService extends JFrame implements ActionListener {
    private JPanel panel;    
    private HelpAboutMenuBar menuBar;
    private JLabel title;
    private JLabel name;
    private JLabel cpf;
    private JTextField tname;
    private JTextField tcpf;
    private JComboBox<String> voteSelectBox;
    private JButton confirmButton;
    private JLabel warningLabel;
    private List<Candidate> candidatesList;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Boolean electionsRunning = false;
    private File networkfile;

    public ClientService() {
        super("Sistema de votação");
        setBounds(300, 90, 400, 400);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.panel = new JPanel();
        this.panel.setLayout(null);
        add(panel);

        this.menuBar = new HelpAboutMenuBar(this);
        setJMenuBar(menuBar);

        this.title = new JLabel("Sistema de votação");
        this.title.setFont(new Font("Calibri", Font.PLAIN, 30));
        this.title.setSize(300, 30);
        this.title.setLocation(80, 30);
        this.panel.add(title);

        this.name = new JLabel("Nome*");
        this.name.setFont(new Font("Calibri", Font.PLAIN, 20));
        this.name.setSize(100, 20);
        this.name.setLocation(25, 100);
        this.panel.add(name);

        this.tname = new JTextField();
        this.tname.setFont(new Font("Calibri",Font.PLAIN, 15));
        this.tname.setSize(190, 20);
        this.tname.setLocation(125, 100);
        this.tname.setEnabled(false);
        this.panel.add(tname);

        this.cpf = new JLabel("CPF*");
        this.cpf.setFont(new Font("Calibri", Font.PLAIN, 20));
        this.cpf.setSize(100, 20);
        this.cpf.setLocation(25, 150);
        this.panel.add(cpf);
        
        this.tcpf = new JTextField();
        this.tcpf.setFont(new Font("Calibri",Font.PLAIN, 15));
        this.tcpf.setSize(150, 20);
        this.tcpf.setLocation(125, 150);
        this.tcpf.setEnabled(false);
        this.panel.add(tcpf);

        this.voteSelectBox = new JComboBox<String>();
        this.voteSelectBox.setFont(new Font("Calibri", Font.PLAIN, 15));
        this.voteSelectBox.setSize(100, 30);
        this.voteSelectBox.setLocation(125, 200);
        this.voteSelectBox.setEnabled(false);
        this.panel.add(voteSelectBox);

        this.warningLabel = new JLabel("Aviso: eleições não iniciadas!");
        this.warningLabel.setFont(new Font("Calibri", Font.PLAIN, 12));
        this.warningLabel.setSize(300, 30);
        this.warningLabel.setLocation(50,250);
        this.panel.add(warningLabel);

        this.confirmButton = new JButton("Confirmar");
        this.confirmButton.setFont(new Font("Calibri", Font.PLAIN, 15));
        this.confirmButton.setSize(100, 50);
        this.confirmButton.setLocation(125,300);
        this.confirmButton.setBackground(new Color(146, 245, 100));
        this.confirmButton.addActionListener(this);
        this.confirmButton.setEnabled(false);
        this.panel.add(confirmButton);

        setVisible(true);
        connectToServer();
    }

    public void connectToServer() {
        new Thread(() -> {
            try {
                networkfile = new File("network.properties");
                String IP = null;
                int port = 0;
                System.out.println("Working Directory = " + System.getProperty("user.dir"));
                if(networkfile.exists()) {
                    FileInputStream input = new FileInputStream(networkfile);
                    Properties config = new Properties();
                    config.load(input);
                    IP = config.getProperty("IP");
                    System.out.println(IP);
                    port = Integer.parseInt(config.getProperty("PORT"));
                }
                socket = new Socket(IP, port);
                System.out.println(socket.getInetAddress() + " conectado");

                electionsRunning = true;
                
                out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                in = new ObjectInputStream(socket.getInputStream());

                while(electionsRunning) {
                    String command = (String) in.readObject();
                    System.out.println("Comando recebido do servidor: " + command);
                    processCommand(command);
                }
                
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("ERRO:: " + e.getMessage());
            }
        }).start();
    }
    
    private void enableVote () {
        try { 
            candidatesList = (List<Candidate>) in.readObject();
            candidatesList.forEach(c -> voteSelectBox.addItem(c.getName()));

            tname.setEnabled(true);
            tcpf.setEnabled(true);
            voteSelectBox.setEnabled(true);
            warningLabel.setText("");
            confirmButton.setEnabled(true);
        }
        catch (Exception e) {
            System.out.println("ERRO:: " + e.getMessage());
        }
    }

    private void disableVote () {
        tname.setEnabled(false);
        tcpf.setEnabled(false);
        voteSelectBox.setEnabled(false);
        warningLabel.setText("Aviso: eleições encerradas!");
        confirmButton.setEnabled(false);
    }

    private void disableVoteForClient () {
        tname.setEnabled(false);
        tcpf.setEnabled(false);
        voteSelectBox.setEnabled(false);
        warningLabel.setText("Voto Realizado!");
        confirmButton.setEnabled(false);
    }

    private void processCommand (String command) {
        switch (command) {
            case "begin":
                enableVote();
                break;
            case "end":
                disableVote();
                try {
                    socket.close();
                    System.out.println(socket.getInetAddress() + " desconectado");
                } 
                catch (Exception e) {
                    String str = "Erro inesperado";
                    SwingUtilities.invokeLater(() -> new DialogBox(this, str, "Não foi possível desconectar"));

                    this.dispose();
                    System.out.println(e.getMessage());
                }
                break;
            case "authentication":
                try {
                    int error = (int)in.readObject();
                    voteStatusFeedback(error);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
                break;
            default:
                break;
        }
    }

    public void voteStatusFeedback(int error) {
        String str;
        switch (error){
            case 0:
                str = String.format("Voto confirmado no %s!", voteSelectBox.getSelectedItem());

                SwingUtilities.invokeLater(() -> new DialogBox(this, str, "Confirmação de voto"));

                try {
                    socket.close();
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                disableVoteForClient();
                break;
            case 1:
                str = "Erro ao votar: Nome ou CPF inválido";
                SwingUtilities.invokeLater(() -> new DialogBox(this, str, "Voto não confirmado"));
                break;
            case 2:
                str = "Este usuário já realizou o voto";
                SwingUtilities.invokeLater(() -> new DialogBox(this, str, "Voto não confirmado"));
                break;

        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (tname.getText().equals("") || tcpf.getText().equals(""))
        {
            warningLabel.setText("Por favor, preencha todos os campos que contém \"*\"");
            warningLabel.setForeground(Color.RED);
        }
        else 
        {
            User user = new User(tname.getText(), tcpf.getText());
            String candidateSelected = (String)voteSelectBox.getSelectedItem();

            String strCommand = "validate";
            try {
                out.writeObject(strCommand);
                out.writeObject(user);
                out.writeObject(candidateSelected);
                out.flush();
            }
            catch (IOException exc) {
                System.out.println("ERRO:: " + exc.getMessage());
            }
        }
    }   
}
