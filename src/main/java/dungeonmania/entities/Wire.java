package dungeonmania.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import dungeonmania.Dungeon;
import dungeonmania.util.Position;

public class Wire extends StaticEntity {

    public Wire(Position position, String type, String ID, boolean isInteractable) {
        super(position, type, ID, isInteractable);
    }
    
    public boolean checkSwitch(Queue<Wire> wireQueue, Dungeon main) {
        List<Wire> prevList = new ArrayList<Wire>();
        while (!(wireQueue.isEmpty())) {
            Wire wireEntity = wireQueue.remove();
            if (wireEntity.checkAdjacentEntities(main, wireQueue, prevList) == true) {
                wireQueue.clear();
                return true;
            }
            prevList.add(wireEntity);
        }
        return false;
        
    }

    public boolean checkAdjacentEntities(Dungeon main, Queue<Wire> wireQueue, List<Wire> prevList) {
        
        //Get all entities that are cardinally adjacent to the Wire
        Position N = super.getPosition().translateBy(0, -1);
        Position E = super.getPosition().translateBy(1, 0);
        Position S = super.getPosition().translateBy(0, 1);
        Position W = super.getPosition().translateBy(-1, 0);
        
        List<Entity> entsAbove = main.getEntitiesAtPos(N);
        List<Entity> entsRight = main.getEntitiesAtPos(E);
        List<Entity> entsBelow = main.getEntitiesAtPos(S);
        List<Entity> entsLeft = main.getEntitiesAtPos(W); 
        
        Wire wireEntity = findWire(entsAbove);
        Wire wireEntity1 = findWire(entsRight);
        Wire wireEntity2 = findWire(entsBelow);
        Wire wireEntity3 = findWire(entsLeft);

        if (wireEntity != null && !prevList.contains(wireEntity)) {
            wireQueue.add(wireEntity);        
        }
        if (wireEntity1 != null && !prevList.contains(wireEntity1)) {
            wireQueue.add(wireEntity1);    
        }
        if (wireEntity2 != null && !prevList.contains(wireEntity2)) {
            wireQueue.add(wireEntity2);     
        }
        if (wireEntity3 != null && !prevList.contains(wireEntity3)) {
            wireQueue.add(wireEntity3);     
        }
        if (boulderAndSwitch(entsAbove) || boulderAndSwitch(entsRight) || boulderAndSwitch(entsBelow) || boulderAndSwitch(entsLeft)) {
            return true;
        }
        return false;

    }


    public Wire findWire(List<Entity> posEntities) {
        for (Entity entity: posEntities) {
            if (entity.getType().equals("wire")) {
                return ((Wire) entity);
            }
        }
        return null;
    }

    /**
     * Checks for switches and boulders in a list of entities
     * @param posEntities
     * @return boolean
     */
    public boolean boulderAndSwitch(List<Entity> posEntities) {
        boolean switchExist = false;
        for (Entity entity: posEntities) {
            if (entity.getType().equals("switch")) {
                switchExist = true;
            }
        }
        if (switchExist == true) {
            for (Entity boulder: posEntities) {
                if (boulder.getType().equals("boulder")) {
                    return true;
                }
            }
        }     
        return false;
    }


}
