package dungeonmania.entities;

import dungeonmania.util.Position;

public class SwampTile extends StaticEntity {
    private int movementFactor;
    




     /**
     * Constructor for SwampTile
     * @param position
     * @param type
     * @param ID
     * @param IsInteractable
     */
    public SwampTile(Position position, String type, String ID, boolean isInteractable) {
        super(position,type, ID, isInteractable);
    }  
    
    public int getMovementFactor() {
        return this.movementFactor;
    }
    public void setMovementFactor(int movementFactor) {
        this.movementFactor = movementFactor;
    }
}
