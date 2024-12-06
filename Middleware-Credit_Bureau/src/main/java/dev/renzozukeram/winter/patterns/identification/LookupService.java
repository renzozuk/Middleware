package dev.renzozukeram.winter.patterns.identification;

import java.util.HashMap;
import java.util.Map;

public class LookupService {

    private final Map<String, AbsoluteObjectReference> registry = new HashMap<>();

    public void register(String name, AbsoluteObjectReference reference) {
        registry.put(name, reference);
    }

    public AbsoluteObjectReference lookup(String name) {
        return registry.get(name);
    }
}
