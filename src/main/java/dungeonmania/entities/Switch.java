package dungeonmania.entities;

import java.util.List;
import dungeonmania.util.Position;

public class Switch extends StaticEntity {
    
    /**
     * Constructor for Switch
     * @param position
     * @param type
     * @param ID
     * @param IsInteractable
     */
    public Switch(Position position, String type, String ID, boolean isInteractable) {
        super(position,type, ID, isInteractable);
    }

    

    /**
     * Determines if a boulder is present on a switch
     * @param entities
     * @return boolean
     */
    public boolean isBoulderPresent(List<Entity> entities) {
        for (Entity entity : entities) {
            if (entity.getType().equals("boulder") && entity.getPosition().equals(super.getPosition())) {
                return true;
            }
        }
        return false;
    
    }

}
