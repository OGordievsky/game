package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface PlayerService {

    List<Player> getAll(Sort sort);
    Integer getCount();
    Player add(Player player);
    Optional<Player> getById(Long id);
    Player edit(Player player);
    void delete(Player player);
//    List<Player> findByName(String name, String order);

}
