- Remove ignoreDurabilityLoss, it wasn't working anyway since b5122b87c09e137e95402b14d2621b190bae646b as the event isn't cancelled anymore
- Fix an issue where 1 more durability is applied on the tool
- Setting damage multiplicand to 0 will have the effect of taking away 1 durability on the tool per cut (#12)
 