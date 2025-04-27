package WindowStates;

import ApplicationDefaults.*;
import DataStructures.FileHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;

public class Calendar extends WindowState implements AcceptMouseResponse {
    private JPanel popup;
    private boolean popupOpen;
    private MouseController mouseController;
    private JFrame parentFrame;
    private java.util.Calendar calendar;
    private BevelPanel calendarBody;
    private JLabel dateLabel;
    public Calendar (JFrame parentFrame) {
        // setup frame
        super(WindowStateName.CALENDAR);
        this.parentFrame = parentFrame;

        // setup a popup UI
        popup = new BevelPanel();
        popup.setBackground(Color.PINK);
        ((BevelPanel) popup).setRoundTop(true);
        ((BevelPanel) popup).setRoundBottom(true);
        ((BevelPanel) popup).setRounding(10);
        popup.setOpaque(false);
        popupOpen = false;

        // setup UI
        JPanel background = setupUI();

        // setup bounds and add items
        background.setBounds(0,0,1200,800);
        popup.setBounds(0,0,100,100);
        getContentPane().add(background,0);
        parentFrame.addComponentListener(new ResizeListener(parentFrame,background));
        mouseController = new MouseController(this);
        parentFrame.addMouseListener(mouseController.getMouseListener());
        parentFrame.addMouseMotionListener(mouseController.getMouseMotionListener());

        // setup calendar with current date, then adjust UI
        calendar = java.util.Calendar.getInstance();
        updateUI();
    }

    private void updateUI () {
        // gather information about what the calendar should look like
        int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);
        String month = calendar.getDisplayName(java.util.Calendar.MONTH,java.util.Calendar.LONG,
                new Locale("en"));
        int monthNum = calendar.get(java.util.Calendar.MONTH);
        int year = calendar.get(java.util.Calendar.YEAR);

