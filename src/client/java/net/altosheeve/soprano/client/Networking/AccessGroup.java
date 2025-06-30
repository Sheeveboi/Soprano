package net.altosheeve.soprano.client.Networking;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.InetAddress;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;

/**
 * Provides group management and data encryption utilities for access groups. Some methods may require a ConnectionInterface
 * as this class does not provide any native networking methods or utilities. See ConnectionInterface and SocketClient
 * for details on networking implementations.
 * @since 1.0.1
 * @author Alto, Feather
 */

public class AccessGroup extends ArrayList<AccessGroup.GroupMember> {
    private final String salt;
    private ArrayList<InetAddress> addresses;
    private final byte[] iv;
    public final String groupName;
    private final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    private final SecretKeyFactory fac = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    public AccessGroup(String groupName,String salt) throws NoSuchPaddingException, NoSuchAlgorithmException {
        super();
        this.salt = salt;
        this.groupName = groupName;
        this.iv = createIV();
    }
    public AccessGroup(String groupName, String salt, byte[] iv) throws NoSuchPaddingException, NoSuchAlgorithmException {
        super();
        this.salt = salt;
        this.iv = iv;
        this.groupName = groupName;
    }
    public static byte[] createIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return iv;
    }
    public String encrypt(String inp) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        //secret key
        KeySpec spec = new PBEKeySpec(salt.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey secret = new SecretKeySpec(this.fac.generateSecret(spec).getEncoded(), "AES");

        //final encrypted data generation
        this.cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(iv));
        byte[] cipherText = cipher.doFinal(inp.getBytes());

        return Base64.getEncoder().encodeToString(cipherText);
    }

    public String decrypt(String inp) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        //secret key
        KeySpec spec = new PBEKeySpec(salt.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey secret = new SecretKeySpec(this.fac.generateSecret(spec).getEncoded(), "AES");

        //final decrypted data generation
        this.cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(inp));

        return new String(plainText);
    }
    public static class GroupMember {

    }
}
