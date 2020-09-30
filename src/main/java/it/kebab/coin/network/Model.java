package it.kebab.coin.network;


import it.kebab.coin.model.data.Block;
import it.kebab.coin.model.data.Transaction;

import java.io.Serializable;
import java.util.Set;

public class Model implements Serializable {

    private Transaction transaction;
    private Block block;
    private String msgType;
    private Set<String> addressNodes;

    public Model(Transaction transaction, Block block, String msgType, Set<String> addressNodes) {
        this.transaction = transaction;
        this.block = block;
        this.msgType = msgType;
        this.addressNodes = addressNodes;
    }

    public Model() {
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Set<String> getAddressNodes() {
        return addressNodes;
    }

    public void setAddressNodes(Set<String> addressNodes) {
        this.addressNodes = addressNodes;
    }

    @Override
    public String toString() {
        return "Model{" +
                "transaction=" + transaction +
                ", block=" + block +
                ", msgType='" + msgType + '\'' +
                ", addressNodes=" + addressNodes +
                '}';
    }
}
