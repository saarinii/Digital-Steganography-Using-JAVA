import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextAndImage extends JFrame {

    private static final String BACKGROUND_IMAGE_PATH = "home.png"; 

    public TextAndImage() {
        setTitle("Digital Steganography on Time or Image");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        int width = 1200;
        int height = 800;
        setSize(width, height);
        setResizable(false);

 
        BackgroundPanel backgroundPanel = new BackgroundPanel(BACKGROUND_IMAGE_PATH);
        setContentPane(backgroundPanel);

        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        backgroundPanel.setLayout(gridBagLayout);

        // buttons for text and image 
        JButton textButton = createCustomButton("Text", "Arial", Font.PLAIN, 50);
        JButton imageButton = createCustomButton("Image", "Arial", Font.PLAIN, 50);

        // Action listeners to the buttons
        textButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add your text button functionality here
                JOptionPane.showMessageDialog(TextAndImage.this, "Text button clicked");
            }
        });

        imageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add your image button functionality here
                JOptionPane.showMessageDialog(TextAndImage.this, "Image button clicked");
            }
        });


        gridBagConstraints.gridx = 0; // Column 0
        gridBagConstraints.gridy = 0; // Row 0
        gridBagConstraints.insets = new Insets(0, 20, 0, 0); // Left margin
        backgroundPanel.add(textButton, gridBagConstraints);

        gridBagConstraints.gridx = 1; // Column 1
        gridBagConstraints.gridy = 0; // Row 0
        gridBagConstraints.insets = new Insets(0, 80, 0, 50); // Right margin
        backgroundPanel.add(imageButton, gridBagConstraints);

        setLocationRelativeTo(null);
    }

    private JButton createCustomButton(String text, String fontName, int fontStyle, int fontSize) {
        JButton button = new JButton(text);

        // text size and font
        Font customFont = new Font(fontName, fontStyle, fontSize);
        button.setFont(customFont);

        // bordercolor of buttons 
        Color purpleColor = new Color(118, 57, 111); 
        Border purpleBorder = BorderFactory.createLineBorder(purpleColor, 3); 
        button.setBorder(purpleBorder);

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TextAndImage().setVisible(true);
            }
        });
    }

    private static class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            this.backgroundImage = new ImageIcon(imagePath).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
