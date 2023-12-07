import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.swing.*;
//import javax.swing.border.Border;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.Key;
import java.util.List;
import java.awt.image.DataBufferByte; 
import java.awt.image.BufferedImage;

public class UploadPageDecode extends JFrame {

    //private JTextArea droppedFilesTextArea;
    private final String predefinedKey = "YourPredefinedKey"; // Replace with your actual key
    private File selectedFile;
    public UploadPageDecode() {
    setTitle("Upload and Decode");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set frame size
        int width = 1200;
        int height = 800;
        setSize(width, height);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel headerLabel = new JLabel("UPLOAD FILES");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 40)); 
        headerPanel.add(headerLabel);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Browse Files button
        JPanel firstBoxPanel = createDragAndDropPanel();
        JButton browseButton = new JButton("Browse Files");
        browseButton.setPreferredSize(new Dimension(120, 30)); 
        browseButton.addActionListener(e -> {
            // Add your browse button functionality here
            openFileChooser();
        });
        firstBoxPanel.add(browseButton);

        panel.add(firstBoxPanel, BorderLayout.CENTER);

        // Content has been ENCODED
        JPanel secondBoxPanel = createOutlinedBoxPanel("Content has been DECODED");
        JButton downloadButton = new JButton("Download");
        JButton homeButton = new JButton("Home");
        downloadButton.setPreferredSize(new Dimension(120, 30)); 
        downloadButton.addActionListener(e -> {
            // Add your download button functionality here
            openKeyDialog();
        });
        homeButton.setPreferredSize(new Dimension(120, 30));
        homeButton.addActionListener(e -> goHome());

        JLabel encodedTextLabel = new JLabel("                                Content has been DECODED");
        encodedTextLabel.setFont(new Font("Arial", Font.BOLD, 30));
        encodedTextLabel.setForeground(Color.BLACK);

        secondBoxPanel.setLayout(new BorderLayout());
        secondBoxPanel.add(encodedTextLabel, BorderLayout.CENTER);
        secondBoxPanel.add(downloadButton, BorderLayout.WEST);
        secondBoxPanel.add(homeButton, BorderLayout.EAST);

        panel.add(secondBoxPanel, BorderLayout.SOUTH);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 90, 20, 90));

        setContentPane(panel);
        setLocationRelativeTo(null);
    }
    private void goHome() {
        // Add your logic to navigate back to the Home.java class
        // For example:
        dispose(); // Close the current frame
        new Home().setVisible(true); // Open the Home.java class
    }


    private JPanel createOutlinedBoxPanel(String title) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{1}, 0));
                g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g2d.dispose();
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Adjust border size

        return panel;
    }

    private JPanel createDragAndDropPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{1}, 0));
                g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g2d.dispose();
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(90, 90, 90, 90)); // Adjust border size

        DropTarget dropTarget = new DropTarget(panel, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                dtde.acceptDrop(DnDConstants.ACTION_COPY);
                Transferable transferable = dtde.getTransferable();
                try {
                    List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                    handleDroppedFiles(files);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        panel.setDropTarget(dropTarget);

        return panel;
    }

    private void handleDroppedFiles(List<File> files) {
        // Handle the dropped files 
        StringBuilder message = new StringBuilder("Dropped files:\n");
        for (File file : files) {
            message.append(file.getName()).append("\n");
        }
        JOptionPane.showMessageDialog(this, message.toString());
    }

    private void openKeyDialog() {
        JDialog keyDialog = new JDialog(this, "Key Information", true);
        keyDialog.setSize(250, 150);
        keyDialog.setResizable(false);
        keyDialog.setLocationRelativeTo(this);

        JPanel keyPanel = new JPanel(new BorderLayout());

        JTextArea keyTextArea = new JTextArea();
        keyTextArea.setEditable(true); // Allow user to paste the key

        JButton copyButton = new JButton("Key");
        copyButton.setPreferredSize(new Dimension(100, 25));
        copyButton.addActionListener(e -> {
            String enteredKey = keyTextArea.getText();
            if (isValidKey(enteredKey)) {
                JOptionPane.showMessageDialog(this, "Valid Key");
                decryptFiles();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Key");
            }
        });
        keyPanel.add(new JLabel("Paste the Key"), BorderLayout.NORTH);
        keyPanel.add(keyTextArea, BorderLayout.CENTER);
        keyPanel.add(copyButton, BorderLayout.SOUTH);

        keyDialog.setContentPane(keyPanel);
        keyDialog.setVisible(true);
    }
    private boolean isValidKey(String enteredKey) {
        // Replace this with your actual key validation logic
        return enteredKey.equals(predefinedKey);
    }
    private void decryptFiles() {
    // Assume the selectedFile is the file you want to decrypt
    // You need to modify this logic based on your actual decryption requirements

    if (isTextFile(selectedFile)) {
        // Decrypt text file using RSA with XOR
        byte[] decryptedContent = performRsaWithXor(selectedFile, predefinedKey);
        
        // TODO: Handle the decrypted text content as needed
        System.out.println("Decrypted Text Content: " + new String(decryptedContent));
    } else {
        // Decrypt image file using AES with LSB
        byte[] decryptedImage = performAesWithLsb(selectedFile, predefinedKey);
        
        // TODO: Handle the decrypted image data as needed
        // For example, you can save the image to a specific location
        Path imagePath = saveDecryptedImage(decryptedImage);
        System.out.println("Decrypted Image saved at: " + imagePath);
    }
}

private byte[] performRsaWithXor(File textFile, String key) {
    try {
        // Read the content of the text file
        byte[] fileContent = Files.readAllBytes(textFile.toPath());

        // XOR operation to decrypt
        byte[] decryptedContent = xorOperation(fileContent, key.getBytes());

        return decryptedContent;
    } catch (Exception e) {
        e.printStackTrace();
        return new byte[0];
    }
}

private byte[] xorOperation(byte[] data, byte[] key) {
    // XOR logic to decrypt
    for (int i = 0; i < data.length; i++) {
        data[i] = (byte) (data[i] ^ key[i % key.length]);
    }
    return data;
}

private byte[] performAesWithLsb(File imageFile, String key) {
    try {
        BufferedImage encryptedImage = ImageIO.read(imageFile);

        // Assuming the encrypted image contains the hidden message
        byte[] encryptedBytes = ((DataBufferByte) encryptedImage.getRaster().getDataBuffer()).getData();

        // Example key derivation, replace with your key generation logic
        byte[] keyData = key.getBytes();
        Key aesKey = new SecretKeySpec(keyData, "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);

        // Decrypt the image data
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        // Extract the hidden message using LSB steganography
        String extractedMessage = extractMessage(decryptedBytes);

        System.out.println("Decrypted Message: " + extractedMessage);

        return decryptedBytes;
    } catch (Exception e) {
        e.printStackTrace();
        return new byte[0];
    }
}

private String extractMessage(byte[] decryptedBytes) {
    // Extract the hidden message using LSB steganography logic
    // This is a placeholder, replace it with your actual implementation
    // The example below simply converts the LSB of each byte to characters
    StringBuilder messageBuilder = new StringBuilder();

    for (byte b : decryptedBytes) {
        int lsb = b & 1; // Extract the LSB
        messageBuilder.append((char) ('0' + lsb)); // Convert to character
    }

    return messageBuilder.toString();
}


private Path saveDecryptedImage(byte[] decryptedImage) {
    // Save the decrypted image to a file
    Path imagePath = Paths.get("C:/Users/akank/OneDrive/Documents/projects/Digital-Steganography-Using-JAVA/saved/decrypted_image.png");
    try {
        Files.write(imagePath, decryptedImage, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return imagePath;
}

private boolean isTextFile(File file) {
    return file.getName().toLowerCase().endsWith(".txt");
}


    private void openFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(this, "Selected file: " + selectedFile.getName());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UploadPageDecode().setVisible(true));
    }
}
