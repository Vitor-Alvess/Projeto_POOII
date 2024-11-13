package controller;

import javax.swing.SwingUtilities;

import Service.ClientService;

public class ClientController {
    public static void main (String[] args){
        SwingUtilities.invokeLater(ClientService::new);
    }
}
