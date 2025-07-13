package com.akazaki.api.infrastructure.exceptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException() {
        super("La commande demandée est introuvable. Veuillez vérifier l'identifiant.");
    }
}
