package it.kebab.coin.model.repository;

import it.kebab.coin.model.data.Block;
import org.springframework.data.repository.CrudRepository;

public interface BlockRepository extends CrudRepository<Block, String> {

}
