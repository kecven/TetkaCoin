package it.kebab.coin.model.data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;


@Entity
public class Transaction implements Serializable {

    private String txid;
    private Long amount;
    private String fromUser;
    private String toUser;
    private String type;
    private Block block;
    private Integer userTransactionNumber;
    private byte[] publicKey;
    private byte[] signature;

    public Transaction() {
    }


    @Id
    @Column(unique = true, nullable = false)
    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    @Column(unique = false, nullable = false)
    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    @Column(unique = false, nullable = false)
    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    @Column(unique = false, nullable = false)
    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    @Column(unique = false, nullable = false)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @ManyToOne
    @JoinColumn(name="block")
    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    @Column(unique = false, nullable = false)
    public Integer getUserTransactionNumber() {
        return userTransactionNumber;
    }

    public void setUserTransactionNumber(Integer userTransactionNumber) {
        this.userTransactionNumber = userTransactionNumber;
    }

    @Column(unique = false, nullable = false)
    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    @Column(unique = true, nullable = false)
    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "txid='" + txid + '\'' +
                ", amount=" + amount +
                ", fromUser='" + fromUser + '\'' +
                ", toUser='" + toUser + '\'' +
                ", type='" + type + '\'' +
                ", block=" + block +
                ", userTransactionNumber=" + userTransactionNumber +
                ", publicKey=" + Arrays.toString(publicKey) +
                ", signature=" + Arrays.toString(signature) +
                '}';
    }
}
