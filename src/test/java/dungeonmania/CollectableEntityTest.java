package dungeonmania;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class CollectableEntityTest {
    // Check if inventory actually contains the items
    @Test

    public void testCollectableItem() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse newDungeon = controller.newGame("advanced-2", "Peaceful");
        DungeonResponse tickHolder = null;
        for (int i = 0; i < 5; i++) {
            tickHolder =  controller.tick(null, Direction.RIGHT);
        }
        int sword = 0;
        for (ItemResponse item: tickHolder.getInventory()) {
            if (item.getType().equals("sword")) {
                sword = 1;
            }
        }
        /*
        for (int i = 0; i < 5; i++) {
            tickHolder =  controller.tick(null, Direction.RIGHT);
        }
        */
        assertTrue(sword == 1);
    }
}
