import GUI.FxUI;
import Music.BackgroundMusic;
import console.ScanException;
import core.BoardConfig;
import core.Game;
import core.GameImpl;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.*;

public class Launcher extends Application {
    private final static Logger logger = Logger.getLogger(Launcher.class.getName());
    //Logger Level:
    //OFF    : nothing will be logged
    //SEVERE : kritischer Fehler, der dazu führt, dass das Programm nicht ordnungsgemäß fortgesetzt werden kann, eventuell Programmabbruch
    //WARNING: es ist ein Fehler aufgetreten (gecatchte Fehler)
    //INFO   : Wichtige Information ( Start game, Quit game ...)
    //CONFIG : Ausgabe von Information über eine Konfiguration (Welche BotFactory wurde gewählt)
    //FINE   : Ausgabe von wichtigen Schritten im Programmfluss
    //FINER  : detailliertere Ausgabe als FINE
    //FINEST : detailliertere Ausgabe als FINER (zum Beispiel Start und Ende einer Methode)
    //ALL    : Alle Obengenante level werden in einer Date gespeichert oder ausgegeben.

    private static Launcher launcher = new Launcher();
    private static BoardConfig boardConfig = new BoardConfig();
    private Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) throws Exception {
        LogManager.getLogManager().reset();
        logger.setLevel(Level.ALL);

        //logging to the console
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.SEVERE);
        logger.addHandler(consoleHandler);

        //logging to the log.txt
        FileHandler fileHandler = new FileHandler("log.txt");
        fileHandler.setLevel(Level.FINE);
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);

        launcher.menu(args);
    }

    private void menu(String[] args) {
        System.out.println("Choose Game Mode: [1] Multithr. Console [2] Singlethr. Console [3] GUI [4] Exit");
        switch (scanner.nextInt()) {
            case 1:
                logger.log(Level.INFO, "Game Type: Multithreaded Console");
                startGameMultiThreaded(new GameImpl(true, boardConfig));
                break;
            case 2:
                logger.log(Level.INFO, "Game Type: Singlethreaded Console");
                startGameSingleThreaded(new GameImpl(false, boardConfig));
                break;
            case 3:
                logger.log(Level.INFO, "Game Type: GUI");
                Application.launch(args);
                break;
            case 4:
                logger.log(Level.INFO, "Game Menu Exit");
                System.exit(0);
        }
    }

    private static void startGameMultiThreaded(Game game) throws ScanException {
        logger.log(Level.INFO, "Start Game Multithreaded");

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    game.processInput();
                    game.run();
                } catch (ScanException e) {
                    e.printStackTrace();
                }
            }
        };

        System.out.println("Get ready to rumble!");
        timer.schedule(timerTask, 2000, 1);
        game.ui.multiThreadCommandProcess();
    }

    private static void startGameSingleThreaded(Game game) throws ScanException {
        logger.log(Level.INFO, "Start Game Singlethreaded");

        game.run();
    }

    @Override
    public void start(Stage primaryStage) throws ScanException {
        logger.log(Level.INFO, "Start GUI Game");

        FxUI fxUI = FxUI.createInstance(boardConfig.getBoardSize());
        final Game game = new GameImpl(true, boardConfig);
        //BackgroundMusic.backgroundMusic.loop();
        game.setUi(fxUI);
        fxUI.setGameImpl((GameImpl) game);
        primaryStage.setScene(fxUI);
        primaryStage.setTitle("DEEZNUTZ");
        primaryStage.setAlwaysOnTop(false);
        fxUI.getWindow().setOnCloseRequest(evt -> System.exit(-1));
        primaryStage.show();
        startGameMultiThreaded(game);
    }

}
