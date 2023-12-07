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

        ImageIcon placeholderImage = new ImageIcon("resources/h.png"); 
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
        Font customFont = new Font("Arial", Font.PLAIN, 18); 
        encodeButton.setFont(customFont);
        decodeButton.setFont(customFont);

        // bordercolor of buttons 
        Color purpleColor = new Color(118, 57, 111); 
        Border purpleBorder = BorderFactory.createLineBorder(purpleColor, 4); 
        encodeButton.setBorder(purpleBorder);
        decodeButton.setBorder(purpleBorder);

        // text color and font for the "ENCODE" button
        Color encodeButtonTextColor = new Color(118, 57, 111); 
        encodeButton.setForeground(encodeButtonTextColor);
        Font encodeButtonFont = new Font("Epilogue", Font.BOLD, 20); 
        encodeButton.setFont(encodeButtonFont);

        // text color and font for the "DECODE" button
        Color decodeButtonTextColor = new Color(118, 57, 111);
        decodeButton.setForeground(decodeButtonTextColor);
        Font decodeButtonFont = new Font("Epilogue", Font.BOLD, 20); 
        decodeButton.setFont(decodeButtonFont);


        // Action listeners to the buttons
        encodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open UploadPageEncode when encodeButton is clicked
                UploadPageEncode uploadPageEncode = new UploadPageEncode();
                uploadPageEncode.setVisible(true);
                // Hide the current frame (Home)
                setVisible(false);
            }
        });

        decodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open UploadPageDecode when decodeButton is clicked
                UploadPageDecode uploadPageDecode = new UploadPageDecode();
                uploadPageDecode.setVisible(true);
                // Hide the current frame (Home)
                setVisible(false);
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
