# Changelog

## [Unreleased]
### Changed
- ClothConfig is supported again for forge
- Fix concurrency issue with breaking speed (#180)

## [1.17.1-2.12.2] - 2021-07-31
### Changed
- Add forge support

## [1.17.1-2.12.1] - 2021-07-06
### Changed
- Port to 1.17.1

## [1.17.0-2.12.1] - 2021-07-03
### Changed
- Added some logs (#145)
- Added a configuration `notificationMode` to choose how messages are sent to the player. Values are CHAT or ACTION_BAR (#146)
- Fixed adjacent blocks not being checked around the hit log (#147)

## [1.17.0-2.12.0] - 2021-06-24
### Changed
- Split max size configuration into `maxSearchSize` and `maxTreeSize` (#133)
- Define what action to perform when `maxTreeSize` is reached (#133)
- Define the break order of logs in INSTANTANEOUS mode (#133)
- Polish translations (#134 thanks to @goodideagiver)
- Allow nether warts to be broken instantly in SHIFT DOWN mode (#126)

## [1.17.0-2.11.5] - 2021-06-17
### Changed
- Fix wrong tooltip (max tree size applies to every breaking mode) (#129)

## [1.17.0-2.11.4] - 2021-06-10
### Changed
- Port to 1.17

## [1.16.5-2.11.4] - 2021-05-17
### Changed
- Forge - Fire events when blocks are broken by the mod. This should improve compatibility with other mods (Project MMO) (#107)

## [1.16.5-2.11.3] - 2021-04-26
### Changed
- Fabric - Fixed incompatibility with Croptopia (and possibly other mods too) (#100)

## [1.16.5-2.11.2] - 2021-04-24
### Changed
- Fabric - Move cloth GUI logic into a separate class so it isn't loaded on the server when config file is read (#94)
- Forge - Fix mod not loading when cloth-config is present with Forge 36.1.10+ (#98)

## [1.16.5-2.11.1] - 2021-04-13
### Changed
- Jar is now the same for fabric and forge

## [1.16.5-2.11.0] - 2021-03-24
### Changed
- Add configuration whitelistedNonDecayLeaves to add leaves that doesn't decay and need to be broken by the tool (uses durability) (#77)

## [1.16.5-2.10.2] - 2021-02-17
### Changed
- Cut trees from the branches towards the cut point (#71)

## [1.16.5-2.10.1] - 2021-02-11
### Changed
- Abort search as soon as max log count is reached (#70)

## [1.16.5-2.10.0] - 2021-01-13
### Changed
- Port to 1.16.5

## [1.16.4-2.10.0] - 2021-01-13
### Changed
- Cache configuration so white/black lists are not fetched all the time (#56)
- Add a configuration to whitelist the blocks that can be adjacent to a trunk. By default (empty) this won't apply any restrictions (#54)

## [1.16.4-2.9.0] - 2021-01-09
### Changed
- Break mode matches description text. Now only logs that are ABOVE the y value will be cut. See https://github.com/RakSrinaNa/FallingTree/issues/53#issuecomment-757332779 for a visual representation of each mode (#53)
- Add a new option under `Tree > Search area radius` to only search a tree in a certain area. This can be configured to somehow define how "wide" a tree can be. By default there's no limit (#53)

## [1.16.4-2.8.0] - 2020-12-16
### Changed
- Add option to break only parts of the tree that are above the cut.

## [1.16.4-2.7.0] - 2020-11-14
### Changed
- Allow to whitelist/blacklist by tags

## [1.16.4-2.6.0] - 2020-11-08
### Changed
- Add an option to disable tree cutting (so can only keep leaf decay activated)

## [1.16.4-2.5.1] - 2020-11-02
### Changed
- Port to 1.16.4

## [1.16.3-2.5.1] - 2020-10-02
### Changed
- Add Brazilian Portuguese language (@Eufranio)

## [1.16.3-2.5.0] - 2020-09-29
### Changed
- Add option (activated by default) to break nether tree warts (leaves)

## [1.16.3-2.4.0] - 2020-09-21
### Changed
- Add option to cut trees with mixed log blocks

## [1.16.3-2.3.2] - 2020-09-10
### Changed
- Port to 1.16.3
- Do not take into account "minimumLeavesAroundRequired" when using shift down mode

## [1.16.3-2.3.1] - 2020-09-07
### Changed
- Port to 1.16.3 release candidate 1

## [1.16.2-2.3.1] - 2020-09-04
### Changed
- Set minimum required fabric api (#22)

## [1.16.2-2.3.0] - 2020-09-04
### Changed
- Perform less ticking on leaves (#20)

## [1.16.2-2.2.3] - 2020-09-01
### Changed
- Make break speed mixin cancellable, should fix #19

## [1.16.2-2.2.2] - 2020-08-29
### Changed
- Port to fabric

## [1.16.2-2.2.2] - 2020-08-14
### Changed
- Update pack format

## [1.16.2-2.2.1] - 2020-08-13
### Changed
- Port to 1.16.2

## [1.16.1-2.2.1] - 2020-07-31
### Changed
- Remove some useless logs in the console.
- Add an option `speed_multiplicand` to modify the breaking speed of the log.
- Improve shift_down mode for big trees.

## [1.16.1-2.2.0] - 2020-07-31
### Changed
- Add an option `ignore_tools` to ignore tool detection (this has the same effect as whitelisting everything, including empty hand).
- Add an option `break_mode` to choose how to break the tree. INSTANTANEOUS is the default and what you have been using up to now. Though there is a new addition, SHIFT_DOWN, where instead of breaking everything, the log will slowly fall to the ground as you chop it.

## [1.16.1-2.1.8] - 2020-07-29
### Changed
- Add new `license` field in mod description
- On servers the mod is now only required server-side (so clients can join without the mod)

## [1.16.1-2.1.7] - 2020-07-23
### Changed
- Add an option (`minimum_leaves_around_required` default `0`) to tell how many leaves blocks should be at the top of the tree to apply the mod. This should be useful if you want to break only trees an not your houses. Set it to something like 3 and the whole log stack will be cut only if there's at least 3 leaves next to the top most log.

## [1.16.1-2.1.6] - 2020-07-18
### Changed
- Add option in the configuration (`break_in_creative`) to cut down trees even when in creative

## [1.16.1-2.1.5] - 2020-07-09
### Changed
- Remove ignoreDurabilityLoss, it wasn't working anyway since b5122b87c09e137e95402b14d2621b190bae646b as the event isn't cancelled anymore
- Fix an issue where 1 more durability is applied on the tool
- Setting damage multiplicand to 0 will have the effect of taking away 1 durability on the tool per cut (#12)

## [1.16.1-2.1.4] - 2020-06-26
### Changed
- Port to 1.16.1.

## [1.15.2-2.1.4] - 2020-06-08
### Changed
- Do not cancel event so other mods can see it. Project MMO should now gather stats about the broken log (#11).

## [1.15.2-2.1.3] - 2020-05-02
### Changed
- Add whitelist/blacklist for leaves.

## [1.15.2-2.1.2] - 2020-05-01
### Changed
- Apply silk touch effect (useful if whitelisting mushrooms for example).

## [1.15.2-2.1.1] - 2020-04-29
### Changed
- Add new configuration `tools > damage_multiplicand` to control how much damage the tools take (#9).

## [1.15.2-2.1.0] - 2020-03-08
### Changed
- Refactor configuration by using categories thus making it a bit clearer than having everything stacked up at the same place. (/!\ You may have to redo your configuration if you changed values so back up the configuration before updating in order to copy values after)
- Break leaves without sound when using force breaking leaves (the option with the radius) to avoid breaking your ears (#7)

## [1.15.2-2.0.4] - 2020-02-29
### Changed
- Delay breaking leaves by a few ticks making (fixes #6)

## [1.15.2-2.0.3] - 2020-01-28
### Changed
- Added an option to force destroy leaves withing a certain radius. This will be applied from one of the top most log blocks and will destroy all leaves within it (including leaves that shouldn't despawn because another tree is too close, or because they've been placed by a player). (#5)

## [1.15.2-2.0.2] - 2020-01-23
### Changed
- Port to 1.15.2

