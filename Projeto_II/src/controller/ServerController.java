package controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Models.Candidate;
import Models.User;
import Utils.BarChart;
import Utils.DialogBox;
import Utils.GetIP;
import Utils.HelpAboutMenuBar;

public class ServerController extends JFrame implements ActionListener {
    private ServerSocket server;
    private JPanel panel;
    private HelpAboutMenuBar menuBar;
    private BarChart chart;
    private JPanel panelChart;
    private JButton electionsButton;
    private List<User> userRepository;
    private List<Candidate> candidatesRepository;
    private List<ClientHandler> clientsList;
    private Boolean allowConnections;
    private Boolean electionsRunning;
    private File reportFile;

    
    public ServerController (List<Candidate> list) {
        super("Controle de votos");
        setBounds(300, 90, 1000, 600);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        this.panel = new JPanel();
        this.panel.setLayout(new BorderLayout());
        this.panel.setVisible(true);
        add(panel);

        this.menuBar = new HelpAboutMenuBar(this);
        setJMenuBar(menuBar);

        this.candidatesRepository = list;
        
        this.chart = new BarChart();
        this.panelChart = chart.createPanel(candidatesRepository);
        this.panelChart.setVisible(true);
        this.panel.add(panelChart, BorderLayout.CENTER);
        
        this.electionsButton = new JButton("");
        this.electionsButton.setFocusable(false);
        this.electionsButton.setFont(new Font("Calibri", Font.PLAIN, 15));
        this.electionsButton.addActionListener(this);
        this.electionsButton.setVisible(true);
        panel.add(electionsButton, BorderLayout.SOUTH);

        this.userRepository = new ArrayList<>();

        this.clientsList = new ArrayList<>();

        this.reportFile = new File("Relatorio.txt");

        this.allowConnections = true;

        this.electionsRunning = false;
        setVisible(true);
        startServer();
    }
    
    public Boolean getConnectionsStatus () { 
        return this.allowConnections;
    }

    public Boolean getElectionsRunning() {
        return this.electionsRunning;
    }

    public List<Candidate> getCandidatesRepository () {
        return this.candidatesRepository;
    }

