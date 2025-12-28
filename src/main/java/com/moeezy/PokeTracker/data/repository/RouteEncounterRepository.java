package com.moeezy.PokeTracker.data.repository;

import com.moeezy.PokeTracker.data.entity.RouteEncounter;
import com.moeezy.PokeTracker.data.entity.RouteEncounterId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteEncounterRepository extends JpaRepository<RouteEncounter, RouteEncounterId> {
}
