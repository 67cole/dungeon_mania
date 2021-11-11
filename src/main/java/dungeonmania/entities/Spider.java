package dungeonmania.entities;

import dungeonmania.util.Direction;
import java.util.ArrayList;
import java.util.List;
import dungeonmania.util.*;

public class Spider extends MovingEntity {
    private final static int STARTING_HEALTH = 1;
    private final static int ATTACK = 1;
    private int loopPos = 0;
    private boolean clockwise = true;
    List<Position> clockwiseLoop = new ArrayList<Position>();
    List<Position> anticlockwiseLoop = new ArrayList<Position>();
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
        anticlockwiseLoop.add(up);
        anticlockwiseLoop.add(up);
        
        anticlockwiseLoop.add(right);
        anticlockwiseLoop.add(right);
        anticlockwiseLoop.add(down);
        anticlockwiseLoop.add(down);
        
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
     * @param direction
     */
    @Override
    public void moveEntity(Position direction) {
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

    @Override
    public void moveSpider(List<Entity> entities, Entity entity) {
        MovingEntity temp = (MovingEntity) entity;
        MovingEntity spider = (Spider) entity;
        int loopPos = spider.getLoopPos();
        // if just spawned, move upward. do not need to check for
        // boulder above since cannot spawn below a boulder
        if (loopPos == 0) {
            temp.moveUpward();
            // if finished a loop, reset
            if (loopPos == 9) {
                loopPos = 0;
            }
            spider.setLoopPos(loopPos + 1);
        } else {
            // 1. get currLoop based on movement direction
            // 2. check if next pos is a boulder
            //      if boulder, setClockwise to opposite
            // 3. move
            List<Position> posLoop = spider.getClockwiseLoop();
            List<Position> negLoop = spider.getAnticlockwiseLoop();

            // get direction of movement based on whether moving clockwise
            Position dir = posLoop.get(loopPos);                           
            if (spider.getClockwise() == false) {
                dir = negLoop.get(loopPos);
            }
            int spiderBlocked = 0;
            // if blocked, set dir to opposite
            for (Entity currEnt: entities) {
                Position nextPos = spider.getPosition().translateBy(dir);

                if (currEnt.getPosition().equals(nextPos) && currEnt.getType().equals("boulder")) {
                    spider.setClockwise(!spider.getClockwise());
                    
                    spiderBlocked = 1;
                    
                }
                else if (currEnt.getPosition().equals(nextPos) && currEnt.getType().equals("door")) {
                    spider.setClockwise(!spider.getClockwise());
                    
                    spiderBlocked = 1;
                    
                }
            }

            // double check if movement direction changed
            if (spider.getClockwise() == false) {
                dir = negLoop.get(loopPos);
            } else {
                dir = posLoop.get(loopPos);
            }
            if (spiderBlocked == 0) spider.moveEntity(dir);
            
            // update loopPos
            if (spider.getClockwise() == true) {
                if (loopPos == 8) {
                    loopPos = 0;
                }
                spider.setLoopPos(loopPos + 1);
            } else {
                if (loopPos == 1) {
                    loopPos = 9;
                }
                spider.setLoopPos(loopPos - 1);
            }

        }
    }

    public Position getSpiderSpawn(List<Entity> entities) {

        boolean posFound = false;
        while (posFound == false) {
            int x = getRandomNumber(0, 15);
            int y = getRandomNumber(0, 15);
            int check = 0;
            Position pos = new Position(x, y);
            Position posAbove = new Position(x, y + 1);
            for (Entity entity : entities) {
                // if the square is a boulder
                if ((entity.getPosition().equals(pos)) && (entity.getType().equals("boulder"))) {
                    check = 1;
                    break;
                } else if ((entity.getPosition().equals(posAbove)) && (entity.getType().equals("boulder"))) {
                    check = 1;
                    break;
                }
            }
            if (check == 0) {
                return pos;
            }
            // check if the square is a bouldeer
            // check if the square above is a boulder
        }
        


        return null;
    }
    
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }



}
