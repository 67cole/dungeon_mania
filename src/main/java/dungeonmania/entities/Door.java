package dungeonmania.entities;

import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.entities.CollectableEntities.Key;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Door extends StaticEntity {
    private int keyType;
    private boolean locked;


    /**
     * Constructor for door
     * @param position
     * @param type
     * @param ID
     * @param IsInteractable
     * @param keyType
     * @param locked
     */

    public Door(Position position, String type, String ID, boolean isInteractable, int keyType, boolean locked) {
        super(position,type, ID, isInteractable);
        this.keyType = keyType;
        this.locked = locked;
    } 

    public int getKeyType() {
        return this.keyType;
    }

    public boolean getLocked() {
        return this.locked;
    }

    /**
     * Sets the Door as unlocked or locked
     * @param locked
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     * checkDoorLock checks for the next square if it's a door. If the door is locked,
     * it should check for the specific key inside the characters inventory and open the door 
     * if the key matches the door. Returns true if the door is open and false if not
     * @param entityDoor
     * @param entities
     * @param main
     * @return boolean
     **/
    public boolean checkKey(Dungeon main) {
        for (CollectableEntity item : main.inventory) {
            if (item.getType().equals("sun_stone")) {
                this.setLocked(false);
                return true;
            }
        }
        int remove = 0;
        int keyNum = 0;
        int keyType = this.getKeyType();
        CollectableEntity itemKey = null;
        for (CollectableEntity item : main.inventory) {
            if (item.getType().equals("key")) {
                Key key = (Key) item;
                keyNum =  key.getKeyNum();
                //If the key and door match, return true
                if (keyType == keyNum) {
                    remove = 1;
                    this.setLocked(false);
                    itemKey = item;

                } 
            }
        }
        if (remove == 1) {
            main.inventory.remove(itemKey);
            main.setKeyStatus(true);
            return true;
        }
        return false;
    } 

    @Override
    public void entityFunction(List<Entity> entities, Character player, Direction direction, Dungeon main) {  
    }
    
}
