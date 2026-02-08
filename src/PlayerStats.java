public class PlayerStats {

    private int wins,losses,ties;

    public PlayerStats(int w,int l,int t){
        wins = w;
        losses = l;
        ties = t;
    }

    public void addWin(){
        wins++;
    }
    public void addLoss(){
        losses++;
    }
    public void addTie(){
        ties++;
    }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }
    public int getTies() { return ties; }

    public void setWins(int w) { wins = w; }
    public void setLosses(int l) { losses = l; }
    public void setTies(int t) { ties = t; }

}
