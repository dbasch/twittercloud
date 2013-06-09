# twittercloud

Generate word clouds from Twitter search

## Usage

1) edit src/twittercloud/config.sample, set your Twitter credentials, and rename it to 
sr/twittercloud/config.clj

Go [here](https://dev.twitter.com/apps/new) if you don't already have an app with credentials.

2) lein run [keyword]

3) the program will search [keyword] on Twitter repeatedly until it has retrieved 1000 results.
It will save the word cloud as [keyword].png

Example:

$ lein run soccer

Just generated

!(ScreenShot)[https://raw.github.com/dbasch/twittercloud/master/soccer.png]

Have fun!

## License

Copyright © 2013 Diego Basch

Distributed under the Eclipse Public License, the same as Clojure.
