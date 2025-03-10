package DataStructures;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class FileHandler {
    private String projectPath;
    private NonvolatileStream stream;
    private NonvolatileHashMap hashMap;
    public FileHandler (String projectPath) {
        this.projectPath = projectPath;
        File hashMapFile = new File(projectPath + "addressing.bin");
        File streamFile = new File(projectPath + "data.bin");
        stream = new NonvolatileStream(streamFile);
        hashMap = new NonvolatileHashMap(hashMapFile);
    }

    public boolean retrieveBoolean (String name) {
        if (!hashMap.hasKey(name))
            return false;

        int location = hashMap.retrieve(name);
        return (boolean) stream.retrieve(location);
    }

    public void save (String name, boolean data) {
        int location = stream.add(data);
        hashMap.add(name,location);
    }

    public int retrieveInt (String name) {
        if (!hashMap.hasKey(name))
            return -1;

        int location = hashMap.retrieve(name);
        return (int) stream.retrieve(location);
    }

    public void save (String name, int data) {
        int location = stream.add(data);
        hashMap.add(name,location);
    }

    public String retrieveString (String name) {
        if (!hashMap.hasKey(name))
            return "";

        int location = hashMap.retrieve(name);
        return (String) stream.retrieve(location);
    }

    public void save (String name, String data) {
        int location = stream.add(data);
        hashMap.add(name,location);
    }

    public boolean[] retrieveBooleanArray (String name) {
        if (!hashMap.hasKey(name))
            return new boolean[0];

        int location = hashMap.retrieve(name);
        return (boolean[]) stream.retrieve(location);
    }

    public void save (String name, boolean[] data) {
        int location = stream.add(data);
        hashMap.add(name,location);
    }

    public int[] retrieveIntArray (String name) {
        if (!hashMap.hasKey(name))
            return new int[0];

        int location = hashMap.retrieve(name);
        return (int[]) stream.retrieve(location);
    }

    public void save (String name, int[] data) {
        int location = stream.add(data);
        hashMap.add(name,location);
    }

    public String[] retrieveStringArray (String name) {
        if (!hashMap.hasKey(name))
            return new String[0];

        int location = hashMap.retrieve(name);
        return (String[]) stream.retrieve(location);
    }

    public void save (String name, String[] data) {
        int location = stream.add(data);
        hashMap.add(name,location);
    }

    public ImageIcon retrieveImage (String name) {
        if (projectPath == null)
            return new ImageIcon();

        ImageIcon output = new ImageIcon();
        try {
            BufferedImage image = ImageIO.read(new File(projectPath+name+".png"));
            output = new ImageIcon(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public void save (String name, ImageIcon image) {
        if (projectPath == null)
            return;

        // get a rendered image from our ImageIcon
        Image imageToWrite = image.getImage();
        // if the ImageIcon's Image is not already a BufferedImage, i.e. not a RenderedImage
        if (!(imageToWrite instanceof BufferedImage)) {
            BufferedImage bufferedImage = new BufferedImage(imageToWrite.getWidth(null),
                    imageToWrite.getHeight(null),BufferedImage.TYPE_INT_ARGB);
            bufferedImage.getGraphics().drawImage(imageToWrite,0,0,null);
            imageToWrite = bufferedImage;
        }

        // write image to file
        try {
            ImageIO.write((BufferedImage) imageToWrite, "png",
                    new File(projectPath+name+".png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
