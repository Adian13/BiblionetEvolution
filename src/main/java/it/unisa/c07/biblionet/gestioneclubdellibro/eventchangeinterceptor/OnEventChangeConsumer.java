package it.unisa.c07.biblionet.gestioneclubdellibro.eventchangeinterceptor;

import java.util.function.Consumer;

import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Evento;

/**
 * Interfaccia.
 */
public interface OnEventChangeConsumer extends Consumer<Evento> { }
