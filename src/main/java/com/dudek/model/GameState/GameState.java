package com.dudek.model.GameState;

import com.dudek.file.SerializableFileManager;
import com.dudek.model.Car.Car;
import com.dudek.model.Car.CarGenerator;
import com.dudek.model.Car.CarParts.CarPart;
import com.dudek.model.Car.NewCarsDatabase;
import com.dudek.model.Client.Client;
import com.dudek.model.Client.ClientBase;
import com.dudek.model.Client.ClientGenerator;
import com.dudek.model.Commercial.Commercial;
import com.dudek.model.Commercial.CommercialFactory;
import com.dudek.model.Mechanic.Mechanic;
import com.dudek.model.Mechanic.MechanicGarage;
import com.dudek.model.Player.Player;
import com.dudek.model.Transaction.TransactionContainer;

import java.io.Serializable;
import java.math.BigDecimal;

public class GameState implements Serializable {

    private final Player player;
    private final NewCarsDatabase newCarsDatabase;
    private final ClientBase clients;
    private final TransactionContainer transactions;
    private final MechanicGarage mechanicGarage;
    private int moveCounter;
    private final CommercialFactory commercialFactory;

    public GameState() {
        this.moveCounter = 0;

        this.transactions = new TransactionContainer();
        this.player = new Player();
        this.mechanicGarage = new MechanicGarage();
        this.clients = new ClientBase(new ClientGenerator());
        this.newCarsDatabase = new NewCarsDatabase(new CarGenerator());
        this.commercialFactory = new CommercialFactory();
    }

    public TransactionContainer getTransactions() {
        return transactions;
    }

    public NewCarsDatabase getCarBase() {
        return newCarsDatabase;
    }

    public int getMoveCounter() {
        return moveCounter;
    }

    public ClientBase getClients() {
        return clients;
    }

    public Player getPlayer() {
        return player;
    }

    public void buyACar() {
        Car boughtCar = newCarsDatabase.getChosenCar();
        finalizePurchase(boughtCar);
    }

    private void finalizePurchase(Car boughtCar) {
        player.buyACar(boughtCar);
        newCarsDatabase.sellACar(boughtCar);
        newCarsDatabase.generateNewCar();
        transactions.addBuyCarTransaction(boughtCar.getValueWithParts(), boughtCar, null, null, null);
        moveCounter++;
    }

    public void sellACar() {
        Car potentialCar = player.getOwnedCars().getCarFromBase();
        BigDecimal sellingPrice = player.getOwnedCars().calculateCarPrice15PercentHigher(potentialCar);
        Client potentialClient = clients.getClientFromBase();

        if (potentialClient.canAfford(sellingPrice) && potentialClient.isInterestedInThisCar(potentialCar)) {
            finalizeSale(potentialCar, sellingPrice, potentialClient);
        }
    }

    private void finalizeSale(Car potentialCar, BigDecimal sellingPrice, Client potentialClient) {
        player.sellACar(potentialCar, potentialClient);
        clients.attractNewClients(2);
        transactions.addSellCarTransaction(sellingPrice, potentialCar, potentialClient, null, null);
        moveCounter++;
    }

    public void repairCar() {
        Car brokenCar = player.getOwnedCars().getBrokenCarFromBase();
        CarPart brokenPart = brokenCar.choosePartToRepair();
        Mechanic chosenMechanic = mechanicGarage.chooseMechanic();

        if (player.canAfford(chosenMechanic.calculateRepairCostWithSalary(brokenCar, brokenPart))) {
            player.payForRepair(chosenMechanic.repairCarPart(brokenCar, brokenPart));
            brokenCar.addRepairedPartToList(brokenPart);
        } else {
            System.err.println("Niewystarczające środki na naprawę!");
        }
        transactions.addCarRepairTransaction(chosenMechanic.calculateRepairCostWithSalary(brokenCar, brokenPart), brokenCar, null, chosenMechanic, null);
        moveCounter++;
        System.out.println("Naprawa przeszla pomyślnie");

    }

    public void buyCommercial() {
        Commercial commercial = commercialFactory.chooseCommercial();
        player.payForCommercial(commercial);
        transactions.addBuyCommercialTransaction(commercial.getPrice(), null, null, null, commercial);
        moveCounter++;
        for (int i = 0; i < commercial.getClientsInterested(); i++) {
            clients.attractNewClients(commercial.getClientsInterested());
        }
    }

    public void calculateTotalRepairAndWashingCost() {
        player.getOwnedCars().getOwnedCarList().forEach(Car::printRepairAndWashCost);
    }

    public void saveGameState() {
        SerializableFileManager.save(this);
    }

}
