# Falling Tree

This mod will allow you to break a full tree by only breaking one log off of it. Sneaking will disable this functionality.

![Demonstration of breaking a tree](https://github.com/RakSrinaNa/FallingTree/raw/1.14.4/assets/demo.gif)

Several options are available in the config file:
- logs_whitelisted: The mod automatically supports all bloks identified as logs. However if you want to manually add another kind of block (because it isn't identified as a log, or because you make trees out of obsidian) under the form `mod_id:block_id` (eg: `biomesoplenty:fir_log`).
- tools_whitelisted: The mod automatically supports all axes as tools. However if you can manually add more (because it isn't identified as an axe, or because you want to break your trees with a special tool) under the form `mod_id:item_id` (eg: `powder_power:axe_trilium`).
- logs_blacklisted: If you want to disallow some blocks to be broken all at once, you can do so here (if a block is both present in the whitelist & blacklist, the blacklist will win).
- tools_blacklisted: If you want to disallow the use of some tools, you can do so here (if an item is both present in the whitelist & blacklist, the blacklist will win).
- ignore_durability: If activated breaking down a tree won't damage your tool.
- max\_log\_count: The maximum number of log a tree can be mad of (if more the mod won't apply).
- preserve_tools: If this option is enabled your tool won't be broken by chopping down a big tree, it'll instead be left with 1 of durability.
- reverse_sneaking: If this option is enabled you'll need to sneak in order to break the whole tree.
- break_leaves: If this is set to true, leaves will despawn instantly when a tree is broken (even if not cut in one shot).
