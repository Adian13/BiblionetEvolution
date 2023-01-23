package it.unisa.c07.biblionet.evolution.utils.eventchangeinterceptor;

import java.util.function.Consumer;

import it.unisa.c07.biblionet.evolution.model.entity.Evento;

/**
 * Interfaccia.
 */
public interface OnEventChangeConsumer extends Consumer<Evento> { }