import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import static javax.sound.sampled.AudioSystem.getAudioInputStream;

public class DifficultyGUI extends JFrame implements ActionListener {

    JLabel bg;
    ImageIcon bgTBS = new ImageIcon("neon.jpg");
    Image bgScaled = bgTBS.getImage().getScaledInstance(1980, 1200, Image.SCALE_SMOOTH);
    ImageIcon image = new ImageIcon(bgScaled);

    Font neonFont = new Font("SansSerif", Font.BOLD, 20);
    Color neonC = new Color(255, 0, 255);
    Dimension btnSize = new Dimension(150, 50);

    File btnFile = new File("menu btn.wav");
    AudioInputStream MenuButtonAudioStream = getAudioInputStream(btnFile);
    Clip clip = AudioSystem.getClip();


    Font orbitron;
    JLabel heading;

    JPanel top_Panel;

    JPanel difficulties;

    JButton easy;
    JButton medium;
    JButton hard;
    JButton back;

    StatsManager statsM;

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == easy) {
            startGame(true, false, false);
        } else if (e.getSource() == medium) {
            startGame(false, true, false);
        } else if (e.getSource() == hard) {
            startGame(false, false, true);
        } else if (e.getSource() == back) {
            dispose();
        }
    }

    private void startGame(boolean easyFlag, boolean mediumFlag, boolean hardFlag) {
        // Handle Audio
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();

        // Get Player Name
        String playerName = JOptionPane.showInputDialog(
                this,
                "Enter your name:",
                "Player Name",
                JOptionPane.QUESTION_MESSAGE
        );

        // Validate and Start
        if (playerName != null && !playerName.trim().isEmpty()) {
            try {
                new GameGUI(easyFlag, mediumFlag, hardFlag, playerName, statsM).setVisible(true);
                dispose();
            } catch (LineUnavailableException | UnsupportedAudioFileException | IOException ex) {
                throw new RuntimeException(ex);
            }
        } else if (playerName != null) {
            // Only show warning if they didn't click "Cancel"
            JOptionPane.showMessageDialog(
                    this,
                    "You must enter a name!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }
    public void btnDesign(JButton button){
        button.setFont(neonFont);
        button.setForeground(neonC);
        button.setBackground(Color.black);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 255), 3));
        button.addActionListener(this);
        button.setPreferredSize(btnSize);

    }
    public DifficultyGUI() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        try {

            orbitron = Font.createFont(Font.TRUETYPE_FONT, new File("Orbitron-Regular.ttf")).deriveFont(72f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(orbitron);


        } catch (Exception e) {
            e.printStackTrace();
        }


        clip.open(MenuButtonAudioStream);

        statsM = new StatsManager();
        statsM.loadFromFile();

        bg = new JLabel(image);
        bg.setLayout(new BorderLayout());
        setContentPane(bg);


        top_Panel = new JPanel(new BorderLayout());

        heading = new JLabel("Difficulties", SwingConstants.CENTER);
        heading.setFont(orbitron);
        heading.setBorder(BorderFactory.createEmptyBorder(280, 0, 220, 0));
        heading.setForeground(new Color(180, 0, 255)); // purple neon
        heading.setOpaque(true);
        heading.setBackground(new Color(0,0,0,120));

        top_Panel.add(heading, BorderLayout.CENTER);
        top_Panel.setOpaque(false);
        bg.add(top_Panel, BorderLayout.NORTH);

        difficulties = new JPanel(new FlowLayout(FlowLayout.CENTER,30,10));
        difficulties.setOpaque(false);
        bg.add(difficulties, BorderLayout.CENTER);

        easy = new JButton("Beginner");
        btnDesign(easy);

        medium = new JButton("Standard");
        btnDesign(medium);

        hard = new JButton("Final Boss");
        hard.setFont(neonFont);
        hard.setForeground(Color.RED);
        hard.setBackground(Color.black);
        hard.setFocusPainted(false);
        hard.setBorder(BorderFactory.createLineBorder(Color.RED));
        hard.addActionListener(this);
        hard.setPreferredSize(btnSize);

        back = new JButton("Back");
        btnDesign(back);

        difficulties.add(easy);
        difficulties.add(medium);
        difficulties.add(hard);
        difficulties.add(back);

        setVisible(true);
    }
}
