package dungeonmania.entities;

import dungeonmania.util.Direction;
import java.util.ArrayList;
import java.util.List;
import dungeonmania.util.*;

public class Spider extends MovingEntity {
    private final static int STARTING_HEALTH = 1;
    private final static int ATTACK = 1;
    List<Position> clockwiseLoop = new ArrayList<Position>();
    List<Position> anticlockwiseLoop = new ArrayList<Position>();
    int loopPos = 0;
    boolean clockwise = true;
    Position up = new Position(0, -1);
    Position down = new Position(0, 1);
    Position left = new Position(-1, 0);
    Position right = new Position(1, 0);


    /**
     * Creates the spider
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     */
    public Spider(Position position, String type, String ID, boolean isInteractable) {
        super(position, type, ID, isInteractable);
        setHealth(STARTING_HEALTH);
        setAttack(ATTACK);
        setClockwiseLoop();
        setAnticlockwiseLoop();
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
    public List<Position> getClockwiseLoop() {
        return this.clockwiseLoop;
    }

    /**
     * Creates the clockwiseloop
     */
    public void setClockwiseLoop() {
        clockwiseLoop.add(up);
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
    public List<Position> getAnticlockwiseLoop() {
        return this.anticlockwiseLoop;
    }

    /**
     * Creates the anticlockwiseloop
     */
    public void setAnticlockwiseLoop() {
        
        anticlockwiseLoop.add(up);
        anticlockwiseLoop.add(left);
        anticlockwiseLoop.add(down);
        anticlockwiseLoop.add(down);
        anticlockwiseLoop.add(right);
        anticlockwiseLoop.add(right);
        anticlockwiseLoop.add(up);
        anticlockwiseLoop.add(up);
        anticlockwiseLoop.add(left);
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
    public void moveSpider(Position direction) {
        if (direction.equals(up)) {
            super.moveUpward();
        } else if (direction.equals(down)) {
            super.moveDownward();
        } else if (direction.equals(left)) {
            super.moveLeft();
        } else if (direction.equals(right)) {
            super.moveRight();
        }
    
    }

    


}
