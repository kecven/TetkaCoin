package it.kebab.coin.model.data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
public class Block implements Serializable{

    private String txid;
    private Block upBlock;
    private Set<Transaction> transactions;
    private long value;
    private Long blockNumber;
    private byte[] signature;
    private String generatedByMe;

    public Block() {
    }

    @Id
    @Column(unique = false, nullable = false)
    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

//    @Column(unique = true, nullable = false)
    @OneToOne
    @JoinColumn(name="txid")
    public Block getUpBlock() {
        return upBlock;
    }

    public void setUpBlock(Block upBlock) {
        this.upBlock = upBlock;
    }

    @OneToMany(mappedBy="block")
    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Column(unique = false, nullable = false)
    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Column(unique = false, nullable = false)
    public Long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(Long blockNumber) {
        this.blockNumber = blockNumber;
    }

    @Column(unique = true, nullable = false)
    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }
}
