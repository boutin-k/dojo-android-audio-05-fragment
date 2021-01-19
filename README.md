# Les Fragments

## But du TD
Dans le projet MediaPlayer, nous avons l'interface qui se trouve directement intégré dans l'UI de l'activité. Imaginons que nous voulions réutiliser cette interface dans une autre application, nous serions obligé de la réécrire.
Une des solutions pour éviter cela est d'utiliser un [Fragment](https://developer.android.com/training/basics/fragments/creating) dans lequel nous pouvons déporter le code et le design.

* Tu vas devoir déplacer les boutons PLAY, PAUSE, STOP ainsi que la SeekBar dans un fragment.
* Tu peux reprendre ton MediaPlayer ou alors tu peux récupérer une version [ici](https://github.com/WildCodeSchool/dojo-android-audio-service).

## Etapes
### Créer un Fragment
* Tu vas créer un fragment *ControllerFragment* dans ton projet.
* Tu vas déplacer l'UI du xml de l'activité vers le xml du fragment.
* Tu vas integrer le fragment dans l'UI de l'activité de la manière suivante:
```xml
<?xml version="1.0" encoding="utf-8"?>
<android.support.design.widget.CoordinatorLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main_content"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true"
    tools:context=".MainActivity">

    <fragment
        android:name="fr.wildcodeschool.mediaplayer.ControllerFragment"
        android:id="@+id/controller_fragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</android.support.design.widget.CoordinatorLayout>
```

### Déplacer le code de l'activité vers le fragment
* Tu vas déporter le code de la SeekBar, Handler, Runnable dans le fragment.
* Tu vas déporter l'appel aux actions PLAY, PAUSE, STOP du player dans le fragment.
* Tu vas déporter l'implementation de l'interface ```WildOnPlayerListener``` dans le fragment.

## Documentation
* [Create a fragment](https://developer.android.com/training/basics/fragments/creating)
* [fragments](https://developer.android.com/guide/components/fragments)

## TD suivant
* [OBB](https://github.com/boutin-k/dojo-android-audio-06-obb)
