package dungeonmania.entities;

import dungeonmania.util.Position;

public abstract class StaticEntity implements Entity{
    private Position position;
    private String type;
    private String ID;
    private boolean IsInteractable;

    StaticEntity(Position position, String type, String ID, boolean IsInteractable) {
        this.position = position;
        this.type = type; 
        this.ID = ID;
        this.IsInteractable = IsInteractable;   
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
        return this.IsInteractable;
    }
    
}
