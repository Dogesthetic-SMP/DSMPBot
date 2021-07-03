package me.elJoa.dsmpbot.utilities;

import com.djrapitops.plan.query.CommonQueries;
import com.djrapitops.plan.query.QueryService;

import java.sql.ResultSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class QueryAPIAccessor {

    private final QueryService queryService;

    public QueryAPIAccessor(QueryService queryService) {
        this.queryService = queryService;
        ensureDBSchemaMatch();
    }

    private void ensureDBSchemaMatch() {
        CommonQueries queries = queryService.getCommonQueries();
        if (!queries.doesDBHaveTable("plan_sessions")
                || !queries.doesDBHaveTableColumn("plan_sessions", "uuid")) {
            throw new IllegalStateException("Cuidado con la DB, no se encontró plan_sessions o no se encontró uuid en plan_sessions.");
        }
    }

    public long getAFKTime(String playerName) {
        CommonQueries queries = queryService.getCommonQueries();
        Optional<UUID> playerUUID = queries.fetchUUIDOf(playerName);
        String playerId;
        try {
            playerId = String.valueOf(playerUUID.get());
        } catch (NoSuchElementException ignored) {
            return -1;
        }
        String sql = "SELECT SUM(afk_time) as afk_time FROM plan_sessions WHERE uuid=? GROUP BY uuid=?";

        return queryService.query(sql, statement -> {
            statement.setString(1, playerId);
            statement.setString(2, playerId);
            try (ResultSet results = statement.executeQuery()) {
                return results.next() ? results.getLong("afk_time") : -1;
            }
        });
    }
}