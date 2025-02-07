package su.nightexpress.sunlight.module.worlds.config;

import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JOption;
import su.nexmedia.engine.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class WorldsConfig {

    public static final Set<InventoryType> INVENTORY_SPLIT_TYPES = Set.of(InventoryType.PLAYER, InventoryType.ENDER_CHEST);

    public static final JOption<Boolean> INVENTORY_SPLIT_ENABLED = JOption.create("Inventory_Split.Enabled", false,
        "Enables Inventory Split feature for different inventories per world.");

    public static final JOption<Set<InventoryType>> INVENTORY_SPLIT_INVENTORIES = new JOption<>("Inventory_Split.Affected_Inventories",
        (cfg, path, def) -> {
            return cfg.getStringSet(path).stream().map(raw -> StringUtil.getEnum(raw, InventoryType.class).orElse(null))
                .filter(type -> type != null && INVENTORY_SPLIT_TYPES.contains(type)).collect(Collectors.toSet());
        },
        INVENTORY_SPLIT_TYPES,
        "List of Inventory Types that are affected by the Split feature.",
        "Allowed values: " + INVENTORY_SPLIT_TYPES.stream().map(Enum::name).collect(Collectors.joining(", "))
    ).setWriter((cfg, path, set) -> cfg.set(path, set.stream().map(Enum::name).toList()));

    public static final JOption<Map<String, Set<String>>> INVENTORY_SPLIT_WORLD_GROUPS = new JOption<>("Inventory_Split.World_Groups",
        (cfg, path, def) -> {
            Map<String, Set<String>> map = new HashMap<>();
            for (String group : cfg.getSection(path)) {
                map.put(group.toLowerCase(), cfg.getStringSet(path + "." + group));
            }
            return map;
        },
        Map.of(
            "survival", Set.of("world", "world_nether", "world_the_end"),
            "creative", Set.of("world_creative")
        ),
        "List of custom world groups, that will share the same inventories in that worlds."
    ).setWriter((cfg, path, map) -> map.forEach((group, set) -> cfg.set(path + "." + group, set)));

    public static final JOption<Boolean> COMMAND_BLOCKER_ENABLED = JOption.create("Command_Blocker.Enabled", false,
        "Sets whether or not certain commands should be blocked in specified worlds.");

    public static final JOption<Map<String, Set<String>>> COMMAND_BLOCKER_COMMANDS = new JOption<>("Command_Blocker.World_Commands",
        (cfg, path, def) -> {
            Map<String, Set<String>> map = new HashMap<>();
            for (String world : cfg.getSection(path)) {
                map.put(world.toLowerCase(), cfg.getStringSet(path + "." + world));
            }
            return map;
        },
        Map.of("world_custom", Set.of("god", "fly")),
        "List of worlds and commands that will be disabled in that worlds.",
        "This can be bypassed with '" + WorldsPerms.BYPASS_COMMANDS.getName() + "' permission.",
        "You only need to put one alias for each command. It will auto-detect all other aliases and block them too."
    ).setWriter((cfg, path, map) -> map.forEach((world, set) -> cfg.set(path + "." + world, set)));

    public static final JOption<Set<String>> NO_FLY_WORLDS = JOption.create("NoFlyWorlds", Set.of("world1", "world2"),
        "List of worlds, where players can not fly.",
        "This can be bypassed with '" + WorldsPerms.BYPASS_FLY.getName() + "' permission."
    );

    public static boolean isInventoryAffected(@NotNull InventoryType inventoryType) {
        return INVENTORY_SPLIT_INVENTORIES.get().contains(inventoryType);
    }
}
