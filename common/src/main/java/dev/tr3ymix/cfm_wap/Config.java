package dev.tr3ymix.cfm_wap;

import com.mrcrayfish.framework.api.config.ConfigProperty;
import com.mrcrayfish.framework.api.config.ConfigType;
import com.mrcrayfish.framework.api.config.DoubleProperty;
import com.mrcrayfish.framework.api.config.FrameworkConfig;
import com.mrcrayfish.framework.api.config.IntProperty;

public class Config {

    @FrameworkConfig(id = CFM_WAP.MOD_ID, name = "server", type = ConfigType.SERVER_SYNC)
    public static final Server SERVER = new Server();

    public static class Server{
        @ConfigProperty(name = "electricity", comment = "Electricity related properties")
        public final Electricity electricity = new Electricity();

        public static class Electricity{
            @ConfigProperty(name = "maximumLinksPerCircuitBreaker", comment = """
                The maximum amount of links that can be connected to a circuit breaker.""")
            public final IntProperty maximumLinksPerCircuitBreaker = IntProperty.create(16, 1, 256);

            @ConfigProperty(name = "maximumNodesInCircuitBreakerNetwork", comment = """
                The maximum amount of nodes in a network that can be powered by the circuit breaker.
                Note: only nodes within the powerable area radius of Refurbished Furniture's own config are reached.
                Values above 32767 are not possible, the circuit breaker screen cannot display them.""")
            public final IntProperty maximumNodesInCircuitBreakerNetwork = IntProperty.create(10000, 1, 30000);

            @ConfigProperty(name = "energyDrawPerTick", comment = """
                The energy an online circuit breaker consumes every tick.
                Fractions are supported and are paid out exactly (to 1/1000), for example 0.1 consumes 1 energy every 10 ticks.""")
            public final DoubleProperty energyDrawPerTick = DoubleProperty.create(1.0, 0.0, 1000.0);

            @ConfigProperty(name = "nodeSearchIntervalTicks", comment = """
                How often (in ticks) an online circuit breaker searches its network for nodes to power.
                In between, the previous result is reused, which is what keeps networks with thousands of nodes cheap.
                Newly linked nodes are powered after at most this many ticks, destroyed nodes are dropped immediately.""")
            public final IntProperty nodeSearchIntervalTicks = IntProperty.create(10, 1, 200);

            @ConfigProperty(name = "lightningStrikeOutageRadius", comment = """
                The radius (in blocks) within which the circuit breaker is disabled when a lightning bolt occurs.""")
            public final IntProperty lightningStrikeOutageRadius = IntProperty.create(30, 15, 1024);
        }
    }
}
