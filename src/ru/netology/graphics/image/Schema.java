package ru.netology.graphics.image;

public class Schema implements TextColorSchema {
    char [] chars = {'#', '$', '@', '%', '*', '+', '-', '"'};
    @Override
    public char convert(int color) {
        return chars[color/(225/8)];
    }
}
