package dungeonmania.entities;

import dungeonmania.util.Position;

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
     * Move up into square
     */
    public void moveUp() {
        int newY = position.getY() - 1;
        position.translateBy(position.getX(), newY);
    }

    /**
     * Move down into square
     */
    public void moveDown() {
        int newY = position.getY() + 1;
        position.translateBy(position.getX(), newY);
    }

    /**
     * Move left in square
     */
    public void moveLeft() {
        int newX = position.getX() - 1;
        position.translateBy(newX, position.getY());
    }

    /**
     * Move right in square
     */
    public void moveRight() {
        int newX = position.getX() + 1;
        position.translateBy(newX, position.getX());
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
