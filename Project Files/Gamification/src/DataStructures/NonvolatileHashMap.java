package DataStructures;

import java.io.File;

public class NonvolatileHashMap {
    private File file;
    private ByteEditor fileEditor;
    private KeyValuePair[] array;
    private int size;
    public NonvolatileHashMap (File addressingFile) {
        this.file = addressingFile;
        this.fileEditor = new ByteEditor(file.getPath());
        fileEditor.open(true);

        // if hash map has been loaded before, reload from disk
        if (fileEditor.length() >= 2) {
            fileEditor.goTo(0);
            size = fileEditor.readInt();
            array = new KeyValuePair[fileEditor.readInt()];
            for (int i = 0; i < size; i++) {
                String key = fileEditor.readString();
                int value = fileEditor.readInt();
                int index = fileEditor.readInt();
                array[index] = new KeyValuePair(key, value);
            }
        } else {
            resize(10);
        }
        fileEditor.close();
    }

    public void add (String key, int value) {
        boolean newKey = !hasKey(key);
        if (newKey)
            size++;

        // resize if neccessary
        if (size >= 7*array.length/10)
            resize(array.length*2);

        // if this is a new key
        if (!hasKey(key)) {
            int index = addKeyValuePair(key,value);
            fileEditor.open();
            fileEditor.goTo(0);
            fileEditor.writeInt(size);
            fileEditor.goTo(fileEditor.length());
            fileEditor.writeString(key);
            fileEditor.writeInt(value);
            fileEditor.writeInt(index);
            fileEditor.close();
            return;
        }

        // if the key already exists, find where the data is in the file
        int position = -1;
        fileEditor.open(true);
        fileEditor.goTo(0);
        fileEditor.readInt();
        fileEditor.readInt();
        for (int i = 0; i < size; i++) {
            String currentKey = fileEditor.readString();
            position = fileEditor.getPosition();
            if (currentKey.equals(key))
                break;
            fileEditor.readInt();
            fileEditor.readInt();
        }
        fileEditor.close();

        // replace the data associated with the key in the file
        fileEditor.open();
        fileEditor.goTo(position);
        fileEditor.writeInt(value);
        fileEditor.close();

        // replace key value pair in volatile memory (in array)
        addKeyValuePair(key,value);
    }

    public int retrieve (String key) {
        int index = hash(key);
        for (int i = 0; i < array.length; i++) {
            if (array[index] == null)
                return -1;
            else if (array[index].key().equals(key))
                return array[index].value();

            index = (index + 1) % array.length;
        }
        return -1;
    }

    public boolean hasKey (String key) {
        int index = hash(key);
        for (int i = 0; i < array.length; i++) {
            if (array[index] == null)
                return false;
            else if (array[index].key().equals(key))
                return true;

            index = (index + 1) % array.length;
        }
        return false;
    }

    private int hash (String key) {
        int output = 0;
        for (int i = 0; i < key.length(); i++)
            output = (output*31+key.charAt(i)) % array.length;

        return output;
    }

    private int addKeyValuePair (String key, int value) {
        int index = hash(key);

        // resolve collisions
        for (int i = 0; i < array.length; i++) {
            if (array[index] == null || array[index].key().equals(key))
                break;

            index = (index + 1) % array.length;
        }

        // add pair to array
        array[index] = new KeyValuePair(key,value);
        return index;
    }

    private void resize (int capacity) {
        if (capacity < this.size)
            return;

        // create resized array
        KeyValuePair[] old = array;
        array = new KeyValuePair[capacity];
        for (int i = 0; old != null && i < old.length; i++) {
            if (old[i] == null)
                continue;
            addKeyValuePair(old[i].key(),old[i].value());
        }

        // clear file
        fileEditor.open();
        fileEditor.clearAllContents();
        fileEditor.close();

        // write all data to file
        fileEditor.open();
        fileEditor.writeInt(this.size);
        fileEditor.writeInt(capacity);
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null)
                continue;
            fileEditor.writeString(array[i].key());
            fileEditor.writeInt(array[i].value());
            fileEditor.writeInt(i);
        }
        fileEditor.close();
    }

    public int getCapacity () {
        return array.length;
    }

    public int getSize () {
        return size;
    }
}
