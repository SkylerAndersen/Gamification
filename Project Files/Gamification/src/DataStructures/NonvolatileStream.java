package DataStructures;

import java.io.File;

public class NonvolatileStream {
    private static final int BOOLEAN = 0;
    private static final int INT = 1;
    private static final int STRING = 2;
    private static final int BOOLEAN_ARRAY = 3;
    private static final int INT_ARRAY = 4;
    private static final int STRING_ARRAY = 5;
    private ByteEditor editor;
    public NonvolatileStream (File dataFile) {
        editor = new ByteEditor(dataFile.getPath());
    }
    public int add (boolean data) {
        editor.open();
        int position = editor.length();
        editor.goTo(position);
        editor.writeInt(NonvolatileStream.BOOLEAN);
        editor.writeBoolean(data);
        editor.close();
        return position;
    }
    public int add (int data) {
        editor.open();
        int position = editor.length();
        editor.goTo(position);
        editor.writeInt(NonvolatileStream.INT);
        editor.writeInt(data);
        editor.close();
        return position;
    }
    public int add (String data) {
        editor.open();
        int position = editor.length();
        editor.goTo(position);
        editor.writeInt(NonvolatileStream.STRING);
        editor.writeString(data);
        editor.close();
        return position;
    }
    public int add (boolean[] data) {
        editor.open();
        int position = editor.length();
        editor.goTo(position);
        editor.writeInt(NonvolatileStream.BOOLEAN_ARRAY);
        editor.writeBooleanArray(data);
        editor.close();
        return position;
    }
    public int add (int[] data) {
        editor.open();
        int position = editor.length();
        editor.goTo(position);
        editor.writeInt(NonvolatileStream.INT_ARRAY);
        editor.writeIntArray(data);
        editor.close();
        return position;
    }
    public int add (String[] data) {
        editor.open();
        int position = editor.length();
        editor.goTo(position);
        editor.writeInt(NonvolatileStream.STRING_ARRAY);
        editor.writeStringArray(data);
        editor.close();
        return position;
    }
    public Object retrieve (int position) {
        Object output = new Object();
        editor.open();
        editor.goTo(position);
        int dataType = editor.readInt();
        if (dataType == NonvolatileStream.BOOLEAN)
            output = (Object)editor.readBoolean();
        else if (dataType == NonvolatileStream.INT)
            output = (Object)editor.readInt();
        else if (dataType == NonvolatileStream.STRING)
            output = (Object)editor.readString();
        else if (dataType == NonvolatileStream.BOOLEAN_ARRAY)
            output = (Object)editor.readBooleanArray();
        else if (dataType == NonvolatileStream.INT_ARRAY)
            output = (Object)editor.readIntArray();
        else if (dataType == NonvolatileStream.STRING_ARRAY)
            output = (Object)editor.readStringArray();
        editor.close();
        return output;
    }

    public void clearAllData () {
        editor.open();
        editor.clearAllContents();
        editor.close();
    }
}
