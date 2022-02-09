package org.mayheminc.util;

import java.nio.*;
import java.text.DecimalFormat;

public class ObjectLocation {
    public enum ObjectTypes {
        OBJ_NONE,
        OBJ_CUBE,
        OBJ_SCALE_CENTER,
        OBJ_SCALE_BLUE,
        OBJ_SCALE_RED,
        OBJ_SWITCH_RED,
        OBJ_SWITCH_BLUE,
        OBJ_PORTAL_RED,
        OBJ_PORTAL_BLUE,
        OBJ_EXCHANGE_RED,
        OBJ_EXCHANGE_BLUE,
        OBJ_BUMPERS_RED,
        OBJ_BUMPERS_BLUE,
        OBJ_BUMPERS_CLASS13,
        OBJ_BUMPERS_CLASS14,
        OBJ_BUMPERS_CLASS15,
        OBJ_BUMPERS_CLASS16,
        OBJ_BUMPERS_CLASS17,
        OBJ_BUMPERS_CLASS18,
        OBJ_BUMPERS_CLASS19,
        OBJ_BUMPERS_CLASS20,
        OBJ_EOL,
    };

    public ObjectTypes type;
    public float x;
    public float y;
    public float width;
    public float height;
    public float probability;

    public ObjectLocation() {
        type = ObjectTypes.OBJ_NONE;
        x = y = width = height = probability = 0;
    }

    public ObjectLocation(ByteBuffer buffer) {
        type = ObjectTypes.values()[buffer.getInt()];
        x = (float)buffer.getInt() / Integer.MAX_VALUE;
        y = (float)buffer.getInt() / Integer.MAX_VALUE;
        width = (float)buffer.getInt() / Integer.MAX_VALUE;
        height = (float)buffer.getInt() / Integer.MAX_VALUE;
        probability = (float)buffer.getInt() / Integer.MAX_VALUE;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.00");

        return type.name() + "@" + df.format(x) + "x" + df.format(y) + "+" + df.format(width) + "x" + df.format(height) + "[" + df.format(probability * 100) + "%]";
    }
}
