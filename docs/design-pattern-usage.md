# Publish-subscribe pattern
The main window holds a list of favourite stations.
Stations can be favourited or unfavourited from the detail window.
Hence, it is necessary for the detail window to notify the main 
window of a change in favourites without tightly coupling the two.

To this end, the `MainWindow` instance `window` is passed to the 
`DetailWindow` instance `details` as a listener, subscribing to 
notifications whenever the favourites are changed:

    details.setFavouritesListener(window)

To facilitate loose coupling, `setFavouritesListener` accepts a single 
argument of a type that must implement `FavouritesUpdatedListener`,
which is implemented by `MainWindow`. 
 

