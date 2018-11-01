package Utils;

import java.util.ArrayList;
import java.util.List;

public enum PlayerColor {
    BLUE,
    RED,
    BLACK,
    GREEN,
    YELLOW;

    private int value;
    private String name;

    private PlayerColor() {
        name = super.name().toLowerCase();
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private void setValue(int value) {
        this.value = value;
    }

    private static final List<PlayerColor> colors = new ArrayList<PlayerColor>();
    static {
        for (PlayerColor color : PlayerColor.values()) {
            color.setValue(colors.size());
            colors.add(color);
        }
    }

    public static PlayerColor valueOf(int value) {
        return colors.get(value);
    }

    public String getName() {
        return name;
    }
    public int getValue() {
        return value;
    }
}
