package dungeonmania.entities;

import dungeonmania.util.Position;
import dungeonmania.util.Direction;

import java.util.List;

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
        this.position = position;
        this.type = type;
        this.ID = ID;
        this.isInteractable = isInteractable;
    }

    /**
     * Check whether or not entities can move
     */
    public boolean checkMovement(Direction direction, List<Entity> entities) {
        switch (direction) {
            case UP:
                Position attemptedMove = position.translateBy(0, -1);

                for (Entity entity : entities) {
                    if (entity.getPosition().equals(attemptedMove)) {
                        return false;
                    }
                }
                break;

            case DOWN:
                Position attemptedMove1 = position.translateBy(0, 1);

                for (Entity entity : entities) {
                    if (entity.getPosition().equals(attemptedMove1)) {
                        return false;
                    }
                }
                break;

            case LEFT:
                Position attemptedMove2 = position.translateBy(-1, 0);

                for (Entity entity : entities) {
                    if (entity.getPosition().equals(attemptedMove2)) {
                        return false;
                    }
                }
                break;

            case RIGHT:
                Position attemptedMove3 = position.translateBy(1, 0);

                for (Entity entity : entities) {
                    if (entity.getPosition().equals(attemptedMove3)) {
                        return false;
                    }
                }
                break;

            case NONE:
                return true;
        }

        // If it's a white square, you can move
        return true; 
    }

    /**
     * Move the entity around
     */
    public void moveEntity(Direction direction, List<Entity> entities) {}

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
     * Get position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the position
     * @param position
     */
    public void setPosition(Position position) {
        this.position = position;
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
