package Service;

import javax.swing.SwingUtilities;

import controller.ClientController;

public class ClientService {
    public static void main (String[] args){
        SwingUtilities.invokeLater(ClientController::new);
    }
}
