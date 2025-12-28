package com.moeezy.PokeTracker.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name="pokemon")
@Data
@ToString
public class Pokemon {

    @Id
    @Column(name="pokedex_number")
    private int pokedexNumber;

    @Column(name="name")
    private String name;

    @Column(name="primary_type")
    private String primaryType;

    @Column(name="secondary_type")
    private String secondaryType;

    @Column(name="has_gender")
    private boolean hasGender;
}
