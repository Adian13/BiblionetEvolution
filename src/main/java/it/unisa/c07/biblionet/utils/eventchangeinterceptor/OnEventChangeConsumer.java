package it.unisa.c07.biblionet.utils.eventchangeinterceptor;

import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Evento;

import java.util.function.Consumer;


/**
 * Interfaccia.
 */
public interface OnEventChangeConsumer extends Consumer<Evento> { }