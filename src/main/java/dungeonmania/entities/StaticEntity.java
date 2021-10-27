package dungeonmania.entities;

import java.util.List;

import dungeonmania.util.Position;

public abstract class StaticEntity implements Entity{
    private Position position;
    private String type;
    private String ID;
    private boolean isInteractable;

    StaticEntity(Position position, String type, String ID, boolean isInteractable) {
        this.position = position;
        this.type = type; 
        this.ID = ID;
        this.isInteractable = isInteractable;   
    } 

    /**
     * Move the position by one square up
     */
    public void moveUpward() {
        setPosition(position.translateBy(0, -1));
    }

    /**
     * Move the position by one square down
     */
    public void moveDownward() {
        setPosition(position.translateBy(0, 1));
    }

    /**
     * Move the position by one square left
     */
    public void moveLeft() {
        setPosition(position.translateBy(-1, 0));
    }

    /**
     * Move the position by one square right
     */
    public void moveRight() {
        setPosition(position.translateBy(1, 0));
    }

    /**
     * Sets the position
     * @param position
     */
    public void setPosition(Position position) {
        this.position = position;
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
    
    public void entityFunction(List<Entity> entities, Character player) {
    }   
}
