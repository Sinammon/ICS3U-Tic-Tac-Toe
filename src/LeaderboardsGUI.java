import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.List;

public class LeaderboardsGUI extends JFrame implements ActionListener {
    Font neonFont = new Font("SansSerif", Font.BOLD, 20);
    Color neonC = new Color(255, 0, 255);

    StatsManager statsM;

    JLabel bg;
    ImageIcon bgTBS = new ImageIcon("leaderboard bg.jpg");
    Image bgScaled = bgTBS.getImage().getScaledInstance(1280, 750, Image.SCALE_SMOOTH);
    ImageIcon image = new ImageIcon(bgScaled);

    JLabel trophyLabel;
    ImageIcon trophyTBS = new ImageIcon("Champion.png");
    Image trophyScaled = trophyTBS.getImage().getScaledInstance(180, -1, Image.SCALE_SMOOTH);
    ImageIcon trophyImage = new ImageIcon(trophyScaled);

    JPanel top_panel;
    JLabel winner;
    JLabel wins;

    JPanel center_panel;

    JPanel bottom_panel;
    JButton search;
    JButton back;
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == search){
            String playerName = JOptionPane.showInputDialog(
                    this,                      // parent component
                    "Enter player name:",        // message
                    "Player Name",             // title
                    JOptionPane.QUESTION_MESSAGE
            );

            if (playerName != null && !playerName.trim().isEmpty()) {
                new StatsManagerGUI(playerName,statsM);

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "You must enter a name!",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }
        if (e.getSource() == back){
            dispose();
        }
    }
    public void btnDesign(JButton button){
        button.setFont(neonFont);
        button.setForeground(neonC);
        button.setBackground(Color.black);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 255), 3));
        button.addActionListener(this);

    }
    public LeaderboardsGUI(StatsManager sm){
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        statsM = sm;

        statsM = new StatsManager();
        statsM.loadFromFile();

        bg = new JLabel(image);
        bg.setLayout(new BorderLayout());
        setContentPane(bg);

        // creating a list of players with the number of wins in descending order.
        List<Map.Entry<String,PlayerStats>> players = statsM.getPlayersSortedByWins();

        top_panel = new JPanel();
        top_panel.setLayout(new BoxLayout(top_panel,BoxLayout.Y_AXIS));

        trophyLabel = new JLabel(trophyImage);
        trophyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        winner = new JLabel ("#1 " + players.get(0).getKey().toUpperCase());
        winner.setAlignmentX(Component.CENTER_ALIGNMENT);
        winner.setForeground(neonC); // neon pink
        winner.setFont(new Font("Segoe UI", Font.BOLD, 36));
        top_panel.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));

        wins = new JLabel(players.get(0).getValue().getWins()+" WINS! ");
        wins.setAlignmentX(Component.CENTER_ALIGNMENT);
        wins.setForeground(neonC); // neon pink
        wins.setFont(new Font("Segoe UI", Font.BOLD, 26));

        top_panel.add(Box.createVerticalStrut(20));
        top_panel.add(trophyLabel);
        top_panel.add(Box.createVerticalStrut(20));
        top_panel.add(winner);
        top_panel.add(wins);
        bg.add(top_panel, BorderLayout.NORTH);

        top_panel.setOpaque(false);


        center_panel = new JPanel();
        bg.add(center_panel,BorderLayout.CENTER);

        String[] columns = {"Rank","Player Name","Wins"};

        //each row has 3 columns and it is 1 less row because number 1 is presented on top.
        Object[][] data = new Object[players.size() - 1][3];

        for (int i = 1; i < players.size(); i++) {
            Map.Entry<String, PlayerStats> entry = players.get(i);

            data[i - 1][0] = i + 1; // rank
            data[i - 1][1] = entry.getKey(); // name
            data[i - 1][2] = entry.getValue().getWins(); // wins
        }

        JTable table = new JTable(data,columns);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(120, 0, 200));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));

        table.setBackground(new Color(30, 0, 50)); // dark purple
        table.setForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(32);
        table.setEnabled(false);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(700, 250));

        scroll.getViewport().setBackground(new Color(30, 0, 50));
        scroll.setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setOpaque(true);
        // designing the scroll bar
        JScrollBar vBar = scroll.getVerticalScrollBar();
        vBar.setPreferredSize(new Dimension(8, Integer.MAX_VALUE));
        vBar.setBackground(new Color(30, 0, 50));

        center_panel.add(scroll);
        center_panel.setOpaque(false);


        bottom_panel = new JPanel(new FlowLayout(FlowLayout.CENTER,30,20));
        bottom_panel.setBorder(BorderFactory.createEmptyBorder(120,0,70,0));
        search = new JButton("Search for Player");
        btnDesign(search);

        back = new JButton("Back");
        btnDesign(back);

        bg.add(bottom_panel, BorderLayout.SOUTH);
        bottom_panel.add(search);
        bottom_panel.add(back);
        bottom_panel.setOpaque(false);

        setVisible(true);
    }
}
