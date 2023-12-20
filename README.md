# JawaCommands

JawaCommands provides a number of commands that apply to user actions. It is not a user administration plugin (like JawaPermissions) but it does provide some administrative tools as well as QoL commands for staff and users.

## Getting Started

1. Clone the repo:
```
git clone https://github.com/arthurbulin/JawaCommands
```
2. Either open the project in Netbeans and build with the Maven plugin or build from the CLI. NOTE: I never build from the CLI because I'm lazy.

## Prerequisites

You will need 
    - Maven to build the plugin
    - Java 17
    - ElasticSearch (7.15.0 is the current version)
    - Minecraft server running on Paper/Spigot (Paper is prefered and I will probably not support Spigot by the end of 1.20 support)

## Installing

1. Place the JawaCommands-1.20.X-#.#.jar into the server's plugin folder and start the server. Needed configuration files will be created.
2. Shutdown your server
2. Edit the config.yml. See the Configuration Paramaters section below.
4. Restart your server. If you have any malformed config files the server will let you know, although I cannot guarantee how enlightening what it tells you will be.

### Configuration Parameters
* homes-settings:
  * homes-prohibited-worlds:
    \- \<world name\>
        - This is a list of world names in which a user is NOT allowed to create homes in
    * homes-enabled: \<true/false\>
        - If homes are enabled or not. False actually disables command registration.
    * homes-limits:
        \<rank name\>: \<integer\>
            - This is the number of homes a specific rank is allowed to set
* teleport-settings:
    * safe-tp-enabled: \<true/false\>
        - Safe teleport enable/disable. Safe teleport fuzzes the tp location a little, as well as freezes and god-modes the player to protect them from chunk load lag
    * safe-tp-delay: \<integer\>
        - This is the tp delay for safe tp. It is how long the user is frozen for protection
    * safe-tp-messages: \<true/false\>
        - The message to be sent on a safetp event
* freeze-public-messages: \<true/false\>
    - If a staff member freezes someone, if the message is broadcast by default.
* messages:
  * safe-tp-freeze: \<arbitrary string\>
    - Player message for a safe TP event
  * safe-tp-thaw: \<arbitrary string\>
    - Player message for thaw event
  * freeze-public-freeze: \<arbitrary string\>
    - Public broadcast when frozen, accepts {p} as a replacement for the player name
  * freeze-public-thaw: \<arbitrary string\>
    - Public broadcast when thawed, accepts {p} as a replacement for the player name
  * home-no-addpermission: \<arbitrary string\>
    - Player message when they don't have permission to add a home in that world
  * home-add: \<arbitrary string\>
    - Message sent to the player when a home is created. Accepts {h} as a replacement for the homename
  * home-del: \<arbitrary string\>
    - Message sent to the player when a home is deleted. Accepts {h} as a replacement for the homename
  * home-tp: \<arbitrary string\>
    - Message sent to the player when tp'd to a home. Accepts {h} as a replacement for the homename
* index-customization:
  * warps: \<arbitrary string\>
    - This is the index name that will be used by the plugins to store warp data. I recommend naming it warps-<minecraft version> i.e. warps-120. So that in the future you can keep database versions seperate if I change the schema. Although if I do I'll try to ensure the update is automatic but up to this point, it is NOT.
  * homes: \<arbitrary string\>
    - This is the index name that will be used by the plugins to store homes data. I recommend naming it homes-<minecraft version> i.e. homes-120. So that in the future you can keep database versions seperate if I change the schema. Although if I do I'll try to ensure the update is automatic but up to this point, it is NOT.
* warp-settings:
  * warps-enabled: \<true/false\>
    - If the warp functionality is enabled or not. False prevents registration of the commands.

## Commands and Permissions
### Commands and root permission nodes
These are the commands available within JawaCommands, their descriptions, and relevent command usage permissions nodes.
home:
description: Used for home control.
usage: '/home <[d|sr|s|i|l|h]> <homename>'
permission: jawacommands.home

