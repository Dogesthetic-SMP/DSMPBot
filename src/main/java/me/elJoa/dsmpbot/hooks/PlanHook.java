package me.elJoa.dsmpbot.hooks;

import com.djrapitops.plan.capability.CapabilityService;
import com.djrapitops.plan.query.QueryService;
import me.elJoa.dsmpbot.utilities.QueryAPIAccessor;

import java.util.Optional;

public class PlanHook {

    public PlanHook() {}

    public Optional<QueryAPIAccessor> hookIntoPlan() {
        if (!areAllCapabilitiesAvailable()) return Optional.empty();
        return Optional.ofNullable(createQueryAPIAccessor());
    }

    private boolean areAllCapabilitiesAvailable() {
        CapabilityService capabilities = CapabilityService.getInstance();
        return capabilities.hasCapability("QUERY_API");
    }

    private QueryAPIAccessor createQueryAPIAccessor() {
        try {
            return new QueryAPIAccessor(QueryService.getInstance());
        } catch (IllegalStateException planIsNotEnabled) {
            // Plan is not enabled, handle exception
            return null;
        }
    }
}