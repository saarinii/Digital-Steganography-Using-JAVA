import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Home extends JFrame {

    public Home() {
        setTitle("Digital Steganography");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int width = 1200;
        int height = 800;
        setSize(width, height);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout());

        ImageIcon placeholderImage = new ImageIcon("home.png"); 
        JLabel imageLabel = new JLabel(placeholderImage);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50)); 
        panel.add(imageLabel, BorderLayout.CENTER);

        // Encode and Decode buttons 
        JButton encodeButton = new JButton("ENCODE");
        JButton decodeButton = new JButton("DECODE");

        // size for the buttons
        Dimension buttonSize = new Dimension(200, 80);
        encodeButton.setPreferredSize(buttonSize);
        decodeButton.setPreferredSize(buttonSize);

        // text size
        Font customFont = new Font("Arial", Font.PLAIN, 18); // Adjust the font and size as needed
        encodeButton.setFont(customFont);
        decodeButton.setFont(customFont);

        // bordercolor of buttons 
        Color purpleColor = new Color(118, 57, 111); // RGB values for purple
        Border purpleBorder = BorderFactory.createLineBorder(purpleColor, 4); // Adjust the border size as needed
        encodeButton.setBorder(purpleBorder);
        decodeButton.setBorder(purpleBorder);

        // text color and font for the "ENCODE" button
        Color encodeButtonTextColor = new Color(118, 57, 111); // RGB values for text color
        encodeButton.setForeground(encodeButtonTextColor);
        Font encodeButtonFont = new Font("Epilogue", Font.BOLD, 20); // Adjust the font and size as needed
        encodeButton.setFont(encodeButtonFont);

        // text color and font for the "DECODE" button
        Color decodeButtonTextColor = new Color(118, 57, 111); // RGB values for text color
        decodeButton.setForeground(decodeButtonTextColor);
        Font decodeButtonFont = new Font("Epilogue", Font.BOLD, 20); // Adjust the font and size as needed
        decodeButton.setFont(decodeButtonFont);


        // Add action listeners to the buttons
        encodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add your encode functionality here
                JOptionPane.showMessageDialog(Home.this, "Encode button clicked");
            }
        });

        decodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add your decode functionality here
                JOptionPane.showMessageDialog(Home.this, "Decode button clicked");
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(encodeButton);
        buttonPanel.add(decodeButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(panel);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Home().setVisible(true);
            }
        });
    }
}
