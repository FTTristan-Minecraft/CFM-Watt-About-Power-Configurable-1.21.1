✨ **About the Mod** ✨

Have you ever wanted to flip a switch and bring your Minecraft home to life—without feeding your resource hungry electricity generator? With the help of this addon for MrCrayfish's new furniture mod, "Furniture Refurbished", now you can! ⚡

This mod adds a new block called the circuit breaker. This nifty little block is the new heart for your home's electricity system, drawing power straight from your favorite tech mods like Mekanism, Tech Reborn, and Create: Crafts and Additions. No more having to feed your generator coal that could be used to craft your torches and fuel your campfires—just simple, sustainable energy that keeps your lamps glowing and your ceiling fans spinning.

 

![](https://media.forgecdn.net/attachments/description/1170251/description_46ddc807-aaad-4342-bb90-ab6143075cb2.png)

![](https://media.forgecdn.net/attachments/description/1170251/description_3047e4e7-129f-4f3f-874c-bc834dcc5fa7.png)

*Tested on Mekanism, Tech Reborn, Integrated Dynamics, and Create Crafts & Additions.

Full credit for the original mod and its content goes to MrCrayfish. Thank you for your amazing work!


🔧 **Server config (`cfm_wap-server.toml`)**

- `maximumNodesInCircuitBreakerNetwork` (default 10000): nodes a single circuit breaker can power.
- `energyDrawPerTick` (default 1.0): energy an online breaker consumes every tick. Fractions are exact, e.g. `0.1` is 1 energy every 10 ticks.
- `lightningStrikeOutageRadius` (default 30): breakers within this many blocks of a lightning strike switch off. `0` disables the outage completely.
- `nodeSearchIntervalTicks` (default 10): how often a breaker rescans its network. The result is reused in between, which keeps big networks cheap.

Only nodes inside Furniture Refurbished's own `powerableAreaRadius` can be reached by a breaker.
