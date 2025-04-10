package DataStructures;

public class PlayerData {
    public static String name = "Player";
    public static int coins = 0;
    public static GenericItem[] items = new GenericItem[0];
    public static int level = 1;
    public static int xp = 0;

    public static String currentUser = null;

    public static void save(FileHandler fileHandler) {
        if (currentUser == null) return;

        fileHandler.save(currentUser + "_name", name);
        fileHandler.save(currentUser + "_coins", coins);
        fileHandler.save(currentUser + "_level", level);
        fileHandler.save(currentUser + "_xp", xp);
    }

    public static void load(FileHandler fileHandler) {
        if (currentUser == null) return;

        name = fileHandler.retrieveString(currentUser + "_name");

        int loadedCoins = fileHandler.retrieveInt(currentUser + "_coins");
        coins = (loadedCoins == -1) ? 0 : loadedCoins;

        int loadedLevel = fileHandler.retrieveInt(currentUser + "_level");
        level = (loadedLevel == -1) ? 1 : loadedLevel;

        int loadedXp = fileHandler.retrieveInt(currentUser + "_xp");
        xp = (loadedXp == -1) ? 0 : loadedXp;
    }
}