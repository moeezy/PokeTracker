package com.moeezy.PokeTracker.service;

import com.moeezy.PokeTracker.data.entity.Route;
import com.moeezy.PokeTracker.data.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RouteService {
    private final RouteRepository routeRepository;

    @Autowired
    public RouteService(RouteRepository routeRepository){
        this.routeRepository = routeRepository;
    }

    public List<Route> routes(){
        return routeRepository.findAll();
    }
}
