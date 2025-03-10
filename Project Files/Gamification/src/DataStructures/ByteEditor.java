package DataStructures;

import java.io.File;
import java.io.RandomAccessFile;

public class ByteEditor {
    private final String filePath;
    private RandomAccessFile file;
    private boolean open;
    private boolean readOnly;
    public ByteEditor(String filePath) {
        this.filePath = filePath;
        open = false;
        readOnly = false;
    }
    public void open () {
        open(false);
    }
    public void open (boolean readOnly) {
        try {
            File tempFile = new File(filePath);
            if (readOnly && !tempFile.exists()) {
                tempFile.createNewFile();
            }
            file = new RandomAccessFile(filePath, readOnly ? "r" : "rw");
            open = true;
            this.readOnly = readOnly;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int getPosition () {
        if (!open)
            return -1;

        try {
            return (int) file.getFilePointer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    public int length () {
        if (!open)
            return -1;

        try {
            return (int) file.length();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    public void goTo (long bytePos) {
        if (!open)
            return;

        try {
            file.seek(bytePos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void close () {
        try {
            file.close();
            open = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void clearAllContents () {
        if (!open || readOnly)
            return;

        try {
            file.setLength(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public byte[] readBytes (int numBytes) {
        if (!open)
            return new byte[0];

        byte[] output = new byte[numBytes];
        try {
            file.read(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
    public int readInt () {
        if (!open)
            return -1;

        byte[] data = readBytes(4);
        int output = 0;
        for (int i = 3; i >= 0; i--) {
            // convert each byte that makes up the int to its own int
            // conversions may have caused sign extension, (i.e. -38=[11011000]_byte=[1...111011000]_int)
            // remove sign extension, (i.e. -38=[11011000]_byte -> [0...011011000]_int)
            int nonSignExtendedValue = ((int)data[3-i]) & 0xFF;
            // shift values so each bit is aligned with its final place value
            int shiftedValue = nonSignExtendedValue << (i*8);
            // OR the value with output to combine all values into one int
            output = output | shiftedValue;
        }
        return output;
    }
    public boolean readBoolean () {
        if (!open)
            return false;

        byte[] data = readBytes(1);
        return (data[0] != 0);
    }
    public String readString () {
        if (!open)
            return "";

        byte[] data = null;
        int stringLength = -1;

        // find the next null, and read string byte data, and move file pointer beyond the null
        try {
            int start = (int)file.getFilePointer();
            data = readBytes((int)file.length()-start);
            goTo(start);
            for (int i = 0; i < data.length; i++) {
                if (data[i] == 0) {
                    stringLength = i;
                    break;
                }
            }
            data = readBytes(stringLength);
            goTo(start+stringLength+1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // if no null is found
        if (stringLength == -1 || data == null)
            return "";

        // get String from data
        StringBuilder output = new StringBuilder(data.length);
        for (int i = 0; i < data.length; i++) {
            output.append((char)data[i]);
        }

        // output
        return output.toString();
    }
    public String[] readStringArray () {
        if (!open)
            return new String[0];

        // read string array
        int length = readInt();
        String[] output = new String[length];
        for (int i = 0; i < length; i++)
            output[i] = readString();

        return output;
    }
    public int[] readIntArray () {
        if (!open)
            return new int[0];

        // read int array
        int length = readInt();
        int[] output = new int[length];
        for (int i = 0; i < length; i++)
            output[i] = readInt();

        return output;
    }
    public boolean[] readBooleanArray () {
        if (!open)
            return new boolean[0];

        // read boolean array
        int length = readInt();
        boolean[] output = new boolean[length];
        for (int i = 0; i < length; i++)
            output[i] = readBoolean();

        return output;
    }
    public void writeBytes (byte[] bytes) {
        if (!open || readOnly)
            return;

        try {
            file.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void writeInt (int value) {
        byte[] content = new byte[4];
        content[0] = (byte)(value >> 24);
        content[1] = (byte)(value >> 16 & 0xFF);
        content[2] = (byte)(value >> 8 & 0xFF);
        content[3] = (byte)(value & 0xFF);
        writeBytes(content);
    }
    public void writeBoolean (boolean value) {
        byte[] content = new byte[1];
        content[0] = value ? (byte)0x01 : (byte)0x00;
        writeBytes(content);
    }
    public void writeString (String text) {
        byte[] content = new byte[text.length()+1];
        for (int i = 0; i < text.length(); i++) {
            content[i] = (byte)text.charAt(i);
        }
        content[content.length-1] = (byte)0;
        writeBytes(content);
    }
    public void writeIntArray (int[] values) {
        writeInt(values.length);
        for (int i = 0; i < values.length; i++)
            writeInt(values[i]);
    }
    public void writeBooleanArray (boolean[] values) {
        writeInt(values.length);
        for (int i = 0; i < values.length; i++)
            writeBoolean(values[i]);
    }
    public void writeStringArray (String[] values) {
        writeInt(values.length);
        for (int i = 0; i < values.length; i++)
            writeString(values[i]);
    }
}
