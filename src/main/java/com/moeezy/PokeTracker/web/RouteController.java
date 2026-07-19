package com.moeezy.PokeTracker.web;

import com.moeezy.PokeTracker.data.entity.Route;
import com.moeezy.PokeTracker.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/v1/Routes")
public class RouteController {
    private final RouteService routeService;

    @Autowired
    public RouteController(RouteService routeService){
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<List<Route>> getAllRoutes(){
        List<Route> allRoutes = routeService.routes();

        if(allRoutes.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(allRoutes);
    }
}
