package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
public class PlayerServiceImpl implements PlayerService {

    private PlayerRepository playerRepository;

    @Autowired
    public void setPlayerRepository(PlayerRepository playerRepository){
        this.playerRepository = playerRepository;
    }


    @Transactional
    public List<Player> getAll(Sort sort) {
//        return playerRepository.findAll();
        return playerRepository.findAll(sort);
    }

//    @Transactional
//    public List<Player> findByName(String name, String order) {
//        String value = "%" + name + "%";
//        return playerRepository.findByNameContaining(value, order);
//    }


    @Transactional
    public Integer getCount() {
        long countLong =  playerRepository.count();
        return (int) countLong;
    }

    @Transactional
    public Player add(Player player) {
        return playerRepository.save(player);
    }


    @Transactional
    public Optional<Player> getById(Long id) {
        return playerRepository.findById(id);
    }

    @Transactional
    public Player edit(Player player) {
        return playerRepository.saveAndFlush(player);
    }

    @Override
    public void delete(Player player) {
        playerRepository.delete(player);
    }
}
