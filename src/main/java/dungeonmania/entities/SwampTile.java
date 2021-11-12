package dungeonmania.entities;

import dungeonmania.util.Position;

import java.util.List;

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

    /**
     * Checks if the entity could move away from the swamp tile 
     * @param entity
     * @param entities
     * @return boolean
     */
    public static boolean swampCanMove (MovingEntity entity, List<Entity> entities) {
        boolean swampMove = true;
        for (Entity staticEntity: entities) {
            //If the spider is on the swamp tile, slow it down
            if (staticEntity.getType().equals("swamp_tile") && staticEntity.getPosition().equals(entity.getPosition())) {
                SwampTile swampEntity = (SwampTile) staticEntity;
                if (entity.getTotalMovement() < swampEntity.getMovementFactor()) {
                    entity.swampMove();
                    swampMove = false;
                    continue;
                } else {
                    entity.resetTotalMovement();
                }
            }
        }
        return swampMove;
    }
}
