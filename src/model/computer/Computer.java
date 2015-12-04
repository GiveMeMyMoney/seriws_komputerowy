package model.computer;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by Marcin on 2015-12-02.
 */
public class Computer {
    private static Logger logger = Logger.getLogger(Computer.class.getName());
    private final static Boolean BROKEN = false;
    private final static Boolean EFFICIENT = true;
    Map<EComponent, Boolean> mapOfComponentAndCondition = new HashMap<>();

    private void initMap() {
        mapOfComponentAndCondition.put(EComponent.PROCESSOR, EFFICIENT);
        mapOfComponentAndCondition.put(EComponent.MEMORY, EFFICIENT);
        mapOfComponentAndCondition.put(EComponent.GRAPHICS_CARD, EFFICIENT);
        mapOfComponentAndCondition.put(EComponent.MAIN_BOARD, EFFICIENT);
        mapOfComponentAndCondition.put(EComponent.HARD_DISK, EFFICIENT);
    }

    public Computer() {
        initMap();
    }

    public Map<EComponent, Boolean> getMapOfComponentAndCondition() {
        return mapOfComponentAndCondition;
    }

    public List<EComponent> getBrokenComponent() {  //trzeba sprawdzic czy size = 0 wtedy nie ma zepsutych.
        List<EComponent> brokenComponents = new ArrayList<>();
        for (Map.Entry<EComponent, Boolean> componentAndCondition : this.mapOfComponentAndCondition.entrySet()) {
            if (!componentAndCondition.getValue()) {
                brokenComponents.add(componentAndCondition.getKey());
            }
        }
        return brokenComponents;
    }

    public void setBrokenComponents(List<EComponent> listOfBrokenComponent) {
        for (Map.Entry<EComponent, Boolean> componentAndCondition : this.mapOfComponentAndCondition.entrySet()) {
            for (EComponent brokenComponent : listOfBrokenComponent) {
                if (Objects.equals(brokenComponent, componentAndCondition.getKey())) {
                    componentAndCondition.setValue(BROKEN);
                    break;
                }
            }
        }
    }

    public void setBrokenComponent(EComponent brokenComponent) {
        for (Map.Entry<EComponent, Boolean> componentAndCondition : this.mapOfComponentAndCondition.entrySet()) {
            if (Objects.equals(brokenComponent, componentAndCondition.getKey())) {
                componentAndCondition.setValue(BROKEN);
            } else {
                logger.info("Chyba cos poszlo nie tak dla " + brokenComponent);
            }
        }
    }

    public boolean checkIfComponentIsBroken(EComponent brokenComponent) {  //trzeba sprawdzic czy size = 0 wtedy nie ma zepsutych.
        for (Map.Entry<EComponent, Boolean> componentAndCondition : this.mapOfComponentAndCondition.entrySet()) {
            if (Objects.equals(brokenComponent, componentAndCondition.getKey())) {
                return !componentAndCondition.getValue();
            }
        }
        logger.info("Chyba cos poszlo nie tak przy sprawdzaniu czy komponent jest zepsuty dla: " + brokenComponent);
        return false;
    }

    public void repairBrokenComponent(EComponent brokenComponent) {
        for (Map.Entry<EComponent, Boolean> componentAndCondition : this.mapOfComponentAndCondition.entrySet()) {
            if (Objects.equals(brokenComponent, componentAndCondition.getKey())) {
                componentAndCondition.setValue(EFFICIENT);
                break;
            }
        }
    }

    @Override
    public String toString() {
        return mapOfComponentAndCondition.toString();
    }
}
