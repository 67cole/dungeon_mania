package dungeonmania.entities;

import dungeonmania.util.Position;
import dungeonmania.util.Direction;

import java.util.List;
import java.util.Random;
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
                mercenaryEntity.moveEntity(entities, player);
            }
        }
    }


    /**
     * Moving the mercenary
     */
    public void moveEntity (List<Entity> entities, Position playerPosition) {
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
                    temp.moveEntity(entities, character.getPosition());
                }
            }
        }
    }
}
