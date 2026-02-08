import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


public class MusicPlayerGUI extends JFrame implements ActionListener {

    // importing files
    File goldLofi = new File("chill gold lofi.wav");

    File lobbyMusic1 = new File("lobby music 1.wav");

    File lobbyMusic2 = new File("lobby music 2.wav");

    File smackThat = new File("hip hop.wav");

    Clip clip = AudioSystem.getClip();

    Font neonFont = new Font("SansSerif", Font.BOLD, 20);
    Color neonC = new Color(255, 0, 255);

    JButton chill;
    JButton hipHop;
    JButton Lobby1;
    JButton Lobby2;
    JButton stop;
    public void btnDesign(JButton button){
        button.setFont(neonFont);
        button.setForeground(neonC);
        button.setBackground(Color.black);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 255), 3));
        button.addActionListener(this);

    }

    public MusicPlayerGUI() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        setBounds(100,10,400,700);
        setResizable(true);
        setLayout(new GridLayout(5,1));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Lobby1 = new JButton("Lobby music #1 \uD83C\uDFB5");
        btnDesign(Lobby1);

        Lobby2 = new JButton("Lobby music #2 \uD83C\uDFB5");
        btnDesign(Lobby2);

        chill = new JButton("Chill \uD83D\uDE2E\u200D\uD83D\uDCA8");
        btnDesign(chill);

        hipHop = new JButton("Hip Hop \uD83E\uDD19");
        btnDesign(hipHop);

        stop = new JButton("Stop ⏹");
        btnDesign(stop);

        add(Lobby1);
        add(Lobby2);
        add(chill);
        add(hipHop);
        add(stop);

        setVisible(true);
    }
    private void playSound(File soundFile) {
        try {
            // Stop and close the previous clip if it's running
            if (clip != null) {
                clip.stop();
                clip.close();
            }
            // Create a fresh stream and clip every time
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            clip.setFramePosition(0); // starts from the beginning
            clip.start(); // start the music
            clip.loop(Clip.LOOP_CONTINUOUSLY); // loops after sound ends

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)   {
        if (e.getSource()== chill){
           playSound(goldLofi);
        }
        if (e.getSource()== Lobby1){
           playSound(lobbyMusic1);

        }
        if (e.getSource()== Lobby2){
            playSound(lobbyMusic2);
        }
        if (e.getSource()== hipHop){
            playSound(smackThat);
        }
        if (e.getSource() == stop) {
            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.setFramePosition(0); // rewind to start
            }
        }


    }
}
