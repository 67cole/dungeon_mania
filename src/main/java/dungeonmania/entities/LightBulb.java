package dungeonmania.entities;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import dungeonmania.Dungeon;
import dungeonmania.util.Position;

public class LightBulb extends StaticEntity {

    Queue<Wire> wireQueue = new LinkedList<Wire>();
    private String logic;

    
    public LightBulb(Position position, String type, String ID, boolean isInteractable, String logic) {
        super(position,type, ID, isInteractable);
        this.logic = logic;
    }  
    /**
     * Turns lightbulb on
     */
    public void lightOn () {
        this.setType("light_bulb_on");
    }

    /**
     * Turns lightbulb off
     */
    public void lightOff () {
        this.setType("light_bulb_off");
    }
    
    public String getLogic() {
        return this.logic;
    }
    /**
     * Checks for switches and boulders cardinally adjacent to the lightbulbs
     * @param main
     * @param entities
     * @return boolean
     */
    public boolean checkSwitchBoulder(Dungeon main) {
        //Get all entities that are cardinally adjacent to the lightbulb
        Position N = super.getPosition().translateBy(0, -1);
        Position E = super.getPosition().translateBy(1, 0);
        Position S = super.getPosition().translateBy(0, 1);
        Position W = super.getPosition().translateBy(-1, 0);
        
        List<Entity> entsAbove = main.getEntitiesAtPos(N);
        List<Entity> entsRight = main.getEntitiesAtPos(E);
        List<Entity> entsBelow = main.getEntitiesAtPos(S);
        List<Entity> entsLeft = main.getEntitiesAtPos(W);  
        
        if (boulderAndSwitch(entsAbove) || boulderAndSwitch(entsRight) || boulderAndSwitch(entsBelow) || boulderAndSwitch(entsLeft)) {
            return true;
        }
        return false;

    }

    /**
     * Checks for switches and boulders cardinally adjacent to the lightbulbs
     * @param main
     * @param entities
     * @return boolean
     */
    public boolean checkMultipleSwitch(Dungeon main) {
        //Get all entities that are cardinally adjacent to the lightbulb
        int switchNum = 0;
        Position N = super.getPosition().translateBy(0, -1);
        Position E = super.getPosition().translateBy(1, 0);
        Position S = super.getPosition().translateBy(0, 1);
        Position W = super.getPosition().translateBy(-1, 0);
        
        List<Entity> entsAbove = main.getEntitiesAtPos(N);
        List<Entity> entsRight = main.getEntitiesAtPos(E);
        List<Entity> entsBelow = main.getEntitiesAtPos(S);
        List<Entity> entsLeft = main.getEntitiesAtPos(W);  
        
        if (boulderAndSwitch(entsAbove)) {
            switchNum++;
        }
        if (boulderAndSwitch(entsRight)) {
            switchNum++;
        }
        if (boulderAndSwitch(entsBelow)) {
            switchNum++;
        }
        if (boulderAndSwitch(entsLeft)) {
            switchNum++;
        }
        if (switchNum >= 2) {
            return true;
        }
        return false;
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

    /**
     * Checks for wires cardinally adjacent to the lightbulb
     * @param main
     * @param entities
     * @return boolean
     */
    public boolean checkWires(Dungeon main) {
        //Get all entities that are cardinally adjacent to the lightbulb
        
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

        if (wireEntity != null) {
            wireQueue.add(wireEntity);
            if (wireEntity.checkSwitch(wireQueue, main)) {
                return true;
            }
        }
        if (wireEntity1 != null) {
            wireQueue.add(wireEntity1);
            if (wireEntity1.checkSwitch(wireQueue, main)) {
                return true;
            }
        }
        if (wireEntity2 != null) {
            wireQueue.add(wireEntity2);
            if (wireEntity2.checkSwitch(wireQueue, main)) {
                return true;
            }
        }
        if (wireEntity3 != null) {
            wireQueue.add(wireEntity3);
            if (wireEntity3.checkSwitch(wireQueue, main)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks for wires cardinally adjacent to the lightbulb
     * @param main
     * @param entities
     * @return boolean
     */
    public boolean checkMultipleWires(Dungeon main) {
        //Get all entities that are cardinally adjacent to the lightbulb
        int totalSwitches = 0;
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

        if (wireEntity != null) {
            wireQueue.add(wireEntity);
            if (wireEntity.checkSwitch(wireQueue, main)) {
                totalSwitches++;
            }
        }
        if (wireEntity1 != null) {
            wireQueue.add(wireEntity1);
            if (wireEntity1.checkSwitch(wireQueue, main)) {
                totalSwitches++;
            }
        }
        if (wireEntity2 != null) {
            wireQueue.add(wireEntity2);
            if (wireEntity2.checkSwitch(wireQueue, main)) {
                totalSwitches++;
            }
        }
        if (wireEntity3 != null) {
            wireQueue.add(wireEntity3);
            if (wireEntity3.checkSwitch(wireQueue, main)) {
                totalSwitches++;
            }
        }
        if (totalSwitches >= 2) {
            return true;
        }
        return false;
    }


    /**
     * Checks for wires in a list of entities
     * @param posEntities
     * @return Wire
     */
    public Wire findWire(List<Entity> posEntities) {
        for (Entity entity: posEntities) {
            if (entity.getType().equals("wire")) {
                return ((Wire)entity);
            }
        }
        return null; 
    }
}
