package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.text.Text;
import javafx.util.Callback;
import model.Model;
import model.chain.Reparation;
import model.computer.Computer;
import model.computer.EComponent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class Controller implements Initializable {
    private static Logger logger = Logger.getLogger(Controller.class.getName());
    private static final String TXT_TO_REPAIR = "to repair";
    private static final String TXT_OK = "OK";
    //This attribute is for making my drag n drop totally private
    public static DataFormat dataFormat =  new DataFormat("mycell");


    ///"slabe" ladowanie modelu na sztywno. Brak konstr.
    private Model model = Model.getInstance();
    //View mam bezposrednio z fxmla.

    List<Computer> computers = new ArrayList<>();
    /**
     * @see EComponent
     */
    List<EComponent> components = new ArrayList<>();
    /**
     * Pobieranie lancucha zaleznosci w standardowym polaczeniu.
     */
    Reparation chainOfReparation = model.getChainOfReparation();

    int selectedComputerIdx;
    Computer selectedComputer;

    int indexOfSelectedItem;
    int choosenIndexForComponent;
    boolean dragAndDropFlag = false;
    EComponent selectedComponent;

    ///FXML variable region - slabe nazwy ale juz mi sie nie chce zmieniac :D
    @FXML
    private Text txt_Processor, txt_Memory, txt_Graphic_Card, txt_Main_Board, txt_Hard_Disk;
    @FXML private ListView<Computer> lv_Computer;
    @FXML private ListView<EComponent> lv_Order;
    //CheckBoxes
    @FXML private CheckBox CB_Processor, CB_Memory, CB_Graphic_Card, CB_Main_Board, CB_Hard_Disk;
    @FXML private TextField ed_Name;
    //endregion

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("Initialize");
        initializeListeners();

        /**
         * Brak polaczenia z baza danych(nie trzeba) wiec ladowanie
         * poczatkowych danych
         */
        initTempData();
        /**
         * Ustawianie poczatkowych zmiennych
         */
        if (computers.size() > 0 && computers != null) {
            selectedComputerIdx = 0;
            selectedComputer = computers.get(selectedComputerIdx);
            whichComponentsAreBroken(computers.get(selectedComputerIdx));
        }
        /**
         * Ladowanie danych do ListView Computer
         */
        setComputersArray();
        setListViewComputer(computers);
        /**
         * Ladowanie danych do ListView Order
         */
        setComponentsArray();
        setListViewComponents(components);

    }

    //region private Methods
    private void initializeListeners() {

        lv_Order.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (lv_Order.getSelectionModel().getSelectedItem() == null) {
                    return;
                }

                indexOfSelectedItem = lv_Order.getSelectionModel().getSelectedIndex();
                selectedComponent = lv_Order.getSelectionModel().getSelectedItem();
                logger.info("indexOfSelectedItem: " + indexOfSelectedItem);


                Dragboard dragBoard = lv_Order.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(lv_Order.getSelectionModel().getSelectedItem().name());
                logger.info("Wybraniec: " + content);
                dragBoard.setContent(content);
                event.consume();
            }
        });
        lv_Order.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                Dragboard db = dragEvent.getDragboard();
                if (db.hasString()) {
                    dragEvent.acceptTransferModes(TransferMode.MOVE);
                }
                dragEvent.consume();
            }
        });
        lv_Order.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                Dragboard db = dragEvent.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                    dragAndDropFlag = true;
                    logger.info("indexOfSelectedItem: " + indexOfSelectedItem);
                    //int thisIdx = lv_Order.getItems().indexOf(db.get);
                    success = true;
                }
                dragEvent.setDropCompleted(success);
                dragEvent.consume();
            }
        });
    }



    private void initTempData() {
        computers.addAll(model.getInitComputers());
    }

    private void setListViewComputer(List<Computer> computers) {
        lv_Computer.setCellFactory(new Callback<ListView<Computer>, ListCell<Computer>>() {
            @Override
            public ListCell<Computer> call(ListView<Computer> param) {
                ListCell<Computer> cell = new ListCell<Computer>() {
                    @Override
                    protected void updateItem(Computer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText("Computer " + String.valueOf(computers.indexOf(item) + 1));
                        }
                    }
                };
                return cell;
            }
        });
    }

    private void setComputersArray() {
        ObservableList<Computer> data = FXCollections.observableArrayList(computers);
        lv_Computer.setItems(data);
    }

    private void setListViewComponents(List<EComponent> components) {
        lv_Order.setCellFactory(new Callback<ListView<EComponent>, ListCell<EComponent>>() {
            @Override
            public ListCell<EComponent> call(ListView<EComponent> param) {
                /**
                 * Wyjasnienie: DaD - Drag and Drop, cell - poszczegolna komorka ListView.
                 * indexOfSelectedItem - index cell'a ktorego item chcemy przeniesc.
                 * choosenIndexForComponent - index cell'a gdzie chcemy wsadzic przenoszony item.
                 *
                 * DaD slabo dziala dlatego gdy ktos przytrzymuje i unosi cell ustawiam w listenerach DaD
                 * flage dragAndDropFlag ktora powoduje zasloniecie updateItem (nic sie nie zreloaduje)
                 * wejscie do setOnMouseEntered i pobranie nowego indexu: choosenIndexForComponent.
                 * Czyszcze flage. Wywoluje metode ktora przemienia ORDER w lancuchu i od nowa
                 * wywoluje ta metode juz ze zmieniona kolejnoscia i wyczyszczona flaga.
                 * bez ustawionej flagi wchodzi i update'uje cala listView od zmienionej
                 * kolejnoscia listy COMPONENTS.
                 * Slabo zrobione ale dziala :)
                 * Na koncu jeszcze aktualizuje "wybor" cell'a na przeniesiony index: choosenIndexForComponent.
                 */
                ListCell<EComponent> cell = new ListCell<EComponent>() {
                    @Override
                    protected void updateItem(EComponent item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            if (dragAndDropFlag) {
                            } else {
                                logger.info("W updateItem components: " + components.toString());
                                setText(item.name());
                            }
                        }
                    }
                };
                cell.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (dragAndDropFlag) {
                            dragAndDropFlag = false;
                            choosenIndexForComponent = cell.getIndex();
                            System.out.println("index: " + choosenIndexForComponent);
                            setComponentsArrayDragAndDrop();
                        }
                    }
                });
                return cell;
            }
        });
        lv_Order.scrollTo(choosenIndexForComponent);
        //lv_Order.getFocusModel().focus(choosenIndexForComponent);
        lv_Order.getSelectionModel().select(choosenIndexForComponent);
    }

    private void setComponentsArrayDragAndDrop() {
        /**
         * nie dziala cos dla Procka, bo jest jako pierwszy i pewnie od niego zostaje wywolany lanuch
         * pozniej zmienic.
         */
        chainOfReparation.changeOrderOfItems(indexOfSelectedItem, choosenIndexForComponent);
        List<Reparation> reparations = new ArrayList<>(Reparation.getOrderOfReparations());
        List<EComponent> components1 = new ArrayList<>();
        for (Reparation reparation : reparations) {
            components1.add(reparation.getComponent());
        }
        components = new ArrayList<>(components1);
        ObservableList<EComponent> data = FXCollections.observableArrayList(components);
        lv_Order.setItems(data);
        setListViewComponents(components);
    }

    private void setComponentsArray() {
        List<Reparation> reparations = new ArrayList<>(Reparation.getOrderOfReparations());
        List<EComponent> components1 = new ArrayList<>();
        for (Reparation reparation : reparations) {
            components1.add(reparation.getComponent());
        }
        components = new ArrayList<>(components1);
        ObservableList<EComponent> data = FXCollections.observableArrayList(components);
        lv_Order.setItems(data);
    }

    private void whichComponentsAreBroken(Computer computer) {
        List<EComponent> brokenComponents = computer.getBrokenComponent();
        if (brokenComponents.contains(EComponent.PROCESSOR)) {
            txt_Processor.setText(TXT_TO_REPAIR);
        } else {
            txt_Processor.setText(TXT_OK);
        }
        if (brokenComponents.contains(EComponent.MEMORY)) {
            txt_Memory.setText(TXT_TO_REPAIR);
        } else {
            txt_Memory.setText(TXT_OK);
        }
        if (brokenComponents.contains(EComponent.GRAPHICS_CARD)) {
            txt_Graphic_Card.setText(TXT_TO_REPAIR);
        } else {
            txt_Graphic_Card.setText(TXT_OK);
        }
        if (brokenComponents.contains(EComponent.MAIN_BOARD)) {
            txt_Main_Board.setText(TXT_TO_REPAIR);
        } else {
            txt_Main_Board.setText(TXT_OK);
        }
        if (brokenComponents.contains(EComponent.HARD_DISK)) {
            txt_Hard_Disk.setText(TXT_TO_REPAIR);
        } else {
            txt_Hard_Disk.setText(TXT_OK);
        }

    }

    private void whichCheckBoxesAreSelected(Computer computer) {
        if (CB_Processor.isSelected()) {
            computer.setBrokenComponent(EComponent.PROCESSOR);
        }
        if (CB_Memory.isSelected()) {
            computer.setBrokenComponent(EComponent.MEMORY);
        }
        if (CB_Graphic_Card.isSelected()) {
            computer.setBrokenComponent(EComponent.GRAPHICS_CARD);
        }
        if (CB_Main_Board.isSelected()) {
            computer.setBrokenComponent(EComponent.MAIN_BOARD);
        }
        if (CB_Hard_Disk.isSelected()) {
            computer.setBrokenComponent(EComponent.HARD_DISK);
        }
    }

    private void clearCheckBoxes() {
        //Unselect all CB
        CB_Processor.setSelected(false); CB_Memory.setSelected(false); CB_Graphic_Card.setSelected(false);
        CB_Main_Board.setSelected(false); CB_Hard_Disk.setSelected(false);
    }

    /*private void commitChanges() {
        setComputersArray();
        setListViewComputer(computers);
        List<String> ingredients = new ArrayList<>(selectedPizza.showIngredients());
        separateCheckBoxes(ingredients);

        String ingredientsWithoutBrackets = ingredients.toString().replace("[", "").replace("]", "");
        ingredientsText.setText(ingredientsWithoutBrackets);
    }*/
    //endregion


    @FXML protected void mouseReleasedOrderLV() {
        //indexOfSelectedItem = lv_Order.getItems().;
        logger.info("indexOfSelectedItem: " + indexOfSelectedItem);
    }

    @FXML protected void mouseClickComputerLV() {
        selectedComputerIdx = lv_Computer.getSelectionModel().getSelectedIndex();
        selectedComputer = lv_Computer.getSelectionModel().getSelectedItem();
        whichComponentsAreBroken(selectedComputer);
    }

    @FXML protected void btnRepairClick() {
        chainOfReparation.repair(selectedComputer);
        whichComponentsAreBroken(selectedComputer);
    }



    @FXML protected void btnOkClick() {
        Computer computer = new Computer();
        whichCheckBoxesAreSelected(computer);
        computers.add(computer);
        selectedComputerIdx = computers.size() - 1;
        selectedComputer = computer;
        /**
         * Ustawienie ListView Computer
         */
        setComputersArray();
        setListViewComputer(computers);
        whichComponentsAreBroken(selectedComputer);
        clearCheckBoxes();
    }




}
