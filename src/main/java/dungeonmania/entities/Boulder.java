package dungeonmania.entities;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Collection;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.Dungeon;

public class Boulder extends StaticEntity{

    public Boulder(Position position, String type, String ID, boolean isInteractable) {
        super(position,type, ID, isInteractable);
    }
    
    @Override
    public void entityFunction(List<Entity> entities, Character player, Direction direction, Dungeon main) {
        // we need to move the boulder by the direction
        switch(direction) {
            case UP:
                super.moveUpward();
                break;

            case DOWN:
                super.moveDownward();
                break;
            
            case LEFT:
                super.moveLeft();
                break;
            
            case RIGHT:
                super.moveRight();
                break;
            case NONE:
                break;
        }   
        
    }

    /**
     * attempts to explode bombs given a boulder position
     * @param entities
     * @param player
     * @param main
     * @param boulder
     * @param nearby
     */
    public void doExplode(List<Entity> entities, Character player,  Dungeon main, Entity boulder, List<Entity> nearby) {
        // pos = boulder position
        Position boulderPos = boulder.getPosition();
        // find a switch

        List<Entity> entitiesAtBoulder = main.getEntitiesAtPos(boulderPos);
        Position N = boulderPos.translateBy(0, -1);
        Position E = boulderPos.translateBy(1, 0);
        Position S = boulderPos.translateBy(0, 1);
        Position W = boulderPos.translateBy(-1, 0);
        
        List<Entity> entsAbove = main.getEntitiesAtPos(N);
        List<Entity> entsRight = main.getEntitiesAtPos(E);
        List<Entity> entsBelow = main.getEntitiesAtPos(S);
        List<Entity> entsLeft = main.getEntitiesAtPos(W);

        for (Entity currEnt : entitiesAtBoulder) {
            if (currEnt.getType().equals("switch")) {
                // see if there are bombs cardinally adjacent, if so, explode any adjacent bombs
                if (isBombAtPos(entsAbove)) {
                    explode(entsAbove, entities, N, main, player, nearby);
                }
                if (isBombAtPos(entsRight)) {
                    explode(entsRight, entities, E, main, player, nearby);
                    
                } 
                if (isBombAtPos(entsBelow)) {
                    explode(entsBelow, entities, S, main, player, nearby);
                    
                } 
                if (isBombAtPos(entsLeft)) {
                    explode(entsLeft, entities, W, main, player, nearby);
                }
                
            }
        }
    }
    
    /**
     * returns whether a bomb is in the list of entities at a given position
     * @param entities
     * @return Boolean
     */
    public Boolean isBombAtPos(List<Entity> entities) {
        for (Entity currEnt : entities) {
            if (currEnt.getType().equals("bomb")) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Explodes any bombs adjacent to a given bomb position
     * @param entitiesAtPos
     * @param entities
     * @param pos
     * @param main
     * @param player
     * @param NearbyEntities
     */
    public void explode(List<Entity> entitiesAtPos, List<Entity> entities, Position pos, Dungeon main, Character player, List<Entity> NearbyEntities) {
        
        Position N = pos.translateBy(0, -1);
        Position NE = pos.translateBy(1, -1);
        Position E = pos.translateBy(1, 0);
        Position SE = pos.translateBy(1, 1);
        Position S = pos.translateBy(0, 1);
        Position SW = pos.translateBy(-1, 1);
        Position W = pos.translateBy(-1, 0);
        Position NW = pos.translateBy(-1, -1);
        
        List<Entity> entsN = main.getEntitiesAtPos(N);
        List<Entity> entsNE = main.getEntitiesAtPos(NE);
        List<Entity> entsE = main.getEntitiesAtPos(E);
        List<Entity> entsSE = main.getEntitiesAtPos(SE);
        List<Entity> entsS = main.getEntitiesAtPos(S);
        List<Entity> entsSW = main.getEntitiesAtPos(SW);
        List<Entity> entsW = main.getEntitiesAtPos(W);
        List<Entity> entsNW = main.getEntitiesAtPos(NW);
        List<Entity> entsO = entitiesAtPos;
        // add all nearby non-player entities to the list of entities to be removed 
        for (Entity ent : entsN) {
            if (!ent.getType().equals("player")) {
                NearbyEntities.add(ent);
            }
        }
        for (Entity ent : entsNE) {
            if (!ent.getType().equals("player")) {
                NearbyEntities.add(ent);
            }
        }
        for (Entity ent : entsE) {
            if (!ent.getType().equals("player")) {
                NearbyEntities.add(ent);
            }
        }
        for (Entity ent : entsSE) {
            if (!ent.getType().equals("player")) {
                NearbyEntities.add(ent);
            }
        }
        for (Entity ent : entsS) {
            if (!ent.getType().equals("player")) {
                NearbyEntities.add(ent);
            }
        }
        for (Entity ent : entsSW) {
            if (!ent.getType().equals("player")) {
                NearbyEntities.add(ent);
            }
        }
        for (Entity ent : entsW) {
            if (!ent.getType().equals("player")) {
                NearbyEntities.add(ent);
            }
        }
        for (Entity ent : entsNW) {
            if (!ent.getType().equals("player")) {
                NearbyEntities.add(ent);
            }
        }
        for (Entity ent : entsO) {
            if (!ent.getType().equals("player")) {
                NearbyEntities.add(ent);
            }
        }
    }
}
