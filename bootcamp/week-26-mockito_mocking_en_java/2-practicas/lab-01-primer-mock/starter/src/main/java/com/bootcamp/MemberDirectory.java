package com.bootcamp;

/** Servicio externo con el estado de cada socio. En producción, una API HTTP. */
public interface MemberDirectory {

    boolean isActive(String memberId);
}
