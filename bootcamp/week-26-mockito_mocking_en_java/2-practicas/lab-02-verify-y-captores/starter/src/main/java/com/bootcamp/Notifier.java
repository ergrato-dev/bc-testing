package com.bootcamp;

/** Envía avisos a los socios. En producción, correo electrónico. */
public interface Notifier {

    void send(Notice notice);
}
