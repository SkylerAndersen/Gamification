import java.util.Arrays;
import java.util.HashSet;

public class Main {
    /**
     * Method to intended to simply run the application. No core logic here.
     * @param args -d for debug mode, blank otherwise
     * */
    public static void main (String[] args) {
        HashSet<String> flags = new HashSet<>(args.length);
        flags.addAll(Arrays.asList(args));

        // -d for debug mode / development build etc
        QuestifyApplication application = new QuestifyApplication(flags.contains("-d"));
        application.openGui();
    }
}