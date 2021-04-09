## [Forge-1.16.5-2.11.1] - 2021-04-09

* Take into account break speed modifications from other mods that may have handled the event before (#80)
* Use official minecraft mappings
* Jar should now be signed

## [Forge-1.16.5-2.11.0] - 2021-03-24

* Fixed leaves whitelist/blacklist not being saved (#76)
* Add configuration whitelistedNonDecayLeaves to add leaves that doesn't decay and need to be broken by the tool (uses durability) (#77)

## [Forge-1.16.5-2.10.2] - 2021-02-17

* Cut trees from the branches towards the cut point (#71)

## [Forge-1.16.5-2.10.1] - 2021-02-11

* Abort search as soon as max log count is reaches (#70)

## [Forge-1.16.5-2.10.0] - 2021-01-15

* Port to 1.16.5

## [Forge-1.16.4-2.10.0] - 2021-01-13

* Cache configuration so white/black lists are not fetched all the time (#56)
* Add a configuration to whitelist the blocks that can be adjacent to a trunk. By default (empty) this won't apply any restrictions (#54)

## [Forge-1.16.4-2.9.0] - 2021-01-09

* Break mode matches description text. Now only logs that are ABOVE the y value will be cut. See https://github.com/RakSrinaNa/FallingTree/issues/53#issuecomment-757332779 for a visual representation of each mode (#53)
* Add a new option under `Tree > Search area radius` to only search a tree in a certain area. This can be configured to somehow define how "wide" a tree can be. By default there's no limit (#53)

## [Forge-1.16.4-2.8.3] - 2021-01-07

* Fix server starting when cloth-config is installed on server (#52)

## [Forge-1.16.4-2.8.2] - 2021-01-06

* Only add config GUI if cloth config is present ; thus makin cloth config an optional dependency

## [Forge-1.16.4-2.8.1] - 2020-12-20

* Avoid having config tooltips getting out of screen by reducing text width (#40)

## [Forge-1.16.4-2.8.0] - 2020-12-16

* Add option to break only parts of the tree that are above the cut.
* Add GUI to change configuration (Main menu > Mods > FallingTree > Config).

## [Forge-1.16.4-2.7.0] - 2020-11-14

* Allow to whitelist/blacklist by tags

## [Forge-1.16.4-2.6.0] - 2020-11-08

* Add an option to disable tree cutting (so can only keep leaf decay activated)

## [Forge-1.16.4-2.5.0] - 2020-11-02

* Port to 1.16.4

## [Forge-1.16.3-2.5.0] - 2020-09-29

* Add option (activated by default) to break nether tree warts (leaves)

## [Forge-1.16.3-2.4.0] - 2020-09-21

* Add option to cut trees with mixed log blocks

## [Forge-1.16.3-2.3.1] - 2020-09-11

* Port to 1.16.3

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

