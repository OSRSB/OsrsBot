# RSB
A runelite extension bot api

A bot api to automate the game Old School Runescape (OSRS) using the RuneLite third party client. 
RuneLite is an open-source third party client and despite their claims to take steps to prevent
or reduce the ease of botting via removing the deobfuscator, one can still make a bot with their 
api with some effort. 

While certainly it would be easier to create one using a deobfuscator and their api
it reduces the amount of effort needed to maintain our code by depending on their large number
of contributers to keep it up to date and for this project to focus on its own goal.

To run you'll need to modify your program arguments to the following depending on how you wish it to run:

--bot-runelite
This runs the bot, it uses the runelite interface

--runelite
This runs the bot in runelite stock form. It has no additions and behaves normally.

--ea
This enables assertations, it should never be used with bot mode.
It does need to be enabled for base runelite to use --developer-mode

--developer-mode
Enables the developer features in runelite stock and runelite bot modes. 

VM args:
-debug
Activates debug log messages


After entering the program arguements you wish to enable (one mode only --runelite/--bot-runelite)
it is ready to run.

To make and test scripts look at the corresponding wiki pages.