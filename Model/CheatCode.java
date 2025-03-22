package Model;

import java.util.HashSet;
import java.util.List;

public class CheatCode {
    private static final HashSet<String> cheatCodes = new HashSet<>(List.of("GODMODE", "EZWIN", "GAMEOVER", "GHOST"));

    public static boolean isCheatCode(String input) {
        return cheatCodes.contains(input);
    }
}
