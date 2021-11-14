package dungeonmania;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.List;
import java.util.ArrayList;
import java.util.Currency;

public class MovingEntityTest {
    // Checks the movement of the player
    @Test 
    public void testPlayerMovement() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse newDungeon = controller.newGame("testRoom", "standard");
        Position toBePos = new Position(1,1);
        assertTrue(entityInPlace(toBePos, newDungeon, "player"));

        DungeonResponse tickHolder = null;

        tickHolder =  controller.tick(null, Direction.DOWN);
        Position moveDown = new Position(1,2);
        assertTrue(entityInPlace(moveDown, tickHolder, "player"));

        tickHolder =  controller.tick(null, Direction.RIGHT);
        Position moveRight = new Position(2,2);
        assertTrue(entityInPlace(moveRight, tickHolder, "player"));

        tickHolder =  controller.tick(null, Direction.RIGHT);
        Position moveRight2 = new Position(3,2);
        assertTrue(entityInPlace(moveRight2, tickHolder, "player"));

        tickHolder =  controller.tick(null, Direction.UP);
        Position moveUp = new Position(3,1);
        assertTrue(entityInPlace(moveUp, tickHolder, "player"));

        tickHolder =  controller.tick(null, Direction.LEFT);
        Position moveLeft = new Position(2,1);
        assertTrue(entityInPlace(moveLeft, tickHolder, "player"));
    }
    
    
    // Check if newGame imports the json data properly and adds everything into the entitieslist
    @Test
    public void testMercenarySpawn() {
        DungeonManiaController controll = new DungeonManiaController();
        DungeonResponse newDungeon = controll.newGame("advanced-2", "peaceful");
        Position toBePos = new Position(1,1);
        assertTrue(entityInPlace(toBePos, newDungeon, "player"));
        DungeonResponse tickHolder = null;
        for (int i = 0; i < 40; i++) {
            tickHolder =  controll.tick(null, Direction.DOWN);
        }
        assertTrue(entityInPlace(toBePos, tickHolder, "mercenary"));

    }

    // Check if zombie spawns in every 20 tick on Standard Difficulty
    @Test
    public void testZombieSpawnStandard() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse newDungeon = controller.newGame("testRoom", "standard");
        Position toBePos = new Position(1,1);
        assertTrue(entityInPlace(toBePos, newDungeon, "player"));
        DungeonResponse tickHolder = null;
        
        for (int i = 0; i < 20; i++) {
            tickHolder =  controller.tick(null, Direction.DOWN);
        }

        Position zombieSpawn = new Position(3,3);

        assertTrue(entityInPlace(zombieSpawn, tickHolder, "zombie_toast"));
    }

    // Check if zombie spawns in every 15 tick on Hard Difficulty
    @Test
    public void testZombieSpawnHard() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse newDungeon = controller.newGame("testRoom", "hard");
        Position toBePos = new Position(1,1);
        assertTrue(entityInPlace(toBePos, newDungeon, "player"));
        DungeonResponse tickHolder = null;
        
        for (int i = 0; i < 15; i++) {
            tickHolder =  controller.tick(null, Direction.DOWN);
        }

        Position zombieSpawn = new Position(3,3);

        assertTrue(entityInPlace(zombieSpawn, tickHolder, "zombie_toast"));
    }

    // Checking the zombie movement - it should move randomly in any adjacent square
    @Test
    public void testZombieMovement() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("testRoom", "standard");

        DungeonResponse tickHolder = null;

        for (int i = 0; i < 20; i++) {
            tickHolder =  controller.tick(null, Direction.UP);
        }

        Position zombieSpawn = new Position(3,3);
        assertTrue(entityInPlace(zombieSpawn, tickHolder, "zombie_toast"));

        tickHolder =  controller.tick(null, Direction.UP);
        List<Position> adjacents = zombieSpawn.getAdjacentPositions();

        assertTrue(entityInPlace(adjacents.get(1), tickHolder, "zombie_toast") ||
        entityInPlace(adjacents.get(3), tickHolder, "zombie_toast") ||
        entityInPlace(adjacents.get(5), tickHolder, "zombie_toast") ||
        entityInPlace(adjacents.get(7), tickHolder, "zombie_toast"));
    }


    // Check the movement of the mercenary - should be moving towards player
    @Test
    public void testMercenaryMovement() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("testRoom", "standard");

        DungeonResponse tickHolder = null;
        
        tickHolder =  controller.tick(null, Direction.UP);
        Position close = new Position(5,4);
        
        assertTrue(entityInPlace(close, tickHolder, "mercenary"));

        tickHolder =  controller.tick(null, Direction.UP);
        Position close2 = new Position(5, 3);
        assertTrue(entityInPlace(close2, tickHolder, "mercenary"));
    }

    // Checking the battle radius of the mercenary - when in battle, it should move
    // twice as fast (or again)
    @Test
    public void testMercenaryBattleRadius() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("testRoom", "standard");

        DungeonResponse tickHolder = null;

        tickHolder =  controller.tick(null, Direction.RIGHT);
        Position close = new Position(5,4);
        assertTrue(entityInPlace(close, tickHolder, "mercenary"));

        tickHolder =  controller.tick(null, Direction.RIGHT);
        Position moveFaster = new Position(5,2);
        
        assertTrue(entityInPlace(moveFaster, tickHolder, "mercenary"));
    }

    // Testing the movement of enemies when player drinks an invincibility potion 
    @Test 
    public void testMovementofInvincibilityPotion() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("testRoom", "standard");

        DungeonResponse tickHolder = null;
        tickHolder =  controller.tick(null, Direction.DOWN);
        tickHolder =  controller.tick(findItemID(tickHolder, "invincibility_potion"), Direction.NONE);

        tickHolder =  controller.tick(null, Direction.UP);
        Position moveAwayM1 = new Position(4,2);
        Position moveAwayM2 = new Position(6,3);
        assertTrue(entityInPlace(moveAwayM1, tickHolder, "mercenary"));
        assertTrue(entityInPlace(moveAwayM2, tickHolder, "mercenary"));

        tickHolder =  controller.tick(null, Direction.UP);
        Position moveAwayM11 = new Position(5,2);
        Position moveAwayM21 = new Position(6,4);
        assertTrue(entityInPlace(moveAwayM11, tickHolder, "mercenary"));
        assertTrue(entityInPlace(moveAwayM21, tickHolder, "mercenary"));

        tickHolder =  controller.tick(null, Direction.UP);
        Position moveAwayM12 = new Position(6,2);
        Position moveAwayM22 = new Position(6,5);
        assertTrue(entityInPlace(moveAwayM12, tickHolder, "mercenary"));
        assertTrue(entityInPlace(moveAwayM22, tickHolder, "mercenary"));

        tickHolder =  controller.tick(null, Direction.UP);
        Position moveAwayM13 = new Position(6,3);
        Position moveAwayM23 = new Position(6,6);
        assertTrue(entityInPlace(moveAwayM13, tickHolder, "mercenary"));
        assertTrue(entityInPlace(moveAwayM23, tickHolder, "mercenary"));

        tickHolder =  controller.tick(null, Direction.UP);
        Position moveAwayM14 = new Position(6,4);
        Position moveAwayM24 = new Position(6,7);
        assertTrue(entityInPlace(moveAwayM14, tickHolder, "mercenary"));
        assertTrue(entityInPlace(moveAwayM24, tickHolder, "mercenary"));

        tickHolder =  controller.tick(null, Direction.UP);
        Position moveAwayM15 = new Position(6,5);
        Position moveAwayM25 = new Position(6,8);
        assertTrue(entityInPlace(moveAwayM15, tickHolder, "mercenary"));
        assertTrue(entityInPlace(moveAwayM25, tickHolder, "mercenary"));

        tickHolder =  controller.tick(null, Direction.UP);
        Position moveAwayM16 = new Position(6,6);
        Position moveAwayM26 = new Position(6,9);
        assertTrue(entityInPlace(moveAwayM16, tickHolder, "mercenary"));
        assertTrue(entityInPlace(moveAwayM26, tickHolder, "mercenary"));

        tickHolder =  controller.tick(null, Direction.UP);
        Position moveAwayM17 = new Position(6,7);
        Position moveAwayM27 = new Position(6,9);
        assertTrue(entityInPlace(moveAwayM17, tickHolder, "mercenary"));
        assertTrue(entityInPlace(moveAwayM27, tickHolder, "mercenary"));

        tickHolder =  controller.tick(null, Direction.UP);
        Position moveAwayM18 = new Position(6,8);
        Position moveAwayM28 = new Position(6,9);
        assertTrue(entityInPlace(moveAwayM18, tickHolder, "mercenary"));
        assertTrue(entityInPlace(moveAwayM28, tickHolder, "mercenary"));

        // This is the end of the invincibility potion tick
        tickHolder =  controller.tick(null, Direction.UP);
        Position moveAwayM19 = new Position(6,7);
        Position moveAwayM29 = new Position(6,8);
        assertTrue(entityInPlace(moveAwayM19, tickHolder, "mercenary"));
        assertTrue(entityInPlace(moveAwayM29, tickHolder, "mercenary"));
    }

    // Checks whether or not the mercenary attacks the player in peaceful
    @Test
    public void mercenaryPeaceful() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse newDungeon = controller.newGame("testRoom", "peaceful");
        Position toBePos = new Position(1,1);
        assertTrue(entityInPlace(toBePos, newDungeon, "player"));
        DungeonResponse tickHolder = null;
        
        for (int i = 0; i < 15; i++) {
            tickHolder =  controller.tick(null, Direction.UP);
        }

        assertTrue(entityInPlace(toBePos, tickHolder, "player"));
        assertTrue(entityInPlace(toBePos, tickHolder, "mercenary"));
    }



    @Test
    public void testSpiderSpawn() {
        DungeonManiaController control = new DungeonManiaController();
        DungeonResponse newDungeon = control.newGame("advanced-2", "standard");
        DungeonResponse tickHolder = null;
        for (int i = 0; i < 25; i++) {
            tickHolder =  control.tick(null, Direction.LEFT);
        }
        Dungeon dung = control.getCurrDungeon();
        List<Entity> entities = dung.getEntities();
        Position spiderPos = new Position(0, 0);
        for (Entity currEnt : entities) {
            if (currEnt.getType().equals("spider")) {
                spiderPos = currEnt.getPosition();
            }
        }
        assertTrue(spiderPos.getX() != 0);
        assertTrue(spiderPos.getY() != 0);
        assertTrue(entityInPlace(spiderPos, tickHolder, "spider"));
        
    }

    @Test
    public void testSpiderDontSpawnOnBoulder() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse newDungeon = controller.newGame("advanced-2", "standard");
        DungeonResponse tickHolder = null;
        for (int i = 0; i < 25; i++) {
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
        List<Entity> boulderList = new ArrayList<Entity>();
        for (Entity currEnt : entities) {
            if (currEnt.getType().equals("boulder")) boulderList.add(currEnt);
        }
        for (Entity currBoulder : boulderList) {
            assertFalse(entityInPlace(currBoulder.getPosition(), newDungeon, "spider"));
        }

    }

    @Test
    public void testSpiderDontSpawnUnderBoulder() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse newDungeon = controller.newGame("advanced-2", "standard");
        DungeonResponse tickHolder = null;
        for (int i = 0; i < 25; i++) {
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
        List<Entity> boulderList = new ArrayList<Entity>();
        for (Entity currEnt : entities) {
            if (currEnt.getType().equals("boulder")) boulderList.add(currEnt);
        }
        for (Entity currBoulder : boulderList) {
            assertFalse(entityInPlace(currBoulder.getPosition().translateBy(0, 1), newDungeon, "spider"));
        }

    }
    
    @Test
    public void testSpiderReverseDirection() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse newDungeon = controller.newGame("spiderDungeon", "standard");
        DungeonResponse tickHolder = null;
        

        Dungeon dung = controller.getCurrDungeon();
        List<Entity> entities = dung.getEntities();
        Position spiderPos = new Position(0, 0);
        for (Entity currEnt : entities) {
            if (currEnt.getType().equals("spider")) {
                spiderPos = currEnt.getPosition();
            }
        }
        tickHolder = controller.tick(null, Direction.LEFT);
        assertTrue(entityInPlace(spiderPos.translateBy(Direction.UP), tickHolder, "spider"));
        
        tickHolder = controller.tick(null, Direction.LEFT);
        assertTrue(entityInPlace(spiderPos.translateBy(Direction.UP), tickHolder, "spider"));
        tickHolder = controller.tick(null, Direction.LEFT);
        assertTrue(entityInPlace(spiderPos.translateBy(Direction.LEFT).translateBy(Direction.UP), tickHolder, "spider"));
        boolean clockwise = true; 

        for (Entity currEnt : entities) {
            if (currEnt.getType().equals("spider")) {
                clockwise = ((MovingEntity) currEnt).getClockwise();
            }
        }
        // test that clockwise is reverted to false (since now moving anticlockwise)
        assertFalse(clockwise);
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

    /**
     * Helper to find the itemUsed id
     */
    public String findItemID(DungeonResponse main, String type) {
        for (ItemResponse items : main.getInventory()) {
            if (items.getType().equals(type)) {
                return items.getId();
            }
        }
        
        return null;
    }

}
