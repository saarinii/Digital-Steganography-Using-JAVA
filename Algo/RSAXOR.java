import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Base64;

public class RSAXOR {
    public static void main(String[] args) throws Exception {
        // Generate a key pair
        KeyPair keyPair = generateKeyPair();

        // Get the public and private keys
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // Encode and write a file
        String inputFile = "input.txt"; // Replace with the path to your input file
        String encodedFile = "encoded.bin";

        long startEncodingTime = System.currentTimeMillis();
        encodeText(inputFile, encodedFile, publicKey);
        long endEncodingTime = System.currentTimeMillis();
        long encodingTime = endEncodingTime - startEncodingTime;

        // Decode and write a file
        String decodedFile = "decoded.txt";

        decodeText(encodedFile, decodedFile, privateKey);
    }

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // You can change the key size for different security levels
        return keyPairGenerator.generateKeyPair();
    }

    public static void encodeText(String inputFile, String encodedFile, PublicKey publicKey) throws Exception {
        byte[] inputBytes = Files.readAllBytes(Paths.get(inputFile));
        long startEncodeTime = System.currentTimeMillis();
        byte[] encodedBytes = encode(inputBytes, publicKey);

        // Generate XOR key and perform XOR on encoded data
        String xorKey = generateXORKey();
        byte[] xorEncodedBytes = performXOR(encodedBytes, Base64.getDecoder().decode(xorKey));

        try (FileOutputStream fileOutputStream = new FileOutputStream(encodedFile)) {
            fileOutputStream.write(xorEncodedBytes);
        }

        long endEncodeTime = System.currentTimeMillis();
        long encodingTime = endEncodeTime - startEncodeTime;

        System.out.println("Encoded successfully. XOR key: " + xorKey);
    }

    public static void decodeText(String encodedFile, String decodedFile, PrivateKey privateKey) throws Exception {
        byte[] xorEncodedBytes = Files.readAllBytes(Paths.get(encodedFile));

        // Perform XOR on encoded data
        byte[] encodedBytes = performXOR(xorEncodedBytes, Base64.getDecoder().decode("secretkey")); // Replace with your XOR key

        long startDecodeTime = System.currentTimeMillis();
        byte[] decodedBytes = decode(encodedBytes, privateKey);
        long endDecodeTime = System.currentTimeMillis();
        long decodingTime = endDecodeTime - startDecodeTime;

        try (FileOutputStream fileOutputStream = new FileOutputStream(decodedFile)) {
            fileOutputStream.write(decodedBytes);
        }

        System.out.println("Decoded successfully.");
    }

    public static byte[] encode(byte[] input, PublicKey publicKey) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return encryptCipher.doFinal(input);
    }

    public static byte[] decode(byte[] input, PrivateKey privateKey) throws Exception {
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
        return decryptCipher.doFinal(input);
    }

    public static byte[] performXOR(byte[] data, byte[] key) {
        byte[] result = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = (byte) (data[i] ^ key[i % key.length]);
        }
        return result;
    }

    public static String generateXORKey() {
        // Generate a random XOR key (you may need to adjust the key length)
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[16]; // Adjust the length based on your requirements
        secureRandom.nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }
}
