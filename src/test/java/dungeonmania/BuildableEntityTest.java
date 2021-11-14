package dungeonmania;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class BuildableEntityTest {
    //Testing build for shield and bow
    @Test

    public void testCollectableItem() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("buildableTest", "peaceful");
        DungeonResponse tickHolder = null;
        // Picking up items to build items
        for (int i = 0; i < 12; i++) {
            tickHolder =  controller.tick(null, Direction.RIGHT);
        }
        int bow = 0;
        // Checking if bow is in buildables
        for (String buildable: tickHolder.getBuildables()) {
            if (buildable.equals("bow")) {
                bow = 1;
            }
        }
        assertTrue(bow == 1);
        // Building the bow
        tickHolder = controller.build("bow");
        // Checking if shield is in buildable
        int shield = 0;
        for (String buildable: tickHolder.getBuildables()) {
            if (buildable.equals("shield")) {
                shield = 1;
            }
        }
        assertTrue(shield == 1);
        // Building the shield
        tickHolder = controller.build("shield");
        // Checking if midnightArmour is in buildable
        int midnightArmour = 0;
        for (String buildable: tickHolder.getBuildables()) {
            if (buildable.equals("midnight_armour")) {
                midnightArmour = 1;
            }
        }
        assertTrue(midnightArmour == 1);
        // Building the midnightArmour
        tickHolder = controller.build("midnightArmour");
         // Checking if sceptre is in buildable
        int sceptre = 0;
        for (String buildable: tickHolder.getBuildables()) {
            if (buildable.equals("sceptre")) {
                sceptre = 1;
            }
        }
        assertTrue(sceptre == 1);
        // Building the midnightArmour
        tickHolder = controller.build("sceptre");
        // Checking if shield and bow are in inventory. Components are removed
        bow = 0;
        shield = 0;
        midnightArmour = 0;
        sceptre = 0;
        int arrow = 0;
        int wood = 0;
        int treasure = 0;
        int sunStone = 0;
        for (ItemResponse item: tickHolder.getInventory()) {
            if (item.getType().equals("bow")) {
                bow = 1;
            }
            if (item.getType().equals("shield")) {
                shield = 1;
            }
            if (item.getType().equals("midnight_armour")) {
                midnightArmour = 1;
            }
            if (item.getType().equals("sceptre")) {
                sceptre = 1;
            }
            if (item.getType().equals("sun_stone")) {
                sunStone = 1;
            }
            if (item.getType().equals("arrow")) {
                arrow = 1;
            }
            if (item.getType().equals("wood")) {
                wood = 1;
            }
            if (item.getType().equals("treasure")) {
                treasure = 1;
            }
        }
        assertTrue(bow == 1);
        assertTrue(shield == 1);
        assertTrue(sunStone == 1);
        assertTrue(sceptre == 1);
        assertTrue(wood == 0);
        assertTrue(treasure == 0);
        assertTrue(arrow == 0);
    }
}
