package model;

import model.chain.*;
import model.computer.Computer;
import model.computer.EComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Marcin on 2015-12-03.
 */
public class Model implements IModel {
    private static Logger logger = Logger.getLogger(Model.class.getName());
    //ArrayList<Contact> contacts = new ArrayList<>();

    private static volatile Model instance = null;
    //private static DBrepo dBrepo = null;

    public static Model getInstance() {
        if (instance == null) {
            synchronized (Model.class) {
                if (instance == null) {
                    instance = new Model();
                }
            }
        }
        return instance;
    }

    /**
     * Konstruktor.
     */
    private Model() {
    }

    //Methods:

    @Override
    public Reparation getChainOfReparation() {
        Reparation processorReparation = new ProcessorReparation();
        Reparation memoryReparation = new MemoryReparation();
        Reparation graphicsReparation = new GraphicsReparation();
        Reparation mainBoardReparation = new MainBoardReparation();
        Reparation hardDiskReparation = new HardDiskReparation();

        processorReparation.setNextReparation(memoryReparation);
        memoryReparation.setNextReparation(graphicsReparation);
        graphicsReparation.setNextReparation(mainBoardReparation);
        mainBoardReparation.setNextReparation(hardDiskReparation);

        return processorReparation;
    }

    public List<Computer> getInitComputers() {
        List<Computer> computers = new ArrayList<>();
        //1
        Computer computer = new Computer();
        List<EComponent> components = new ArrayList<>();
        components.add(EComponent.PROCESSOR);
        components.add(EComponent.MAIN_BOARD);
        components.add(EComponent.HARD_DISK);
        computer.setBrokenComponents(components);
        computers.add(computer);

        //2
        computer = new Computer();
        components = new ArrayList<>();
        components.add(EComponent.MEMORY);
        components.add(EComponent.HARD_DISK);
        computer.setBrokenComponents(components);
        computers.add(computer);

        return computers;
    }

    @Override
    public Computer getComputer(List<EComponent> brokenComponents) {
        Computer computer = new Computer();
        if (brokenComponents.size() > 0) {
            computer.setBrokenComponents(brokenComponents);
        }

        return computer;
    }
}
