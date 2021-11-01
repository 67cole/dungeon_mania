package dungeonmania;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.exceptions.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class MiscTest {
    @Test
    public void testInvalidLoadGame() {

        DungeonManiaController controller = new DungeonManiaController();

        assertThrows(IllegalArgumentException.class, () -> 
        controller.newGame("xd", "peaceful"));
    }

    @Test
    public void testInvalidLoadGameGamemode() {

        DungeonManiaController controller = new DungeonManiaController();

        assertThrows(IllegalArgumentException.class, () -> 
        controller.newGame("advanced", "LMFAODOASDOASSINJGFK"));
    }

    @Test
    public void testTickItemUsedException() {

        DungeonManiaController controller = new DungeonManiaController();

        controller.newGame("advanced.json", "Standard");

        assertThrows(IllegalArgumentException.class, () -> 
        controller.tick("xd", Direction.UP));
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