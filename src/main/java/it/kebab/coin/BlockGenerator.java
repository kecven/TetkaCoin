package it.kebab.coin;

import it.kebab.coin.model.data.Block;
import it.kebab.coin.model.data.Transaction;
import it.kebab.coin.model.repository.BlockRepository;
import it.kebab.coin.network.Model;
import it.kebab.coin.network.NetworkServer;
import it.kebab.coin.util.Const;
import it.kebab.coin.util.HashUtil;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;


@Component
public class BlockGenerator {

    public volatile Set<Transaction> newTransactions = new ConcurrentSkipListSet();

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private HashUtil hashUtil;

    @Autowired
    private TetkaCoin tetkaCoin;

    @Autowired
    private NetworkServer networkServer;

    private Block lastBlock;
    private volatile long lastBlockId = 0L;

    @PostConstruct
    public void generateNewBlock(){
        blockRepository.findAll().forEach(e->lastBlock = e);
        try {
            while (true){
                Block newBlock = new Block();
                newBlock.setBlockNumber(blockRepository.count());
                newBlock.setTransactions(newTransactions);
                newBlock.setUpBlock(lastBlock);
                newBlock.setValue(new Random().nextLong());
                newBlock.setBlockNumber(lastBlockId);
                newBlock.setTxid(generateTxidForTransaction(newBlock));

                if (checkBlock(newBlock)){
                    Model model = new Model();
                    model.setBlock(newBlock);
                    networkServer.sendAllNodes(model);
                    lastBlockId++;
                }
            }
        } catch (Exception e){
            generateNewBlock();
        }
    }

    private String generateTxidForTransaction(Block block){
        String txidText = block.getBlockNumber()
                + block.getValue()
                + block.getUpBlock().getTxid()
                + block.getTransactions().toString();
        return hashUtil.generate(txidText);
    }

    public boolean checkBlock(Block block) {
        for (Transaction transaction : block.getTransactions()){
            try {
                if ( ! tetkaCoin.checkTransaction(transaction)){
                    return false;
                }
            } catch (Exception classNotFoundException) {
                return false;
            }
        }

        //check block hash less that our difficult
        return true;
    }

    }
