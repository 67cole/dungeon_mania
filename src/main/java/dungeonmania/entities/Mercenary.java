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
    private final static int STARTING_HEALTH = 4;
    private final static int ATTACK = 5;
    private boolean friendly;

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
        this.friendly = false;
    }

    public boolean getFriendly() {
        return this.friendly;
    }

    public void setFriendly() {
        this.friendly = true;
    }

    // returns a list of walkable positions
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
        curr = nextPosition;
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
     * Moves the mercenary around
     * @param entities - The list of all entities in the dungeon
     * @param direction - The direction of the character
     */
    public static void mercenaryMovement(List<Entity> entities, Direction direction) {
        Position player = Character.getPlayerPosition(entities);
        player = player.translateBy(direction);

        boolean swampMove = true;
        for (Entity entity : entities) {
            if (entity.getType().equals("mercenary")) {
                Mercenary mercenaryEntity = (Mercenary) entity;
                swampMove = SwampTile.swampCanMove(mercenaryEntity, entities);
                if (swampMove == false) {
                    continue;
                }             
                if (!mercenaryEntity.getFriendly()) mercenaryEntity.moveEntity(entities, player, player);
            }
        }
    }

    /**
     * Moves the mercenary around
     * @param entities - The list of all entities in the dungeon
     */
    public static void allyMercenaryMovement(List<Entity> entities) {
        Position player = Character.getPlayerPosition(entities);

        boolean swampMove = true;
        for (Entity entity : entities) {
            if (entity.getType().equals("mercenary")) {
                Mercenary mercenaryEntity = (Mercenary) entity;
                swampMove = SwampTile.swampCanMove(mercenaryEntity, entities);
                if (swampMove == false) {
                    continue;
                }             
                if (mercenaryEntity.getFriendly()) mercenaryEntity.moveEntity(entities, player, player);
            }
        }
    }

    /**
     * moves the mercenary
     * @param entities
     * @param playerPosition
     */
    public void moveEntity (List<Entity> entities, Position playerPosition, Position nextPosition) {

        List<Position> posList = posList(entities);

        Position newPos = dijkstra(posList, super.getPosition(), entities, nextPosition);
        
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
        double shortestDistance = Double.POSITIVE_INFINITY;
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
        if (shortestDistance < originalDistance || this.getFriendly()) {
            if (newPos != null) {
                if (newPos.equals(playerPosition) && this.getFriendly()) {

                    if (!destination.equals(playerPosition)) {
                        super.setPosition(destination);
                        return;
                    }

                    return;
                }

                super.setPosition(newPos);

            } else {
                if (destination.equals(playerPosition) && this.getFriendly()) {
                    return;
                }
                
                super.setPosition(destination);  
            }
            
        } 
    }

    /**
     * Checks whether the place is close enough to the mercenary to bribe
     * @param character - the character class
     * @param interaction - the entity the character is interacting with, mercenary in this case
     */
    public static boolean playerProximityMercenary(Character character, Entity interaction) {
        // First, wrap the entity and get its position
        Mercenary mercenary = (Mercenary) interaction;
        Position mercenaryPosition = mercenary.getPosition();

        // Checking the positions around the mercenary
        List<Position> adjacent = mercenaryPosition.getAdjacentPositions();

        // Index 1, 3, 5, 7 are cardinally adjacent positions, so place into temporary list holder
        List<Position> validPositions = new ArrayList<>();

        // However, also add one tiles to the left, right, up and down as it is 2 cardinal tiles
        validPositions.add(adjacent.get(1));
        validPositions.add(adjacent.get(1).translateBy(0, -1));
        validPositions.add(adjacent.get(3));
        validPositions.add(adjacent.get(3).translateBy(1, 0));
        validPositions.add(adjacent.get(5));
        validPositions.add(adjacent.get(5).translateBy(0, 1));
        validPositions.add(adjacent.get(7));
        validPositions.add(adjacent.get(7).translateBy(-1, 0));

        // Now, get player position and check if they're in any of these squares
        Position characterPosition = character.getPosition();

        for (Position position : validPositions) {
            if (position.equals(characterPosition)) return true;
        }

        return false; 
    }

    /**
     * This function checks whether there are any players in the mercenary's battle radius
     * @param character - the player
     * @param entities - the list of all entities
     */
    public static void mercenaryBattleRadiusChecker(MovingEntity character, List<Entity> entities) {
        for (Entity entity : entities) {
            if (entity.getType().equals("mercenary")) {
                Position vector = Position.calculatePositionBetween(character.getPosition(), entity.getPosition());
                double distance = Math.sqrt(Math.pow(vector.getX(), 2) + Math.pow(vector.getY(), 2));

                if (distance < 4) {
                    Mercenary temp = (Mercenary) entity;
                    temp.moveEntity(entities, character.getPosition(), null);
                }
            }
        }
    }

    /**
     * This function checks whether or not the mercenary can battle the enemy
     * @param enemy
     * @return boolean
     */
    public boolean mercenaryBattle(MovingEntity enemy) {
        Position vector = Position.calculatePositionBetween(enemy.getPosition(), this.getPosition());
        double distance = Math.sqrt(Math.pow(vector.getX(), 2) + Math.pow(vector.getY(), 2));
        if (distance < 4) {
            return true;
        }
        return false;
    }
}
