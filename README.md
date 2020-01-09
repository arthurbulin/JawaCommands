# JawaCommands

This is a command plugin written for Minecraft Spigot using the Elastic Search database. This plugin is a constant work in progress and will constantly evolve. I DO NOT RECOMMEND using this yet. It is still an alpha plugin and is really only intended for my own server (mc.foxelbox.com)

## Getting Started

1. Clone the repo:
```
git clone https://github.com/arthurbulin/JawaCommands
```
2. Either open the project in Netbeans and build with the Maven plugin or build from the CLI. NOTE: I never build from the CLI because I'm lazy.
3. You need the Elastic Search Database installed. I test with version 7.2.0, I haven't tried older or newer versions.
4. You will need to initialize the Elastic Search indexes for the plugin. To do this you need another Java application I have written, but not yet put on GitHub. Once it is up there you can create a proper index.

### Prerequisites

You will need Maven, Elastic Search, JawaPermissions, and of course a Minecraft server running on Spigot.

### Installing

1. Place the original-JawaCommands-1.15.X.jar into the server's plugin folder and start the server. Needed configuration files will be created.
2. Edit the config.yml and include your Elastic Search database details. I DO NOT RECOMMEND turning on debug. Its gonna give you a ton of output.
3. Restart your server. If you have any malformed config files the server will let you know, although I cannot guarantee how enlightening what it tells you will be.

## Commands and Permissions

home:
description: Used for home control.
usage: /home <-[d|sr|s|i|l|h]> <homename>
permission: jawacommands.home

sethome:
description: Used to set the player's home positions
permission: jawacommands.home.set
alias: [addhome]

commandtest:
description: Jawacommands test command
permission: jawacommands

delhome:
description: Used to delete a home
permission: jawacommands.home.del
alias: [remhome]

listhome:
description: Lists a user's homes
permission: jawacommands.home.list

homeinfo:
description: Provides a user with specific home information
usage: /homeinfo <homename>
permission: jawacommands.home.info

sudo:
description: Allows an administrative user to force a regular user to run a command.
permission: jawacommands.sudo

warp:
description: Warps a player to a specified warp.
permission: warps.warp

makewarp:
description: Creates a warp.
permission: warps.admin.create

modwarp:
description: Modifies a warp.
permission: warps.admin.mod

delwarp:
description: Deletes a warp.
permission: warps.admin.delete

wlwarp:
description: Adds a user or users to the whitelist, removes them in the same process.
permission: warps.admin.wl

blwarp:
description: Adds a player to the warps blacklist
permission: warps.admin.bl

gm:
description: Changes a player's game mode.
permission: gamemode.toggle

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
permission: jawacommands.fullbright

## Built With

* [NetBeans](https://netbeans.org/) - The IDE used
* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Arthur Bulin aka Jawamaster** - [Arthur Bulin](https://github.com/arthurbulin)

## License

This project is licensed under the MIT License. Just don't be a jerk about it.

