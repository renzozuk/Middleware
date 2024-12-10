package dev.renzozukeram.winter.patterns.identification;

import dev.renzozukeram.winter.enums.RequisitionType;

import java.util.Objects;

public class MethodIdentifier {

    private final RequisitionType requisitionType;
    private final String route;

    public MethodIdentifier(RequisitionType requisitionType, String route) {
        this.requisitionType = requisitionType;
        this.route = route;
    }

    public RequisitionType getRequisitionType() {
        return requisitionType;
    }

    public String getRoute() {
        return route;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodIdentifier that)) return false;
        return requisitionType == that.requisitionType && Objects.equals(route, that.route);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requisitionType, route);
    }

    @Override
    public String toString() {
        return requisitionType.toString() + " " + route;
    }
}