homeinfo:
description: Provides a user with specific home information
usage: /homeinfo <homename>
permission: jawacommands.home.info

sudo:
description: Allows an administrative user to force a regular user to run a command.
permission: jawacommands.sudo

warp:
description: Warps a player to a specified warp.
usage: '/warp <warp> [other|@p|@a] [override]'
permission: jawacommands.warps.warp

makewarp:
description: 'Creates a warp.'
permission: jawacommands.warps.create

modwarp:
description: Modifies a warp.
usage: '/modwarp <warpname> [whitelist|location|hidden|owner|type] <[owner|type]>'
permission: jawacommands.warps.modify

warpinfo:
description: Displayer relevent warp information.
usage: '/warpinfo <warpname>'
permission: jawacommands.warps.info

delwarp:
description: Deletes a warp.
permission: jawacommands.warps.delete

wlwarp:
usage: '/wlwarp <warp name> <add|remove> <player name|selector>'
description: Add/remove a user or users to the whitelist

blwarp:
description: Adds a player to the warps blacklist

gm:
description: Changes a player's game mode.
permission: jawacommands.gamemode.toggle

spawn:
description: Send a player to the corresponding world's spawn
permission: jawacommands.spawn

setspawn:
description: Sets the spawn location of a world.
permission: jawacommands.setspawn

gothere:
description: Asks a player if you can teleport to them
permission: jawacommands.gothere

comehere:
description: Asks a player to teleport to you
permission: jawacommands.comehere

accept:
description: Accepts a teleport request
permission: jawacommands.accept

fullbright:
description: Gives you nightvision
permission: jawacommands.playeraugmentation.fullbright

removespawn:
description: Removes a custom spawn
permission: jawacommands.spawn.remove

kit:
description: 'Used to create, modify, and give kits'
permission: jawacommands.kit.user
usage: 'Run /kit for full instructions'

givekit:
description: 'Used to give a kit to a player by staff, commandblock, or console'
permission: jawacommands.kit.other
usage: 'Run /givekit for full instructions'

yeetport:
description: 'Yeets a player into the air before teleporting to a warp'
permission: jawacommands.yeetport

back:
description: 'sends you back to the last teleport location'
permission: jawacommands.back

colors:
description: 'sends a message to the player containing all of the color names and char codes as they appear in the code'
usage: '/colors'

testcommand:
description: Does whatever whenever i use it
usage: test usage

tpr:
description: Randomly teleports a target
usage: '/tpr [target]'

freeze:
description: "Freezes a player's motion preventing them from moving"
usage: "/freeze <player>"
permission: jawacommands.freeze

pweather:
description: "Allows a player to toggle their local player weather"
usage: "/pweather [clear|rain|reset] [player]. Run without arguments to reset your own weather."
permission: "jawacommands.playeraugmentation.pweather"

ptime:
description: "Allows a player to toggle their local player time"
usage: "/ptime [morning|noon|sunset|night|midnight|sunrise|reset] [player]. Run without arguments to reset your own weather."
permission: "jawacommands.playeraugmentation.ptime"

sfly:
description: "Allows a survival player to enable flight for themselves"
usage: "/sfly"
permission: "jawacommands.playeraugmentation.sfly"

flyspeed:
description: Sets a player's flying speed
usage: "/flyspeed <[1|2|3|4|6|8|10]>"
permission: "jawacommands.playeraugmentation.flyspeed"

walkspeed:
description: Sets a player's walk speed
usage: "/walkspeed <[1|2|3|4|5|6|7|8|9|10]>"
permission: "jawacommands.playeraugmentation.walkspeed"

otherhome:
description: View another player's home data
usage: "/otherhome <player name> <[info|homename]> <[homename]>"
permission: "jawacommands.home.other"

repair:
description: Repair the item held in hand
usage: "/repair"
permission: "jawacommands.items.repair"

