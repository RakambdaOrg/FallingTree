# General
You can modify the configuration through a GUI. It is located under `Main menu > Mods > FallingTree > Config`.

Another wat is through the configuration of the mod which is located under `config/falling_tree-common.toml`.
This file can be edited by a text editor and follow the TOML file format.
You can look online the syntax of that kind of file.
This is also the default format that forge uses so you should be able to find some more specific clues too.

## Values
### Boolean
Every value that is a boolean can take these two values:
* `true`: The feature is enabled
* `false`: The feature is disabled

Example: `my_value = true`

### Number
Every value that is an integer will have its range indicated above under the format: `Range: min ~ max` or `Range: > min` or `Range: < max`.

Example: `my_value = 24`

### Enumerations
Enumerations allow only a specific set of values.
Those will be indicated above under the format: `Allowed values: VALUE1, VALUE2, VALUE3`.
Only one can be picked and have to be between quotes.

Example: `my_value = "VALUE2"`

### Array
Arrays are under the format `[value1, value2, value3]` where each value can be one of the types explained above.

Example with an array of strings: `["value1", "value2", "value3"]`

### Minecraft ids
When an item or block is expected, you'll have to enter its id as a `string`.
You can obtain it in-game by pressing `CTRL + H` and hover an item in your inventory.
Its id will be displayed in gray under the name and have a format of `mod_id:item_name`.

You can also reference tags with the following format: `#mod_id:tag_name`.

## FallingTree values
### Booleans
* reverse_sneaking
* break_in_creative
* trees
  * tree_breaking
  * leaves_breaking
  * break_nether_tree_warts
* tools
  * ignore_tools
  * preserve

### Number
* trees
  * minimum_leaves_around_required
  * logs_max_count
  * leaves_breaking_force_radius
* tools
  * speed_multiplicand
  * damage_multiplicand

### Enumeration
* trees
  * break_mode

### Array
* tree
  * logs_blacklisted: Array of Minecraft ids (eg: `["minecraft:cobblestone","minecraft:iron_block", "#minecraft:logs"]`)
  * logs_whitelisted: Array of Minecraft ids (eg: `["minecraft:cobblestone","minecraft:iron_block", "#minecraft:logs"]`)
* tools
  * blacklisted: Array of Minecraft ids (eg: `["minecraft:diamond_axe","minecraft:iron_axe", "#minecraft:anvil"]`)
  * whitelisted: Array of Minecraft ids (eg: `["minecraft:diamond_axe","minecraft:iron_axe, "#minecraft:anvil""]`)