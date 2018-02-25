# Telegram Bot exceldb-telegram-bot
[![Build Status](https://travis-ci.org/cedricwalter/exceldb-telegram-bot.svg?branch=master)](https://travis-ci.org/cedricwalter/exceldb-telegram-bot)

Telegram bot you can ask if something is existing in an excel database, you can also add stuff in excel


# Features
* Can check in an excel for some value and return all lines matching
* Can append a new line in an Excel
* Can return all unique values of any column

## help

ask the bot for help with /help
```
Help
These are the registered commands for this Bot:

/help
Get all the commands this bot provides

/getsubcat
get all startup sub-categories

/getcat
get all startup categories

/addentry
Add entry new startup to database

/hello
Say hallo to this bot

/hasentry
Check if database contains company name or company url already, if found returns it
```

# Installation

Create a new bot using https://telegram.me/BotFather and /newbot

if it goes well you will get in return

```
Done! Congratulations on your new bot. You will find it at t.me/xx-name-of-bot-xx. 

You can now add a description, about section and profile picture for your bot, see /help for a list of commands.
By the way, when you've finished creating your cool bot, ping our Bot Support if you want a better username for it. 
Just make sure the bot is fully operational before you do this.

Use this token to access the HTTP API:
xxxxxxxxxx:xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

For a description of the Bot API, see this page: https://core.telegram.org/bots/api
```

Copy the config.properties in classpath and configure

```
token=xxxxxxxxxx:xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
user=xx-name-of-bot-xx
whitelisted=name of @username allowed to use the bot
excel=full path to excel file
```

Execute maven package and run like this 
```
java -jar exceldb-bot-1.0-SNAPSHOT-jar-with-dependencies.jar
```

# Resources
https://core.telegram.org/bots