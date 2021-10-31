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

    // @Override
    // public void doExplode(List<Entity> entities, Character player,  Dungeon main) {
    //     // pos = boulder position
    //     Position boulderPos = super.getPosition();
    //     // find a switch

    //     List<Entity> entitiesAtBoulder = main.getEntitiesAtPos(boulderPos);
    //     Position N = boulderPos.translateBy(0, -1);
    //     Position E = boulderPos.translateBy(1, 0);
    //     Position S = boulderPos.translateBy(0, 1);
    //     Position W = boulderPos.translateBy(-1, 0);
        
    //     List<Entity> entsAbove = main.getEntitiesAtPos(N);
    //     List<Entity> entsRight = main.getEntitiesAtPos(E);
    //     List<Entity> entsBelow = main.getEntitiesAtPos(S);
    //     List<Entity> entsLeft = main.getEntitiesAtPos(W);

    //     for (Entity currEnt : entitiesAtBoulder) {
    //         if (currEnt.getType().equals("switch")) {
    //             // see if there are bombs cardinally adjacent, if so, explode any adjacent bombs
    //             if (isBombAtPos(entsAbove)) {
    //                 System.out.println("Bomb above");
    //                 explode(entsAbove, entities, N, main, player);
    //             }
    //             if (isBombAtPos(entsRight)) {
    //                 explode(entsRight, entities, E, main, player);
    //                 System.out.println("Bomb right");
    //             } 
    //             if (isBombAtPos(entsBelow)) {
    //                 explode(entsBelow, entities, S, main, player);
    //                 System.out.println("Bomb below");
    //             } 
    //             if (isBombAtPos(entsLeft)) {
    //                 explode(entsLeft, entities, W, main, player);
    //                 System.out.println("Bomb left");
    //             }
                
    //         }
    //     }
    // }
    
    // public Boolean isBombAtPos(List<Entity> entities) {
    //     for (Entity currEnt : entities) {
    //         if (currEnt.getType().equals("bomb")) {
    //             return true;
    //         }
    //     }
    //     return false;
    // }
    
    // public void explode(List<Entity> entitiesAtPos, List<Entity> entities, Position pos, Dungeon main, Character player) {
        
    //     Position N = pos.translateBy(0, -1);
    //     Position NE = pos.translateBy(1, -1);
    //     Position E = pos.translateBy(1, 0);
    //     Position SE = pos.translateBy(1, 1);
    //     Position S = pos.translateBy(0, 1);
    //     Position SW = pos.translateBy(-1, 1);
    //     Position W = pos.translateBy(-1, 0);
    //     Position NW = pos.translateBy(-1, -1);
        
    //     List<Entity> entsN = main.getEntitiesAtPos(N);
    //     List<Entity> entsNE = main.getEntitiesAtPos(NE);
    //     List<Entity> entsE = main.getEntitiesAtPos(E);
    //     List<Entity> entsSE = main.getEntitiesAtPos(SE);
    //     List<Entity> entsS = main.getEntitiesAtPos(S);
    //     List<Entity> entsSW = main.getEntitiesAtPos(SW);
    //     List<Entity> entsW = main.getEntitiesAtPos(W);
    //     List<Entity> entsNW = main.getEntitiesAtPos(NW);
    //     List<Entity> entsO = entitiesAtPos;

    //     List<Entity> allNearbyEntities = new ArrayList<Entity>();
    //     allNearbyEntities.addAll(entsN);
    //     allNearbyEntities.addAll(entsNE);
    //     allNearbyEntities.addAll(entsE);
    //     allNearbyEntities.addAll(entsSE);
    //     allNearbyEntities.addAll(entsS);
    //     allNearbyEntities.addAll(entsSW);
    //     allNearbyEntities.addAll(entsW);
    //     allNearbyEntities.addAll(entsNW);
    //     allNearbyEntities.addAll(entsO);

    //     // Iterator<Entity> itr = allNearbyEntities.iterator();
    //     // while (itr.hasNext()) {
    //     //     Entity curr = itr.next();
    //     //     if (!(curr.getType().equals("player"))) {
    //     //         itr.remove();
    //     //         System.out.println("removed");
    //     //     }
            
    //     // }
        
    //     // allNearbyEntities.removeIf(s -> (!s.getType().equals("player")));

    //     // // // entities.removeAll(allNearbyEntities);
    //     // for (Entity curr : allNearbyEntities) {
    //     //     if (!curr.getType().equals("player")) entities.remove(curr);
    //     // }


    //     // entities.removeAll(allNearbyEntities);
    //     // entities.add(player);

        
        
    // }

}
