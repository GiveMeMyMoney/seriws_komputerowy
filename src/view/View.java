package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.computer.Computer;
import model.computer.EComponent;
import model.chain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class View extends Application {
    private static Logger logger = Logger.getLogger(View.class.getName());

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 410.0, 475.0));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);

        /*Computer computer = new Computer();
        List<EComponent> components = new ArrayList<>();
        components.add(EComponent.PROCESSOR);
        components.add(EComponent.MAIN_BOARD);
        components.add(EComponent.HARD_DISK);

        computer.setBrokenComponents(components);

        Reparation processorReparation = new ProcessorReparation();
        Reparation memoryReparation = new MemoryReparation();
        Reparation graphicsReparation = new GraphicsReparation();
        Reparation mainBoardReparation = new MainBoardReparation();
        Reparation hardDiskReparation = new HardDiskReparation();

        //return processorReparation

        processorReparation.setNextReparation(memoryReparation);
        memoryReparation.setNextReparation(graphicsReparation);
        graphicsReparation.setNextReparation(mainBoardReparation);
        mainBoardReparation.setNextReparation(hardDiskReparation);

        processorReparation.changeOrderOfItems(1, 3);



        processorReparation.repair(computer);*/

        //logger.info(computer.toString());



    }
}