        calendar.set(java.util.Calendar.DAY_OF_MONTH,1);
        int offset = calendar.get(java.util.Calendar.DAY_OF_WEEK)-1;
        int daysInMonth = calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);

        calendar.set(java.util.Calendar.MONTH,calendar.get(java.util.Calendar.MONTH)-1);
        int firstDayOfLastMonth = calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH)-(offset-1);
        int lastMonthNum = calendar.get(java.util.Calendar.MONTH);
        int yearOfLastMonth = calendar.get(java.util.Calendar.YEAR);

        // create a list of dates in "mm-dd-yy" format and a list of the days
        int[] days = new int[42];
        String[] dates = new String[42];
        for (int i = 0; i < offset; i++) {
            days[i] = firstDayOfLastMonth+i;
            dates[i] = (lastMonthNum > 9 ? ""+lastMonthNum : "0"+lastMonthNum)+(days[i] > 9 ? "-"+days[i] :
                    "-0"+days[i])+"-"+yearOfLastMonth;
        }
        for (int i = 1; i <= daysInMonth; i++) {
            days[i-1+offset] = i;
            dates[i-1+offset] = (monthNum > 9 ? ""+monthNum : "0"+monthNum)+(i > 9 ? "-"+i : "-0"+i)+"-"+year;
        }
        int currentDay = 1;
        for (int i = daysInMonth+offset; i < 42; i++) {
            days[i] = currentDay++;
            dates[i] = (monthNum > 9 ? ""+monthNum : "0"+monthNum)+(days[i] > 9 ? "-"+days[i] : "-0"+days[i])+"-"+year;
        }

        // set calendar days
        calendarBody.removeAll();
        calendarBody.setLayout(new GridLayout(6,7));
        for (int currDay : days) {
            calendarBody.add(setupDay(currDay));
        }

        // set overall date
        dateLabel.setText(month + " " + day + ", " + year);
        repaint();
        revalidate();

        // reset calendar object
        calendar.set(year,monthNum,day);
    }

    public void onDrag (int x, int y) {}

    public void onRightClick (int x, int y) {}

    public void onLeftClick (int x, int y) {}

    public JPanel setupUI () {
        JPanel background = new JPanel();
        JPanel topBar = new JPanel();
        JPanel topBarContent = new JPanel();
        dateLabel = new JLabel();
        JPanel dateSeekingButtons = new JPanel();
        JLabel lastMonthButton = new JLabel();
        JLabel nextMonthButton = new JLabel();
        JPanel calendar = new JPanel();
        BevelPanel calendarHeader = new BevelPanel();
        JLabel[] daysOfTheWeek = new JLabel[7];
        for (int i = 0; i < daysOfTheWeek.length; i++)
            daysOfTheWeek[i] = new JLabel();
        calendarBody = new BevelPanel();
        JPanel spacer = new JPanel();

        // setup background
        background.setBackground(new Color(251,254,255));
        background.setLayout(new BoxLayout(background,BoxLayout.Y_AXIS));
        background.setAlignmentX(Component.CENTER_ALIGNMENT);

        // setup top-bar
        topBar.setBackground(new Color(240,240,240));
        topBar.setMaximumSize(new Dimension(5000,100));
        topBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        topBar.setLayout(new BoxLayout(topBar,BoxLayout.Y_AXIS));
        background.add(topBar);

        // setup top-bar content
        topBarContent.setBackground(topBar.getBackground());
        topBarContent.setPreferredSize(new Dimension(1000,70));
        topBarContent.setMaximumSize(new Dimension(1000,70));
        topBarContent.setMinimumSize(new Dimension(1000,70));
        topBarContent.setLayout(new BorderLayout());
        topBarContent.setAlignmentX(Component.CENTER_ALIGNMENT);
        topBar.add(topBarContent);

        // setup date
        dateLabel.setText("March 11, 2025");
        dateLabel.setForeground(new Color(116,116,116));
        dateLabel.setFont(new Font("Helvetica",Font.PLAIN,48));
        topBarContent.add(dateLabel,BorderLayout.WEST);

        // setup date-seeking buttons panel
        dateSeekingButtons.setBackground(topBarContent.getBackground());
        dateSeekingButtons.setSize(new Dimension(120,70));
        dateSeekingButtons.setPreferredSize(dateSeekingButtons.getSize());
        dateSeekingButtons.setMaximumSize(dateSeekingButtons.getSize());
        dateSeekingButtons.setMinimumSize(dateSeekingButtons.getSize());
        dateSeekingButtons.setLayout(new BorderLayout());
        topBarContent.add(dateSeekingButtons,BorderLayout.EAST);

        // setup date seeking buttons
        lastMonthButton.setText("<");
        lastMonthButton.setForeground(new Color(140,140,140));
        lastMonthButton.setFont(new Font("Helvetica",Font.PLAIN,48));
        lastMonthButton.setSize(new Dimension(50,60));
        lastMonthButton.setMinimumSize(lastMonthButton.getSize());
        lastMonthButton.setMaximumSize(lastMonthButton.getSize());
        lastMonthButton.setPreferredSize(lastMonthButton.getSize());
        lastMonthButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        lastMonthButton.setVerticalAlignment(SwingConstants.CENTER);
        lastMonthButton.setHorizontalAlignment(SwingConstants.CENTER);
        lastMonthButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                java.util.Calendar cal = Calendar.this.calendar;
                int month = java.util.Calendar.MONTH;
                cal.set(month,(cal.get(month)-2) % 12 + 1);
                Calendar.this.updateUI();
                System.out.println("PREVIOUS");
            }
        });
        dateSeekingButtons.add(lastMonthButton,BorderLayout.WEST);
        nextMonthButton.setText(">");
        nextMonthButton.setForeground(new Color(140,140,140));
        nextMonthButton.setFont(new Font("Helvetica",Font.PLAIN,48));
        nextMonthButton.setSize(new Dimension(50,60));
        nextMonthButton.setMinimumSize(nextMonthButton.getSize());
        nextMonthButton.setMaximumSize(nextMonthButton.getSize());
        nextMonthButton.setPreferredSize(nextMonthButton.getSize());
        nextMonthButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextMonthButton.setVerticalAlignment(SwingConstants.CENTER);
        nextMonthButton.setHorizontalAlignment(SwingConstants.CENTER);
        nextMonthButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                java.util.Calendar cal = Calendar.this.calendar;
                int month = java.util.Calendar.MONTH;
                cal.set(month,cal.get(month) % 12 + 1);
                Calendar.this.updateUI();
                System.out.println("NEXT");
            }
        });
        dateSeekingButtons.add(nextMonthButton,BorderLayout.EAST);

        // setup calendar
        calendar.setBackground(background.getBackground());
        calendar.setAlignmentX(Component.LEFT_ALIGNMENT);
        calendar.setPreferredSize(new Dimension(5000,1000));
        calendar.setMaximumSize(new Dimension(5000,1000));
        calendar.setMinimumSize(new Dimension(5000,100));
        calendar.setLayout(new BoxLayout(calendar,BoxLayout.Y_AXIS));
        calendar.setBorder(BorderFactory.createEmptyBorder(5,0,0,0));
        background.add(calendar);

        // setup calendar header
        calendarHeader.setBackground(new Color(220,220,220));
        calendarHeader.setPreferredSize(new Dimension(1000,35));
        calendarHeader.setMaximumSize(new Dimension(1000,35));
        calendarHeader.setMinimumSize(new Dimension(500,35));
        calendarHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        calendarHeader.setLayout(new GridLayout(1,7));
        calendarHeader.setRoundTop(true);
        calendar.add(calendarHeader);

        // setup calendar header text
        String[] abbrevDay = {"SUN","MON","TUE","WED","THU","FRI","SAT"};
        for (int i = 0; i < daysOfTheWeek.length; i++) {
            daysOfTheWeek[i].setFont(new Font("Helvetica", Font.BOLD,24));
            daysOfTheWeek[i].setForeground(dateLabel.getForeground());
            daysOfTheWeek[i].setText(abbrevDay[i]);
            daysOfTheWeek[i].setHorizontalAlignment(SwingConstants.CENTER);
            calendarHeader.add(daysOfTheWeek[i]);
        }

        // setup calendar body
        calendarBody.setBackground(new Color(240,240,240));
        calendarBody.setPreferredSize(new Dimension(1000,1000));
        calendarBody.setMaximumSize(new Dimension(1000,1000));
        calendarBody.setMinimumSize(new Dimension(500,50));
        calendarBody.setLayout(new GridLayout(6,7));
        calendarBody.setAlignmentX(Component.CENTER_ALIGNMENT);
        calendarBody.setRoundBottom(true);
        calendar.add(calendarBody);
        int day = 22;
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                calendarBody.add(setupDay(day+1));
                day = (day + 1) % 28;
            }
        }

        // setup spacer
        spacer.setBackground(background.getBackground());
        spacer.setSize(new Dimension(50,40));
        spacer.setPreferredSize(spacer.getSize());
        spacer.setMinimumSize(spacer.getSize());
        spacer.setMaximumSize(spacer.getSize());
        calendar.add(spacer);

        return background;
    }

    public JPanel setupDay (int day) {
        JPanel panel = new JPanel();
        BevelPanel bevelPanel = new BevelPanel();
        JLabel label = new JLabel(""+day);

        panel.setPreferredSize(new Dimension(50,20));
        panel.setMaximumSize(new Dimension(50,20));
        panel.setBackground(new Color(240,240,240));
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);

        bevelPanel.setLayout(new BoxLayout(bevelPanel,BoxLayout.Y_AXIS));
        bevelPanel.setRounding(20);
        bevelPanel.setRoundBottom(true);
        bevelPanel.setRoundTop(true);
        bevelPanel.setBackground(panel.getBackground());
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                bevelPanel.setBackground(new Color(panel.getBackground().getRed()-10,
                        panel.getBackground().getGreen()-10,
                        panel.getBackground().getBlue()-10));
                bevelPanel.repaint();
                Timer timer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        bevelPanel.setBackground(new Color(panel.getBackground().getRed(),
                                panel.getBackground().getGreen(),
                                panel.getBackground().getBlue()));
                        bevelPanel.repaint();
                    }
                });
                timer.setRepeats(false);
                timer.start();
                System.out.println("CLICK");
            }
        });

        label.setForeground(new Color(116,116,116));
        label.setFont(new Font("Helvetica",Font.PLAIN,24));
        label.setSize(new Dimension(200,30));
        label.setPreferredSize(label.getSize());
        label.setMinimumSize(label.getSize());
        label.setMaximumSize(label.getSize());
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setBorder(BorderFactory.createEmptyBorder(5,5,0,5));
        bevelPanel.add(label);

        panel.add(bevelPanel,BorderLayout.CENTER);
        return panel;
    }

    @Override
    public void onEscapePressed () {
        setNextWindow(WindowStateName.MISSION_SELECT);
        setCloseEvent(WindowStateEvent.SWITCH_STATE);
        close();
    }

    @Override
    public void save(FileHandler fileHandler) {

    }

    @Override
    public void load(FileHandler fileHandler) {

    }
}
