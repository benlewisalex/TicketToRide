package Utils;

import java.util.ArrayList;
import java.util.List;

public enum TrainColor {
    PURPLE, //0
    WHITE, //1
    BLUE, //2
    YELLOW, //3
    ORANGE, //4
    BLACK, //5
    RED, //6
    GREEN, //7
    WILD, //8
    ANY, //9
    ERROR; //10

    private int value;
    private String name;

    private TrainColor() {
        name = super.name().toLowerCase();
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static TrainColor nameOf(String name)
    {

        switch (name.toLowerCase())
        {
            case "purple":
                return TrainColor.PURPLE;
            case "white":
                return TrainColor.WHITE;
            case "blue":
                return TrainColor.BLUE;
            case "yellow":
                return TrainColor.YELLOW;
            case "orange":
                return TrainColor.ORANGE;
            case "black":
                return TrainColor.BLACK;
            case "red":
                return TrainColor.RED;
            case "green":
                return TrainColor.GREEN;
            case "wild":
                return TrainColor.WILD;
            case "any":
                return TrainColor.ANY;
            default:
                System.out.println("Error invalid color name in TrainColor -> nameOf: " + name);
                return TrainColor.ERROR;
        }
    }

    private void setValue(int value) {
        this.value = value;
    }

    private static final List<TrainColor> colors = new ArrayList<TrainColor>();
    static {
        for (TrainColor color : TrainColor.values()) {
            color.setValue(colors.size());
            colors.add(color);
        }
    }

    public static TrainColor valueOf(int value) {
        return colors.get(value);
    }

    public String getName() {
        return name;
    }
    public int getValue() {
        return value;
    }
}
