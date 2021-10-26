package dungeonmania.entities;

import dungeonmania.util.Position;
import dungeonmania.util.Direction;

public abstract class MovingEntity implements Entity {
    /**
     * Position in the path
     */
    private Position position;

    /**
     * Type of moving entity
     */
    private String type;

    /**
     * ID of moving entity
     */
    private String ID;

    /**
     * If it is interactable
     */
    private boolean isInteractable;

    /**
     * Creates a moving entity that can be moved up, down, left and right into cardinally adjacent square
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     */
    public MovingEntity(Position position, String type, String ID, boolean isInteractable) {
        super();
        this.position = position;
        this.type = type;
        this.ID = ID;
        this.isInteractable = isInteractable;
    }

    /**
     * Move the entity around
     */
    public void moveEntity(Direction direction) {}

    /**
     * Move the position by one square up
     */
    public void moveUpward() {
        int newY = position.getY() - 1;
        position.translateBy(position.getX(), newY);
    }

    /**
     * Move the position by one square down
     */
    public void moveDownward() {
        int newY1 = position.getY() + 1;
        position.translateBy(position.getX(), newY1);
    }

    /**
     * Move the position by one square left
     */
    public void moveLeft() {
        int newX = position.getX() - 1;
        position.translateBy(newX, position.getY());
    }

    /**
     * Move the position by one square right
     */
    public void moveRight() {
        int newX1 = position.getX() + 1;
        position.translateBy(newX1, position.getX());
    }

    /**
     * Get position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Get type
     */
    public String getType() {
        return type;
    }

    /**
     * Get ID
     */
    public String getID() {
        return ID;
    }

    /**
     * Get isInteractable
     */
    public boolean getIsInteractable() {
        return isInteractable;
    }
}
