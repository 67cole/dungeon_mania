package dungeonmania.entities;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.Dungeon;
import java.util.List;

public class BuildableEntity extends CollectableEntity{

    /**
     * Creates a buildable entity that can be stored by a character
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     */
    public BuildableEntity(Position position, String type, String ID, boolean isInteractable) {
        super(position, type, ID, isInteractable);
    }

    @Override
    public void entityFunction(List<Entity> entities, Character player, Direction direction, Dungeon main) {
    }   
}
