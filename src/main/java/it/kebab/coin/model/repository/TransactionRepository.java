package it.kebab.coin.model.repository;

import it.kebab.coin.model.data.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, String> {
}
