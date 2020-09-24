## [1.14.4-2.4.0] - 2020-09-24
Port back features

- Delay breaking leaves by a few ticks making (fixes #6)
- Refactor configuration by using categories thus making it a bit clearer than having everything stacked up at the same place. (/!\ You may have to redo your configuration if you changed values so back up the configuration before updating in order to copy values after)
- Break leaves without sound when using force breaking leaves (the option with the radius) to avoid breaking your ears (#7)
- Add new configuration `tools > damage_multiplicand` to control how much damage the tools take (#9).
- Apply silk touch effect (useful if whitelisting mushrooms for example).
- Add whitelist/blacklist for leaves.
- Do not cancel event so other mods can see it. Project MMO should now gather stats about the broken log (#11).
- Remove ignoreDurabilityLoss, it wasn't working anyway since b5122b87c09e137e95402b14d2621b190bae646b as the event isn't cancelled anymore
- Fix an issue where 1 more durability is applied on the tool
- Setting damage multiplicand to 0 will have the effect of taking away 1 durability on the tool per cut (#12
- Add option in the configuration (`break_in_creative`) to cut down trees even when in creative
* Add an option (`minimum_leaves_around_required` default `0`) to tell how many leaves blocks should be at the top of the tree to apply the mod. This should be useful if you want to break only trees an not your houses. Set it to something like 3 and the whole log stack will be cut only if there's at least 3 leaves next to the top most log.
* On servers the mod is now only required server-side (so clients can join without the mod).
* Add an option `ignore_tools` to ignore tool detection (this has the same effect as whitelisting everything, including empty hand).
* Add an option `break_mode` to choose how to break the tree. INSTANTANEOUS is the default and what you have been using up to now. Though there is a new addition, SHIFT_DOWN, where instead of breaking everything, the log will slowly fall to the ground as you chop it.
* Add an option `speed_multiplicand` to modify the breaking speed of the log.
* Perform less ticking on leaves (#20)
* Check world type before scheduling leaf breaking (#23)
* Do not take into account "minimumLeavesAroundRequired" when using shift down mode
* Add option to cut trees with mixed log blocks

## [1.14.4-2.0.3] - 2020-02-16
- Backport 1.15 features
  - Add an option in the config to instantly break leaves (defaults to false).
  - Added an option to force destroy leaves withing a certain radius. This will be applied from one of the top most log blocks and will destroy all leaves within it (including leaves that shouldn't despawn because another tree is too close, or because they've been placed by a player).

## [1.14.4-1.1.0] - 2019-12-16
- Automatically support all logs by default (now use the blacklist if you want to exclude some logs)
- Automatically support all axes by default (now use the blacklist if you want to exclude some tools)

## [1.14.4-1.0.4] - 2019-10-28
- Added the option to require sneaking to break a whole tree.

## [1.14.4-1.0.3] - 2019-10-27
- Modified field in the config to use underscores.

## [1.14.4-1.0.2] - 2019-10-27
- Added the option to preserve tools, this time working(#1)
- Added a notification to the player when the tree is too big.
- Modified the order the tree is broken to break logs closer to the hit point first.

## [1.14.4-1.0.1] - 2019-10-27
- Added the option to preserve tools (#1)

## [1.14.4-1.0.0] - 2019-10-26
- Initial release
