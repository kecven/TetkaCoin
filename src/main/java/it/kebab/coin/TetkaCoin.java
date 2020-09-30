package it.kebab.coin;

import it.kebab.coin.model.data.Transaction;
import it.kebab.coin.model.repository.BlockRepository;
import it.kebab.coin.model.repository.TransactionRepository;
import it.kebab.coin.network.Model;
import it.kebab.coin.network.NetworkServer;
import it.kebab.coin.util.HashUtil;
import it.kebab.coin.util.Setting;
import it.kebab.coin.util.SignatureUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.util.concurrent.atomic.AtomicInteger;


@Component
public class TetkaCoin {

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private HashUtil hashUtil;

    @Autowired
    private SignatureUtil signatureUtil;

    @Autowired
    private NetworkServer networkServer;

    @Autowired
    private Setting setting;

    private AtomicInteger userTransactionNumber;

    private String myWallet;

    @PostConstruct
    private void init() throws InvalidKeyException, IOException, SignatureException, ClassNotFoundException {
        userTransactionNumber = new AtomicInteger(setting.getUserTransactionNumber());
        myWallet = hashUtil.generate(signatureUtil.getByteArrayPublicKey());

        System.out.println("My wallet: " + myWallet);

    }

    public Transaction generateSendTransaction(String toUser, long value) throws InvalidKeyException, IOException, SignatureException {
        Transaction result = new Transaction();

        int myNumberTransaction = userTransactionNumber.incrementAndGet();
        setting.setUserTransactionNumberAndSave(myNumberTransaction);

        result.setFromUser(myWallet);
        result.setType("send");
        result.setToUser(toUser);
        result.setAmount(value);
        result.setPublicKey(signatureUtil.getByteArrayPublicKey());
        result.setUserTransactionNumber(myNumberTransaction);
        result.setTxid(generateTxidForTransaction(result));
        result.setSignature(generateSignatureForTransaction(result));

        Model model = new Model();
        model.setTransaction(result);
        networkServer.sendAllNodes(model);

        return result;
    }

    private String generateTxidForTransaction(Transaction transaction){
        String txidText = transaction.getFromUser()
                + transaction.getToUser()
                + transaction.getType()
                + transaction.getAmount()
                + transaction.getUserTransactionNumber();
        return hashUtil.generate(txidText);
    }

    private byte[] generateSignatureForTransaction(Transaction transaction) throws InvalidKeyException, IOException, SignatureException {
        String signatureText = transaction.getTxid()
                + transaction.getFromUser()
                + transaction.getToUser()
                + transaction.getType()
                + transaction.getAmount()
                + transaction.getUserTransactionNumber();
        return signatureUtil.signingMessage(signatureText.getBytes());
    }

    public boolean checkTransaction(Transaction transaction) throws ClassNotFoundException, SignatureException, InvalidKeyException, IOException {
        String signatureText = transaction.getTxid()
                + transaction.getFromUser()
                + transaction.getToUser()
                + transaction.getType()
                + transaction.getAmount()
                + transaction.getUserTransactionNumber();

        return generateTxidForTransaction(transaction).equals(transaction.getTxid())
                && transaction.getFromUser().equals(hashUtil.generate(transaction.getPublicKey()))
                && signatureUtil.verifyMessage(signatureText.getBytes(), transaction.getSignature(), transaction.getPublicKey());
    }

    public void sentTransactionToUser() throws InvalidKeyException, IOException, SignatureException {
        Transaction transaction = generateSendTransaction("user", 10);
    }

}
