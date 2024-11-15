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

        String helpText = """
                    Bem-vindo ao Sistema de Votação Eletrônica!\n
                    Este sistema permite tanto a votação em um candidato, 
                    quanto a visualização dos resultados da eleição em tempo real. 
                    Abaixo, você encontrará uma explicação sobre como usar o sistema e a navegabilidade entre as opções do menu:\n\n
                    1. **Tela Inicial**:\n
                    O sistema conta com duas telas principais: a do servidor, que controla a eleição e pode ver os resultados, e a do cliente,
                    que permite a realização da votação por parte dos usuários.\n

                    2. **Menu de Navegação**:\n
                    O menu oferece as seguintes opções:\n
                    - **Ajuda**: Clique aqui para abrir esta janela de ajuda, que fornece informações sobre o uso do sistema.\n
                    - **Sobre**: Essa opção fornece informações gerais sobre o sistema, como a versão atual e os desenvolvedores do projeto.\n

                    3. **Votação**:\n
                    A votação é realizada apenas pelo cliente, e basta seguir os seguintes passos:\n
                    - Preencher o seu nome e o CPF;\n
                    - Selecionar o candidato;\n
                    - Clicar em "Confirmar";\n
                    Ao seguir esses passos, o voto será computado e a tela será bloqueada.\n

                    4. **Controle e Resultados**:\n
                    O servidor pode iniciar as eleições e consegue mostrar os resultados em tempo real da votação, a seguir estão os passos
                    para iniciar e encerrar o servidor:\n
                    - Clicar em "Iniciar eleições";\n
                    - Os votos computados atualizarão o gráfico da votação em tempo real;\n
                    - Para finalizar a votação, basta clicar em "Encerrar eleições";\n
                    - Por fim, um relatório com os dados dos votantes e dos seus respectivos candidatos será criado e salvo na máquina do servidor.\n

                    5.**Observações**\n
                    - O sistema foi desenvolvido para garantir a integridade da eleição, validando os CPFs e contabilizando os votos de maneira segura.\n
                    - Em caso de dúvidas, entre em contato com o administrador do sistema.\n
                    """;
        
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setText(helpText);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
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
