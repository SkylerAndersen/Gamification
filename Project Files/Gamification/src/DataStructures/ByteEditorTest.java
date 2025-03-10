package DataStructures;

import java.nio.file.Paths;
import java.util.Arrays;

public class ByteEditorTest {
    public ByteEditorTest () {
        String filePath = Paths.get(System.getProperty("user.home"),"Desktop","test.bin").toString();
        ByteEditor editor = new ByteEditor(filePath);

        // generate test data
        int num = 1;
        boolean condition = true;
        String text = "Hello, World!";
        int[] nums = {1,2,3};
        boolean[] conditions = {true,false,true};
        String[] textBlock = {"Hello","Goodbye","How are you?"};

        // test writing
        editor.open();
        editor.writeInt(num);
        editor.writeBoolean(condition);
        editor.writeString(text);
        editor.writeStringArray(textBlock);
        editor.writeIntArray(nums);
        editor.writeBooleanArray(conditions);
        editor.close();

        // generate place for output
        int numOutput = -1;
        boolean conditionOutput = false;
        String textOutput = "";
        int[] numsOutput = {0,0,0};
        boolean[] conditionsOutput = {false,false,false};
        String[] textBlockOutput = {"","",""};

        // test reading
        editor.open(true);
        numOutput = editor.readInt();
        conditionOutput = editor.readBoolean();
        textOutput = editor.readString();
        textBlockOutput = editor.readStringArray();
        numsOutput = editor.readIntArray();
        conditionsOutput = editor.readBooleanArray();
        editor.close();

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
