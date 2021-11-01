package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
public class StaticEntityTest {

    // Check if newGame imports the json data properly and adds everything into the entitieslist
    @Test
    public void testEntitiesAdded() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse newDungeon = controller.newGame("advanced", "Peaceful");
        Position toBePos = new Position(0,0);
        assertTrue(entityInPlace(toBePos, newDungeon, "wall"));

        toBePos = new Position(5,0);
        assertTrue(entityInPlace(toBePos, newDungeon, "wall"));
        
        toBePos = new Position(1,1);
        assertTrue(entityInPlace(toBePos, newDungeon, "player"));

        toBePos = new Position(7,10);
        assertTrue(entityInPlace(toBePos, newDungeon, "treasure"));

        toBePos = new Position(11,10);
        assertTrue(entityInPlace(toBePos, newDungeon, "invincibility_potion"));
    
    }

     // Check if the character would be stopped from moving due to walls
     @Test
     public void testWallFunction() {
        DungeonManiaController controller = new DungeonManiaController();
        
        DungeonResponse newDungeon = controller.newGame("advanced", "Peaceful");
        Position toBePos = new Position(1,1);
        assertTrue(entityInPlace(toBePos, newDungeon, "player"));

        DungeonResponse tickHolder =  controller.tick(null, Direction.DOWN);

        toBePos =  new Position(1,2);
        assertTrue(entityInPlace(toBePos, tickHolder, "player"));

        tickHolder =  controller.tick(null, Direction.DOWN);

        assertTrue(entityInPlace(new Position(1,3), tickHolder, "player"));
        
        tickHolder =  controller.tick(null, Direction.LEFT);
        
        assertTrue(entityInPlace(new Position(1,3), tickHolder, "player"));
     }

    // Check if the character could move boulders, also tests if the character would be 
    // stopped if there are two boulders side by side
    @Test
    public void testBouldersInDungeon() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse newDungeon = controller.newGame("boulders", "Peaceful");
        assertTrue(entityInPlace(new Position(2,2), newDungeon, "player"));

        DungeonResponse tickHolder =  controller.tick(null, Direction.RIGHT);
        assertTrue(entityInPlace(new Position(3,2), tickHolder, "player"));
        tickHolder =  controller.tick(null, Direction.RIGHT);
        assertTrue(entityInPlace(new Position(4,2), tickHolder, "player"));
        tickHolder =  controller.tick(null, Direction.DOWN);

        assertTrue(entityInPlace(new Position(4,2), tickHolder, "player"));
    
    }

    
    // Check if the character would be blocked by locked doors, also checks if 
    // character could move through unlocked doors.
    @Test
    public void testDoorFunction() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse newDungeon = controller.newGame("advanced-2", "Peaceful");
        assertTrue(entityInPlace(new Position(1,1), newDungeon, "player"));
        DungeonResponse tickHolder = null;
        for(int i = 1; i < 9; i++) {
            tickHolder =  controller.tick(null, Direction.DOWN);    
        }
        assertTrue(entityInPlace(new Position(1,9), tickHolder, "player"));
        for(int i = 1; i < 4; i++) {
            tickHolder =  controller.tick(null, Direction.RIGHT);    
        }
        assertTrue(entityInPlace(new Position(4,9), tickHolder, "player"));
        tickHolder =  controller.tick(null, Direction.DOWN);   
        assertTrue(entityInPlace(new Position(4,9), tickHolder, "player"));

        for(int i = 1; i < 4; i++) {
            tickHolder =  controller.tick(null, Direction.RIGHT);    
        }
        tickHolder =  controller.tick(null, Direction.DOWN);   
        assertTrue(entityInPlace(new Position(7,10), tickHolder, "player"));
        for(int i = 1; i < 5; i++) {
            tickHolder =  controller.tick(null, Direction.RIGHT);    
        }
        assertTrue(entityInPlace(new Position(11,10), tickHolder, "player"));
        for(int i = 1; i < 5; i++) {
            tickHolder =  controller.tick(null, Direction.RIGHT);    
        }
        tickHolder =  controller.tick(null, Direction.UP);  
        assertTrue(entityInPlace(new Position(15,9), tickHolder, "player"));
    }

    // Check if the character would be teleported by portals
    @Test
    public void testPortalsInDungeon() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse newDungeon = controller.newGame("portals", "Peaceful");
        assertTrue(entityInPlace(new Position(0,0), newDungeon, "player"));

        DungeonResponse tickHolder = controller.tick(null, Direction.RIGHT);
        assertTrue(entityInPlace(new Position(4,0), tickHolder, "player"));
    
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
