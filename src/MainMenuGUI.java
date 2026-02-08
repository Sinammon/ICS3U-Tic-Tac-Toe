import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;


import static javax.sound.sampled.AudioSystem.getAudioInputStream;

public class MainMenuGUI extends JFrame implements ActionListener {

    // images
    JLabel bg;
    ImageIcon bgVid = new ImageIcon("tictactoe bg.gif");

    //Button Design
    Font neonFont = new Font("SansSerif", Font.BOLD, 20);
    Color neonC = new Color(255, 0, 255);

    // Sound effect
    File btnFile = new File("menu btn.wav");
    AudioInputStream MenuButtonAudioStream = getAudioInputStream(btnFile);
    Clip clip = AudioSystem.getClip();

    Font orbitron;

    JPanel menu;
    JLabel title;

    JPanel titlePanel;

    JButton pVp;
    JButton pVa;
    JButton stats;
    JButton music;
    JButton back;

    StatsManager statsM;

    Dimension buttonSize = new Dimension(250,50);

    public void actionPerformed(ActionEvent e) {
        String choice = e.getActionCommand();
        switch (choice) {
            case "PvP" -> {
                if (clip.isRunning()) {
                    clip.stop();
                }
                clip.setFramePosition(0);
                clip.start();
                String playerName = JOptionPane.showInputDialog(
                        this,                      // parent component
                        "Enter your name:",        // message
                        "Player Name",             // title
                        JOptionPane.QUESTION_MESSAGE
                );

                if (playerName != null && !playerName.trim().isEmpty()) {
                    try {
                        new GameGUI(false,false,false, playerName,statsM).setVisible(true);
                    } catch (LineUnavailableException ex) {
                        throw new RuntimeException(ex);
                    } catch (UnsupportedAudioFileException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "You must enter a name!",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE
                    );
                }

            }
            case "PvA" -> {
                if (clip.isRunning()) {
                    clip.stop();
                }
                clip.setFramePosition(0); // rewind to start
                clip.start();
                try {
                    new DifficultyGUI();
                } catch (UnsupportedAudioFileException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (LineUnavailableException ex) {
                    throw new RuntimeException(ex);
                }
            }
            case "Stats" -> {
                if (clip.isRunning()) {
                    clip.stop();
                }
                clip.setFramePosition(0); // rewind to start
                clip.start();
                new LeaderboardsGUI(statsM);
            }
            case "music" -> {
                if (clip.isRunning()) {
                    clip.stop();
                }
                clip.setFramePosition(0); // rewind to start
                clip.start();
                try {
                    new MusicPlayerGUI();
                } catch (UnsupportedAudioFileException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (LineUnavailableException ex) {
                    throw new RuntimeException(ex);
                }
            }
            case "Back" -> System.exit(0);
        }
    }
    public void btnDesign(JButton button){
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(buttonSize);
        button.setMinimumSize(buttonSize);
        button.setPreferredSize(buttonSize);

        button.setFont(neonFont);
        button.setForeground(neonC);
        button.setBackground(Color.black);

        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 255), 3));
        button.addActionListener(this);

    }

    public MainMenuGUI() throws UnsupportedAudioFileException, IOException, LineUnavailableException, FontFormatException {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        try {
            // creates font
            orbitron = Font.createFont(Font.TRUETYPE_FONT, new File("Orbitron-Regular.ttf")).deriveFont(72f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            // installing the font
            ge.registerFont(orbitron);
        } catch (Exception e) {
            e.printStackTrace();
        }


        clip.open(MenuButtonAudioStream);

        statsM = new StatsManager();
        statsM.loadFromFile();

        bg = new JLabel(bgVid);
        bg.setLayout(new BorderLayout());

        setContentPane(bg);

        titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(120, 0, 40, 0));

        title = new JLabel("Tic Tac Toe Deluxe", SwingConstants.CENTER);
        title.setFont(orbitron);

        title.setForeground(new Color(255, 0, 255)); // neon magenta

        bg.add(titlePanel, BorderLayout.NORTH);
        titlePanel.add(title, BorderLayout.CENTER);


        menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setOpaque(false);
        bg.add(menu,BorderLayout.CENTER);

        menu.add(Box.createVerticalGlue());


        pVp = new JButton("Player Vs Player");
        btnDesign(pVp);
        pVp.setActionCommand("PvP");

        menu.add(pVp);
        menu.add(Box.createVerticalStrut(20));

        pVa = new JButton("Player Vs AI");
        btnDesign(pVa);
        pVa.setActionCommand("PvA");

        menu.add(pVa);
        menu.add(Box.createVerticalStrut(20));

        stats = new JButton("Career Stats");
        btnDesign(stats);
        stats.setActionCommand("Stats");

        menu.add(stats);
        menu.add(Box.createVerticalStrut(20));

        music = new JButton("Music Playlist");
        btnDesign(music);
        music.setActionCommand("music");

        menu.add(music);
        menu.add(Box.createVerticalStrut(20));

        back = new JButton("Exit");
        btnDesign(back);
        back.setActionCommand("Back");

        menu.add(back);
        back.setActionCommand("Back");

        menu.add(Box.createVerticalGlue());


        setVisible(true);
    }
}
