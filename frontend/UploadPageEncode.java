import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.io.File;
import java.util.List;

public class UploadPageEncode extends JFrame {

    private JTextArea droppedFilesTextArea;

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
            // Add your browse button functionality here
            openFileChooser();
        });
        firstBoxPanel.add(browseButton);
        panel.add(firstBoxPanel, BorderLayout.CENTER);

        // Content has been ENCODED button
        JPanel secondBoxPanel = createOutlinedBoxPanel("Content has been ENCODED");
        JButton downloadButton = new JButton("Download");
        downloadButton.setPreferredSize(new Dimension(120, 30)); 
        downloadButton.addActionListener(e -> {
            // Add your download button functionality here
            openKeyDialog();
        });

        // Customize the size, font, and color of the text
        JLabel encodedTextLabel = new JLabel("                                Content has been ENCODED");
        encodedTextLabel.setFont(new Font("Arial", Font.BOLD, 30)); 
        encodedTextLabel.setForeground(Color.BLACK); 

        secondBoxPanel.setLayout(new BorderLayout());
        secondBoxPanel.add(encodedTextLabel, BorderLayout.CENTER);
        secondBoxPanel.add(downloadButton, BorderLayout.SOUTH);

        panel.add(secondBoxPanel, BorderLayout.SOUTH);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 90, 20, 90));

        setContentPane(panel);
        setLocationRelativeTo(null);
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
        keyTextArea.setEditable(false);

        JButton copyButton = new JButton("Copy Key");
        copyButton.setPreferredSize(new Dimension(100, 25)); 
        copyButton.addActionListener(e -> {
            // Add your key copy functionality here
            String key = generateKey(); 
            keyTextArea.setText(key);
            StringSelection stringSelection = new StringSelection(key);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });

        keyPanel.add(new JLabel("Here is the Key"), BorderLayout.NORTH);
        keyPanel.add(keyTextArea, BorderLayout.CENTER);
        keyPanel.add(copyButton, BorderLayout.SOUTH);

        keyDialog.setContentPane(keyPanel);
        keyDialog.setVisible(true);
    }

    private void openFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(this, "Selected file: " + selectedFile.getName());
        }
    }

    // Replace this function with your actual key generation logic
    private String generateKey() {
        return "GeneratedKey123";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UploadPageEncode().setVisible(true));
    }
}
