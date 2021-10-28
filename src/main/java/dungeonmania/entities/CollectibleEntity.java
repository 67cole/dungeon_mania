package dungeonmania.entities;
import dungeonmania.util.Position;
import java.util.List;

public abstract class CollectibleEntity implements Entity {
    /**
     * Position in the path
     */
    private Position position;

    /**
     * Type of collectible entity
     */
    private String type;

    /**
     * ID of collectible entity
     */
    private String ID;

    /**
     * If it is interactable
     */
    private boolean isInteractable;

    /**
     * Creates a collectible entity that can be stored by a character
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     */
    public CollectibleEntity(Position position, String type, String ID, boolean isInteractable) {
        this.position = position;
        this.type = type;
        this.ID = ID;
        this.isInteractable = isInteractable;
    }

    @Override
    public Position getPosition() {
        return this.position;
    }
    
    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public String getID() {
        return this.ID;
    }
    
    @Override
    public boolean getIsInteractable() {
        return this.isInteractable;
    }
    @Override
    public void entityFunction(List<Entity> entities, Character player) {
        
    }   
}
