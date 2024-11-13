package controller;

import java.util.ArrayList;
import java.util.List;

import Models.Candidate;
import Service.ServerService;

public class ServerController {
    public static void main (String[] args) {
        Candidate candidate1 = new Candidate("Candidato 1");
        Candidate candidate2 = new Candidate("Candidato 2");

        List<Candidate> candidatesList = new ArrayList<>();
        candidatesList.add(candidate1);
        candidatesList.add(candidate2);
        
        ServerService s = new ServerService(candidatesList);
    }
}
