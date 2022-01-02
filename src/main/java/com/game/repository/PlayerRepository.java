package com.game.repository;

import com.game.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
//    @Query(
//            value = "SELECT player.* FROM player WHERE name LIKE :value ORDER BY #order",
//            nativeQuery = true
//    )
//    List<Player> findByNameContaining (@Param("value") String value, @Param("order") String order);
}
