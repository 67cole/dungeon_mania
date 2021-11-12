package dungeonmania.entities;
import dungeonmania.util.Position;
import dungeonmania.util.Direction;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.Dungeon;
import java.util.List;

public abstract class CollectableEntity implements Entity {
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
    public CollectableEntity(Position position, String type, String ID, boolean isInteractable) {
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
        main.inventory.add(this);
        
        // Check if buildable list already contains shield or bow
        int bow = 0;
        int shield = 0;
        int midnightArmour = 0;
        int sceptre = 0;
        for (String buildable: main.buildables) {
            if (buildable.equals("bow")) {
                bow = 1;
            }
            if (buildable.equals("shield")) {
                shield = 1;
            }
            if (buildable.equals("midnight_armour")) {
                midnightArmour = 1;
            }
            if (buildable.equals("sceptre")) {
                sceptre = 1;
            }
        }   
        // Check if buildable can be made
        int wood = 0;
        int arrow = 0;
        int key = 0;
        int armour = 0;
        int treasure = 0;
        int sunStone = 0;
        for (CollectableEntity item: main.inventory) {
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
                case "sun_stone":
                    sunStone++;
                    break;
                case "armour":
                    armour++;
                    break;
            }
        }
        // Creating a bow if bow does not already exist in buildables
        if (wood >= 1 && arrow >= 3 && bow != 1) {
            main.buildables.add("bow");
        } 
        // Creating a shield  if shield does not already exist in buildables
        if (wood >= 2 && (treasure >= 1 || key == 1)&& shield != 1) {
            main.buildables.add("shield");
        } 
        // Creating a sceptre if sceptre does not already exist in buildables
        if ((wood >= 1 || arrow >= 2) && (key >= 1 || treasure >= 1) && sunStone >= 1 && sceptre != 1) {
            main.buildables.add("sceptre");
        } 
        // Creating midnight armour if midnight armour does not already exist in buildables
        // If armour is within player's inventory
        if (armour >= 1 && sunStone >= 1 && midnightArmour != 1) {
            if (zombieChecker(main.getEntities())) {
                main.buildables.add("midnight_armour");
            }
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
     * @param inventory - inventory of the player
     */
    public boolean keyChecker(List<CollectableEntity> inventory) {
        for (CollectableEntity item: inventory) {
            if (item.getType().equals("key")) {
                return true;
            }
        }
        return false;
    }
    /**
     * Searches for a zombie
     * @param entites - list of entities in the dungeon
     */
    public boolean zombieChecker(List<Entity> entities) {
        for (Entity entity: entities) {
            if (entity.getType().equals("zombie_toast")) {
                return false;
            }
        }
        return true;
    }
}
