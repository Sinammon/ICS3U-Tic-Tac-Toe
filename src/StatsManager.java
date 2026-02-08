import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatsManager {
    private HashMap<String, PlayerStats> stats;


    public StatsManager() {
        stats = new HashMap<>();
        loadFromFile();
    }

    public List<Map.Entry<String, PlayerStats>> getPlayersSortedByWins() {
        // maps don't have an order so you need to convert the entries into a list to perform sort.
        List<Map.Entry<String, PlayerStats>> list = new ArrayList<>(stats.entrySet());

        // a and b are 2 different players being compared
        list.sort((a, b) ->
                Integer.compare(
                        // putting b before a sorts them in a descending order; since we have a leaderboard we want it like this
                        b.getValue().getWins(),
                        a.getValue().getWins()
                )
        );
        return list;
    }


    public PlayerStats getPlayer(String name) {
        if (!stats.containsKey(name)) {
            stats.put(name, new PlayerStats(0, 0, 0));
        }
        return stats.get(name);
    }

    public void recordWin(String name) {
        getPlayer(name).addWin();
    }

    public void recordLosses(String name) {
        getPlayer(name).addLoss();
    }

    public void recordTies(String name) {
        getPlayer(name).addTie();
    }

    public void saveToFile() {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter("stats.txt"));
            for (String name : stats.keySet()) {
                PlayerStats ps = getPlayer(name);
                pw.println(name + "," + ps.getWins() + "," + ps.getLosses() + "," + ps.getTies());
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile() {
        try {
            stats.clear();
            BufferedReader br = new BufferedReader(new FileReader("stats.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];

                PlayerStats ps = new PlayerStats(0, 0, 0);
                ps.setWins(Integer.parseInt(parts[1]));
                ps.setLosses(Integer.parseInt(parts[2]));
                ps.setTies(Integer.parseInt(parts[3]));

                stats.put(name, new PlayerStats(ps.getWins(), ps.getLosses(), ps.getTies()));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
