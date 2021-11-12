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


public class Mercenary extends MovingEntity {
    private final static int STARTING_HEALTH = 3;
    private final static int ATTACK = 2;

    /**
     * Creates the mercenary
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     */
    public Mercenary(Position position, String type, String ID, boolean isInteractable) {
        super(position, type, ID, isInteractable);
        setHealth(STARTING_HEALTH);
        setAttack(ATTACK);
        Random random = new Random();
        int chance = random.nextInt(6);
        if (chance == 3) {
            setArmour(true);
        }
    }

    // returns a list of walkable positions
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

    public int cost (List<Entity> entities, Position source, Position dest) {

        for (Entity ent : entities) {
            if (ent.getPosition().equals(dest) && ent.getType().equals("swamp_tile")) {
                System.out.println("HELALEPOWKADPOAKSD");
                return ((SwampTile) ent).getMovementFactor();
            }
        }

        return 1;
    }

    public Position djikstra(List<Position> posList, Position source, List<Entity> entities) {
        
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
        // let queuePos be a queue of every position in the grid
        // for (Position pos : posList) {
        //     posQueue.add(pos);
        // }
        posQueue.add(source);
        // Queue<Position> unvisited = posQueue;
        // iterate through each position thru queuePos, use temp as reference
        // pop items off temp (think of it as unvisited)
        while(posQueue.size() > 0) {
            // find next unvisited node with smallest dist
            Position pos = posQueue.poll();
            List<Position> cardinalNeighbours = getCardinalNeighbours(pos);
            for (Position currNeighbour : cardinalNeighbours) {
                if (!dist.containsKey(currNeighbour)) continue;
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
        if (dist.get(curr).isInfinite()) {
            return null;
        }
        while (!curr.equals(source)) {
            System.out.println(curr);
            previous = curr;
            curr = prev.get(curr);
        }
        return previous;
    }

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
     * Moving the mercenary
     */
    public void moveEntity (List<Entity> entities, Position playerPosition) {

        List<Position> posList = posList(entities);

        Position newPos = djikstra(posList, super.getPosition(), entities);
        if (newPos != null) {
            super.setPosition(newPos);
        } else {
            Position current = super.getPosition();

            // Get the adjacent positions around mercenary
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

            // Checks whether we need to move mercenary at all
            Position originalVector = Position.calculatePositionBetween(current, playerPosition);
            double originalDistance = Math.sqrt(Math.pow(originalVector.getX(), 2) + Math.pow(originalVector.getY(), 2));

            // Now moving the mercenary
            if (shortestDistance < originalDistance) super.setPosition(destination);
        }
    }

   
    
}
