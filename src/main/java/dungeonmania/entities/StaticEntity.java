package dungeonmania.entities;

import java.util.List;

import dungeonmania.util.Position;

public abstract class StaticEntity implements Entity{
    private Position position;
    private String type;
    private String ID;
    private boolean isInteractable;
    private String colour;

    StaticEntity(Position position, String type, String ID, boolean isInteractable, String colour) {
        this.position = position;
        this.type = type; 
        this.ID = ID;
        this.isInteractable = isInteractable;  
        this.colour = colour; 
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

    public String getColour() {
        return this.colour;
    }

    @Override
    public void entityFunction(List<Entity> entities, Character player) {
    }   
}
