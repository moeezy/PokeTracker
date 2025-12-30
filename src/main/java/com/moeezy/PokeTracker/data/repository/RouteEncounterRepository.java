package com.moeezy.PokeTracker.data.repository;

import com.moeezy.PokeTracker.data.entity.RouteEncounter;
import com.moeezy.PokeTracker.data.entity.RouteEncounterId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RouteEncounterRepository extends JpaRepository<RouteEncounter, RouteEncounterId> {

    @Query("SELECT r from RouteEncounter r WHERE r.routeId = :routeId AND r.time = :time")
    Optional<RouteEncounter> findAvailableRoutePokemon(int routeId, String time);
}
