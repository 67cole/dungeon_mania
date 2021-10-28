package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;
public class StaticEntityTest {
    @Test
    public void testEntitiesAddedToDungeon() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("portals", null);
    
    }

    @Test
    public void testPortalsInDungeon() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("portals", null);
        controller.tick(null, Direction.RIGHT);
    
    }
    
}
