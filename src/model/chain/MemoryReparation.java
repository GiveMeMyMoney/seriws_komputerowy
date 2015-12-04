package model.chain;

import model.computer.Computer;
import model.computer.EComponent;

/**
 * Created by Marcin on 2015-12-02.
 */
public class MemoryReparation extends Reparation {
    private final static EComponent COMPONENT = EComponent.MEMORY;

    public MemoryReparation() {
        this.component = COMPONENT;
        if (orderOfReparations == null && orderOfReparations.size() == 0) {
            orderOfReparations.add(this);
        }
    }

    @Override
    public String repairComponent(Computer computer) {
        computer.repairBrokenComponent(COMPONENT);
        return COMPONENT.toString();
    }


}
