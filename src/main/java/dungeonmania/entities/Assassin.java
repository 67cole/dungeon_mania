package dungeonmania.entities;

import dungeonmania.util.Position;
import dungeonmania.util.Direction;

import java.util.List;
import java.util.Random;

import javax.xml.stream.events.EntityDeclaration;

import java.lang.Math;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.PriorityQueue;

public class Assassin extends MovingEntity {
    private final static int STARTING_HEALTH = 6;
    private final static int ATTACK = 6;
    private boolean friendly = false;

    /**
     * Creates the assassin
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     */
    public Assassin(Position position, String type, String ID, boolean isInteractable) {
        super(position, type, ID, isInteractable);
        setHealth(STARTING_HEALTH);
        setAttack(ATTACK);
    }

    public boolean getFriendly() {
        return this.friendly;
    }

    public void setFriendly(boolean friendly) {
        this.friendly = friendly;
    }

    /**
     * returns a list of walkable positions
     * @param entities
     * @return
     */
    public List<Position> posList(List<Entity> entities) {
        List<Position> ls = new ArrayList<Position>();
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 18; j++) {
                ls.add(new Position(j, i));
            }
        }
        for (Entity ent : entities) {
            if (ent.getType().equals("wall") || ent.getType().equals("door")) {
                ls.remove(ent.getPosition());
            }
        }
        return ls;

    }


    /**
     * returns the cost of moving one tile to another
     * @param entities
     * @param source
     * @param dest
     * @return int
     */
    public int cost (List<Entity> entities, Position source, Position dest) {
        for (Entity ent : entities) {
            // if swamp tile, return movement factor instead of 1 (movement factor counts as distance of 2)
            if (ent.getPosition().equals(source) && ent.getType().equals("swamp_tile")) {
                return ((SwampTile) ent).getMovementFactor();
            }
        }

        return 1;
    }

    /**
     * Performs dijkstra and returns the first position along the shortest found path
     * @param posList
     * @param source
     * @param entities
     * @return Position
     */
    public Position dijkstra(List<Position> posList, Position source, List<Entity> entities, Position nextPosition) {
        
        // create hashmap of dist and prev
        HashMap<Position, Double> dist = new HashMap<Position, Double>();
        HashMap<Position, Position> prev = new HashMap<Position, Position>();

        // initialise all distances to infinity and prev to null
        for (Position pos : posList) {
            dist.put(pos, Double.POSITIVE_INFINITY);
            prev.put(pos, null);
        }
        // dist->source = 0
        dist.put(source, (double) 0);
        
        Queue<Position> posQueue = new LinkedList<Position>();
        posQueue.add(source);
        // start with source, and add neighbours onto queue
        while(posQueue.size() > 0) {
            // find next unvisited node with smallest dist
            Position pos = posQueue.poll();
            List<Position> cardinalNeighbours = getCardinalNeighbours(pos);
            for (Position currNeighbour : cardinalNeighbours) {
                if (!dist.containsKey(currNeighbour)) continue;
                // cost returns movement factor is swamp tile is present at neighbour
                int moveCost = cost(entities, pos, currNeighbour);
                if (dist.get(pos) + moveCost < dist.get(currNeighbour)) {
                    dist.put(currNeighbour, dist.get(pos) + moveCost);
                    prev.put(currNeighbour, pos);
                    posQueue.add(currNeighbour);
                }
            }
        }
        
        Position curr = new Position(0, 0);
        for (Entity ent : entities) {
            if (ent.getType().equals("player")) curr = ent.getPosition();
        }
        Position previous = curr;
        if (dist.get(curr) == null) {
            return null;
        }
        if (dist.get(curr).isInfinite()) {
            return null;
        }
        while (!curr.equals(source)) {
            previous = curr;
            curr = prev.get(curr);
        }
        return previous;
    }

    /**
     * returns a list of cardinal neighbour positions
     * @param pos
     * @return List<Position>
     */
    public List<Position> getCardinalNeighbours(Position pos) {
        List<Position> neighbours = new ArrayList<Position>();
        // above
        if (pos.getY() > 0) neighbours.add(pos.translateBy(0, -1));
        // below
        if (pos.getY() < 15) neighbours.add(pos.translateBy(0, 1));
        // left 
        if (pos.getX() > 0) neighbours.add(pos.translateBy(-1, 0));
        // right
        if (pos.getX() > 0) neighbours.add(pos.translateBy(1, 0));
        return neighbours;
    }

    
    /**
     * Moves the assassin around
     * @param entities - The list of all entities in the dungeon
     * @param direction - The direction of the character
     */
    public static void assassinMovement(List<Entity> entities, Direction direction) {
        Position player = Character.getPlayerPosition(entities);
        player = player.translateBy(direction);
        boolean swampMove = true;
        for (Entity entity : entities) {
            if (entity.getType().equals("assassin")) {
                Assassin temp = (Assassin) entity;
                swampMove = SwampTile.swampCanMove(temp, entities);
                if (swampMove == false) {
                    continue;
                }
                temp.moveEntity(entities, player, player);
            }
        }
    }  
    
    
    /**
     * Moving the assassin
     */
    public void moveEntity (List<Entity> entities, Position playerPosition, Position nextPosition) {

        List<Position> posList = posList(entities);

        Position newPos = dijkstra(posList, super.getPosition(), entities, nextPosition);
        
        Position current = super.getPosition();

        // Get the adjacent positions around assassin
        List<Position> adjacent = current.getAdjacentPositions();

        // Index 1, 3, 5 and 7 are adjacent positions, so use a temporary list holder
        List<Position> validPositions = new ArrayList<>();
        validPositions.add(adjacent.get(1));
        validPositions.add(adjacent.get(3));
        validPositions.add(adjacent.get(5));
        validPositions.add(adjacent.get(7));


        // Final placeholders for positions moved and shortest distance
        double shortestDistance = 99999999;
        Position destination = null; 

        // This looks through adjacent positions, checks whether the next square is movable
        // then checks for the shortest distance between these squares
        for (Position position : validPositions) {
            if (checkMovement(position, entities)) {
                //Checks if the next square is a door, if its not locked, then 
                Door doorEntity = checkDoor(position, entities);
                if (doorEntity != null) {
                    if (doorEntity.getLocked() == true) {
                        continue;
                    }
                }
                
                Position vector = Position.calculatePositionBetween(position, playerPosition);
                double distance = Math.sqrt(Math.pow(vector.getX(), 2) + Math.pow(vector.getY(), 2));

                if (distance <= shortestDistance) {
                    destination = position; 
                    shortestDistance = distance;
                }
            }
        }   

        // Checks whether we need to move assassin at all
        Position originalVector = Position.calculatePositionBetween(current, playerPosition);
        double originalDistance = Math.sqrt(Math.pow(originalVector.getX(), 2) + Math.pow(originalVector.getY(), 2));

        // Now moving the assassin
        if (shortestDistance < originalDistance) {
            // if dijkstra has found a shorter path, set position returned by dijkstra
            if (newPos != null) {
                super.setPosition(newPos);
            // else set position to result of pythagoreas
            } else {
                super.setPosition(destination);
            }
            
        } 
    }

   

}