package dungeonmania.entities;

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
}
