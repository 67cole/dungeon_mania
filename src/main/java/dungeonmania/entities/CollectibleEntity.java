package dungeonmania.entities;
import dungeonmania.util.Position;
import dungeonmania.util.Direction;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.Dungeon;
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
    public void entityFunction(List<Entity> entities, Character player , Direction direction, Dungeon main) {
        // Checking to see if there is already an existing key
        if (this.getType().equals("key")) {
            if (keyChecker(main.inventory)) {
                return;
            }
        }
        // Add the collectible to inventory
        ItemResponse newItem = new ItemResponse(this.getID(), this.getType());
        main.inventory.add(newItem);
        
        // Check if buildable list already contains shield or bow
        int bow = 0;
        int shield = 0;
        for (String buildable: main.buildables) {
            if (buildable.equals("bow")) {
                bow = 1;
            }
            if (buildable.equals("shield")) {
                shield = 1;
            }
        }   
        // Check if buildable can be made
        int wood = 0;
        int arrow = 0;
        int key = 0;
        int treasure = 0;
        for (ItemResponse item: main.inventory) {
            switch(item.getType()) {
                case "wood":
                    wood++;
                    break;
                case "arrow":
                    arrow++;
                    break;
                case "key":
                    key++;
                    break;
                case "treasure":
                    treasure++;
                    break;
            }
        }
        // Creating a bow if bow does not already exist in buildables
        if (wood == 1 && arrow == 3 && bow != 1) {
            main.buildables.add("bow");
        } 
        // Creating a shield with treasure if shield does not already exist in buildables
        if (wood == 2 && treasure == 1 && shield != 1) {
            main.buildables.add("shield");
        } 
        // Creating a shield with key if shield does not already exist in buildables
        else if (wood == 2 && key == 1 && shield != 1) {
            main.buildables.add("shield");
        } 
    }   
    /**
     * 
     * 
     *              HELPER FUNCTIONS
     * 
     * 
     */

    /**
     * Searches for a key
     */
    public boolean keyChecker(List<ItemResponse> inventory) {
        for (ItemResponse item: inventory) {
            if (item.getType().equals("key")) {
                return true;
            }
        }
        return false;
    }
}
