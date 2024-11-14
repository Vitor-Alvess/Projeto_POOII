package Utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class HelpAboutMenuBar extends JMenuBar {
    private JMenu menu;
    private JMenuItem help;
    private JMenuItem about;

    public HelpAboutMenuBar(JFrame parent) {
        this.menu = new JMenu("Menu");
        add(menu);

        this.help = new JMenuItem("Ajuda");
        this.menu.add(help);
        help.addMouseListener((MouseListener) new MouseAdapter() {
            String helpText = 
    "Bem-vindo ao Sistema de Votação Eletrônica!\n\n" +
    "Este sistema permite que você vote em um candidato, visualizando os resultados da eleição em tempo real. Abaixo, você encontrará uma explicação sobre como usar o sistema e a navegabilidade entre as opções do menu:\n\n" +

    "1. **Tela Inicial**:\n" +
    "   Ao iniciar o sistema, você verá a tela inicial com as opções de menu disponíveis no topo.\n\n" +

    "2. **Menu de Navegação**:\n" +
    "   O menu oferece as seguintes opções:\n" +
    "   - **Votar**: Clique aqui para acessar a tela de votação, onde você poderá escolher um candidato e registrar seu voto.\n" +
    "   - **Resultados**: Clique aqui para visualizar os resultados da eleição em tempo real, apresentados através de um gráfico de barras.\n" +
    "   - **Ajuda**: Clique aqui para abrir esta janela de ajuda, que fornece informações sobre o uso do sistema e como votar.\n" +
    "   - **Sair**: Clique aqui para sair do sistema.\n\n" +

    "3. **Votação**:\n" +
    "   - Ao clicar em 'Votar', você será levado para a tela de votação, onde pode escolher um candidato e inserir seu nome e CPF.\n" +
    "   - Após inserir seus dados, clique no botão 'Confirmar Voto' para registrar seu voto.\n" +
    "   - O sistema irá validar seu CPF para garantir que o voto seja único.\n" +
    "   - Caso o CPF já tenha sido utilizado, uma mensagem de erro será exibida, e você precisará usar um CPF diferente.\n\n" +

    "4. **Resultados**:\n" +
    "   - Ao clicar em 'Resultados', você verá o gráfico de barras com o número de votos de cada candidato.\n" +
    "   - O gráfico será atualizado automaticamente sempre que um novo voto for registrado.\n\n" +

    "5. **Fechar o Sistema**:\n" +
    "   - Ao terminar sua sessão ou quando não precisar mais do sistema, clique no botão 'Sair' para fechar o aplicativo.\n\n" +

    "6. **Observações**:\n" +
    "   - O sistema foi desenvolvido para garantir a integridade da eleição, validando os CPFs e contabilizando os votos de maneira segura.\n" +
    "   - Em caso de dúvidas, entre em contato com o administrador do sistema.\n\n" +;
            JOptionPane.showMessageDialog(null, helpText, "Ajuda", JOptionPane.INFORMATION_MESSAGE);
            @Override
            public void mousePressed (MouseEvent e) {
                new DialogBox(parent);
            }
        });

        this.about = new JMenuItem("Sobre");
        this.menu.add(about);
        about.addMouseListener((MouseListener) new MouseAdapter() {
            @Override
            public void mousePressed (MouseEvent e) {
                String str = """
                        Projeto com GUI em Java - Sistema de votação\n
                        Versão 1.0\n
                        Autores:\n
                        Victor Oliveira Batista da Silva\n
                        Lucas Santana Aguiar\n
                        Rafael Dos Santos Alves\n
                        Guilherme Peliceri Faitarone\n
                        Vitor Alves De Souza"
                        """;;
                JOptionPane.showMessageDialog(parent, str, "Sobre", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        setVisible(true);
    }
}
