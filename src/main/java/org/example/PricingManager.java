package org.example;

import java.math.BigDecimal;

public class PricingManager {

    public void setTariff(Location location, BigDecimal acPrice, BigDecimal dcPrice) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }
        if (acPrice == null || dcPrice == null) {
            throw new IllegalArgumentException("Prices cannot be null");
        }

        Tariff tariff = new Tariff("TAR-" + location.getLocationId(), location);
        tariff.setPrices(acPrice, dcPrice);
        location.setTariff(tariff);
    }

    public void changeTariff(Location location, BigDecimal newAcPrice, BigDecimal newDcPrice) {
        if (location == null || location.getTariff() == null) {
            throw new IllegalStateException("Location has no tariff set yet");
        }
        location.getTariff().setPrices(newAcPrice, newDcPrice);
    }

    public Tariff getTariffForLocation(Location location) {
        return location.getTariff();
    }
}
