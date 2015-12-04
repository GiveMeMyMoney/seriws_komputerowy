package model.chain;

import model.computer.Computer;
import model.computer.EComponent;

/**
 * Created by Marcin on 2015-12-02.
 */
public class ProcessorReparation extends Reparation {
    private final static EComponent COMPONENT = EComponent.PROCESSOR;

    public ProcessorReparation() {
        this.component = COMPONENT;
        if (orderOfReparations.size() == 0) {
            orderOfReparations.add(this);
        }
    }

    @Override
    public String repairComponent(Computer computer) {
        computer.repairBrokenComponent(COMPONENT);
        return COMPONENT.toString();
    }
}
