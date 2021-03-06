package seedu.schedar.ui;

import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.schedar.commons.core.GuiSettings;
import seedu.schedar.commons.core.LogsCenter;
import seedu.schedar.logic.Logic;
import seedu.schedar.logic.commands.CommandResult;
import seedu.schedar.logic.commands.exceptions.CommandException;
import seedu.schedar.logic.parser.exceptions.ParseException;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private TaskListPanel taskListPanel;
    private SundayPanel sundayPanel;
    private MondayPanel mondayPanel;
    private TuesdayPanel tuesdayPanel;
    private WednesdayPanel wednesdayPanel;
    private ThursdayPanel thursdayPanel;
    private FridayPanel fridayPanel;
    private SaturdayPanel saturdayPanel;
    private TodoPanel todoPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane taskListPanelPlaceholder;

    @FXML
    private StackPane sundayPanelPlaceholder;

    @FXML
    private StackPane mondayPanelPlaceholder;

    @FXML
    private StackPane tuesdayPanelPlaceholder;

    @FXML
    private StackPane wednesdayPanelPlaceholder;

    @FXML
    private StackPane thursdayPanelPlaceholder;

    @FXML
    private StackPane fridayPanelPlaceholder;

    @FXML
    private StackPane saturdayPanelPlaceholder;

    @FXML
    private StackPane todoPanelPlaceholder;


    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        taskListPanel = new TaskListPanel(logic.getFilteredTaskList());
        taskListPanelPlaceholder.getChildren().add(taskListPanel.getRoot());

        sundayPanel = new SundayPanel(logic.getFilteredTaskList());
        sundayPanelPlaceholder.getChildren().add(sundayPanel.getRoot());

        mondayPanel = new MondayPanel(logic.getFilteredTaskList());
        mondayPanelPlaceholder.getChildren().add(mondayPanel.getRoot());

        tuesdayPanel = new TuesdayPanel(logic.getFilteredTaskList());
        tuesdayPanelPlaceholder.getChildren().add(tuesdayPanel.getRoot());

        wednesdayPanel = new WednesdayPanel(logic.getFilteredTaskList());
        wednesdayPanelPlaceholder.getChildren().add(wednesdayPanel.getRoot());

        thursdayPanel = new ThursdayPanel(logic.getFilteredTaskList());
        thursdayPanelPlaceholder.getChildren().add(thursdayPanel.getRoot());

        fridayPanel = new FridayPanel(logic.getFilteredTaskList());
        fridayPanelPlaceholder.getChildren().add(fridayPanel.getRoot());

        saturdayPanel = new SaturdayPanel(logic.getFilteredTaskList());
        saturdayPanelPlaceholder.getChildren().add(saturdayPanel.getRoot());

        todoPanel = new TodoPanel(logic.getFilteredTaskList());
        todoPanelPlaceholder.getChildren().add(todoPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getTaskManagerFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    public TaskListPanel getTaskListPanel() {
        return taskListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.schedar.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }
}
