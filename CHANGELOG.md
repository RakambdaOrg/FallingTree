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
