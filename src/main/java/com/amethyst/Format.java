package com.amethyst;

public class Format {
    public static final char SECTION = '\u00A7';

    // Color codes
    public static final String BLACK = SECTION + "0";
    public static final String DARK_BLUE = SECTION + "1";
    public static final String DARK_GREEN = SECTION + "2";
    public static final String DARK_AQUA = SECTION + "3";
    public static final String DARK_RED = SECTION + "4";
    public static final String DARK_PURPLE = SECTION + "5";
    public static final String GOLD = SECTION + "6";
    public static final String GRAY = SECTION + "7";
    public static final String DARK_GRAY = SECTION + "8";
    public static final String BLUE = SECTION + "9";
    public static final String GREEN = SECTION + "a";
    public static final String AQUA = SECTION + "b";
    public static final String RED = SECTION + "c";
    public static final String LIGHT_PURPLE = SECTION + "d";
    public static final String YELLOW = SECTION + "e";
    public static final String WHITE = SECTION + "f";

    // Formatting codes
    public static final String OBFUSCATED = SECTION + "k";
    public static final String BOLD = SECTION + "l";
    public static final String STRIKETHROUGH = SECTION + "m";
    public static final String UNDERLINE = SECTION + "n";
    public static final String ITALIC = SECTION + "o";
    public static final String RESET = SECTION + "r";

    // Utility: Translate &-codes to section codes
    public static String format(String message) {
        return message.replaceAll("&([0-9a-fk-or])", SECTION + "$1");
    }
}