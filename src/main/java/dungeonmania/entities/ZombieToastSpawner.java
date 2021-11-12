package dungeonmania.entities;

import java.util.List;
import java.util.ArrayList;
import dungeonmania.entities.CollectableEntities.Sword;
import dungeonmania.util.Position;
import dungeonmania.Dungeon;

public class ZombieToastSpawner extends StaticEntity{
    public ZombieToastSpawner(Position position, String type, String ID, boolean isInteractable) {
        super(position,type, ID, isInteractable);
    }


    /**
     * Checks whether the player is close enough to the spawner
     * @param character - the character class
     * @param interaction - the entity the character is interacting with, spawner in this case
     */
    public static boolean playerProximitySpawner(Character character, Entity interaction) {
        // First, wrap the entity and get its position
        ZombieToastSpawner spawner = (ZombieToastSpawner) interaction;
        Position spawnerPosition = spawner.getPosition();

        // Checking the positions around the spawner
        List<Position> adjacent = spawnerPosition.getAdjacentPositions();

        // Index 1, 3, 5, 7 are cardinally adjacent positions, so place into temporary list holder
        List<Position> validPositions = new ArrayList<>();
        validPositions.add(adjacent.get(1));
        validPositions.add(adjacent.get(3));
        validPositions.add(adjacent.get(5));
        validPositions.add(adjacent.get(7));

        // Now, get player position and check if they're in any of these squares
        Position characterPosition = character.getPosition();

        for (Position position : validPositions) {
            if (position.equals(characterPosition)) return true;
        }

        return false; 
    }


    /**
     * Check if white space is empty or not for zombies to spawn in
     * @param position - the position of the zombie toast spawner
     * @param entities - the list of all the entities
     */
    public static Position checkWhiteSpace(Position position, List<Entity> entities) {
        List<Position> adjacents = position.getAdjacentPositions();

        // List Position 1, 3, 5, 7 are adjacent
        for (int i = 1; i < adjacents.size(); i = i + 2) {
            Position temp = adjacents.get(i);
            int check = 0;

            for (Entity entity : entities) {
                if (entity.getPosition().equals(temp)) {
                    check = 1; 
                    break;
                } 
            }

            if (check == 0) {
                return temp;
            }
        }   

        return null;
    }
}
