package dungeonmania.entities;

import java.util.List;

import dungeonmania.util.Position;

public class Portal extends StaticEntity {
    private String colour;
    public Portal(Position position, String type, String ID, boolean IsInteractable, String colour) {
        super(position,type, ID, IsInteractable);
        this.colour = colour;
    } 

    public String getColour() {
        return this.colour;
    }
    
    /**
     * @param position_of_player
     * @param entities
     * 
     * 
     * This entity function changes the players position into the corresponding portals position
     */
    @Override
    public void entityFunction(List<Entity> entities, Character player) {  
        String colour = null;
        for (Entity entity: entities) {
            //If the looped entity is a portal and that portal is in the same position as the player, obtain the colour of the portal
            if (entity.getType().equals("portal") && entity.getPosition().equals(player.getPosition())) {
                Portal portal = (Portal) entity;
                colour = portal.getColour();
                break;               
            }
        } 
        //Loop through entity list again for a portal with the same corresponding colour
        for (Entity entity: entities) {
            //If entity is a portal and its position isnt the same position as the player, then change the position of the character
            //Into position of the corresponding portal
            if (entity.getType().equals("portal") && !entity.getPosition().equals(player.getPosition())) {
                Portal portal = (Portal) entity;
                if (portal.getColour().equals(colour)) {
                    player.setPosition(portal.getPosition());
                    break;
                }      
            }
        }
    }
}
