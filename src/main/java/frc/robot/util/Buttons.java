package frc.robot.util;

import lombok.Getter;

public enum Buttons {
    TRIGGER(1),
    TOP_CENTER(2),
    TOP_LEFT(3),
    TOP_RIGHT(4); // TODO verify this


    @Getter public final int id;

    Buttons(int id) {
        this.id = id;
    }
}
