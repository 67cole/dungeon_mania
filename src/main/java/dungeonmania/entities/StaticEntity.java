package dungeonmania.entities;

public abstract class StaticEntity implements Entity{
    private int x;
    private int y;
    private String type;

    StaticEntity(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;    
    }  
    
    @Override
    public int getX() {
        return this.x;
    }
    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public String getType() {
        return this.type;
    }
}
