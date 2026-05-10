package com.moeezy.PokeTracker.data.repository;

import com.moeezy.PokeTracker.data.entity.Route;
import com.moeezy.PokeTracker.data.entity.RouteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route, RouteId> {
}
