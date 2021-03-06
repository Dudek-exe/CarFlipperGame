package com.dudek.model.Client;

import com.dudek.menu.DataReader;

import java.io.Serializable;
import java.util.List;

public class ClientBase implements Serializable {

    private final static int initialClientNumber = 5;
    private final List<Client> clientList;

    public ClientBase(ClientGenerator generator) {
        clientList = generator.generateClients(initialClientNumber);
    }

    public void printClientList() {
        for (int i = 1; i < clientList.size() + 1; i++) {
            ClientView carView = new ClientView(i, clientList.get(i - 1));
            System.out.println(carView);
        }
    }

    private void addClientToBase() {
        Client client = new Client.ClientRandomizer().getNewRandomClient();
        clientList.add(client);
    }

    public void attractNewClients(int howMany) {
        for (int i = 0; i < howMany; i++) {
            addClientToBase();
        }
    }

    public Client getClientFromBase() {
        System.out.println("Podaj indeks klienta do pobrania: ");
        int index = DataReader.readOptionFromRange(1, clientList.size());
        return clientList.get(index);
    }


}
