package dungeonmania.entities;

import java.util.List;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Exit extends StaticEntity {

    /**
     * Constructor for exit
     * @param position
     * @param type
     * @param ID
     * @param IsInteractable
     */
    public Exit(Position position, String type, String ID, boolean isInteractable) {
        super(position,type, ID, isInteractable);
    }

}
