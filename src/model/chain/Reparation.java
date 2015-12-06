package model.chain;

import model.computer.Computer;
import model.computer.EComponent;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcin on 2015-12-02.
 */
public abstract class Reparation {
    /**
     * Kazdy element ma jeden unikalny component.
     */
    protected EComponent component;

    /**
     * Kolejnosc lancucha wyznaczana przez liste i kolejne komponenty sprawdzane sa poprzez 'iter'.
     * Na koncu iter jest = 0.
     */
    protected static List<Reparation> orderOfReparations = new ArrayList<>();
    protected static int iter = 0;    //taka sama w calym obrebie lancucha
    protected static boolean flagFirstTime = true;

    public void setNextReparation(Reparation reparation) {
        orderOfReparations.add(reparation);
    }

    /**
     * Ogolna metoda ktora sprawdza czy komponent jest zepsuty
     * if (true) - przekaz komputer do konkretnej metody w danym lancuchu i go napraw
     *             sprawdz czy lancuch sie nie skonczyl jesli nie sprawdz nastepny komponent z listy dla tego samego komputera .
     * if (false) - po prostu sprawdz czy lancuch sie nie skonczyl jesli nie sprawdz nastepny komponent z listy dla tego samego komputera .
     * @param computer
     */
    public void repair(Computer computer) {
        /*if (flagFirstTime) {
            flagFirstTime = false;
            orderOfReparations.get(iter).repair(computer);
        }*/
        if (iter != orderOfReparations.size()) {
            if (computer.checkIfComponentIsBroken(orderOfReparations.get(iter).getComponent())) { //orderOfReparations.get(iter++)
                String repairedComponent = orderOfReparations.get(iter).repairComponent(computer);
                JOptionPane.showMessageDialog(null, repairedComponent + " has been repaired");
            }
            orderOfReparations.get(iter++).repair(computer);
        } else {
            iter = 0;
            //flagFirstTime = true; //wymyslic co zrobic z ta flaga by dawac ja na true.
        }
    }

    /**
     * Metoda naprawiajaca dany komponent implementowana w ukonkretnionej klasie
     * @param computer
     * @return String - jaki komponent zostal naprawiony.
     */
    protected abstract String repairComponent(Computer computer);

    /**
     * Metoda ktora zmienia kolejnosc elementow (gdy ktos przeciagnie np. Procesor z miejsca 5 na 2 w liœcie).
     * @param indexOfSelectedItem - stary index wybranego reparatora.
     * @param choosenIndexForComponent - nowy index wybranego reparatora.
     */
    public void changeOrderOfItems(int indexOfSelectedItem, int choosenIndexForComponent) {
        if (orderOfReparations.size() > indexOfSelectedItem) {
            if (indexOfSelectedItem != choosenIndexForComponent) {
                Reparation reparatorToChangeOrder = orderOfReparations.get(indexOfSelectedItem);
                orderOfReparations.remove(indexOfSelectedItem);
                orderOfReparations.add(choosenIndexForComponent, reparatorToChangeOrder);
            }
        }
    }

    public static List<Reparation> getOrderOfReparations() {
        return orderOfReparations;
    }

    public EComponent getComponent() {
        return component;
    }
}
