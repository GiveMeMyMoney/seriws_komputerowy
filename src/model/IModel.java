package model;

/**
 * Created by Marcin on 2015-12-03.
 */

import model.chain.Reparation;
import model.computer.Computer;
import model.computer.EComponent;

import java.util.List;

/**
 * interfejs rozkazujacy Modelowi
 */
public interface IModel {
    Reparation getChainOfReparation();
    Computer getComputer(List<EComponent> brokenComponents);
}
