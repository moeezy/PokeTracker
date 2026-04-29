package com.moeezy.PokeTracker.web.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class PokemonInsertException extends DataIntegrityViolationException {
    public PokemonInsertException(String message) {
        super(message);
    }
}
