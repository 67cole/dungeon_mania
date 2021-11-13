package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

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
        DungeonResponse newDungeon = controller.newGame("advanced", "peaceful");
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
        
        DungeonResponse newDungeon = controller.newGame("advanced", "peaceful");
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
        DungeonResponse newDungeon = controller.newGame("boulders", "peaceful");
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
        DungeonResponse newDungeon = controller.newGame("advanced-2", "peaceful");
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
        DungeonResponse newDungeon = controller.newGame("portals", "peaceful");
        assertTrue(entityInPlace(new Position(0,0), newDungeon, "player"));

        DungeonResponse tickHolder = controller.tick(null, Direction.RIGHT);
        assertTrue(entityInPlace(new Position(5,0), tickHolder, "player"));
    
    }

    // Check if Mercenary would be slowly down by swamp tiles
    @Test
    public void testSwampTilesMercenary() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("swamp_tile", "standard");

        DungeonResponse tickHolder = null;

        tickHolder =  controller.tick(null, Direction.UP);
        Position close = new Position(1,4);
        Position pos2 = new Position(2,4);
        assertTrue(entityInPlace(close, tickHolder, "mercenary"));
        assertTrue(entityInPlace(pos2, tickHolder, "mercenary"));

        tickHolder =  controller.tick(null, Direction.UP);
        assertTrue(entityInPlace(close, tickHolder, "mercenary"));
        assertTrue(entityInPlace(pos2, tickHolder, "mercenary"));

        tickHolder =  controller.tick(null, Direction.UP);
        assertTrue(entityInPlace(close, tickHolder, "mercenary"));
        assertTrue(entityInPlace(pos2, tickHolder, "mercenary"));
    
    }

    // Check if Zombies would be slowly down by swamp tiles
    @Test
    public void testSwampTilesZombie() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse newGame = controller.newGame("swamp_tile", "standard");

        DungeonResponse tickHolder = null;
        Position zombieSpawn = new Position(7,7);
        assertTrue(entityInPlace(zombieSpawn, newGame, "zombie_toast"));
        List<Position> adjacents = zombieSpawn.getAdjacentPositions();

        //Despite 3 ticks passing, the zombie should still be at the 4 positions
        //That are cardinally adjacent to its spawn position
        tickHolder =  controller.tick(null, Direction.UP);
        tickHolder =  controller.tick(null, Direction.UP);
        tickHolder =  controller.tick(null, Direction.UP);

        assertTrue(entityInPlace(adjacents.get(1), tickHolder, "zombie_toast") ||
        entityInPlace(adjacents.get(3), tickHolder, "zombie_toast") ||
        entityInPlace(adjacents.get(5), tickHolder, "zombie_toast") ||
        entityInPlace(adjacents.get(7), tickHolder, "zombie_toast"));
    
    }
    // Check if hydra would be slowly down by swamp tiles
    @Test
    public void testSwampTilesHydra() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse newGame = controller.newGame("swamp_tile", "hard");

        DungeonResponse tickHolder = null;
        Position hydraSpawn = new Position(7,5);
        assertTrue(entityInPlace(hydraSpawn, newGame, "hydra"));
        List<Position> adjacents = hydraSpawn.getAdjacentPositions();
        tickHolder =  controller.tick(null, Direction.UP);

        //Despite 3 ticks passing, the hydra should still be at the 4 positions
        //That are cardinally adjacent to its spawn position
        tickHolder =  controller.tick(null, Direction.UP);
        tickHolder =  controller.tick(null, Direction.UP);
        tickHolder =  controller.tick(null, Direction.UP);
        assertTrue(entityInPlace(adjacents.get(1), tickHolder, "hydra") ||
        entityInPlace(adjacents.get(3), tickHolder, "hydra") ||
        entityInPlace(adjacents.get(5), tickHolder, "hydra") ||
        entityInPlace(adjacents.get(7), tickHolder, "hydra"));
    }
    

    // Check if Spiders would be slowly down by swamp tiles
    @Test
    public void testSwampTilesSpider() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse newGame = controller.newGame("swamp_tile", "standard");

        DungeonResponse tickHolder = null;
        Position spiderSpawn = new Position(8,8);
        assertTrue(entityInPlace(spiderSpawn, newGame, "spider"));
        tickHolder =  controller.tick(null, Direction.UP);
        //When spiders spawn, they always move up one square
        Position pos2 = new Position(8,7);
        assertTrue(entityInPlace(pos2, tickHolder, "spider"));
        //Since theres a swamp tile there, the spider should be stuck there for 4 ticks
        tickHolder =  controller.tick(null, Direction.UP);
        assertTrue(entityInPlace(pos2, tickHolder, "spider"));

        tickHolder =  controller.tick(null, Direction.UP);
        assertTrue(entityInPlace(pos2, tickHolder, "spider"));
    }

    // Check if assassins would be slowly down by swamp tiles
    @Test
    public void testSwampTilesAssassin() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse newGame = controller.newGame("swamp_tile", "standard");

        DungeonResponse tickHolder = null;
        Position assassinSpawn = new Position(3,5);
        assertTrue(entityInPlace(assassinSpawn, newGame, "assassin"));
        tickHolder =  controller.tick(null, Direction.UP);
        Position pos2 = new Position(3,4);
        assertTrue(entityInPlace(pos2, tickHolder, "assassin"));

        tickHolder =  controller.tick(null, Direction.UP);
        assertTrue(entityInPlace(pos2, tickHolder, "assassin"));
    }

    // Check if lightbulb would turn on when a switch is right next to it
    @Test
    public void testLogicSwitchBulb() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse  newGame = controller.newGame("light_switch", "standard");
        DungeonResponse tickHolder = null;
        //Check if lightbulb is off prior to switch turning on
        Position bulbPos = new Position(9,9);
        assertTrue(entityInPlace(new Position(1,1), newGame, "player"));
        assertTrue(entityInPlace(bulbPos, newGame, "light_bulb_off"));
        tickHolder = controller.tick(null, Direction.RIGHT);

        for (int i = 0; i < 7; i++) {
            tickHolder = controller.tick(null, Direction.DOWN);
        }
        for (int j = 0; j < 6; j++) {
            tickHolder = controller.tick(null, Direction.RIGHT);
        }
        assertTrue(entityInPlace(new Position(8,8), tickHolder, "player"));

        assertTrue(entityInPlace(bulbPos, tickHolder, "light_bulb_on"));
    }

    
    // Check if switch doors block movement and also if they will allow momement once unlocked
    @Test
    public void testLogicSwitchDoor() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse  newGame = controller.newGame("light_switch", "standard");
        DungeonResponse tickHolder = null;
        //Check if lightbulb is off prior to switch turning on
        Position doorPos = new Position(9,7);
        assertTrue(entityInPlace(new Position(1,1), newGame, "player"));
        assertTrue(entityInPlace(doorPos, newGame, "switch_door"));
        tickHolder = controller.tick(null, Direction.RIGHT);

        for (int i = 0; i < 6; i++) {
            tickHolder = controller.tick(null, Direction.DOWN);
        }
        for (int j = 0; j < 6; j++) {
            tickHolder = controller.tick(null, Direction.RIGHT);
        }
        //First check if the door stops movement when locked
        assertTrue(entityInPlace(new Position(8,7), tickHolder, "player"));
        tickHolder = controller.tick(null, Direction.RIGHT);
        assertTrue(entityInPlace(new Position(8,7), tickHolder, "player"));

        //Now unlock the door and check if movement is still stopped
        tickHolder = controller.tick(null, Direction.LEFT);
        tickHolder = controller.tick(null, Direction.DOWN);
        tickHolder = controller.tick(null, Direction.RIGHT);

        //Check if player can walk through door
        tickHolder = controller.tick(null, Direction.UP);
        tickHolder = controller.tick(null, Direction.RIGHT);
        assertTrue(entityInPlace(new Position(9,7), tickHolder, "player"));
    }

    @Test
    public void testWire() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("light_switch", "standard");
        DungeonResponse tickHolder = null;
        for (int i = 0; i < 5; i++) {
            tickHolder = controller.tick(null, Direction.DOWN);
        }
        Position playerPos = new Position(1,3);
        Position bulbPos = new Position(1,2);
        Position bulbPos2 = new Position(5,2);
        //Check that door and lightbulbs are turned off 
        assertTrue(entityInPlace(playerPos, tickHolder, "player"));
        assertTrue(entityInPlace(bulbPos, tickHolder, "light_bulb_off"));
        assertTrue(entityInPlace(bulbPos2, tickHolder, "light_bulb_off"));
        for (int i = 0; i < 2; i++) {
            tickHolder = controller.tick(null, Direction.UP);
        }
        tickHolder = controller.tick(null, Direction.RIGHT);
        //Check that lightbulbs are on after boulder has been placed onto switch
        assertTrue(entityInPlace(bulbPos, tickHolder, "light_bulb_on"));
        assertTrue(entityInPlace(bulbPos2, tickHolder, "light_bulb_on"));

        for (int i = 0; i < 3; i++) {
            tickHolder = controller.tick(null, Direction.DOWN);
        }
        tickHolder = controller.tick(null, Direction.RIGHT);
        assertTrue(entityInPlace(new Position(3,4), tickHolder, "player"));

    }
    /**
     * 
     * 
     *              HELPER FUNCTIONS
     * 
     * 
     */


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
