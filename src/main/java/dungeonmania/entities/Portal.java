package dungeonmania.entities;

import java.util.List;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.Dungeon;

public class Portal extends StaticEntity {
    private String colour;

    /**
     * Constructor for portal
     * @param position
     * @param type
     * @param ID
     * @param IsInteractable
     * @param colour
     */
    public Portal(Position position, String type, String ID, boolean IsInteractable, String colour) {
        super(position,type, ID, IsInteractable);
        this.colour = colour;
    } 

    public String getColour() {
        return this.colour;
    }
    
    /**
     * This function teleports the character onto the position of the corresponding portal
     * @param entities
     * @param player
     * @param direction
     * @param main
     */
    @Override
    public void entityFunction(List<Entity> entities, Character player, Direction direction, Dungeon main) {  
        String type = null;
        
        for (Entity entity: entities) {
            //If the looped entity is a portal and that portal is in the same position as the player, obtain the colour of the portal
            if (entity.getPosition().equals(player.getPosition()) && !entity.getType().equals(player.getType())) {
                Portal portal = (Portal) entity;
                type = portal.getType();
                break;
            }
        } 
        //Loop through entity list again for a portal with the same corresponding colour
        for (Entity entity: entities) {
            //If entity is a portal and its position isnt the same position as the player, then change the position of the character
            //Into position of the corresponding portal
            if (entity.getType().equals(type) && !entity.getPosition().equals(player.getPosition())) {             
                Portal portal = (Portal) entity;
                player.setPosition(portal.getPosition().translateBy(direction));
                break;   
            }
        }
    }
}
