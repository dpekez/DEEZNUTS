import botapi.BotControllerFactory;
import botimpl.mozartuss.BrainFactory;
import core.Board;
import core.EntityContext;
import core.XY;
import entities.MasterSquirrel;
import entities.MasterSquirrelBot;
import entities.MiniSquirrelBot;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ControllerContextTest {

    MasterSquirrelBot masterBot;
    MasterSquirrel master;
    MiniSquirrelBot miniBot;
    EntityContext context;
    XY spawnPositionMasterBot = new XY(40, 30);
    XY spawnPositionMiniBot = new XY(42, 35);
    Board board;
    MasterSquirrelBot.ControllerContextImpl viewMaster;
    BotControllerFactory botControllerFactory;
    BrainFactory factory;

    @Before
    public void init() {
        masterBot = new MasterSquirrelBot(spawnPositionMasterBot, factory);

        board.createBots();
    }

    public void tearDown() {
        viewMaster = null;
    }

    @Test
    public void locate() {
        viewMaster = masterBot.new ControllerContextImpl(context, masterBot);

        assertEquals(spawnPositionMasterBot, viewMaster.locate());
    }


}
