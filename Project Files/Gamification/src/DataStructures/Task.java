package DataStructures;

/**
 * A single to-do task, with a description, XP reward, and status.
 */
public class Task {
    public enum Status { IN_PROGRESS, COMPLETED }

    private final String description;
    private final int xp;
    private Status status;

    /** Now includes xp in the constructor */
    public Task(String description, int xp, Status status) {
        this.description = description;
        this.xp          = xp;
        this.status      = status;
    }

    public String getDescription() { return description; }
    public int    getXp()          { return xp; }
    public Status getStatus()      { return status; }
    public void   setStatus(Status status) { this.status = status; }

    /**
     * Serialize to a single String so it can be written with FileHandler.
     * Format: description|xp|IN_PROGRESS (or COMPLETED)
     */
    public String serialize() {
        return description + "|" + xp + "|" + status.name();
    }

    public static Task deserialize(String s) {
        String[] parts = s.split("\\|");
        String desc    = parts[0];
        int xpValue    = Integer.parseInt(parts[1]);
        Status st      = Status.valueOf(parts[2]);
        return new Task(desc, xpValue, st);
    }

    @Override
    public String toString() {
        return description + " (" + xp + " XP)";
    }
}
