import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatsManagerGUI extends JFrame implements ActionListener {

    Font neonFont = new Font("SansSerif", Font.BOLD, 20);
    Color neonC = new Color(255, 0, 255);

    Font label = new Font("Segoe UI", Font.BOLD, 24);
    Color labelC = new Color (255, 0, 255);

    JLabel bg;
    ImageIcon bgTBS = new ImageIcon("leaderboard bg.jpg");
    Image bgScaled = bgTBS.getImage().getScaledInstance(1280, 750, Image.SCALE_SMOOTH);
    ImageIcon image = new ImageIcon(bgScaled);

    String playerName;
    StatsManager statsManager;

    JPanel top_panel;

    JLabel playerLabel;
    JPanel center;
    JLabel wins,losses,ties;

    JPanel bottomPanel;
    JButton back;
    StatsManagerGUI(String pName, StatsManager sm){
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        playerName = pName;
        statsManager = sm;

        // name entered is now being converted to playerStats
        PlayerStats ps = sm.getPlayer(playerName);

        bg = new JLabel(image);
        bg.setLayout(new BorderLayout());
        setContentPane(bg);


        top_panel = new JPanel();
        top_panel.setBorder(BorderFactory.createEmptyBorder(150,0,100,0));
        top_panel.setOpaque(false);

        bg.add(top_panel,BorderLayout.NORTH);

        playerLabel = new JLabel("Player - " + playerName.toUpperCase());
        playerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerLabel.setFont(new Font("Segoe UI", Font.BOLD, 45));
        playerLabel.setForeground(labelC);
        top_panel.add(playerLabel);

        center = new JPanel();
        center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        center.setOpaque(false);
        bg.add(center,BorderLayout.CENTER);


        // player stats
        wins = new JLabel("Number Of Games Won: "+ ps.getWins());
        wins.setAlignmentX(Component.CENTER_ALIGNMENT);
        wins.setFont(label);
        wins.setForeground(labelC);

        losses = new JLabel("Number Of Games Lost: "+ ps.getLosses());
        losses.setAlignmentX(Component.CENTER_ALIGNMENT);
        losses.setFont(label);
        losses.setForeground(labelC);

        ties = new JLabel("Number of Games Tied: "+ ps.getTies());
        ties.setAlignmentX(Component.CENTER_ALIGNMENT);
        ties.setFont(label);
        ties.setForeground(labelC);

        // spacing
        center.add(Box.createVerticalStrut(20));
        center.add(wins);
        center.add(Box.createVerticalStrut(20));
        center.add(losses);
        center.add(Box.createVerticalStrut(20));
        center.add(ties);

        bottomPanel = new JPanel(new FlowLayout());
        bg.add(bottomPanel, BorderLayout.SOUTH);

        back = new JButton("Back");
        back.setFont(neonFont);
        back.setForeground(neonC);
        back.setBackground(Color.black);
        back.setFocusPainted(false);
        back.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 255), 3));
        back.addActionListener(this);

        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0,0,150,0));
        bottomPanel.setOpaque(false);
        bottomPanel.add(back);

        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back){
            dispose();
        }
    }
}
