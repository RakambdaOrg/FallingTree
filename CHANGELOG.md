## [Forge-1.16.2-2.4.0] - 2020-09-21
* Add option to cut trees with mixed log blocks

## [Forge-1.16.2-2.3.1] - 2020-09-09
* Check world type before scheduling leaf breaking (#23)
* Do not take into account "minimumLeavesAroundRequired" when using shift down mode

## [Forge-1.16.2-2.3.0] - 2020-09-04
* Perform less ticking on leaves (#20)

## [Forge-1.16.2-2.2.2] - 2020-08-14
* Update pack format

## [Forge-1.16.2-2.2.1] - 2020-08-13
* Port to 1.16.2

## [1.16.1-2.2.1] - 2020-07-31
* Remove some useless logs in the console.
* Add an option `speed_multiplicand` to modify the breaking speed of the log.
* Improve shift_down mode for big trees.

## [1.16.1-2.2.0] - 2020-07-31
* Add an option `ignore_tools` to ignore tool detection (this has the same effect as whitelisting everything, including empty hand).
* Add an option `break_mode` to choose how to break the tree. INSTANTANEOUS is the default and what you have been using up to now. Though there is a new addition, SHIFT_DOWN, where instead of breaking everything, the log will slowly fall to the ground as you chop it.

## [1.16.1-2.1.8] - 2020-07-29
* Add new `license` field in mod description
* On servers the mod is now only required server-side (so clients can join without the mod)

## [1.16.1-2.1.7] - 2020-07-23
- Add an option (`minimum_leaves_around_required` default `0`) to tell how many leaves blocks should be at the top of the tree to apply the mod. This should be useful if you want to break only trees an not your houses. Set it to something like 3 and the whole log stack will be cut only if there's at least 3 leaves next to the top most log.

## [1.16.1-2.1.6] - 2020-07-18
- Add option in the configuration (`break_in_creative`) to cut down trees even when in creative

## [1.16.1-2.1.5] - 2020-07-09
- Remove ignoreDurabilityLoss, it wasn't working anyway since b5122b87c09e137e95402b14d2621b190bae646b as the event isn't cancelled anymore
- Fix an issue where 1 more durability is applied on the tool
- Setting damage multiplicand to 0 will have the effect of taking away 1 durability on the tool per cut (#12)

## [1.16.1-2.1.4] - 2020-06-26
- Port to 1.16.1.

## [1.15.2-2.1.4] - 2020-06-08
- Do not cancel event so other mods can see it. Project MMO should now gather stats about the broken log (#11).

## [1.15.2-2.1.3] - 2020-05-02
- Add whitelist/blacklist for leaves.

## [1.15.2-2.1.2] - 2020-05-01
- Apply silk touch effect (useful if whitelisting mushrooms for example).

## [1.15.2-2.1.1] - 2020-04-29
- Add new configuration `tools > damage_multiplicand` to control how much damage the tools take (#9).

## [1.15.2-2.1.0] - 2020-03-08
- Refactor configuration by using categories thus making it a bit clearer than having everything stacked up at the same place. (/!\ You may have to redo your configuration if you changed values so back up the configuration before updating in order to copy values after)
- Break leaves without sound when using force breaking leaves (the option with the radius) to avoid breaking your ears (#7)

## [1.15.2-2.0.4] - 2020-02-29
- Delay breaking leaves by a few ticks making (fixes #6)

## [1.15.2-2.0.3] - 2020-01-28
- Added an option to force destroy leaves withing a certain radius. This will be applied from one of the top most log blocks and will destroy all leaves within it (including leaves that shouldn't despawn because another tree is too close, or because they've been placed by a player). (#5)

## [1.15.2-2.0.2] - 2020-01-23
- Port to 1.15.2

