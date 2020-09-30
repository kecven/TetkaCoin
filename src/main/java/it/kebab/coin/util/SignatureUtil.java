package it.kebab.coin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;

@Component
/**
 * need rewrite
 */
public class SignatureUtil {
    private static final Logger log = LoggerFactory.getLogger(SignatureUtil.class);

    /**
     * String to hold the name of the private key file.
     */
    private static final File PRIVATE_KEY_FILE = new File(Const.PRIVATE_KEY_FILENAME);

    /**
     * String to hold name of the public key file.
     */
    private static final File PUBLIC_KEY_FILE = new File(Const.PUBLIC_KEY_FILENAME);


    private PrivateKey privateKey;				 // Приватный ключ
    private PublicKey publicKey;                 // Открытый ключ
    private Signature signature;                 // Цифровая подпсь


    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public byte[] getByteArrayPublicKey() {
        byte[] result = new byte[0];

        try {
            Path path = Paths.get(PUBLIC_KEY_FILE.getAbsolutePath());
            result = Files.readAllBytes(path);
        } catch (FileNotFoundException e) {
            log.error("File not found. " + PUBLIC_KEY_FILE.getAbsolutePath(), e);
        } catch (IOException e) {
            log.error("Error read file. " + PUBLIC_KEY_FILE.getAbsolutePath(), e);
        }

        return result;
    }

    /**
     * Generate key which contains a pair of private and public key using 1024
     * bytes. Store the set of keys in Prvate.key and Public.key files.
     */
    @PostConstruct
    private void initKey() throws NoSuchAlgorithmException, IOException, InvalidKeyException, ClassNotFoundException {
        if (areKeysPresent()){
            readKey();
        } else {
            generateNewKey();
            saveKey();
        }
        signature = Signature.getInstance(Const.SING_ALGORITHM_FOR_DIGITAL_SIGNATURE);
    }

    /**
     * The method checks if the pair of public and private key has been generated.
     *
     * @return flag indicating if the pair of keys were generated.
     */
    private boolean areKeysPresent() {
        if (PRIVATE_KEY_FILE.exists() && PUBLIC_KEY_FILE.exists()) {
            return true;
        }
        return false;
    }

    void saveKey() throws IOException {
        FileOutputStream fileOutputStreamPrivateKey = new FileOutputStream(PRIVATE_KEY_FILE);
        ObjectOutputStream objectOutputStreamPrivateKey = new ObjectOutputStream(fileOutputStreamPrivateKey);
        objectOutputStreamPrivateKey.writeObject(privateKey);
        objectOutputStreamPrivateKey.close();
        fileOutputStreamPrivateKey.close();

        FileOutputStream fileOutputStreamPublicKey = new FileOutputStream(PUBLIC_KEY_FILE);
        ObjectOutputStream objectOutputStreamPublicKey = new ObjectOutputStream(fileOutputStreamPublicKey);
        objectOutputStreamPublicKey.writeObject(publicKey);
        objectOutputStreamPublicKey.close();
        fileOutputStreamPublicKey.close();
    }

    private Object readKey() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStreamKey = new FileInputStream(PRIVATE_KEY_FILE);
        ObjectInputStream objectInputStreamKey = new ObjectInputStream(fileInputStreamKey);
        Object object = objectInputStreamKey.readObject();
        privateKey = (PrivateKey) object;

        fileInputStreamKey = new FileInputStream(PUBLIC_KEY_FILE);
        objectInputStreamKey = new ObjectInputStream(fileInputStreamKey);
        object = objectInputStreamKey.readObject();
        publicKey = (PublicKey) object;
        return object;
    }

    /**
     * Конструктор класса <code>SigningMessage</code> используеться тогда, когда нет пары ключей.
     * Данный конструктор генерирует их автоматически на основании введенных данных, и сохраняет их в
     * поля класса.
     * <code>PrivateKey privateKey</code> - личный ключ
     * <code>PublicKey publicKey</code> - открытый ключ
     * @throws NullPointerException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    private void generateNewKey() throws NoSuchAlgorithmException {

        byte[] seedForRandom = new Long(System.nanoTime()).toString().getBytes();

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(Const.ALGORITHM_FOR_DIGITAL_SIGNATURE);
        keyPairGenerator.initialize(Const.KEY_SIZE, new SecureRandom(seedForRandom));
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    /**
     * Метод <code>signingMessage</code> создает цифровую подпись из указаного открытого текста
     * @param byteMsg - Поток ввода с открытым текстом
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public byte[] signingMessage(byte[] byteMsg) throws IOException, InvalidKeyException, SignatureException {
        //Set private key
        if (privateKey == null) {
            throw new IllegalArgumentException();
        }

        signature.initSign(privateKey);
        signature.update(byteMsg);

        return signature.sign();
    }


    /**
     * Метод <code>verifyMessage</code> проверяет действительность цифровой подписи
     * @param byteMsg - Поток ввода с открытым текстом
     * @param byteSignature - Поток ввода с цифровой подписью
     * @return - Возвращяет результат проверки цифровой подписи
     * @throws InvalidKeyException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws SignatureException
     */
    public boolean verifyMessage(byte[] byteMsg, byte[] byteSignature) throws InvalidKeyException,
            IOException, SignatureException {
        return verifyMessage(byteMsg, byteSignature, publicKey);
    }

    public boolean verifyMessage(byte[] byteMsg, byte[] byteSignature, PublicKey publicKey) throws InvalidKeyException,
            IOException, SignatureException {

        //Verifying message
        signature.initVerify(publicKey);
        signature.update(byteMsg);

        return signature.verify(byteSignature);
    }

    public boolean verifyMessage(byte[] byteMsg, byte[] byteSignature, byte[] publicKeyBytes) throws InvalidKeyException,
            IOException, SignatureException, ClassNotFoundException {

        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(publicKeyBytes));

        PublicKey publicKey = (PublicKey) objectInputStream.readObject();
        //Verifying message
        signature.initVerify(publicKey);
        signature.update(byteMsg);

        return signature.verify(byteSignature);
    }

}
