package DataStructures;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

public class FileHandlerTest {
    private FileHandler handler;
    public FileHandlerTest () {
        // setup FileHandler
        String projectPath = Paths.get(System.getProperty("user.dir"),"resources",".").toString();
        projectPath = projectPath.substring(0,projectPath.length()-1);
        handler = new FileHandler(projectPath);
//        testImage();
        testOtherTypes();
    }

    private void testOtherTypes () {
        // generate test data
        int num = 1;
        boolean condition = true;
        String text = "Hello, World!";
        int[] nums = {1,2,3};
        boolean[] conditions = {true,false,true};
        String[] textBlock = {"Hello","Goodbye","How are you?"};

        // test writing
//        handler.save("data0",num);
//        handler.save("data1",condition);
//        handler.save("data2",text);
//        handler.save("data3",textBlock);
//        handler.save("data4",nums);
//        handler.save("data5",conditions);

        // generate place for output
        int numOutput = -1;
        boolean conditionOutput = false;
        String textOutput = "";
        int[] numsOutput = {0,0,0};
        boolean[] conditionsOutput = {false,false,false};
        String[] textBlockOutput = {"","",""};

        // test reading
        numOutput = handler.retrieveInt("data0");
        conditionOutput = handler.retrieveBoolean("data1");
        textOutput = handler.retrieveString("data2");
        textBlockOutput = handler.retrieveStringArray("data3");
        numsOutput = handler.retrieveIntArray("data4");
        conditionsOutput = handler.retrieveBooleanArray("data5");

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

    private void testImage () {
        // create a buffer for an image
        int h = 5, w = 10;
        int white = 0xFFFFFFFF;
        int black = 0xFF000000;
        int[] pixels = {white,white,white,black,white,white,black,white,white,white,
                white,white,white,black,white,white,black,white,white,white,
                white,white,white,black,white,white,black,white,white,white,
                black,white,white,white,white,white,white,white,white,black,
                white,black,black,black,black,black,black,black,black,white};

        // create two images, one is a BufferedImage, the other is not
        Image image = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(w,h,pixels,0,w));
        Image bufferedImage = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        bufferedImage.getGraphics().drawImage(image,0,0,null);
        System.out.println("image is a BufferedImage: " + (image instanceof BufferedImage));
        System.out.println("bufferedImage is a BufferedImage: " + (bufferedImage instanceof BufferedImage));

        // create ImageIcons (Compatible with Java Swing) with these images
        ImageIcon iconFromImage = new ImageIcon(image);
        ImageIcon iconFromBufferedImage = new ImageIcon(bufferedImage);

        // try writing each to the disk
//        handler.save("image",iconFromImage);
//        handler.save("buffered-image",iconFromBufferedImage);

        // try loading them
        JFrame frame = new JFrame();
        frame.setSize(new Dimension(10,40));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(handler.retrieveImage("image"));
        frame.add(imageLabel);
        JLabel imageLabel2 = new JLabel();
        imageLabel2.setIcon(handler.retrieveImage("buffered-image"));
        frame.add(imageLabel2);
        frame.setVisible(true);
    }
}
