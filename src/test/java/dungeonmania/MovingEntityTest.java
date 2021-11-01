package dungeonmania;
import static org.junit.jupiter.api.Assertions.assertTrue;


import dungeonmania.entities.*;
import dungeonmania.entities.Character;
import dungeonmania.entities.Spider;
import dungeonmania.entities.MovingEntity;
import dungeonmania.entities.BuildableEntities.*;
import dungeonmania.entities.CollectableEntities.*;
import dungeonmania.entities.RareCollectableEntities.*;
import org.junit.jupiter.api.Test;
import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.List;
import java.util.ArrayList;

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

    @Test
    public void testSpiderSpawn() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse newDungeon = controller.newGame("advanced-2", "Standard");
        DungeonResponse tickHolder = null;
        for (int i = 0; i < 27; i++) {
            tickHolder =  controller.tick(null, Direction.LEFT);
        }
        Dungeon dung = controller.getCurrDungeon();
        List<Entity> entities = dung.getEntities();
        Position spiderPos = new Position(0, 0);
        for (Entity currEnt : entities) {
            if (currEnt.getType().equals("spider")) {
                spiderPos = currEnt.getPosition();
            }
        }
        assertTrue(spiderPos.getX() != 0);
        assertTrue(spiderPos.getY() != 0);
        System.out.printf("%d, %d", spiderPos.getX(), spiderPos.getY());
        assertTrue(entityInPlace(spiderPos, tickHolder, "spider"));
        
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
