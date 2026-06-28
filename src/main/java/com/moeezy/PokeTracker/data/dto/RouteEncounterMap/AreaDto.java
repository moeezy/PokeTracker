package com.moeezy.PokeTracker.data.dto.RouteEncounterMap;

import com.moeezy.PokeTracker.data.dto.RouteEncounterMap.EncounterDto;

import java.util.List;

public class AreaDto {
    String areaId;
    String areaName;
    List<EncounterDto> encounters;

    public AreaDto(String areaId, String areaName, List<EncounterDto> encounters) {
        this.areaId = areaId;
        this.areaName = areaName;
        this.encounters = encounters;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public List<EncounterDto> getEncounters() {
        return encounters;
    }

    public void setEncounters(List<EncounterDto> encounters) {
        this.encounters = encounters;
    }
}
