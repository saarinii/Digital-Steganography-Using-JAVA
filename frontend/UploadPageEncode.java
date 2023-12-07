import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

public class UploadPageEncode extends JFrame {
    private static final String ALGORITHM = "RSA";

    public UploadPageEncode() {
        setTitle("Upload and Encode");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        int width = 1200;
        int height = 800;
        setSize(width, height);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout());

        // header with UPLOAD FILES
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
            openFileChooser();
        });
        firstBoxPanel.add(browseButton);
        panel.add(firstBoxPanel, BorderLayout.CENTER);

        // Content has been ENCODED button
        JPanel secondBoxPanel = createOutlinedBoxPanel("Content has been ENCODED");
        JButton downloadButton = new JButton("Get Key");
        downloadButton.setPreferredSize(new Dimension(120, 30));
        downloadButton.addActionListener(e -> {
            openKeyDialog();
        });

        // Customize the size, font, and color of the text
        JLabel encodedTextLabel = new JLabel("                     Content has been ENCODED");
        encodedTextLabel.setFont(new Font("Arial", Font.BOLD, 30));
        encodedTextLabel.setForeground(Color.BLACK);

        secondBoxPanel.setLayout(new BorderLayout());
        secondBoxPanel.add(encodedTextLabel, BorderLayout.CENTER);
        secondBoxPanel.add(downloadButton, BorderLayout.SOUTH);

        // Home button
        JButton homeButton = new JButton("Home");
        homeButton.setPreferredSize(new Dimension(120, 30));
        homeButton.addActionListener(e -> {
            goHome();
        });
        secondBoxPanel.add(homeButton, BorderLayout.WEST);

        panel.add(secondBoxPanel, BorderLayout.SOUTH);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 90, 20, 90));

        setContentPane(panel);
        setLocationRelativeTo(null);
    }
    private void goHome() {
        dispose(); 
        Home home = new Home(); 
        home.setVisible(true); // Home.java class
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
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); 

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
        panel.setBorder(BorderFactory.createEmptyBorder(90, 90, 90, 90)); 

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
        // Assuming the first dropped file is an image
        File imageFile = files.get(0);
        encryptAndSaveImage(imageFile);
    }

    private void openKeyDialog() {
        String key = generateKey();
        StringSelection stringSelection = new StringSelection(key);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        JOptionPane.showMessageDialog(this, "Key copied to clipboard:\n" + key, "Key Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(this, "Selected file: " + selectedFile.getName());
            encryptAndSaveImage(selectedFile);
        }
    }

    private String generateKey() {
        return "GeneratedKey123";
    }

    private void encryptAndSaveImage(File imageFile) {
        byte[] encryptedImage = performAesWithLsb(imageFile);
        Path filePath = saveEncryptedImage(encryptedImage);
        JOptionPane.showMessageDialog(this, "Image encrypted and saved:\n" + filePath, "Encryption Complete", JOptionPane.INFORMATION_MESSAGE);
    }
    private byte[] performEncryption(File file) {
        if (isTextFile(file)) {
            return performRsaWithXor(file);
        } else {
            return performAesWithLsb(file);
        }
    }
    private boolean isTextFile(File file) {
        return file.getName().toLowerCase().endsWith(".txt");
    }
    private byte[] performRsaWithXor(File textFile) {
        try {
            // Read the content of the text file
            byte[] fileContent = Files.readAllBytes(Paths.get(textFile.getAbsolutePath()));

            // Generate RSA key pair
            KeyPair keyPair = generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // Encrypt the file content using RSA
            byte[] encryptedContent = encryptWithRSA(fileContent, publicKey);

            // XOR the encrypted content
            byte[] xorEncryptedContent = xorOperation(encryptedContent);

            // Save the XORed content to a new file
            Path filePath = saveXorEncryptedData(xorEncryptedContent, textFile);

            return xorEncryptedContent;
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    private KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(2048); // 2048 bits key size
        return keyPairGenerator.generateKeyPair();
    }

    private byte[] encryptWithRSA(byte[] data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    private byte[] xorOperation(byte[] data) {
        byte xorKey = (byte) 0xAA;
    
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (data[i] ^ xorKey);
        }
    
        return data;
    }
    private Path saveXorEncryptedData(byte[] xorEncryptedData, File textFile) {
        // Save the XOR encrypted data to a file
        Path filePath = Paths.get("C:/Users/akank/OneDrive/Documents/projects/Digital-Steganography-Using-JAVA/saved/xor_encrypted_data.txt");
        try {
            Files.write(filePath, xorEncryptedData, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }
    private byte[] performAesWithLsb(File imageFile) {
        try {
            BufferedImage originalImage = ImageIO.read(imageFile);
            byte[] imageBytes = ((DataBufferByte) originalImage.getRaster().getDataBuffer()).getData();

            String keyString = "YourSecretKey";
            byte[] keyData = keyString.getBytes();
            Key key = new SecretKeySpec(keyData, "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(imageBytes);

            // Embed data using LSB steganography
            String secretMessage = "YourSecretMessage";
            byte[] messageBytes = secretMessage.getBytes();

            embedMessage(encryptedBytes, messageBytes);

            return encryptedBytes;
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    private void embedMessage(byte[] container, byte[] message) {
        int messageLength = message.length;

        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(messageLength);
        byte[] lengthBytes = buffer.array();

        int offset = 0;
        for (int i = 0; i < lengthBytes.length; i++) {
            for (int j = 7; j >= 0; j--) {
                container[offset] &= ~(1 << 0); // Clear the LSB
                container[offset] |= ((lengthBytes[i] >> j) & 1) << 0;
                offset++;
            }
        }

        for (byte b : message) {
            for (int i = 7; i >= 0; i--) {
                container[offset] &= ~(1 << 0); // Clear the LSB
                container[offset] |= ((b >> i) & 1) << 0;
                offset++;
            }
        }
    }


    private Path saveEncryptedImage(byte[] encryptedImage) {
        Path filePath = Paths.get("C:/Users/akank/OneDrive/Documents/projects/Digital-Steganography-Using-JAVA/saved/image.png");
        try {
            Files.write(filePath, encryptedImage, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UploadPageEncode().setVisible(true));
    }
}