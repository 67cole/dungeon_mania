package dungeonmania;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MovingEntityTest {
    
    // Check if newGame imports the json data properly and adds everything into the entitieslist
    @Test

    public void testMercenarySpawn() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse newDungeon = controller.newGame("advanced-2", "Peaceful");
        Position toBePos = new Position(1,1);
        assertTrue(entityInPlace(toBePos, newDungeon, "player"));
        DungeonResponse tickHolder = null;
        for (int i = 0; i < 75; i++) {
            tickHolder =  controller.tick(null, Direction.DOWN);
        }

        assertTrue(entityInPlace(toBePos, tickHolder, "mercenary"));

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
