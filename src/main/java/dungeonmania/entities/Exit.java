package dungeonmania.entities;

import java.util.List;

import dungeonmania.util.Position;

public class Exit extends StaticEntity {

    public Exit(Position position, String type, String ID, boolean isInteractable, String colour) {
        super(position,type, ID, isInteractable, colour);
    }

    @Override
    public void entityFunction(List<Entity> entities, Character player) {
    }
}
