package dungeonmania.entities;

import dungeonmania.util.Direction;
import java.util.ArrayList;
import java.util.List;
import dungeonmania.util.*;

public class Spider extends MovingEntity {
    private final static int STARTING_HEALTH = 1;
    private final static int ATTACK = 1;
    List<Direction> clockwiseLoop = new ArrayList<Direction>();
    List<Direction> anticlockwiseLoop = new ArrayList<Direction>();
    int loopPos = 0;
    boolean clockwise = true;


    /**
     * Health of character
     */
    private int health;

    /**
     * Attack of character
     */
    private int attack;

    /**
     * Condition of zombie
     */
    private boolean alive;

    /**
     * Creates the main zombie
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     */
    public Spider(Position position, String type, String ID, boolean isInteractable) {
        super(position, type, ID, isInteractable);
        this.health = STARTING_HEALTH;
        this.attack = ATTACK;
        this.alive = true;
        setClockwiseLoop();
        
    }

    /**
     * Getter for health
     */
    public int getHealth() {
        return health; 
    }

    /**
     * Getter for attack
     */
    public int getAttack() {
        return attack;
    }

    /**
     * Getter for alive
     */
    public boolean getAliveStatus() {
        return alive;
    }

    /**
     * Setter for attack
     * If the health is less than 1, i.e. <= 0, the character is dead
     * @param health
     */
    public void setHealth(int health) {
        this.health = Math.min(health, STARTING_HEALTH);

        if (health < 1) {
            this.setAlive(false);
        }
    }

    /**
     * Setter for character's condition
     * @param alive
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }


    /**
     * Getter for position in loop
     */
    @Override
    public int getLoopPos() {
        return this.loopPos;
    }

    /**
     * Setter for position in loop
     * @param alive
     */
    @Override
    public void setLoopPos(int loopPos) {
        this.loopPos = loopPos;
    }
    
    
    /**
     * Getter for clockwiseLoop
     */
    @Override
    public List<Direction> getClockwiseLoop() {
        return this.clockwiseLoop;
    }

    /**
     * Creates the clockwiseloop
     */
    public void setClockwiseLoop() {
        Direction up = Direction.UP;
        Direction down = Direction.DOWN;
        Direction left = Direction.LEFT;
        Direction right = Direction.RIGHT;
        clockwiseLoop.add(right);
        clockwiseLoop.add(down);
        clockwiseLoop.add(down);
        clockwiseLoop.add(left);
        clockwiseLoop.add(left);
        clockwiseLoop.add(up);
        clockwiseLoop.add(up);
        clockwiseLoop.add(right);
    }

    /**
     * Getter for anticlockwiseLoop
     */
    @Override
    public List<Direction> getAnticlockwiseLoop() {
        return this.anticlockwiseLoop;
    }

    /**
     * Creates the anticlockwiseloop
     */
    public void setAnticlockwiseLoop() {
        Direction up = Direction.UP;
        Direction down = Direction.DOWN;
        Direction left = Direction.LEFT;
        Direction right = Direction.RIGHT;
        clockwiseLoop.add(left);
        clockwiseLoop.add(down);
        clockwiseLoop.add(down);
        clockwiseLoop.add(right);
        clockwiseLoop.add(right);
        clockwiseLoop.add(up);
        clockwiseLoop.add(up);
        clockwiseLoop.add(left);
    }

    /**
     * Getter for clockwise
     */
    @Override
    public boolean getClockwise() {
        return this.clockwise;
    }

    /**
     * Setter for clockwise
     * @param clockwise
     */
    @Override
    public void setClockwise(boolean clockwise) {
        this.clockwise = clockwise;
    }
    

    /**
     * Movement for the spider
     */
    @Override
    public void moveEntity(Direction direction) {
        switch (direction) {
            case UP:
                super.moveUpward();
                break;
            case DOWN:
                super.moveDownward();
                break;
            case LEFT:
                super.moveLeft();
                break;
            case RIGHT:
                super.moveRight();
                break;
            case NONE:
                break;
        }   
    }



}