bypasssafetp:
description: Toggle safetp bypassing
usage: "/bypasssafetp"
permission: "jawacommands.teleport.bypass"

### Permissions
Some of these permissions are root permissions (which entitle a user to run a command), but some are additional features of a command.

jawacommands.back:
description: Allows back command
default: true

jawacommands.yeetport:
description: Allows a player to trugger a yeetport
default: op

jawacommands.kit.user:
description: Allows a user to use the kit command
default: false

jawacommands.kit.admin:
description: Allows admin access to the kit command
default: op

jawacommands.kit.other:
description: Allows admin access to the givekit command
default: op

jawacommands.spawn.remove:
description: Allows a user to remove a spawn
default: false

jawacommands.playeraugmentation.fullbright:
description: Allows a user to use fullbright
default: true

jawacommands.accept:
description: Allows a user to accept tp requests
default: true

jawacommands.comehere:
description: Allows a user to ask a player to teleport to them
default: true

jawacommands.gothere:
description: Allows a user to ask a player if they can teleport to them
default: true

jawacommands.setspawn:
description: Allows an admin to create a specific spawn
default: true

jawacommands.spawn:
description: Allows a user to go to spawn
default: true

jawacommands.gamemode.toggle:
description: Allows a user to toggle between gamemodes
default: true

jawacommands.gamemode.admin.other:
description: Allows an admin to change the gamemode of another player
default: op

jawacommands.warps.blacklist:
description: Allows an admin to add a black list to a warp
default: op

jawacommands.warps.whitelist:
description: Allows an admin to add a white list to a warp
default: op

jawacommands.warps.delete:
description: Allows an admin to delete a warp
default: op

jawacommands.warps.modify:
description: Allows an admin to add modify a warp
default: op

jawacommands.warps.create:
description: Allows an admin to create a warp
default: op

jawacommands.warps.warp:
description: Allows a player to list warps they can see and visit them
default: true

jawacommands.warps.info:
description: Allows a player to see information regarding a warp
default: true

jawacommands.sudo:
description: Allows an admin to force a player to run a command
default: op

jawacommands.home.info:
description: Allows an player to see their home info
default: true

jawacommands.home.add:
description: Allows an player to add a home
default: true

jawacommands.home.list:
description: Allows an player to list their homes
default: true

jawacommands.home.del:
description: Allows an player to delete a home
default: true

jawacommands.home.set:
description: Allows an player to set homes
default: true

jawacommands.home:
description: Allows an player to access the generalized home command
default: true

jawacommands.freeze:
description: Allows an admin to freeze or thaw a player with the freeze command
default: op

jawacommands.playeraugmentation.pweather:
description: "Allows a user to change their local player weather"
default: true

jawacommands.playeraugmentation.pweather.other: 
description: "Allows an admin to control the weather of another user"
default: op

jawacommands.playeraugmentation.ptime:
description: "Allows a user to change their local player time"
default: true

jawacommands.playeraugmentation.ptime.other:
description: "Allows an admin to control the time of another user"
default: op

jawacommands.playeraugmentation.sfly:
description: "Allows a player to enable flight for themselves"
default: op

jawacommands.playeraugmentation.flyspeed:
description: "Allows a player to set their fly speed"
default: op

jawacommands.playeraugmentation.walkspeed:
description: "Allows a player to set their walk speed"
default: op

jawacommands.home.other:
description: "Allows staff to view/tp to another player's homes"
default: op

jawacommands.items.repair:
description: "Allows a user to repair the item in hand"
default: op

jawacommands.teleport.bypass:
description: "Allows a user to bypass the safe tp system"
default: true

## Built With

* [NetBeans](https://netbeans.org/) - The IDE used
* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Arthur Bulin aka Jawamaster** - [Arthur Bulin](https://github.com/arthurbulin)

## License

This project is licensed under the MIT License. Just don't be a jerk about it.

