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
                        Projeto de Interface Gráfica com Java - Leitor de arquivos .txt \n
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
