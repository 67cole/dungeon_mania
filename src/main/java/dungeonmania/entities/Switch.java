package dungeonmania.entities;

import java.util.List;
import dungeonmania.util.Position;

public class Switch extends StaticEntity {
    private String logic;



    /**
     * Constructor for Switch
     * @param position
     * @param type
     * @param ID
     * @param IsInteractable
     * @param logic
     */
    public Switch(Position position, String type, String ID, boolean isInteractable, String logic) {
        super(position, type, ID, isInteractable);
        this.logic = logic;
    }
    

    public String getLogic() {
        return this.logic;
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
