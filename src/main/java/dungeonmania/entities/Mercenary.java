package dungeonmania.entities;

import dungeonmania.util.Position;
import dungeonmania.util.Direction;

import java.util.List;
import java.util.Random;

import javax.xml.stream.events.EntityDeclaration;

import java.lang.Math;
import java.util.ArrayList;

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

    /**
     * Moving the mercenary
     */
    public void moveEntity (List<Entity> entities, Position playerPosition) {
        // TODO: NEW FUNCTION
        // Position movementDir = djikstra(entities, playerPosition);
        // super.setPosition(super.getPosition().translateBy(movementDir));

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
