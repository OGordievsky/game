package com.game.controller;


import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class PlayerController {

    private PlayerService playerService;


    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @RequestMapping(value = "/rest/players", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Player>> getAll(
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize,
            @RequestParam(value = "order", required = false, defaultValue = "ID") PlayerOrder playerOrder,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) String banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel
    ) {

        List<Player> players = playerService.getAll(Sort.by(playerOrder.getFieldName()));

        if (name != null && !name.isEmpty()) {
            ArrayList<Player> tempList = new ArrayList<>();
            for (Player player : players) {
                if (player.getName().toLowerCase().contains(name.toLowerCase())) tempList.add(player);
            }
            players = tempList;
        }

        if (title != null && !title.isEmpty()) {
            ArrayList<Player> tempList = new ArrayList<>();
            for (Player player : players) {
                if (player.getTitle().toLowerCase().contains(title.toLowerCase())) tempList.add(player);
            }
            players = tempList;
        }

        if (race != null) {
            ArrayList<Player> tempList = new ArrayList<>();
            for (Player player : players) {
                if (player.getRace() == race) tempList.add(player);
            }
            players = tempList;
        }

        if (profession != null) {
            ArrayList<Player> tempList = new ArrayList<>();
            for (Player player : players) {
                if (player.getProfession() == profession) tempList.add(player);
            }
            players = tempList;
        }

        if (after != null) {
            ArrayList<Player> tempList = new ArrayList<>();
            for (Player player : players) {
                if (player.getBirthday().after(new Date(after))) tempList.add(player);
            }
            players = tempList;
        }

        if (before != null) {
            ArrayList<Player> tempList = new ArrayList<>();
            for (Player player : players) {
                if (player.getBirthday().before(new Date(before))) tempList.add(player);
            }
            players = tempList;
        }

        if (banned != null && !banned.isEmpty()) {
            ArrayList<Player> tempList = new ArrayList<>();
            for (Player player : players) {
                if (player.isBanned() == Boolean.parseBoolean(banned)) tempList.add(player);
            }
            players = tempList;
        }

        if (minExperience != null) {
            ArrayList<Player> tempList = new ArrayList<>();
            for (Player player : players) {
                if (player.getExperience() >= minExperience) tempList.add(player);
            }
            players = tempList;
        }

        if (maxExperience != null) {
            ArrayList<Player> tempList = new ArrayList<>();
            for (Player player : players) {
                if (player.getExperience() <= maxExperience) tempList.add(player);
            }
            players = tempList;
        }

        if (minLevel != null) {
            ArrayList<Player> tempList = new ArrayList<>();
            for (Player player : players) {
                if (player.getLevel() >= minLevel) tempList.add(player);
            }
            players = tempList;
        }

        if (maxLevel != null) {
            ArrayList<Player> tempList = new ArrayList<>();
            for (Player player : players) {
                if (player.getLevel() <= maxLevel) tempList.add(player);
            }
            players = tempList;
        }


        // here realized a pagination method
        int endIndex = Math.min(((pageNumber + 1) * pageSize), (players.size()));
        int startIndex = pageNumber * pageSize;
        return new ResponseEntity<List<Player>>(players.subList(startIndex, endIndex), HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/players/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getCount(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) String banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel
    ) {
        List<Player> countList = getAll(0, playerService.getCount(), PlayerOrder.ID, name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel).getBody();
        Integer count = countList != null ? countList.size() : 0;
        return new ResponseEntity<Integer>(count, HttpStatus.OK);

//        Integer count = playerService.getCount();
//        return new ResponseEntity<Integer>(count, HttpStatus.OK);

    }


    @RequestMapping(value = "/rest/players", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Player> createPlayer(@RequestBody Map<String, String> json
    ) {
        String name = json.get("name");
        String title = json.get("title");
        String raceStr = json.get("race");
        if (raceStr == null) return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        Race race = Race.valueOf(raceStr);
        String professionStr = json.get("profession");
        if (professionStr == null) return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        Profession profession = Profession.valueOf(professionStr);
        String birthdayStr = json.get("birthday");
        if (birthdayStr == null) return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        long birthday = Long.parseLong(birthdayStr);
        String experienceStr = json.get("experience");
        if (experienceStr == null) return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        int experience = Integer.parseInt(experienceStr);
        boolean banned = false;
        String bannedStr = json.get("banned");
        if (bannedStr != null) banned = Boolean.parseBoolean(bannedStr);

        if (name == null || name.isEmpty() || name.length() > 12 || title == null || title.length() > 30 || experience < 0 || experience > 10000000 || birthday < 0L) {
            return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        }
        Player player = new Player();
        player.setName(name);
        player.setTitle(title);
        player.setRace(race);
        player.setProfession(profession);
        player.setBirthday(new Date(birthday));
        player.setBanned(banned);
        player.setExperience(experience);
        int level = (int) (Math.sqrt(2500.0 + 200.0 * experience) - 50) / 100;
        int untilNextLevel = (int) 50 * (level + 1) * (level + 2) - experience;
        player.setLevel(level);
        player.setUntilNextLevel(Math.max(untilNextLevel, 0));

        return new ResponseEntity<Player>(playerService.add(player), HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/players/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Player> getById(@PathVariable Long id) {
        if (id <= 0L) return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        Optional<Player> optionalPlayer = playerService.getById(id);
        if (!optionalPlayer.isPresent()) return new ResponseEntity<Player>(HttpStatus.NOT_FOUND);
        Player player = optionalPlayer.get();
        return new ResponseEntity<Player>(player, HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/players/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Player> updateById(
            @PathVariable Long id,
            @RequestBody Map<String, String> json
    ) {
        if (id <= 0L) return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        Optional<Player> optionalPlayer = playerService.getById(id);
        if (!optionalPlayer.isPresent()) return new ResponseEntity<Player>(HttpStatus.NOT_FOUND);
        Player player = optionalPlayer.get();
        if (json.containsKey("name")) {
            String name = json.get("name");
            if (name.isEmpty() || name.length() > 12) return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
            player.setName(name);
        }

        if (json.containsKey("title")) {
            String title = json.get("title");
            if (title.isEmpty() || title.length() > 30) return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
            ;
            player.setTitle(title);
        }

        if (json.containsKey("race")) {
            String race = json.get("race");
            try {
                player.setRace(Race.valueOf(race));
            } catch (Exception e) {
                return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
            }
        }
        if (json.containsKey("profession")) {
            String profession = json.get("profession");
            try {
                player.setProfession(Profession.valueOf(profession));
            } catch (Exception e) {
                return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
            }
        }

        if (json.containsKey("birthday")) {
            String birthday = json.get("birthday");
            try {
                long birtLong = Long.parseLong(birthday);
                if (birtLong < 0L) return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
                player.setBirthday(new Date(birtLong));
            } catch (Exception e) {
                return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
            }
        }

        if (json.containsKey("banned")) {
            String banned = json.get("banned");
            try {
                player.setBanned(Boolean.parseBoolean(banned));
            } catch (Exception e) {
                return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
            }
        }

        if (json.containsKey("experience")) {
            String experience = json.get("experience");
            try {
                Integer expInt = Integer.parseInt(experience);
                if (expInt < 0 || expInt > 10000000) return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
                player.setExperience(expInt);
                int level = (int) (Math.sqrt(2500.0 + 200.0 * expInt) - 50) / 100;
                int untilNextLevel = (int) 50 * (level + 1) * (level + 2) - expInt;
                player.setLevel(level);
                player.setUntilNextLevel(Math.max(untilNextLevel, 0));

            } catch (Exception e) {
                return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<Player>(playerService.edit(player), HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/players/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Player> deleteById(@PathVariable Long id) {
        if (id <= 0L) return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        Optional<Player> optionalPlayer = playerService.getById(id);
        if (!optionalPlayer.isPresent()) return new ResponseEntity<Player>(HttpStatus.NOT_FOUND);
        Player player = optionalPlayer.get();
        playerService.delete(player);
        return new ResponseEntity<Player>(player, HttpStatus.OK);
    }

}
