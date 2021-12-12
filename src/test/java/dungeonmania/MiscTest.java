package dungeonmania;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.exceptions.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class MiscTest {
    @Test
    public void testInvalidLoadGameGamemode() {

        DungeonManiaController controller = new DungeonManiaController();

        assertThrows(IllegalArgumentException.class, () -> 
        controller.newGame("advanced", "LMFAODOASDOASSINJGFK"));
    }

    @Test
    public void testTickItemUsedException() {

        DungeonManiaController controller = new DungeonManiaController();

        controller.newGame("advanced", "standard");

        assertThrows(IllegalArgumentException.class, () -> 
        controller.tick("xd", Direction.UP));
    }

    @Test
    public void testSaveGameLoadGame() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("advanced", "standard");
        controller.tick(null, Direction.RIGHT);
        controller.saveGame("testSave");
        DungeonResponse loadedGame = controller.loadGame("testSave");
        Position toBePos = new Position(2,1);
        assertTrue(entityInPlace(toBePos, loadedGame, "player"));

        controller.clearDatabase();
    }

    @Test
    public void testLoadGameInvalidId() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("advanced", "standard");
        controller.tick(null, Direction.RIGHT);
        controller.saveGame("testSave");

        assertThrows(IllegalArgumentException.class, () -> 
        controller.loadGame("xd"));

        controller.clearDatabase();
    }

    @Test
    public void testLoadGameItemPicked() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("advanced-2", "standard");
        for (int i = 0; i < 10; i++) {
            controller.tick(null, Direction.RIGHT);
        }
        controller.saveGame("testSave");
        DungeonResponse loadedGame = controller.loadGame("testSave");
        Position toBePos = new Position(11,1);
        assertTrue(entityInPlace(toBePos, loadedGame, "player"));
        int sword = 0;
        for (ItemResponse item: loadedGame.getInventory()) {
            if (item.getType().equals("sword")) {
                sword = 1;
            }
        }
        assertTrue(sword == 1);
        controller.clearDatabase();
    }

    @Test
    public void testTimeTurner() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse loadedGame;
        controller.newGame("timeTurner", "standard");
        for (int i = 0; i < 4; i++) {
            loadedGame = controller.tick(null, Direction.RIGHT);
        }
        loadedGame = controller.tick(null, Direction.RIGHT);
        int sword = 0;
        for (ItemResponse item: loadedGame.getInventory()) {
            if (item.getType().equals("sword")) {
                sword = 1;
            }
        }
        assertTrue(sword == 1);

        loadedGame = controller.rewind(5);
        Position toBePos = new Position(1,1);
        assertTrue(entityInPlace(toBePos, loadedGame, "player"));
        int newSword = 0;
        for (ItemResponse item: loadedGame.getInventory()) {
            if (item.getType().equals("sword")) {
                newSword = 1;
            }
        }

        assertTrue(newSword == 0);
    }

    @Test
    public void testRewindExceptions() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("timeTurner", "standard");
        for (int i = 0; i < 4; i++) {
            DungeonResponse loadedGame = controller.tick(null, Direction.RIGHT);
        }

        assertThrows(IllegalArgumentException.class, () -> 
        controller.rewind(-1));

        assertThrows(IllegalArgumentException.class, () -> 
        controller.rewind(10));
    }


    /**
     * Helper function that returns true if the entity type is on the same position
     * as whats given in the param 
     * @param pos
     * @param main
     * @param type
     * @return boolean
     */
    public boolean entityInPlace(Position pos, DungeonResponse main, String type) {
        for (EntityResponse entity: main.getEntities()) {
            if (entity.getPosition().equals(pos) && entity.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }
}