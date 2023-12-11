package brickGame;

import java.io.Serializable;

/**
 * BlockSerializable is used to serialize the game data
 */
public class BlockSerializable implements Serializable {
    public final int row;
    public final int j;
    public final int type;

    public BlockSerializable(int row , int j , int type) {
        this.row = row;
        this.j = j;
        this.type = type;
    }
}