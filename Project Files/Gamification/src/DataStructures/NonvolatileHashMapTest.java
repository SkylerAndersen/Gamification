package DataStructures;

import java.io.File;
import java.nio.file.Paths;

public class NonvolatileHashMapTest {
    public NonvolatileHashMapTest () {
        File file = Paths.get(System.getProperty("user.home"),"Desktop","test.bin").toFile();
        NonvolatileHashMap hashMap = new NonvolatileHashMap(file);

        // add some values
        for (int i = 0; i < 10; i++) {
            String key = ""+i;
            hashMap.add(key,i);
        }

        // retrieve some values
        for (int i = 0; i < 10; i++) {
            System.out.println(hashMap.retrieve(""+i));
        }

        // retrieve a value I added before
        System.out.println(hashMap.retrieve("hello"));

        // retrieve a value that has not been added
        System.out.println(hashMap.retrieve("hello2"));
        System.out.println(hashMap.hasKey("hello2"));
    }
}
