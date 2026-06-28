package com.moeezy.PokeTracker.data.repository;

import com.moeezy.PokeTracker.data.dto.RouteEncounterMap.RouteEncounterSqlRowProjection;
import com.moeezy.PokeTracker.data.entity.RouteEncounter;
import com.moeezy.PokeTracker.data.entity.RouteEncounterId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteEncounterRepository extends JpaRepository<RouteEncounter, RouteEncounterId> {

    @Query("SELECT r from RouteEncounter r WHERE r.routeId = :routeId AND r.time = :time")
    List<RouteEncounter> findAvailableRoutePokemon(int routeId, String time);

    @Query(value = """
    select
        r.route_id,
        r.area_id,
        r.route_name,
        r.area_name,
        re.pokedex_number,
        p.name,
        CASE
                when up.user_id = :userId and up.caught = true then true
                else false
                end AS caught,
        re.time_of_day,
        re.radio,
        re.swarm,
        re.encounter_method
        from routes r
        join route_encounters re
        on r.route_id = re.route_id
        and r.area_id = re.area_id
        join pokemon p on re.pokedex_number = p.pokedex_number
        left join user_pokemon up on re.pokedex_number = up.pokedex_number
        order by r.route_id, r.area_id, re.encounter_method, re.pokedex_number asc
    """, nativeQuery = true)
    List<RouteEncounterSqlRowProjection> findAllMapPokemon(int userId);
}
