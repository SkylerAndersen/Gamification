package DataStructures;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

public class NonvolatileStreamTest {
    public NonvolatileStreamTest () {
        File file = Paths.get(System.getProperty("user.home"),"Desktop","test.bin").toFile();
        NonvolatileStream stream = new NonvolatileStream(file);

        // generate test data
        int num = 1;
        boolean condition = true;
        String text = "Hello, World!";
        int[] nums = {1,2,3};
        boolean[] conditions = {true,false,true};
        String[] textBlock = {"Hello","Goodbye","How are you?"};

//        // test writing
//        int location1 = stream.add(num);
//        int location2 = stream.add(condition);
//        int location3 = stream.add(text);
//        int location4 = stream.add(textBlock);
//        int location5 = stream.add(nums);
//        int location6 = stream.add(conditions);

        // use previous locations, written before
        int location1 = 0;
        int location2 = 8;
        int location3 = 13;
        int location4 = 31;
        int location5 = 66;
        int location6 = 86;

        // print locations
        System.out.println(location1);
        System.out.println(location2);
        System.out.println(location3);
        System.out.println(location4);
        System.out.println(location5);
        System.out.println(location6);

        // generate place for output
        int numOutput = -1;
        boolean conditionOutput = false;
        String textOutput = "";
        int[] numsOutput = {0,0,0};
        boolean[] conditionsOutput = {false,false,false};
        String[] textBlockOutput = {"","",""};

        // test reading
        numOutput = (int) stream.retrieve(location1);
        conditionOutput = (boolean) stream.retrieve(location2);
        textOutput = (String) stream.retrieve(location3);
        textBlockOutput = (String[]) stream.retrieve(location4);
        numsOutput = (int[]) stream.retrieve(location5);
        conditionsOutput = (boolean[]) stream.retrieve(location6);

        // output results
        boolean numMatch = num == numOutput;
        boolean conditionMatch = condition == conditionOutput;
        boolean textMatch = text.equals(textOutput);
        boolean textBlockMatch = Arrays.equals(textBlock, textBlockOutput);
        boolean numsMatch = Arrays.equals(nums, numsOutput);
        boolean conditionsMatch = Arrays.equals(conditions, conditionsOutput);
        System.out.println("Num Match: " + numMatch);
        System.out.println("Condition Match: " + conditionMatch);
        System.out.println("Text Match: " + textMatch);
        System.out.println("Text Block Match: " + textBlockMatch);
        System.out.println("Nums Match: " + numsMatch);
        System.out.println("Conditions Match: " + conditionsMatch);
    }
}
