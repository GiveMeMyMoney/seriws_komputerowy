package model.chain;

import model.computer.Computer;
import model.computer.EComponent;

/**
 * Created by Marcin on 2015-12-02.
 */
public class GraphicsReparation extends Reparation {
    private final static EComponent COMPONENT = EComponent.GRAPHICS_CARD;

    public GraphicsReparation() {
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