    public void startServer () {
        new Thread(() -> {
            try {
                changeButtonText("Iniciar eleições");
                String IP = GetIP.getMyIP();
                int port = 8080;
                server = new ServerSocket(port);

                System.out.printf("Servidor em %s ouvindo a porta %d!",IP,port);
                
                while (true) {
                    Socket socket = server.accept();
                    System.out.println("Cliente conectado: " + socket.getInetAddress());
                    
                    ClientHandler handler = new ClientHandler(socket, this);
                    clientsList.add(handler);
                    handler.start();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("ERROR:: " + e.getMessage());
            }
        }).start();
    }
    
    public void endElections () {
        try {
            allowConnections = false;
            server.close();
            
            System.out.println("Servidor desligado!");

            generateReport();
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR:: " + e.getMessage());
        }
        
        changeButtonText("Eleições encerradas");
    }
    
    public int validateUser(User user) {
        if (user.getCpf().length() == 11 && user.getName().length() >= 5)
        {
            int i = 0, verificador = 0;
            int soma = 0;

            for (i = 0; i <= 8; i++)
                soma += (Integer.parseInt(String.valueOf((user.getCpf().charAt(i))))) * (10 - i);

            if (soma % 11 <= 11) verificador = 11 - (soma % 11);
            else return 1;

            verificador = (verificador >= 10) ? 0 : verificador;
            
            if (String.valueOf((user.getCpf()).charAt(9)).equals(String.valueOf(verificador)))
            {
                soma = 0;
                verificador = 0;

                for (i = 0;i <= 9; i++) 
                    soma += (Integer.parseInt(String.valueOf(user.getCpf().charAt(i)))) * (11 - i);
                
                if (soma % 11 <= 11) verificador = 11 - (soma % 11);
                else return 1;

                verificador = (verificador >= 10) ? 0 : verificador;
                
                if (String.valueOf((user.getCpf().charAt(10))).equals(String.valueOf(verificador))) System.out.println("CPF Válido");
                else return 1;
                
            }
            else return 1;
            
        }
        else return 1;
        

        if (userRepository.isEmpty())
        {
            userRepository.add(user);
            return 0;
        }

        for (User index : userRepository) {
            if (index.getCpf().equals(user.getCpf())) return 1;
        }

        userRepository.add(user);
        return 0;
    }
    
    
    public void changeButtonText(String text) {
        electionsButton.setText(text);
        
        if (text.equals("Iniciar eleições")) 
        {
            electionsButton.setBackground(Color.RED);
            electionsButton.setForeground(Color.WHITE);
        }
        else if (text.equals("Eleições encerradas"))
        {
            electionsButton.setEnabled(false);
            electionsButton.setBackground(Color.DARK_GRAY);
            electionsButton.setForeground(Color.BLACK);
        }
        else 
        {
            electionsButton.setBackground(Color.GRAY);
            electionsButton.setForeground(Color.BLACK);
        }
    }
    
    public void processCommand (String command, ObjectInputStream clientIn, ObjectOutputStream clientOut) {
        switch (command) {
            case "validate":
            try {                    
                User user = (User) clientIn.readObject();
                String candidateName = (String) clientIn.readObject();
                
                processVote(user, candidateName, clientOut);
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("ERRO:: " + e.getMessage());
            }
        }
    }
        
    private void processVote (User user, String name, ObjectOutputStream out) throws Exception {
        int error = validateUser(user);
        out.writeObject("authentication");
        out.flush();
        
        if (error == 0) 
        { 
            System.out.println("Server: user authenticated...");

            for (Candidate c : candidatesRepository) {
                if (c.getName().equals(name))
                {
                    c.incrementVotesCounter();
                    
                    user.setCandidate(c);

                    System.out.println(String.format(
                        "%s: %d",c.getName(), c.getVotesCount()));

                    SwingUtilities.invokeLater(() -> updateChart());
                }
            }

            out.writeObject(error);
            out.flush();
        }
        else 
        {
            out.writeObject(error);
            out.flush();

            throw new Exception("ERROR:: user authentication failed...");
        }
    }
    
    public void updateChart() {
        chart.updateDataset(this.candidatesRepository);
        
        panel.revalidate();
        panel.repaint();
    }

    public void actionPerformed (ActionEvent e) {
        try {
            if (electionsButton.getText().equals("Iniciar eleições"))
            {
                electionsRunning = true;

                for (ClientHandler client : clientsList) {
                    client.sendMessage("begin");
                    client.sendMessage(candidatesRepository);
                }
                    
                

                changeButtonText("Encerrar Eleições");
            }
            else 
            {
                for (ClientHandler client : clientsList) 
                    client.sendMessage("end");

                endElections();
            }
        }
        catch (Exception exc) {
            System.out.println("ERROR:: " + exc.getMessage());

            String str = "Erro ao iniciar as eleições";
            SwingUtilities.invokeLater(() -> new DialogBox(this, str, "Tente novamente"));
        }
    }
    
    public void generateReport () {
        try {
            FileWriter input = new FileWriter(reportFile);

            input.write("NOME - CPF - CANDIDATO\n");

            for (User user : userRepository) 
            {
                input.write(String.format("%s - %s - %s\n", 
                        user.getName(), user.getCpf(), user.getCandidateName()));
            }

            String str = "Relatório das eleições gerado";
            SwingUtilities.invokeLater(() -> new DialogBox(this, str, "Eleições encerradas"));
            
            input.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
         
            String str = "Erro inesperado ao encerrar";
            SwingUtilities.invokeLater(() -> new DialogBox(this, str, "Tente novamente"));
        }
    }

}

class ClientHandler extends Thread {
    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ServerController parent;

    public ClientHandler(Socket socket, ServerController parent) {
        this.clientSocket = socket;
        this.parent = parent;
    }

    public <T> void sendMessage (T message) {
        try {
            out.writeObject(message);
            out.flush();
        } 
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR:: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(clientSocket.getInputStream());

            if (parent.getElectionsRunning())
            {
                sendMessage("begin");
                sendMessage(parent.getCandidatesRepository());
            }

            while (parent.getConnectionsStatus()) {
                String command = (String) in.readObject();
                parent.processCommand(command, in, out);
            }
        
        } 
        catch (Exception e) {
           e.printStackTrace();
           System.out.println("ERROR:: " + e.getMessage());
        }      
    }

}
