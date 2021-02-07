# SpotifyInteractions
Java app that provides additional Spotify functionality. Utilises [thelinmichael/spotify-web-api-java](https://github.com/thelinmichael/spotify-web-api-java).

## Features
- Automatic Spotify Access Token generation using Selenium browser automation
- Playlist shuffler - Spotify's shuffle seems to favour certain songs over others so this can be used to truly randomise a playlist

## Command Line Parameters
- `-help` Display command line help
- `-u <username>` Uses the input username to login to Spotify (required)
- `-p <password>` Uses the input password to login to Spotify (required)
- `-s <playlist name>` Shuffles the input playlist (assuming the logged in user can edit the playlist)
